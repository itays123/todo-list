package com.itay.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddCategoryDialog extends AppCompatDialogFragment {

    private OnCategoryAddRequestListener onCategoryAddRequestListener;

    private EditText editTextTitle;
    private EditText editTextValue;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(view)
                .setTitle("Add category")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = editTextTitle.getText().toString();
                int value = Integer.parseInt(editTextValue.getText().toString());
                onCategoryAddRequestListener.onCategoryAddRequest(name, value);
            }
        });
        editTextTitle = view.findViewById(R.id.category_title_et);
        editTextValue = view.findViewById(R.id.category_value_et);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onCategoryAddRequestListener = (OnCategoryAddRequestListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "You forgot to implement your interface");
        }

    }

    public interface OnCategoryAddRequestListener {
        void onCategoryAddRequest(String name, int value);
    }
}
