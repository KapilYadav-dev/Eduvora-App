package in.kay.edvora.Views.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import in.kay.edvora.Adapter.BookmarkTabAdapter;
import in.kay.edvora.Adapter.TabAdapter;
import in.kay.edvora.R;

public class BookmarkActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    BookmarkTabAdapter tabAdapter;
    TabItem tabMaterials, tabSubjects, tabFeeds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        initz();
    }

    private void initz() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabMaterials = findViewById(R.id.materials);
        tabSubjects = findViewById(R.id.subjects);
        tabFeeds = findViewById(R.id.feeds);
        tabAdapter = new BookmarkTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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