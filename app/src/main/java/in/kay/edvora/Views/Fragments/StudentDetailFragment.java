package in.kay.edvora.Views.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentDetailFragment extends Fragment {
    SpinnerDialog spinnerDialog;
    Context mcontext;
    View view;
    ArrayList<String> colleges = new ArrayList<>();
    ArrayList<String> year = new ArrayList<>();
    ArrayList<String> branch = new ArrayList<>();
    TextView etCollege, etBranch, etYear;
    String choosen;
    Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_detail, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        Initz();
    }

    private void Initz() {
        etCollege = view.findViewById(R.id.et_college);
        etBranch = view.findViewById(R.id.et_branch);
        etYear = view.findViewById(R.id.et_year);
        etCollege = view.findViewById(R.id.et_college);
        btnSave=view.findViewById(R.id.btn_save);
        etCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopUpCollege();
            }
        });
        etBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopUpBranch();
            }
        });
        etYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopUpYear();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Savedata();
            }
        });
    }

    private void Savedata() {
        final ProgressDialog pd = new ProgressDialog(mcontext);
        pd.setMax(100);
        pd.setMessage("Saving...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.updateUser(etCollege.getText().toString(),etBranch.getText().toString(),2,"Bearer " + Prefs.getString("accessToken",""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(mcontext, "Code is "+response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void ShowPopUpBranch() {
        final ProgressDialog pd = new ProgressDialog(mcontext);
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
        Call<ResponseBody> call = apiInterface.getBranches();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        JSONObject jsonObject = new JSONObject(str);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String clgname = object.getString("name");
                            branch.add(clgname); }
                        DoWorkBranch();
                    } else {
                        //Server error Response
                    }
                    pd.dismiss();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Server error Response
                pd.dismiss();
                Toast.makeText(mcontext, "Error is " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DoWorkBranch() {
        spinnerDialog = new SpinnerDialog((Activity) mcontext, branch, "Select your Branch", R.style.DialogAnimations, "");// With 	Animation
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default
        spinnerDialog.setItemColor(Color.parseColor("#263238"));
        spinnerDialog.showSpinerDialog();
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                choosen = item;
                branch.clear();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etBranch.setText(choosen);
                        etBranch.setTextColor(getResources().getColor(R.color.colorBlack));
                    }
                });
            }
        });
    }

    private void ShowPopUpYear() {
        year.add("1st Year");
        year.add("2nd Year");
        year.add("3rd Year");
        year.add("Final Year");
        spinnerDialog = new SpinnerDialog((Activity) mcontext, year, "Select your year", R.style.DialogAnimations, "");// With 	Animation
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default
        spinnerDialog.setItemColor(Color.parseColor("#263238"));
        spinnerDialog.showSpinerDialog();
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                choosen = item;
                year.clear();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etYear.setText(choosen);
                        etYear.setTextColor(getResources().getColor(R.color.colorBlack));
                    }
                });
            }
        });
    }

    private void ShowPopUpCollege() {
        final ProgressDialog pd = new ProgressDialog(mcontext);
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
        Call<ResponseBody> call = apiInterface.getColleges();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        JSONObject jsonObject = new JSONObject(str);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String clgname = object.getString("name");
                            colleges.add(clgname);
                        }
                        ArrayList<String> arrayList = new ArrayList<>();
                        DoWorkCollege();
                    } else {
                        //Server error Response
                    }
                    pd.dismiss();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Server error Response
                pd.dismiss();
                Toast.makeText(mcontext, "Error is " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DoWorkCollege() {
        spinnerDialog = new SpinnerDialog((Activity) mcontext, colleges, "Select your college", R.style.DialogAnimations, "");// With 	Animation
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default
        spinnerDialog.setItemColor(Color.parseColor("#263238"));
        spinnerDialog.showSpinerDialog();
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                choosen = item;
                colleges.clear();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etCollege.setText(choosen);
                        etCollege.setTextColor(getResources().getColor(R.color.colorBlack));
                    }
                });
            }
        });
    }

}