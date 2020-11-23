package in.kay.edvora.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.kay.edvora.Views.Fragments.BooksFragment;
import in.kay.edvora.Views.Fragments.NotesFragment;
import in.kay.edvora.Views.Fragments.PaperFragment;

public class TabAdapter extends FragmentPagerAdapter {
    int tabCount;

    public TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BooksFragment();
            case 1:
                return new NotesFragment();
            case 2:
                return new PaperFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
