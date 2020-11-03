package in.kay.edvora.Views.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import in.kay.edvora.R;
import in.kay.edvora.Utils.CustomToast;
import in.kay.edvora.Views.Fragments.ClassFragment;
import in.kay.edvora.Views.Fragments.HomeFragment;
import in.kay.edvora.Views.Fragments.LibraryFragment;
import in.kay.edvora.Views.Fragments.ProfileFragment;
import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {
    AnimatedBottomBar bottomBar;
    Fragment mFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar = findViewById(R.id.bottom_bar);
        SwitchFragement();
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NotNull AnimatedBottomBar.Tab tab1) {
                switch (i1) {
                    case 0:
                        mFragment = new HomeFragment();
                        SwitchFragement();
                        break;
                    case 1:
                        mFragment = new LibraryFragment();
                        SwitchFragement();
                        break;
                    case 2:
                        mFragment = new ClassFragment();
                        SwitchFragement();
                        break;
                    case 3:
                        mFragment = new ProfileFragment();
                        SwitchFragement();
                        break; }}

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {
            }
        });
    }


    private void SwitchFragement() {
        if (mFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mFragment).commit();
        } else {
            mFragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        ShowDialog();
    }

    private void ShowDialog() {
        View view = getLayoutInflater().inflate(R.layout.alert_diag, null);
        final AlertDialog alertbox = new AlertDialog.Builder(this)
                .setView(view)
                .show();
        Button yes, no;
        yes = alertbox.findViewById(R.id.btn_yes);
        no = alertbox.findViewById(R.id.btn_no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseApp();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertbox.dismiss();
            }
        });
    }

    private void CloseApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    public void Fab(View view) {
        Toast.makeText(this, "Fab clicked", Toast.LENGTH_SHORT).show();
        CustomToast customToast=new CustomToast();
        customToast.ShowToast(this,"We are on Home base auth activity.");
    }
}