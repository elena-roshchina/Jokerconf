package ru.jokerconf.jokerconf.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.jokerconf.jokerconf.UpdateModel;

public interface DayUpdateApi {
    @GET("/jpoint2/get_update_dt.php")
    Call<UpdateModel> getData(@Query("confid") int count);
}
