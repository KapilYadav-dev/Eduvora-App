package in.kay.edvora.Views.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupFragment extends Fragment {
    Context context;
    View view;
    ImageView ivLogin;
    EditText etName,etEmail,etPassword;
    Button btnSignUp;
    Dialog dialog;
    String userType;
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
        dialog=new Dialog(context);
        ivLogin=view.findViewById(R.id.iv_go_to_login);
        etEmail=view.findViewById(R.id.et_email);
        etName=view.findViewById(R.id.et_name);
        etPassword=view.findViewById(R.id.et_password);
        btnSignUp=view.findViewById(R.id.btn_sign_up);
        view.findViewById(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = null;
                mFragment = new OtpFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mFragment).commit();
            }
        });
        IvLoginLogic();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFn();
            }
        });
    }

    private void ShowDiag() {
        dialog.setContentView(R.layout.usertype_diag);
        final Button btnStudent,btnTeacher;
        final TextView tvDone;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btnStudent=dialog.findViewById(R.id.btnStudent);
        tvDone=dialog.findViewById(R.id.tvDone);
        btnTeacher=dialog.findViewById(R.id.btnTeacher);
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userType="student";
                btnStudent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_student, 0, 0, 0);
                btnTeacher.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_inactive_teacher, 0, 0, 0);
                btnStudent.setBackground(getResources().getDrawable(R.drawable.ic_btn_one));
                btnTeacher.setBackground(getResources().getDrawable(R.drawable.ic_in_active));
                btnStudent.setTextColor(getResources().getColor(R.color.white));
                btnTeacher.setTextColor(Color.parseColor("#263238"));
                tvDone.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userType="teacher";
                btnStudent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_inactive_student, 0, 0, 0);
                btnTeacher.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_teacher, 0, 0, 0);
                btnStudent.setBackground(getResources().getDrawable(R.drawable.ic_in_active));
                btnTeacher.setBackground(getResources().getDrawable(R.drawable.ic_btn_one));
                btnStudent.setTextColor(Color.parseColor("#263238"));
                btnTeacher.setTextColor(getResources().getColor(R.color.white));
                tvDone.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(userType))
                {
                    dialog.dismiss();
                    Toast.makeText(context, "Choice is "+userType, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Please choose at-least one user...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoginFn() {
        if (TextUtils.isEmpty(etName.getText().toString()))
        {

        }
        else if (TextUtils.isEmpty(etEmail.getText().toString()))
        {

        }
        else if (TextUtils.isEmpty(etPassword.getText().toString()))
        {

        }
        else {
            ShowDiag();
        //    DoWork();
       //     Call<Response> call=apiInterface.createUser();

        }
    }

    private void DoWork() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
    }

    private void IvLoginLogic() {
        ivLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = null;
                mFragment = new LoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mFragment).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }
}