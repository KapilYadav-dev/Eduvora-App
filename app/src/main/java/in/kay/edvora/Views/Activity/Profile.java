package in.kay.edvora.Views.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.Models.HomeModel;
import in.kay.edvora.R;
import in.kay.edvora.Utils.CustomToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView tvName,tvCollege,tvBranch,tvYear;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userId=getIntent().getStringExtra("userId");
        Initz();
        GetAllData();
    }

    private void GetAllData() {
        final ProgressDialog pd = new ProgressDialog(this);
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
        Call<ResponseBody> call = api.viewProfile(userId,"Bearer "+ Prefs.getString("accessToken",""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful())
                    {
                        pd.dismiss();
                        String string=response.body().string();
                        JSONObject jsonObject=new JSONObject(string);
                        String userType=jsonObject.getString("userType");
                        JSONObject object=jsonObject.getJSONObject("user");
                        String name=object.getString("name");
                        String imageUrl=object.getString("imageUrl");
                        String email=object.getString("email");
                        String branch=object.getString("branch");
                        String college=object.getString("college");
                        Integer year=object.getInt("year");
                        LoadData(userType,name,email,branch,college,year,imageUrl);
                    }
                    else if (response.code()==502)
                    {
                        MyApplication myApplication = new MyApplication();
                        myApplication.RefreshToken(Prefs.getString("refreshToken", ""),Profile.this);
                        GetAllData();
                    }
                    else {
                        pd.dismiss();
                        CustomToast customToast=new CustomToast();
                        customToast.ShowToast(Profile.this,"Error: Server is down..");
                        onBackPressed();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                CustomToast customToast=new CustomToast();
                customToast.ShowToast(Profile.this,"Failure: Server is down.."+t.getLocalizedMessage());
                onBackPressed();
            }
        });
    }

    private void LoadData(String userType, String name, String email, String branch, String college, Integer year,String imageUrl) {
        if (year==0)
        {
            tvYear.setVisibility(View.GONE);
            tvCollege.setText("Faculty at "+college);
        }else {
            tvYear.setVisibility(View.VISIBLE);
            tvYear.setText(GetStringOfYear(year)+" Year");
            tvCollege.setText("Student at "+college);
        }
        tvName.setText(name);
        tvBranch.setText(branch);
        Picasso.get().load(imageUrl).into(circleImageView);
    }

    private String GetStringOfYear(Integer year) {
        if (year==1)
            return "First";
        else if (year==2)
            return "Second";
        else if (year==3)
            return "Third";
        else
            return "Final";
    }

    private void Initz() {
        circleImageView=findViewById(R.id.circleImageView);
        tvName=findViewById(R.id.tvName);
        tvYear=findViewById(R.id.tvYear);
        tvCollege=findViewById(R.id.tvCollege);
        tvBranch=findViewById(R.id.tvBranch);
    }

    public void OnBack(View view) {
        onBackPressed();
    }
}