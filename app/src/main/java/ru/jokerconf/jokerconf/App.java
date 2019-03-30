package ru.jokerconf.jokerconf;

import android.app.Application;

import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jokerconf.jokerconf.api.DayContentApi;
import ru.jokerconf.jokerconf.api.DayUpdateApi;

public class App extends Application {


    private static DayUpdateApi dayUpdateApi;
    private static DayContentApi dayContentApi;

    private static final String BASE_URL = "http://starfox.spb.ru";
    //



    private OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS);
    //

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dayUpdateApi = retrofit.create(DayUpdateApi.class);
        dayContentApi = retrofit.create(DayContentApi.class);
    }

    public static DayUpdateApi getDayUpdateApi(){ return dayUpdateApi;}
    public static DayContentApi getDayContentApi(){ return dayContentApi;}
    public static String getMyBaseUrl(){return BASE_URL;}
}
