package com.example.d308.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.d308.R;
import com.example.d308.adapter.VacationAdapter;
import com.example.d308.entities.Vacation;
import com.example.d308.viewmodel.VacationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_VACATION_REQUEST = 1;
    public static final int EDIT_VACATION_REQUEST = 2;
    public static final int DELETE_VACATION_REQUEST = 3;
    private VacationViewModel vacationViewModel;
    public static int numAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddVacation = findViewById(R.id.button_add_vacation);
        buttonAddVacation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VacationActivity.class);
                startActivityForResult(intent, ADD_VACATION_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        final VacationAdapter vacationAdapter = new VacationAdapter();
        recyclerView.setAdapter(vacationAdapter);

        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        vacationViewModel.getAll().observe(this, new Observer<List<Vacation>>() {
            @Override
            public void onChanged(@Nullable List<Vacation> vacationList) {
                vacationAdapter.submitList(vacationList);
            }
        });

        vacationAdapter.setOnItemClickListener(new VacationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Vacation vacation) {
                Intent intent = new Intent(MainActivity.this, VacationActivity.class);
                intent.putExtra(VacationActivity.EXTRA_VACATION_ID, String.valueOf(vacation.getId()));
                intent.putExtra(VacationActivity.EXTRA_VACATION_TITLE, vacation.getTitle());
                intent.putExtra(VacationActivity.EXTRA_VACATION_LODGING, vacation.getLodging());
                intent.putExtra(VacationActivity.EXTRA_VACATION_START_DATE, vacation.getStart_date());
                intent.putExtra(VacationActivity.EXTRA_VACATION_END_DATE, vacation.getEnd_date());
                startActivityForResult(intent, EDIT_VACATION_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_VACATION_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(VacationActivity.EXTRA_VACATION_TITLE);
            String lodging = data.getStringExtra(VacationActivity.EXTRA_VACATION_LODGING);
            String start_date = data.getStringExtra(VacationActivity.EXTRA_VACATION_START_DATE);
            String end_date = data.getStringExtra(VacationActivity.EXTRA_VACATION_END_DATE);

            Vacation vacation = new Vacation(title, lodging, start_date, end_date, 0);
            vacationViewModel.insert(vacation);

            Toast.makeText(this, "Vacation saved!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_VACATION_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(VacationActivity.EXTRA_VACATION_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Vacation cannot be updated.", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(VacationActivity.EXTRA_VACATION_TITLE);
            String lodging = data.getStringExtra(VacationActivity.EXTRA_VACATION_LODGING);
            String start_date = data.getStringExtra(VacationActivity.EXTRA_VACATION_START_DATE);
            String end_date = data.getStringExtra(VacationActivity.EXTRA_VACATION_END_DATE);
            int exNum = vacationViewModel.getVacationByID(id).getNumberOfExcursion();

            Vacation vacation = new Vacation(title, lodging, start_date, end_date, exNum);
            vacation.setId(id);
            vacationViewModel.update(vacation);

            Toast.makeText(this, "Vacation updated!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_VACATION_REQUEST && resultCode == DELETE_VACATION_REQUEST) {
            int id = data.getIntExtra(VacationActivity.EXTRA_VACATION_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Vacation cannot be deleted.", Toast.LENGTH_SHORT).show();
                return;
            }

            Vacation vacation = vacationViewModel.getVacationByID(id);
            if (vacation.getNumberOfExcursion() > 0) {
                Toast.makeText(this, "Vacation cannot be deleted with associated excursions.", Toast.LENGTH_SHORT).show();
                return;
            }

            vacationViewModel.delete(vacation);
            Toast.makeText(MainActivity.this, "Vacation deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_sample_vacations) {
            Vacation vacation = new Vacation("Italy", "Hotel", "2024-10-01", "2024-10-10", 0);
            vacationViewModel.insert(vacation);
            vacation = new Vacation("France", "Hotel", "2024-10-01", "2024-10-10", 0);
            vacationViewModel.insert(vacation);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}