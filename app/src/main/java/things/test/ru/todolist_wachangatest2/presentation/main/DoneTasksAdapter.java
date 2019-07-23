package things.test.ru.todolist_wachangatest2.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import things.test.ru.todolist_wachangatest2.R;
import things.test.ru.todolist_wachangatest2.domain.model.Task;
import things.test.ru.todolist_wachangatest2.presentation.edit.EditActivity;

public class DoneTasksAdapter extends RecyclerView.Adapter<DoneTasksAdapter.ViewHolder> {

    private List<Task> tasks;
    private Context mContext;

    public DoneTasksAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.text.setText(task.text);
        holder.checkBox.setChecked(true);

    }

    @Override
    public int getItemCount() {
        if (tasks == null)
            return 0;
        return tasks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.task_text);
            text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); //someTextView.
            checkBox=(CheckBox) itemView.findViewById(R.id.task_status);
            itemView.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (view.getId() == checkBox.getId()) {
                if (position != RecyclerView.NO_POSITION) {

                    Task oneTask = tasks.get(position);

                        oneTask.status=false;

                    ((MainActivity)mContext).onStatusChanged(oneTask);
                }
            }
            else {
                if (position != RecyclerView.NO_POSITION) {

                    Task oneTask = tasks.get(position);
                    Intent intent = new Intent(mContext, EditActivity.class);
                    intent.putExtra("task", oneTask);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent); //все активити над прописывать в манифесте!!
                }
            }
        }
    }
}