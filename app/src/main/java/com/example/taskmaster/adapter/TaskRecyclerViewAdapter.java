package com.example.taskmaster.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.R;
import com.example.taskmaster.activities.AllTasks;
import com.example.taskmaster.activities.TaskDetails;
import com.example.taskmaster.model.Tasks;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> {
    List<Tasks> taskList;
    Context callingActivity;

    public TaskRecyclerViewAdapter(List<Tasks> taskList, Context callingActivity) {
        this.taskList = taskList;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent, false);
        return new TaskViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TextView taskFragmentTVname = holder.itemView.findViewById(R.id.TasksFragmentTVtitle);
        TextView taskFragmentTVbody = holder.itemView.findViewById(R.id.TaskFragmentTVbody);
        TextView taskFragmentTVcontent = holder.itemView.findViewById(R.id.TaskFragmentTVEnumState);
        String body = taskList.get(position).getBody();
        String title = taskList.get(position).getTitle();
        String state = String.valueOf(taskList.get(position).getState());
        taskFragmentTVname.setText(position + ". " + title);
        taskFragmentTVbody.setText(body);
        taskFragmentTVcontent.setText(state);
        View taskItemView = holder.itemView;
        taskItemView.setOnClickListener(v -> {
            Intent goToDetailsIntent = new Intent(callingActivity, TaskDetails.class);
            goToDetailsIntent.putExtra(AllTasks.TASK_TITLE_TAG, title);
            goToDetailsIntent.putExtra(AllTasks.TASK_BODY_TAG, body);
            goToDetailsIntent.putExtra(AllTasks.TASK_STATE_TAG, state);
            callingActivity.startActivity(goToDetailsIntent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
