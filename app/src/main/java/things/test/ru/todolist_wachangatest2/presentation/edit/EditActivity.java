package things.test.ru.todolist_wachangatest2.presentation.edit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.List;

import things.test.ru.todolist_wachangatest2.app.TaskReceiver;
import things.test.ru.todolist_wachangatest2.presentation.main.MainActivity;
import things.test.ru.todolist_wachangatest2.R;
import things.test.ru.todolist_wachangatest2.app.App;
import things.test.ru.todolist_wachangatest2.app.AppDatabase;
import things.test.ru.todolist_wachangatest2.domain.localStorage.DatabaseCallback;
import things.test.ru.todolist_wachangatest2.domain.localStorage.DatabaseManager;
import things.test.ru.todolist_wachangatest2.domain.model.Task;


public class EditActivity extends AppCompatActivity implements DatabaseCallback {

    Task this_task;
    DatabaseManager dbmanager;
    AppDatabase db;
    TextInputEditText text_field;
    AppCompatImageButton delete_button;
    ImageButton notif_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

       text_field=findViewById(R.id.editText2);
       delete_button=findViewById(R.id.delete_button);
       notif_button=findViewById(R.id.notification_button);



        db = App.getInstance().getDatabase();

        dbmanager= App.getInstance().getDbmanager();

        this_task=getIntent().getParcelableExtra("task");
        if(this_task==null){
            getSupportActionBar().setTitle("Новая заметка");
            RelativeLayout relativeLayout=findViewById(R.id.relative_with_buttons);
            relativeLayout.removeView(delete_button);

        }
        else{
            getSupportActionBar().setTitle("Заметка");

            text_field.setText(this_task.text);



            if(this_task.status){
                notif_button.setVisibility(View.GONE);
            }

            if(this_task.notification){

                notif_button.setImageResource(R.drawable.ic_cross);
            }
            else if(!this_task.notification){
                notif_button.setImageResource(R.drawable.ic_alarmclock);

            }


        }


    }



    public void save_exit(View view) {

        String new_task_text=text_field.getText().toString();

        if(this_task==null){
            if(new_task_text.length()!=0){
                dbmanager.addTask(this,new_task_text,false);
                exit();
            }
            else{
                return;
            }
        }
        else{
            if(new_task_text.length()!=0){
                this_task.text=new_task_text;
                dbmanager.updateTask(this,this_task);
                exit();
            }

            else{

                return;
            }


        }


    }

    public void delete(View view) {

        dbmanager.deleteTask(this,this_task);
    }

    @Override
    public void onTasksLoaded(List<Task> tasks) {


        this_task = tasks.get(tasks.size()-1);


    }

    @Override
    public void onTaskDeleted() {

        exit();
    }

    @Override
    public void onTaskAdded() {

}

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void onTaskUpdated() {
    }

    @Override
    public void onLastTaskLoaded(Task task) {
        this_task=task;
        settingNotification(this_task);
    }

    public void exit(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setNotification(View view) {

        String new_task_text=text_field.getText().toString();

        if(this_task==null){
            if(new_task_text.length()!=0){

                dbmanager.addTask(this,new_task_text,true);

                dbmanager.getTheLastTask(this);
            }
            else{
                return;
            }
        }
        else{
            if(new_task_text.length()!=0){
                this_task.text=new_task_text;

                if(this_task.notification){
                    this_task.notification = false;
                }
                else {
                    this_task.notification = true;
                }
                    dbmanager.updateTask(this, this_task); //можно и убрать, но вдруг на поменялась

                    settingNotification(this_task);

            }
            else{
                return;
            }
        }

        //




    }

    public void settingNotification(final Task notificationObject){



        final int NOTIFY_ID = 228+(int)this_task.id;

        String notificationText=notificationObject.text.substring(0,Math.min(notificationObject.text.length(),19));


        Intent getDoneIntent = new Intent(this,TaskReceiver.class);
        getDoneIntent.putExtra("task", notificationObject);
        PendingIntent getDonePendingIntent= PendingIntent.
                getBroadcast(this,228,getDoneIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action getDoneAction =
                new NotificationCompat.Action(R.drawable.ic_stat_name,"завершить",getDonePendingIntent);



        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = this.getResources();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Напоминание")
                .setContentText(notificationText)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_stat_name))
                .setAutoCancel(true)
                .addAction(getDoneAction);


        builder.setDefaults(Notification.DEFAULT_VIBRATE);

        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationObject.notification) {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {


                    notificationManager.notify(NOTIFY_ID, builder.build());
                    this_task.notification=false;
                    dbmanager.updateTask(this_task);
                    notif_button.setImageResource(R.drawable.ic_alarmclock);

                }
            };
            handler.postDelayed(runnable, 10 * 60 * 1000);
            notif_button.setImageResource(R.drawable.ic_cross);


        }
        else if(!notificationObject.notification){

            notificationManager.cancel(NOTIFY_ID);
            notif_button.setImageResource(R.drawable.ic_alarmclock);
        }
    }
}
