package com.itay.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskCheckListener, SwipeToDeleteCallback.OnTaskSwipeListener {
    public static final String USER_KEY = "status";
    public static final int TASK_DONE = 10001;
    public static final String TOAST_MESSAGE_CHECK = "well done!";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String userPath;

    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            userPath = "user1";
        } else {
            userPath = user.getEmail();
        }
        CollectionReference tasks = db.collection("Users").document(userPath).collection("Tasks");
        Query query = tasks.orderBy("priority");
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class).build();
        adapter = new TaskAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnTaskCheckListener(this);
        SwipeToDeleteCallback deleteCallback = new SwipeToDeleteCallback(this);
        deleteCallback.setOnTaskSwipeListener(this);
        new ItemTouchHelper(deleteCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void onTaskAddButtonClick(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(USER_KEY, userPath);
        startActivity(intent);
    }

    @Override
    public void onTaskCheck(DocumentSnapshot documentSnapshot, int position) {
        Task task = documentSnapshot.toObject(Task.class);
        task.setPriority(TASK_DONE);
        documentSnapshot.getReference().set(task, SetOptions.merge());
        Toast.makeText(this, TOAST_MESSAGE_CHECK, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskSwipe(int position) {
        adapter.deleteItem(position);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
