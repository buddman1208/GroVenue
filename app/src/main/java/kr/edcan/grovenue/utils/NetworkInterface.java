package kr.edcan.grovenue.utils;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import kr.edcan.grovenue.model.Spot;
import kr.edcan.grovenue.model.SpotResult;
import kr.edcan.grovenue.model.Star;
import kr.edcan.grovenue.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Junseok on 2016-09-30.
 */

public interface NetworkInterface {
    @POST("/users")
    @FormUrlEncoded
    Call<User> userRegister(
            @Field("isMale") boolean isMale,
            @Field("age") int age,
            @Field("purpose") int purpose,
            @Field("budget") int budget,
            @Field("job") String job,
            @Field("name") String name,
            @Field("id") String id  ,
            @Field("password") String password
    );
    @POST("/users/login")
    @FormUrlEncoded
    Call<User> userLogin(
            @Field("id") String id,
            @Field("password") String password
    );

    @GET("/users/me")
    Call<User> getUserInfo(
            @Header("Login-Token") String token
    );


    @GET("/spots")
    Call<SpotResult> getSpotList(
            @Header("Login-Token") String token,
            @Query("skip") int skip,
            @Query("limit") int limit,
            @Query("longitude") Float longitude,
            @Query("latitude") Float latitude,
            @Query("query") String query,
            @Query("purpose") Integer purpose,
            @Query("budget") Integer budget,
            @Query("maxDistance") Integer maxDistance,
            @Query("minScore") int minScore
    );

    @GET("/spots/{id}")
    Call<Spot> getSpotInfo(
            @Header("Login-Token") String token,
            @Path("id") String id
    );

    @GET("/spots/{id}/stars")
    Call<ArrayList<Star>> getSpotStarList(
            @Header("Login-Token") String token,
            @Path("id") String id
    );


    @POST("/spots/{id}/stars")
    @FormUrlEncoded
    Call<ResponseBody> postSpotStar(
            @Header("Login-Token") String token,
            @Path("id") String spotId,
            @Field("score") int score,
            @Field("title") String title,
            @Field("content") String content
    );

}
