package in.kay.edvora.Views.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import in.kay.edvora.Adapter.HomeFeedAdapter;
import in.kay.edvora.Models.HomeModel;
import in.kay.edvora.R;
import in.kay.edvora.ViewModel.HomeViewModel;
import in.kay.edvora.Views.Activity.AskQuestion;

public class HomeFragment extends Fragment {
    HomeViewModel homeViewModel;
    Context context;
    View view;
    RecyclerView recyclerView;
    HomeFeedAdapter adapter;
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView fab;
    List<HomeModel> initlist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        fab=view.findViewById(R.id.iv_ask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AskQuestion.class));
                Animatoo.animateSlideUp(context);
            }
        });
        if (Prefs.getString("userType","student").equalsIgnoreCase("teacher"))
        {
            fab.setVisibility(View.GONE);
        }
        else {
            fab.setVisibility(View.VISIBLE);
        }
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new HomeFeedAdapter(initlist, context);
        recyclerView.setAdapter(adapter);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        homeViewModel = ViewModelProviders.of((FragmentActivity) context).get(HomeViewModel.class);
        LoadData();
        ShowHideFab();
    }

    private void ShowHideFab() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0){
                    fab.animate().translationY(view.getHeight());
                } else{
                    fab.animate().translationY(0);
                }

            }
        });
    }

    private void LoadData() {
            homeViewModel.getFeed(context).observe(getViewLifecycleOwner(), new Observer<List<HomeModel>>() {
            @Override
            public void onChanged(List<HomeModel> homeModels) {
                shimmerFrameLayout.setVisibility(View.GONE);
                if (homeModels !=null)
                {
                    initlist=homeModels;
                    adapter.setNewData(homeModels,context);
                }
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
}