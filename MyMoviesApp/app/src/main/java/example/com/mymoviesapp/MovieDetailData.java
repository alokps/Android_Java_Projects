package example.com.mymoviesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import alokpotrapalli.com.mymoviesapp.R;
import cz.msebera.android.httpclient.Header;


/**
 * Created by alokps on 9/18/15.
 *
 * The MovieDetailData is a Class that
 *
 *
 */
public class MovieDetailData extends AppCompatActivity{

    private ImageView LargeImage;
    private TextView  movietitle;
    private TextView  moviescore;
    private TextView  moviecast;
    private TextView  movieSynopsis;
    private FloatingActionButton floatingActionButton;
    private String PosterUrl;
    MoviesAPIClient PosterAPI;
    private ProgressBar progressBar;
    private static final String TAG = MovieDetailData.class.getSimpleName();

    //Get the Request Queue Instance
    RequestQueue pRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_movies_frame);

        PosterAPI = new MoviesAPIClient();

        pRequestQueue = Volley.newRequestQueue(this);

        LargeImage = (ImageView)findViewById(R.id.LargeImageView);
        movietitle = (TextView)findViewById(R.id.movie_title);
        moviecast  = (TextView)findViewById(R.id.movie_cast);
        movieSynopsis = (TextView)findViewById(R.id.movie_synopsis);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MoviesDataModel dataModel = (MoviesDataModel) getIntent().getSerializableExtra(MoviesFrame.MOVIE_DETAIL_KEY);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(dataModel.getMovie_title());


        LoadMovie(dataModel);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d(TAG,"I am in Share Intent");
                setShareIntent();
            }
        });

    }


    public void LoadMovie(MoviesDataModel model){

        //String LargePoster;
        String Poster;


        final ImageView imageView = (ImageView) findViewById(R.id.LargeImageView);

        Poster = PosterAPI.getPosterBaseUrl(model.getMovie_title().toString());

        //Log.d(TAG, "This is PosterUrl:" + PosterUrl);

        FetchPoster(Poster);

        movietitle.setText(model.getMovie_title());
        moviecast.setText(model.getCastList());
        movieSynopsis.setText(model.getMovie_synopsis());
    }


    public void FetchPoster(String movieurl){

        AsyncHttpClient posterClient = new AsyncHttpClient();

        progressBar.setVisibility(ProgressBar.VISIBLE);

        posterClient.get(movieurl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    PosterUrl = response.getString("Poster");
                    //Log.d(TAG, PosterUrl);
                    progressBar.setVisibility(ProgressBar.GONE);

                    //Load the movie poster
                    Picasso.with(getApplicationContext()).load(PosterUrl).error(R.drawable.large_movie_poster).into(LargeImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies_frame, menu);
        return true;
    }


    private void setShareIntent(){

        ImageView imageView = (ImageView) findViewById(R.id.LargeImageView);
        final  TextView tvtitle = (TextView)findViewById(R.id.movie_title);

        //get access to the bitmap of the URI
        Uri bmpUri = getLocalBitMapUri(imageView);

        //Construct a Share Intent with Link to Image
        Intent shareIntent = new Intent();

        //Construct a Share Intent with Link to Image
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, (String) tvtitle.getText());
        shareIntent.putExtra(Intent.EXTRA_STREAM,bmpUri);

        //Launch Share Menu
        startActivity(Intent.createChooser(shareIntent,"Share Image"));

    }

    public Uri getLocalBitMapUri(ImageView imageView){

        //Extract BitMap from the ImageView Drawable
        Drawable drawable = imageView.getDrawable();

        Bitmap bmp = null;

        if(drawable instanceof BitmapDrawable){

            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else{
            return null;
        }

        //Store the image to default External storage
        Uri bmpuri = null;

        try{

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.
                    DIRECTORY_DOWNLOADS),"share_image_"+System.currentTimeMillis()+".bmp");

            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG,90,out);
            out.close();
            bmpuri = Uri.fromFile(file);

        }catch (IOException e){
            e.printStackTrace();
        }

        return bmpuri;
    }

}
