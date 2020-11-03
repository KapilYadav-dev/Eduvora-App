package in.kay.edvora.Views.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import in.kay.edvora.Adapter.HomeFeedAdapter;
import in.kay.edvora.Models.HomeModel;
import in.kay.edvora.R;
import in.kay.edvora.ViewModel.HomeViewModel;

public class HomeFragment extends Fragment {
    HomeViewModel homeViewModel;
    Context context;
    View view;
    RecyclerView recyclerView;
    HomeFeedAdapter adapter;
    ShimmerFrameLayout shimmerFrameLayout;
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
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        homeViewModel = ViewModelProviders.of((FragmentActivity) context).get(HomeViewModel.class);
        LoadData();
    }

    private void LoadData() {
        homeViewModel.getFeed(context).observe((LifecycleOwner) context, new Observer<List<HomeModel>>() {
            @Override
            public void onChanged(List<HomeModel> homeModels) {
                adapter = new HomeFeedAdapter(homeModels, context);
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
}