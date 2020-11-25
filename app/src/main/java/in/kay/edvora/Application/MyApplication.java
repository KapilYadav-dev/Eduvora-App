package in.kay.edvora.Application;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Utils.CustomToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OfflineImageCache();
        EasyPrefInitz();
    }

    private void OfflineImageCache() {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }

    private void EasyPrefInitz() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName("Edvora")
                .setUseDefaultSharedPreference(true)
                .build();
    }

    public void RefreshToken(String token, final Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getNewToken("Bearer " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String req = response.body().string();
                        JSONObject jsonObject = new JSONObject(req);
                        String accessToken = jsonObject.getString("accessToken");
                        Prefs.putString("accessToken", accessToken);
                        Log.d("ACCESSTOKENLOG", "NEWTOKEN: " + Prefs.getString("accessToken", ""));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    CustomToast customToast = new CustomToast();
                    try {
                        customToast.ShowToast(context, "Error occured " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(context, "Failure occured " + t.getLocalizedMessage());
            }
        });

    }

    public void DownloadFile(String url, String type, String title, String path, String extension, Context context) {
        if (ActivityCompat.checkSelfPermission((Activity)context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 10);
        } else {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription(type);
            request.setTitle(title);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalPublicDir(path, title + "." + extension);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }
    }
}
