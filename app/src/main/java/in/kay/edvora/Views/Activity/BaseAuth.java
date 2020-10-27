package in.kay.edvora.Views.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import in.kay.edvora.R;
import in.kay.edvora.Views.Fragments.LoginFragment;
import in.kay.edvora.Views.Fragments.SignupFragment;

public class BaseAuth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_auth);
        String from = getIntent().getStringExtra("from");
        Fragment mFragment = null;
        if (from.equalsIgnoreCase("Login")) {
            mFragment = new LoginFragment();
        } else {
            mFragment = new SignupFragment();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mFragment).commit();
    }
}