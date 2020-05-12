package com.example.project.database.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project.model.weather.local.incident.BasicIncident;
import com.example.project.model.weather.local.Info;
import com.example.project.model.weather.local.incident.MeasuredIncident;
import com.example.project.model.weather.local.incident.MinIncident;
import com.example.project.model.weather.Report;
import com.example.project.types.ITypeIncident;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = { Report.class, MinIncident.class, BasicIncident.class,
        MeasuredIncident.class}, version = 1, exportSchema = false)
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
            //populate();
            //clean();
        };
    };

    public static void clean() {
        databaseWriteExecutor.execute(() -> {
            // Populate the database in the background.
            WeatherReportDao dao = INSTANCE.weatherReportDao();
            dao.deleteAllReports();
            dao.deleteAllMinIncidents();
            dao.deleteAllBasicIncidents();
            dao.deleteAllMeasuredIncidents();
        });
    }

    public static void populate() {
        databaseWriteExecutor.execute(() -> {
            // Populate the database in the background.
            WeatherReportDao dao = INSTANCE.weatherReportDao();
            dao.deleteAllReports();
            dao.deleteAllMinIncidents();
            dao.deleteAllBasicIncidents();
            dao.deleteAllMeasuredIncidents();

            Report report = new Report("my-device", false,43.7, 6.9966);
            dao.insert(report);

            BasicIncident basic = new BasicIncident(report.getId(), 0,
                    new Info("Rain", "ic_rain"), ITypeIncident.LEVEL_TWO,
                    "Raining cats and dogs!");
            dao.insert(basic);
            basic = new BasicIncident(report.getId(), 1,
                    new Info("Storm", "ic_storm"), ITypeIncident.LEVEL_THREE,
                    "Thunder... thunder... thunderstruck!");
            dao.insert(basic);


            report = new Report("my-device", false,43.68, 6.89);
            dao.insert(report);

            basic = new BasicIncident(report.getId(), 2,
                    new Info("Hail", "ic_hail"), ITypeIncident.LEVEL_ONE,
                    "Let it go! Let it goooo!");
            dao.insert(basic);
            MeasuredIncident measured = new MeasuredIncident(report.getId(), 3,
                    new Info("Transparency", "ic_transparency"), "5","m",
                    "I seA you!");
            dao.insert(measured);
        });
    }
}