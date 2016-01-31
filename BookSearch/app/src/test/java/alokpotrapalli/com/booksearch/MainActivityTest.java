package alokpotrapalli.com.booksearch;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.shadows.ShadowLog;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by alokps on 9/7/15.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class MainActivityTest {

    private MainActivity activity;
    private ListView lstview;

    //@Before -> Junit4 annotation that specifies this method should run before each test is run
    //useful to do setup for objects that are needed in the test
    @Before
    public void setup(){

        //Convenience method to run MainActivity through Activity LifeCycle methods:
        //onCreate -> onStart -> onPostCreate -> onResume.
        activity = Robolectric.setupActivity(MainActivity.class);
        lstview = (ListView)activity.findViewById(R.id.lvbooks);//getting list XML layout
        ShadowLog.stream = System.out;//Printing the log messages in the console
    }

    //@Test -> JUnit 4 annotation specifying this is a test to run.
    @Test
    public void validateViewContent(){

        ListView testListView = (ListView) activity.findViewById(R.id.lvbooks);
        assertNotNull("List View could not be found", testListView);


        ProgressBar progressBarview = (ProgressBar)activity.findViewById(R.id.progressbar);
        assertNotNull("Progress Bar View could not be found",progressBarview);
    }

    @Test
    public void SearchBook(){

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Menu shadowMenu = new RoboMenu(shadowActivity.getApplicationContext());
        shadowActivity.onCreateOptionsMenu(shadowMenu);
        MenuItem item = new RoboMenuItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        assertNotNull("Menu View could not be found",item);

        searchView.setQuery("ChetanBhagat", true);
        shadowActivity.clickMenuItem(R.id.action_search);
    }

    @Test
    public void DetailActivityLaunched(){

        ShadowListView shadowListView = Shadows.shadowOf(lstview);//we need to shadow the list view
        shadowListView.populateItems();//Populates the List View

        //Click on the first Item
        shadowListView.performItemClick(0);

        Intent expectedIntent = new Intent(activity,BookDetailActivity.class);

        /**
         * An Android Activity doesnt expose a way out to find out about activities it launches
         * Robolectric's "ShadowActivity" keeps track of all launched activities and exposes this information
         * through "getNextStartedActivity" method
         */

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        actualIntent.putExtra("",0);


        //Determine if two intents are same for the purpose of intent resolution(filtering)
        //That is, if their action, data, type, class and categories are the same.This does not
        //compare any extra data included in the intents
        assertTrue(actualIntent.filterEquals(expectedIntent));


    }


}
