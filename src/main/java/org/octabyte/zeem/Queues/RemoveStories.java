package org.octabyte.zeem.Queues;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import org.octabyte.zeem.Datastore.*;
import org.octabyte.zeem.Helper.DataType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.octabyte.zeem.Helper.OfyService.ofy;

@SuppressWarnings("serial")
@WebServlet(name = "RemoveStories", description = "Cron job remove stories after 24 hours",
        urlPatterns = "/queue/remove_stories")
public class RemoveStories extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Create list to hold batch save
        List<Story> stories = new ArrayList<>();

        // Create list to hold batch Delete
        List<Story> deleteStories = new ArrayList<>();

        // Create list to hold batch delete for StoryComment
        List<StoryComment> deleteStoryComments = new ArrayList<>();

        // Create list to hold Private Story Feed Ref. for batch delete
        List<Ref<Story>> privateStoryRefs = new ArrayList<>();

        // Create list to hold Public Story Feed Ref. for batch delete
        List<Ref<Story>> publicStoryRefs = new ArrayList<>();

        long aDayAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000);

        // Get all stories which are upload 24 hours before on this Time
        List<Story> storyList = ofy().load().type(Story.class).filter("updatedOn <", aDayAgo).list();

        // check some stories are found or not
        if (storyList.size() > 0){ // Some stories are found

            // Loop each story
            for (Story story : storyList){

                // Now find the inner stories
                List<Stories> innerStories = story.getStories();

                // Story index is used to get posted on date from next story
                int storyIndex = 0;
                // Hold inner story length
                int innerStoryLen = innerStories.size();

                // Hold list to remove inner stories from ArrayList
                List<Stories> removeInnerStories = new ArrayList<>();

                // Loop each inner story
                for (Stories innerStory : innerStories) {
                    storyIndex++;

                    // Check if this is 24 hours old delete this
                    if (innerStory.getPostedOn() < aDayAgo) { // This story is 24 hour late

                        // Check story index is less than story length
                        if (storyIndex < innerStoryLen){ // Yes it is less then, Now update story date
                            // Get the next story posted on time And set it into Story
                            story.setUpdatedOn(innerStories.get(storyIndex).getPostedOn());
                        } // end inner if

                        removeInnerStories.add(innerStory);
                        storyIndex--; // Remove one index
                        innerStoryLen--; // Remove one length

                        // Remove story comment
                        // Get all story comment which are child of this story
                        StoryComment storyComment = ofy().load().type(StoryComment.class).ancestor(Key.create(Story.class, story.getStoryId()))
                                .filter("storyNum", innerStories.get(storyIndex).getStoryNum()).first().now();

                        // Check comment is available for this story or not
                        if(storyComment != null) { // There are some comments
                            // Add this story comment in delete list for batch delete
                            deleteStoryComments.add(storyComment);
                        } // end inner if

                    } // end if

                } // end inner loop

                // Remove only when there is some innerStories in the list
                if(removeInnerStories.size() > 0) {
                    innerStories.removeAll(removeInnerStories); // Remove inner story from list
                } // end if

                // If there is no inner story left remove the Story
                if (innerStoryLen == 0){ // There is no story left
                    deleteStories.add(story);

                    if(story.getMode() == DataType.Mode.PRIVATE) { // Story private mode
                        privateStoryRefs.add(Ref.create(Key.create(story.getWebSafeKey())));
                    }else{ // Public mode
                        publicStoryRefs.add(Ref.create(Key.create(story.getWebSafeKey())));
                    } // end inner if-else

                }else {
                    story.setStories(innerStories);
                    stories.add(story);
                }

                // Delete Private Story Feed if batch delete is more than 50
                if (privateStoryRefs.size() > 50){
                    // Get Story Feeds
                    List<PrivateStories> privateStories = ofy().load().type(PrivateStories.class)
                                                    .filter("storyRef in", privateStoryRefs).list();
                    // Delete all these story feeds
                    ofy().delete().entities(privateStories); // Async Task

                    // clear list
                    privateStoryRefs.clear();
                } // end if

                // Delete Public Story Feed if batch delete is more than 50
                if (publicStoryRefs.size() > 50){
                    // Get Story Feeds
                    List<PublicStories> publicStories = ofy().load().type(PublicStories.class)
                            .filter("storyRef in", publicStoryRefs).list();
                    // Delete all these story feeds
                    ofy().delete().entities(publicStories); // Async Task

                    // clear list
                    publicStoryRefs.clear();
                } // end if

                // Delete stories if batch delete is more than 500
                if (deleteStories.size() > 500){
                    ofy().delete().entities(deleteStories); // Async Task
                    // clear stories list
                    deleteStories.clear();
                } // end if

                // Save stories if batch save is more than 500
                if (stories.size() > 500){
                    ofy().save().entities(stories); // Async Task
                    // clear stories list
                    stories.clear();
                } // end if

                // Delete story comment if batch delete is more than 500
                if (deleteStoryComments.size() > 500){
                    ofy().delete().entities(deleteStoryComments); // Async Task
                    // clear story comment list
                    deleteStoryComments.clear();
                } // end if

            } // end loop

            // Remove and save every remaining lists that are less then Batch save 500

            if(deleteStories.size() > 0)
                ofy().delete().entities(deleteStories); // Async Task : Delete Stories

            if(deleteStoryComments.size() > 0)
                ofy().delete().entities(deleteStoryComments); // Async Task : Delete Story Comments

            if (stories.size() > 0)
                ofy().save().entities(stories); // Async Task : Update Stories

            if(privateStoryRefs.size() > 0) {
                List<PrivateStories> privateStories = ofy().load().type(PrivateStories.class)
                        .filter("storyRef in", privateStoryRefs).list();
                // Delete all these story feeds
                ofy().delete().entities(privateStories); // Async Task
            }

            if(publicStoryRefs.size() > 0) {
                // Get Story Feeds
                List<PublicStories> publicStories = ofy().load().type(PublicStories.class)
                        .filter("storyRef in", publicStoryRefs).list();
                // Delete all these story feeds
                ofy().delete().entities(publicStories); // Async Task
            }


        } // end if

    }

}
