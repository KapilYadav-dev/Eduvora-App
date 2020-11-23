package in.kay.edvora.Views.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.kay.edvora.Adapter.QuestionSearchAdapter;
import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Models.QuestionsModel;
import in.kay.edvora.R;
import in.kay.edvora.Views.Activity.UploadLibrary;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibraryFragment extends Fragment {
    Context context;
    View view;
    ImageView search;
    ImageView fab;
    Toolbar toolbar;
    Dialog dialog;
    QuestionSearchAdapter questionSearchAdapter;

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

        });
        fab.setOnClickListener(view -> {
            startActivity(new Intent(context, UploadLibrary.class));
            Animatoo.animateSlideUp(context);
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