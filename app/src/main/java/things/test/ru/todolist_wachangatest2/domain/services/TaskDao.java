package things.test.ru.todolist_wachangatest2.domain.services;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import things.test.ru.todolist_wachangatest2.domain.model.Task;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    Flowable<List<Task>> getAll();


    @Query("SELECT * FROM task WHERE id = :id")
    Maybe<Task> getById(long id);

    @Query("SELECT * FROM task WHERE id = (SELECT MAX(id) FROM task) LIMIT 1")
    Single<Task> getLastTask();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

}

