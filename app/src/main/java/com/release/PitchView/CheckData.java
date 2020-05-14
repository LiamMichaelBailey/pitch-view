package com.release.PitchView;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.widget.TextView;

import java.util.List;

// Liam Bailey
/* This class read data from a database using a select all statement into a list view and bundles
    data to passed to a new activity*/
public class CheckData extends ListActivity {

    DatabaseManipulator dm;

    List<String[]> session2 = null;
    String[] stg1;
    String[] titles;
    String[] notes;
    String[] imageStrs;
    String[] ids;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_data);

        // Create db connection and string arrays needed to read from db.
        dm = new DatabaseManipulator(this);
        session2 = dm.selectAll();
        stg1 = new String[session2.size()];
        titles = new String[session2.size()];
        notes = new String[session2.size()];
        imageStrs = new String[session2.size()];
        ids = new String[session2.size()];
        int x = 0;
        String stg;

        // Cycle through each session in the list.
        for (String[] session : session2) {
            // If the session title is greater than 10 chars
            // Fill individual value arrays
            if(session[2].length() > 10) {
                stg = session[1] + " - "
                        + session[2].substring(0, 20) + "...";
                stg1[x] = stg;
                titles[x] = session[1];
                notes[x] = session[2];
                imageStrs[x] = session[3];
                ids[x] = session[0];
                x++;
            }

            else
            {
                stg = session[1] + " - "
                        + session[2];
                stg1[x] = stg;

                titles[x] = session[1];
                notes[x] = session[2];
                imageStrs[x] = session[3];
                ids[x] = session[0];
                x++;
            }
        }
        // Set the list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                stg1);
        this.setListAdapter(adapter);
    }

    // Method to handle list item click
    public void onListItemClick(ListView parent, View v, int position, long id) {
        // Bundle session data and send to new activity.
        String selectedTitle = titles[position];
        String selectedNote = notes[position];
        String selectedImage = imageStrs[position];
        int selectedID = Integer.parseInt(ids[position]);
        Intent intent = new Intent(this, DisplaySession.class);
        intent.putExtra("SELECTEDTITLE", selectedTitle);
        intent.putExtra("SELECTEDNOTE", selectedNote);
        intent.putExtra("SELECTEIMAGESTR",selectedImage);
        intent.putExtra("SELECTEDID", selectedID);
        startActivity(intent);
    }
}
