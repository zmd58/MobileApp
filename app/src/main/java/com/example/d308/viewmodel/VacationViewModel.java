package com.example.d308.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.d308.database.Repository;
import com.example.d308.entities.Vacation;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Vacation>> vacationList;

    public VacationViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        vacationList = repository.getAllVacations();
    }

    public void insert(Vacation vacation) {
        repository.insertVacation(vacation);
    }

    public void update(Vacation vacation) {
        repository.updateVacation(vacation);
    }

    public void delete(Vacation vacation) {
        repository.deleteVacation(vacation);
    }

    public Vacation getVacationByID(int id) { return repository.getVacationByID(id); }

    public LiveData<List<Vacation>> getAll() {
        return vacationList;
    }

}
