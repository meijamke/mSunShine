package com.example.msunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherDataText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherDataText = findViewById(R.id.tv_weather_data);

    }

//    public class FetchFromUrl extends AsyncTask<String,Void,String[]>{
//
//        @Override
//        protected String[] doInBackground(String...loc){
//            String location=loc[0];
//            try{
//                URL url= NetworkUtils.buildUrl(location);
//                String result=NetworkUtils.getResponseFromHttpUrl(url);
//
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    }
}
