package com.androiddev.myweatherapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androiddev.myweatherapplication.dummy.DummyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean mTwoPane;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String max_temp_value;
    private static String min_temp_value;
    private static HashMap<String, String> weather_map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.GetWeatherData("5731612", new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    weather_map.put("weather", result.getJSONArray("weather").
                            getJSONObject(0).get("description").toString());
                    Log.d(TAG, result.getJSONArray("weather").getJSONObject(0).
                            get("description").toString());
                    weather_map.put("current_temp", result.getJSONObject("main").get("temp").
                            toString());
                    weather_map.put("city_name",result.get("name").toString());
                    weather_map.put("country",result.getJSONObject("sys").get("country").toString());
                    max_temp_value = result.getJSONObject("main").get("temp_max").toString();
                    weather_map.put("max_temp", max_temp_value);
                    min_temp_value = result.getJSONObject("main").get("temp_min").toString();
                    weather_map.put("min_temp", min_temp_value);
                    Log.d(TAG, "max_temp is:"+ max_temp_value);
                    Log.d(TAG, "min_temp is:"+ min_temp_value);
                    View recyclerView = findViewById(R.id.item_list);
                    assert recyclerView != null;
                    setupRecyclerView((RecyclerView) recyclerView);
                } catch (JSONException e) {
                    Log.e(TAG, "The Error Message is:" + e.getMessage());
                }

            }
        });

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.d(TAG, "weather_map size is:"+weather_map.size() );
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS,
                mTwoPane, weather_map));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final MainActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final Map<String, String> data_map;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.temp_max);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.temp_max);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(MainActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane, HashMap<String, String> w_map ) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            data_map = w_map;
            for(String x: data_map.values()){
                Log.d(TAG, "The Values are:"+x);
            }

        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull  final ViewHolder holder, int position) {
            Log.d(TAG, "max_temp_value is:"+max_temp_value);
            String location = data_map.get("city_name") + "," + data_map.get("country");
            holder.location.setText(location);
            holder.current_temp.setText(data_map.get("current_temp"));
            holder.temp_max.setText(data_map.get("max_temp"));
            holder.temp_min.setText(data_map.get("min_temp"));
            Log.d(TAG, "min_temp_value is:"+min_temp_value);
            switch (data_map.get("weather")){
                case "mist":
                    holder.mWeatherImage.setImageResource(R.drawable.mist);
                    break;
                case "cloud":
                    holder.mWeatherImage.setImageResource(R.drawable.cloudy);
                    break;
                case "sunny":
                    holder.mWeatherImage.setImageResource(R.drawable.sunny);
                    break;
            }


            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView mWeatherImage;
            final TextView temp_max;
            final TextView temp_min;
            final TextView current_temp;
            final TextView location;

            ViewHolder(View view) {
                super(view);
                mWeatherImage = view.findViewById(R.id.weather_image);
                temp_max = view.findViewById(R.id.temp_max);
                temp_min = view.findViewById(R.id.temp_min);
                current_temp = view.findViewById(R.id.current_temp);
                location = view.findViewById(R.id.location);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void GetWeatherData(String city_id, final VolleyCallback callback){

        StringBuilder builder = new StringBuilder(WeatherApiClient.GetCurrWeatherUrl(city_id));
        builder.append("&APPID=");
        builder.append(WeatherApiClient.getOwmApiKey());
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, builder.toString(),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "The Error is"+ error);
            }
        });

        queue.add(request);
    }

}
