package com.example.oopee10;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oopee10.adapter.MovieAdapter;
import com.example.oopee10.model.Movie;
import com.example.oopee10.util.JSONUtility;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private TextView errorView;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        initViews();
        
        // Initialize thread pool
        executorService = Executors.newSingleThreadExecutor();
        
        // Load movie data
        loadMovies();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorView = findViewById(R.id.errorView);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void loadMovies() {
        showLoading();
        
        executorService.execute(() -> {
            try {
                JSONUtility jsonUtility = new JSONUtility(this);
                List<Movie> movies = jsonUtility.loadMovies();
                
                runOnUiThread(() -> {
                    if (movies.isEmpty()) {
                        showError("No movie data found");
                    } else {
                        showMovies(movies);
                    }
                });
            } catch (JSONException e) {
                runOnUiThread(() -> showError("Data format error: " + e.getMessage()));
            } catch (IOException e) {
                runOnUiThread(() -> showError("Unable to read movie data: " + e.getMessage()));
            } catch (Exception e) {
                runOnUiThread(() -> showError("Unknown error: " + e.getMessage()));
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    private void showMovies(List<Movie> movies) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        adapter.setMovies(movies);
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}