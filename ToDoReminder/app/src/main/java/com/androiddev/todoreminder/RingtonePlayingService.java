package com.androiddev.todoreminder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

public class RingtonePlayingService extends Service {
	static Ringtone r;
	AppPreferences myPreferences;
    private SharedPreferences RPreferences;
    private final boolean SERVICE_DEBUG = false;
	
	private static final String TAG = RingtonePlayingService.class
			.getSimpleName();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Uri notification;
        if(SERVICE_DEBUG == true) {
            Log.d(TAG, "Inside RingtonePlayingService");
        }
		myPreferences = new AppPreferences();

        RPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String myringtone = RPreferences.getString("RING_PREF_KEY1","content://media/internal/audio/media/5");
        boolean vibrate_flag = RPreferences.getBoolean("VIBRATE_PREF_KEY", false);


        if(vibrate_flag){

            VibratorService(getBaseContext());
        }


        if(myringtone != null){

         if(SERVICE_DEBUG == true) {
             Log.d("Alok", myringtone);
         }
		 notification = Uri.parse(myringtone);
       }	else{
    	     notification = RingtoneManager
    					.getDefaultUri(RingtoneManager.TYPE_ALARM);
       }

        //Get the Ringtone
		r = RingtoneManager.getRingtone(getBaseContext(), notification);

		// playing sound alarm
		r.play();

		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		r.stop();
	}

    public void VibratorService(Context context){
        //Vibrator Service
        Vibrator vib = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(2000);
    }

}