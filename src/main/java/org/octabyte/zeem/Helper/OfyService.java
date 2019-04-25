package org.octabyte.zeem.Helper;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import org.octabyte.zeem.API.Helper.ContactHolder;
import org.octabyte.zeem.Datastore.*;

/**
 * Custom Objectify Service that this application should use.
 */
public class OfyService {
    /**
     * This static block ensure the entity registration.
     */
    static {
    	factory().register(AppInfo.class);
    	factory().register(BlockFriend.class);
    	factory().register(Comment.class);
    	factory().register(Contact.class);
    	factory().register(ExpirePost.class);
    	factory().register(Follower.class);
    	factory().register(Following.class);
    	factory().register(Friend.class);
    	factory().register(FriendRequest.class);
    	factory().register(ListMember.class);
    	factory().register(Post.class);
    	factory().register(PostBlocked.class);
    	factory().register(PostReport.class);
    	factory().register(PostStar.class);
    	factory().register(PostTag.class);
    	factory().register(PrivateFeed.class);
    	factory().register(PrivateNotification.class);
    	factory().register(PrivateStories.class);
    	factory().register(PublicFeed.class);
    	factory().register(PublicNotification.class);
    	factory().register(PublicStories.class);
    	factory().register(SavedPost.class);
    	factory().register(Story.class);
    	factory().register(StoryComment.class);
    	factory().register(User.class);
    	factory().register(UserAlert.class);
    	factory().register(UserList.class);
    	factory().register(UserProfile.class);
    	factory().register(UserSuggestion.class);
    	factory().register(ViolatedUser.class);
    	factory().register(ContactHolder.class);
    }

    /**
     * Use this static method for getting the Objectify service object in order to make sure the
     * above static block is executed before using Objectify.
     * @return Objectify service object.
     */
    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    /**
     * Use this static method for getting the Objectify service factory.
     * @return ObjectifyFactory.
     */
    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}