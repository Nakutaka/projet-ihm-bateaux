package com.example.projet.database.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.projet.database.models.Cloud;
import com.example.projet.database.models.Current;
import com.example.projet.database.models.Fog;
import com.example.projet.database.models.Hail;
import com.example.projet.database.models.Other;
import com.example.projet.database.models.Rain;
import com.example.projet.database.models.Report;
import com.example.projet.database.models.Storm;
import com.example.projet.database.models.Temperature;
import com.example.projet.database.models.Transparency;
import com.example.projet.database.models.Wind;
import com.example.projet.types.ITypeIncident;

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

            Rain r = new Rain(time, ITypeIncident.INCIDENT_CLOUD, "Raining like a dog!");
            dao.insert(r);
            Storm s = new Storm(time, ITypeIncident.INCIDENT_STORM, "Thunder... thunder... thunderstruck!");
            dao.insert(s);

            currentDate = Calendar.getInstance().getTime();
            time = currentDate.getTime();
            report = new Report(time, 43.68, 6.89);
            dao.insert(report);

            Hail h  = new Hail(time, ITypeIncident.INCIDENT_HAIL, "Let it go! Let it goooo!");
            dao.insert(h);
            Transparency t = new Transparency(time, ITypeIncident.INCIDENT_TRANSPARENCY, "Under the seeeeeaaaaaa!!");
            dao.insert(t);
        });
    }
}