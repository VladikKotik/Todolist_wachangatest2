package things.test.ru.todolist_wachangatest2.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Task implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String text;
    public boolean status;
    public boolean notification;

    public Task(String text) {
        this.text = text;
        status = false;
        notification = false;
    }

    protected Task(Parcel in) {
        id = in.readLong();
        text = in.readString();
        status = in.readByte() != 0;
        notification = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(text);
        parcel.writeByte((byte) (status ? 1 : 0));
        parcel.writeByte((byte) (notification ? 1 : 0));
    }
}
