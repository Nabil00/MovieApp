package com.mal.nabil.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

public class Poster extends AppCompatActivity {

    ImageView imageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        Intent i = getIntent();
        imageView = (ImageView) findViewById(R.id.movie_poster);
        Ion.with(this)
                .load(i.getStringExtra("url"))
                .withBitmap()
                .fadeIn(true)
                .intoImageView(imageView);
    }
}
