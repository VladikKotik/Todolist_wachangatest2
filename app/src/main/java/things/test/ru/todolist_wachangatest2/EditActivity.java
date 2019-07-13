package things.test.ru.todolist_wachangatest2;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);


        task_id=getIntent().getIntExtra("task_ID",-1);
        if(task_id==-1){
            getSupportActionBar().setTitle("Новая заметка");
        }
        else{
            getSupportActionBar().setTitle("Заметка");
        }
        db = App.getInstance().getDatabase();
        dbmanager = new DatabaseManager(this,db);


    }

    public void save_exit(View view) {

        EditText text=findViewById(R.id.editText);
        String new_task_text=text.getText().toString();
        if(task_id==-1){
            dbmanager.addTask(this,new_task_text);
        }
        else{

        }


    }

    @Override
    public void onTasksLoaded(List<Task> tasks) {

    }

    @Override
    public void onTaskDeleted() {

        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onTaskAdded() {

        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void onTaskUpdated() {

        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
