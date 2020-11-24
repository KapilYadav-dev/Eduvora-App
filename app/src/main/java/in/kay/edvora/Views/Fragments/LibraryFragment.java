package in.kay.edvora.Views.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import in.kay.edvora.Adapter.MySubjetsAdapter;
import in.kay.edvora.Adapter.QuestionSearchAdapter;
import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Application.MyApplication;
import in.kay.edvora.Models.QuestionsModel;
import in.kay.edvora.R;
import in.kay.edvora.Utils.CustomToast;
import in.kay.edvora.Views.Activity.UploadLibrary;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class LibraryFragment extends Fragment {
    Context context;
    View view;
    ImageView search;
    ImageView fab, addSubject;
    Toolbar toolbar;
    Dialog dialog;
    QuestionSearchAdapter questionSearchAdapter;
    RecyclerView recyclerView;
    MySubjetsAdapter mySubjetsAdapter;
    ArrayList<String> subject = new ArrayList<>();
    SpinnerDialog spinnerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        Initz();
    }

    private void Initz() {
        dialog = new Dialog(context);
        addSubject = view.findViewById(R.id.iv_add_subject);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        GetSubjects();
        fab = view.findViewById(R.id.fab);
        search = view.findViewById(R.id.search);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> {
            SearchView searchView;
            RecyclerView recyclerView;
            dialog.setContentView(R.layout.search_view_dialog);
            searchView = dialog.findViewById(R.id.searchBar);
            searchView.setIconified(false);
            recyclerView = dialog.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.show();
            TextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
            Typeface myCustomFont = Typeface.createFromAsset(dialog.getContext().getAssets(), "gilroymedium.ttf");
            searchText.setTypeface(myCustomFont);
            LoadData(recyclerView);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    questionSearchAdapter.getFilter().filter(newText);
                    return false;
                }
            });
            ImageView closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
            closeButton.setOnClickListener(v -> {
                dialog.dismiss();
            });

        });
        fab.setOnClickListener(view -> {
            startActivity(new Intent(context, UploadLibrary.class));
            Animatoo.animateSlideUp(context);
        });
        addSubject.setOnClickListener(view -> {
            SelectSubject();
        });
    }

    private void GetSubjects() {
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
                    mySubjetsAdapter = new MySubjetsAdapter(response.body(), context);
                    recyclerView.setAdapter(mySubjetsAdapter);
                } else if (response.code() == 502) {
                    MyApplication myApplication = new MyApplication();
                    myApplication.RefreshToken(Prefs.getString("refreshToken", ""), context);
                    GetSubjects();
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
    }

    private void SelectSubject() {
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
        Call<ResponseBody> call = apiInterface.getSubjects();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            subject.add(name);
                        }
                        DoSelectSubject();
                        pd.dismiss();
                    } else {
                        pd.dismiss();
                        CustomToast customToast = new CustomToast();
                        customToast.ShowToast(context, "Server down...");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(context, "Server down...");
            }
        });
    }

    private void DoSelectSubject() {
        spinnerDialog = new SpinnerDialog((Activity) context, subject, "Select subject", R.style.DialogAnimations, "");// With 	Animation
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default
        spinnerDialog.setItemColor(Color.parseColor("#263238"));
        spinnerDialog.showSpinerDialog();
        spinnerDialog.bindOnSpinerListener((item, position) -> {
            subject.clear();
            runOnUiThread(() -> {
                SaveSubject(item);
            });
        });
    }

    private void SaveSubject(String item) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setMax(100);
        pd.setMessage("Adding...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.addSubject(item, "Bearer " + Prefs.getString("accessToken", ""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    CustomToast customToast = new CustomToast();
                    customToast.ShowToast(context, "Added Subject Successfully");
                } else if (response.code() == 502) {
                    MyApplication myApplication = new MyApplication();
                    myApplication.RefreshToken(Prefs.getString("refreshToken", ""), context);
                    SaveSubject(item);
                } else {
                    pd.dismiss();
                    CustomToast customToast = new CustomToast();
                    customToast.ShowToast(context, "Subject already added...");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                CustomToast customToast = new CustomToast();
                customToast.ShowToast(context, "Failure Error occur...");
            }
        });
    }


    private void LoadData(RecyclerView recyclerView) {
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
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getSubjects();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    if (response.isSuccessful()) {
                        List<QuestionsModel> list = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            list.add(new QuestionsModel(name));
                        }
                        questionSearchAdapter = new QuestionSearchAdapter(list, context);
                        recyclerView.setAdapter(questionSearchAdapter);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
            }

        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

}