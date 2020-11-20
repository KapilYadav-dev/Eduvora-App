package in.kay.edvora.Views.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pixplicity.easyprefs.library.Prefs;

import in.kay.edvora.R;
import in.kay.edvora.Views.Fragments.FacultyClassroom;
import in.kay.edvora.Views.Fragments.HomeFragment;
import in.kay.edvora.Views.Fragments.LibraryFragment;
import in.kay.edvora.Views.Fragments.ProfileFragment;
import in.kay.edvora.Views.Fragments.StudentClassroom;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class MainActivity extends AppCompatActivity {
    BottomNavigation bottomBar;
    Fragment mFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar = findViewById(R.id.bottom_bar);
        SwitchFragement();
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "gilroybold.ttf");
        bottomBar.setDefaultTypeface(typeface);
        bottomBar.setMenuItemSelectionListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(int i, int i1, boolean b) {
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
                        String str = Prefs.getString("userType", "student");
                        if (str.equalsIgnoreCase("student"))
                            mFragment = new StudentClassroom();
                        else
                            mFragment = new FacultyClassroom();
                        SwitchFragement();
                        break;
                    case 3:
                        mFragment = new ProfileFragment();
                        SwitchFragement();
                        break;
                }
            }

            @Override
            public void onMenuItemReselect(int i, int i1, boolean b) {

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}