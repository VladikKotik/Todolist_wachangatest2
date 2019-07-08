package things.test.ru.todolist_wachangatest2;

import java.util.List;

public interface DatabaseCallback {
        void onTasksLoaded(List<Task> tasks);

        void onTaskDeleted();

        void onTaskAdded();

        void onDataNotAvailable();

        void onTaskUpdated();

}
