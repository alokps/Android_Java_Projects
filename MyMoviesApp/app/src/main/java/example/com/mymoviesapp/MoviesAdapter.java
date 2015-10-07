package example.com.mymoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import alokpotrapalli.com.mymoviesapp.R;

/**
 * Created by alokps on 9/14/15.
 */
public class MoviesAdapter extends ArrayAdapter<MoviesDataModel> {


    public MoviesAdapter(Context context,ArrayList<MoviesDataModel> aMovies){
        super(context,0,aMovies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MoviesDataModel movie = getItem(position);

        if(convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_under_gridview, parent, false);
        }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.gridviewImage);
            TextView textView = (TextView) convertView.findViewById(R.id.CriticScore);
            TextView textView1 = (TextView) convertView.findViewById(R.id.mymovietitle);
            TextView textView2 = (TextView) convertView.findViewById(R.id.moviecast);

            Picasso.with(getContext()).load(movie.getMovie_poster_Url_Large()).
                    placeholder(R.drawable.large_movie_poster).into(imageView);
            textView.setText("Score: " + Integer.toString(movie.getMovie_critics_Score()) + "%");
            textView1.setText(movie.getMovie_title());
            textView2.setText("Cast: " + movie.getCastList());


        return convertView;
    }

}
