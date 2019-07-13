package things.test.ru.todolist_wachangatest2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private List<Task> tasks;
    private Context mContext;

    public TasksAdapter(Context context, List<Task> tasks) {
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

    }

    @Override
    public int getItemCount() {
        if (tasks == null)
            return 0;
        return tasks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.task_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                Task oneTask = tasks.get(position);
                System.out.println("!!!! adapter!!! "+oneTask.id);
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("task_ID", oneTask.id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent); //все активити над прописывать в манифесте!!
            }
        }
    }
}