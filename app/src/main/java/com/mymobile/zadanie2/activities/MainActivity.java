package com.mymobile.zadanie2.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mymobile.zadanie2.R;
import com.mymobile.zadanie2.adapters.TaskListAdapter;
import com.mymobile.zadanie2.models.UserTask;
//import com.mymobile.zadanie2.receivers.NotificationPublisher;
import com.mymobile.zadanie2.receivers.NotificationPublisher;
import com.mymobile.zadanie2.utils.ListIOManager;
import com.mymobile.zadanie2.utils.NotificationID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int FILL_NEW_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;

    private ListView listView;
    private ArrayList<UserTask> userTasks;
    private ArrayAdapter<UserTask> arrayAdapter;

    private void initializeIdCounter() {
        NotificationID.setBeginning(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);

        NotificationID.setBeginning(sp.getInt("unique_notification_id", 1));

        Log.i("main", "after clear: "+NotificationID.getWithoutInc());

        if(savedInstanceState != null) {
            Log.i("main", "nie było nulla");
            userTasks = savedInstanceState.getParcelableArrayList("tasks");
        } else {
            Log.i("main", "był null");
            try {
                userTasks = ListIOManager.getTasks(this);
            } catch(Exception exc) {
                exc.printStackTrace();
            }
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        listView = (ListView) findViewById(R.id.main_list);
        arrayAdapter = new TaskListAdapter(this, R.layout.item_layout, userTasks);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("main", "restart");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            ListIOManager.persist(this, userTasks);
        } catch(Exception exc) {
            Log.i("main", "nie udało się zapisać");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_option_add) {
            Intent intent = new Intent(this, AddingActivity.class);
            Log.i("main", "aktywność wystartowana");
            startActivityForResult(intent, FILL_NEW_TASK_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == FILL_NEW_TASK_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                UserTask newUserTask = bundle.getParcelable("user_form");
                userTasks.add(newUserTask);
                if(newUserTask == null) {
                    Log.i("main", "received task is null");
                }
                Log.i("main", "received hour is" + newUserTask.getDateOfTask().getHours());
                Log.i("main", "received task is "+newUserTask.getDescription());
                Log.i("main", "task id is "+newUserTask.getId());
                Collections.sort(userTasks, new Comparator<UserTask>() {
                    @Override
                    public int compare(UserTask ut1, UserTask ut2) {
                        return ut1.getDateOfTask().compareTo(ut2.getDateOfTask());
                    }
                });
                arrayAdapter.notifyDataSetChanged();
                Log.i("main", "sorted");
                Snackbar.make(findViewById(R.id.main_layout), "Dodano zadanie", Snackbar.LENGTH_LONG).show();
                if(newUserTask.isNotification())
                    scheduleAlarm(newUserTask.getDescription(),
                            newUserTask.getId(),newUserTask.getDateOfTask());
            }

            if(resultCode == RESULT_CANCELED) {
                Snackbar.make(findViewById(R.id.main_layout), "Anulowano dodawanie", Snackbar.LENGTH_LONG).show();
            }
        }

        if(requestCode == EDIT_TASK_REQUEST) {
            if(resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                int id = bundle.getInt("id");
                UserTask updatedTask = bundle.getParcelable("task");
                userTasks.set(id, updatedTask);
                Collections.sort(userTasks, new Comparator<UserTask>() {
                    @Override
                    public int compare(UserTask ut1, UserTask ut2) {
                        return ut1.getDateOfTask().compareTo(ut2.getDateOfTask());
                    }
                });
                arrayAdapter.notifyDataSetChanged();
                if(updatedTask.isNotification())
                    scheduleAlarm(updatedTask.getDescription(),
                            updatedTask.getId(),updatedTask.getDateOfTask());
            }

            if(resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("main", "zapisujemy");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("tasks", userTasks);

        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("unique_notification_id", NotificationID.getWithoutInc());
        editor.apply();

        Log.i("main", "saved state");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("main", "restoring");
        super.onRestoreInstanceState(savedInstanceState);
        userTasks = savedInstanceState.getParcelableArrayList("tasks");

        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        NotificationID.setBeginning(sp.getInt("unique_notification_id", 1));
    }

    private void scheduleAlarm(String name, int notifId, Date dateOfNotification) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notifId);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notifId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateOfNotification.getTime(), pendingIntent);
        Log.i("main", "alarm_manager set");
    }
}
