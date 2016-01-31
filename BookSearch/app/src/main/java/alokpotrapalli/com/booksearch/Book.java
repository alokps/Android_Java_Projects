package alokpotrapalli.com.booksearch;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by alokps on 9/4/15.
 *
 * Lets Define a Model that represents the relevant data for a single book.
 * The object needs to contain basic info for each book such as "title", "author"
 * "cover image" and "open Library Id"
 *
 */
public class Book implements Serializable{
    private String OpenLibraryId;
    private String author;
    private String title;

    public String getOpenLibraryId(){

        return OpenLibraryId;
    }

    public String getAuthor(){
        return author;
    }

    public String getTitle(){
        return title;
    }

    //get medium sized book covers from Covers API
    public String getCoverUrl(){

        return "http://covers.openlibrary.org/b/olid/" + OpenLibraryId + "-M.jpg?default=false";
    }

    //get Large sized book covers from Covers API
    public String getLargeCoverUrl(){

        return  "http://covers.openlibrary.org/b/olid/" + OpenLibraryId + "-L.jpg?default=false";
    }


    //Method to deserialize the JSON response in order to construct an instance of
    //Book class

    public static Book fromJson(JSONObject jsonObject){

        Book book = new Book();

        try{

            if(jsonObject.has("cover_edition_key")){
                book.OpenLibraryId = jsonObject.getString("cover_edition_key");
            }else if (jsonObject.has("edition_key")){
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.OpenLibraryId = ids.getString(0);
            }
            book.title = jsonObject.has("title_suggest")? jsonObject.getString("title_suggest"): " ";
            book.author = getAuthor(jsonObject);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return book;

    }

    // Return comma separated author list when there is more than one author
    private static String getAuthor(final JSONObject jsonObject) {

        try {
            final JSONArray authors = jsonObject.getJSONArray("author_name");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return " ";
        }
    }


    //Decodes array of book json results into business model objects
    public static ArrayList<Book> fromJson(JSONArray jsonArray){

        ArrayList<Book> books = new ArrayList<Book>(jsonArray.length());

        //process each result in json array,decode and convert to business
        //object
        for(int i=0;i< jsonArray.length();i++){

            JSONObject bookJson = null;
            try{
                bookJson = jsonArray.getJSONObject(i);
            }catch (JSONException e){
                e.printStackTrace();
                continue;
            }

            Book book = Book.fromJson(bookJson);
            if(book != null){
                books.add(book);
            }
        }

        return books;
    }

}

