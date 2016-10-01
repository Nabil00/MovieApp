package com.mal.nabil.movieapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ListFragment extends Fragment  {

    public static MoviesAdapter mAdapter;
    private DataMaker maker ;
    Communicator c ;
    RecyclerView recyclerView ;

    public void setC(Communicator c) {
        this.c = c;
    }

    int number_of_colon = 2;

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        if(getSizeName(getContext()).equals("large") && getActivity().getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT ) {
            number_of_colon = 1;
        }

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), number_of_colon);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MoviesAdapter(getContext() , maker.movie_List , null);
        mAdapter.setCommunicator(c);
        recyclerView.setAdapter(mAdapter);

    }

    public void topRate() {
        mAdapter.setmCursor(null);
        maker.movie_List.clear();
        maker.prepareUrl("Top Rated");
        maker.prepareMoviesData();
        mAdapter.notifyDataSetChanged();
    }

    public void mostPop() {
        mAdapter.setmCursor(null);
        maker.movie_List.clear();
        maker.prepareUrl("Most Popular");
        maker.prepareMoviesData();
        mAdapter.notifyDataSetChanged();
    }

    public void moreResult() {
        maker.page_number++;
        maker.URL = maker.URL + "&page=" + maker.page_number ;
        maker.prepareMoviesData();
        mAdapter.notifyDataSetChanged();
    }

    String TAG = "TAG" ;
    String interest = "Most Popular";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment,container,false);
        recyclerView= (RecyclerView) v.findViewById(R.id.movie_list);
        assert recyclerView != null;
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView( recyclerView);
        maker = new DataMaker(getContext() , mAdapter);
        maker.prepareUrl(interest);
        if(savedInstanceState == null && DataMaker.movie_List.size() <= 20)
            maker.prepareMoviesData();
        Log.d(TAG, "onViewCreated: "+DataMaker.movie_List.size());
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(getSizeName(getContext()).equals("large")) {
                number_of_colon = 3;
                GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), number_of_colon);
                recyclerView.setLayoutManager(mLayoutManager);
            }else if(getSizeName(getContext()).equals("normal") || getSizeName(getContext()).equals("xlarge") ) {
                number_of_colon = 3;
                GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), number_of_colon);
                recyclerView.setLayoutManager(mLayoutManager);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            if(getSizeName(getContext()).equals("normal")){
                number_of_colon=2;
                GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), number_of_colon);
                recyclerView.setLayoutManager(mLayoutManager);
            }else if(getSizeName(getContext()).equals("large")){
                number_of_colon=1;
                GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), number_of_colon);
                recyclerView.setLayoutManager(mLayoutManager);
            }else if(getSizeName(getContext()).equals("xlarge") ) {
                number_of_colon = 3;
                GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), number_of_colon);
                recyclerView.setLayoutManager(mLayoutManager);
            }

        }
    }



    private static String getSizeName(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                    return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return "large";
            case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
                return "xlarge";
            default:
                return "undefined";
        }
    }

}