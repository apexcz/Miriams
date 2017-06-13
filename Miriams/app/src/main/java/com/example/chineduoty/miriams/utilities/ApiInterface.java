package com.example.chineduoty.miriams.utilities;

import com.example.chineduoty.miriams.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chineduoty on 6/12/17.
 */

public interface ApiInterface {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
