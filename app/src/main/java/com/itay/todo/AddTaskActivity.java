package com.itay.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, OnSuccessListener<QuerySnapshot> {
    public static final List<Category> DEF_CATEGORIES = Arrays.asList(
            new Category("Work", 3),
            new Category("Personal", 2),
            new Category("House", 5)
    );

    private EditText editTextTitle;
    private EditText editTextExecutionTime;
    private AutoCompleteTextView textViewCategory;

    private DatePickerDialog dialog;
    private Calendar calendar;

    private String userPath;
    private List<Category> categories;
    private String[] categoriesArray;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setTitle("Add Task");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextExecutionTime = findViewById(R.id.edit_text_priority);
        textViewCategory = findViewById(R.id.edit_text_category);
        calendar = Calendar.getInstance();
        dialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        userPath = getIntent().getStringExtra(MainActivity.USER_KEY);
        if (userPath == null) userPath = "user1";
        categories = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Users").document(userPath).collection("Categories")
                .get().addOnSuccessListener(this);
    }

    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
            Category category = documentSnapshot.toObject(Category.class);
            categories.add(category);
            //Toast.makeText(AddTaskActivity.this, "Category " + category.getName(), Toast.LENGTH_SHORT).show();
        }
        if (categories.isEmpty()){
            categories.addAll(DEF_CATEGORIES);
        }
        categoriesArray = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++){
            categoriesArray[i] = categories.get(i).getName();
        }
        adapter = new ArrayAdapter<>(AddTaskActivity.this,
                android.R.layout.simple_list_item_1, categoriesArray);
        textViewCategory.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_task:
                onTaskSaveRequest();
                return true;
            case R.id.edit_categories:
                onCategoryEditRequest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onCategoryEditRequest() {
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra(MainActivity.USER_KEY, userPath);
        startActivity(intent);
    }

    private void onTaskSaveRequest() {
        String title = editTextTitle.getText().toString();
        if (title.trim().isEmpty()){
            Toast.makeText(this, "Please insert valid values", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show(getFragmentManager(), "Dialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //get title ad priority
        String title = editTextTitle.getText().toString();
        int executionTime = Integer.parseInt(editTextExecutionTime.getText().toString());
        int category = Category.categoryValue(categories, textViewCategory.getText().toString());
        long now = calendar.getTime().getTime();
        calendar.set(year, monthOfYear, dayOfMonth);
        long date = calendar.getTime().getTime();
        int daysToComplete = (int) ((date - now) / 1000 / 60 / 60 / 24);
        int priority = Task.taskPriority(daysToComplete, category, executionTime);
        CollectionReference listRef = FirebaseFirestore.getInstance().collection("Users").document(userPath).collection("Tasks");
        listRef.add(new Task(title, priority, daysToComplete, dayOfMonth)).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddTaskActivity.this, "there is a problem", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(this, "task priority: " + priority, Toast.LENGTH_SHORT).show();
        finish();
    }


}
