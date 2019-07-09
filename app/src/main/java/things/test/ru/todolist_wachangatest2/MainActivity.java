package things.test.ru.todolist_wachangatest2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DatabaseCallback{

    List<Task> tasks;
    RecyclerView recyclerView;
    DatabaseManager dbmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        boolean isReady = false;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "тут переход", Snackbar.LENGTH_LONG).show();
            }
        });

        AppDatabase db = App.getInstance().getDatabase();
        //TaskDao taskDao = db.TaskDao();

        //ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
       // progressBar.setVisibility(ProgressBar.VISIBLE);
        //progressBar.

        recyclerView = (RecyclerView) findViewById(R.id.tasks_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dbmanager = new DatabaseManager(this,db);

        //вот тут цикл и тип отображать или нет прогрес/статусбар

        //делэй там в нутри на 1сек
        dbmanager.getTasks(this);

        //progressBar.setVisibility(ProgressBar.INVISIBLE);


        db.close();
    }

    @Override
    public void onTasksLoaded(List<Task> tasks) {


        TasksAdapter adapter = new TasksAdapter(this, tasks);
        recyclerView.setAdapter(adapter);

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
