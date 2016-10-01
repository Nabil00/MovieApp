package com.mal.nabil.movieapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {


    Communicator communicator ;
    private Cursor mCursor = null;
    private final Context context;
    private List<Movie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }

    public MoviesAdapter(Context context, List<Movie> moviesList, Cursor cursor) {
        this.moviesList = moviesList;
        this.context = context;
        this.mCursor = cursor;
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public void setmCursor(Cursor mCursor) {
        this.mCursor = mCursor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(mCursor == null) {
            Movie movie = moviesList.get(position);
            new DataMaker(context).getImage(holder.img , movie.getMovie_poster());
        }else
        {
            if(mCursor.moveToPosition(position)){
                new DataMaker(context).getImage(holder.img ,mCursor.getString(mCursor.getColumnIndex("POSTER")));
            }
        }


        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.respond(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return moviesList.size();
    }

}
