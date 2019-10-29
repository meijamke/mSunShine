package com.example.msunshine;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msunshine.data.MSunshinePreference;
import com.example.msunshine.utilities.DateUtils;
import com.example.msunshine.utilities.WeatherImageUtils;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private OnClickListItemListener mItemListener;

    private static final int TYPE_TODAY = 0;
    private static final int TYPE_FUTURE_DAY = 1;
    private boolean mUseTodayLayout;

    ForecastAdapter(Context context, OnClickListItemListener onClickListItemListener) {
        mContext = context;
        mItemListener = onClickListItemListener;
        mUseTodayLayout = context.getResources().getBoolean(R.bool.use_today_layout);
    }

    public interface OnClickListItemListener {
        void onClickItem(String weatherData);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapterViewHolder holder, int position) {

        if (!(mCursor.moveToPosition(position)))
            return;
        String date = mCursor.getString(MainActivity.INDEX_WEATHER_DATE);
        String week = mCursor.getString(MainActivity.INDEX_WEATHER_WEEK);
        String dayCondition = mCursor.getString(MainActivity.INDEX_WEATHER_DAY_CONDITION);
        String nightCondition = mCursor.getString(MainActivity.INDEX_WEATHER_NIGHT_CONDITION);

        String dayTemperature = mCursor.getString(MainActivity.INDEX_WEATHER_DAY_TEMP);
        String dayTemp = MSunshinePreference.formatTemperature(mContext, dayTemperature);

        String nightTemperature = mCursor.getString(MainActivity.INDEX_WEATHER_NIGHT_TEMP);
        String nightTemp = MSunshinePreference.formatTemperature(mContext, nightTemperature);

        int viewType = getItemViewType(position);
        int weatherIcon;
        String weatherCondition;
        if (DateUtils.getNormalizedHourNow() >= mContext.getResources().getInteger(R.integer.hour_between_night_and_day) &&
                DateUtils.getNormalizedHourNow() <= mContext.getResources().getInteger(R.integer.hour_between_day_and_night)) {
            switch (viewType) {
                case TYPE_TODAY:
                    weatherIcon = WeatherImageUtils.getLargeArtResIdForWeatherCondition(dayCondition);
                    break;
                case TYPE_FUTURE_DAY:
                    weatherIcon = WeatherImageUtils.getSmallIcResIdForWeatherCondition(dayCondition);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown view type ");
            }
            weatherCondition = dayCondition;
        } else {
            switch (viewType) {
                case TYPE_TODAY:
                    weatherIcon = WeatherImageUtils.getLargeArtResIdForWeatherCondition(dayCondition);
                    break;
                case TYPE_FUTURE_DAY:
                    weatherIcon = WeatherImageUtils.getSmallIcResIdForWeatherCondition(dayCondition);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown view type ");
            }
            weatherCondition = nightCondition;
        }

        holder.mWeatherIcon.setImageResource(weatherIcon);
        holder.mWeatherDate.setText(date);
        holder.mWeatherDescription.setText(weatherCondition);
        holder.mWeatherWeek.setText(week);
        holder.mWeatherLowTemperature.setText(nightTemp);
        holder.mWeatherHighTemperature.setText(dayTemp);

    }

    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        int layoutId;
        switch (viewType) {
            case TYPE_TODAY:
                layoutId = R.layout.forecast_list_item_today;
                break;
            case TYPE_FUTURE_DAY:
                layoutId = R.layout.forecast_list_item;
                break;
            default:
                throw new IllegalArgumentException("Unknown view type ");
        }
        View view = inflater.inflate(layoutId, viewGroup, false);
        return new ForecastAdapterViewHolder(view);
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mWeatherIcon;
        final TextView mWeatherDate;
        final TextView mWeatherDescription;
        final TextView mWeatherWeek;
        final TextView mWeatherLowTemperature;
        final TextView mWeatherHighTemperature;

        ForecastAdapterViewHolder(View view) {
            super(view);
            mWeatherIcon = view.findViewById(R.id.weather_icon);
            mWeatherDate = view.findViewById(R.id.weather_date);
            mWeatherDescription = view.findViewById(R.id.weather_description);
            mWeatherWeek = view.findViewById(R.id.weather_week);
            mWeatherLowTemperature = view.findViewById(R.id.weather_low_temperature);
            mWeatherHighTemperature = view.findViewById(R.id.weather_high_temperature);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mCursor.moveToPosition(getAdapterPosition())) {
                String date = mCursor.getString(MainActivity.INDEX_WEATHER_DATE);
                mItemListener.onClickItem(date);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null)
            return mCursor.getCount();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseTodayLayout && position == 0)
            return TYPE_TODAY;
        else
            return TYPE_FUTURE_DAY;
    }

    void setWeatherData(Cursor weatherData) {
        mCursor = weatherData;
        notifyDataSetChanged();
    }
}
