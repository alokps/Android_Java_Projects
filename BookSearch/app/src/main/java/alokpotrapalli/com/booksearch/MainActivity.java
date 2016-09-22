package alokpotrapalli.com.booksearch;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BOOK_DETAIl_KEY = "book";
    private ListView listView;
    private BookClient client;
    private BookAdapter bookAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        listView = (ListView)findViewById(R.id.lvbooks);
        ArrayList<Book> abooks = new ArrayList<Book>();
        bookAdapter = new BookAdapter(this,abooks);
        listView.setAdapter(bookAdapter);
        setupBookSelectedListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Fetch the data remotely
                FetchBooks(query);

                //Reset the SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();

                //Set Activity title to search query
                MainActivity.this.setTitle(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

    private void FetchBooks(String Author){

        //Show Progress Bar before making request
        progressBar.setVisibility(ProgressBar.VISIBLE);

        client = new BookClient();
        client.getBooks(Author,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                try{

                    //Log the JSON response
                    Log.d(TAG,response.toString());

                    //Show Progress Bar before making request
                    progressBar.setVisibility(ProgressBar.GONE);

                    JSONArray docs = null;
                    if(response != null){

                        //get the docs Json Array
                        docs = response.getJSONArray("docs");

                        //parse the JSON array into an array of model objects
                        final List<Book> books = Book.fromJson(docs);

                        //Set DataStructure removes duplicates
                        Set<Book> myset = new HashSet<Book>();
                        myset.addAll(books);
                        books.clear();
                        books.addAll(myset);

                        //Remove all the books from adapter
                        bookAdapter.clear();

                        //Load all the Books in the book Adapter
                        for(Book book:books){
                            bookAdapter.add(book); //Add the book through Adapter

                        }

                        bookAdapter.notifyDataSetChanged();//This is used to auto load the listview
                        //when there is a change in adapter data
                    }

                }catch (JSONException e){

                    //Invalid JSON data
                    e.printStackTrace();
                }
            }
          });
    }

    public void setupBookSelectedListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Launch the detail view passing book as an Extra
                Intent intent = new Intent(getApplicationContext(), BookDetailActivity.class);
                intent.putExtra(BOOK_DETAIl_KEY, bookAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }



}
