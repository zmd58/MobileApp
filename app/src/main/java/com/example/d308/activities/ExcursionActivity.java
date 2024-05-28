package com.example.d308.activities;

import static android.view.View.GONE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.d308.R;
import com.example.d308.entities.Vacation;
import com.example.d308.viewmodel.VacationViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionActivity extends AppCompatActivity {

    public static final String EXTRA_EXCURSION_ID = "EXTRA_EXCURSION_ID";
    public static final String EXTRA_EXCURSION_TITLE = "EXTRA_EXCURSION_TITLE";
    public static final String EXTRA_EXCURSION_DATE = "EXTRA_EXCURSION_DATE";
    public static final String EXTRA_EXCURSION_VACATION_ID = "EXTRA_EXCURSION_VACATION_ID";
    public static final int EDIT_VACATION_REQUEST = 2;
    public static final int DELETE_EXCURSION_REQUEST = 6;
    private EditText editTextExcursionID;
    private EditText editTextTitle;
    private EditText editTextVacationID;
    private DatePickerDialog datePickerDialog;
    private Button datePickerButton;
    private VacationViewModel vacationViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion);

        initDatePicker();
        datePickerButton = findViewById(R.id.datePickerButton);
        datePickerButton.setText(getMinDate());
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        editTextExcursionID = findViewById(R.id.edit_excursion_text_id);
        editTextExcursionID.setEnabled(false);
        editTextTitle = findViewById(R.id.edit_excursion_text_title);
        editTextVacationID = findViewById(R.id.edit_excursion_text_vacation_id);
        editTextVacationID.setEnabled(false);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_EXCURSION_ID)) {
            setTitle("Edit Excursion");
            editTextExcursionID.setText(intent.getStringExtra(EXTRA_EXCURSION_ID));
            editTextTitle.setText(intent.getStringExtra(EXTRA_EXCURSION_TITLE));
            editTextVacationID.setText(intent.getStringExtra(EXTRA_EXCURSION_VACATION_ID));
            datePickerButton.setText(intent.getStringExtra(EXTRA_EXCURSION_DATE));
        } else {
            setTitle("Add Excursion");
            editTextExcursionID.setVisibility(GONE);
            editTextVacationID.setText(intent.getStringExtra(EXTRA_EXCURSION_VACATION_ID));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.excursion_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_excursion) {
            saveExcursion();
            return true;
        } else if (item.getItemId() == R.id.delete_excursion) {
            deleteExcursion();
            return true;
        } else if (item.getItemId() == R.id.notify_excursion) {
            notifyExcursion();
            return true;
        } else if (item.getItemId() ==  R.id.share_excursion) {
            shareExcursion();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            back();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void shareExcursion() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(ExcursionActivity.EXTRA_EXCURSION_TITLE, editTextTitle.getText().toString());
        sendIntent.putExtra(ExcursionActivity.EXTRA_EXCURSION_DATE, datePickerButton.getText().toString());
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void notifyExcursion() {
        String dateFromScreen = datePickerButton.getText().toString();
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date myDate = null;

        try {
            myDate = sdf.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            Long trigger = myDate.getTime();
            Intent intent = new Intent(ExcursionActivity.this, MyReceiver.class);
            intent.putExtra("key", editTextTitle.getText().toString() + " excursion starts today.");
            PendingIntent sender = PendingIntent.getBroadcast(ExcursionActivity.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void back() {
        vacationViewModel = new VacationViewModel(getApplication());
        int id = Integer.parseInt(getIntent().getStringExtra(EXTRA_EXCURSION_VACATION_ID));
        Vacation vacation = vacationViewModel.getVacationByID(id);
        Intent intent = new Intent(ExcursionActivity.this, VacationActivity.class);
        intent.putExtra(VacationActivity.EXTRA_VACATION_ID, String.valueOf(vacation.getId()));
        intent.putExtra(VacationActivity.EXTRA_VACATION_TITLE, vacation.getTitle());
        intent.putExtra(VacationActivity.EXTRA_VACATION_LODGING, vacation.getLodging());
        intent.putExtra(VacationActivity.EXTRA_VACATION_START_DATE, vacation.getStart_date());
        intent.putExtra(VacationActivity.EXTRA_VACATION_END_DATE, vacation.getEnd_date());
        startActivityForResult(intent, EDIT_VACATION_REQUEST);
    }

    private void saveExcursion() {
        String title = editTextTitle.getText().toString().trim();
        String vacationID = editTextVacationID.getText().toString().trim();
        String date = datePickerButton.getText().toString().trim();

        if (title.isEmpty() || vacationID.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_EXCURSION_TITLE, title);
        data.putExtra(EXTRA_EXCURSION_VACATION_ID, Integer.parseInt(vacationID));
        data.putExtra(EXTRA_EXCURSION_DATE, date);

        String id = getIntent().getStringExtra(EXTRA_EXCURSION_ID);
        if (id != null) {
            data.putExtra(EXTRA_EXCURSION_ID, Integer.valueOf(getIntent().getStringExtra(EXTRA_EXCURSION_ID)));
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private void deleteExcursion() {
        String id = editTextExcursionID.getText().toString();

        if (id.isEmpty()) {
            Toast.makeText(this, "Cannot delete unsaved excursion.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_EXCURSION_ID, Integer.parseInt(id));
        setResult(DELETE_EXCURSION_REQUEST, data);

        finish();
    }

    // Date Picker
    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                String date = makeDateString(day, month, year);
                datePickerButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        vacationViewModel = new VacationViewModel(getApplication());
        int id = Integer.parseInt(getIntent().getStringExtra(EXTRA_EXCURSION_VACATION_ID));
        Vacation current = vacationViewModel.getVacationByID(id);

        String[] startDate = current.getStart_date().split("-");
        Calendar minCal = Calendar.getInstance();
        minCal.set(Calendar.YEAR, Integer.parseInt(startDate[0]));
        minCal.set(Calendar.MONTH, Integer.parseInt(startDate[1]) - 1);
        minCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startDate[2]));
        datePickerDialog.getDatePicker().setMinDate(minCal.getTimeInMillis());

        String[] endDate = current.getEnd_date().split("-");
        Calendar maxCal = Calendar.getInstance();
        maxCal.set(Calendar.YEAR, Integer.parseInt(endDate[0]));
        maxCal.set(Calendar.MONTH, Integer.parseInt(endDate[1]) - 1);
        maxCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(endDate[2]));
        datePickerDialog.getDatePicker().setMaxDate(maxCal.getTimeInMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return year + "-" + month + "-" + day;
    }

    private String getMinDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(datePickerDialog.getDatePicker().getMinDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public void openDatePicker() {
        datePickerDialog.show();
    }

}