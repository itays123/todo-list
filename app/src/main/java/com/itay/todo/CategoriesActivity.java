package com.itay.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements OnSuccessListener<QuerySnapshot>, SwipeToDeleteCallback.OnCategorySwipeListener, AddCategoryDialog.OnCategoryAddRequestListener {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String userPath;

    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        userPath = getIntent().getStringExtra(MainActivity.USER_KEY);
        if (userPath == null) userPath = "user1";
        CollectionReference categories = db.collection("Users").document(userPath).collection("Categories");
        categories.get().addOnSuccessListener(this);
        Query query = categories.orderBy("value");
        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class).build();
        adapter = new CategoryAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_categories);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        SwipeToDeleteCallback callback = new SwipeToDeleteCallback(this);
        callback.setOnCategorySwipeListener(this);
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        List<Category> categories = new ArrayList<>();
        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
            Category category = documentSnapshot.toObject(Category.class);
            categories.add(category);
        }
        if (categories.isEmpty()){
            for (Category category : AddTaskActivity.DEF_CATEGORIES){
                db.collection("Users").document(userPath).collection("Categories").add(category);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onCategorySwipe(int position) {
        adapter.deleteItem(position);
    }

    public void onCategoryAddButtonClick(View view) {
        AddCategoryDialog dialog = new AddCategoryDialog();
        dialog.show(getSupportFragmentManager(), "my dialog");
    }

    @Override
    public void onCategoryAddRequest(String name, int value) {
        Toast.makeText(this, "adding category...", Toast.LENGTH_SHORT).show();
        Category category = new Category(name, value);
        db.collection("Users").document(userPath).collection("Categories").add(category);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
