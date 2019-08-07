package things.test.ru.todolist_wachangatest2.app;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import things.test.ru.todolist_wachangatest2.domain.model.Task;
import things.test.ru.todolist_wachangatest2.domain.services.TaskDao;

@Database(entities = {Task.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao TaskDao();
}
