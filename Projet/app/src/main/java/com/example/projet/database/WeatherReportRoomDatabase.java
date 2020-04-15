package com.example.projet.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.projet.database.models.Incident;
import com.example.projet.database.models.Report;
import com.example.projet.database.models.ReportWithIncidents;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Report.class, Incident.class},
        version = 1, exportSchema = false)
public abstract class WeatherReportRoomDatabase extends RoomDatabase {

    public abstract WeatherReportDao wordDao();

    private static volatile WeatherReportRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
           Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static WeatherReportRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherReportRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherReportRoomDatabase.class, "weather_report_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
   }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {//onCreate//onOpen... re-init at each start
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            populate();
        };
    };

    static void populate() {
        databaseWriteExecutor.execute(() -> {
            // Populate the database in the background.
            // If you want to start with more words, just add them.
            WeatherReportDao dao = INSTANCE.wordDao();
            //dao.deleteAll();
            dao.deleteAllIncidents();
            dao.deleteAllReports();

            Date currentDate = Calendar.getInstance().getTime();
            long time = currentDate.getTime();
            Report report = new Report(time);
            //long id =
            dao.insert(report);
            ReportWithIncidents weatherReport = new ReportWithIncidents(report);
            weatherReport.addIncident(time, 0, "Raining like a dog!");
            weatherReport.addIncident(time, 1, "Thunder... thunder... thunderstruck!");
            weatherReport.incidents.forEach(dao::insert);

            currentDate = Calendar.getInstance().getTime();
            time = currentDate.getTime();
            report = new Report(time);
            //id =
            dao.insert(report);
            weatherReport = new ReportWithIncidents(report);
            weatherReport.addIncident(time, 0, "S.O.S");
            weatherReport.addIncident(time, 1, "Under the seeeeeaaaaaa!!");
            weatherReport.incidents.forEach(dao::insert);
        });
    }
}