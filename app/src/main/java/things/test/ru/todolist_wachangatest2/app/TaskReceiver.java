package things.test.ru.todolist_wachangatest2.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import things.test.ru.todolist_wachangatest2.domain.localStorage.DatabaseManager;
import things.test.ru.todolist_wachangatest2.domain.model.Task;

public class TaskReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        Task task = intent.getParcelableExtra("task");
        task.status=true;
        DatabaseManager dbmanager=App.getInstance().getDbmanager();
        dbmanager.updateTask(task);


    }
}
