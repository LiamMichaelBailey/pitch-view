package com.release.PitchView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

// Activity to display an individual session.
public class DisplaySession extends AppCompatActivity {

    private DatabaseManipulator dm;

    private FloatingActionButton FabDelete;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_session);

        // Get bundled data from CheckData
        Bundle extras = getIntent().getExtras();

        String title = extras.getString("SELECTEDTITLE");
        String notes = extras.getString("SELECTEDNOTE");
        String imageStr = extras.getString("SELECTEIMAGESTR");
        final int id = extras.getInt("SELECTEDID");

        Bitmap recoveredImage = retriveBitmap(imageStr);

        // Set views to passed data.
        TextView titelView = findViewById(R.id.sessionTitle);
        TextView notesView = findViewById(R.id.sessionNotes);
        ImageView imageView = findViewById(R.id.sessionImage);

        titelView.setText(title);
        notesView.setText(notes);
        imageView.setImageBitmap(recoveredImage);

        // Button to delete the session.
        this.dm = new DatabaseManipulator(this);

        FabDelete = findViewById(R.id.menu_Delete);

        FabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm.deleteEntry(id);
                Toast.makeText(getApplicationContext(),"Session deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DisplaySession.this, CheckData.class);
                startActivity(intent);
            }
        });
    }

    // Method to convert string from db to bitmap.
    public Bitmap retriveBitmap(String encodedImage)
    {
        byte data[]= android.util.Base64.decode(encodedImage, android.util.Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

        return bmp;
    }
}
