package com.example.msunshine.utilities;

import android.annotation.TargetApi;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class NetworkUtils {

    private static final String CHINA_CURRENT_WEATHER_DATA =
            "https://www.mxnzp.com/api/weather/current/";


    private static final String CHINA_FORECAST_WEATHER_DATA =
            "https://www.mxnzp.com/api/weather/forecast/";

    /**
     * @param query_city：必须是中文
     * @return 返回网址URL
     */
    public static URL mBuildUrl(String query_city) {

        Uri uri = Uri.parse(CHINA_FORECAST_WEATHER_DATA).buildUpon()
                .appendPath(query_city)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext())
                return scanner.next();
            else
                return null;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * OkHttp works on Android 5.0+ (API level 21+) and on Java 8+.
     * <p>
     * The OkHttp 3.12.x branch supports Android 2.3+ (API level 9+) and Java 7+.
     * These platforms lack support for TLS 1.2 and should not be used.
     * But because upgrading is difficult we will backport critical fixes to the 3.12.x branch
     * through December 31, 2020.
     **/
    @TargetApi(21)
    public static String getResponseFromOkHttp(URL url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
