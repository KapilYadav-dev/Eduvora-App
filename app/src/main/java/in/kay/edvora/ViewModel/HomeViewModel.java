package in.kay.edvora.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.kay.edvora.Models.HomeModel;
import in.kay.edvora.Repository.HomeRepository;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<HomeModel>> homefeed;

    public LiveData<List<HomeModel>> getFeed(Context context) {
        if (homefeed == null) {
            homefeed = new MutableLiveData<List<HomeModel>>();
            HomeRepository hr = new HomeRepository(homefeed,context);
            hr.LoadFeed();
        }
        return homefeed;
    }
}
