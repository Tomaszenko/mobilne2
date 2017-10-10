package com.mymobile.zadanie2.utils;

import android.app.Service;
import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.mymobile.zadanie2.models.UserTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by tomek on 09.10.17.
 */

public class ListIOManager {

    private static File file;
    private static final String filename = "tasks.json";

    public static void persist(Context context, List<UserTask> userTasks) throws FileNotFoundException, JSONException {
        File file = new File(context.getFilesDir(), filename);

        Log.i("service", "serialized: "+toJson(userTasks));

        String jsonified = toJson(userTasks);

        PrintWriter printWriter = new PrintWriter(file);
        printWriter.print(jsonified);
        printWriter.close();
    }

    public static ArrayList<UserTask> getTasks(Context context)
            throws FileNotFoundException, JSONException, ParseException {
        FileInputStream fis = context.openFileInput(filename);
        Log.i("service", "file found");
        Scanner scanner = new Scanner(fis);
        scanner.useDelimiter("\\Z");
        String content = scanner.next();
        scanner.close();

        Log.i("service", "content: "+content);

        ArrayList<UserTask> tasks = fromJson(content);
        for(UserTask task: tasks) {
            Log.i("service", "taskname: "+task.getDescription());
        }
        return tasks;
    }

    private static String toJson(List<UserTask> userTasks) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(UserTask task: userTasks) {
            JSONObject object = new JSONObject();
            object.put("id", task.getId());
            object.put("name", task.getDescription());
            object.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(task.getDateOfTask()));
            object.put("notification", task.isNotification());

            jsonArray.put(object);
        }
        return jsonArray.toString();
    }

    private static ArrayList<UserTask> fromJson(String listString) throws JSONException, ParseException {
        ArrayList<UserTask> userTasks = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(listString);
        Log.i("service", "array ok");

        for(int i=0; i!=jsonArray.length(); ++i) {
            JSONObject json_data = jsonArray.getJSONObject(i);
            Log.i("service", "object: "+json_data);
            userTasks.add(new UserTask(json_data.getString("name"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(json_data.getString("date").toString()),
                    json_data.getBoolean("notification")));
        }
        return userTasks;
    }
}
