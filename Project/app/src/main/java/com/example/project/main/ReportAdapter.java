package com.example.project.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.data.model.Incident;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    List<Incident> incidents;

    public ReportAdapter(List<Incident> incidents) {
        this.incidents = incidents;
    }

    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_incidents, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ReportAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Incident incident = incidents.get(position);
        // Set item views based on your views and data model
        TextView tv1 = viewHolder.tv_NameIncident;
        tv1.setText(incident.getInfo().getName());
        TextView tv2 = viewHolder.tv_value;
        tv2.setText(incident.getValue());
        TextView tv3 = viewHolder.tv_commentIncident;
        tv3.setText(incident.getComment());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return incidents.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_NameIncident;
        TextView tv_commentIncident;
        TextView tv_value;
        ImageView iv_icon;

        ViewHolder(View itemView) {
            super(itemView);

            tv_NameIncident = itemView.findViewById(R.id.txtNameIncident);
            tv_commentIncident = itemView.findViewById(R.id.txtCommentIncident);
            tv_value = itemView.findViewById(R.id.txtValueIncident);
            iv_icon = itemView.findViewById(R.id.iconIncident);
        }
    }
}
