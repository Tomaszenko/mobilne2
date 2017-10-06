package com.mymobile.zadanie2.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.mymobile.zadanie2.R;
import com.mymobile.zadanie2.models.UserTask;

import java.util.Calendar;
import java.util.Date;

public class AddingActivity extends AppCompatActivity {

    private String taskName;
    private Date taskDateTime;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("add", "in creation");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);

        intent = this.getIntent();

        Log.i("add", "received intent");

        DatePicker datePicker = (DatePicker) findViewById(R.id.date_input);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
    }

    public void getDataFromView() {
        taskName = getTextFromNameInput();
        taskDateTime = getDatetimeFromDatetimeInputs();
    }

    public void goBack(View view) {
        this.setResult(RESULT_CANCELED, intent);
        finish();
    }

    public String getTextFromNameInput() {
        return ((EditText) findViewById(R.id.name_input)).getText().toString();
    }

    public Date getDatetimeFromDatetimeInputs() {
        Calendar calendar = Calendar.getInstance();

        DatePicker datePicker = ((DatePicker) findViewById(R.id.date_input));

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        TimePicker timePicker = ((TimePicker) findViewById(R.id.time_input));

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();


        calendar.set(year, month, dayOfMonth, hour, minute);

        return calendar.getTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("add", "menu creation");
        try {
            getMenuInflater().inflate(R.menu.menu_adding, menu);
        }catch(Exception exc) {
            exc.printStackTrace();
        }
        Log.i("add", "menu creation");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public void confirm(MenuItem item) {
        getDataFromView();
        intent.putExtra("user_form", new UserTask(taskName, taskDateTime));
        setResult(RESULT_OK, intent);
        finish();
    }
}
