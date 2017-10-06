package com.mymobile.zadanie2.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mymobile.zadanie2.R;
import com.mymobile.zadanie2.models.UserTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_layout, viewGroup, false);
        TextView textView = (TextView) rowView.findViewById(R.id.task_name);
        TextView dateView = (TextView) rowView.findViewById(R.id.task_date);
        textView.setText(userTasks.get(i).getDescription());
        dateView.setText(formatDateToString(userTasks.get(i).getDateOfTask()));

        return rowView;
    }


    private String formatDateToString(Date date) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH).format(date);
        return formattedDate;
    }
}
