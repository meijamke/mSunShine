package com.example.msunshine;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

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
}
