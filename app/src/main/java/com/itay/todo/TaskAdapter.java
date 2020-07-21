package com.itay.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class TaskAdapter extends FirestoreRecyclerAdapter<Task, TaskAdapter.TaskHolder> {

    private OnTaskCheckListener onTaskCheckListener;

    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskHolder holder, int position, @NonNull Task model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.imageViewPriority.setBackgroundResource(model.drawableTaskPicture());
        holder.textViewDate.setText(model.taskTimeStamp());

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (model.getPriority() > 10000) {
                holder.textViewTitle.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                holder.textViewDate.setBackgroundResource(R.color.transparent);
                holder.textViewDate.setText("");
                holder.checkBoxTask.setChecked(true);
            } else {
                holder.textViewTitle.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
                holder.textViewDate.setBackgroundResource(R.drawable.item_calendar);
                holder.checkBoxTask.setChecked(false);
            }
        } */

    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,
                parent, false);
        return new TaskHolder(view);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class TaskHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewDate;
        CheckBox checkBoxTask;
        ImageView imageViewPriority;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.task_title);
            textViewDate = itemView.findViewById(R.id.task_date);
            checkBoxTask = itemView.findViewById(R.id.task_checkbox);
            imageViewPriority = itemView.findViewById(R.id.task_priority);
            checkBoxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onTaskCheckListener != null){
                        onTaskCheckListener.onTaskCheck(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnTaskCheckListener {
        void onTaskCheck(DocumentSnapshot documentSnapshot, int position);
    }


    public void setOnTaskCheckListener(OnTaskCheckListener onTaskCheckListener) {
        this.onTaskCheckListener = onTaskCheckListener;
    }
}
