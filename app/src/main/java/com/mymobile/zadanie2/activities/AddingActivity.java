package com.mymobile.zadanie2.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.mymobile.zadanie2.R;
import com.mymobile.zadanie2.models.UserTask;

import java.util.Calendar;
import java.util.Date;

public class AddingActivity extends AppCompatActivity {

    private Intent intent;
    private String taskName;
    private Date taskDateTime;
    private boolean notification;

    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.intent = getIntent();

        setContentView(R.layout.activity_adding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_adding);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        Log.i("add", "in creation");

        DatePicker datePicker = (DatePicker) findViewById(R.id.date_input);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        timePicker = ((TimePicker) findViewById(R.id.time_input));
        timePicker.setIs24HourView(true);

        Log.i("add", "created");
    }

    public void getDataFromView() {
        taskName = getTextFromNameInput();
        taskDateTime = getDatetimeFromDatetimeInputs();
        notification = getBooleanFromCheckbox();
    }

    public void goBack() {
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public boolean getBooleanFromCheckbox() {
        return ((CheckBox) findViewById(R.id.notification_input)).isChecked();
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

        try {
            int hour = timePicker.getCurrentHour();
            Log.i("add", "hour="+hour);
            int minute = timePicker.getCurrentMinute();
            calendar.set(year, month, dayOfMonth, hour, minute, 0);
        } catch(Exception exc) {
            Log.i("add", "zlapalem");
        }

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
        int id = item.getItemId();

        Log.i("add", "menu item selected");

        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else {
            //noinspection SimplifiableIfStatement
            if (id == R.id.action_confirm) {
                confirm();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }



    public void confirm() {
        Log.i("add", "confirm");
        getDataFromView();
        Log.i("add", String.valueOf(taskDateTime.getHours()));
        Log.i("add", "putting data");
        intent.putExtra("user_form", new UserTask(taskName, taskDateTime, notification));
        setResult(RESULT_OK, intent);
        Log.i("add", "finish");
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", taskName);
        outState.putSerializable("time", taskDateTime);
        outState.putBoolean("notification", notification);
    }
}
