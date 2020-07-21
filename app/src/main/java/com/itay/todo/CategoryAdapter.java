package com.itay.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CategoryAdapter extends FirestoreRecyclerAdapter<Category, CategoryAdapter.CategoryHolder> {

    public CategoryAdapter(@NonNull FirestoreRecyclerOptions<Category> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryHolder holder, int position, @NonNull Category model) {
        holder.textViewTitle.setText(model.getName());
        holder.imageViewPriority.setBackgroundResource(model.categoryImageResource());
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_category, parent, false);
        return new CategoryHolder(view);
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageViewPriority;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.category_title);
            imageViewPriority = itemView.findViewById(R.id.category_priority);
        }
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

}
