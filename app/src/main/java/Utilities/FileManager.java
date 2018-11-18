package Utilities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class FileManager extends AppCompatActivity{
    private EditText editText;
    private Context context;

    public FileManager(EditText editText, Context context){
        this.editText = editText;
        this.context = context;
    }

    public FileManager(Context context){
        this.context = context;
    }

    //Takes text from the edit text and saves it to a file.
    public void save(String fileName) {
        try { //Open output stream in the correct context.
            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(fileName, 0));
            out.write(editText.getText().toString());
            out.close();
        } catch (Throwable t) {
            Toast.makeText(context, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //Function used to check if the specified file exists.
    public boolean fileExists(String fName){
        File file = context.getFileStreamPath(fName);
        return file.exists();
    }

    //Open the file to display to the screen.
    public String open(String fName) {
        String result = ""; //Will hold all of the text read in from the file.
        if (fileExists(fName)) {
            try {
                InputStream in = context.openFileInput(fName); //Open file for reading in the correct context.
                if (in != null) { //Keep reading as long as the input stream is not null.
                    InputStreamReader tempReader = new InputStreamReader(in);
                    BufferedReader bufferedReader = new BufferedReader(tempReader); //Temp reader is loaded into the buffer.
                    String string;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((string = bufferedReader.readLine()) != null) {//Read from buffered reader until null.
                        stringBuilder.append(string + "\n");
                    }
                    in .close(); //Close the input stream once all lines are read.
                    result = stringBuilder.toString(); //Save string builder to result string.
                }
            } catch (java.io.FileNotFoundException e) {}
            catch (Throwable t) {
                Toast.makeText(context, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return result;
    }

    public ArrayList<String> getTasks(String text) {
        ArrayList<String> results = new ArrayList();

        String[] tempArray = text.split("\n");
        for (int i = 0; i < tempArray.length; i++) {
            if (!tempArray[i].trim().equals(""))
                results.add("• " + tempArray[i].trim());
        }
        return results;
    }
}
