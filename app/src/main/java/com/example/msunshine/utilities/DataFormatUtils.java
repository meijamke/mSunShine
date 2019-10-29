package com.example.msunshine.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.msunshine.R;

public class DataFormatUtils {

    public static boolean isChinese(Context context, String value) {
        //判断是否为汉字
        char[] chars = value.toCharArray();
        for (char ch : chars)
            if (ch < 0x4e00 || ch > 0x9fa5) {
                Toast.makeText(context, context.getString(R.string.edit_hint), Toast.LENGTH_LONG).show();
                return false;
            }
        return true;
    }
}
