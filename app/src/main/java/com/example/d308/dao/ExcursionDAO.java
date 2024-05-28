package com.example.d308.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursions WHERE id = :id")
    Excursion getExcursionByID(int id);

    @Query("SELECT * FROM excursions ORDER BY date DESC")
    LiveData<List<Excursion>> getAll();

}
