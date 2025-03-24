package com.example.oopee10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oopee10.R;
import com.example.oopee10.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private final Context context;

    public MovieAdapter(Context context) {
        this.context = context;
        this.movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView yearView;
        private final TextView genreView;
        private final ImageView posterView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.movieTitle);
            yearView = itemView.findViewById(R.id.movieYear);
            genreView = itemView.findViewById(R.id.movieGenre);
            posterView = itemView.findViewById(R.id.moviePoster);
        }

        public void bind(Movie movie) {
            titleView.setText(movie.getTitle());
            yearView.setText(movie.getFormattedYear());
            genreView.setText(movie.getGenre());
            
            // Set movie poster
            try {
                int resourceId = itemView.getContext().getResources().getIdentifier(
                    movie.getPoster(),
                    "drawable",
                    itemView.getContext().getPackageName()
                );
                if (resourceId != 0) {
                    posterView.setImageResource(resourceId);
                } else {
                    // Set default image
                    posterView.setImageResource(R.drawable.default_movie_poster);
                }
            } catch (Exception e) {
                // If loading fails, use default image
                posterView.setImageResource(R.drawable.default_movie_poster);
            }
        }
    }
}