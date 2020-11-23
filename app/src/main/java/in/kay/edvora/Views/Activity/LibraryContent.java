package in.kay.edvora.Views.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import in.kay.edvora.Adapter.TabAdapter;
import in.kay.edvora.R;

public class LibraryContent extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    TabAdapter tabAdapter;
    TabItem tabBooks, tabNotes, tabPapers;
    String string;
    TextView subjectName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_content);
        string=getIntent().getStringExtra("Subject");
        initz();
    }

    private void initz() {
        subjectName = findViewById(R.id.subjectName);
        subjectName.setText(string);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabBooks = findViewById(R.id.books);
        tabNotes = findViewById(R.id.notes);
        tabPapers = findViewById(R.id.papers);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2) {
                    tabAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void OnBack(View view) {
        onBackPressed();
    }
}