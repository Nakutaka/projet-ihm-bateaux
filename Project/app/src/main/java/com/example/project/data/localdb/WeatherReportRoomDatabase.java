package com.example.project.data.localdb;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project.data.model.entities.Cloud;
import com.example.project.data.model.entities.Current;
import com.example.project.data.model.entities.Fog;
import com.example.project.data.model.entities.Hail;
import com.example.project.data.model.entities.Other;
import com.example.project.data.model.entities.Rain;
import com.example.project.data.model.entities.Report;
import com.example.project.data.model.entities.Storm;
import com.example.project.data.model.entities.Temperature;
import com.example.project.data.model.entities.Transparency;
import com.example.project.data.model.entities.Wind;
import com.example.project.types.ITypeIncident;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Report.class, Cloud.class, Current.class, Fog.class, Hail.class, Other.class,
        Rain.class, Storm.class, Temperature.class, Transparency.class, Wind.class},//Incident.class},
        version = 1, exportSchema = false)
public abstract class WeatherReportRoomDatabase extends RoomDatabase {

    public abstract WeatherReportDao weatherReportDao();

    private static volatile WeatherReportRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
           Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static WeatherReportRoomDatabase getDatabase(final Context context) {
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
            populate();
        };
    };

    public static void populate() {
        databaseWriteExecutor.execute(() -> {
            // Populate the database in the background.
            WeatherReportDao dao = INSTANCE.weatherReportDao();
            //dao.deleteAll();
            dao.deleteAllIncidentsCloud();
            dao.deleteAllIncidentsCurrent();
            dao.deleteAllIncidentsFog();
            dao.deleteAllIncidentsHail();
            dao.deleteAllIncidentsOther();
            dao.deleteAllIncidentsRain();
            dao.deleteAllIncidentsStorm();
            dao.deleteAllIncidentsTemperature();
            dao.deleteAllIncidentsTransparency();
            dao.deleteAllIncidentsWind();

            dao.deleteAllReports();

            Date currentDate = Calendar.getInstance().getTime();//possible de stocker Calendar too
            long time = currentDate.getTime();
            Report report = new Report(time, 43.7, 6.9966);
            dao.insert(report);

            Rain r = new Rain(time, report.getLatitude(), report.getLongitude(), ITypeIncident.CLOUD, 0,"Raining like a dog!", "value");
            dao.insert(r);
            Storm s = new Storm(time, report.getLatitude(), report.getLongitude(), ITypeIncident.STORM, 0, "Thunder... thunder... thunderstruck!", "value");
            dao.insert(s);

            currentDate = Calendar.getInstance().getTime();
            time = currentDate.getTime();
            report = new Report(time, 43.68, 6.89);
            dao.insert(report);

            Hail h  = new Hail(time, report.getLatitude(), report.getLongitude(), ITypeIncident.HAIL, 0, "Let it go! Let it goooo!", "value");
            dao.insert(h);
            Transparency t = new Transparency(time, report.getLatitude(), report.getLongitude(), ITypeIncident.TRANSPARENCY, 0, "Under the seeeeeaaaaaa!!", "value", "unit");
            dao.insert(t);
        });
    }
}