package com.example.d308.activities;


import static android.view.View.GONE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308.R;
import com.example.d308.adapter.ExcursionAdapter;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;
import com.example.d308.viewmodel.ExcursionViewModel;
import com.example.d308.viewmodel.VacationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationActivity extends AppCompatActivity {
    // excursion
    public static final int DELETE_VACATION_REQUEST = 3;
    public static final int ADD_EXCURSION_REQUEST = 4;
    public static final int EDIT_EXCURSION_REQUEST = 5;
    public static final int DELETE_EXCURSION_REQUEST = 6;
    private ExcursionViewModel excursionViewModel;
    // vacation
    public static final String EXTRA_VACATION_ID = "EXTRA_VACATION_ID";
    public static final String EXTRA_VACATION_TITLE = "EXTRA_VACATION_TITLE";
    public static final String EXTRA_VACATION_LODGING = "EXTRA_VACATION_LODGING";
    public static final String EXTRA_VACATION_START_DATE = "EXTRA_VACATION_START_DATE";
    public static final String EXTRA_VACATION_END_DATE = "EXTRA_VACATION_END_DATE";
    private EditText editTextID;
    private EditText editTextTitle;
    private EditText editTextLodging;
    private DatePickerDialog datePickerDialogStartDate;
    private DatePickerDialog datePickerDialogEndDate;
    private Button buttonStartDate;
    private Button buttonEndDate;
    private Vacation currentVacation;
    private VacationViewModel vacationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation);

        // date picker
        initStartDatePicker();
        initEndDatePicker();
        buttonStartDate = findViewById(R.id.buttonStartDate);
        buttonEndDate = findViewById(R.id.buttonEndDate);
        buttonStartDate.setText(getTodayDate());
        Calendar minEndDate = Calendar.getInstance();
        int year = minEndDate.get(Calendar.YEAR);
        int month = minEndDate.get(Calendar.MONTH);
        month += 1;
        int day = minEndDate.get(Calendar.DAY_OF_MONTH);
        day += 1;
        buttonEndDate.setText(makeDateString(day, month, year));
        buttonStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStartDatePicker();
            }
        });

        buttonEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEndDatePicker();
            }
        });

        editTextID = findViewById(R.id.edit_text_id);
        editTextID.setEnabled(false);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextLodging = findViewById(R.id.edit_text_lodging);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_VACATION_ID)) {
            setTitle("Edit Vacation");
            editTextID.setText(intent.getStringExtra(EXTRA_VACATION_ID));
            editTextTitle.setText(intent.getStringExtra(EXTRA_VACATION_TITLE));
            editTextLodging.setText(intent.getStringExtra(EXTRA_VACATION_LODGING));
            buttonStartDate.setText(intent.getStringExtra(EXTRA_VACATION_START_DATE));
            buttonEndDate.setText(intent.getStringExtra(EXTRA_VACATION_END_DATE));
        } else {
            setTitle("Add Vacation");
            editTextID.setVisibility(GONE);
        }

        // Excursion related

        FloatingActionButton buttonAddExcursion = findViewById(R.id.button_add_excursion);
        buttonAddExcursion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent.hasExtra(EXTRA_VACATION_ID)) {
                    Intent intent = new Intent(VacationActivity.this, ExcursionActivity.class);
                    intent.putExtra(ExcursionActivity.EXTRA_EXCURSION_VACATION_ID, editTextID.getText().toString());
                    startActivityForResult(intent, ADD_EXCURSION_REQUEST);
                } else {
                    Toast.makeText(VacationActivity.this, "Must save vacation before adding excursion.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.vacation_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExcursionAdapter excursionAdapter = new ExcursionAdapter();
        recyclerView.setAdapter(excursionAdapter);

        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);
        excursionViewModel.getAll().observe(this, new Observer<List<Excursion>>() {
            @Override
            public void onChanged(@Nullable List<Excursion> excursionList) {
                List<Excursion> displayList = new ArrayList<>();
                for (Excursion e : excursionList) {
                    if (String.valueOf(e.getVacationID()).equals(intent.getStringExtra(EXTRA_VACATION_ID))) {
                        displayList.add(e);
                    }
                }
                excursionAdapter.submitList(displayList);
            }
        });

        excursionAdapter.setOnItemClickListener(new ExcursionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Excursion excursion) {
                Intent intent = new Intent(VacationActivity.this, ExcursionActivity.class);
                intent.putExtra(ExcursionActivity.EXTRA_EXCURSION_ID, String.valueOf(excursion.getId()));
                intent.putExtra(ExcursionActivity.EXTRA_EXCURSION_TITLE, excursion.getTitle());
                intent.putExtra(ExcursionActivity.EXTRA_EXCURSION_VACATION_ID, String.valueOf(excursion.getVacationID()));
                intent.putExtra(ExcursionActivity.EXTRA_EXCURSION_DATE, excursion.getDate());
                startActivityForResult(intent, EDIT_EXCURSION_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EXCURSION_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(ExcursionActivity.EXTRA_EXCURSION_TITLE);
            String date = data.getStringExtra(ExcursionActivity.EXTRA_EXCURSION_DATE);
            int vacationID = data.getIntExtra(ExcursionActivity.EXTRA_EXCURSION_VACATION_ID, -1);

            if (vacationID == -1) {
                Toast.makeText(this, "Cannot save excursion.", Toast.LENGTH_SHORT).show();
                return;
            }

            vacationViewModel = new VacationViewModel(getApplication());
            currentVacation = vacationViewModel.getVacationByID(vacationID);
            currentVacation.setNumberOfExcursion(currentVacation.getNumberOfExcursion() + 1);
            vacationViewModel.update(currentVacation);

            Excursion excursion = new Excursion(title, date, vacationID);
            excursionViewModel.insert(excursion);

            Toast.makeText(this, "Excursion saved!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_EXCURSION_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(ExcursionActivity.EXTRA_EXCURSION_ID, -1);
            int vacationID = data.getIntExtra(ExcursionActivity.EXTRA_EXCURSION_VACATION_ID, -1);
            if (id == -1 || vacationID == -1) {
                Toast.makeText(this, "Excursion cannot be updated.", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(ExcursionActivity.EXTRA_EXCURSION_TITLE);
            String date = data.getStringExtra(ExcursionActivity.EXTRA_EXCURSION_DATE);

            Excursion excursion = new Excursion(title, date, vacationID);
            excursion.setId(id);
            excursionViewModel = new ExcursionViewModel(getApplication());
            excursionViewModel.update(excursion);
            Toast.makeText(this, "Excursion updated!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_EXCURSION_REQUEST && resultCode == DELETE_EXCURSION_REQUEST) {
            int id = data.getIntExtra(ExcursionActivity.EXTRA_EXCURSION_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Excursion cannot be deleted.", Toast.LENGTH_SHORT).show();
                return;
            }

            Excursion excursion = excursionViewModel.getExcursionByID(id);
            excursionViewModel.delete(excursion);
            vacationViewModel = new VacationViewModel(getApplication());
            currentVacation = vacationViewModel.getVacationByID(Integer.parseInt(getIntent().getStringExtra(EXTRA_VACATION_ID)));
            currentVacation.setNumberOfExcursion(currentVacation.getNumberOfExcursion() - 1);
            vacationViewModel.update(currentVacation);

            Toast.makeText(VacationActivity.this, "Excursion deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    // Vacation related

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.vacation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_vacation) {
            saveVacation();
            return true;
        } else if (item.getItemId() == R.id.delete_vacation) {
            deleteVacation();
            return true;
        } else if (item.getItemId() == R.id.notify_vacation) {
            notifyVacation();
            return true;
        } else if (item.getItemId() == R.id.share_vacation) {
            shareVacation();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            back();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void shareVacation() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(VacationActivity.EXTRA_VACATION_TITLE, editTextTitle.getText().toString());
        sendIntent.putExtra(VacationActivity.EXTRA_VACATION_LODGING, editTextLodging.getText().toString());
        sendIntent.putExtra(VacationActivity.EXTRA_VACATION_START_DATE, buttonStartDate.getText().toString());
        sendIntent.putExtra(VacationActivity.EXTRA_VACATION_END_DATE, buttonEndDate.getText().toString());
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void notifyVacation() {
        String startDate = buttonStartDate.getText().toString();
        String endDate = buttonEndDate.getText().toString();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        Date myStartDate;
        Date myEndDate;

        try {
            myStartDate = sdf.parse(startDate);
            myEndDate = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            Long trigger = myStartDate.getTime();
            Log.d("test", trigger.toString());
            Log.d("test", sdf.format(myStartDate));
            Intent intent = new Intent(this, MyReceiver.class);
            intent.putExtra("key", editTextTitle.getText().toString() + " vacation starts today.");
            PendingIntent sender = PendingIntent.getBroadcast(this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Long trigger = myEndDate.getTime();
            Intent intent = new Intent(this, MyReceiver.class);
            intent.putExtra("key", editTextTitle.getText().toString() + " vacation ends today.");
            PendingIntent sender = PendingIntent.getBroadcast(this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void back() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveVacation() {
        String title = editTextTitle.getText().toString().trim();
        String lodging = editTextLodging.getText().toString().trim();
        String startDate = buttonStartDate.getText().toString().trim();
        String endDate = buttonEndDate.getText().toString().trim();

        if (title.isEmpty() || lodging.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!verifyDateSelect()) {
            Toast.makeText(this, "Invalid date range selected.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_VACATION_TITLE, title);
        data.putExtra(EXTRA_VACATION_LODGING, lodging);
        data.putExtra(EXTRA_VACATION_START_DATE, startDate);
        data.putExtra(EXTRA_VACATION_END_DATE, endDate);

        int id = getIntent().getStringExtra(EXTRA_VACATION_ID) == null ? -1 : Integer.valueOf(getIntent().getStringExtra(EXTRA_VACATION_ID));
        if (id != -1) {
            data.putExtra(EXTRA_VACATION_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private void deleteVacation() {
        vacationViewModel = new VacationViewModel(getApplication());
        String id = editTextID.getText().toString();

        if (id.isEmpty()) {
            Toast.makeText(this, "Cannot delete unsaved vacation.", Toast.LENGTH_SHORT).show();
            return;
        }

        Vacation current = vacationViewModel.getVacationByID(Integer.parseInt(id));
        if (current.getNumberOfExcursion() > 0) {
            Toast.makeText(this, "Cannot delete vacation when there are excursion associated!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_VACATION_ID, Integer.parseInt(id));
        setResult(DELETE_VACATION_REQUEST, data);

        finish();
    }

    // Date Picker

    public void initStartDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListenerStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar minCal = Calendar.getInstance();
                minCal.set(Calendar.YEAR, year);
                minCal.set(Calendar.MONTH, month);
                minCal.set(Calendar.DAY_OF_MONTH, day + 1);
                datePickerDialogEndDate.getDatePicker().setMinDate(minCal.getTimeInMillis());
                month += 1;
                String date = makeDateString(day, month, year);
                buttonStartDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialogStartDate = new DatePickerDialog(this, dateSetListenerStartDate, year, month, day);
        datePickerDialogStartDate.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    public void initEndDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListenerEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                String date = makeDateString(day, month, year);
                buttonEndDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialogEndDate = new DatePickerDialog(this, dateSetListenerEndDate, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return year + "-" + month + "-" + day;
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public void openStartDatePicker() {
        datePickerDialogStartDate.show();
    }

    public void openEndDatePicker() {
        datePickerDialogEndDate.show();
    }

    private boolean verifyDateSelect() {
        String[] startDate = buttonStartDate.getText().toString().split("-");
        int sYear = Integer.parseInt(startDate[0]);
        int sMonth = Integer.parseInt(startDate[1]);
        int sDay = Integer.parseInt(startDate[2]);
        String[] endDate = buttonEndDate.getText().toString().split("-");
        int eYear = Integer.parseInt(endDate[0]);
        int eMonth = Integer.parseInt(endDate[1]);
        int eDay = Integer.parseInt(endDate[2]);

        return (eYear > sYear) ||
                ((eYear == sYear) && (eMonth > sMonth)) ||
                ((eYear == sYear) && (eMonth == sMonth) && (eDay > sDay));
    }

}