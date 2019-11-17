package com.example.www.petdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.www.petdetails.data.PetContract.PetEntry;
import com.example.www.petdetails.data.PetDBHelper;

import java.util.PriorityQueue;

public class PetAddActivity extends AppCompatActivity {

    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;
    private int mGender = 0;
    private PetDBHelper mPetDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_add);

        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupSpinner();
        mPetDbHelper = new PetDBHelper(this);
    }
    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetEntry.MALE_GENDER; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetEntry.FEMALE_GENDER; // Female
                    } else {
                        mGender = PetEntry.UNKNOWN_GENDER; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    public long insertData(){
        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();

        String name = mNameEditText.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,name+"cannot be entry",Toast.LENGTH_SHORT).show();
            return -1;
        }
        String breed = mBreedEditText.getText().toString().trim();
        if(TextUtils.isEmpty(breed)){
            Toast.makeText(this,name+"cannot be entry",Toast.LENGTH_SHORT).show();
            return -1;
        }
        int weight = Integer.parseInt(mWeightEditText.getText().toString().trim());
        if(TextUtils.isEmpty(weight+"")){
            Toast.makeText(this,weight+"cannot be entry",Toast.LENGTH_SHORT).show();
            return -1;
        }
        int gender = mGender;
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_NAME,name);
        values.put(PetEntry.COLUMN_BREED,breed);
        values.put(PetEntry.COLUMN_GENDER,gender);
        values.put(PetEntry.COLUMN_WEIGHT,weight);

        long id = db.insert(PetEntry.TABLIE_NAME,null,values);
        return id;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                long id = insertData();
                if(id==-1){
                    Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,id+"",Toast.LENGTH_SHORT).show();
                }
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
