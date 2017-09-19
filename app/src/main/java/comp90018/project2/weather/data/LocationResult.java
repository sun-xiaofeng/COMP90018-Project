package comp90018.project2.weather.data;

/**
 * Created by sunxiaofeng208 on 2017/9/19.
 */

public class LocationResult {
    private String address;

    public LocationResult(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
