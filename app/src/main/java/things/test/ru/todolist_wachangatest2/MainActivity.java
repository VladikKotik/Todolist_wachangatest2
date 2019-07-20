package things.test.ru.todolist_wachangatest2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements DatabaseCallback{

    List<Task> tasks;
    RecyclerView recyclerView;
    RecyclerView doneTasks_recyclerView;
    DatabaseManager dbmanager;
    boolean doneShown;
    Button showDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //boolean isReady = false;

        doneShown=true;

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

        doneTasks_recyclerView = (RecyclerView) findViewById(R.id.done_tasks_recycle_view);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        doneTasks_recyclerView.setLayoutManager(layoutManager2);

        showDoneButton=findViewById(R.id.show_done_button);


        dbmanager = new DatabaseManager(this,db);

        dbmanager.getTasks(this);
       // db.close();
    }


    public void showDone(View view) { //or unshow)))
        if(doneShown){
            doneTasks_recyclerView.setVisibility(View.GONE);
            showDoneButton.setText("Показать выполненные");
            doneShown=false;
        }
        else{
            doneTasks_recyclerView.setVisibility(View.VISIBLE);
            showDoneButton.setText("Скрыть выполненные");
            doneShown=true;

        }
    }

    public void onStatusChanged(Task task){

        dbmanager.updateTask(this,task);
        dbmanager.getTasks(this);
    }

    @Override
    public void onTasksLoaded(List<Task> tasks) {


//        Task task1=new Task("s");
//        List<Task> done_tasks = null;
//        done_tasks.add(task1);

        ArrayList<Task> done_tasks=new ArrayList<Task>();
        ArrayList<Task> undone_tasks=new ArrayList<Task>();

        for(Task oneTask:tasks){
            if(oneTask.status){
                done_tasks.add(oneTask);
            }
            else{
                undone_tasks.add(oneTask);
            }
        }

        UnDoneTasksAdapter adapter = new UnDoneTasksAdapter(this, undone_tasks);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DoneTasksAdapter doneTasksAdapter=new DoneTasksAdapter(this,done_tasks);
        doneTasks_recyclerView.setAdapter(doneTasksAdapter);
        doneTasks_recyclerView.setItemAnimator(new DefaultItemAnimator());



        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);


        if(progressBar!=null) {
           // LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_main);
            //RelativeLayout relativeLayout=findViewById(R.id.linear_main);
            //linearLayout.removeView(progressBar);
            //relativeLayout.removeView(progressBar);

            CoordinatorLayout coordinatorLayout=(CoordinatorLayout)findViewById(R.id.main_coordintator);
            coordinatorLayout.removeView(progressBar);
        }
        if(done_tasks.size()!=0){
            showDoneButton.setVisibility(View.VISIBLE);
        }
        else{
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


}
