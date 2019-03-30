
package ru.jokerconf.jokerconf;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoOld implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("wifi_net")
    @Expose
    private String wifiNet;
    @SerializedName("wifi_pass")
    @Expose
    private String wifiPass;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("events")
    @Expose
    private List<EventOld> events = null;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    public final static Parcelable.Creator<InfoOld> CREATOR = new Creator<InfoOld>() {


        @SuppressWarnings({
            "unchecked"
        })
        public InfoOld createFromParcel(Parcel in) {
            return new InfoOld(in);
        }

        public InfoOld[] newArray(int size) {
            return (new InfoOld[size]);
        }

    }
    ;
    private final static long serialVersionUID = -8976673874591049004L;

    protected InfoOld(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.startDate = ((String) in.readValue((String.class.getClassLoader())));
        this.endDate = ((String) in.readValue((String.class.getClassLoader())));
        this.place = ((String) in.readValue((String.class.getClassLoader())));
        this.about = ((String) in.readValue((String.class.getClassLoader())));
        this.wifiNet = ((String) in.readValue((String.class.getClassLoader())));
        this.wifiPass = ((String) in.readValue((String.class.getClassLoader())));
        this.picture = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.events, (EventOld.class.getClassLoader()));
        in.readList(this.tags, (java.lang.String.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public InfoOld() {
    }

    /**
     * 
     * @param tags
     * @param picture
     * @param wifiNet
     * @param id
     * @param startDate
     * @param title
     * @param events
     * @param about
     * @param endDate
     * @param wifiPass
     * @param place
     */
    public InfoOld(String id, String title, String startDate, String endDate, String place, String about, String wifiNet, String wifiPass, String picture, List<EventOld> events, List<String> tags) {
        super();
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.place = place;
        this.about = about;
        this.wifiNet = wifiNet;
        this.wifiPass = wifiPass;
        this.picture = picture;
        this.events = events;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWifiNet() {
        return wifiNet;
    }

    public void setWifiNet(String wifiNet) {
        this.wifiNet = wifiNet;
    }

    public String getWifiPass() {
        return wifiPass;
    }

    public void setWifiPass(String wifiPass) {
        this.wifiPass = wifiPass;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<EventOld> getEvents() {
        return events;
    }

    public void setEvents(List<EventOld> events) {
        this.events = events;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeValue(place);
        dest.writeValue(about);
        dest.writeValue(wifiNet);
        dest.writeValue(wifiPass);
        dest.writeValue(picture);
        dest.writeList(events);
        dest.writeList(tags);
    }

    public int describeContents() {
        return  0;
    }

}
