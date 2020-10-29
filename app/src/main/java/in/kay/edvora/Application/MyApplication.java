package in.kay.edvora.Application;

import android.app.Application;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyPrefInitz();
    }

    private void EasyPrefInitz() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName("Edvora")
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
