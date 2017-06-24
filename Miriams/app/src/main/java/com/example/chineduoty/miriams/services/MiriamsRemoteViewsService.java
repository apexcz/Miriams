package com.example.chineduoty.miriams.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.chineduoty.miriams.MainActivity;
import com.example.chineduoty.miriams.adapter.MiriamsRemoteViewsFactory;
import com.example.chineduoty.miriams.model.Ingredient;
import com.example.chineduoty.miriams.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by chineduoty on 6/24/17.
 */

public class MiriamsRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new MiriamsRemoteViewsFactory(this.getApplicationContext(),intent);
    }
}
