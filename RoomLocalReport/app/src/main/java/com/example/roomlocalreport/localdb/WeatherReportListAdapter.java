package com.example.roomlocalreport.localdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomlocalreport.R;
import com.example.roomlocalreport.localdb.models.ReportWithIncidents;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class WeatherReportListAdapter extends RecyclerView.Adapter<WeatherReportListAdapter.WeatherReportViewHolder> {

   class WeatherReportViewHolder extends RecyclerView.ViewHolder {
       private final TextView reportItemView;

       private WeatherReportViewHolder(View itemView) {
           super(itemView);
           reportItemView = itemView.findViewById(R.id.textView);
       }
   }

   private final LayoutInflater mInflater;
   private List<ReportWithIncidents> mReports; // Cached copy of weather reports

   public WeatherReportListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

   @NonNull
   @Override
   public WeatherReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
       return new WeatherReportViewHolder(itemView);
   }

   @Override
   public void onBindViewHolder(@NonNull WeatherReportViewHolder holder, int position) {
       if (mReports != null) {
           ReportWithIncidents current = mReports.get(position);
           //holder.reportItemView.setText(current.getWord());
           AtomicReference<String> chain = new AtomicReference<String>("\n"+current.incidents.size() +
                   (current.incidents.size()>1? " incidents" : " incident"));
           current.incidents.forEach(i -> {
               chain.set(chain.get() + "\n" + i.getIncidentId() + ": " + i.getComment());
           });
           holder.reportItemView.setText("Report - " + (int)current.report.getReportId() + chain);
           //current.incidents.forEach(i -> holder.reportItemView.setText(i.getIncidentId() + ": " + i.getComment()));
       } else {
           // Covers the case of data not being ready yet.
           holder.reportItemView.setText("No Report");
       }
   }

   public void setReports(List<ReportWithIncidents> reports){
       mReports = reports;
       notifyDataSetChanged();
   }

   // getItemCount() is called many times, and when it is first called,
   // mWords has not been updated (means initially, it's null, and we can't return null).
   @Override
   public int getItemCount() {
       if (mReports != null)
           return mReports.size();
       else return 0;
   }
}