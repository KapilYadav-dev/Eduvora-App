package in.kay.edvora.Views.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.kay.edvora.Adapter.FindLibraryAdapter;
import in.kay.edvora.Api.ApiInterface;
import in.kay.edvora.Models.FindLibraryBody;
import in.kay.edvora.Models.FindLibraryModel;
import in.kay.edvora.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BooksFragment extends Fragment {
    Context context;
    View view;
    RecyclerView recyclerView;
    FindLibraryAdapter findLibraryAdapter;
    String TAG="MERA_APP";
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        Initz();
    }

    private void Initz() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        Log.d(TAG, "Initz: ");
        DoWork();
    }

    private void DoWork() {
        Log.d(TAG, "DoWork: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<List<FindLibraryModel>> call = api.viewAllLibrary();
        call.enqueue(new Callback<List<FindLibraryModel>>() {
            @Override
            public void onResponse(Call<List<FindLibraryModel>> call, Response<List<FindLibraryModel>> response) {
                Log.d(TAG, "onResponse: "+response.code());
                if (response.isSuccessful()) {
                    List<FindLibraryModel> list;
                    list = response.body();
                    findLibraryAdapter=new FindLibraryAdapter(list,context);
                    recyclerView.setAdapter(findLibraryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<FindLibraryModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }
}