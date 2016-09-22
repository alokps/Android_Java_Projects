package com.androiddev.todoreminder;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import database.DBHandler;
import widgets.AlarmApp;

public class ToDoReminder extends ListActivity {

	private static final String TAG = "DoNotes";
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private Long mRowId;
	private static final int DELETE_ID = Menu.FIRST + 1;
    private boolean REMIND_DEBUG = false;

	private DBHandler mDbHelper;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_do_reminder);

		if(REMIND_DEBUG) {
            Log.d(TAG, "Inside DoReminders");
        }
		if (savedInstanceState == null) {

			// Get the Intent from the DoNotes Class
			mRowId = null;
		} else {

			mRowId = (Long) savedInstanceState
					.getSerializable(DBHandler.KEY_ROWID);
		}

		mDbHelper = new DBHandler(this);
		mDbHelper.open();
		getListData();
		registerForContextMenu(getListView());

	}

	@SuppressWarnings("deprecation")
	public void getListData() {

		Cursor ListViewCursor = mDbHelper.fetchAllNotes();
		startManagingCursor(ListViewCursor);

		// Create an array to specify the fields we want to display in the list
		// (only TITLE)
		String[] from = new String[] { DBHandler.KEY_REMINDER_TEXT };


		int[] to = new int[] { R.id.donotesrow };

		
		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.donotes_row, ListViewCursor, from, to);
				
		setListAdapter(notes);
				
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.do_reminder_add, menu);
	    
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {

		case R.id.add_symbol_label:

			CreateDoNote();

			break;
			
		case R.id.action_settings:
			
			Intent AppPref = new Intent(getApplicationContext(),AppPreferences.class);			
			startActivity(AppPref);

			break;

		}

		return super.onMenuItemSelected(featureId, item);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.Del);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			mDbHelper.deleteDoNote(info.id);
			getListData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {

		case R.id.action_settings:
			return true;

		case R.id.add_symbol_label:

			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	public void CreateDoNote() {

		Intent i = new Intent(this, AlarmApp.class);
		startActivityForResult(i, ACTIVITY_CREATE);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent(this, AlarmApp.class);
		i.putExtra(DBHandler.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);


	}

}
