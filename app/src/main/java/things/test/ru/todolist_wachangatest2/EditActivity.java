package things.test.ru.todolist_wachangatest2;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class EditActivity extends AppCompatActivity implements DatabaseCallback{

    int task_id;
    DatabaseManager dbmanager;
    AppDatabase db;
    TextInputEditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

       text=findViewById(R.id.editText2);

        db = App.getInstance().getDatabase();
        dbmanager = new DatabaseManager(this,db);

        task_id=getIntent().getIntExtra("task_ID",-1); //id типа лонг, переделать!!!!
        System.out.println("!!!!!!!!!!!! activity!  "+task_id);
        if(task_id==-1){
            getSupportActionBar().setTitle("Новая заметка");
        }
        else{
            getSupportActionBar().setTitle("Заметка");
            text.setText(dbmanager.getTaskById(task_id).text);
        }

    }

    public void save_exit(View view) {          //if length=0, ne save

        String new_task_text=text.getText().toString();

        if(task_id==-1){
            if(new_task_text.length()!=0){
                dbmanager.addTask(this,new_task_text);
            }
            else{
                exit();
            }
        }
        else{
            if(new_task_text.length()!=0){
                dbmanager.updateTask(this,task_id,new_task_text);
            }

            else{
                exit();
            }

        }


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
        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
