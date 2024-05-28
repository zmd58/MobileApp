package com.example.d308.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d308.dao.ExcursionDAO;
import com.example.d308.dao.VacationDAO;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;

@Database(version = 1, entities = {Vacation.class, Excursion.class}, exportSchema = false)
public abstract class DatabaseBuilder extends RoomDatabase {

    private static volatile DatabaseBuilder instance;
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    public static synchronized DatabaseBuilder getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "d308_app.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}