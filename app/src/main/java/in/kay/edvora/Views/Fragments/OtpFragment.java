package in.kay.edvora.Views.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.io.IOException;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OtpFragment extends Fragment {
    Context context;
    TextView tvEmail;
    OtpView otpView;
    Button btnNext;
    View view;
    String value,userType;
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
        this.view=view;
        tvEmail=view.findViewById(R.id.tvEmail);
        tvEmail.setText(value);
        btnNext=view.findViewById(R.id.btn_next);
        btnNext.setTextColor(Color.WHITE);
        otpView = view.findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(final String otp) {
                btnNext.setBackground(getResources().getDrawable(R.drawable.ic_btn_one));
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DoWork(otp);
                    }
                });
            }
        });
    }

    private void DoWork(String otp) {
        final ProgressDialog pd = new ProgressDialog( context);
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
        Call<ResponseBody> call = apiInterface.verifyOtp(value,otp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        pd.dismiss();
                        Fragment mFragment = null;
                        if (userType.equalsIgnoreCase("student")) mFragment = new StudentDetailFragment();
                        else mFragment = new StudentDetailFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, mFragment).commit();
                    }
                    else {
                        pd.dismiss();
                        Toast.makeText(context, "Error" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
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