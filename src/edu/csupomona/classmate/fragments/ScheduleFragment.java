package edu.csupomona.classmate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.schedule.DailyScheduleTab;
import edu.csupomona.classmate.fragments.schedule.WeeklyScheduleTab;
import edu.csupomona.classmate.fragments.schedule.FreeTimeTab;

public class ScheduleFragment extends Fragment {
	private final User VIEWER;

	public ScheduleFragment() {
		super();
		this.VIEWER = null;
	}

	public ScheduleFragment(User viewer) {
		super();
		this.VIEWER = viewer;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.viewpager_tab_layout, container, false);

		final ViewPager vpContentPane = (ViewPager)ROOT.findViewById(R.id.vpContentPane);
		vpContentPane.setAdapter(new ScheduleFragmentPagerAdapter(getChildFragmentManager()));
		if (VIEWER != null) {
			vpContentPane.setCurrentItem(1);
		}

		return ROOT;
	}

	private class ScheduleFragmentPagerAdapter extends FragmentPagerAdapter {
		ScheduleFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			
			if(VIEWER==null)
			{
				switch (i) {
					case 0: return new DailyScheduleTab(VIEWER);
					case 1: return new WeeklyScheduleTab(VIEWER);
					default: return null;
				}
			}
			else
			{
				switch (i) {
				case 0: return new DailyScheduleTab(VIEWER);
				case 1: return new WeeklyScheduleTab(VIEWER);
				case 2: return new FreeTimeTab(VIEWER);
				default: return null;
			}
			}
		}

		@Override
		public CharSequence getPageTitle(int i) {
			if(VIEWER==null)
			{
				switch (i) {
					case 0: return getString(R.string.schedule_tab_daily);
					case 1: return getString(R.string.schedule_tab_weekly);
					default: throw new RuntimeException();
				}
			}
			else
			{
				switch (i) {
				case 0: return getString(R.string.schedule_tab_daily);
				case 1: return getString(R.string.schedule_tab_weekly);
				case 2: return getString(R.string.schedule_tab_freetime);
				default: throw new RuntimeException();
				}
			}
		}

		@Override
		public int getCount() {
			if(VIEWER==null)
			{
				return 2;
			}
			else
			{
				return 3;
			}
		}
	}
}
