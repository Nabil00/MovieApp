package com.mal.nabil.movieapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, Communicator{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */


    ListFragment listFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

    }

    //    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {

            case R.id.menu_top_rate:
            {
                listFragment.topRate();
                return true;
            }
            case R.id.menu_most_pop:
            {
                listFragment.mostPop();
                return true;
            }
            case R.id.nxt:
            {
                listFragment.moreResult();
                return true;
            }
            case R.id.fav:
            {
                getSupportLoaderManager().initLoader(0,null,this);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private static final String PROVIDER_NAME = "nabil.contentdemo.movies";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/movies");

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader = new CursorLoader(this,CONTENT_URI,null,null,null,null);
        Log.i("ssd", "onCreateLoader: ");
        return loader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

        listFragment.mAdapter.setmCursor(data);
        listFragment.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        loader.stopLoading();
    }

    @Override
    public void respond(int pos) {
        if(findViewById(R.id.movie_detail_container) != null){
            MovieDetailFragment detailFragment = new MovieDetailFragment();
            Bundle args = new Bundle();
            args.putInt("pos",pos);
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, detailFragment).commit();
        }else {
            Intent i = new Intent(MovieListActivity.this,MovieDetailActivity.class);
            i.putExtra("pos",pos);
            startActivity(i);
        }

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        try{
            listFragment = (ListFragment) fragment;
            listFragment.setC(this);
        }catch (Exception e){}
    }

}

