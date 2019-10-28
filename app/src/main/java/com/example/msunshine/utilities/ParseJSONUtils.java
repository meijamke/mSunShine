package com.example.msunshine.utilities;


import android.content.Context;

import com.example.msunshine.data.NetworkData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJSONUtils {

    public static final int TYPE_WEATHER_SUMMARY = 0;
    public static final int TYPE_WEATHER_DETAIL = 1;

    /**
     * 返回数据的参考格式
     * {
     * "code": 1,
     * "msg": "数据返回成功",
     * "data": {
     * "address": "广东省 广州市",
     * "cityCode": "440100",
     * "temp": "28℃",
     * "weather": "多云",
     * "windDirection": "东",
     * "windPower": "≤3级",
     * "humidity": "84%",
     * "reportTime": "2019-09-09 12:15:02"
     * }
     * }
     *
     */
//    public static String getCurrentWeatherStringFromJSON(String forecastJsonStr) throws JSONException {
//        //数据成功返回时code=1，失败时coed=0
//        final String RESULT_CODE = "code";
//        //数据请求结果的描述信息
//        final String RESULT_MESSAGE = "msg";
//        //返回的数据
//        final String RESULT_DATA = "data";
//
//        final String ADDRESS = "address";
//        final String TEMPERATURE = "temp";
//        final String WEATHER = "weather";
//        final String WIND_DIRECTION = "windDirection";
//        final String WIND_POWER = "windPower";
//        final String HUMIDITY = "humidity";
//        final String DATE = "reportTime";
//
//        String parsedWeatherDetail;
//        JSONObject jsonObject = new JSONObject(forecastJsonStr);
//
//        //判断是否拿到了数据
//        if (jsonObject.has(RESULT_CODE)) {
//            int code = jsonObject.getInt(RESULT_CODE);
//            if (code == 0) {
//                parsedWeatherDetail = jsonObject.getString(RESULT_MESSAGE) + "\n";
//                return parsedWeatherDetail;
//            }
//        }
//
//        JSONObject data = jsonObject.getJSONObject(RESULT_DATA);
//
//        String city = data.getString(ADDRESS);
//        String temp = data.getString(TEMPERATURE);
//        String weather = data.getString(WEATHER);
//        String windDirection = data.getString(WIND_DIRECTION);
//        String windPower = data.getString(WIND_POWER);
//        String humidity = data.getString(HUMIDITY);
//        String date = data.getString(DATE).split(" ")[0];
//
//        parsedWeatherDetail =
//                city + "\n" +
//                temp + "\n" +
//                weather + "\n" +
//                windDirection + "\n" +
//                windPower + "\n" +
//                humidity + "\n" +
//                date;
//        return parsedWeatherDetail;
//    }

    /**
     * 返回数据的参考格式
     * {
     * "code": 1,
     * "msg": "数据返回成功",
     * "data": {
     * "address": "北京市",
     * "cityCode": "110000",
     * "reportTime": "2019-09-09 13:20:56",
     * "forecasts": [
     * {
     * "date": "2019-09-09",
     * "dayOfWeek": "1",
     * "dayCondition": "小雨",
     * "nightCondition": "小雨",
     * "dayTemp": "32℃",
     * "nightTemp": "21℃",
     * "dayWindDirection": "东北",
     * "nightWindDirection": "东北",
     * "dayWindPower": "4级",
     * "nightWindPower": "4级"
     * },
     * ...
     * ]
     * }
     * }
     */


    public static String[] getForecastWeatherStringFromJSON(Context context, String forecastJsonStr, int dataType) throws JSONException {

        //数据成功返回时code=1，失败时coed=0
        final String RESULT_CODE = "code";

        //数据请求结果的描述信息
        final String RESULT_MESSAGE = "msg";

        //返回的数据
        final String RESULT_DATA = "data";

        final String ADDRESS = "address";
        final String FORECAST = "forecasts";

        final String DATE = "date";
        final String DAY_OF_WEEK = "dayOfWeek";
        final String DAY_WEATHER = "dayWeather";
        final String NIGHT_WEATHER = "nightWeather";
        final String DAY_TEMP = "dayTemp";
        final String NIGHT_TEMP = "nightTemp";
        final String DAY_WIND_DIRECTION = "dayWindDirection";
        final String NIGHT_WIND_DIRECTION = "nightWindDirection";
        final String DAY_WIND_POWER = "dayWindPower";
        final String NIGHT_WIND_POWER = "nightWindPower";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        //判断是否拿到了数据
        if (forecastJson.has(RESULT_CODE)) {
            int code = forecastJson.getInt(RESULT_CODE);
            if (code == 0)
                return new String[]{""};
        }

        JSONObject data = forecastJson.getJSONObject(RESULT_DATA);

        String city = data.getString(ADDRESS);
        JSONArray forecast = data.getJSONArray(FORECAST);

        String[] parsedWeatherData = new String[forecast.length()];

        for (int i = 0; i < forecast.length(); i++) {
            JSONObject dayForecast = forecast.getJSONObject(i);

            String date = dayForecast.getString(DATE).trim();
            String dayOfWeek = dayForecast.getString(DAY_OF_WEEK).trim();
            String dayCondition = dayForecast.getString(DAY_WEATHER).trim();
            String nightCondition = dayForecast.getString(NIGHT_WEATHER).trim();
            String dayTemp = dayForecast.getString(DAY_TEMP).trim();
            String nightTemp = dayForecast.getString(NIGHT_TEMP).trim();
            String dayWindDirection = dayForecast.getString(DAY_WIND_DIRECTION).trim();
            String nightWindDirection = dayForecast.getString(NIGHT_WIND_DIRECTION).trim();
            String dayWindPower = dayForecast.getString(DAY_WIND_POWER).trim();
            String nightWindPower = dayForecast.getString(NIGHT_WIND_POWER).trim();

            if (dataType == TYPE_WEATHER_SUMMARY)
                parsedWeatherData[i] =
                        date + "\n" +
                                NetworkData.getWeekName(context, dayOfWeek) + "\n" +
                                dayCondition + "\n" +
                                nightCondition + "\n" +
                                dayTemp + "\n" +
                                nightTemp;
            else if (dataType == TYPE_WEATHER_DETAIL)
                parsedWeatherData[i] =
                        date + "\n" +
                                NetworkData.getWeekName(context, dayOfWeek) + "\n" +
                                dayCondition + "\n" +
                                nightCondition + "\n" +
                                dayTemp + "\n" +
                                nightTemp + "\n" +
                                dayWindDirection + "\n" +
                                nightWindDirection + "\n" +
                                dayWindPower + "\n" +
                                nightWindPower;
        }
        return parsedWeatherData;
    }

}
