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
import android.widget.RelativeLayout;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import things.test.ru.todolist_wachangatest2.presentation.main.MainActivity;
import things.test.ru.todolist_wachangatest2.R;
import things.test.ru.todolist_wachangatest2.app.App;
import things.test.ru.todolist_wachangatest2.app.AppDatabase;
import things.test.ru.todolist_wachangatest2.domain.localStorage.DatabaseCallback;
import things.test.ru.todolist_wachangatest2.domain.localStorage.DatabaseManager;
import things.test.ru.todolist_wachangatest2.domain.model.Task;

public class EditActivity extends AppCompatActivity implements DatabaseCallback {

    //int task_id;
    Task this_task;
    DatabaseManager dbmanager;
    AppDatabase db;
    TextInputEditText text_field;
    AppCompatImageButton delete_button;
    //private static final int NOTIFY_ID = 228;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

       text_field=findViewById(R.id.editText2);
       delete_button=findViewById(R.id.delete_button);



        db = App.getInstance().getDatabase();
        //dbmanager = new DatabaseManager(this,db);

        dbmanager= App.getInstance().getDbmanager();

        this_task=getIntent().getParcelableExtra("task");
        //System.out.println("status:  "+this_task.status);

        //System.out.println("!!!!!!!!!!!! activity!  "+task_id);
        if(this_task==null){
            getSupportActionBar().setTitle("Новая заметка");
            RelativeLayout relativeLayout=findViewById(R.id.relative_with_buttons);
            relativeLayout.removeView(delete_button);

        }
        else{
            getSupportActionBar().setTitle("Заметка");

            text_field.setText(this_task.text);

        }


    }


    //button.setEnabled(false);

    public void save_exit(View view) {          //if length=0, ne save

        String new_task_text=text_field.getText().toString();

        if(this_task==null){
            if(new_task_text.length()!=0){
                dbmanager.addTask(this,new_task_text);
                exit();
            }
            else{
                //exit();
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
                //exit();
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

        System.out.println("!!!!!!onLastTaskLoaded!!!!!!!!");
        System.out.println(this_task.id+" "+this_task.text);

    }

    @Override
    public void onTaskDeleted() {

        exit();
    }

    @Override
    public void onTaskAdded() {


        //exit();
        //this_task=
    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void onTaskUpdated() {

        //exit();
    }

    @Override
    public void onLastTaskLoaded(Task task) {
        this_task=task;
        System.out.println("!!!!!!onLastTaskLoaded!!!!!!!!");
        System.out.println(task.id+" "+task.text);
        settingNotification(this_task);
    }

    public void exit(){
      //  db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setNotification(View view) {

        //таск и таск ид иметь бы здесь!!

        String new_task_text=text_field.getText().toString();
        String notificationText = new String();

        boolean isSet;

        //System.out.println(new_task_text);

        if(this_task==null){
            if(new_task_text.length()!=0){
               // notificationText=new_task_text.substring(0,Math.min(new_task_text.length(),19));

                dbmanager.addTask(this,new_task_text);

                dbmanager.getTheLastTask(this);


                // dbmanager.getTasks(this);
                //System.out.println(notificationText);
            }
            else{
                return;
            }
        }
        else{
            if(new_task_text.length()!=0){
             //   notificationText=new_task_text.substring(0,Math.min(new_task_text.length(),19));
                this_task.text=new_task_text;
                dbmanager.updateTask(this,this_task); //можно и убрать, но вдруг на поменялась
                //dbmanager.getTheLastTask(this);

                settingNotification(this_task);

               // MainActivity.getLastTaskFromMA(dbmanager, this);

            }
            else{
                return;
            }
        }

        //




    }

    public void settingNotification(Task notificationObject){
        final int NOTIFY_ID = 228+(int)this_task.id;

        String notificationText=notificationObject.text.substring(0,Math.min(notificationObject.text.length(),19));


        System.out.println(NOTIFY_ID);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = this.getResources();

        // до версии Android 8.0 API 26
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_stat_name)
                //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle("Напоминание")
                //.setContentText(res.getString(R.string.notifytext))
                .setContentText(notificationText) // Текст уведомления
                // необязательные настройки
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_stat_name)) // большая
                // картинка
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true); // автоматически закрыть уведомление после нажатия

        builder.setDefaults(Notification.DEFAULT_VIBRATE);

        //System.out.println(System.currentTimeMillis());


        // Альтернативный вариант
        // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        //notificationManager.cancel(NOTIFY_ID);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFY_ID, builder.build());
            }
        };
        handler.postDelayed(runnable, 10 * 1000);

    }
}
