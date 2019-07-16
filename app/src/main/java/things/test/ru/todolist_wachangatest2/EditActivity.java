package things.test.ru.todolist_wachangatest2;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

public class EditActivity extends AppCompatActivity implements DatabaseCallback{

    //int task_id;
    Task this_task;
    DatabaseManager dbmanager;
    AppDatabase db;
    TextInputEditText text_field;
    AppCompatImageButton delete_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

       text_field=findViewById(R.id.editText2);
       delete_button=findViewById(R.id.delete_button);



        db = App.getInstance().getDatabase();
        dbmanager = new DatabaseManager(this,db);

        this_task=getIntent().getParcelableExtra("task");
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

    public void save_exit(View view) {          //if length=0, ne save

        String new_task_text=text_field.getText().toString();

        if(this_task==null){
            if(new_task_text.length()!=0){
                dbmanager.addTask(this,new_task_text);
            }
            else{
                exit();
            }
        }
        else{
            if(new_task_text.length()!=0){
                this_task.text=new_task_text;
                dbmanager.updateTask(this,this_task);
            }

            else{
                exit();
            }

        }


    }

    public void delete(View view) {

        dbmanager.deleteTask(this,this_task);
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

        exit();
    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void onTaskUpdated() {

        exit();
    }

    public void exit(){
      //  db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
