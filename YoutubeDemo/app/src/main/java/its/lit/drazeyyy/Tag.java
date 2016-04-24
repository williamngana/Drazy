package its.lit.drazeyyy;

/**
 * Created by ahndrewtam on 4/24/2016.
 */
public class Tag {
    double longitude;
    double latitude;
    String msg;
    String img;


    public Tag(double longitude, double latitude, String msg, String img){
        this.longitude = longitude;
        this.latitude = latitude;
        this.msg = msg;
        this.img = img;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getImg() {
        return img;
    }

    public String getMsg() {
        return msg;
    }
}
