package com.example.d308.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.d308.dao.ExcursionDAO;
import com.example.d308.dao.VacationDAO;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository {
    private VacationDAO vacationDAO;
    private ExcursionDAO excursionDAO;
    private LiveData<List<Vacation>> vacationList;
    private LiveData<List<Excursion>> excursionList;

    public Repository(Application application) {
        DatabaseBuilder databaseBuilder = DatabaseBuilder.getInstance(application);
        excursionDAO = databaseBuilder.excursionDAO();
        excursionList = excursionDAO.getAll();
        vacationDAO = databaseBuilder.vacationDAO();
        vacationList = vacationDAO.getAll();
    }

    // Vacation

    public void insertVacation(Vacation vacation) {
        new InsertVacationAsyncTask(vacationDAO).execute(vacation);
    }

    public void updateVacation(Vacation vacation) {
        new UpdateVacationAsyncTask(vacationDAO).execute(vacation);
    }

    public void deleteVacation(Vacation vacation) {
        new DeleteVacationAsyncTask(vacationDAO).execute(vacation);
    }

    public Vacation getVacationByID(int id) {
        try {
            return new GetVacationByIDAsyncTask(vacationDAO).execute(id).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return vacationList;
    }

    public static class InsertVacationAsyncTask extends AsyncTask<Vacation, Void, Void> {
        private VacationDAO vacationDAO;

        public InsertVacationAsyncTask(VacationDAO vacationDAO) {
            this.vacationDAO = vacationDAO;
        }

        @Override
        protected Void doInBackground(Vacation... vacations) {
            vacationDAO.insert(vacations[0]);
            return null;
        }
    }

    public static class UpdateVacationAsyncTask extends AsyncTask<Vacation, Void, Void> {
        private VacationDAO vacationDAO;

        public UpdateVacationAsyncTask(VacationDAO vacationDAO) {
            this.vacationDAO = vacationDAO;
        }

        @Override
        protected Void doInBackground(Vacation... vacations) {
            vacationDAO.update(vacations[0]);
            return null;
        }
    }

    public static class DeleteVacationAsyncTask extends AsyncTask<Vacation, Void, Void> {
        private VacationDAO vacationDAO;

        public DeleteVacationAsyncTask(VacationDAO vacationDAO) {
            this.vacationDAO = vacationDAO;
        }

        @Override
        protected Void doInBackground(Vacation... vacations) {
            vacationDAO.delete(vacations[0]);
            return null;
        }
    }

    public static class GetVacationByIDAsyncTask extends AsyncTask<Integer, Void, Vacation> {
        private VacationDAO vacationDAO;

        public GetVacationByIDAsyncTask(VacationDAO vacationDAO) {
            this.vacationDAO = vacationDAO;
        }


        @Override
        protected Vacation doInBackground(Integer... integers) {
            return vacationDAO.getVacationByID(integers[0]);
        }
    }

    // Excursion

    public void insertExcursion(Excursion excursion) {
        new InsertExcursionAsyncTask(excursionDAO).execute(excursion);
    }

    public void updateExcursion(Excursion excursion) {
        new UpdateExcursionAsyncTask(excursionDAO).execute(excursion);
    }

    public void deleteExcursion(Excursion excursion) {
        new DeleteExcursionAsyncTask(excursionDAO).execute(excursion);
    }

    public Excursion getExcursionByID(int id) {
        try {
            return new GetExcursionByIDAsyncTask(excursionDAO).execute(id).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<List<Excursion>> getAllExcursions() {
        return excursionList;
    }

    public static class InsertExcursionAsyncTask extends AsyncTask<Excursion, Void, Void> {
        private ExcursionDAO excursionDAO;

        public InsertExcursionAsyncTask(ExcursionDAO excursionDAO) {
            this.excursionDAO = excursionDAO;
        }

        @Override
        protected Void doInBackground(Excursion... excursions) {
            excursionDAO.insert(excursions[0]);
            return null;
        }
    }

    public static class UpdateExcursionAsyncTask extends AsyncTask<Excursion, Void, Void> {
        private ExcursionDAO excursionDAO;

        public UpdateExcursionAsyncTask(ExcursionDAO excursionDAO) {
            this.excursionDAO = excursionDAO;
        }

        @Override
        protected Void doInBackground(Excursion... excursions) {
            excursionDAO.update(excursions[0]);
            return null;
        }
    }

    public static class DeleteExcursionAsyncTask extends AsyncTask<Excursion, Void, Void> {
        private ExcursionDAO excursionDAO;

        public DeleteExcursionAsyncTask(ExcursionDAO excursionDAO) {
            this.excursionDAO = excursionDAO;
        }

        @Override
        protected Void doInBackground(Excursion... excursions) {
            excursionDAO.delete(excursions[0]);
            return null;
        }
    }

    public static class GetExcursionByIDAsyncTask extends AsyncTask<Integer, Void, Excursion> {
        private ExcursionDAO excursionDAO;

        public GetExcursionByIDAsyncTask(ExcursionDAO excursionDAO) {
            this.excursionDAO = excursionDAO;
        }

        @Override
        protected Excursion doInBackground(Integer... integers) {
            return excursionDAO.getExcursionByID(integers[0]);
        }
    }

}
