package com.sahil.gupte.HomeCalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.sahil.gupte.HomeCalc.Utils.ThemeUtils;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this, getApplicationContext());
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        final Boolean[] dark = new Boolean[1];
        Object accent;

        private String TAG = "SettingsFragment";

        private void setTheme() {
            if (accent.equals("Blue")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.BLUEDARK, getContext());
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.BLUE, getContext());
                }

            } else if (accent.equals("Orange")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.ORANGEDARK, getContext());
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.ORANGE, getContext());
                }

            } else if (accent.equals("Yellow")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.YELLOWDARK, getContext());
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.YELLOW, getContext());
                }

            } else if (accent.equals("Green")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.GREENDARK, getContext());
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.GREEN, getContext());
                }

            } else if (accent.equals("Cyan")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.CYANDARK, getContext());
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.CYAN, getContext());
                }

            } else if (accent.equals("Pink")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.PINKDARK, getContext());
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.PINK, getContext());
                }
            }
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            final SwitchPreference dark_mode = findPreference("dark_mode");
            ListPreference red = findPreference("accent");
            dark[0] = dark_mode.isChecked();
            accent = red.getValue();
            Log.d(TAG, "onCreatePreferences: "+dark[0]);
            dark_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    dark[0] = Boolean.parseBoolean(newValue.toString());
                    ThemeUtils.PutDark(dark[0], getContext());
                    setTheme();
                    return true;
                }
            });

            red.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d(TAG, "onPreferenceChange: " + dark[0] + ", " + newValue.toString());
                    Log.d(TAG, "onPreferenceChange: " + (newValue.equals("Blue")));
                    accent = newValue;
                    setTheme();
                    return true;
                }
            });
        }
    }
}