package edu.csupomona.cs.cs356.classmate;
 
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
 
public class FriendManagerActivity extends Activity {
    // Declare Tab Variable
    ActionBar.Tab Tab1, Tab2, Tab3;
    Fragment fragmentTab1 = new FragmentTab1();
    Fragment fragmentTab2 = new FragmentTab2();
    Fragment fragmentTab3 = new FragmentTab3();
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendmanager_activity);
 
        ActionBar actionBar = getActionBar();
 
        // Hide Actionbar Icon
        actionBar.setDisplayShowHomeEnabled(false);
 
        // Hide Actionbar Title
        actionBar.setDisplayShowTitleEnabled(false);
 
        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        // Set Tab Icon and Titles
       // Tab1 = actionBar.newTab().setIcon(R.drawable.tab1);
        Tab1 = actionBar.newTab().setText("Add Friend");
        Tab2 = actionBar.newTab().setText("Requests");
        Tab3 = actionBar.newTab().setText("Friend List");
 
        // Set Tab Listeners
        Tab1.setTabListener(new TabListener(fragmentTab1));
        Tab2.setTabListener(new TabListener(fragmentTab2));
        Tab3.setTabListener(new TabListener(fragmentTab3));
 
        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
        actionBar.addTab(Tab3);
    }
}