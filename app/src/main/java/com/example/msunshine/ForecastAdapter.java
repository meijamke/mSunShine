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
import com.example.msunshine.utilities.MSunshineDateUtils;
import com.example.msunshine.utilities.MSunshineWeatherUtils;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private OnClickListItemListener mItemListener;

    ForecastAdapter(Context context, OnClickListItemListener onClickListItemListener) {
        mContext = context;
        mItemListener = onClickListItemListener;
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

        int weatherIcon;
        String weatherCondition;
        if (MSunshineDateUtils.getNormalizedHourNow() >= mContext.getResources().getInteger(R.integer.hour_between_night_and_day) &&
                MSunshineDateUtils.getNormalizedHourNow() <= mContext.getResources().getInteger(R.integer.hour_between_day_and_night)) {
            weatherIcon = MSunshineWeatherUtils.getSmallIcResIdForWeatherCondition(dayCondition);
            weatherCondition = dayCondition;
        } else {
            weatherIcon = MSunshineWeatherUtils.getSmallIcResIdForWeatherCondition(nightCondition);
            weatherCondition = nightCondition;
        }
        holder.mWeatherIcon.setImageResource(weatherIcon);
        holder.mWeatherDate.setText(date);
        holder.mWeatherDescription.setText(weatherCondition);
        holder.mWeatherWeek.setText(week);
        holder.mWeatherLowTemperature.setText(dayTemp);
        holder.mWeatherHighTemperature.setText(nightTemp);

    }

    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.forecast_list_item, viewGroup, false);
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

    void setWeatherData(Cursor weatherData) {
        mCursor = weatherData;
        notifyDataSetChanged();
    }
}
