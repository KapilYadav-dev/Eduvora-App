package in.kay.edvora.Repository;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.List;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.Models.FindLibraryModel;
import in.kay.edvora.Utils.CustomToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibraryRepository extends ViewModel {
    public MutableLiveData<List<FindLibraryModel>> model;
    Context context;

    public LibraryRepository(MutableLiveData<List<FindLibraryModel>> model, Context context) {
        this.model = model;
        this.context = context;
    }

    public MutableLiveData<List<FindLibraryModel>> LoadData() {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setMax(100);
        pd.setMessage("Loading...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<List<FindLibraryModel>> call = api.viewAllLibrary();
        call.enqueue(new Callback<List<FindLibraryModel>>() {
            @Override
            public void onResponse(Call<List<FindLibraryModel>> call, Response<List<FindLibraryModel>> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    model.setValue(response.body());
                } else if (response.code() == 502) {
                    //Token expired
                    MyApplication application = new MyApplication();
                    application.RefreshToken(Prefs.getString("refreshToken", ""), context);
                    LoadData();
                } else {
                    pd.dismiss();
                    try {
                        CustomToast customToast = new CustomToast();
                        customToast.ShowToast(context, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<FindLibraryModel>> call, Throwable t) {
                pd.dismiss();
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(context, "Failure is " + t.getLocalizedMessage());
            }
        });
        return model;
    }
}
