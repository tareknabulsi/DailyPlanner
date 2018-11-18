package csc472.depaul.edu.DailyPlanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Utilities.FileManager;

public class MainActivity extends AppCompatActivity {

    private FileManager fileManager;
    private TextView date;
    private TextView noTasksMessage;
    private Button calendarButton;
    private String fileText;
    private ListView listView;
    private ArrayList<String> tasks;
    private ArrayList<Integer> markedPositions;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        date            = findViewById(R.id.date);
        calendarButton  = findViewById(R.id.goToCalendar);
        noTasksMessage  = findViewById(R.id.noTasksMessage);
        tasks           = new ArrayList();
        markedPositions = new ArrayList();
        fileText        = "";
        fileManager = new FileManager(getApplicationContext());

        getTheDateAndTasks(); //Display the date and today's tasks.

        calendarButton.setOnClickListener(new View.OnClickListener() { //Button takes us to the calendar activity.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                markedPositions.add(position); //Save the positions of the checked tasks.
            }
        });
    }

    @Override //Save the positions of the checked tasks to the save instance state.
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putIntegerArrayList("list", markedPositions);
    }

    //TODO: On Restore Instance isn't being called, which isn't allowing us to restore the markedPositions Array.

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Toast.makeText(MainActivity.this, savedInstanceState.getIntegerArrayList("list").toString(), Toast.LENGTH_SHORT).show();
        markedPositions = savedInstanceState.getIntegerArrayList("list");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTheDateAndTasks(); //Check to see if new tasks are created.
    }

    //Function to show the current date and display the associated tasks.
    private void getTheDateAndTasks(){
        //Get date first.
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat form = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        String theDate = form.format(date);
        this.date.setText(theDate);

        //Then get file name and open contents.
        SimpleDateFormat form2 = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        String fileName = form2.format(date);
        fileText = fileManager.open(fileName);
        if (fileText.trim().equals("")) { //If file is empty, show the "No Tasks" message.
            Toast.makeText(MainActivity.this, "No tasks!", Toast.LENGTH_SHORT).show();
            noTasksMessage.setVisibility(View.VISIBLE);
            tasks.removeAll(tasks); //Remove any leftover contents from the tasks list.
        } else { //Otherwise show the file contents.
            noTasksMessage.setVisibility(View.GONE);
            tasks = fileManager.getTasks(fileText);
        }

        //Build the list view to display the items in the tasks list.
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_checked, tasks);
        listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
    }
}
