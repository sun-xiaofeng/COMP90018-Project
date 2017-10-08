package comp90018.project2.weather.service;

/**
 * A GeocodingResult represents the geocoding result
 * from the geocoding service
 */
public class GeocodingResult {
    /** The address */
    private String address;

    /**
     * Creates a GeocodingResult instance.
     * @param address the address
     */
    public GeocodingResult(String address) {
        this.address = address;
    }

    /**
     * Returns the address
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address
     * @param address to address to be set
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
