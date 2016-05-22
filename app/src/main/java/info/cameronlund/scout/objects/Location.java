package info.cameronlund.scout.objects;

import com.google.gson.JsonObject;

public class Location {
    private String line1;
    private String line2;
    private String zip;
    private String city;
    private String region;
    private String country;
    private String venue;


    public Location(JsonObject object) {
        line1 = object.get("loc_address1").getAsString();
        line2 = object.get("loc_address2").getAsString();
        city = object.get("loc_city").getAsString();
        country = object.get("loc_country").getAsString();
        region = object.get("loc_region").getAsString();
        zip = object.get("loc_postcode").getAsString();
        venue = object.get("loc_venue").getAsString();
        serialize(line1, line2, zip, city, region, country, venue);
    }

    public Location(String loc) {
        String[] locSplit = loc.split(";");
        line1 = locSplit[0];
        line2 = locSplit[1];
        zip = locSplit[2];
        city = locSplit[3];
        region = locSplit[4];
        country = locSplit[5];
        venue = locSplit[6];
    }

    public static String serialize(String line1, String line2, String zip, String city, String region, String country, String venue) {
        String message = "";
        message += line1.length() > 0 ? line1 : " ";
        message +=";";
        message += line2.length() > 0 ? line2 : " ";
        message +=";";
        message += zip.length() > 0 ? zip : " ";
        message +=";";
        message += city.length() > 0 ? city : " ";
        message +=";";
        message += region.length() > 0 ? region : " ";
        message +=";";
        message += country.length() > 0 ? country : " ";
        message +=";";
        message += venue.length() > 0 ? venue : " ";
        return message;
    }

    public String makeDirectable() {
        String address = "";
        address += line1.length() > 0 ? line1+" " : "";
        address += line2.length() > 0 ? line2+" " : "";
        address += city.length() > 0 ? city+" " : "";
        address += region.length() > 0 ? region+" " : "";
        address += zip.length() > 0 ? zip+" " : "";
        address += country.length() > 0 ? country+" " : "";
        return address;
    }

    public String serialize() {
        return serialize(line1, line2, zip, city, region, country, venue);
    }

    public String getVenue() {
        return venue;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine1() {
        return line1;
    }

    public String getRegion() { return region; }
}
