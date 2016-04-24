package its.lit.drazeyyy;

import java.util.ArrayList;

/**
 * Created by ahndrewtam on 4/24/2016.
 */
public class User {

    final static String USER_PREF_DB = "user_database";
    final static String DRONE_IMG = "drone_img";
    final static String TAGGED_SET = "tagged_set";

    private String username;
    private ArrayList<Tag> tagged;
    private String firstName;
    private String lastName;
    private char sex;
    private float activityScore;
    private String dateCreated;

    private ArrayList<String> friends;
    private String droneImage;

    public User(ArrayList<String> friends, ArrayList<Tag> tagged){

    }

    void addFriend(String friendName){
        friends.add(friendName);
    }

    void increaseScore(float inc){
        activityScore += inc;
    }

    void addTag(double longitude, double latitude, String msg, String image){
        Tag tag = new Tag(longitude, latitude, msg, image);
        tagged.add(tag);
    }

    public String getDroneImage() {
        return droneImage;
    }

    public void setDroneImage(String droneImage) {
        this.droneImage = droneImage;
    }
}
