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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;
import java.util.function.Predicate;

import in.kay.edvora.Adapter.FindLibraryAdapter;
import in.kay.edvora.Models.FindLibraryModel;
import in.kay.edvora.R;
import in.kay.edvora.ViewModel.LibraryViewModel;

public class NotesFragment extends Fragment {

    Context context;
    View view;
    RecyclerView recyclerView;
    FindLibraryAdapter findLibraryAdapter;
    LibraryViewModel libraryViewModel;

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
        libraryViewModel = ViewModelProviders.of((FragmentActivity) context).get(LibraryViewModel.class);
        DoWork();
    }

    private void DoWork() {
        libraryViewModel.GetData(context).observe(getViewLifecycleOwner(), findLibraryModels -> {
            List<FindLibraryModel> filteredList = Stream.of(findLibraryModels).filter(item -> item.getSubject().equalsIgnoreCase( Prefs.getString("subject",""))).collect(Collectors.toList());
            List<FindLibraryModel> finalList = Stream.of(filteredList).filter(item -> item.getType().equalsIgnoreCase("Notes")).collect(Collectors.toList());
            findLibraryAdapter = new FindLibraryAdapter(finalList, context);
            recyclerView.setAdapter(findLibraryAdapter);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }
}