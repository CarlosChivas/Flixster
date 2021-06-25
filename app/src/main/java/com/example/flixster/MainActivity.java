package com.example.flixster;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    MovieAdapter movieAdapter;

    //Elements for info movie
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    TextView title;
    TextView description;
    ImageView imageView;
    ImageButton close_info;
    RatingBar rbVoteAverage;

    //EditText etEdit;
    ImageButton btnInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        btnInfo = findViewById(R.id.btnInfo);

        MovieAdapter.OnClickListener onClickListener = new MovieAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                infoMovie(position);
            }
        };

        //Create the adapter
        movieAdapter = new MovieAdapter(this, movies, onClickListener);

        //Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        //Set a Layout Manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception");

                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        /*btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //Add item to the model
                if(todoItem.equals("")){
                    //If field is empty, we create a message but we not save nothing
                    Toast.makeText(getApplicationContext(), "You need to write something", Toast.LENGTH_SHORT).show();
                }
                else {
                    items.add(todoItem);
                    //Notify adapter that an item is inserted
                    itemsAdapter.notifyItemInserted(items.size()-1);
                    etItem.setText("");
                    Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                    saveItems();
                }
            }
        });*/
    }

    public void infoMovie(int posicion){

        dialogBuilder = new AlertDialog.Builder(this);
        final View infoMovieView = getLayoutInflater().inflate(R.layout.info_movie, null);

        title = infoMovieView.findViewById(R.id.info_title);
        description = infoMovieView.findViewById(R.id.info_description);
        imageView = infoMovieView.findViewById(R.id.info_image);
        close_info = infoMovieView.findViewById(R.id.info_close);
        rbVoteAverage = (RatingBar) infoMovieView.findViewById(R.id.rbVoteAverage);

        title.setText(movies.get(posicion).getTitle());
        description.setText(movies.get(posicion).getOverview());
        float voteAverage = movies.get(posicion).getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);
        LayerDrawable stars = (LayerDrawable) rbVoteAverage.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        //rbVoteAverage.setBackgroundColor(Color.parseColor("#D6D0CE"));

        Glide.with(this).load(movies.get(posicion).getPosterPath()).placeholder(R.drawable.flicks_movie_placeholder).transform(new CircleCrop()).into(imageView);
        //buttonDone = editTextView.findViewById(R.id.btnEdit);

        //etEdit = editTextView.findViewById(R.id.textEdited);
        //etEdit.setText(items.get(posicion));
        dialogBuilder.setView(infoMovieView);
        dialog = dialogBuilder.create();
        dialog.show();

        close_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //items.set(posicion, etEdit.getText().toString());
                //Notify adapter that an item was changed
                //itemsAdapter.notifyDataSetChanged();
                //saveItems();
                //Toast.makeText(getApplicationContext(), "Item was updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });



    }
}