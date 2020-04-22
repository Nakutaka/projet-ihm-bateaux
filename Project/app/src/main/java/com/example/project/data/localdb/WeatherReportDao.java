package com.example.project.data.localdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.project.data.model.entities.Cloud;
import com.example.project.data.model.entities.Current;
import com.example.project.data.model.entities.Fog;
import com.example.project.data.model.entities.Hail;
import com.example.project.data.model.entities.Other;
import com.example.project.data.model.entities.Rain;
import com.example.project.data.model.entities.Report;
import com.example.project.data.model.entities.ReportWithIncidentsDB;
import com.example.project.data.model.entities.Storm;
import com.example.project.data.model.entities.Temperature;
import com.example.project.data.model.entities.Transparency;
import com.example.project.data.model.entities.Wind;

import java.util.List;

@Dao
public interface WeatherReportDao {
   //report
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   void insert(Report report);

   @Query("DELETE FROM Report")
   void deleteAllReports();

   @Transaction
   @Query("SELECT * from Report ORDER BY report_id DESC LIMIT 5")
   LiveData<List<ReportWithIncidentsDB>> getLastReportsWithIncidents();

   @Transaction
   @Query("SELECT * FROM Report")
   LiveData<List<ReportWithIncidentsDB>> getReportsWithIncidents();

   /*@Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Incident incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Incident... incidents);

   @Query("DELETE FROM incident")
   void deleteAllIncidents();*/

   //cloud
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Cloud incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Cloud... incidents);

   @Query("DELETE FROM cloud")
   void deleteAllIncidentsCloud();

   //current
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Current incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Current... incidents);

   @Query("DELETE FROM current")
   void deleteAllIncidentsCurrent();

   //fog
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Fog incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Fog... incidents);

   @Query("DELETE FROM fog")
   void deleteAllIncidentsFog();

   //hail
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Hail incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Hail... incidents);

   @Query("DELETE FROM hail")
   void deleteAllIncidentsHail();

   //other
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Other incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Other... incidents);

   @Query("DELETE FROM other")
   void deleteAllIncidentsOther();

   //rain
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Rain incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Rain... incidents);

   @Query("DELETE FROM rain")
   void deleteAllIncidentsRain();

   //storm
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Storm incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Storm... incidents);

   @Query("DELETE FROM storm")
   void deleteAllIncidentsStorm();

   //temperature
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Temperature incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Temperature... incidents);

   @Query("DELETE FROM temperature")
   void deleteAllIncidentsTemperature();

   //transparency
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Transparency incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Transparency... incidents);

   @Query("DELETE FROM transparency")
   void deleteAllIncidentsTransparency();

   //wind
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long insert(Wind incident);

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   long[] insert(Wind... incidents);

   @Query("DELETE FROM wind")
   void deleteAllIncidentsWind();
}