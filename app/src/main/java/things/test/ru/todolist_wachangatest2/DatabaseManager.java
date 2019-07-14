package things.test.ru.todolist_wachangatest2;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DatabaseManager {
    private static final String DB_NAME = "database-name";
    private Context context;
    private AppDatabase db;


    public DatabaseManager(Context context, AppDatabase db) {
        this.context = context;
        this.db = db;
    }

    public void getTasks(final DatabaseCallback databaseCallback) {
        db.TaskDao().getAll().subscribeOn(Schedulers.io()).delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Task>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<Task> tasks) throws Exception {
                databaseCallback.onTasksLoaded(tasks);

            }
        });
    }

    public Task getTaskById(int id) { //null
        final Task[] task_needed = {null};
        db.TaskDao().getById((long)id).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Task>() {
                    @Override
            public void accept(@io.reactivex.annotations.NonNull Task task) throws Exception {//сюда не заходит вообще
                        task_needed[0] =task;
                        if(task !=null){
                            System.out.println("accept_not null");
                        }
                        else{
                            System.out.println("accept_null");
                        }
                    }
        });
        if(task_needed[0] !=null){
            System.out.println("not null");
        }
        else{
            System.out.println("null");
        }
        return task_needed[0];
    }

    public void addTask(final DatabaseCallback databaseCallback, final String ltext) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Task task = new Task(ltext);
                db.TaskDao().insert(task);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                databaseCallback.onTaskAdded();
            }

            @Override
            public void onError(Throwable e) {
                databaseCallback.onDataNotAvailable();
            }
        });
    }

    public void deleteTask(final DatabaseCallback databaseCallback, final Task task) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.TaskDao().delete(task);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                databaseCallback.onTaskDeleted();
            }

            @Override
            public void onError(Throwable e) {
                databaseCallback.onDataNotAvailable();
            }
        });
    }


    public void updateTask(final DatabaseCallback databaseCallback, final Task task) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.TaskDao().update(task);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                databaseCallback.onTaskUpdated();
            }

            @Override
            public void onError(Throwable e) {
                databaseCallback.onDataNotAvailable();
            }
        });
    }
}
