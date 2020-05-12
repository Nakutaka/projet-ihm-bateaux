package com.example.project.database.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.project.model.weather.local.incident.BasicIncident;
import com.example.project.model.weather.local.incident.MeasuredIncident;
import com.example.project.model.weather.local.incident.MinIncident;
import com.example.project.model.weather.Report;
import com.example.project.model.weather.WeatherReport;

import java.util.List;

@Dao
public interface WeatherReportDao {

   @Transaction
   @Query("SELECT * from Report")
   LiveData<List<WeatherReport>> getAllReportsWithIncidents();

   @Transaction
   @Query("SELECT * from Report WHERE time >= :time ORDER BY id DESC LIMIT 10")
   LiveData<List<WeatherReport>> getLastReportsWithIncidents(long time);

   @Transaction
   @Query("SELECT * from Report ORDER BY id DESC LIMIT 10")
   LiveData<List<WeatherReport>> getTenLastReportsWithIncidents();

   /*:::::::::::::::::::::::::::::::::::::::::report:::::::::::::::::::::::::::::::::::::::::*/

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   void insert(Report report);
/*
   @Transaction
   @Query("SELECT * from Report WHERE time >= :time ORDER BY id DESC LIMIT 10")
   LiveData<List<Report>> getLastReports(long time);

   @Transaction
   @Query("SELECT * FROM Report")
   LiveData<List<Report>> getAllReports();
*/
   @Query("DELETE FROM Report")
   void deleteAllReports();

   @Query("DELETE FROM Report WHERE id = :id")
   void deleteReport(String id);

   /*::::::::::::::::::::::::::::::::::::::::incidents::::::::::::::::::::::::::::::::::::::::*/

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   void insert(MinIncident incident);

   /*@Transaction
   @Query("SELECT * FROM MinIncident WHERE report_id = :reportId ORDER BY num")
   LiveData<List<MinIncident>> getAllMinIncidents(String reportId);
*/
   @Query("DELETE FROM MinIncident WHERE report_id = :reportId")
   void deleteAllMinIncidents(String reportId);

   @Query("DELETE FROM MinIncident")
   void deleteAllMinIncidents();


   @Insert(onConflict = OnConflictStrategy.IGNORE)
   void insert(BasicIncident incident);
/*
   @Transaction
   @Query("SELECT * FROM BasicIncident WHERE report_id = :reportId ORDER BY num")
   LiveData<List<BasicIncident>> getAllBasicIncidents(String reportId);
*/
   @Query("DELETE FROM BasicIncident WHERE report_id = :reportId")
   void deleteAllBasicIncidents(String reportId);

   @Query("DELETE FROM BasicIncident")
   void deleteAllBasicIncidents();


   @Insert(onConflict = OnConflictStrategy.IGNORE)
   void insert(MeasuredIncident incident);
/*
   @Transaction
   @Query("SELECT * FROM MeasuredIncident WHERE report_id = :reportId ORDER BY num")
   LiveData<List<MeasuredIncident>> getAllMeasuredIncidents(String reportId);
*/
   @Query("DELETE FROM MeasuredIncident WHERE report_id = :reportId")
   void deleteAllMeasuredIncidents(String reportId);

   @Query("DELETE FROM MeasuredIncident")
   void deleteAllMeasuredIncidents();
}