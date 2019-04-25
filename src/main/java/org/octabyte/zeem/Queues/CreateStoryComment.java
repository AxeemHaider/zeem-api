package org.octabyte.zeem.Queues;

import com.googlecode.objectify.Key;
import org.octabyte.zeem.API.Helper.Datastore;
import org.octabyte.zeem.Datastore.Story;
import org.octabyte.zeem.Datastore.StoryComment;
import org.octabyte.zeem.Helper.DataType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.octabyte.zeem.Helper.OfyService.ofy;

@SuppressWarnings("serial")
@WebServlet(name = "CreateStoryComment", description = "Create Comment for Story",
        urlPatterns = "/queue/creating_story_comment")
public class CreateStoryComment extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long userId = Long.valueOf(req.getParameter("userId"));
        String commentSafeKey = req.getParameter("commentSafeKey");
        String storySafeKey = req.getParameter("storySafeKey");

        // Create comment key from comment safe key
        Key commentKey = Key.create(commentSafeKey);

        // Get Comment object from commentId
        StoryComment comment = (StoryComment) ofy().load().key(commentKey).now();

        // Get Post and update recent comment list
        Story story = (Story) ofy().load().key(Key.create(storySafeKey)).now();



        // Inform all user who already comment on this post and the owner of post that new comment is added
        // Get all comments on this Post
        List<StoryComment> commentList = ofy().load().type(StoryComment.class).ancestor(Key.create(Story.class, story.getStoryId())).list();

        // Make it unique list
        commentList = commentList.stream().filter(Datastore.distinctByKey(StoryComment::getUserId)).collect(Collectors.toList());

        // Check post mode is private or public
        if(story.getMode() == DataType.Mode.PRIVATE) { // Story is private

            // Check owner is not commenting his own post
            if (!Objects.equals(userId, story.getUserKey().getId())) { // Owner is not commenting
                // Send notification to owner of post
                Datastore.sendPrivateNotification(story.getUserKey().getId(), userId, DataType.NotificationType.STORY_COMMENT, storySafeKey, comment.getAnonymous());
            }

            // Loop each comment to get user
            for (StoryComment eachComment : commentList) {

                // Don't send notification to creater of comment
                if(!Objects.equals(userId, eachComment.getUserRef().get().getUserId())
                        && !Objects.equals(story.getUserKey().getId(), eachComment.getUserRef().get().getUserId())) { // This is not creater of comment
                    // Get userId from eachComment and send notification that new comment is added
                    Datastore.sendPrivateNotification(
                            eachComment.getUserRef().get().getUserId(),
                            userId,
                            DataType.NotificationType.STORY_COMMENT,
                            storySafeKey, comment.getAnonymous());
                } // end If

            }

        }else { // Post is public

            // Loop each comment to get user
            for (StoryComment eachComment : commentList) {

                // Get userId from eachComment and send notification that new comment is added
                Datastore.sendPublicNotification(
                        eachComment.getUserRef().get().getUserId(),
                        userId,
                        DataType.NotificationType.STORY_COMMENT,
                        storySafeKey, comment.getAnonymous());

            }

        } // End if-else

        // Check if comment is tag to someone inform him
        try {
            if(comment.getTaggedUser() != null) { // Someone is tagged with this comment
                CreateComment.tagInformer(userId, comment.getTaggedUser(), story.getMode(), storySafeKey, comment.getAnonymous());
            }
        } catch (Exception e) {
            // It return NullPointerException when no user is tagged
            e.printStackTrace();
        }


    }

}
