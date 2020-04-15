package com.example.projet.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.projet.database.models.Incident;
import com.example.projet.database.models.Report;
import com.example.projet.database.models.ReportWithIncidents;

import java.util.List;

@Dao
public interface WeatherReportDao {

   // allowing the insert of the same word multiple times by passing a 
   // conflict resolution strategy

   //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   void insert(Report report);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Incident incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Incident... incidents);

   @Query("DELETE FROM incident")
   void deleteAllIncidents();

   @Query("DELETE FROM report")
   void deleteAllReports();

   /*@Transaction
   @Query("SELECT * from report ORDER BY report_id DESC LIMIT 10")
   LiveData<List<ReportWithIncidents>> getLastReportsWithIncidents();*/

   @Transaction
   @Query("SELECT * from report ORDER BY report_id DESC LIMIT 10")
   LiveData<List<ReportWithIncidents>> getLastReportsWithIncidents();

   @Transaction
   @Query("SELECT * FROM report")
   LiveData<List<ReportWithIncidents>> getReportsWithIncidents();

}