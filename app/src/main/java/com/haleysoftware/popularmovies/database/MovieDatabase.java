package com.haleysoftware.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.haleysoftware.popularmovies.objects.MovieItem;

/**
 * Database holder used by Room
 * <p>
 * Created by haleysoft on 10/19/18.
 */
@Database(entities = {MovieItem.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movie_db";
    private static MovieDatabase dbInstance;

    /**
     * Returns a link to the database of favorite movies.
     *
     * @param context Context of the calling activity.
     * @return The favorite movie database.
     */
    public static MovieDatabase getDbInstance(Context context) {
        if (dbInstance == null) {
            synchronized (LOCK) {
                dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, MovieDatabase.DATABASE_NAME).build();
            }
        }
        return dbInstance;
    }

    public abstract MovieDao movieDao();
}
