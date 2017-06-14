package com.example.chineduoty.miriams.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chineduoty.miriams.R;
import com.example.chineduoty.miriams.model.Step;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by chineduoty on 6/13/17.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private Context context;
    private List<Step> lstSteps;
    private StepAdapterClickHandler clickHandler;

    public StepsAdapter(Context context, List<Step> steps, StepAdapterClickHandler handler){
        this.context = context;
        lstSteps = steps;
        clickHandler = handler;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.step_item,parent,false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        Step step = lstSteps.get(position);

        holder.textStepCount.setText(""+ (position + 1));
        holder.textStepTitle.setText(step.getShortDescription());
        holder.textStepDescription.setText(step.getDescription());


    }

    @Override
    public int getItemCount() {
        if(lstSteps == null) return 0;
        return lstSteps.size();
    }

    public  void updateSteps(List<Step> steps){
        lstSteps = steps;
        notifyDataSetChanged();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements
    View.OnClickListener{
        public final TextView textStepCount,textStepTitle,textStepDescription;

        public StepsViewHolder(View view){
            super(view);
            textStepCount = (TextView)view.findViewById(R.id.step_count_text);
            textStepTitle = (TextView)view.findViewById(R.id.step_title_text);
            textStepDescription  =(TextView)view.findViewById(R.id.step_description_text);

            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            Step step = lstSteps.get(itemPosition);
            clickHandler.onClick(step);
        }
    }

    public interface StepAdapterClickHandler{
        void onClick(Step step);
    }
}
