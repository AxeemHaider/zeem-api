package org.octabyte.zeem.API;

import com.google.api.server.spi.config.*;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import org.octabyte.zeem.Datastore.ListMember;
import org.octabyte.zeem.Datastore.User;
import org.octabyte.zeem.Datastore.UserList;
import org.octabyte.zeem.Helper.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.octabyte.zeem.Helper.OfyService.ofy;

/**
 * Zeem API version v1 - Defining Account EndPoints
 */
@Api(
        name = "zeem",
        version = "v1",
        apiKeyRequired = AnnotationBoolean.TRUE
)

public class ListApi {

    /**
     * Create User list
     * @param userId    Id of user who want to create list
     * @param memberIds Members of list
     * @param userList  userList object contain list name, pic, member count etc
     */
    public void createList(@Named("userId") Long userId, @Nullable @Named("memberIds") List<Long> memberIds, UserList userList){

        // Set userKey in userList
        userList.setUserKey(Key.create(User.class, userId));
        // Save this list
        ofy().save().entity(userList).now();
        // Create listKey
        Key listKey = Key.create(UserList.class, userList.getListId());

        // Create member list
        List<ListMember> listMembers = new ArrayList<>();

        // Loop each member id and convert into ListMember object
        for( Long memberId : memberIds ) {

            listMembers.add( new ListMember(listKey, Ref.create(Key.create(User.class, memberId))) );

        } // end for loop

        // Bulk save
        ofy().save().entities(listMembers); // Async Task

    }

    /**
     * Get user list
     * @param userId    Id of user who want to get userLists
     * @return          List of userList
     */
    public List<UserList> getUserList(@Named("userId") Long userId){

        // Get userList where ancestor is userId
        return ofy().load().type(UserList.class).ancestor(Key.create(User.class, userId)).list();
    }

    /**
     * Get list Members
     * @param listId    Id of list that need to get members(User)
     * @return          list of Members(User)
     */
    @ApiMethod(path = "getListMembers/{listId}")
    public List<User> getListMembers(@Named("listId") Long listId){

        // Get listMembers(User) where ancestor is listId
        List<ListMember> listMembers = ofy().load().type(ListMember.class).ancestor(Key.create(UserList.class, listId)).list();

        // Create User list to hold user object
        List<User> userList = new ArrayList<>();

        // Loop each member and add into userList
        for( ListMember listMember : listMembers ) {

            User user = listMember.getUserRef().get();
            user.setProfilePic(Utils.bucketURL + user.getProfilePic());

            userList.add( user );

        } // end for loop


        return userList;
    }

    /**
     * Remove user list
     * @param listSafeKey    safeKey of list that need to be remove
     */
    public void removeList(@Named("listSafeKey") String listSafeKey){

        ofy().delete().key(Key.create(listSafeKey)); // Async Task
    }

    /**
     * Remove member from user list
     * @param listId    Id of list from which you need to remove member
     * @param userId    Id of the member that need to be remove
     */
    public void removeListMember(@Named("listId") Long listId, @Named("userId") Long userId){

        // Fetch list Member
        ListMember listMember = ofy().load().type(ListMember.class).ancestor(Key.create(UserList.class, listId))
                .filter("userRef", Ref.create(Key.create(User.class, userId))).first().now();
        // Remove this member from list
        ofy().delete().entity(listMember); // Async Task
    }

}
