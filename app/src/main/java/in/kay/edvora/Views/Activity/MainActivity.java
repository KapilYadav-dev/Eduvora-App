package in.kay.edvora.Views.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Fragment mFragment = null;
    ImageView tabHome,tabClass,tabLibrary,tabProfile;
    RelativeLayout rlHome,rlClass,rlLib,rlProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initz();
        rlHome.setOnClickListener(this::onClick);
        rlProfile.setOnClickListener(this::onClick);
        rlClass.setOnClickListener(this::onClick);
        rlLib.setOnClickListener(this::onClick);
        SwitchFragement();
    }

    private void Initz() {
        tabHome=findViewById(R.id.homeTab);
        rlHome=findViewById(R.id.rl_homeTab);
        tabClass=findViewById(R.id.classroomTab);
        rlClass=findViewById(R.id.rl_class);
        tabLibrary=findViewById(R.id.libraryTab);
        rlLib=findViewById(R.id.rl_library);
        tabProfile=findViewById(R.id.profileTab);
        rlProfile=findViewById(R.id.rl_profile);

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
        yes.setOnClickListener(view1 -> CloseApp());
        no.setOnClickListener(view12 -> alertbox.dismiss());
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_homeTab:
                tabHome.setImageResource(R.drawable.ic_selected_home);
                tabProfile.setImageResource(R.drawable.ic_unselected_profile);
                tabClass.setImageResource(R.drawable.ic_unselected_classroom);
                tabLibrary.setImageResource(R.drawable.ic_unselected_library);
                mFragment = new HomeFragment();
                SwitchFragement();
                break;
            case R.id.rl_library:
                tabHome.setImageResource(R.drawable.ic_unselected_home);
                tabProfile.setImageResource(R.drawable.ic_unselected_profile);
                tabClass.setImageResource(R.drawable.ic_unselected_classroom);
                tabLibrary.setImageResource(R.drawable.ic_selected_library);
                mFragment = new LibraryFragment();
                SwitchFragement();
                break;
            case R.id.rl_class:
                tabHome.setImageResource(R.drawable.ic_unselected_home);
                tabProfile.setImageResource(R.drawable.ic_unselected_profile);
                tabClass.setImageResource(R.drawable.ic_selected_classroom);
                tabLibrary.setImageResource(R.drawable.ic_unselected_library);
                String str = Prefs.getString("userType", "student");
                if (str.equalsIgnoreCase("student"))
                    mFragment = new StudentClassroom();
                else
                    mFragment = new FacultyClassroom();
                SwitchFragement();
                break;
            case R.id.rl_profile:
                tabHome.setImageResource(R.drawable.ic_unselected_home);
                tabProfile.setImageResource(R.drawable.ic_selected_profile);
                tabClass.setImageResource(R.drawable.ic_unselected_classroom);
                tabLibrary.setImageResource(R.drawable.ic_unselected_library);
                mFragment = new ProfileFragment();
                SwitchFragement();
                break;

        }
    }
}