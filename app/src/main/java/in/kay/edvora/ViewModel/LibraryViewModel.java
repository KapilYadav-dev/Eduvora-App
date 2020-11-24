package in.kay.edvora.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.kay.edvora.Models.FindLibraryModel;
import in.kay.edvora.Repository.LibraryRepository;

public class LibraryViewModel extends ViewModel {
    public MutableLiveData<List<FindLibraryModel>> model;

    public LiveData<List<FindLibraryModel>> GetData(Context context) {
        if (model == null) {
            model = new MutableLiveData<>();
            LibraryRepository libraryRepository = new LibraryRepository(model, context);
            libraryRepository.LoadData();
        }
        return model;
    }
}
