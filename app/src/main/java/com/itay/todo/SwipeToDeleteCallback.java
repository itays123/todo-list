package com.itay.todo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private Drawable trashIcon;
    private ColorDrawable backgroundColor;
    private OnTaskSwipeListener onTaskSwipeListener;
    private OnCategorySwipeListener onCategorySwipeListener;

    public SwipeToDeleteCallback(Context context) {
        super(0, ItemTouchHelper.RIGHT);
        trashIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete);
        backgroundColor = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (onTaskSwipeListener != null){
            onTaskSwipeListener.onTaskSwipe(viewHolder.getAdapterPosition());
        }
        if (onCategorySwipeListener != null){
            onCategorySwipeListener.onCategorySwipe(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View view = viewHolder.itemView;
        int margin = (view.getHeight() - trashIcon.getIntrinsicHeight()) / 2;
        int top = view.getTop() + (view.getHeight() - trashIcon.getIntrinsicHeight()) / 2;
        int bottom = top + trashIcon.getIntrinsicHeight();
        if (dX > 0){
            swipeRight(view, margin, top, bottom, dX);
        } else backgroundColor.setBounds(0, 0, 0, 0);
        backgroundColor.draw(c);
        trashIcon.draw(c);
    }

    private void swipeRight(View view, int margin ,int top, int bottom, float dX){
        int left = view.getLeft() + margin;
        int right = view.getLeft() + margin + trashIcon.getIntrinsicWidth();
        int iconConstraint = (view.getLeft() + (int) dX < right + margin)
                ? (int) dX - trashIcon.getIntrinsicWidth() - (margin * 2) : 0;
        left += iconConstraint;
        right += iconConstraint;
        trashIcon.setBounds(left, top, right, bottom);
        backgroundColor.setBounds(view.getLeft(), view.getTop(), view.getLeft() + (int) dX,
                view.getBottom());
    }

    public interface OnTaskSwipeListener {
        void onTaskSwipe(int position);
    }

    public void setOnTaskSwipeListener(OnTaskSwipeListener onTaskSwipeListener) {
        this.onTaskSwipeListener = onTaskSwipeListener;
    }

    public interface OnCategorySwipeListener {
        void onCategorySwipe(int position);
    }

    public void setOnCategorySwipeListener(OnCategorySwipeListener onCategorySwipeListener) {
        this.onCategorySwipeListener = onCategorySwipeListener;
    }
}
