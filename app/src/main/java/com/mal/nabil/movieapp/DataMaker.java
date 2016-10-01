package com.mal.nabil.movieapp;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class DataMaker {

    private final static String BASE_URL ="http://api.themoviedb.org/3/discover/movie?api_key=####"  ;
    private final static String BASE_IMG_URL = "http://image.tmdb.org/t/p/w500/";

    public static List<Movie> movie_List = new ArrayList<>();
    private MoviesAdapter mAdapter;

    protected String URL ;
    protected int page_number =1;
    private Context context;

    public DataMaker(Context context) {
        this.context = context;
    }

    public DataMaker(Context context, MoviesAdapter mAdapter) {
        this.context = context;
        this.mAdapter = mAdapter;
    }

    public void prepareMoviesData() {
        Ion.with(context)
                .load(URL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String msg = "";
                        try {
                            JsonArray jsonArray = result.getAsJsonArray("results");
                            if (e != null) {
                                msg = "Error occur";
                            } else if (jsonArray.size() == 0) {
                                msg = "Empty JSON";
                            } else {

                                for (int i = 0; i < jsonArray.size(); i++) {
                                    Movie movie = new Movie();
                                    JsonObject m = jsonArray.get(i).getAsJsonObject();
                                    movie.setMovie_id(m.get("id")+"");
                                    movie.setTitle(m.get("original_title").toString());
                                    movie.setRelease_date(m.get("release_date").toString());
                                    movie.setVote_average(m.get("vote_average").toString());
                                    movie.setMovie_poster(m.get("poster_path").toString());
                                    movie.setPlot_synopsis(m.get("overview").toString());

                                    movie_List.add(movie);
                                }
                                msg = "done";
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e1) {
                            msg = "INTERNET ERROR";
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void prepareUrl(String s) {
        if(s.equals("Top Rated"))
            URL = BASE_URL + "&sort_by=vote_average.desc";
        else if(s.equals("Most Popular"))
            URL = BASE_URL + "&sort_by=popularity.desc";
    }

    public void getImage (ImageView img , String url){
        Ion.with(context)
                .load(BASE_IMG_URL+url)
                .withBitmap()
                .fadeIn(true)
                .error(R.drawable.posternotavailable)
                .intoImageView(img);
    }

}
