package com.example.project.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        TextView tv_name = viewHolder.tv_NameIncident;
        if (incident.getInfo().getName() != null)
            tv_name.setText(String.format("Incident: %s", incident.getInfo().getName()));
        else tv_name.setVisibility(View.GONE);

        TextView tv_value = viewHolder.tv_value;
        if (incident.getValue() != null)
            tv_value.setText(String.format("Information:  %s", incident.getValue()));
        else tv_value.setVisibility(View.GONE);

        TextView tv_comment = viewHolder.tv_commentIncident;
        if (incident.getComment() != null)
            tv_comment.setText(String.format("Comment: %s", incident.getComment()));
        else tv_comment.setVisibility(View.GONE);
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
        Button btn_close;

        ViewHolder(View itemView) {
            super(itemView);

            tv_NameIncident = itemView.findViewById(R.id.txtNameIncident);
            tv_commentIncident = itemView.findViewById(R.id.txtCommentIncident);
            tv_value = itemView.findViewById(R.id.txtValueIncident);
            iv_icon = itemView.findViewById(R.id.iconIncident);
        }
    }
}
