package in.kay.edvora.Api;

import java.util.List;

import in.kay.edvora.Models.HomeModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    String BASE_URL = "https://edvora.herokuapp.com/";
    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseBody> loginUser(@Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("auth/signup")
    Call<ResponseBody> createUser(@Field("email") String email, @Field("password") String password, @Field("userType") String userType, @Field("name") String name);
    @FormUrlEncoded
    @POST("profile/update")
    Call<ResponseBody> updateUser(@Field("college") String college, @Field("branch") String branch, @Field("year") int year, @Header("Authorization") String header);
    @FormUrlEncoded
    @POST("auth/verifyotp")
    Call<ResponseBody> verifyOtp(@Field("email") String email, @Field("otp") String otp);
    @GET("admin/listcolleges")
    Call<ResponseBody> getColleges();
    @GET("admin/listbranches")
    Call<ResponseBody> getBranches();
    @GET("admin/listdepartments")
    Call<ResponseBody> getDept();
    @GET("admin/listsubjects")
    Call<ResponseBody> getSubjects();
    @GET("feed/view")
    Call<List<HomeModel>> getFeed(@Header("Authorization") String header);
    @GET("feed/view/{id}")
    Call<HomeModel> getParticularFeed(@Path("id") String id, @Header("Authorization") String header);
    @GET("profile/view/{id}")
    Call<ResponseBody> viewProfile(@Path("id") String id, @Header("Authorization") String header);
    @GET("auth/getaccesstoken")
    Call<ResponseBody> getNewToken(@Header("Authorization") String header);
    @FormUrlEncoded
    @POST("feed/ask")
    Call<ResponseBody> askQuestion(@Field("question") String question, @Field("topic") String topic, @Field("imageUrl") String imageUrl, @Field("subject") String subject, @Header("Authorization") String header);
    @FormUrlEncoded
    @POST("feed/answer")
    Call<ResponseBody> sendAnswer(@Field("questionId") String questionId, @Field("answer") String answer, @Header("Authorization") String header);
    @FormUrlEncoded
    @POST("library/add")
    Call<ResponseBody> uploadFile(@Field("title") String title,@Field("year") String year,@Field("subject") String subject,@Field("type") String type,@Field("url") String url);
}
