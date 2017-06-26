package com.example.chineduoty.miriams.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chineduoty.miriams.R;
import com.example.chineduoty.miriams.RecipeDetailActivity;
import com.example.chineduoty.miriams.adapter.IngredientsAdapter;
import com.example.chineduoty.miriams.adapter.StepsAdapter;
import com.example.chineduoty.miriams.model.Recipe;
import com.example.chineduoty.miriams.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chineduoty on 6/25/17.
 */

public class MasterListFragment extends Fragment implements StepsAdapter.StepAdapterClickHandler {

    OnStepItemClickListener mCallback;

    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;
    private Recipe recipe;
    private List<Step> lstStep;

    @BindView(R.id.recipe_name_text)
    TextView recipeName;
    @BindView(R.id.recipe_serving_text)
    TextView recipeServing;
    @BindView(R.id.ingredient_rv)
    RecyclerView ingredientRecyclerView;
    @BindView(R.id.steps_rv)
    RecyclerView stepRecyclerView;

    public interface OnStepItemClickListener {
        void onStepClicked(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    public MasterListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        recipe = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_TEXT);

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, rootView);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ingredientRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {
            ingredientRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        }

        ingredientsAdapter = new IngredientsAdapter(getActivity(), recipe.getIngredients());
        ingredientRecyclerView.setAdapter(ingredientsAdapter);


        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lstStep = recipe.getSteps();
        stepsAdapter = new StepsAdapter(getActivity(), lstStep, this);
        stepRecyclerView.setAdapter(stepsAdapter);

        recipeName.setText(recipe.getName());
        recipeServing.setText("" + recipe.getServings());

        return rootView;
    }

    @Override
    public void onClick(int position) {
        mCallback.onStepClicked(position);
    }
}
