package org.octabyte.zeem.Queues;

import com.googlecode.objectify.Key;
import org.octabyte.zeem.API.Helper.Datastore;
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
import java.util.stream.Stream;

import static org.octabyte.zeem.Helper.OfyService.ofy;

@SuppressWarnings("serial")
@WebServlet(name = "Suggestion", description = "Create suggestion for user",
        urlPatterns = "/queue/suggestion_queue")
public class Suggestion extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // ##############################################
        // ### This Queue is only Called once per Day ###
        // ##############################################

        // Get userId from request
        Long userId = Long.valueOf(req.getParameter("userId"));

        // Get friend list
        List<Friend> friendList = UserHelper.getFriendList(userId);
        // Get only users id from friendList
        Long[] friendUserIds = friendList.stream().map(Friend::getFriendId).toArray(Long[]::new);

        // Get Follower list but no more than 1000
        List<Follower> followerList = UserHelper.getFollowerList(userId, 1000);

        // Get My Following list
        List<Following> followingList = UserHelper.getFollowingList(userId);

        // Remove any Follower if I following it
        followerList.removeAll(followingList);

        // Get only users ids from followerList
        Long[] followerUserIds = followerList.stream().map(Follower::getFollowerId).toArray(Long[]::new);

        // Merge these two lists
        Long[] userIds = Stream.concat(Arrays.stream(friendUserIds), Arrays.stream(followerUserIds)).toArray(Long[]::new);

        // Check there are some users or not
        if(userIds.length <= 0){ // There are no users end this function
            return;
        }

        // Get those posts who are liked by these userIds
        List<PostStar> postStars = ofy().load().type(PostStar.class)
                .filter("userId in", userIds).limit(300).list();

        // Check there are some post stars or not
        if (postStars.size() <= 0){ // No post finding return this function
            return;
        }

        // Get posts where I liked
        List<PostStar> myPosts = new ArrayList<>();
        for (PostStar postStar : postStars){
            if (Key.create(postStar.getPostSafeKey()).getParent().getId() == userId){
                myPosts.add(postStar);
            }
        }
        // remove these posts from list
        postStars.removeAll(myPosts);

        // Get only postIds from this
        String[] postIds = postStars.stream().map(PostStar::getPostSafeKey).toArray(String[]::new);

        // Get those posts who liked by this user
        List<PostStar> userLikedPosts = ofy().load().type(PostStar.class)
                .filter("userId", userId).limit(200).list();
        // Get only postIds from this
        String[] likedPosts = userLikedPosts.stream().map(PostStar::getPostSafeKey).toArray(String[]::new);

        // Remove these posts from postIds
        List<String> l1 = new ArrayList<>(Arrays.asList(postIds));
        List<String> l2 = new ArrayList<>(Arrays.asList(likedPosts));
        l1.removeAll(l2);
        postIds = l1.toArray(new String[0]);
        //Arrays.stream(postIds).forEach(t->Arrays.asList(likedPosts).remove(t)); // array can't be modify


        // Sort postIds by frequency
        postIds = Datastore.sortArrayByFreq(postIds);



        // Save these Ids in UserSuggestion
        UserSuggestion userSuggestion = new UserSuggestion(userId, postIds);
        // Save it
        ofy().save().entity(userSuggestion); // Async Task

    }
}
