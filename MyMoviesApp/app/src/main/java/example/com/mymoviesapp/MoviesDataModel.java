package example.com.mymoviesapp;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by alokps on 9/14/15.
 */
public class MoviesDataModel implements Serializable{

    private String movie_title;
    private int movie_year;
    private String movie_synopsis;
    private String movie_poster_Url;
    private String movie_poster_Url_Large;
    private int movie_critics_Score;
    private ArrayList<String> CastList;


    public int getMovie_critics_Score() {
        return movie_critics_Score;
    }

    public String getMovie_poster_Url() {
        return movie_poster_Url;
    }

    public String getMovie_synopsis() {
        return movie_synopsis;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public int getMovie_year() {
        return movie_year;
    }

    public String getCastList() {
        return TextUtils.join(", ", CastList);
    }

    public String getMovie_poster_Url_Large() {
        return movie_poster_Url_Large;
    }

    public static MoviesDataModel MoviesParseJsonResponse(JSONObject jsonObject){

      MoviesDataModel model = new MoviesDataModel();

        try{

          //Deserialize the JSON Response
          model.movie_title = jsonObject.getString("title");
          model.movie_year = jsonObject.getInt("year");
          model.movie_synopsis = jsonObject.getString("synopsis");
          model.movie_critics_Score = jsonObject.getJSONObject("ratings").getInt("critics_score");
          model.movie_poster_Url = jsonObject.getJSONObject("posters").getString("thumbnail");
            model.movie_poster_Url_Large = jsonObject.getJSONObject("posters").getString("detailed");

          //Construct the simple array of Cast Names
          model.CastList = new ArrayList<String>();

            JSONArray abridgedCast = jsonObject.getJSONArray("abridged_cast");

            for(int i=0; i<abridgedCast.length();i++){
                model.CastList.add(abridgedCast.getJSONObject(i).getString("name"));

            }

      }catch (JSONException e){
            //Print the Stack Trace
          e.printStackTrace();
            return null;

      }

      //Return New Object
        return model;

    }

    public static ArrayList<MoviesDataModel> FromJson(JSONArray jsonArray){

        ArrayList<MoviesDataModel> MoviesList = new ArrayList<MoviesDataModel>(jsonArray.length());

        MoviesList.clear();

        //Process each result in json Array, decode and convert to movie object
        for(int i=0;i < jsonArray.length(); i++){

            JSONObject moviesJson = null;

            try {
                moviesJson = jsonArray.getJSONObject(i);

            }catch (Exception e){
                e.printStackTrace();
                continue;
            }

            MoviesDataModel movie = MoviesDataModel.MoviesParseJsonResponse(moviesJson);
            if(movie != null){
                MoviesList.add(movie);
            }

        }
       return MoviesList;

    }

}
