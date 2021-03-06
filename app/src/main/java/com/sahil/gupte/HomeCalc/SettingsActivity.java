package com.sahil.gupte.HomeCalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.sahil.gupte.HomeCalc.Fragments.Dialogs.SpinnerSortDialogFragment;
import com.sahil.gupte.HomeCalc.Utils.CurrencyUtils;
import com.sahil.gupte.HomeCalc.Utils.ThemeUtils;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this, getApplicationContext());
        setContentView(R.layout.settings_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        final Boolean[] dark = new Boolean[1];
        Object accent;

        private void setTheme() {
            if (accent.equals("Blue")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.BLUEDARK, Objects.requireNonNull(getContext()));
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.BLUE, Objects.requireNonNull(getContext()));
                }

            } else if (accent.equals("Orange")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.ORANGEDARK, Objects.requireNonNull(getContext()));
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.ORANGE, Objects.requireNonNull(getContext()));
                }

            } else if (accent.equals("Yellow")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.YELLOWDARK, Objects.requireNonNull(getContext()));
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.YELLOW, Objects.requireNonNull(getContext()));
                }

            } else if (accent.equals("Green")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.GREENDARK, Objects.requireNonNull(getContext()));
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.GREEN, Objects.requireNonNull(getContext()));
                }

            } else if (accent.equals("Cyan")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.CYANDARK, Objects.requireNonNull(getContext()));
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.CYAN, Objects.requireNonNull(getContext()));
                }

            } else if (accent.equals("Pink")) {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.PINKDARK, Objects.requireNonNull(getContext()));
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.PINK, Objects.requireNonNull(getContext()));
                }
            } else {
                if (dark[0]) {
                    ThemeUtils.changeToTheme(ThemeUtils.CYANDARK, Objects.requireNonNull(getContext()));
                } else {
                    ThemeUtils.changeToTheme(ThemeUtils.CYAN, Objects.requireNonNull(getContext()));
                }
            }
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            final SwitchPreference dark_mode = findPreference("dark_mode");
            ListPreference red = findPreference("accent");
            assert dark_mode != null;
            dark[0] = dark_mode.isChecked();
            ThemeUtils.PutDark(dark[0], Objects.requireNonNull(getContext()));
            assert red != null;
            accent = red.getValue();
            String TAG = "SettingsFragment";
            Log.d(TAG, "onCreatePreferences: "+dark[0]);
            dark_mode.setOnPreferenceChangeListener((preference, newValue) -> {
                dark[0] = Boolean.parseBoolean(newValue.toString());
                ThemeUtils.PutDark(dark[0], Objects.requireNonNull(getContext()));
                setTheme();
                return true;
            });

            red.setOnPreferenceChangeListener((preference, newValue) -> {
                accent = newValue;
                setTheme();
                return true;
            });

            SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("Currency", 0);
            SharedPreferences.Editor editor = pref.edit();

            ListPreference defaultCurrency = findPreference("currency");
            assert defaultCurrency != null;
            CurrencyUtils.defaultCurrency = defaultCurrency.getValue();

            defaultCurrency.setOnPreferenceChangeListener((preference, newValue) -> {
                CurrencyUtils.defaultCurrency = newValue.toString();
                editor.putString("defaultCurrency", CurrencyUtils.defaultCurrency);
                editor.apply();
                return true;
            });

            Preference button = findPreference("spinnerSort");
            button.setOnPreferenceClickListener(preference -> {
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SpinnerSortDialogFragment sortDialogFragment = new SpinnerSortDialogFragment();
                sortDialogFragment.show(ft, "dialog");
                return true;
            });

        }
    }
}