package org.octabyte.zeem.Queues;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Ref;
import org.octabyte.zeem.API.Helper.ContactHolder;
import org.octabyte.zeem.API.Helper.Datastore;
import org.octabyte.zeem.API.Helper.UserHelper;
import org.octabyte.zeem.Datastore.*;
import org.octabyte.zeem.Helper.DataType;

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
@WebServlet(name = "Friendship", description = "Create friends from contact list",
        urlPatterns = "/queue/friendship_queue")
public class Friendship extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get contactHolderKey (WebSafeKey) from request
        String contactHolderKey = req.getParameter("contactHolderKey");

        // Create ContactHolder key from webSafeKey
        Key key = Key.create(contactHolderKey);

        // Get user id and contact list from Datastore
        ContactHolder contactHolder = null;
        try {
            contactHolder = (ContactHolder) ofy().load().key(key).safe();
        } catch (NotFoundException e) {
            e.printStackTrace();

            return;
        }

        // Check contact list is not null if null end this queue
        if (contactHolder.getContactList() == null ){ // contact list is null
            return;
        }

        // Create Contact list which hold the Contacts of user
        List<Contact> contacts = new ArrayList<>();
        // Create Friend list which hold Friends
        List<Friend> friends = new ArrayList<>();

        // Iterate contact list for each contact
        for (Long contact: contactHolder.getContactList()) {
            // Create User Key
            Key userKey = Key.create(User.class, contactHolder.getUserId());
            // Add each contact in contacts ArrayList
            contacts.add(new Contact(contact, userKey)); // User is Parent of each contact

            Long friendId = addFriend(contactHolder.getUserId(), contactHolder.getPhone(), contact);

            if(friendId != 0L)  // Both become friend for each other
                 friends.add(new Friend(userKey, friendId)); // For batch save

        } // end for loop

        // Batch save every Contact
        ofy().save().entities(contacts).now();
        // Batch save every Friend
        ofy().save().entities(friends).now();

        // At the end delete ContactHolder Entity Because there is no further use of this
        // delete ContactHolder entity
        ofy().delete().key(key).now();
    }

    /**
     * Add a New Friend or send friend request
     * @param userId        Who is making friend
     * @param userPhone     User phone who is making friend
     * @param friendPhone   Phone of that user who is going to become friend
     * @return              0L for request and user not found with this number else friendId
     */
    public static Long addFriend(Long userId, Long userPhone, Long friendPhone){
        // check user (friend) exist with this number
        User friend = ofy().load().type(User.class).filter("phone", friendPhone).first().now();

        if (friend != null) { // friend is exist with this phone

            // Create Contact key with User(friend) key
             Key contactKey = Key.create( Key.create(User.class, friend.getUserId()), Contact.class, userPhone);
            // check my(userPhone) phone number from friend contact list
            if (ofy().load().key(contactKey).now() == null) {  // my number is not in friend contact list
                // Send Friend request
                // Save user id and phone in Friend Request with friend parent key
                Key friendKey = Key.create(User.class, friend.getUserId());
                FriendRequest friendRequest =
                        new FriendRequest(Datastore.autoGeneratedId(), friendKey, Ref.create(Key.create(User.class, userId)), userPhone);
                ofy().save().entity(friendRequest).now();

                // Send notification to friend about Friend Request
                Datastore.sendPrivateNotification(friend.getUserId(), userId, DataType.NotificationType.FRIEND_REQUEST, null);

                return 0L;

            }else{ // when number is already in friend contact list
                // Automatically make both user as Friend of each other
                // create me as a Friend of my Friend
                makeFriend(friend.getUserId(), userId, false);
                    /* (REMOVE) Friend userAsFriend = new Friend(Key.create(User.class, friend.getUserId()), contactHolder.getUserId()); // Me as a Friend for other person
                    // Save me in Friend list for other person
                    ofy().save().entity(userAsFriend).now(); (REMOVE) */

                // Inform friend by notification that new friend is added
                Datastore.sendPrivateNotification(friend.getUserId(), userId, DataType.NotificationType.NEW_FRIEND, null);

                // increment in both Friend count
                UserHelper.incrementFriendCount(userId);
                UserHelper.incrementFriendCount(friend.getUserId());

                // Other Friend is created with batch save
                return friend.getUserId();
            } // end inner if-else

        } // end if

        return 0L;
    }

    /**
     * Make Friend
     * @param userId    Id of that user for how Friend is made
     * @param friendId  Id of that user how is going to be friend
     * @param sendNotification  Send notification to friendId or not TRUE | FALSE
     */
    public static void makeFriend(Long userId, Long friendId, Boolean sendNotification){
        // Make friendId as a Friend of userId
        Friend userAsFriend = new Friend(Key.create(User.class, userId), friendId);
        // Save friendId in user Friends
        ofy().save().entity(userAsFriend).now();

        if (sendNotification) { // Send Notification
            // Inform friend by notification that new friend is added
            Datastore.sendPrivateNotification(friendId, userId, DataType.NotificationType.NEW_FRIEND, null);
        }
    }

}
