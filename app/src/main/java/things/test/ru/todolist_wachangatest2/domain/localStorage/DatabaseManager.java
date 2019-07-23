package things.test.ru.todolist_wachangatest2.domain.localStorage;

import android.content.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import things.test.ru.todolist_wachangatest2.app.AppDatabase;
import things.test.ru.todolist_wachangatest2.domain.model.Task;

public class DatabaseManager {
    private static final String DB_NAME = "database-name";
    private AppDatabase db;


    public DatabaseManager( AppDatabase db) {
        this.db = db;
    }

    public void getTasks(final DatabaseCallback databaseCallback) {
       // System.out.println("try");
        db.TaskDao().getAll().subscribeOn(Schedulers.io()).delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Task>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<Task> tasks) throws Exception {
             //   System.out.println("stilltry");

                databaseCallback.onTasksLoaded(tasks);

            }
        });
    }

//    public Task getTaskById(int id) { //null
//        final Task[] task_needed = {null};
//        db.TaskDao().getById((long)id).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Task>() {
//                    @Override
//            public void accept(@io.reactivex.annotations.NonNull Task task) throws Exception {//сюда не заходит вообще
//                        task_needed[0] =task;
//                        if(task !=null){
//                            System.out.println("accept_not null");
//                        }
//                        else{
//                            System.out.println("accept_null");
//                        }
//                    }
//        });
//        if(task_needed[0] !=null){
//            System.out.println("not null");
//        }
//        else{
//            System.out.println("null");
//        }
//        return task_needed[0];
//    }

//    public void getTheLastTask(final DatabaseCallback databaseCallback) {
//        System.out.println("!!!trying to get last task!!!");//А ХЕР ТО ТАМ!!!
//        db.TaskDao().getLastTask().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableSingleObserver<Task>() {
//
//                    @Override
//                    public void onSuccess(Task task) {
//                        System.out.println("success");
//                        databaseCallback.onLastTaskLoaded(task);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println("error!!!");
//                       // System.err.println(e);
//                    }
//                });
//
//
//    }

    public void getTheLastTask(final DatabaseCallback databaseCallback){ //это, конечно, неоч правильно, НО
        System.out.println("trying to get");
        db.TaskDao().getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Task>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<Task> tasks) throws Exception {
                System.out.println("still trying to get");

                Task lastTask = tasks.get(tasks.size()-1);
                databaseCallback.onLastTaskLoaded(lastTask);

            }
        });
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
                // System.out.println(task.id);

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

    public void updateTask(final Task task) {

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

            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }
}
