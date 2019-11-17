package com.example.www.petdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.PeriodicSync;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.www.petdetails.data.PetContract;
import com.example.www.petdetails.data.PetDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TextView petTextView;
    PetDBHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        petTextView = (TextView)findViewById(R.id.pet_text_view);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PetAddActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new PetDBHelper(this);
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo(){

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                PetContract.PetEntry.COLUMN_ID,
                PetContract.PetEntry.COLUMN_NAME,
                PetContract.PetEntry.COLUMN_BREED,
                PetContract.PetEntry.COLUMN_GENDER,
                PetContract.PetEntry.COLUMN_WEIGHT};

        Cursor cursor = db.query(
                PetContract.PetEntry.TABLIE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        try{
            petTextView.setText("The pets table contains "+
                    cursor.getCount()+" pets.\n\n");
            petTextView.append(PetContract.PetEntry.COLUMN_ID+" - "+
                    PetContract.PetEntry.COLUMN_NAME+" - "+
                    PetContract.PetEntry.COLUMN_BREED+" - "+
                    PetContract.PetEntry.COLUMN_GENDER+" - "+
                    PetContract.PetEntry.COLUMN_WEIGHT+"\n");

            int idColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_ID);
            int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_WEIGHT);

            while (cursor.moveToNext()){
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentWeight =cursor.getInt(weightColumnIndex);

                petTextView.append("\n"+ currentId+" - "+
                        currentName+" - "+
                        currentBreed+" - "+
                        currentGender+" - "+
                        currentWeight);
            }

        }finally {
            cursor.close();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    private void insertData(){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_NAME,"TOTO");
        values.put(PetContract.PetEntry.COLUMN_BREED,"Terrier");
        values.put(PetContract.PetEntry.COLUMN_GENDER, PetContract.PetEntry.MALE_GENDER);
        values.put(PetContract.PetEntry.COLUMN_WEIGHT,7);

        long rowId = db.insert(PetContract.PetEntry.TABLIE_NAME,null,values);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }
}
