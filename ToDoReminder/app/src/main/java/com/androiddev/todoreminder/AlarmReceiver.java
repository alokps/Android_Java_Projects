package com.androiddev.todoreminder;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;


/**
 * The AlarmReceiver Method extends Broadcast Receiver and implements the
 * @author alokps
 *
 */

public class AlarmReceiver extends BroadcastReceiver {

	private static final String TAG = "AlarmReceiver";
	public static final String NMessage = "com.alokpotrapalli.com.androiddev.todoreminder.AlarmReceiver.nmessage";
	public static final String MYrow = "com.alokpotrapalli.com.androiddev.todoreminder.AlarmReceiver.myrow";
    private final boolean ALARMRX_DEBUG = false;
	Notification notification;
	
	@SuppressLint("Wakelock") @SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {

        if(ALARMRX_DEBUG == true) {
            Log.d(TAG, "Inside AlarmReceiver");
        }
		
		PowerManager powerManager = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

        //Create a new Wake Lock to wake up the Phone from sleep when the
        //notification is fired
		PowerManager.WakeLock wl = powerManager.newWakeLock(
				(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | 
				PowerManager.ACQUIRE_CAUSES_WAKEUP) , "TAG");
		
        String message = intent.getStringExtra(widgets.AlarmApp.reminder);
        long nrowid = intent.getLongExtra(widgets.AlarmApp.MYROWID, 0);
              
        //Acquire the CPU wake Lock
		wl.acquire();

        //get the Notification Manager System Service
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		//release the CPU wake Lock
        wl.release();
		
		/*Build the Notification Message
        Notification notification = new Notification(R.drawable.remind_notify_icon,
				message, System.currentTimeMillis());*/

		Notification.Builder builder = new Notification.Builder(context);

        //Create an Intent to Launch the Alert Dialog
		Intent nintent = new Intent(context, AlarmDialog.class);
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				nintent, 0);

		builder.setAutoCancel(false);
		builder.setContentTitle("ToDoReminder");
		builder.setContentText(message);
		builder.setContentIntent(contentIntent);
		builder.setSmallIcon(R.drawable.remind_notify_icon);

		notification = builder.build();
		notification.when = System.currentTimeMillis();

		/*notification.setLatestEventInfo(context, "DoReminder",
				message, contentIntent);*/

		notificationManager.notify("DoReminder", (int)nrowid, notification);

		Intent i = new Intent(context, RingtonePlayingService.class);
		context.startService(i);
		
		Intent dialogintent = new Intent(context, AlarmDialog.class);
		dialogintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		dialogintent.putExtra(NMessage, message);
		dialogintent.putExtra(MYrow, nrowid);
		context.startActivity(dialogintent);
					
	}


}