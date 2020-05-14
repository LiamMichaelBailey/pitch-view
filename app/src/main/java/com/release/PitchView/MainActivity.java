package com.release.PitchView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import  com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/* Liam Bailey
*  Main activity that holds fragments and FAB menu*/
public class MainActivity extends AppCompatActivity {

    // Initialise fragment classes.
    private SectionsPageAdapter mSectionsPageAdapter;
    private NonSwipeableViewPager mViewPager;

    // Initialise db
    private DatabaseManipulator dm;

    // Initialise FAB buttons
    private FloatingActionMenu FAM;
    private FloatingActionButton FabSave, FabClear, FabView, FabPitch;

    // Array of drawable addresses
    final int pitches[] = {R.drawable.pitch, R.drawable.basketball, R.drawable.hockey};
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.dm = new DatabaseManipulator(this);

        // Set up tab layout and fragments
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.pitchicon);
        tabLayout.getTabAt(1).setIcon(R.drawable.notesicon);


        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        FAM = findViewById(R.id.menu);
        FabClear = findViewById(R.id.menu_clear);
        FabSave = findViewById(R.id.menu_save);
        FabView = findViewById(R.id.menu_viewData);
        FabPitch = findViewById(R.id.menu_swapPitch);


        // FAB menu listeners.
        // Change pitch layout.
        FabPitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = findViewById(R.id.imageView);

                counter++;
                counter = counter % pitches.length;

                imageView.setImageResource(pitches[counter]);

            }
        });

        // Open session list view activity.
        FabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this, CheckData.class);
                startActivity(i1);
            }
        });

        // Clear the canvas.
        FabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomCanvas canvas = findViewById(R.id.canvas);
                canvas.clearCanvas();
            }
        });

        // Save data to the db.
        FabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((EditText) findViewById(R.id.title)).getText().toString();
                String notes = ((EditText) findViewById(R.id.notes)).getText().toString();

                // Convert canvas to bitmap string
                CustomCanvas canvas = findViewById(R.id.canvas);
                ImageView iv = findViewById(R.id.imageView);

                Bitmap back = imageViewToBitmap(iv);
                Bitmap front = canvas.getBitmap();

                Bitmap merge = mergeBitmaps(back, front);

                String imageStr = getStringImage(merge);

                // Insert to db.
                dm.insert(title, notes, imageStr);

                Toast.makeText(getApplicationContext(),"Session saved", Toast.LENGTH_SHORT).show();
            }
        });


    }


    // Set up the viewPager
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new PitchFragment(), "Pitch");
        adapter.addFragment(new NotesFragment(), "Notes");

        viewPager.setAdapter(adapter);

    }

    // Method to merge two bitmaps together.
    public static Bitmap mergeBitmaps(Bitmap back, Bitmap front) {
        Bitmap result = Bitmap.createBitmap(back.getWidth(), back.getHeight(), back.getConfig());
        Canvas canvas = new Canvas(result);
        int widthBack = back.getWidth();
        int widthFront = front.getWidth();
        float move = (widthBack - widthFront) / 2;
        canvas.drawBitmap(back, 0f, 0f, null);
        canvas.drawBitmap(front, move, move, null);
        return result;
    }

    // Method to convert a bitmap to byte data and then a string.
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    // Method to convert a view to a bitmap
    public Bitmap imageViewToBitmap(ImageView iv)
    {
        iv.buildDrawingCache();
        Bitmap bmap = iv.getDrawingCache();

        return bmap;
    }

}
