package things.test.ru.todolist_wachangatest2;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    Flowable<List<Task>> getAll();
 //   List<Task> getAll();


    @Query("SELECT * FROM task WHERE id = :id")
    Maybe<Task> getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

}

