package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler {
	
	//Setup All the Database Keys
	public static final String KEY_REMINDER_TEXT = "reminder_text"; 
	public static final String KEY_ALARM_DATE = "alarm_date";
	public static final String KEY_ALARM_TIME = "alarm_time";
	public static final String KEY_ALARM_FREQ = "alarm_freq";
	public static final String KEY_PRIORITY = "priority";
	public static final String KEY_PRIORITY_TEXT="priority_text";
	public static final String KEY_NOTES = "notes";
	public static final String KEY_MYRING = "my_ringtone";
	public static final String KEY_ROWID = "_id";
	private static final String TAG = "DBHandler";
	private DatabaseHelper DbHelper;
	private SQLiteDatabase Db;
    private boolean DBDEBUG = false;

	
	/**
	 * Create the DoNotes Database using the Command below.
	 */
	
    private static final String DATABASE_CREATE =
    		"create table donotes (_id integer primary key autoincrement,"+
         "reminder_text text, alarm_date text , alarm_time text, " +
         "alarm_freq text,priority text,priority_text text,notes text,my_ringtone text);";
    
    //Database Name
    private static final String DATABASE_NAME = "donotesdb";
    //Database Table
    private static final String DATABASE_TABLE = "donotes";
    //Database Version
    private static final int DATABASE_VERSION = 2;
    //Context
    private final Context mCtx;
	
   //Create a Database Helper class by extending the SQLiteHelper 
    private static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			//Create the Database
			db.execSQL(DATABASE_CREATE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS donotes");
			//Create the data base with the New Data
			onCreate(db);
		}
    	
    }
    
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public DBHandler(Context ctx) {
        this.mCtx = ctx;
    }
    
    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DBHandler open() throws SQLException {
        DbHelper = new DatabaseHelper(mCtx);
        Db = DbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DbHelper.close();
    }
    
    /**
     * Create a new note using all the parameters provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long createDoNote(String reminder_text, String alarm_date, String alarm_time,
    		String alarm_freq, String priority, String priority_text, String notes, String my_ringtone) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_REMINDER_TEXT, reminder_text);
        initialValues.put(KEY_ALARM_DATE, alarm_date);
        initialValues.put(KEY_ALARM_TIME, alarm_time);
        initialValues.put(KEY_ALARM_FREQ, alarm_freq);
        initialValues.put(KEY_PRIORITY, priority);
        initialValues.put(KEY_PRIORITY_TEXT, priority_text);
        initialValues.put(KEY_NOTES, notes);
        initialValues.put(KEY_MYRING, my_ringtone);


        return Db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    /**
     * Delete the Do Note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteDoNote(long rowId) {

    	if(DBDEBUG) {
            Log.d(TAG, "Inside the deleteDoNote method");
        }
    	return Db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =
            Db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
            		KEY_REMINDER_TEXT, KEY_ALARM_DATE,KEY_ALARM_TIME,KEY_ALARM_FREQ,
            		KEY_PRIORITY,KEY_PRIORITY_TEXT,KEY_NOTES,KEY_MYRING}, KEY_ROWID + 
            		"=" + rowId, null,null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {

        return Db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_REMINDER_TEXT,
        		KEY_ALARM_DATE,KEY_ALARM_TIME,KEY_ALARM_FREQ,KEY_PRIORITY,KEY_PRIORITY_TEXT,KEY_NOTES,KEY_MYRING}, null, null, null, null, null);
    }

    
    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateNote(long rowId, String reminder_text, String alarm_date, String alarm_time,
    		String alarm_freq, String priority, String priority_text,String notes,String my_ringtone) {
        ContentValues args = new ContentValues();

        args.put(KEY_REMINDER_TEXT, reminder_text);
        args.put(KEY_ALARM_DATE, alarm_date);
        args.put(KEY_ALARM_TIME, alarm_time);
        args.put(KEY_ALARM_FREQ, alarm_freq);
        args.put(KEY_PRIORITY, priority);
        args.put(KEY_PRIORITY_TEXT, priority_text);
        args.put(KEY_NOTES, notes);
        args.put(KEY_MYRING,my_ringtone);

        return Db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    
}
