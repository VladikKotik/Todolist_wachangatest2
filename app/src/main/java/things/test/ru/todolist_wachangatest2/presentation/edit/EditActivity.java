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

    Task thisTask;
    DatabaseManager dbmanager;
    AppDatabase db;
    TextInputEditText textField;
    AppCompatImageButton deleteButton;
    ImageButton notifButton;
    Handler handler = new Handler();
    int NOTIFY_ID;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {



            notificationManager.notify(NOTIFY_ID, builder.build());
            thisTask.notification=false;
            dbmanager.updateTask(thisTask);
            notifButton.setImageResource(R.drawable.ic_alarmclock);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

        textField=findViewById(R.id.editText2);
        deleteButton=findViewById(R.id.delete_button);
        notifButton=findViewById(R.id.notification_button);



        db = App.getInstance().getDatabase();

        dbmanager= App.getInstance().getDbmanager();

        thisTask=getIntent().getParcelableExtra("task");
        if(thisTask==null){
            getSupportActionBar().setTitle("Новая заметка");
            RelativeLayout relativeLayout=findViewById(R.id.relative_with_buttons);
            relativeLayout.removeView(deleteButton);

        }
        else{
            getSupportActionBar().setTitle("Заметка");

            textField.setText(thisTask.text);



            if(thisTask.status){
                notifButton.setVisibility(View.GONE);
            }

            if(thisTask.notification){

                notifButton.setImageResource(R.drawable.ic_cross);
            }
            else if(!thisTask.notification){
                notifButton.setImageResource(R.drawable.ic_alarmclock);

            }


        }


    }



    public void save_exit(View view) {

        String new_task_text=textField.getText().toString();

        if(thisTask==null){
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
                thisTask.text=new_task_text;
                dbmanager.updateTask(this,thisTask);
                exit();
            }

            else{

                return;
            }


        }


    }

    public void delete(View view) {

        dbmanager.deleteTask(this,thisTask);
    }

    @Override
    public void onTasksLoaded(List<Task> tasks) {


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
        thisTask=task;
        settingNotification();
    }

    public void exit(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setNotification(View view) {

        String new_task_text=textField.getText().toString();

        if(thisTask==null){
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
                thisTask.text=new_task_text;

                if(thisTask.notification){
                    thisTask.notification = false;
                }
                else {
                    thisTask.notification = true;
                }
                dbmanager.updateTask(this, thisTask);

                settingNotification();

            }
            else{
                return;
            }
        }

    }

    public void settingNotification(){



        NOTIFY_ID = 228+(int)thisTask.id;

        String notificationText=thisTask.text.substring(0,Math.min(thisTask.text.length(),19));


        Intent getDoneIntent = new Intent(this,TaskReceiver.class);
        getDoneIntent.putExtra("task", thisTask);
        PendingIntent getDonePendingIntent= PendingIntent.
                getBroadcast(this,228,getDoneIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action getDoneAction =
                new NotificationCompat.Action(R.drawable.ic_stat_name,"завершить",getDonePendingIntent);



        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = this.getResources();

        builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Напоминание")
                .setContentText(notificationText)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_stat_name))
                .setAutoCancel(true)
                .addAction(getDoneAction);


        builder.setDefaults(Notification.DEFAULT_VIBRATE);

         notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if(thisTask.notification) {
            handler.postDelayed(runnable, 10 * 60 *1000); //10 * 60 * 1000
            notifButton.setImageResource(R.drawable.ic_cross);

        }
        else if(!thisTask.notification){

            handler.removeCallbacks(runnable);

            notificationManager.cancel(NOTIFY_ID);
            notifButton.setImageResource(R.drawable.ic_alarmclock);
        }
    }

}