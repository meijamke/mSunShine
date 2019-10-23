package com.example.msunshine;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private String[] mWeatherData;
    private Cursor mCursor;
    private OnClickListItemListener mItemListener;

    ForecastAdapter(OnClickListItemListener onClickListItemListener) {
        mItemListener = onClickListItemListener;
    }

    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.forecast_list_item, viewGroup, false);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapterViewHolder holder, int position) {
        if (mWeatherData != null)
            holder.weatherSummaryTextView.setText(mWeatherData[position]);
        else if (mCursor != null) {
            String date = mCursor.getString(MainActivity.INDEX_WEATHER_DATE);
            String week = mCursor.getString(MainActivity.INDEX_WEATHER_WEEK);
            String dayCondition = mCursor.getString(MainActivity.INDEX_WEATHER_DAY_CONDITION);
            String nightCondition = mCursor.getString(MainActivity.INDEX_WEATHER_NIGHT_CONDITION);
            String dayTemp = mCursor.getString(MainActivity.INDEX_WEATHER_DAY_TEMP);
            String nightTemp = mCursor.getString(MainActivity.INDEX_WEATHER_NIGHT_TEMP);
            holder.weatherSummaryTextView.setText(String.format("%s\n%s\n%s\n%s\n%s\n%s\n",
                    date, week, dayCondition, nightCondition, dayTemp, nightTemp));
        }
    }

    @Override
    public int getItemCount() {
        if (mWeatherData != null)
            return mWeatherData.length;
        else if (mCursor != null)
            return mCursor.getCount();
        return 0;
    }

    void setWeatherData(String[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    public interface OnClickListItemListener {
        void onClickItem(String weatherData);
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView weatherSummaryTextView;

        ForecastAdapterViewHolder(View view) {
            super(view);
            weatherSummaryTextView = view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String weather = "";
            if (mWeatherData != null)
                weather = mWeatherData[getAdapterPosition()];
            else if (mCursor != null)
                weather = weatherSummaryTextView.getText().toString();
            mItemListener.onClickItem(weather);
        }
    }
}
