package com.mal.nabil.movieapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class MovieDetailFragment extends Fragment {



    private static final String PROVIDER_NAME = "nabil.contentdemo.movies";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/movies");
    private static final String TAG = "ooo";

    TextView title, over, date, rate, reviews_text, trailer_text;
    String id;
    ImageView img;
    String imgUrl, trailerUrl = "https://www.youtube.com/watch?v=";

    List<Movie> movie;
    int pos =0;
    private Button favourite_button;

    public MovieDetailFragment() {
    }

    SharedPreferences sharedPreferences ;

    private void checkFavourite() {
        if (sharedPreferences.contains(movie.get(pos).getMovie_id())) {
            movie.get(pos).setFavorite(true);
            Log.d(TAG, "checkFavourite: true");
            favourite_button.setBackgroundResource(R.drawable.heartremove);
        }else
            movie.get(pos).setFavorite(false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = DataMaker.movie_List;
        pos=getArguments().getInt("pos",0);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        title = (TextView) rootView.findViewById(R.id.titel_view);
        over = (TextView) rootView.findViewById(R.id.over_view);
        rate = (TextView) rootView.findViewById(R.id.rate_view);
        date = (TextView) rootView.findViewById(R.id.date_view);

        img = (ImageView) rootView.findViewById(R.id.imageView);

        reviews_text = (TextView) rootView.findViewById(R.id.review_text);
        trailer_text = (TextView) rootView.findViewById(R.id.trailer_text);

        rootView.findViewById(R.id.yout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl)));
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),Poster.class);
                i.putExtra("url",imgUrl);
                startActivity(i);
            }
        });


        sharedPreferences =  this.getActivity().getSharedPreferences("MyFavorites", Context.MODE_PRIVATE);
        favourite_button = (Button)rootView.findViewById(R.id.addRemoveFav);

        checkFavourite();
        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(movie.get(pos).isFavorite()){

                    favourite_button.setBackgroundResource(R.drawable.heart_favorite_like_add);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(movie.get(pos).getMovie_id());
                    movie.get(pos).setFavorite(false);

                    getContext().getContentResolver().delete(CONTENT_URI,"TITLE", new String[]{movie.get(pos).getTitle()});

                    Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();

                }else {

                    movie.get(pos).setFavorite(true);
                    favourite_button.setBackgroundResource(R.drawable.heartremove);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(movie.get(pos).getMovie_id(),"true");
                    editor.commit();


                    ContentValues values = new ContentValues();
                    values.put("TITLE", movie.get(pos).getTitle());
                    values.put("RELEASE_DATE", movie.get(pos).getRelease_date());
                    values.put("VOTE_AVERAGE", movie.get(pos).getVote_average());
                    values.put("PLOT", movie.get(pos).getPlot_synopsis());
                    values.put("POSTER", movie.get(pos).getMovie_poster());

                    getContext().getContentResolver().insert(CONTENT_URI, values);

                    Toast.makeText(getContext(), "added", Toast.LENGTH_SHORT).show();
                }


            }
        });



        title.setText(movie.get(pos).getTitle());
        over.setText(movie.get(pos).getPlot_synopsis());
        date.setText(movie.get(pos).getRelease_date());
        rate.setText(movie.get(pos).getVote_average()+"/10");


        imgUrl = "http://image.tmdb.org/t/p/w500/" + movie.get(pos).getMovie_poster();
        Ion.with(getContext())
                .load(imgUrl)
                .withBitmap()
                .error(R.drawable.oimageavail)
                .fadeIn(true)
                .intoImageView(img);

        prepareData();
        return rootView;
    }

    private void prepareData() {
        id = movie.get(pos).getMovie_id();
        Ion.with(this)
                .load("https://api.themoviedb.org/3/movie/" + id + "?api_key=c233794e8e625b9cb5f94d2a40c3941d&append_to_response=trailers,reviews")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String msg = "";
                        try {
                            JsonArray trailer_Array = result.get("trailers").getAsJsonObject().get("youtube").getAsJsonArray();
                            JsonArray reviews_Array = result.get("reviews").getAsJsonObject().get("results").getAsJsonArray();
                            if (e != null) {
                                msg = "Error occur";
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                            if (reviews_Array.size() != 0) {
                                String rr = reviews_Array.get(0).getAsJsonObject().get("content").toString();
                                reviews_text.setText(rr.substring(1, rr.length() - 1));
                            }else
                                Toast.makeText(getContext(), "No Reviews available", Toast.LENGTH_SHORT).show();
                            if (trailer_Array.size() != 0){
                                String tt = trailer_Array.get(0).getAsJsonObject().get("name").toString();
                                trailer_text.setText(tt.substring(1, tt.length() - 1));
                                trailerUrl += trailer_Array.get(0).getAsJsonObject().get("source").toString().substring(1);
                                trailerUrl = trailerUrl.substring(0, trailerUrl.length() - 1);
                            }else
                                Toast.makeText(getContext(), "No Trailers available", Toast.LENGTH_SHORT).show();
                        } catch (Exception e1) {
                            msg = "INTERNET ERROR";
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

