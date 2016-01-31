package alokpotrapalli.com.booksearch;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alokps on 9/5/15.
 *
 *
 *
 */
public class BookAdapter extends ArrayAdapter<Book> {


    //View LookUp Cache
    private static class ViewHolder{

        public ImageView ivCover;
        public TextView  tvTitle;
        public TextView  tvAuthor;

    }

    public BookAdapter(Context context, ArrayList<Book> aBooks){
        super(context,0,aBooks);
    }

    //Translates a particular book given a position into relevant
    //row within an AdapterView

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //get the data item for this position
        final Book book = getItem(position);

        //check if an existing view is being reused,otherwise inflate the view
        ViewHolder viewHolder;


        if(convertView == null){

            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_book,parent,false);
            viewHolder.ivCover = (ImageView)convertView.findViewById(R.id.ivBookCover);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            viewHolder.tvAuthor = (TextView)convertView.findViewById(R.id.tvAuthor);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();

        }
        viewHolder.tvAuthor.setText(book.getAuthor());
        viewHolder.tvTitle.setText(book.getTitle());
        Picasso.with(getContext()).load(Uri.parse(book.getCoverUrl())).error(R.drawable.ic_nocover).into(viewHolder.ivCover);


        return convertView;
    }

}
