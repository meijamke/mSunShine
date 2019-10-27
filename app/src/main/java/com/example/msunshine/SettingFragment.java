package com.example.msunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        addPreferencesFromResource(R.xml.pref_general);

        PreferenceScreen prefScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();
        int count = prefScreen.getPreferenceCount();

        //初始化显示summary
        for (int i = 0; i < count; i++) {
            Preference pref = prefScreen.getPreference(i);
            if (!(pref instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(pref.getKey(), "");
                if (value != null)
                    setPreferenceSummary(pref, value);
            }
        }

        //监听EditText
        Preference cityPreference = findPreference(getString(R.string.pref_city_key));
        if (cityPreference != null)
            cityPreference.setOnPreferenceChangeListener(this);

    }

    /**
     * @param preference 除了checkBox外的preference都需要手动set summary
     * @param newValues  value of preference
     **/
    private void setPreferenceSummary(Preference preference, Object newValues) {
        String values = newValues.toString();
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(values);
            if (index >= 0)
                listPreference.setSummary(listPreference.getEntries()[index]);
        } else
            preference.setSummary(values);
    }

    /**
     * preference value每一次改变都会写入sharedPreference文件中
     * 每一次数值写入sharedPreference文件 后 都会调用onSharedPreferenceChanged，
     * 所以为了在数值写入文件的同时更新preference的summary，需要重写该方法
     **/
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference preference = findPreference(key);
        String value = sharedPreferences.getString(key, "");
        if (preference != null)
            if (!(preference instanceof CheckBoxPreference)) {
                if (value != null)
                    setPreferenceSummary(preference, value);
            }
    }

    /***
     * preference value每一次改变都会写入sharedPreference文件中
     * 每一次数值写入sharedPreference文件 前 都会调用onPreferenceChange，
     * 所以为了在数值写入文件前检查EditPreference的value是否为汉字，需要重写该方法
     *
     * 然后需要在onCreatePreference方法中注册监听该偏好
     * setOnPreferenceChangeListener
     * */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValues) {
        String key = preference.getKey();
        if (getString(R.string.pref_city_key).equals(key)) {
            String value = ((String) newValues).trim();
            //判断是否为汉字
            char[] chars = value.toCharArray();
            for (char ch : chars)
                if (ch < 0x4e00 || ch > 0x9fa5) {
                    Toast.makeText(getContext(), getString(R.string.edit_hint), Toast.LENGTH_LONG).show();
                    return false;
                }
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
