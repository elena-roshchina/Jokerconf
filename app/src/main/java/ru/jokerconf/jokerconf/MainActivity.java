package ru.jokerconf.jokerconf;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final int CONFERENCE_ID = 1; // Joker Oct 2018 - 1; DevOops - 2

    private static final String SHARED_PREF = "JOKER_SHARED_PREF";
    private static final String SHARED_LAST_UPDATE = "LAST_UPDATE";
    private static final String SHARED_DAYS = "SHARED_DAYS";
    private static final int DAYS_DEFAULT = 1;
    private static final String SHARED_DATES = "SHARED_DATES";
    private static final String SHARED_AGENDA = "SHARED_AGENDA";
    private static final String SHARED_INFO = "SHARED_INFO";

    private static final String SHARED_TAGS = "SHARED_TAGS";

    private static final String SHARED_TALKS = "SHARED_TALKS";

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String lang;
    private List<Event> restoredTalks = new ArrayList<>();
    //private List<Filter> restoredTags = new ArrayList<>();

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        lang =(Resources.getSystem().getConfiguration().locale.getLanguage().equals("ru")) ? "ru" : "en";

        if (!sharedPref.contains(SHARED_DAYS)){
            editor = sharedPref.edit();
            editor.putInt(SHARED_DAYS, DAYS_DEFAULT);
            editor.apply();
            editor.commit();
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                downloadData(MainActivity.this);
            }
        });

    } // end onCreate

    private void downloadData(final Context context){

        try {
            Response<DayContentModel>  response = App.getDayContentApi().getData(lang, CONFERENCE_ID).execute();
            if (response.body() != null){
                if (response.body().getStatus()) {

                    Info info = new Info();
                    info = response.body().getInfo();
                    Gson gson =  new Gson();

                    // Save info (about)
                    String infoJson = gson.toJson(info);
                    saveToShared(context, SHARED_INFO, infoJson);
                    // Save tags

                    final List<Filter> tags = new ArrayList<>();
                    tags.add(new Filter ("is Starred", false));
                    for (int i = 0; i < response.body().getInfo().getTags().size(); i++){
                        tags.add(new Filter(response.body().getInfo().getTags().get(i), false));
                    }
                    //
                    if (sharedPref.contains(SHARED_TAGS)){
                        Type tagsType = new TypeToken<List<Filter>>() {}.getType();
                        String strTagsJson = sharedPref.getString(SHARED_TAGS, "");
                        final List<Filter> restoredTags = gson.fromJson(strTagsJson, tagsType);

                        for (int i = 0; i < tags.size(); i++){
                            for (int j = 0; j < restoredTags.size(); j++){
                                if (tags.get(i).getFilterName().equals(restoredTags.get(j).getFilterName())){
                                    tags.get(i).setIsChecked(restoredTags.get(j).getIsChecked());
                                }
                            }
                        }
                    }

                    String tagsJson = gson.toJson(tags);
                    saveToShared(context, SHARED_TAGS, tagsJson);

                    // Save talks
                    List<Event> talks = new ArrayList<>();
                    List<Event> events = new ArrayList<>();
                    List<Day> days =  new ArrayList<>();
                    List<String> dates = new ArrayList<>();
                    List<Event> agenda = new ArrayList<>();
                    days.addAll(response.body().getInfo().getDays());

                    saveIntToShared(context,SHARED_DAYS,days.size());


                    if (sharedPref.contains(SHARED_TALKS)){
                        Type listTalkType = new TypeToken<List<Event>>() {}.getType();
                        String s = sharedPref.getString(SHARED_TALKS, "");
                        restoredTalks = gson.fromJson(s, listTalkType);
                    }

                    for (int i = 0; i < days.size(); i++){
                        events.addAll(days.get(i).getEvents());
                        dates.add(days.get(i).getDay());
                    }
                    String datesJson = gson.toJson(dates);
                    saveToShared(context, SHARED_DATES, datesJson);

                    int n = events.size();


                    for (int i = 0; i < n; i++){
                        if (events.get(i).getKind().equals("talk")){
                            talks.add(events.get(i));
                        } else {
                            agenda.add(events.get(i));
                        }
                    }
                     int m = restoredTalks.size();
                    if (m > 0) {
                        for (Event t: talks){
                            for (int j=0; j < m; j++){
                                if (restoredTalks.get(j).getId().equals(t.getId())){
                                    t.setIsStarred(restoredTalks.get(j).getIsStarred());
                                }
                            }
                        }
                    }

                    String talksJson = gson.toJson(talks);
                    saveToShared(context, SHARED_TALKS, talksJson);

                    String agendaJson = gson.toJson(agenda);
                    saveToShared(context, SHARED_AGENDA, agendaJson);

                }
            }
        } catch (IOException e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,
                            "Connection failed", Toast.LENGTH_SHORT).show();
                }
            });

        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                startSchedule();
            }
        });

    } // end of downloadData

    private void startSchedule(){
        Intent scheduleActivityIntent = new Intent(MainActivity.this, ScheduleActivity.class);
        startActivityForResult(scheduleActivityIntent,0);
        setResult(RESULT_OK);
        finish();
    }

    private void saveToShared(Context context, String key, String savedString) {
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString(key, savedString);
        editor.apply();
        editor.commit();
    }

    private void saveIntToShared(Context context, String key, int savedInt) {
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putInt(key, savedInt);
        editor.apply();
        editor.commit();
    }

    public static int countCheckedTags(List<Filter> filters){
        int c = 0;
        for (Filter f: filters){
            if (f.getIsChecked()){
                c++;
            }
        }
        return c;
    }

}
