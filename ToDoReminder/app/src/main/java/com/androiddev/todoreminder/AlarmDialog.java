package com.androiddev.todoreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import widgets.DateTimePicker;

/**
 * AlarmDialog Class is used to create a Alert Dialog window when the Alarm is Triggered.
 * The user is given Options to Close the Dialog Window and End the Alarm by Pressing OK
 * or to Snooze the Alarm for 5 min by pressing Snooze Option
 */

public class AlarmDialog extends Activity {

	private DateTimePicker myDTPicker;
	private PendingIntent mAlarmSend;
	private static final String TAG = AlarmDialog.class.getSimpleName();
    private static boolean ALARM_DEBUG = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_dialog);


		myDTPicker = new DateTimePicker(AlarmDialog.this);

        //Get the Alarm Manager Context
		final AlarmManager AM = (AlarmManager) AlarmDialog.this
				.getSystemService(Context.ALARM_SERVICE);

		//Build the Alert Dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				AlarmDialog.this);
		
		Intent myintent = getIntent();
		String msg = myintent.getStringExtra(com.androiddev.todoreminder.AlarmReceiver.NMessage);
		final long  my_row_id = myintent.getLongExtra(com.androiddev.todoreminder.AlarmReceiver.MYrow, 0);
		
		// set the title of the Alert Dialog
		alertDialogBuilder.setTitle(msg);

		// set dialog message
		alertDialogBuilder
				.setMessage("Click OK to Close")
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								
								//Stop the Ring Tone Service
								Intent i = new Intent(getApplicationContext(), RingtonePlayingService.class);
								stopService(i);
								
								// if yes is clicked, close the current activity
								AlarmDialog.this.finish();
							}
						})
				.setNegativeButton("Snooze",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
																
								//Stop the earlier Ring Tone Service
								Intent i = new Intent(getApplicationContext(), RingtonePlayingService.class);
								stopService(i);
								
								//Create a new alarm and snooze for 5 seconds
								Intent intent = new Intent(AlarmDialog.this, AlarmReceiver.class);
								mAlarmSend = PendingIntent.getBroadcast(AlarmDialog.this, (int)my_row_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
								
								//Snooze for 5 min
								AM.set(AlarmManager.RTC_WAKEUP, (myDTPicker.getDateTimeMillis()+5*60*1000), mAlarmSend);
								
								if(ALARM_DEBUG) {
                                    Log.d(TAG, "Seconds:" + myDTPicker.getDateTimeMillis());
                                }
								// close the current activity
								AlarmDialog.this.finish();
							}
						});

		//Create the Alert Dialog
		AlertDialog alertDialog =  alertDialogBuilder.create();

		//Show the alert Dialog in the window
		alertDialog.show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_dialog, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
