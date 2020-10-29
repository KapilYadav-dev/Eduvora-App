package in.kay.edvora.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    String BASE_URL = "http://192.168.1.7:8080/";

    @FormUrlEncoded
    @POST("auth/signup")
    Call<ResponseBody> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("userType") String userType,
            @Field("name") String name);

    @FormUrlEncoded
    @POST("profile/update")
    Call<ResponseBody> updateUser(
            @Field("college") String college,
            @Field("branch") String branch,
            @Field("year") int year,
            @Header("Authorization") String header);

    @FormUrlEncoded
    @POST("auth/verifyotp")
    Call<ResponseBody> verifyOtp(
            @Field("email") String email,
            @Field("otp") String otp);

    @GET("admin/listcolleges")
    Call<ResponseBody> getColleges();

    @GET("admin/listbranches")
    Call<ResponseBody> getBranches();

}
