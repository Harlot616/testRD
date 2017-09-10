package test.fugga.com.myapplication.Delegates;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import test.fugga.com.myapplication.Classes.JsonData;

public interface Methods {
    @Headers("Content-Type: application/json")
    @GET("search_by_date?query=android")
    Call<JsonData> getHits();
}
