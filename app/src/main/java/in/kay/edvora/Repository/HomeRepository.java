package in.kay.edvora.Repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONObject;

import java.util.List;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.Models.HomeModel;
import in.kay.edvora.Utils.CustomToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeRepository {
    private MutableLiveData<List<HomeModel>> feedlist;
    Context context;
    public HomeRepository(MutableLiveData<List<HomeModel>> feedlist, Context context) {
        this.feedlist = feedlist;
        this.context=context;
    }
    public MutableLiveData<List<HomeModel>> LoadFeed (){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<List<HomeModel>> call = api.getFeed("Bearer "+ Prefs.getString("accessToken",""));
        call.enqueue(new Callback<List<HomeModel>>() {
            @Override
            public void onResponse(Call<List<HomeModel>> call, Response<List<HomeModel>> response) {
                Log.d("RESPONSECODE", "onResponse: "+response.code());
               if (response.isSuccessful())
               {
                   feedlist.setValue(response.body());
               }
               else if (response.code()==502){
                   //Token expired
                   MyApplication application=new MyApplication();
                   application.RefreshToken(Prefs.getString("refreshToken",""));
                   LoadFeed();
               }
               else {

               }
            }

            @Override
            public void onFailure(Call<List<HomeModel>> call, Throwable t) {
                CustomToast customToast=new CustomToast();
                customToast.ShowToast(context,t.getLocalizedMessage());
            }
        });
        return feedlist;
    }
}
