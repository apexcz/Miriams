package com.example.chineduoty.miriams.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chineduoty.miriams.R;
import com.example.chineduoty.miriams.model.Ingredient;

import java.util.List;

/**
 * Created by chineduoty on 6/13/17.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private Context context;
    private List<Ingredient> lstIngredients;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients){
        this.context = context;
        lstIngredients = ingredients;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =  LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.ingredient_item,parent,false);

        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {

        Ingredient ingredient = lstIngredients.get(position);

        holder.textQuantity.setText(""+ingredient.getQuantity());
        holder.textMeasure.setText(""+ingredient.getMeasure());
        holder.textIngredient.setText(""+ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        if(lstIngredients == null) return 0;
        return lstIngredients.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder{

        public final TextView textQuantity,textMeasure,textIngredient;

        public IngredientsViewHolder(View view){
            super(view);
            textQuantity = (TextView)view.findViewById(R.id.quantity_text);
            textMeasure = (TextView)view.findViewById(R.id.measure_text);
            textIngredient = (TextView)view.findViewById(R.id.ingredient_text);
        }
    }
}
