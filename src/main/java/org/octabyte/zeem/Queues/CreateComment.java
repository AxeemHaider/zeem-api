package org.octabyte.zeem.Queues;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Ref;
import org.octabyte.zeem.API.Helper.Datastore;
import org.octabyte.zeem.Datastore.Comment;
import org.octabyte.zeem.Datastore.Post;
import org.octabyte.zeem.Helper.DataType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.octabyte.zeem.Helper.OfyService.ofy;

@SuppressWarnings("serial")
@WebServlet(name = "CreateComment", description = "Create Comment for Post",
        urlPatterns = "/queue/creating_comment")
public class CreateComment extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long userId = Long.valueOf(req.getParameter("userId"));
        String commentSafeKey = req.getParameter("commentSafeKey");
        String postSafeKey = req.getParameter("postSafeKey");

        // Create comment key from comment safe key
        Key commentKey = Key.create(commentSafeKey);

        // Get Comment object from commentKey
        Comment comment = (Comment) ofy().load().key(commentKey).now();
        Post post = null;

        try {
            // Get Post and update recent comment list
            post = (Post) ofy().load().key(Key.create(postSafeKey)).safe();
        } catch (NotFoundException e) {
            e.printStackTrace();

            return;
        }

        // Only add text comments in recent comment list
        // Check it a TEXT comment or not
        if (comment.getType() == DataType.CommentType.TEXT) { // It's a TEXT comment
            List<Ref<Comment>> recentComment = new ArrayList<>();
            // Check there is already some comment or not
            if (post.getRecentComment() != null) { // There is some comment
                // Get recent list of comments
                recentComment = post.getRecentComment();
            }

            // Add commentInstance into recent comment list
            recentComment.add(0, Ref.create(commentKey));

            // If list length is greater than 3 remove the 4th element from list
            if (recentComment.size() > 3) // List length is greater than 3
                recentComment.remove(3);

            // Update recent comment list in post
            post.setRecentComment(recentComment);

        }

        // increment in totalComments
        post.setTotalComments(post.getTotalComments() + 1);

        // Update Post with totalComments and with recent comment if any - Async
        ofy().save().entity(post);

        // Inform all user who already comment on this post and the owner of post that new comment is added
        // Get all comments on this Post
        List<Comment> commentList = ofy().load().type(Comment.class).ancestor(Key.create(Post.class, post.getPostId())).list();

        // Make it unique list
        commentList = commentList.stream().filter(Datastore.distinctByKey(Comment::getUserId)).collect(Collectors.toList());

        // Check post mode is private or public
        if(post.getMode() == DataType.Mode.PRIVATE) { // Post is private

            // Check owner is not commenting his own post
            if (!Objects.equals(userId, post.getUserKey().getId())) { // Owner is not commenting
                // Send notification to owner of post
                Datastore.sendPrivateNotification(post.getUserKey().getId(), userId, DataType.NotificationType.POST_COMMENT, postSafeKey, comment.getAnonymous());
            }

            // Loop each comment to get user
            for (Comment eachComment : commentList) {

                // Don't send notification to creater of comment
                if(!Objects.equals(userId, eachComment.getUserRef().get().getUserId())
                        && !Objects.equals(post.getUserKey().getId(), eachComment.getUserRef().get().getUserId())) { // This is not creater of comment
                    // Get userId from eachComment and send notification that new comment is added
                    Datastore.sendPrivateNotification(
                            eachComment.getUserRef().get().getUserId(),
                            userId,
                            DataType.NotificationType.POST_COMMENT,
                            postSafeKey, comment.getAnonymous());
                } // end If

            }

        }else { // Post is public

            // Loop each comment to get user
            for (Comment eachComment : commentList) {

                // Get userId from eachComment and send notification that new comment is added
                Datastore.sendPublicNotification(
                        eachComment.getUserRef().get().getUserId(),
                        userId,
                        DataType.NotificationType.POST_COMMENT,
                        postSafeKey, comment.getAnonymous());

            }

        } // End if-else

        // Check if comment is tag to someone inform him
        if(comment.getTaggedUser() != null) { // Someone is tagged with this comment
             tagInformer(userId, comment.getTaggedUser(), post.getMode(), postSafeKey, comment.getAnonymous());
        }

    }

    /**
     * Inform all users who is tagged with this comment
     * @param userId        Id of user who is comment
     * @param taggedUsers   List of user which are tagged with this comment
     * @param mode          Comment is on public or private post/story
     * @param safeKey       Safe Key of Post or Story
     * @param anonymous     Anonymous comment or not
     */
    public static void tagInformer(Long userId, List<Long> taggedUsers, DataType.Mode mode, String safeKey, boolean anonymous){
        // Check post is private or public
        if(mode == DataType.Mode.PRIVATE) { // Post is Private
            // Loop each tagged user and send notification
            for( Long taggedUser : taggedUsers ) {
                Datastore.sendPrivateNotification(taggedUser, userId, DataType.NotificationType.COMMENT_MENTION, safeKey, anonymous);
            }
        }else{
            // Loop each tagged user and send notification
            for( Long taggedUser : taggedUsers ) {
                Datastore.sendPublicNotification(taggedUser, userId, DataType.NotificationType.COMMENT_MENTION, safeKey, anonymous);
            }
        }// End if-else
    }

}
