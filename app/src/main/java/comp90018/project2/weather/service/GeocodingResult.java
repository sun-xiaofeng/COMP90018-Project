package comp90018.project2.weather.service;


public class GeocodingResult {
    private String address;

    public GeocodingResult(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
