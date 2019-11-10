package com.example.msunshine;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msunshine.data.MSunshinePreference;
import com.example.msunshine.data.WeatherContract;
import com.example.msunshine.sync.MSunshineSyncUtils;
import com.example.msunshine.utilities.DataFormatUtils;
import com.example.msunshine.utilities.ExplicitIntentActivityUtils;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements
        ForecastAdapter.OnClickListItemListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int ID_WEATHER_CURSOR = 1;

    public static final String[] MAIN_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_WEEK,
            WeatherContract.WeatherEntry.COLUMN_DAY_CONDITION,
            WeatherContract.WeatherEntry.COLUMN_NIGHT_CONDITION,
            WeatherContract.WeatherEntry.COLUMN_DAY_TEMP,
            WeatherContract.WeatherEntry.COLUMN_NIGHT_TEMP
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_WEEK = 1;
    public static final int INDEX_WEATHER_DAY_CONDITION = 2;
    public static final int INDEX_WEATHER_NIGHT_CONDITION = 3;
    public static final int INDEX_WEATHER_DAY_TEMP = 4;
    public static final int INDEX_WEATHER_NIGHT_TEMP = 5;

    private String mSearchCity;

    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView mErrorMsgDisplay;
    private ProgressBar mLoadingIndicator;

    private static boolean pref_edit_text_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        mRecyclerView = findViewById(R.id.rv_forecast);
        mForecastAdapter = new ForecastAdapter(this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        mRecyclerView.setAdapter(mForecastAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mErrorMsgDisplay = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_search_progress);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        showLoading();

        //启动APP时根据 偏好设置的数值 初始化
        getSupportLoaderManager().initLoader(ID_WEATHER_CURSOR, null, this);

        mSearchCity = MSunshinePreference.getPreferredWeatherCity(this);
        MSunshineSyncUtils.initialize(this, mSearchCity);

    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_logo);
        }
    }

    /**
     * 注册偏好设置监听，当偏好设置改变时，更新主活动的UI
     **/
    @Override
    protected void onStart() {
        super.onStart();
        mSearchCity = MSunshinePreference.getPreferredWeatherCity(this);
        if (pref_edit_text_flag) {
            MSunshineSyncUtils.startImmediateSync(this, mSearchCity);
            pref_edit_text_flag = false;
        }
    }

    /**
     * 注销偏好设置监听
     **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * 点击recyclerView列表项目时回调的函数
     **/
    @Override
    public void onClickItem(String weatherData) {
        ExplicitIntentActivityUtils.toDetail(this, weatherData);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.edit_hint));
//        ImageView closeButton=searchView.findViewById(R.id.search_close_btn);
//        closeButton.setImageResource(R.drawable.art_close_orange_24dp);
        //有文字显示X按钮，无文字不显示
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        //显示提交按钮
//        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (DataFormatUtils.isChinese(getApplicationContext(), query)) {
                    mSearchCity = query;
                    MSunshineSyncUtils.startImmediateSync(getApplicationContext(), mSearchCity);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 点击菜单栏各个项目时回调的函数
     * **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                ExplicitIntentActivityUtils.toSetting(this);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // 使用反射，让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    @SuppressLint("PrivateApi")
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    public void showWeatherData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMsgDisplay.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage() {
        mErrorMsgDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public void showLoading() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMsgDisplay.setVisibility(View.INVISIBLE);
    }

    /**
     * 从SQLiteDatabase读取数据
     * **/
    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {

        switch (id) {
            case ID_WEATHER_CURSOR:
                return new CursorLoader(
                        this,
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        MAIN_PROJECTION,
                        WeatherContract.WeatherEntry.getSQLSelectTodayForwards(),
                        null,
                        WeatherContract.WeatherEntry.COLUMN_DATE + " ASC");
            default:
                throw new RuntimeException("Unknown loader ID :" + id);
        }

    }

    /**
     * 用读取到的SQLiteDatabase的数据来更新recyclerView列表项目
     * **/
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        mForecastAdapter.setWeatherData(cursor);
        if (cursor != null) {
            showWeatherData();
        } else
            showErrorMessage();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    /**
     * 偏好设置的值改变时回调的函数
     * **/
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_city_key)))
            pref_edit_text_flag = true;
    }
}
