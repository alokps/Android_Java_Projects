package alokpotrapalli.com.booksearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by alokps on 9/6/15.
 */
public class BookDetailActivity extends AppCompatActivity {

    private static final String TAG = BookDetailActivity.class.getSimpleName();
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvPageCount;
    private BookClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        //Fetch the Views
        ivBookCover = (ImageView)findViewById(R.id.ivBookCover);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvAuthor = (TextView)findViewById(R.id.tvAuthor);
        tvPublisher = (TextView)findViewById(R.id.tvPublisher);
        tvPageCount = (TextView)findViewById(R.id.tvPageCount);

        //Use the Book Passed through intent to Populate the Detailed view
        Book book = (Book) getIntent().getSerializableExtra(MainActivity.BOOK_DETAIl_KEY);
        LoadBooks(book);

    }

    public void LoadBooks(Book book){

        //Change the activity Title
        this.setTitle(book.getTitle());

        //Populate the Image
        Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());

        //fetch the Extra Books Data from the books API
        client = new BookClient();
        client.getExtraBookDetails(book.getOpenLibraryId(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {

                Log.d(TAG, response.toString());

                try {

                    if (response.has("publishers")) {

                        //Display Comma Separated list of Publishers
                        final JSONArray publisher = response.getJSONArray("publishers");
                        final int numpublishers = publisher.length();
                        final String[] publishers = new String[numpublishers];
                        for (int i = 0; i < numpublishers; i++) {
                            publishers[i] = publisher.getString(i);
                        }

                        tvPublisher.setText(TextUtils.join(",", publishers));
                    }
                    if (response.has("number_of_pages")) {
                        tvPageCount.setText(Integer.toString(response.getInt("number_of_pages")) + " pages");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Inflate the Menu with Custmo Menu Layout
        getMenuInflater().inflate(R.menu.menu_book_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_share){
            setShareIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void setShareIntent(){

        ImageView imageView = (ImageView) findViewById(R.id.ivBookCover);
        final  TextView tvtitle = (TextView)findViewById(R.id.tvTitle);

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
            return  null;
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
