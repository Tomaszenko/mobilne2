package com.mymobile.zadanie2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class EditActivity extends AppCompatActivity {

    private int taskPos;
    private UserTask userTask;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.intent = getIntent();

        if(intent != null) {
            taskPos = intent.getIntExtra("id", 0);
            userTask = intent.getParcelableExtra("task");
        }

        if(savedInstanceState != null) {
            taskPos = savedInstanceState.getInt("id");
            userTask = (UserTask) savedInstanceState.getParcelable("task");
        }

        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_edit);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        EditText editText = (EditText) findViewById(R.id.name_input);
        editText.setText(userTask.getDescription());

        DatePicker datePicker = (DatePicker) findViewById(R.id.date_input);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        Log.i("edit", String.valueOf(userTask.getDateOfTask().getYear() + 1900));
        Log.i("edit", String.valueOf(userTask.getDateOfTask().getMonth()));
        Log.i("edit", String.valueOf(userTask.getDateOfTask().getDate()));

        datePicker.updateDate(userTask.getDateOfTask().getYear() + 1900, userTask.getDateOfTask().getMonth(), userTask.getDateOfTask().getDate());

        TimePicker timePicker = (TimePicker) findViewById(R.id.time_input);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(userTask.getDateOfTask().getHours());
        timePicker.setCurrentMinute(userTask.getDateOfTask().getMinutes());

        CheckBox checkBox = (CheckBox) findViewById(R.id.notification_input);
        checkBox.setChecked(userTask.isNotification());
    }

    public void getDataFromView() {
        String taskName = getTextFromNameInput();
        Date taskDateTime = getDatetimeFromDatetimeInputs();
        boolean notification = getBooleanFromCheckbox();
        userTask.setDescription(taskName);
        userTask.setDateOfTask(taskDateTime);
    }

    public void goBack(View view) {
        this.setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        this.setResult(RESULT_CANCELED, intent);
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

        TimePicker timePicker = ((TimePicker) findViewById(R.id.time_input));

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();


        calendar.set(year, month, dayOfMonth, hour, minute, 0);

        return calendar.getTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("add", "menu creation");
        try {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
        }catch(Exception exc) {
            exc.printStackTrace();
        }
        Log.i("add", "menu creation");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_confirm) {
            confirm();
        }

        return super.onOptionsItemSelected(item);
    }

    public void confirm() {
        getDataFromView();
        intent.putExtra("id", taskPos);
        intent.putExtra("task", userTask);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", taskPos);
        outState.putParcelable("task", userTask);
    }
}
