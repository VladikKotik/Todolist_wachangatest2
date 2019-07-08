package things.test.ru.todolist_wachangatest2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Task> tasks;
    RecyclerView recyclerView;

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
        TaskDao taskDao = db.TaskDao();

       /* Task task1 = new Task();
        task1.id = 1;
        task1.text = "rrrrrrrr";
        task1.status = false;

        Task task2 = new Task();
        task2.id = 2;
        task2.text = "ssssssssss";
        task2.status = false;

        Task task3 = new Task();
        task3.text = "wwwwwwww";
        task3.status = false;

        taskDao.insert(task1);

        taskDao.insert(task2);
        taskDao.insert(task3);*/

        //tasks=taskDao.getAll();

//        db.TaskDao().getAll()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<Task>>() {
//                    @Override
//                    public void accept(List<Task> tasks1) throws Exception {
//                        // ...
//                        tasks = tasks1;
//                    }
//                });


        tasks=taskDao.getAll();// отобразил ток один!!!

        recyclerView = (RecyclerView) findViewById(R.id.tasks_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TasksAdapter adapter = new TasksAdapter(this, tasks);
        recyclerView.setAdapter(adapter);

        System.out.println(adapter.getItemCount());

        db.close();
    }

}
