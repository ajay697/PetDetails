package com.example.www.petdetails.data;

import android.provider.BaseColumns;

public final class PetContract {

    private PetContract(){}
    public static final class PetEntry implements BaseColumns{

        public static final String TABLIE_NAME = "Pets";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_BREED = "breed";
        public static final String COLUMN_WEIGHT = "weight";


        public static final int ID = 1;
        public static final int MALE_GENDER = 1;
        public static final int UNKNOWN_GENDER = 0;
        public static final int FEMALE_GENDER = 2;
        public static final String BREED ="null";


    }
    
}
