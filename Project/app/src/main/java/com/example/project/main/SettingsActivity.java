package com.example.project.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.project.R;
import com.example.project.control.SettingsViewModel;
import com.example.project.main.fragments.settings.SettingsMainFragment;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements  PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, FragmentManager.OnBackStackChangedListener {
    SettingsViewModel viewModel;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fm = getSupportFragmentManager();
        displayFragment(new SettingsMainFragment());
        viewModel = new SettingsViewModel();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        fm.addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            shouldGoBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());

        displayFragment(fragment);
        return true;
    }

    public void shouldGoBack(){
        boolean canGoBack = fm.getBackStackEntryCount()>0;
        if(canGoBack) fm.popBackStack();
    }

    void displayFragment(Fragment frag) {
                fm
                .beginTransaction()
                .replace(R.id.frame_layout_settings, frag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        if(fm.getBackStackEntryCount()==0){
            finish();
        }
    }
}