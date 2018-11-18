package csc472.depaul.edu.DailyPlanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Utilities.FileManager;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendar;
    private TextView date;
    private TextView noTasksMessage;
    private FloatingActionButton editButton;
    private FileManager fileManager;
    private String fileName;
    private String fileText;
    private String selectedDate; //Corresponds to the selected day in the calendar view.
    private ArrayList<String> tasks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.calendar_layout);
        calendar       = findViewById(R.id.calendarView);
        date           = findViewById(R.id.dateText);
        noTasksMessage = findViewById(R.id.noTasksMessage);
        editButton     = findViewById(R.id.edit);
        tasks = new ArrayList();
        fileManager = new FileManager(getApplicationContext());

        //Get the initial date to pass to showPreview() as a file name argument.
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat form = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        fileName = form.format(date);
        showPreview(fileName); //Display the current date's tasks.
        selectedDate = fileName.replace('-', '/');

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { //Change the selected date and show preview of tasks.
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                selectedDate = new String(month + 1 + "/" + dayOfMonth + "/" + year);
                showPreview(new String(month + 1 + "-" + dayOfMonth + "-" + year));
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() { //Button takes us to the agenda activity.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, AgendaActivity.class);
                intent.putExtra("date", selectedDate);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPreview(fileName); //Check to see if new tasks are created.
    }

    private void showPreview(String fName){
        fileText = fileManager.open(fName);
        if (fileText.trim().equals("")){ //If file is empty, show the standard message.
            noTasksMessage.setVisibility(View.VISIBLE);
            tasks.removeAll(tasks);
        } else { //Otherwise replace the tasks list with the new file contents.
            noTasksMessage.setVisibility(View.GONE);
            tasks = fileManager.getTasks(fileText);
        }

        //Build the list view to show the updated version of the tasks list.
        ArrayAdapter adapter = new ArrayAdapter(CalendarActivity.this, android.R.layout.simple_list_item_1, tasks);
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);

        this.date.setText(fName.replace('-', '/'));
    }
}