package com.example.msunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.msunshine.data.MSunshinePreference;

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

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mWeatherTextView;

        ForecastAdapterViewHolder(View view) {
            super(view);
            mWeatherTextView = view.findViewById(R.id.tv_weather_data);
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

    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.forecast_list_item, viewGroup, false);
        return new ForecastAdapterViewHolder(view);
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

        String weatherInfo = String.format("%s\n%s\n%s\n%s\n%s\n%s",
                date, week, dayCondition, nightCondition, dayTemp, nightTemp);
        holder.mWeatherTextView.setText(weatherInfo);

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
