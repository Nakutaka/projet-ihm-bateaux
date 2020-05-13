package com.example.project.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.weather.local.Incident;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private List<Incident> incidents;
    private Context context;

    public ReportAdapter(List<Incident> incidents, Context context) {
        this.incidents = incidents;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_incidents, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ReportAdapter.ViewHolder viewHolder, int position) {
        Incident incident = incidents.get(position);
        TextView tv_name = viewHolder.tv_NameIncident;
        if (incident.getInfo().getName() != null)
            tv_name.setText(String.format("Incident: %s", incident.getInfo().getName()));
        else tv_name.setVisibility(View.GONE);

        TextView tv_value = viewHolder.tv_value;
        if (incident.getOtherInfo() != null)
            tv_value.setText(String.format("Information:  %s", incident.getOtherInfo()));
        else tv_value.setVisibility(View.GONE);

        TextView tv_comment = viewHolder.tv_commentIncident;
        if (incident.getComment() != null)
            tv_comment.setText(String.format("Comment: %s", incident.getComment()));
        else tv_comment.setVisibility(View.GONE);



        ImageView iv_icon = viewHolder.iv_icon;
        if (incident.getInfo().getIcon() != null) {
            String sDrawable = incident.getInfo().getIcon().replace("R.drawable.", "");
            int id = context.getResources().getIdentifier(sDrawable, "drawable", context.getPackageName());
            iv_icon.setImageDrawable(context.getDrawable(id));
        }
    }

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
            iv_icon = itemView.findViewById(R.id.icon_details);
        }
    }
}
