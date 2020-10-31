package in.kay.edvora.Views.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.pixplicity.easyprefs.library.Prefs;

import in.kay.edvora.R;
import in.kay.edvora.Views.Fragments.StudentDetailFragment;

public class Landing extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_landing);
            YoYo.with(Techniques.Bounce)
                    .duration(2000)
                    .repeat(YoYo.INFINITE)
                    .playOn(findViewById(R.id.anim));
    }

    public void SignUpFragment(View view) {
        startCustomIntent("SignUp");
    }

    private void startCustomIntent(String string) {
        Intent intent=new Intent(this,BaseAuth.class);
        intent.putExtra("from",string);
        startActivity(intent);
    }

    public void LogInFragment(View view) {
        startCustomIntent("LogIn");
    }
}