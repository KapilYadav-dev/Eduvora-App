package in.kay.edvora.Views.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.pixplicity.easyprefs.library.Prefs;

import in.kay.edvora.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        YoYo.with(Techniques.Bounce)
                .duration(2000)
                .repeat(YoYo.INFINITE)
                .playOn(findViewById(R.id.iv));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Prefs.getBoolean("isLoggedIn",false) && Prefs.getBoolean("isProfileComplete",false) )
                {
                    startActivity(new Intent(Splash.this,MainActivity.class));
                }else
                {
                    startActivity(new Intent(Splash.this,Landing.class));
                    finish();
                }
            }
        }, 4000);
    }
}