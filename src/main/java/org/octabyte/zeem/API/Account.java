package org.octabyte.zeem.API;

import com.google.api.server.spi.config.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import de.alpharogroup.jgeohash.GeoHashExtensions;
import org.octabyte.zeem.API.Entity.Registered;
import org.octabyte.zeem.API.Entity.Username;
import org.octabyte.zeem.API.Helper.ContactHolder;
import org.octabyte.zeem.API.Helper.UserInfoHolder;
import org.octabyte.zeem.Datastore.AppInfo;
import org.octabyte.zeem.Datastore.User;
import org.octabyte.zeem.Datastore.UserAlert;
import org.octabyte.zeem.Datastore.UserProfile;
import org.octabyte.zeem.Helper.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.octabyte.zeem.Helper.OfyService.ofy;

/**
 * Zeem API version v1 - Defining Account EndPoints
 */
@Api(
        name = "zeem",
        version = "v1",
        apiKeyRequired = AnnotationBoolean.TRUE
)

public class Account {

    /**
     * First of all when user is entered phone number check user is already register or not
     * @param phone user phone number (Required)
     * @return user is registered or not (True | False) if registered return also user info
     */
    public Registered checkRegistration(@Named("phone") Long phone){

        Registered registered = new Registered();

        // Check phone is exist or not
        User user = ofy().load().type(User.class).filter("phone", phone).first().now();

        if (user == null) { // user is not register
            registered.setIsRegistered(false);
        }else { // user is already registered
            registered.setIsRegistered(true);
            user.setProfilePic(Utils.bucketURL+user.getProfilePic());
            registered.setUser(user);
        }

        return registered;
    }

    /**
     * Check username is available or not if not then suggest some user names
     * @param username  username that need to be check
     * @param fullName  fullName is user to generate some username suggestion when current username is not available
     * @return          Username object which contain current username is available or not if not contain some suggestion
     */
    public Username checkUsername(@Named("username") String username, @Named("fullName") String fullName){

        // Convert full name in lower case letter
        fullName = fullName.toLowerCase();

        Username usernameInfo = new Username();

        // Check user name is available or not
        User user = ofy().load().type(User.class).filter("username", username.toLowerCase()).first().now();
        if (user != null){ // username is not available suggest some username

            // Create string variable to hold new generated username
            String[] randomUsername = new String[7];

            // Create a Random variable to generate random numbers
            Random random = new Random();

            // split full name into first name and last name
            String[] nameParts = fullName.split(" ");

            // If there is space between name then it must be divided into two parts, else add some number with this
            if (nameParts.length > 1){ // name is converted into first name and last name
                // Generate at least five random username
                randomUsername[0] = username + "1";
                randomUsername[1] = "me." + username;
                randomUsername[2] = nameParts[0] + "." + nameParts[1]; // Place a dot(.) between two names
                randomUsername[3] = nameParts[0] + "_" + nameParts[1]; // Place underscore between parts
                randomUsername[4] = nameParts[1] + "." + nameParts[0]; // Reverse name parts and place dot
                randomUsername[5] = fullName + "1"; // Add number 1 for example Azeem1, Arfan1 etc
                randomUsername[6] = nameParts[0] + (random.nextInt(999) + 100); // Add random number to first name

            }else { // name is not converted into parts add some number with it
                randomUsername[0] = username + "1";
                randomUsername[1] = "me." + username;
                randomUsername[2] = fullName + (random.nextInt(999) + 100);
                randomUsername[3] = fullName + "1"; // Add number 1 for example Azeem1, Arfan1 etc
                randomUsername[4] = "me." + fullName;
                randomUsername[5] = fullName + (random.nextInt(999) + 100);
                randomUsername[6] = fullName + (random.nextInt(999) + 100);
            }

            // Create variable for max username which is 3
            int countUsername = 0;

            // Create a list to hold available username
            List<String> availableUsername = new ArrayList<>();

            // Loop the random username and check which are available
            for(String thisUsername : randomUsername){

                User thisUser = ofy().load().type(User.class).filter("username", thisUsername).first().now();

                if (thisUser == null){ // This username is available
                    countUsername++;
                    availableUsername.add(thisUsername);
                }

                if (countUsername >= 3)
                    break;

            } // End for loop

            // Check if some username available
            if (availableUsername.size() > 0){ // some username available
                // Add these username into username info
                usernameInfo.setSuggestions(availableUsername);
            }

        }else{ // username is available no need to suggest any username
            usernameInfo.setAvailable(true);
        }

        return usernameInfo;
    }

    /**
     * Register user if user is not already registered
     * @param lat               latitude of user (Location)
     * @param lng               longitude of user (Location)
     * @param userInfoHolder   holder user object and user phone book (contact list)
     * @return  user object
     */
    public User registerUser(@Named("lat") double lat,
                             @Named("lng") double lng,
                             UserInfoHolder userInfoHolder){

        // Convert user lat and lng into geoHash using Library
        String geoHash = GeoHashExtensions.encode(lat, lng);

        geoHash = Utils.reducePrecision(geoHash);

        // Attach geoHash info with user object
        userInfoHolder.getUser().setGeoHash(geoHash);

        // Convert username, full name in lower case
        userInfoHolder.getUser().setUsername(userInfoHolder.getUser().getUsername().toLowerCase());
        userInfoHolder.getUser().setFullName(userInfoHolder.getUser().getFullName().toLowerCase());

        // save user information into Datastore
        ofy().save().entity(userInfoHolder.getUser()).now();

        // Create additional information for user e.g email, status, badge and setting etc
        UserProfile userProfile = new UserProfile(userInfoHolder.getUser().getUserId());
        // Save this information
        ofy().save().entity(userProfile).now();

        // Create UserAlert for this user
        UserAlert userAlert = new UserAlert(userInfoHolder.getUser().getUserId());
        userAlert.setPrivateFeedId(0L);
        userAlert.setPrivateNotificationId(0L);
        userAlert.setPublicFeedId(0L);
        userAlert.setPublicNotificationId(0L);

        // Save UserAlert for user
        ofy().save().entity(userAlert).now();


        // Make Object to hold contact list and user id
        ContactHolder contactHolder = new ContactHolder(
                userInfoHolder.getUser().getUserId(),
                userInfoHolder.getContactList(),
                userInfoHolder.getUser().getPhone());
        // Save this in Datastore
        ofy().save().entity(contactHolder).now();

        // Create Push Queue for adding contacts and making friends
        Queue friendShip = QueueFactory.getDefaultQueue();
        friendShip.add(TaskOptions.Builder.withUrl("/queue/friendship_queue")
                .param("contactHolderKey", Key.create(ContactHolder.class, userInfoHolder.getUser().getUserId()).getString()));

        return userInfoHolder.getUser();
    }

    /**
     * This function is used to setting app info version code and version number
     * @param versionCode       version code of last updated app in play store
     * @param versionNumber     version number of last updated app in play store
     * @return                  AppInfo object contain appId, versionCode and versionNumber
     */
    public AppInfo setAppInfo(@Named("versionCode") int versionCode, @Named("versionNumber") String versionNumber){

        // Static app id - it will be always same
        Long appId = 111333777L;

        // Create object of AppInfo to store versionCode and versionNumber
        AppInfo appInfo = new AppInfo(appId, versionCode, versionNumber);

        // Save app info
        ofy().save().entity(appInfo).now();

        return appInfo;
    }

    /**
     * This function is used to inform android app that any new version is available on play store or not
     * @return                  AppInfo object contain appId, versionCode and versionNumber
     */
    public AppInfo getAppInfo(){

        // Static app id - it will be always same
        Long appId = 111333777L;

        // Create AppInfo key from appId
        Key<AppInfo> appInfoKey = Key.create(AppInfo.class, appId);

        // load and return the object

        try {
            return ofy().load().key(appInfoKey).safe();
        } catch (NotFoundException e) {
            e.printStackTrace();

            return null;
        }

    }

}
