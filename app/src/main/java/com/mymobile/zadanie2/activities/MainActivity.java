package com.mymobile.zadanie2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mymobile.zadanie2.R;
import com.mymobile.zadanie2.adapters.TaskListAdapter;
import com.mymobile.zadanie2.models.UserTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int FILL_NEW_TASK_REQUEST = 1;

    private ListView listView;
    private List<UserTask> userTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017,11,5, 10, 0, 0);

        Date date = calendar.getTime();

        userTasks.add(new UserTask("kolacja", date));
        userTasks.add(new UserTask("lulu", date));

        listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter(new TaskListAdapter(this, R.layout.item_layout, userTasks));
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
                UserTask newUserTask = data.getParcelableExtra("user_task");
                userTasks.add(newUserTask);
            }

            if(resultCode == RESULT_CANCELED) {

            }
        }
    }
}
