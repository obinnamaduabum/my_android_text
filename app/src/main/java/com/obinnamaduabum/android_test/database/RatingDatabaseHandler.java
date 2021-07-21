package com.obinnamaduabum.android_test.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.obinnamaduabum.android_test.R;
import com.obinnamaduabum.android_test.config.AppConfig;
import com.obinnamaduabum.android_test.model.MovieModel;
import com.obinnamaduabum.android_test.model.Rating;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class RatingDatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = RatingDatabaseHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "ratings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SOURCE = "source";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_MOVIE_ID = "movie_id";


    public RatingDatabaseHandler(@Nullable Context context) {
        super(context, AppConfig.DATABASE_NAME, null, DATABASE_VERSION);
    }


    public SQLiteDatabase getDatabase() {
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_GROUP = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_SOURCE + " TEXT,"
                + COLUMN_VALUE + " TEXT,"
                + COLUMN_MOVIE_ID + " INTEGER" +
                ")";

        db.execSQL(CREATE_USER_GROUP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(Rating rating, int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getInsertValues(rating, movieId);
        db.insert(TABLE_NAME, null, values);
    }

    public void bulkInsert(List<Rating> ratingList, long movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Rating rating : ratingList) {
                ContentValues values = getInsertValues(rating, movieId);
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private Rating buildObject(Cursor c) {

        int id = c.getInt(c.getColumnIndex(COLUMN_ID));
        String value = c.getString(c.getColumnIndex(COLUMN_VALUE));
        String source = c.getString(c.getColumnIndex(COLUMN_SOURCE));
        Rating rating = new Rating(source, value);
        return  rating;
    }

    public List<Rating> getAllByMovieId(long movieId) {
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_MOVIE_ID +" = "+ movieId +" ;";
        SQLiteDatabase db = this.getWritableDatabase();
        List<Rating> ratingList = new ArrayList<>();
        try {
            Cursor c = db.rawQuery(SQL_QUERY, null);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    Rating rating = buildObject(c);
                    ratingList.add(rating);
                    c.moveToNext();
                }
            }
            c.close();
            return ratingList;
        } catch (Exception e){
            return new ArrayList<>();
        }
    }

    private ContentValues getInsertValues(Rating rating, long movieId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOURCE, rating.getSource());
        values.put(COLUMN_VALUE, rating.getValue());
        values.put(COLUMN_MOVIE_ID, movieId);
        return values;
    }
}

