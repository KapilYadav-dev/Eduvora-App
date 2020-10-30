package in.kay.edvora.Views.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.R;
import in.kay.edvora.Views.Activity.MainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {
    Context context;
    EditText etEmail,etPassword;
    View view;
    ImageView ivSignUp;
    Button btnLogin;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        Initz();
    }

    private void Initz() {
        etEmail=view.findViewById(R.id.et_email);
        etPassword=view.findViewById(R.id.et_password);
        ivSignUp=view.findViewById(R.id.iv_go_to_signup);
        btnLogin=view.findViewById(R.id.btn_login);
        IvSignUpLogic();
        ShowPasswordLogic();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoWork();
            }
        });
    }

    private void DoWork() {
        final ProgressDialog pd = new ProgressDialog( context);
        pd.setMax(100);
        pd.setMessage("Logging you...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.loginUser(etEmail.getText().toString(),etPassword.getText().toString());
       call.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               try {
                   if (response.isSuccessful())
                   {
                       pd.dismiss();
                       String string= response.body().string();
                       JSONObject jsonObject=new JSONObject(string);
                       Prefs.putString("userType",jsonObject.getString("type"));
                       Prefs.putString("accessToken",jsonObject.getString("accessToken"));
                       Prefs.putString("refreshToken",jsonObject.getString("refreshToken"));
                       Prefs.putBoolean("isLoggedIn",true);
                       Toast.makeText(context, "Welcome "+jsonObject.getString("email"), Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(context, MainActivity.class));
                   }
                   else if (response.code()==403)
                   {
                       pd.dismiss();
                       Fragment mFragment = null;
                       mFragment = new OtpFragment();
                       FragmentManager fragmentManager = getFragmentManager();
                       Bundle args = new Bundle();
                       args.putString("email", etEmail.getText().toString());
                       args.putString("userType", Prefs.getString("userType","student"));
                       mFragment.setArguments(args);
                       fragmentManager.beginTransaction()
                               .replace(R.id.container, mFragment).commit();
                   }
                   else {
                       pd.dismiss();
                       Toast.makeText(context, "User doesn't exist.", Toast.LENGTH_SHORT).show();
                   }
               }
               catch (IOException | JSONException e)
               {
                   pd.dismiss();
               }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {

           }
       });
    }

    private void IvSignUpLogic() {
        ivSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = null;
                mFragment = new StudentDetailFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mFragment).commit();
            }
        });
    }

    private void ShowPasswordLogic() {
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (etPassword.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                            etPassword.setTransformationMethod(new SingleLineTransformationMethod());
                        }
                        else {
                            etPassword.setTransformationMethod(new PasswordTransformationMethod());
                        }

                        etPassword.setSelection(etPassword.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}