package com.androiddev.todoreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androiddev.todoreminder.R;

public class AppPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	private final String TAG = AppPreferences.class.getSimpleName();

	private ListPreference date_pref;
	private CheckBoxPreference time_pref;
	private CheckBoxPreference vibrate_pref;
	private RingtonePreference tone_pref;
    private final boolean APPPREF_DEBUG = false;
	private static boolean timevalue;
	private static String dateformat;
	private static boolean vibrateval;
    private SharedPreferences sharedPreferences;
    private static String ringtoneName;
    private Editor spEditor;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        PreferenceManager.setDefaultValues(AppPreferences.this, R.xml.settings, false);
        getListView().setSelector(R.drawable.listview_selector);

        date_pref = (ListPreference) findPreference("date_format");
        time_pref = (CheckBoxPreference) findPreference("time_format");
        vibrate_pref = (CheckBoxPreference) findPreference("vibrate_pref");
        tone_pref = (RingtonePreference) findPreference("ringtone_pref");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        spEditor = sharedPreferences.edit();

        RestorePreferences();

        if (APPPREF_DEBUG == true){
            Log.d(TAG, "Inside AppPreferences");
        }
		date_pref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {

                        if (APPPREF_DEBUG == true) {
                            Log.d(TAG, "Inside OnPreferenceChange");
                        }
						int index = date_pref.findIndexOfValue(newValue
								.toString());

						if (date_pref != null) {
							preference.setSummary(newValue.toString());
							spEditor.putString("DATE_PREF_KEY", newValue.toString());
                            spEditor.commit();

						}

                        if (APPPREF_DEBUG == true) {
                            Log.d(TAG, "The Index is:" + index);
                        }
						switch (index) {
						case 0:
							dateformat = "yyyy-mm-dd";
							break;
						case 1:
							dateformat = "dd-mm-yyyy";
							break;
						case 2:
							dateformat = "mm-dd-yyyy";
							break;
						case 3:
							dateformat = "yyyy/mm/dd";
							break;
						case 4:
							dateformat = "dd/mm/yyyy";
							break;
						case 5:
							dateformat = "mm/dd/yyyy";
							break;
						default:
							dateformat = "yyyy/mm/dd";
							break;
						}

						boolean key = preference.getKey().equals(
								"date_format_arr");

                        if (APPPREF_DEBUG == true) {
                            Log.d(TAG, "The Date format is clicked" + key);
                        }
						return true;
					}
				});

		time_pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				timevalue = time_pref.isChecked();
				
				spEditor.putBoolean("TIME_PREF_KEY", timevalue);
                spEditor.commit();

				if(timevalue){
				// Send the User to the Settings Activity
				startActivity(new Intent(
						android.provider.Settings.ACTION_DATE_SETTINGS));
				}

                if (APPPREF_DEBUG == true) {
                    Log.d(TAG, "TIME " + timevalue);
                }
				return true;
			}

		});

		vibrate_pref
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {

						vibrateval = vibrate_pref.isChecked();
						
						spEditor.putBoolean("VIBRATE_PREF_KEY", vibrateval);
                        spEditor.commit();

                        if (APPPREF_DEBUG == true) {
                            Log.d(TAG, "VibrateValue " + sharedPreferences.getBoolean("VIBRATE_PREF_KEY", false));

                            Log.d(TAG, "Vibrate " + vibrateval);
                        }
						return true;
					}

				});

		tone_pref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						
						ringtoneName =newValue.toString();
						
						if(ringtoneName == null){
							ringtoneName ="content://media/internal/audio/media/69";
						}
						
						Ringtone ringtone = RingtoneManager.getRingtone(
								preference.getContext(), Uri.parse(ringtoneName));
						
						if(ringtone != null){
							preference.setSummary(ringtone.getTitle(getApplicationContext()));
							spEditor.putString("RING_PREF_KEY", ringtone.getTitle(getApplicationContext()));
                            spEditor.putString("RING_PREF_KEY1", ringtoneName);
                            spEditor.commit();
						}

                        if (APPPREF_DEBUG == true) {
                            Log.d(TAG, "Tone :" + ringtoneName);
                        }
						return true;
					}

				});

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.do_reminder_done, menu);

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		finish();
		return super.onMenuItemSelected(featureId, item);
	}
	
	@SuppressWarnings("deprecation")
	@Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

	
	public void RestorePreferences(){

		  date_pref.setSummary(sharedPreferences.getString("DATE_PREF_KEY", "yyyy/mm/dd"));
        if (APPPREF_DEBUG == true) {
            Log.d(TAG, "Date1 :" + "DATE_PREF_KEY");
        }

		  time_pref.setChecked(sharedPreferences.getBoolean("TIME_PREF_KEY", false));
        if (APPPREF_DEBUG == true) {
            Log.d(TAG, "time1 :" + "TIME_PREF_KEY");
        }

		  vibrate_pref.setChecked(sharedPreferences.getBoolean("VIBRATE_PREF_KEY", false));
        if (APPPREF_DEBUG == true) {
            Log.d(TAG, "vibrate :" + "VIBRATE_PREF_KEY");
        }

		  tone_pref.setSummary(sharedPreferences.getString("RING_PREF_KEY", "Default ringtone"));
        if (APPPREF_DEBUG == true) {
            Log.d(TAG, "ring :" + "RING_PREF_KEY");
        }

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key)
    {
        if (APPPREF_DEBUG == true) {
            Log.d(TAG, "Inside onSharedPreferenceChanged");
        }
            updatePrefSummary(findPreference(key));
	}
	
	private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }
         
    }

}
