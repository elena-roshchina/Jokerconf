package ru.jokerconf.jokerconf;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Day {
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("events")
    @Expose
    private List<Event> events = null;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}
