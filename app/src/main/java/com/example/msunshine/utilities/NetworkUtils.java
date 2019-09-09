package com.example.msunshine.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String STATIC_WEATHER_DATA =
            "https://andfun-weather.udacity.com/staticweather";

    private static final String QUERY_PARAM = "q";
    private static final String FORMAT_PARAM = "mode";
    private static final String DAYS_PARAM = "cnt";
    private static final String UNITS_PARAM = "units";

    private static final String format = "json";
    private static final String numDays = "14";
    private static final String units = "metric";

    public static URL buildUrl(String query) {
        Uri uri = Uri.parse(STATIC_WEATHER_DATA).buildUpon()
                .appendQueryParameter(QUERY_PARAM, query)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(DAYS_PARAM, numDays)
                .appendQueryParameter(UNITS_PARAM, units)
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
}
