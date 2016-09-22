package alokpotrapalli.com.booksearch;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by alokps on 9/4/15.
 *
 * Lets generate the Java Class, that will act as a Open Library
 * API client for sending out network request to specific endpoints.
 *
 */
public class BookClient {

    private static final String TAG = BookClient.class.getSimpleName();
    private static final String API_BASE_URL = "https://openlibrary.org/";
    private AsyncHttpClient client;

    public BookClient(){
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl){
        return API_BASE_URL + relativeUrl;
    }

    //Method for accessing the search API
    public void getBooks(final String query, JsonHttpResponseHandler handler){

        try {

            String url = getApiUrl("search.json?q=");
            client.get(url + URLEncoder.encode(query,"utf-8"),handler);

        }catch (UnsupportedEncodingException e){

            e.printStackTrace();
        }

    }

    //Get the Publisher and number of pages in the book
    public void getExtraBookDetails(String openLibraryId, JsonHttpResponseHandler handler){
        String url = getApiUrl("books/");
        client.get(url + openLibraryId + ".json", handler);
    }


}
