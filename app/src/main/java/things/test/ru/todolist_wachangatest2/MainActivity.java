package things.test.ru.todolist_wachangatest2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DatabaseCallback{

    List<Task> tasks;
    RecyclerView recyclerView;
    DatabaseManager dbmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //boolean isReady = false;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Заметки, заметочки");


        final Context this_context=this;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "тут переход", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(this_context, EditActivity.class);
                intent.putExtra("task_ID", -1);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this_context.startActivity(intent);
            }
        });

        AppDatabase db = App.getInstance().getDatabase();

        recyclerView = (RecyclerView) findViewById(R.id.tasks_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        dbmanager = new DatabaseManager(this,db);

        dbmanager.getTasks(this);
        db.close();
    }

    @Override
    public void onTasksLoaded(List<Task> tasks) {


        TasksAdapter adapter = new TasksAdapter(this, tasks);
        recyclerView.setAdapter(adapter);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.linear_main);
        linearLayout.removeView(progressBar);



    }

    @Override
    public void onTaskDeleted() {

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
}
