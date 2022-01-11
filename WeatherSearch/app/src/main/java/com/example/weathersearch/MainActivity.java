package com.example.weathersearch;

import static java.security.AccessController.getContext;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    protected static TabLayout tabLayout;
    private FragmentStateAdapter pagerAdapter;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    FavoriteService favoriteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.splashScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        // Set below attributes to add logo in ActionBar.
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("WeatherApp");

        favoriteService = new FavoriteService(this);


        viewPager2 = findViewById(R.id.mainViewPager);
        viewPager2.setUserInputEnabled(true);

        tabLayout = findViewById(R.id.mainTabLayout);
        pagerAdapter = new MainFragmentPagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout,viewPager2,(tab, position) -> {
            tab.select();
        }).attach();

        if(!favoriteService.favoriteCurr.isEmpty()){
            for (int i=0; i<favoriteService.favoriteCurr.size();i++){
                MainFragmentPagerAdapter.fragments.add(new FavoriteFragment(favoriteService.favoriteCurr.get(i),
                        favoriteService.favoriteDaily.get(i)));
                tabLayout.addTab(tabLayout.newTab());
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(),true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.options_menu,menu);

        MenuItem search = menu.findItem(R.id.app_bar_menu_search);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) search.getActionView();

        androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete = (androidx.appcompat.widget.SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundResource(R.color.white);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, arrayList);


//        adapter.setDropDownViewResource(R.layout.autocomplete_item);
        searchAutoComplete.setAdapter(adapter);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String searchString = (String) adapterView.getItemAtPosition(i);
                searchAutoComplete.setText(""+searchString);
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Call Results Activity here
                Intent intent = new Intent(MainActivity.this, SearchResults.class);
                intent.putExtra("searchQuery",s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                Toast.makeText(MainActivity.this, "I am here", Toast.LENGTH_SHORT).show();

                if(!adapter.isEmpty()){
                    adapter.clear();
                }
                callAutocomplete(s);
                return false;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }
    private void callAutocomplete(String query){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://nrs-weather-search.wl.r.appspot.com/api/autocomplete?city="+query;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                response -> {
                    try{
                        //Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                        for(int i=0; i<response.length(); i++){
                            JSONArray terms = response.getJSONArray(i);
//                            String term = terms.getString(0) + ", " + terms.getString(1);
//                            Log.d("term",term);
                            adapter.add(terms.getString(0) + ", " + terms.getString(1));
                            adapter.notifyDataSetChanged();
//                            Log.d("adapter",String.valueOf(adapter.getCount()));
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        requestQueue.add(jsonArrayRequest);
    }
}