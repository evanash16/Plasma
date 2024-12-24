package evan.ashley.plasma.constant.db;

public class Follows {

    public static class Table {
        public static final String NAME = "follows";
    }

    public static class Column {
        public static final String ID = "id";
        public static final String FOLLOWER_ID = "follower_id";
        public static final String FOLLOWEE_ID = "followee_id";
        public static final String CREATION_TIME = "creation_time";
    }
}
