package com.example.project.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.preference.CheckBoxPreference;
import androidx.preference.DialogPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toolbar;

import com.example.project.R;
import com.example.project.control.SettingsViewModel;
import com.example.project.data.model.SettingsModel;
import com.example.project.main.fragments.SettingsFragment;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity implements  PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, FragmentManager.OnBackStackChangedListener , Preference.OnPreferenceChangeListener {
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fm = getSupportFragmentManager();
        displayFragment(new SettingsFragment());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        fm.addOnBackStackChangedListener(this);

    }


    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        displayFragment(fragment);
        return true;
    }

    void displayFragment(Fragment frag) {
        fm
                .beginTransaction()
                .replace(R.id.frame_layout_settings, frag)
                .addToBackStack(null)
                .commit();
    }



    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            shouldGoBack();
        }
        return super.onOptionsItemSelected(item);
    }


    public void shouldGoBack() {
        boolean canGoBack = fm.getBackStackEntryCount() > 0;
        if (canGoBack) fm.popBackStack();
    }


    @Override
    public void onBackStackChanged() {
        if (fm.getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        System.out.println("test");
        return false;
    }



}