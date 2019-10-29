package com.example.msunshine;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.msunshine.utilities.DataFormatUtils;

public class SettingFragment extends PreferenceFragmentCompat implements
        Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {

//        关联xml资源文件，该方法也可行
//        addPreferencesFromResource(R.xml.pref_general);
        setPreferencesFromResource(R.xml.pref_general, rootKey);

        //监听EditText
        Preference cityPreference = findPreference(getString(R.string.pref_city_key));
        if (cityPreference != null)
            cityPreference.setOnPreferenceChangeListener(this);

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
            return DataFormatUtils.isChinese(getContext(), value);
        }
        return true;
    }
}
