package com.obinnamaduabum.android_test.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.CaseMap;
import android.util.Log;

import com.obinnamaduabum.android_test.config.AppConfig;
import com.obinnamaduabum.android_test.model.MovieModel;
import com.obinnamaduabum.android_test.model.Rating;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class MovieDatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = MovieDatabaseHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "movies";
    private static final String COLUMN_ID = "id";

    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_RATED = "rated";
    private static final String COLUMN_RELEASED = "released";
    private static final String COLUMN_RUNTIME = "runtime";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_DIRECTOR = "director";
    private static final String COLUMN_WRITER = "writer";
    private static final String COLUMN_ACTOR = "actors";
    private static final String COLUMN_PLOT = "plot";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_LANGUAGE = "language";

    private static final String COLUMN_AWARDS = "awards";
    private static final String COLUMN_POSTER = "poster";

    private static final String COLUMN_metaScore = "metaScore";
    private static final String COLUMN_imdbRating = "imdbRating";

    private static final String COLUMN_imdbVotes = "imdbVotes";
    private static final String COLUMN_IMDB_ID = "imdbID";

    private static final String COLUMN_TYPE = "movie_type";
    private static final String COLUMN_DVD = "dVD";

    private static final String COLUMN_BOX_OFFICE = "boxOffice";
    private static final String COLUMN_PRODUCTION = "production";

    private static final String COLUMN_WEBSITE = "website";
    private static final String COLUMN_TOTAL_SEASONS = "totalSeasons";

    public List<Rating> ratings;




    public MovieDatabaseHandler(@Nullable Context context) {
        super(context, AppConfig.DATABASE_NAME, null, DATABASE_VERSION);
    }


    public SQLiteDatabase getDatabase() {
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_YEAR + " TEXT, "
                + COLUMN_RATED + " TEXT, "
                + COLUMN_RELEASED + " TEXT, "
                + COLUMN_RUNTIME + " TEXT, "
                + COLUMN_GENRE + " TEXT, "
                + COLUMN_DIRECTOR + " TEXT, "
                + COLUMN_WRITER + " TEXT, "
                + COLUMN_ACTOR + " TEXT, "
                + COLUMN_PLOT + " TEXT, "
               + COLUMN_COUNTRY + " TEXT, "
                + COLUMN_LANGUAGE + " TEXT, "
                + COLUMN_AWARDS + " TEXT, "
               + COLUMN_POSTER + " TEXT, "
               + COLUMN_metaScore + " TEXT, "
               + COLUMN_imdbRating + " TEXT, "
                + COLUMN_imdbVotes + " TEXT, "
                + COLUMN_IMDB_ID + " TEXT, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_DVD + " TEXT, "
                + COLUMN_BOX_OFFICE + " TEXT, "
                + COLUMN_PRODUCTION + " TEXT, "
                + COLUMN_WEBSITE + " TEXT, "
                + COLUMN_TOTAL_SEASONS + " TEXT " +
                ")";

        db.execSQL(CREATE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Long insert(MovieModel movieModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getInsertValues(movieModel);
        long id = db.insert(TABLE_NAME, null, values);
        return id;
        // db.close();
    }


    public void bulkInsert(List<MovieModel> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (MovieModel movieModel : list) {
                ContentValues values = getInsertValues(movieModel);
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }





    public long count() {
        // String SQL_QUERY = "SELECT COUNT(*) FROM " + TABLE_NAME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
            // db.close();
            return count;
        } catch (Exception e){
            return 0;
        }
    }

    private MovieModel buildObject(Cursor c) {

        int id = c.getInt(c.getColumnIndex(COLUMN_ID));

        String title = c.getString(c.getColumnIndex(COLUMN_TITLE));
        String year = c.getString(c.getColumnIndex(COLUMN_YEAR));

        String rated = c.getString(c.getColumnIndex(COLUMN_RATED));
        String released = c.getString(c.getColumnIndex(COLUMN_RELEASED));

        String runtime = c.getString(c.getColumnIndex(COLUMN_RUNTIME));
        String genre = c.getString(c.getColumnIndex(COLUMN_GENRE));

        String director = c.getString(c.getColumnIndex(COLUMN_DIRECTOR));
        String writer = c.getString(c.getColumnIndex(COLUMN_WRITER));
        String actors = c.getString(c.getColumnIndex(COLUMN_ACTOR));
        String country = c.getString(c.getColumnIndex(COLUMN_COUNTRY));
        String awards = c.getString(c.getColumnIndex(COLUMN_AWARDS));
        String poster = c.getString(c.getColumnIndex(COLUMN_POSTER));
        String metaScore = c.getString(c.getColumnIndex(COLUMN_metaScore));
        String imdbRating = c.getString(c.getColumnIndex(COLUMN_imdbRating));
        String imdbVotes = c.getString(c.getColumnIndex(COLUMN_imdbVotes));
        String imdbID = c.getString(c.getColumnIndex(COLUMN_IMDB_ID));
        String type = c.getString(c.getColumnIndex(COLUMN_TYPE));
        String totalSeasons = c.getString(c.getColumnIndex(COLUMN_TOTAL_SEASONS));

        String dvd = c.getString(c.getColumnIndex(COLUMN_DVD));
        String plot = c.getString(c.getColumnIndex(COLUMN_PLOT));
        String language = c.getString(c.getColumnIndex(COLUMN_LANGUAGE));
        String boxOffice = c.getString(c.getColumnIndex(COLUMN_BOX_OFFICE));
        String production = c.getString(c.getColumnIndex(COLUMN_PRODUCTION));
        String website = c.getString(c.getColumnIndex(COLUMN_WEBSITE));

        MovieModel movieModel = new MovieModel();
        movieModel.setTitle(title);
        movieModel.setYear(year);
        movieModel.setRated(rated);
        movieModel.setReleased(released);
        movieModel.setRuntime(runtime);
        movieModel.setGenre(genre);
        movieModel.setDirector(director);
        movieModel.setActors(actors);
        movieModel.setWriter(writer);
        movieModel.setCountry(country);
        movieModel.setAwards(awards);
        movieModel.setPoster(poster);
        movieModel.setMetaScore(metaScore);
        movieModel.setImdbID(imdbID);
        movieModel.setImdbRating(imdbRating);
        movieModel.setImdbVotes(imdbVotes);
        // movieModel.setResponse(jsonResponse);
        movieModel.setTotalSeasons(totalSeasons);
        movieModel.setImdbVotes(imdbVotes);
        movieModel.setType(type);

        movieModel.setdVD(dvd);
        movieModel.setPlot(plot);
        movieModel.setLanguage(language);
        movieModel.setBoxOffice(boxOffice);
        movieModel.setProduction(production);
        movieModel.setWebsite(website);

        return movieModel;
    }

    public List<MovieModel> getAll() {
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        List<MovieModel> modelList = new ArrayList<>();
        try {
            Cursor c = db.rawQuery(SQL_QUERY, null);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    MovieModel movieModel = buildObject(c);
                    modelList.add(movieModel);
                    c.moveToNext();
                }
            }
            c.close();
            return modelList;
        } catch (Exception e){
            return new ArrayList<>();
        }
    }

    public MovieModel getByMovieId(long movieId) {
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_ID + " = " + movieId + " ;";
        SQLiteDatabase db = this.getWritableDatabase();
        MovieModel movieModel = null;
        try {
            Cursor c = db.rawQuery(SQL_QUERY, null);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    movieModel = buildObject(c);
                    c.moveToNext();
                }
            }
            c.close();
            return movieModel;
        } catch (Exception e){
            return null;
        }
    }


    public MovieModel getByMovieTitle(String title) {
        String lowerTitle = title.toLowerCase();
        String SQL_QUERY = "";
        SQLiteDatabase db = this.getWritableDatabase();
        MovieModel movieModel = null;
        try {
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_TITLE + " = " + title + " ;", null);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    movieModel = buildObject(c);
                    c.moveToNext();
                }
            }
            c.close();
            return movieModel;
        } catch (Exception e){
            return null;
        }
    }


    private ContentValues getInsertValues(MovieModel movieModel) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, movieModel.getTitle());
        values.put(COLUMN_YEAR, movieModel.getYear());

        values.put(COLUMN_RATED, movieModel.getRated());
        values.put(COLUMN_RELEASED, movieModel.getReleased());

        values.put(COLUMN_RUNTIME, movieModel.getRuntime());
        values.put(COLUMN_GENRE, movieModel.getGenre());

        values.put(COLUMN_DIRECTOR, movieModel.getDirector());
        values.put(COLUMN_WRITER, movieModel.getWriter());
        values.put(COLUMN_ACTOR, movieModel.getActors());
        values.put(COLUMN_PLOT, movieModel.getPlot());
        values.put(COLUMN_COUNTRY, movieModel.getCountry());
        values.put(COLUMN_LANGUAGE, movieModel.getReleased());

        values.put(COLUMN_AWARDS, movieModel.getAwards());
        values.put(COLUMN_POSTER, movieModel.getPoster());

        values.put(COLUMN_metaScore, movieModel.getMetaScore());
        values.put(COLUMN_imdbRating, movieModel.getImdbRating());

        values.put(COLUMN_imdbVotes, movieModel.getImdbVotes());
        values.put(COLUMN_IMDB_ID, movieModel.getImdbID());

        values.put(COLUMN_TYPE, movieModel.getType());
        values.put(COLUMN_DVD, movieModel.getdVD());

        values.put(COLUMN_BOX_OFFICE, movieModel.getBoxOffice());
        values.put(COLUMN_PRODUCTION, movieModel.getProduction());

        values.put(COLUMN_WEBSITE, movieModel.getWebsite());
        values.put(COLUMN_TOTAL_SEASONS, movieModel.getTotalSeasons());


        return values;
    }
}

