package org.octabyte.zeem.Queues;

import com.googlecode.objectify.Key;
import org.octabyte.zeem.API.Helper.UserHelper;
import org.octabyte.zeem.Datastore.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.octabyte.zeem.Helper.OfyService.ofy;

@SuppressWarnings("serial")
@WebServlet(name = "FollowerFeed", description = "Create Follower Feed when Post is Public",
        urlPatterns = "/queue/follower_feed")
public class FollowerFeed extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Getting value from URL
        Long userId = Long.valueOf(req.getParameter("userId"));
        Long postId = Long.valueOf(req.getParameter("postId"));
        String postSafeKey = req.getParameter("postSafeKey");
        Boolean startedByTag = Boolean.valueOf(req.getParameter("startedByTag"));

        Long ownerId = null;
        try {
            ownerId = Long.valueOf(req.getParameter("ownerId"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create postKey from post safe string
        Key postKey = Key.create(postSafeKey);

        // Get list of followers
        List<Follower> followerList = UserHelper.getFollowerList(userId);

        // Get only followers ids from follower list
        Long[] followerIds = followerList.stream().map(Follower::getFollowerId).toArray(Long[]::new);

        // Check queue is started by tag or not if started by tag
        // then remove users who are tagged with this post and also remove owner

        if (startedByTag) {

            // Get tagged user from this post
            List<PostTag> postTags = ofy().load().type(PostTag.class).ancestor(Key.create(Post.class, postId)).list();

            // Get only user ids from this object
            Long[] taggedUserIds = postTags.stream().map(PostTag::getUserId).toArray(Long[]::new);

            // Remove these users from follower ids
            List<Long> l1 = new ArrayList<>(Arrays.asList(followerIds));
            List<Long> l2 = new ArrayList<>(Arrays.asList(taggedUserIds));
            l1.removeAll(l2);

            // Now get the follower of owner
            List<Follower> ownerFollowers = UserHelper.getFollowerList(ownerId);

            // Get only follower ids from owner's follower list
            Long[] ownerFollowerIds = ownerFollowers.stream().map(Follower::getFollowerId).toArray(Long[]::new);

            // Convert owner follower ids into list
            List<Long> ownerFollowerList = new ArrayList<>(Arrays.asList(ownerFollowerIds));

            // remove also these followers from list
            l1.removeAll(ownerFollowerList);

            // Also remove owner from follower ids
            l1.remove(ownerId);

            followerIds = l1.toArray(new Long[0]);

        } //

        // Get list of follower and loop them
        for ( Long followerId : followerIds ) {

            // Save post into public feed for followers
            CreateFeed.generatePublicFeed(followerId, postKey);

        } // end for loop

    }
}
