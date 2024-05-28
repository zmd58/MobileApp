package com.example.d308.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.d308.database.Repository;
import com.example.d308.entities.Excursion;

import java.util.List;

public class ExcursionViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Excursion>> excursionList;

    public ExcursionViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        excursionList = repository.getAllExcursions();
    }

    public void insert(Excursion excursion) {
        repository.insertExcursion(excursion);
    }

    public void update(Excursion excursion) {
        repository.updateExcursion(excursion);
    }

    public void delete(Excursion excursion) {
        repository.deleteExcursion(excursion);
    }

    public Excursion getExcursionByID(int id)  { return repository.getExcursionByID(id); }

    public LiveData<List<Excursion>> getAll() {
        return excursionList;
    }
}
