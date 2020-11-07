package in.kay.edvora.Repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
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

public class AnswerRepository {
    Context context;
    public AnswerRepository( Context context) {
        this.context=context;
    }


    public void SendAnswer(final String questionId, final String answer) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMax(100);
        pd.setMessage("Sending answer...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call=api.sendAnswer(questionId,answer,"Bearer "+Prefs.getString("accessToken",""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context, "Code is "+response.code(), Toast.LENGTH_SHORT).show();
                if (response.isSuccessful())
                {
                    CustomToast customToast=new CustomToast();
                    customToast.ShowToast(context,"Added answer Successfully");
                }
                else if (response.code()==502){
                    MyApplication application=new MyApplication();
                    application.RefreshToken(Prefs.getString("refreshToken",""),context);
                    SendAnswer(questionId,answer);
                }
                else {
                    try {
                        CustomToast customToast=new CustomToast();
                        customToast.ShowToast(context,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomToast customToast=new CustomToast();
                customToast.ShowToast(context,t.getLocalizedMessage());
                pd.dismiss();
            }
        });
    }
}
