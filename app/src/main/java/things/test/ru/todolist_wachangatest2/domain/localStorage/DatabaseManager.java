package things.test.ru.todolist_wachangatest2.domain.localStorage;


import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import things.test.ru.todolist_wachangatest2.app.AppDatabase;
import things.test.ru.todolist_wachangatest2.domain.model.Task;

public class DatabaseManager {

    private AppDatabase db;

    public DatabaseManager(AppDatabase db) {
        this.db = db;
    }

    public void getTasks(final DatabaseCallback databaseCallback) {
        db.TaskDao().getAll().subscribeOn(Schedulers.io()).delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Task>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<Task> tasks) throws Exception {

                databaseCallback.onTasksLoaded(tasks);

            }
        });
    }

    public void getTheLastTask(final DatabaseCallback databaseCallback) { //это, конечно, неоч правильно, НО
        db.TaskDao().getLastTask().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableSingleObserver<Task>() {
            @Override
            public void onSuccess(Task task) {
                databaseCallback.onLastTaskLoaded(task);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void addTask(final DatabaseCallback databaseCallback, final String ltext, final boolean notification) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Task task = new Task(ltext);
                task.notification = notification;
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
