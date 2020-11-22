package in.kay.edvora.Views.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mukesh.OtpView;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.R;
import in.kay.edvora.Utils.CustomToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OtpFragment extends Fragment {
    Context context;
    TextView tvEmail, tvTimer;
    OtpView otpView;
    Button btnNext;
    View view;
    String value, userType;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        value = getArguments().getString("email");
        userType = getArguments().getString("userType");
        this.view = view;
        tvEmail = view.findViewById(R.id.tvEmail);
        tvTimer = view.findViewById(R.id.tv_timer);
        tvEmail.setText(value);
        btnNext = view.findViewById(R.id.btn_next);
        btnNext.setTextColor(Color.WHITE);
        otpView = view.findViewById(R.id.otp_view);
        otpView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Integer length=otpView.length();
                if (length>=4)
                {
                    btnNext.setBackground(getResources().getDrawable(R.drawable.ic_btn_one));
                    btnNext.setClickable(true);
                }
                else {
                    btnNext.setBackground(getResources().getDrawable(R.drawable.ic_in_active));
                    btnNext.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpView.setOtpCompletionListener(otp -> {
            btnNext.setOnClickListener(view1 ->DoWork(otp));
        });
        CountdownLogic();
        tvTimer.setOnClickListener(view12 -> {
            CustomToast customToast = new CustomToast();
            customToast.ShowToast(context, "OTP send successfully...");
        });
    }

    private void CountdownLogic() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText("Time remaining: 00:" + millisUntilFinished / 1000);
                tvTimer.setClickable(false);
            }

            public void onFinish() {
                tvTimer.setText("Resend");
                tvTimer.setClickable(true);
            }

        }.start();
    }

    private void DoWork(String otp) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMax(100);
        pd.setMessage("Setting you...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.verifyOtp(value, otp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        pd.dismiss();
                        Fragment mFragment = null;
                        String str = response.body().string();
                        JSONObject jsonObject = new JSONObject(str);
                        String refreshToken = jsonObject.getString("refreshToken");
                        String accessToken = jsonObject.getString("accessToken");
                        Prefs.putBoolean("isLoggedIn", true);
                        Prefs.putString("userType", userType);
                        Prefs.putString("userId", jsonObject.getString("userId"));
                        Prefs.putString("refreshToken", refreshToken);
                        Prefs.putString("accessToken", accessToken);
                        if (userType.equalsIgnoreCase("student"))
                            mFragment = new StudentDetailFragment();
                        else mFragment = new FacultyDetailFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, mFragment).commit();
                    } else {
                        pd.dismiss();
                        Toast.makeText(context, "Error" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(context, "Exception error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }
}