package things.test.ru.todolist_wachangatest2.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import things.test.ru.todolist_wachangatest2.domain.localStorage.DatabaseManager;

public class App extends Application {

    public static App instance;

    private AppDatabase database;

    private DatabaseManager dbmanager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
        dbmanager= new DatabaseManager(database);
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public DatabaseManager getDbmanager() {
        return dbmanager;
    }
}