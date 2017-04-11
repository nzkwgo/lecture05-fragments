package edu.uw.fragmentdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener {

    private static final String TAG = "MainActivity";

    private SearchFragment searchFragment;
    private MoviesFragment moviesFragment;
    private DetailFragment detailFragment;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchFragment = SearchFragment.newInstance();

        viewPager = (ViewPager)findViewById(R.id.pager);
        pagerAdapter = new MoviePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }


    //respond to search button clicking
    public void handleSearchClick(View v){
        EditText text = (EditText)findViewById(R.id.txtSearch);
        String searchTerm = text.getText().toString();

        //add a new results fragment to the page
        MoviesFragment fragment = MoviesFragment.newInstance(searchTerm);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, "MoviesFragment");
        ft.addToBackStack(null); //remember for the back button
        ft.commit();
    }

    @Override
    public void onMovieSelected(Movie movie) {
        Log.v(TAG, "Detail for " + movie);
        detailFragment = DetailFragment.newInstance(movie.toString(), movie.imdbId);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(2);
    }


    public void onSearchSubmitted(String searchTerm) {
        moviesFragment = MoviesFragment.newInstance(searchTerm);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(1);
    }


    private class MoviePagerAdapter extends FragmentStatePagerAdapter {
        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int page) {
            if (page == 0) {
                return searchFragment;
            } else if (page == 1) {
                return moviesFragment;
            } else if (page == 2) {
                return detailFragment;
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            if(moviesFragment == null){
                return 1;
            } else if (detailFragment == null) {
                return 2;
            } else {
                return 3;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
