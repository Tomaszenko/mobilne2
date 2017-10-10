package com.mymobile.zadanie2.adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mymobile.zadanie2.R;
import com.mymobile.zadanie2.activities.EditActivity;
import com.mymobile.zadanie2.activities.MainActivity;
import com.mymobile.zadanie2.models.UserTask;
import com.mymobile.zadanie2.receivers.NotificationPublisher;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.mymobile.zadanie2.R.id.parent;

/**
 * Created by tomek on 04.10.17.
 */

public class TaskListAdapter extends ArrayAdapter<UserTask> {

    private Activity context;
    private List<UserTask> userTasks = new ArrayList<>();

    static class ViewHolder {
        public TextView taskName;
        public TextView dateTime;
        public ImageButton edit;
        public ImageButton delete;
    }

    public TaskListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<UserTask> objects) {
        super(context, resource, objects);
        this.context = context;
        this.userTasks = objects;
    }

    @Override
    public int getCount() {
        return userTasks.size();
    }

    @Override
    public UserTask getItem(int i) {
        return userTasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_layout, viewGroup, false);

        final int pos = i;

        rowView.findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("id", pos);
                intent.putExtra("task", userTasks.get(pos));
                context.startActivityForResult(intent, MainActivity.EDIT_TASK_REQUEST);
            }
        });

        rowView.findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("adapter", "delete clicked");
                UserTask userTask = getItem(pos);
                Log.i("adapter", "user task get");

                int id = userTask.getId();

                Log.i("adapter", "id retrieved");
                cancelAlarm(userTask.getId());

                Log.i("adapter", "alarm canceled");

                remove(getItem(pos));
                CharSequence text = "Zadanie usunięte";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        TextView textView = (TextView) rowView.findViewById(R.id.task_name);
        TextView dateView = (TextView) rowView.findViewById(R.id.task_date);
        textView.setText(userTasks.get(i).getDescription());
        dateView.setText(formatDateToString(userTasks.get(i).getDateOfTask()));

        return rowView;
    }


    private String formatDateToString(Date date) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date);
        return formattedDate;
    }

    private void cancelAlarm(int intentID) {

        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.putExtra(NotificationPublisher.NOTIFICATION_ID, intentID);
        intent.putExtra(NotificationPublisher.NOTIFICATION, "");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, intentID, intent, PendingIntent.FLAG_CANCEL_CURRENT
        );

        Log.i("adapter", "after broadcasting");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
        Log.i("main", "alarm_manager set");
    }
}
