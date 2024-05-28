package com.example.d308.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String lodging;
    private String start_date;
    private String end_date;
    private int numberOfExcursion;

    public Vacation(String title, String lodging, String start_date, String end_date, int numberOfExcursion) {
        this.title = title;
        this.lodging = lodging;
        this.start_date = start_date;
        this.end_date = end_date;
        this.numberOfExcursion = numberOfExcursion;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLodging() {
        return lodging;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfExcursion() {
        return numberOfExcursion;
    }

    public void setNumberOfExcursion(int numberOfExcursion) {
        this.numberOfExcursion = numberOfExcursion;
    }
}
