package things.test.ru.todolist_wachangatest2.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import things.test.ru.todolist_wachangatest2.R;
import things.test.ru.todolist_wachangatest2.app.App;
import things.test.ru.todolist_wachangatest2.app.AppDatabase;
import things.test.ru.todolist_wachangatest2.domain.localStorage.DatabaseCallback;
import things.test.ru.todolist_wachangatest2.domain.localStorage.DatabaseManager;
import things.test.ru.todolist_wachangatest2.domain.model.Task;
import things.test.ru.todolist_wachangatest2.presentation.edit.EditActivity;


public class MainActivity extends AppCompatActivity implements DatabaseCallback {

    RecyclerView recyclerView;
    RecyclerView doneTasksRecyclerView;
    DatabaseManager dbmanager;
    boolean doneShown;
    Button showDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        doneShown = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Заметки, заметочки");
        final Context this_context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        doneTasksRecyclerView = (RecyclerView) findViewById(R.id.done_tasks_recycle_view);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        doneTasksRecyclerView.setLayoutManager(layoutManager2);
        showDoneButton = findViewById(R.id.show_done_button);
        dbmanager = App.getInstance().getDbmanager();
        dbmanager.getTasks(this);
    }


    public void showDone(View view) { //or unshow)))
        if (doneShown) {
            doneTasksRecyclerView.setVisibility(View.GONE);
            showDoneButton.setText("Показать выполненные");
            doneShown = false;
        } else {
            doneTasksRecyclerView.setVisibility(View.VISIBLE);
            showDoneButton.setText("Скрыть выполненные");
            doneShown = true;
            }
    }

    public void onStatusChanged(Task task) {
        dbmanager.updateTask(this, task);
        dbmanager.getTasks(this);
    }

    @Override
    public void onTasksLoaded(List<Task> tasks) {
        ArrayList<Task> done_tasks = new ArrayList<Task>();
        ArrayList<Task> undone_tasks = new ArrayList<Task>();
        for (Task oneTask : tasks) {
            if (oneTask.status) {
                done_tasks.add(oneTask);
            } else {
                undone_tasks.add(oneTask);
            }
        }
        UnDoneTasksAdapter adapter = new UnDoneTasksAdapter(this, undone_tasks);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DoneTasksAdapter doneTasksAdapter = new DoneTasksAdapter(this, done_tasks);
        doneTasksRecyclerView.setAdapter(doneTasksAdapter);
        doneTasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (progressBar != null) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordintator);
            coordinatorLayout.removeView(progressBar);
        }
        if (done_tasks.size() != 0) {
            showDoneButton.setVisibility(View.VISIBLE);
        } else {
            showDoneButton.setVisibility(View.GONE);
        }
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
        dbmanager.getTasks(this);
    }

    @Override
    public void onLastTaskLoaded(Task task) {
    }
}
