package info.cameronlund.scout.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.List;

// TODO Handle level
public class Event implements Parcelable, Comparable<Event> {
    public static final Creator<Event> CREATOR
            = new Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    JsonObject json;
    private String name;
    private Location location;
    private Date date;
    private String sku;
    private List<Team> teams;
    private String level = "VRC";

    public Event(JsonObject json) {
        this.json = json;
        date = ISODateTimeFormat.dateTimeParser().parseDateTime(json.get("start").getAsString()).toDate();
        location = new Location(json);
        name = json.get("name").getAsString();
        sku = json.get("sku").getAsString();
        level = json.get("program").getAsString();
    }

    public Event(String date, Location location, String name, String sku, String level) {
        this.date = ISODateTimeFormat.dateTimeParser().parseDateTime(date).toDate();
        this.location = location;
        this.name = name;
        this.sku = sku;
        this.level = level;
    }

    public Event(long date, Location location, String name, String sku, String level) {
        this.date = new Date(date);
        this.location = location;
        this.name = name;
        this.sku = sku;
        this.level = level;
    }

    public Event(Parcel in) {
        date = new Date(in.readLong());
        location = new Location(in.readString());
        name = in.readString();
        sku = in.readString();
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Location getLocation() {
        return location;
    }

    public String getSku() {
        return sku;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTime());
        dest.writeString(location.serialize());
        dest.writeString(name);
        dest.writeString(sku);
    }

    @Override
    public int compareTo(@NonNull Event another) {
        long thisDate = date.getTime();
        long thatDate = another.getDate().getTime();
        if (thisDate > thatDate) {
            return -1;
        } else if (thisDate < thatDate) {
            return 1;
        }
        return 0;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getLevel() {
        return level;
    }
}
