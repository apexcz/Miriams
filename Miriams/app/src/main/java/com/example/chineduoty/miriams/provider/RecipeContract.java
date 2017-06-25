package com.example.chineduoty.miriams.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chineduoty on 6/25/17.
 */

public class RecipeContract {

    public static final String AUTHORITY = "com.example.chineduoty.miriams";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECIPES = "recipes";

    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME = "recipes";

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
        public static final String COLUMN_INGREDIENT_MEASURE = "ingredient_measure";
        public static final String COLUMN_INGREDIENT_QUANTITY = "ingredient_quantity";


    }
}
