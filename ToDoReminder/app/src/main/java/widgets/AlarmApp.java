/**
 * Copyright 2010 Lukasz Szmit <devmail@szmit.eu>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package widgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androiddev.todoreminder.AlarmReceiver;
import com.androiddev.todoreminder.AppPreferences;
import com.androiddev.todoreminder.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.TreeMap;

import database.DBHandler;

public class AlarmApp extends Activity implements OnClickListener {
	public static final String TAG = "AlarmApp";
	public static final String reminder = "com.alokpotrapalli.doreminder.reminder";
	public static final String VIBRATE = "com.alokpotrapalli.doreminder.vibrate";
	public static final String MYROWID= "com.alokpotrapalli.doreminder.rowid";
    private final boolean MYDEBUG = false;
	private EditText NoteET;
	private EditText ReminderTV;
	private static String AlarmRepeat;
	private static Long mRowId;
	private static DBHandler mDbHelper;
	private TextView AlarmDate;
	private TextView AlarmTime;
	private TextView AlarmRepeatText;
	private TextView PriorityTextView;
	private String AlarmPriority;
	private static Long second_val;
	private static Long milliinterval;
	private static boolean repeatreq;
	// Maps alarmId -> alarm.
	public TreeMap<Long, PendingAlarm> pendingAlarms;
	private Calendar mycalender;
	AppPreferences appPreferences;
	DateTimePicker dateTimePicker;
	private static String datefmt;
	//private static String alarmring;
    private SharedPreferences mSharedPreference;
    private static AlarmManager am;
    private static int local_hours;
    private static int local_mins;
    private static int local_sec;
    private static String local_ampm;
    private static boolean alarm_initiated = false;
    private static int local_year;
    private static int local_month;
    private static int local_day;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_options_do_reminder);

		// Open the Database
		mDbHelper = new DBHandler(this);
		mDbHelper.open();

		// Initialize all the Views
		NoteET = (EditText) findViewById(R.id.NoteEditText);
		ReminderTV = (EditText) findViewById(R.id.ReminderTextView);
		AlarmDate = (TextView) findViewById(R.id.AlarmDateTV);
		AlarmTime = (TextView) findViewById(R.id.AlarmTimeTV);
		AlarmRepeatText = (TextView) findViewById(R.id.repeatAlarmText);
		PriorityTextView = (TextView) findViewById(R.id.PriorityText);
		pendingAlarms = new TreeMap<Long,PendingAlarm>();
		mycalender = Calendar.getInstance();
		dateTimePicker = new DateTimePicker(AlarmApp.this);
		appPreferences = new AppPreferences();
        mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        if(am == null) {

            am = (AlarmManager) AlarmApp.this.getSystemService(Context.ALARM_SERVICE);
        }

		// Setup OnClick Listeners
		findViewById(R.id.AlarmButton).setOnClickListener(this);
		findViewById(R.id.RedRadioButton).setOnClickListener(this);
		findViewById(R.id.BlueRadioButton).setOnClickListener(this);
		findViewById(R.id.GreenRadioButton).setOnClickListener(this);
		findViewById(R.id.PurpleRadioButton).setOnClickListener(this);
		findViewById(R.id.dailybutton).setOnClickListener(this);
		findViewById(R.id.nonebutton).setOnClickListener(this);
		findViewById(R.id.monthlybutton).setOnClickListener(this);
		findViewById(R.id.yearlybutton).setOnClickListener(this);

		if (savedInstanceState == null) {

			// Get the Intent from the DoNotes Class
			mRowId = null;
		} else {

			mRowId = (Long) savedInstanceState
					.getSerializable(DBHandler.KEY_ROWID);
		}

		if (mRowId == null) {

			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(DBHandler.KEY_ROWID)
					: null;
		}

		Read_Reminder_text();

	}

	@SuppressWarnings("deprecation")
	public void Read_Reminder_text() {

        String AlarmPriorityText;

		if (mRowId != null) {

			Cursor note = mDbHelper.fetchNote(mRowId);
			startManagingCursor(note);

			ReminderTV.setText(note.getString(note
					.getColumnIndexOrThrow(DBHandler.KEY_REMINDER_TEXT)));
			AlarmDate.setText(note.getString(note
					.getColumnIndexOrThrow(DBHandler.KEY_ALARM_DATE)));
			AlarmTime.setText(note.getString(note
					.getColumnIndexOrThrow(DBHandler.KEY_ALARM_TIME)));
			NoteET.setText(note.getString(note
					.getColumnIndexOrThrow(DBHandler.KEY_NOTES)));
			AlarmPriority = note.getString(note
					.getColumnIndexOrThrow(DBHandler.KEY_PRIORITY));
			AlarmPriorityText = note.getString(note
					.getColumnIndexOrThrow(DBHandler.KEY_PRIORITY_TEXT));
			AlarmRepeat = note.getString(note
					.getColumnIndexOrThrow(DBHandler.KEY_ALARM_FREQ));
			//alarmring = note.getString(note.getColumnIndexOrThrow(DBHandler.KEY_MYRING));

            if(MYDEBUG == true) {

                Log.d(TAG, "Alarm Priority is:" + AlarmPriority);
            }

			if (AlarmPriority == null) {

				AlarmPriority = "None";
			}
			if (AlarmRepeat == null) {

				AlarmRepeat = "None";
			}

			if (AlarmRepeat.compareTo("None") == 0) {

				AlarmRepeatText.setText("None");
			}
			if (AlarmRepeat.compareTo("Daily") == 0) {

				AlarmRepeatText.setText("Daily");
			} else if (AlarmRepeat.compareTo("Monthly") == 0) {

				AlarmRepeatText.setText("Monthly");
			} else if (AlarmRepeat.compareTo("Yearly") == 0) {

				AlarmRepeatText.setText("Yearly");
			}

			if (AlarmPriorityText.compareTo("None") == 0) {

				PriorityTextView.setText("None");
			}
			if (AlarmPriorityText.compareTo("!!!!") == 0) {

				PriorityTextView.setText("!!!!");
			} else if (AlarmPriorityText.compareTo("!!!") == 0) {

				PriorityTextView.setText("!!!");
			} else if (AlarmPriorityText.compareTo("!") == 0) {

				PriorityTextView.setText("!");
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.do_reminder_del, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {

		case R.id.remove_symbol_label:

            if(MYDEBUG == true) {

                Log.d(TAG, "DeleteButton Pressed");
            }

			if(mRowId == null) {
                SaveReminderState();
                CancelAlarm(mRowId);
                if(MYDEBUG == true) {
                    Log.d(TAG, "The Cancelled Alarmid is" + mRowId);
                }
                mDbHelper.deleteDoNote(mRowId);
            }else{
                CancelAlarm(mRowId);
                if(MYDEBUG == true) {
                    Log.d(TAG, "The mRowId3 is:" + mRowId);
                }
                mDbHelper.deleteDoNote(mRowId);
                if(MYDEBUG == true) {
                    Log.d(TAG, "The mRowId4 is:" + mRowId);
                }
            }

			finish();

            break;

		case R.id.done_symbol_label:

			SaveReminderState();

			finish();

			break;

		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		SaveReminderState();
		outState.putSerializable(DBHandler.KEY_ROWID, mRowId);
	}

    @Override
    protected void onRestart() {
        super.onRestart();
        Read_Reminder_text();
    }

    @Override
	protected void onPause() {
		super.onPause();
		SaveReminderState();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(MYDEBUG == true) {
            Log.d(TAG, "Inside onDestroy Method");
        }
        mDbHelper.close();
    }


	private void SaveReminderState() {

		String ReminderTxt = ReminderTV.getText().toString();
		String alarmdate = AlarmDate.getText().toString();
		String alarmtime = AlarmTime.getText().toString();
		String Notestxt = NoteET.getText().toString();
		String alarmfreq = AlarmRepeatText.getText().toString();
		String alarmpriority = AlarmPriority;
		String prioritytext = PriorityTextView.getText().toString();

        String myringtone=(mSharedPreference.getString("RING_PREF_KEY1", null));

        if(MYDEBUG == true) {
            Log.d(TAG, "MyRingtone is:" + myringtone);
        }

		if(myringtone == null){
			myringtone = "content://media/internal/audio/media/5";
		}

        if(MYDEBUG == true) {
            System.out.println("The AlarmFreq is: " + alarmfreq);
        }
		if (mRowId == null) {

			long id = mDbHelper.createDoNote(ReminderTxt, alarmdate, alarmtime,
					alarmfreq, alarmpriority, prioritytext, Notestxt,myringtone);
            if(MYDEBUG == true) {
                Log.d(TAG, "The mRowId0 is:" + id);
            }
			if (id > 0) {
				mRowId = id;
                if(MYDEBUG == true) {
                    Log.d(TAG, "The mRowId1 is:" + mRowId);
                }
			}
		} else {
			mDbHelper.updateNote(mRowId, ReminderTxt, alarmdate, alarmtime,
					alarmfreq, alarmpriority, prioritytext, Notestxt,myringtone);
            if(MYDEBUG == true) {
                Log.d(TAG, "The mRowId2 is:" + mRowId);
            }
		}

	}



    @Override
	public void onClick(View view) {

        boolean invaliddate;

		switch (view.getId()) {

		case R.id.AlarmButton:

            if(MYDEBUG == true) {
                Log.d(TAG, "Inside AlarmDialog");
            }

			// Force the keyboard to close
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ReminderTV.getWindowToken(), 0);

            SaveReminderState();

			showDateTimeDialog();

			break;

		case R.id.RedRadioButton:

            if(MYDEBUG == true) {
                Log.d(TAG, "Inside RedRadioButton");
            }

			AlarmPriority = "RED";

			PriorityTextView.setText("!!!!");

			if (ReminderTV.getText().toString().contains("!")) {

				;

			} else {
				ReminderTV.setText("!!!! " + ReminderTV.getText().toString());
			}

			break;

		case R.id.BlueRadioButton:

            if(MYDEBUG == true) {
                Log.d(TAG, "Inside BlueRadioButton");
            }

			AlarmPriority = "BLUE";

			PriorityTextView.setText("!!!");

			if (ReminderTV.getText().toString().contains("!")) {

				// Do nothing

			} else {
				ReminderTV.setText("!!! " + ReminderTV.getText().toString());
			}

			break;

		case R.id.GreenRadioButton:

            if(MYDEBUG == true) {
                Log.d(TAG, "Inside GreenRadioButton");
            }

			AlarmPriority = "GREEN";

			PriorityTextView.setText("!");

			if (ReminderTV.getText().toString().contains("!")) {

				// Do nothing

			} else {
				ReminderTV.setText("! " + ReminderTV.getText().toString());
			}

			break;

		case R.id.PurpleRadioButton:

            if(MYDEBUG == true) {
                Log.d(TAG, "Inside PurpleRadioButton");
            }

			AlarmPriority = "PURPLE";

			PriorityTextView.setText("None");

			if (ReminderTV.getText().toString().contains("!")) {

				String[] splitstring = ReminderTV.getText().toString()
						.split(" ");

				if (splitstring.length > 1) {

					String msg = "";

					for (int i = 1; i < splitstring.length; i++) {
						msg = msg + splitstring[i] + " ";
						ReminderTV.setText(msg);
						msg = ReminderTV.getText().toString();
					}
				} else {
					ReminderTV.setText(splitstring[0]);
				}
			} else {
				// Do nothing
			}

			break;

		case R.id.dailybutton:

            if(MYDEBUG == true) {
                Log.d(TAG, "Daily Button Pressed");
            }

			AlarmRepeatText.setText("Daily");

			AlarmRepeat = "Daily";

			// 24 hours in milliseconds
			milliinterval = Long.valueOf(24 * 60 * 60 * 1000);

            if(MYDEBUG == true) {
                Log.d(TAG, "The Value of second_val now is:" + second_val);
            }

			if (second_val == 0) {

				String time = AlarmTime.getText().toString();
				String[] feilds = time.split(":");
				int hour = Integer.parseInt(feilds[0]);
				String[] feilds1 = feilds[1].split(" ");
				int minutes = Integer.parseInt(feilds1[0]);
                if(MYDEBUG == true) {
                    Log.d(TAG, "Hours is:" + hour);
                    Log.d(TAG, "Minutes is:" + minutes);
                }
				second_val = Long.valueOf(mycalender.getTimeInMillis());

			}
            if(MYDEBUG == true) {
                Log.d(TAG, "The Value of second_val is:" + second_val);
            }

			if(alarm_initiated == true) {
                setAlarmRepeat(second_val, milliinterval, mRowId);
                alarm_initiated = false;
                Toast.makeText(
                        getApplicationContext(),
                        "The Alarm will repeat everyday at:"
                                + AlarmTime.getText().toString(), Toast.LENGTH_LONG)
                        .show();
            }

			repeatreq = true;

			break;

		case R.id.nonebutton:

            if(MYDEBUG == true) {
                Log.d(TAG, "nonebutton Button Pressed");

            }
			AlarmRepeatText.setText("None");

			AlarmRepeat = "None";

            if (repeatreq == true) {
                CancelAlarm(mRowId);
                repeatreq = false;
             }


			break;

		case R.id.monthlybutton:

            if(MYDEBUG == true) {
                Log.d(TAG, "monthlybutton Button Pressed");
                Log.d(TAG, "The Month is:"+local_month);
                Log.d(TAG, "The Day is:"+local_day);
            }
			AlarmRepeatText.setText("Monthly");

            invaliddate = false;

			AlarmRepeat = "Monthly";

			// Calendar this month = Calendar.getInstance();
			if ((local_month == 1)) {


				if ((CheckLeapYear() == true) && (local_day<= 29)) {

					// months in milliseconds
					milliinterval = Long.valueOf(2592000000L);

                    if(MYDEBUG == true) {
                        Log.d(TAG, "Months in Milliseconds:" + milliinterval);
                    }

				} else if ((CheckLeapYear() == false)
						&& (local_month <= 28)) {

					// months in milliseconds (30*24*60*60*1000)
					milliinterval = Long.valueOf(2592000000L);
                    if(MYDEBUG == true) {
                        Log.d(TAG, "Months in Milliseconds1:" + milliinterval);
                    }
				}
                if(local_day > 29)
                {

                    invaliddate = true;

                    //Add a Toast
					Toast.makeText(
							getApplicationContext(),
							"Feb has less than 30 days, "
									+ "please choose another date",
							Toast.LENGTH_LONG).show();

				}

			} else {

				// months in milliseconds
				milliinterval = Long.valueOf(2592000000L);
			}

			if (second_val == 0) {

				String time = AlarmTime.getText().toString();
				String[] feilds = time.split(":");
				int hour = Integer.parseInt(feilds[0]);
				String[] feilds1 = feilds[1].split(" ");
				int minutes = Integer.parseInt(feilds1[0]);
                if(MYDEBUG == true) {
                    Log.d(TAG, "Hours is:" + hour);
                    Log.d(TAG, "Minutes is:" + minutes);
                }
				second_val = Long.valueOf(mycalender.getTimeInMillis());

			}

            if(MYDEBUG == true) {
                Log.d(TAG, "The Value of second_val is:" + second_val);
            }
            if(alarm_initiated == true) {

                if(invaliddate == false) {
                    setAlarmRepeat(second_val, milliinterval, mRowId);
                    Toast.makeText(
                            getApplicationContext(),
                            "The Alarm will repeat every month on the day: "
                                    + (local_day)
                                    + " and time: "
                                    + AlarmTime.getText().toString(), Toast.LENGTH_LONG)
                            .show();
                }
                alarm_initiated = false;

            }
			repeatreq = true;



			break;

		case R.id.yearlybutton:

            if(MYDEBUG == true) {
                Log.d(TAG, "yearlybutton Button Pressed");
            }
			AlarmRepeatText.setText("Yearly");

			AlarmRepeat = "Yearly";

			// Calendar this month = Calendar.getInstance();
			if (CheckLeapYear() == true) {
				// Year in milliseconds 366 * 24 * 60 * 60 * 1000
				milliinterval = Long.valueOf(31622400000L);
			} else {
                // Year in milliseconds 365 * 24 * 60 * 60 * 1000
				milliinterval = Long.valueOf(31536000000L);
			}

			if (second_val == 0) {

				String time = AlarmTime.getText().toString();
				String[] feilds = time.split(":");
				int hour = Integer.parseInt(feilds[0]);
				String[] feilds1 = feilds[1].split(" ");
				int minutes = Integer.parseInt(feilds1[0]);
                if(MYDEBUG == true) {
                    Log.d(TAG, "Hours is:" + hour);
                    Log.d(TAG, "Minutes is:" + minutes);
                }
				second_val = Long.valueOf(mycalender.getTimeInMillis());
			}

            if(MYDEBUG == true) {
                Log.d(TAG, "The Value of second_val is:" + second_val);
            }

			if(alarm_initiated == true) {
                setAlarmRepeat(second_val, milliinterval, mRowId);
                alarm_initiated = false;
                Toast.makeText(
                        getApplicationContext(),
                        "The Alarm will repeat every Year at: "
                                + (local_month)
                                + "/"
                                + (local_day)
                                + " and time "
                                + AlarmTime.getText().toString(), Toast.LENGTH_LONG)
                        .show();
            }
			repeatreq = true;

			break;

		default:
			break;

		}

	}

	@SuppressLint("InflateParams") private void showDateTimeDialog() {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(this);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
				.findViewById(R.id.DateTimePicker);
		// Check is system is set to use 24h time (this doesn't seem to work as
		// expected though)
		final String timeS = android.provider.Settings.System.getString(
				getContentResolver(),
				android.provider.Settings.System.TIME_12_24);

		final boolean is24h = !(timeS == null || timeS.equals("12"));

        if(MYDEBUG == true) {
            Log.d(TAG, "The value is" + is24h);
        }
		// Update TextViews when the "OK" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						mDateTimePicker.clearFocus();

						if(mSharedPreference.getString("DATE_PREF_KEY", null) == null){
							datefmt  = "yyyy/mm/dd";
						}else{
							datefmt = mSharedPreference.getString("DATE_PREF_KEY", null);
						}
						
						if (datefmt
								.compareTo("yyyy-mm-dd") == 0) {
							((TextView) findViewById(R.id.AlarmDateTV)).setText(mDateTimePicker
									.get(Calendar.YEAR)
									+ "-"
									+ (mDateTimePicker.get(Calendar.MONTH) + 1)
									+ "-"
									+ mDateTimePicker
											.get(Calendar.DAY_OF_MONTH));
						} else if (datefmt.compareTo(
								"dd-mm-yyyy") == 0) {
							((TextView) findViewById(R.id.AlarmDateTV))
									.setText(mDateTimePicker
											.get(Calendar.DAY_OF_MONTH)
											+ "-"
											+ (mDateTimePicker
													.get(Calendar.MONTH) + 1)
											+ "-"
											+ mDateTimePicker
													.get(Calendar.YEAR));
						} else if (datefmt.compareTo(
								"mm-dd-yyyy") == 0) {
							((TextView) findViewById(R.id.AlarmDateTV))
									.setText((mDateTimePicker
											.get(Calendar.MONTH) + 1)
											+ "-"
											+ mDateTimePicker
													.get(Calendar.DAY_OF_MONTH)
											+ "-"
											+ mDateTimePicker
													.get(Calendar.YEAR));
						} else if (datefmt.compareTo(
								"dd/mm/yyyy") == 0) {
							((TextView) findViewById(R.id.AlarmDateTV))
									.setText(mDateTimePicker
											.get(Calendar.DAY_OF_MONTH)
											+ "/"
											+ (mDateTimePicker
													.get(Calendar.MONTH) + 1)
											+ "/"
											+ mDateTimePicker
													.get(Calendar.YEAR));
						} else if (datefmt.compareTo(
								"mm/dd/yyyy") == 0) {
							((TextView) findViewById(R.id.AlarmDateTV))
									.setText((mDateTimePicker
											.get(Calendar.MONTH) + 1)
											+ "/"
											+ mDateTimePicker
													.get(Calendar.DAY_OF_MONTH)
											+ "/"
											+ mDateTimePicker
													.get(Calendar.YEAR));
						} else {

							((TextView) findViewById(R.id.AlarmDateTV)).setText(mDateTimePicker
									.get(Calendar.YEAR)
									+ "/"
									+ (mDateTimePicker.get(Calendar.MONTH) + 1)
									+ "/"
									+ mDateTimePicker
											.get(Calendar.DAY_OF_MONTH));
						}

						if (mDateTimePicker.is24HourView()) {
							((TextView) findViewById(R.id.AlarmTimeTV))
									.setText(mDateTimePicker
											.get(Calendar.HOUR_OF_DAY)
											+ ":"
											+ mDateTimePicker
													.get(Calendar.MINUTE)
											+ ":"
											+ mDateTimePicker
													.get(Calendar.SECOND));
						} else {
							((TextView) findViewById(R.id.AlarmTimeTV)).setText(mDateTimePicker
									.get(Calendar.HOUR)
									+ ":"
									+ mDateTimePicker.get(Calendar.MINUTE)
									+ ":"
									+ mDateTimePicker.get(Calendar.SECOND)
									+ " "
									+ (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM ? "AM"
											: "PM"));
						}
						second_val = Long.valueOf(mDateTimePicker.getDateTimeMillis());

                        local_hours = mDateTimePicker
                                .get(Calendar.HOUR);
                        local_mins =  mDateTimePicker.get(Calendar.MINUTE);
                        local_sec =   mDateTimePicker.get(Calendar.SECOND);
                        local_ampm = (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM ? "AM"
                                : "PM");

                        local_year = mDateTimePicker
                                .get(Calendar.YEAR);
                        local_month = (mDateTimePicker
                                .get(Calendar.MONTH) + 1);
                        local_day = mDateTimePicker
                                .get(Calendar.DAY_OF_MONTH);

						setAlarm(second_val, (mRowId));

						mDateTimeDialog.dismiss();

					}

				});

		// Cancel the dialog when the "Cancel" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						mDateTimeDialog.cancel();
					}
				});

		// Reset Date and Time pickers when the "Reset" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						mDateTimePicker.reset();
					}
				});

		// Setup TimePicker
		mDateTimePicker.setIs24HourView(is24h);
		// No title on the dialog window
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();

	}

	public void setAlarm(Long seconds, Long mRowIds) {

		final Context context = AlarmApp.this;

		final PendingIntent mAlarmSender;

        alarm_initiated = true;

        if(MYDEBUG == true) {
            Log.d(TAG, "Inside setAlarm");

            Log.d(TAG, "mRowIds is:" + mRowIds);
        }

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra(reminder, ReminderTV.getText().toString());
		intent.putExtra(MYROWID, mRowIds);
		
		mAlarmSender = PendingIntent.getBroadcast(context, mRowIds.intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);


		try {

			// In API version 19 (KitKat), the set() method of is no longer
			// guaranteed to have exact timing semantics. A setExact()
			// method is supplied, but is not available with the minimum SDK
			// version used by this application. Here we look for this
			// new method and use it if we find it. Otherwise, we fall back
			// to the old set() method.

			Method setExact = AlarmManager.class.getDeclaredMethod("setExact",
					int.class, long.class, PendingIntent.class);
			setExact.invoke(am, AlarmManager.RTC_WAKEUP, seconds, mAlarmSender);
		} catch (NoSuchMethodException e) {
			am.set(AlarmManager.RTC_WAKEUP, (seconds), mAlarmSender);
		} catch (IllegalAccessException e) {
			// TODO(cgallek) combine these all with the java 7 OR syntax.
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		if (mRowId == null) {
			//Store all the Pending Alarms
			pendingAlarms.put(mRowId, new PendingAlarm(seconds,
					mAlarmSender));
		} else {
			pendingAlarms.put(mRowId, new PendingAlarm(seconds, mAlarmSender));
		}
		Toast.makeText(context,
				"The Alarm is set to:" + AlarmTime.getText().toString(),
				Toast.LENGTH_LONG).show();
	}

	public void setAlarmRepeat(Long seconds, Long secinterval, Long mRowIdsa) {

		final Context context = getApplicationContext();

        //Cancel the Alarm Set up by setAlarm and create the Repeat Alarm
        CancelAlarm(mRowIdsa);

		final PendingIntent mAlarmSender1;

        if(MYDEBUG == true) {
            Log.d(TAG, "Inside setAlarmRepeat");
        }
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra(reminder, ReminderTV.getText().toString());
        intent.putExtra(MYROWID, mRowIdsa);

		mAlarmSender1 = PendingIntent.getBroadcast(context, mRowIdsa.intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

			// In API version 19 (KitKat), the set() method of is no longer
			// guaranteed to have exact timing semantics. A setExact()
			// method is supplied, but is not available with the minimum SDK
			// version used by this application. Here we look for this
			// new method and use it if we find it. Otherwise, we fall back
			// to the old set() method.

			am.setRepeating(AlarmManager.RTC_WAKEUP, seconds, secinterval,
					mAlarmSender1);

		if (mRowId == null) {
			// Store all the Pending Alarms
			pendingAlarms.put(mRowId, new PendingAlarm(seconds,
					mAlarmSender1));
		} else {
			pendingAlarms.put(mRowId, new PendingAlarm(seconds, mAlarmSender1));
		}

        ((TextView) findViewById(R.id.AlarmTimeTV)).setText(local_hours
                + ":"
                + local_mins
                + ":"
                + local_sec
                + " "
                + local_ampm);

        //Reset the Second val
        second_val = Long.valueOf(0L);
	}

	public void CancelAlarm(long alarmid) {

        Context context = AlarmApp.this;

        if(MYDEBUG == true) {
            Log.d(TAG, "The Value of AlarmId is:" + alarmid);
        }
        Intent intent = new Intent(context,AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,(int)alarmid,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        am.cancel(sender);

        //Remove the Alarm
        pendingAlarms.remove(alarmid);

	}

	public boolean CheckLeapYear() {

		boolean leapyear = false;

        //Log.d(TAG,"The Value of the year is:"+local_year);

		if ((local_year % 4 == 0) && (local_year % 100 != 0)) {

			leapyear = true;

		} else if ((local_year % 4 == 0)  &&
                   (local_year % 100 == 0)
				&& (local_year % 400 == 0)) {
			leapyear = true;
		}

		return leapyear;
	}


	private class PendingAlarm {
		private Long time;
		private PendingIntent pendingIntent;

		PendingAlarm(Long time, PendingIntent pendingIntent) {
			this.time = time;
			this.pendingIntent = pendingIntent;
		}

		public Long time() {
			return time;
		}

		public PendingIntent pendingIntent() {
			return pendingIntent;
		}
	}

}
