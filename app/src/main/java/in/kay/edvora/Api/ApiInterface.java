package in.kay.edvora.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    public final String BASE_URL="https://192.168.7.1:8080/";
    @FormUrlEncoded
    @POST("auth/signup")
    Call<ResponseBody> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("userType") String userType,
            @Field("name") String name);

    @FormUrlEncoded
    @POST("auth/signup/otp")
    Call<ResponseBody> verifyOtp(
            @Field("email") String email,
            @Field("otp") String otp);
}
