
package ru.jokerconf.jokerconf;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventOld implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("period")
    @Expose
    private String period;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("desc_short")
    @Expose
    private String descShort;
    @SerializedName("desc_long")
    @Expose
    private String descLong;
    @SerializedName("speaker_1")
    @Expose
    private String speaker1;
    @SerializedName("co_speakers")
    @Expose
    private String coSpeakers;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("pdf")
    @Expose
    private String pdf;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("isStarred")
    @Expose
    private boolean isStarred = false;

    public final static Parcelable.Creator<EventOld> CREATOR = new Creator<EventOld>() {


        @SuppressWarnings({
            "unchecked"
        })
        public EventOld createFromParcel(Parcel in) {
            return new EventOld(in);
        }

        public EventOld[] newArray(int size) {
            return (new EventOld[size]);
        }

    }
    ;
    private final static long serialVersionUID = 4656808924016368093L;

    protected EventOld(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.period = ((String) in.readValue((String.class.getClassLoader())));
        this.place = ((String) in.readValue((String.class.getClassLoader())));
        this.kind = ((String) in.readValue((String.class.getClassLoader())));
        this.level = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.descShort = ((String) in.readValue((String.class.getClassLoader())));
        this.descLong = ((String) in.readValue((String.class.getClassLoader())));
        this.speaker1 = ((String) in.readValue((String.class.getClassLoader())));
        this.coSpeakers = ((String) in.readValue((Object.class.getClassLoader())));
        this.language = ((String) in.readValue((String.class.getClassLoader())));
        this.pdf = ((String) in.readValue((String.class.getClassLoader())));
        this.video = ((String) in.readValue((String.class.getClassLoader())));
        this.dateTime = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.tags, (java.lang.String.class.getClassLoader()));
        this.isStarred = ((boolean) in.readValue((boolean.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public EventOld() {
    }

    /**
     * 
     * @param tags
     * @param dateTime
     * @param pdf
     * @param coSpeakers
     * @param period
     * @param descShort
     * @param kind
     * @param speaker1
     * @param id
     * @param descLong
     * @param title
     * @param level
     * @param language
     * @param place
     * @param video
     */
    public EventOld(String id, String period, String place, String kind, String level, String title,
                    String descShort, String descLong, String speaker1, String coSpeakers,
                    String language, String pdf, String video, String dateTime, List<String> tags,
                    boolean isStarred) {
        super();
        this.id = id;
        this.period = period;
        this.place = place;
        this.kind = kind;
        this.level = level;
        this.title = title;
        this.descShort = descShort;
        this.descLong = descLong;
        this.speaker1 = speaker1;
        this.coSpeakers = coSpeakers;
        this.language = language;
        this.pdf = pdf;
        this.video = video;
        this.dateTime = dateTime;
        this.tags = tags;
        this.isStarred = isStarred;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescShort() {
        return descShort;
    }

    public void setDescShort(String descShort) {
        this.descShort = descShort;
    }

    public String getDescLong() {
        return descLong;
    }

    public void setDescLong(String descLong) {
        this.descLong = descLong;
    }

    public String getSpeaker1() {
        return speaker1;
    }

    public void setSpeaker1(String speaker1) {
        this.speaker1 = speaker1;
    }

    public String getCoSpeakers() {
        return coSpeakers;
    }

    public void setCoSpeakers(String coSpeakers) {
        this.coSpeakers = coSpeakers;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(period);
        dest.writeValue(place);
        dest.writeValue(kind);
        dest.writeValue(level);
        dest.writeValue(title);
        dest.writeValue(descShort);
        dest.writeValue(descLong);
        dest.writeValue(speaker1);
        dest.writeValue(coSpeakers);
        dest.writeValue(language);
        dest.writeValue(pdf);
        dest.writeValue(video);
        dest.writeValue(dateTime);
        dest.writeList(tags);
        dest.writeValue(isStarred);
    }

    public int describeContents() {
        return  0;
    }

}
