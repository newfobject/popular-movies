package com.newfobject.popularmovies.ui.activity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.newfobject.popularmovies.R;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_sort_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set listener to watch changes
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getString(getResources().getString(R.string.pref_sort_key), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();
        ListPreference listPreference = (ListPreference) preference;
        int prefIndex = listPreference.findIndexOfValue(stringValue);

        if (stringValue.equals(getString(R.string.pref_sort_default))) {
            preference.setSummary(getString(R.string.pref_sort_summary_popularity));
        } else {
            preference.setSummary(getString(R.string.pref_sort_summary_rating));
        }

        return true;
    }
}
