package org.octabyte.zeem.Queues;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import org.octabyte.zeem.API.Helper.UserHelper;
import org.octabyte.zeem.Datastore.*;
import org.octabyte.zeem.Helper.DataType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.octabyte.zeem.Helper.OfyService.ofy;

@SuppressWarnings("serial")
@WebServlet(name = "CreateStory", description = "Inform Friends and Followers about this Story",
        urlPatterns = "/queue/creating_story")
public class CreateStory extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Getting value from URL (userId, postId)
        Long userId = Long.valueOf(req.getParameter("userId"));
        Long storyId = Long.valueOf(req.getParameter("storyId"));
        Boolean isPublic = !req.getParameter("isPublic").equals(String.valueOf(DataType.Mode.PRIVATE));

        // Create Story Ref
        Ref<Story> storyRef = Ref.create(Key.create(Key.create(User.class, userId), Story.class, storyId));

        // Check story is public or private
        if (isPublic) { // It's a public story

            // Add this story into my public stories feed
            PublicStories myPublicStory = new PublicStories(Key.create(User.class, userId), storyRef);
            ofy().save().entity(myPublicStory); // Async Task

            // Inform all Friends about this
            generatePrivateStories(userId, storyRef);

            // Inform all Followers about this
            for (Follower follower : UserHelper.getFollowerList(userId)) {

                // Create Public stories
                PublicStories publicStory = new PublicStories(Key.create(User.class, follower.getFollowerId()), storyRef);
                ofy().save().entity(publicStory); // Async Task

            } // End for loop

        } else { // It's a private story

            // Add this story into my private stories feed
            PrivateStories myPrivateStory = new PrivateStories(Key.create(User.class, userId), storyRef);
            ofy().save().entity(myPrivateStory); // Async Task

            // Inform only friends about this
            generatePrivateStories(userId, storyRef);

        } // end if-else loop

    } // end do post


    /**
     * Inform all Friends about this story
     * @param userId    How is created this story. Fetch all friends of this user
     * @param storyRef  Reference of the story
     */
    private void generatePrivateStories(Long userId, Ref<Story> storyRef){
        // Inform all Friends about this
        for (Friend friend : UserHelper.getFriendList(userId)) {

            // Create Private Stories
            Key friendKey = Key.create(User.class, friend.getFriendId());
            PrivateStories privateStory = new PrivateStories(friendKey, storyRef);
            ofy().save().entity(privateStory); // Async Task


        } // end for loop
    }

}
