package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    OnClickListener clickListener;

    Context context;
    List<Movie> movies;
    List<String> colors = new ArrayList<String>();

    public MovieAdapter(Context context, List<Movie> movies, OnClickListener clickListener) {
        this.context = context;
        this.movies = movies;
        this.clickListener = clickListener;
        this.colors.add("#06B2BB");
        this.colors.add("#FDD219");
        this.colors.add("#F78828");
        this.colors.add("#FA5456");
    }

    //Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        //Get the movie at the position
        Movie movie = movies.get(position);

        //Bind the movie data into the VH
        holder.bind(movie, position);
    }

    //Return the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        ImageButton btnInfo;
        View view;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            btnInfo = itemView.findViewById(R.id.btnInfo);
            view = itemView;
        }

        public void bind(Movie movie,Integer position) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            view.setBackgroundColor(Color.parseColor(colors.get(position - ((position/4)*4))));

            String imageURL;
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                //imageURL = movie.getBackdropPath();
                Glide.with(context).load(movie.getBackdropPath()).placeholder(R.drawable.flicks_movie_placeholder).into(ivPoster);
            }
            else{
                //imageURL = movie.getPosterPath();
                Glide.with(context).load(movie.getPosterPath()).placeholder(R.drawable.flicks_movie_placeholder).transform(new CircleCrop())/*.centerCrop().transform(new RoundedCornersTransformation(100,0))*/.into(ivPoster);
            }
            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Notify the listener which position was pressed.
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

        }
    }
}
