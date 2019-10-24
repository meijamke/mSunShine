package com.example.msunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.msunshine.utilities.MSunshineDateUtils;

public class WeatherProvider extends ContentProvider {

    private static final String TAG = "WeatherProvider";

    static final int CODE_WEATHER = 100;
    static final int CODE_WEATHER_DATE = 101;
    static final String TYPE_WEATHER = "vnd.android.cursor.dir/vnd.";
    static final String TYPE_WEATHER_DATE = "vnd.android.cursor.item/vnd.";
    static UriMatcher mUriMatcher = buildUriMatcher();
    private WeatherDbHelper mDbHelper;

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER, CODE_WEATHER);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER + "/*", CODE_WEATHER_DATE);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        switch (mUriMatcher.match(uri)) {
            case CODE_WEATHER:
                cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_WEATHER_DATE:
                cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,
                        projection,
                        WeatherContract.WeatherEntry.COLUMN_DATE + "=?",
                        new String[]{"'" + uri.getPathSegments().get(1) + "'"},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        if (cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case CODE_WEATHER:
                return TYPE_WEATHER + WeatherContract.TYPE_WEATHER;
            case CODE_WEATHER_DATE:
                return TYPE_WEATHER_DATE + WeatherContract.TYPE_WEATHER;
            default:
                break;
        }
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        int rowsInserted = 0;
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (mUriMatcher.match(uri)) {
            case CODE_WEATHER:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        String date = (String) value.get(WeatherContract.WeatherEntry.COLUMN_DATE);
                        Log.d(TAG, "bulkInsert: " + date);
                        if ((MSunshineDateUtils.isValidDate(date)))
                            throw new IllegalArgumentException("Unsupported date format, date format should be \"yyyy-MM-dd\"");
                        long id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME,
                                null,
                                value);
                        if (id != -1)
                            rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            default:
                break;
        }
        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return rowsInserted;
        }
        return super.bulkInsert(uri, values);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Uri uriInsert = null;
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (mUriMatcher.match(uri)) {
            case CODE_WEATHER:
                assert values != null;
                String date = (String) values.get(WeatherContract.WeatherEntry.COLUMN_DATE);
                if (!(MSunshineDateUtils.isValidDate(date)))
                    throw new IllegalArgumentException("Unsupported date format, date format should be \"yyyy/MM/dd\"");
                long id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME,
                        null,
                        values);
                if (id != -1)
                    uriInsert = WeatherContract.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
                break;
            default:
                break;
        }
        if (uriInsert != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return uriInsert;
    }

    /**
     * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
     * deleted. However, if we do pass null and delete all of the rows in the table, we won't
     * know how many rows were deleted. According to the documentation for SQLiteDatabase,
     * passing "1" for the selection will delete all rows and return the number of rows
     * deleted, which is what the caller of this method expects.
     **/
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowDeleted = 0;

        if (selection == null)
            selection = "1";

        switch (mUriMatcher.match(uri)) {
            case CODE_WEATHER:
                rowDeleted = db.delete(WeatherContract.WeatherEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_WEATHER_DATE:
                rowDeleted = db.delete(WeatherContract.WeatherEntry.TABLE_NAME,
                        WeatherContract.WeatherEntry.COLUMN_DATE + "=?",
                        new String[]{uri.getPathSegments().get(1)});
                break;
            default:
                break;
        }
        if (rowDeleted > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowUpdated = 0;

        if (selection == null)
            selection = "1";

        switch (mUriMatcher.match(uri)) {
            case CODE_WEATHER:
                rowUpdated = db.update(WeatherContract.WeatherEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_WEATHER_DATE:
                rowUpdated = db.update(WeatherContract.WeatherEntry.TABLE_NAME,
                        values,
                        WeatherContract.WeatherEntry.COLUMN_DATE + "=?",
                        new String[]{uri.getPathSegments().get(1)});
                break;
            default:
                break;
        }
        if (rowUpdated > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowUpdated;
    }
}
