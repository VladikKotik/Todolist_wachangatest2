package things.test.ru.todolist_wachangatest2;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String text;

    public boolean status;
}
