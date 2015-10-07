package example.com.mymoviesapp;

/**
 * Created by alokps on 9/13/15.
 *
 * The MoviesAPIClient is the Client for HTTP Request.
 *
 */
public class MoviesAPIClient {

    //Tag of Class
    private static final String TAG = MoviesAPIClient.class.getSimpleName();

    //API Key String
    private static final String API_KEY = "9htuhtcb4ymusd73d4z6jxcj";
    private static final String API_BASE_URL = "http://api.rottentomatoes.com/api/public/v1.0/";
    private static final String POSTER_BASE_URL = "http://www.omdbapi.com/?t=";


    //Get the API Base Url
    public String getApiBaseUrl(String relativeUrl){

        return (API_BASE_URL+relativeUrl);
    }


    //Get the API Base Url
    public static String getApiKey() {
        return API_KEY;
    }

    //get the Poster base Url
    public String getPosterBaseUrl(String movietitle){

        return (POSTER_BASE_URL+movietitle);
    }

}



