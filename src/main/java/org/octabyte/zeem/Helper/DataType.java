package org.octabyte.zeem.Helper;

public class DataType {

    public static  enum Gender{
        M, F
    }

    public static enum NotificationType {
        POST_LIKE, POST_COMMENT, STORY_COMMENT, FRIEND_REQUEST, FOLLOWER, NEW_FRIEND, LIST_POST, TAG_POST,
        POST_MENTION, COMMENT_MENTION, COMMENT_LIKE, POST_DELETE,
        REPORT_SPAM, REPORT_OTHER, REPORT_SEXUAL_CONTENT, REPORT_ANTI_RELIGION
    }

    public static enum Mode {
        PRIVATE, PUBLIC
    }

    public static enum Alert {
        NOTIFICATION, FEED
    }

    public static enum PostType {
        CARD, IMAGE, AUDIO, VIDEO, GIF, TALKING_PHOTO, STORY
    }

    public static enum CommentType {
        TEXT, IMAGE, AUDIO
    }

    public static enum TagApproved {
        PUBLIC, PRIVATE, NEVER
    }

    public static enum HowTag {
        PUBLIC, FRIEND, NO_ONE
    }

    public static enum Relation {
        FRIEND, FOLLOWER, FOLLOWING, BLOCK_FRIEND, PUBLIC
    }

    public static enum Report {
        ANTI_RELIGION, OTHER, SPAM, SEXUAL_CONTENT
    }

}
