package com.example.chineduoty.miriams.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chineduoty on 6/25/17.
 */

public class RecipeDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "recipe.db";
    public static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
          final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " +
                RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_INGREDIENT_MEASURE + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_INGREDIENT_QUANTITY + " INTEGER NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
