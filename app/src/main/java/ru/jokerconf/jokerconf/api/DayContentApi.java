package ru.jokerconf.jokerconf.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.jokerconf.jokerconf.DayContentModel;

public interface DayContentApi {

    @GET("/jpoint2/get_info.php")
    Call<DayContentModel> getData(@Query("lang") String resourceName,  @Query("confid") int count);

}
