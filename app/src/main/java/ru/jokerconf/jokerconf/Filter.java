package ru.jokerconf.jokerconf;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filter {

    @SerializedName("filterName")
    @Expose
    private String filterName;
    @SerializedName("isChecked")
    @Expose
    private boolean isChecked;

    public Filter(String filterName, boolean isChecked) {
        this.filterName = filterName;
        this.isChecked = isChecked;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String name) {
        this.filterName = filterName;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
