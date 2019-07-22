package things.test.ru.todolist_wachangatest2.domain.localStorage;

import java.util.List;

import things.test.ru.todolist_wachangatest2.domain.model.Task;

public interface DatabaseCallback {
        void onTasksLoaded(List<Task> tasks);

        void onTaskDeleted();

        void onTaskAdded();

        void onDataNotAvailable();

        void onTaskUpdated();

}
