package things.test.ru.todolist_wachangatest2.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import things.test.ru.todolist_wachangatest2.domain.localStorage.DatabaseManager;
import things.test.ru.todolist_wachangatest2.domain.model.Task;

public class TaskReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Task task = intent.getParcelableExtra("task");
        NotificationManagerCompat.from(context).cancel((int)task.id+228);

        task.status=true;
        task.notification=false;
        DatabaseManager dbmanager=App.getInstance().getDbmanager();
        dbmanager.updateTask(task);


    }
}
