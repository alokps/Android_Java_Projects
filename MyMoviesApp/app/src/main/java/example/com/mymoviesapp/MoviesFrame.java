package example.com.mymoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import alokpotrapalli.com.mymoviesapp.R;

public class MoviesFrame extends AppCompatActivity {

    GridView gridView;
    private MoviesAdapter moviesAdapter;
    private MovieDetailData movieDetailData;
    private DrawerLayout mDrawerLayout;


    private static String movie_url = "lists/movies/box_office.json?apikey=";
    private static final String MYMOVIEURL = "com.example.mymovieurl";

    private static final String TAG = MoviesFrame.class.getSimpleName();
    public static final String MOVIE_DETAIL_KEY = "movie";

    //Get the Request Queue Instance
    RequestQueue mRequestQueue;

    //Get the Movies Client Instance
    MoviesAPIClient APIClientInstance;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_frame);

        //Get the API Client Instance
        APIClientInstance = new MoviesAPIClient();
        movieDetailData = new MovieDetailData();

        //Log.d(TAG, "I am onCreate");


        final Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        //Add the Request Queue
        mRequestQueue = Volley.newRequestQueue(this);

        //Setup the Grid View
        gridView = (GridView)findViewById(R.id.gridview1);

        //Populate the ArrayList
        final ArrayList<MoviesDataModel> aMovies= new ArrayList<MoviesDataModel>();

        //Set up Adapter
        moviesAdapter = new MoviesAdapter(this,aMovies);

        gridView.setAdapter(moviesAdapter);

        FetchJsonResponse(movie_url);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), MovieDetailData.class);
                intent.putExtra(MOVIE_DETAIL_KEY, moviesAdapter.getItem(position));
                startActivity(intent);

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        //Log.d(TAG,"I am onStart");

        //Fetch the JSOn Reposnse with the latest URL
        FetchJsonResponse(movie_url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies_frame, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Log.d(TAG,"I am in Drawer");
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()){

                            case R.id.boxoffice:
                                movie_url = "lists/movies/box_office.json?apikey=";
                                //Log.d(TAG, "I am in Drawer1");

                                break;
                            case R.id.intheaters:
                                movie_url = "lists/movies/in_theaters.json?apikey=";
                                //Log.d(TAG, "I am in Drawer2");

                                break;

                            case R.id.upcoming:
                                movie_url = "lists/movies/upcoming.json?apikey=";
                                //Log.d(TAG, "I am in Drawer3");

                                break;

                            case R.id.opening:
                                movie_url = "lists/movies/opening.json?apikey=";
                                //Log.d(TAG, "I am in Drawer4");

                                break;
                        }

                        FetchJsonResponse(movie_url);
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    public void FetchJsonResponse(String movieurl){

        String url = APIClientInstance.getApiBaseUrl(movieurl);
        String ComputedUrl = url + APIClientInstance.getApiKey();

        //progressBar.setVisibility(ProgressBar.VISIBLE);


        //JSONObject Request
        JsonObjectRequest req = new JsonObjectRequest(ComputedUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray items = null;

                try {
                    //Get the movies JSON Array
                    items = response.getJSONArray("movies");

                    //progressBar.setVisibility(ProgressBar.GONE);

                    moviesAdapter.clear();

                    //Parse Json Array into Array of Model Objects
                    ArrayList<MoviesDataModel> movies = MoviesDataModel.FromJson(items);

                    //Load the Model Objects into Adapter
                    for(MoviesDataModel movie: movies){

                        moviesAdapter.add(movie);
                    }

                    moviesAdapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG,error.toString());

            }
        });

        //Add the Request to the request queue for the Async request
        mRequestQueue.add(req);
    }
}
