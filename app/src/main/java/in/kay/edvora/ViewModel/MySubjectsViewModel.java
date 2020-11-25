package in.kay.edvora.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.kay.edvora.Models.FindLibraryModel;
import in.kay.edvora.Repository.LibraryRepository;
import in.kay.edvora.Repository.MySubjectsRepository;

public class MySubjectsViewModel extends ViewModel {
    public MutableLiveData<List<String>> model;

    public LiveData<List<String>> GetData(Context context) {
        if (model == null) {
            model = new MutableLiveData<>();
            MySubjectsRepository mySubjectsRepository = new MySubjectsRepository(model, context);
            mySubjectsRepository.LoadMySubjects();
        }
        return model;
    }
}
