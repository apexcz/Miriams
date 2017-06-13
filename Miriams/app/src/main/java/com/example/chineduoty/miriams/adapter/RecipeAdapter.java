package com.example.chineduoty.miriams.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chineduoty.miriams.R;
import com.example.chineduoty.miriams.model.Recipe;

import java.util.List;

/**
 * Created by chineduoty on 6/12/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> lstRecipes;
    private final RecipeAdapterOnClickHandler clickHandler;

    public RecipeAdapter(Context context, List<Recipe> recipes,RecipeAdapterOnClickHandler
                         handler){
        this.context = context;
        lstRecipes = recipes;
        clickHandler = handler;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_item,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = lstRecipes.get(position);
        holder.name.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if(lstRecipes == null) return 0;
        return  lstRecipes.size();
    }

    public void updateRecipes(List<Recipe> recipes){
        lstRecipes = recipes;
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

        public final TextView name;

        public RecipeViewHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.recipe_name_text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            Recipe recipe = lstRecipes.get(itemPosition);
            clickHandler.onClick(recipe);
        }
    }

    public interface RecipeAdapterOnClickHandler{
        void onClick(Recipe recipe);
    }
}
