package org.octabyte.zeem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
@WebServlet(name = "Welcome", description = "Welcome file",
        urlPatterns = "/welcome")
public class Welcome extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/plain");


        //PushNotification pushNotification = new PushNotification();

        /*Key<Post> postKey = Key.create("aglub19hcHBfaWRyIgsSBFVzZXIYgICAgICA_gkMCxIEUG9zdBiAgICAgIChCQw");
        Post post1 = (Post) ofy().load().key(postKey).now();
        out.print(post1.getPostId());
        out.print(postKey.getId());*/

        /*long different = 1528457079000L - System.currentTimeMillis();


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long monthInMilli = daysInMilli * 30;
        long yearInMilli = monthInMilli * 12;

        long elapsedYears = different / yearInMilli;
        different = different % yearInMilli;

        long elapsedMonth = different / monthInMilli;
        different = different % monthInMilli;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        out.printf(
                "%d years,%d months,%d days, %d hours, %d minutes, %d seconds%n",
                elapsedYears,elapsedMonth,elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);*/


        /*out.print("\n -------------------------------------------------");
        out.print("------------------------------------------------- \n");

        DataType.Mode postMode = DataType.Mode.PRIVATE;
        String strMode = String.valueOf(postMode);

        Boolean isPublic = !strMode.equals(String.valueOf(DataType.Mode.PRIVATE));

        out.print(isPublic);

        out.print("\n -------------------------------------------------");
        out.print("------------------------------------------------- \n");

        Post post = new Post();
        post.setUserKey(Key.create(User.class, 123));
        post.setCaption("Azeem Haider");
        post.setPostId(12345678L);

        out.print(post.getPostSafeKey());

        out.print("\n -------------------------------------------------");
        out.print("------------------------------------------------- \n");

        out.print("Welcome File 1234 no file \n");

        String s = "0";

        out.print(!s.equals("0"));
        out.print("\n");
        out.print(!s.equals("1"));
        out.print("\n");

        UUID number = UUID.randomUUID();
        long uniqueNumber = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

        out.print(uniqueNumber);
        out.print("\n");

        Date date = new Date();
        out.print(date);
        out.print("\n");

        out.print(System.currentTimeMillis());
        out.print("\n");

        DateFormat timstamp = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String timeStamp = timstamp.format(new Date());
        out.print(timeStamp);

        out.print("\n");
        out.print(Datastore.autoGeneratedId());
        out.print("\n");

        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String date1 = dateFormat.format(new Date());
        Long datetime = Long.valueOf(date1 + System.currentTimeMillis());
        out.print(datetime);
*/
    }
}
