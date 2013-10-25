package edu.csupomona.cs.cs356.classmate;

/*import info.androidhive.tabsswipe.GamesFragment;
import info.androidhive.tabsswipe.MoviesFragment;
import info.androidhive.tabsswipe.TopRatedFragment;*/
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            //return new TopRatedFragment();
      	  System.out.println("hello");
        case 1:
            // Games fragment activity
           // return new GamesFragment();
      	  System.out.println("hello");
        case 2:
            // Movies fragment activity
           // return new MoviesFragment();
      	  System.out.println("hello");
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}