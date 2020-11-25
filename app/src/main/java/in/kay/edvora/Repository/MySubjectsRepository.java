package in.kay.edvora.Repository;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.Utils.CustomToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MySubjectsRepository {
    public MutableLiveData<List<String>> model;
    Context context;

    public MySubjectsRepository(MutableLiveData<List<String>> model, Context context) {
        this.model = model;
        this.context = context;
    }

    public MutableLiveData<List<String>> LoadMySubjects() {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMax(100);
        pd.setMessage("Loading...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<String>> call = apiInterface.viewMySubjects("Bearer " + Prefs.getString("accessToken", ""));
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    model.setValue(response.body());
                } else if (response.code() == 502) {
                    MyApplication myApplication = new MyApplication();
                    myApplication.RefreshToken(Prefs.getString("refreshToken", ""), context);
                    LoadMySubjects();
                } else {
                    pd.dismiss();
                    CustomToast customToast = new CustomToast();
                    customToast.ShowToast(context, "Error occured...");
                }

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(context, "Server down...");
            }
        });
        return model;
    }
}
