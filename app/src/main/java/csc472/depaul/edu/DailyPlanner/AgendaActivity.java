package csc472.depaul.edu.DailyPlanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Utilities.FileManager;

public class AgendaActivity extends AppCompatActivity {

    private FileManager fileManager;
    private FloatingActionButton saveAndQuitButton;
    private EditText editText;
    private TextView dateText;
    private String date;
    private String fileName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.agenda_layout);
        editText = findViewById(R.id.EditText);
        dateText = findViewById(R.id.dateText);

        Intent incoming = getIntent();
        date = incoming.getStringExtra("date");
        dateText.setText(date);
        fileName = date.replace('/', '-');
        fileManager = new FileManager(editText, getApplicationContext());

        saveAndQuitButton = findViewById(R.id.saveAndQuit);
        saveAndQuitButton.setOnClickListener(new View.OnClickListener() { //Save the file.
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().matches("")) { //If the file has contents, display message that it was saved.
                    Toast.makeText(AgendaActivity.this, String.format("Notes saved for %s!", fileName), Toast.LENGTH_LONG).show();
                }
                fileManager.save(fileName); //Save file.
                Intent intent = new Intent(AgendaActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); //Return to main screen.
            }
        });

        editText.setText(fileManager.open(fileName)); //Try loading in the file if there is one saved.
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fileManager.save(fileName); //Save file.
        Toast.makeText(AgendaActivity.this, "Return to calendar", Toast.LENGTH_SHORT).show();
    }
}
