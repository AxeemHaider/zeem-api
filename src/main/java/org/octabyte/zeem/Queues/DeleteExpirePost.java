package org.octabyte.zeem.Queues;

import com.googlecode.objectify.Key;
import org.octabyte.zeem.Datastore.ExpirePost;
import org.octabyte.zeem.Datastore.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.octabyte.zeem.Helper.OfyService.ofy;

@SuppressWarnings("serial")
@WebServlet(name = "DeleteExpirePost", description = "Delete those posts that are expire",
        urlPatterns = "/queue/delete_expire_post")
public class DeleteExpirePost extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get current time
        Long currentTime = System.currentTimeMillis();

        // Create a list to Hold post keys
        List<Key<Post>> postKeys = new ArrayList<>();

        // Get All Posts those expire time is less then current time
        List<ExpirePost> expirePosts = ofy().load().type(ExpirePost.class).filter("expireOn <", currentTime).list();

        // Loop each expire post and get post key
        for (ExpirePost expirePost : expirePosts) {

            // Create post key and add into list
            postKeys.add( Key.create(expirePost.getPostSafeKey()) );

        } // end loop

        // Check if there are some expire posts then delete them
        if (postKeys.size() > 0) { // There are some expire posts

            // Delete them
            ofy().delete().keys(postKeys); // Async Task

        }

    }
}
