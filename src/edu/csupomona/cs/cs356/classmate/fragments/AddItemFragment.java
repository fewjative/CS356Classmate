package edu.csupomona.cs.cs356.classmate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.fragments.additem.AddClassTab;
import edu.csupomona.cs.cs356.classmate.fragments.additem.AddEventTab;
import edu.csupomona.cs.cs356.classmate.fragments.additem.SearchEventTab;

public class AddItemFragment extends Fragment {
	private FragmentTabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		tabHost = new FragmentTabHost(getActivity());
		tabHost.setup(getActivity(), getChildFragmentManager(), R.id.flTabContentPane);
		tabHost.addTab(tabHost.newTabSpec("addClassTab").setIndicator("Add Class"), AddClassTab.class, null);//DailyScheduleTab.class
		tabHost.addTab(tabHost.newTabSpec("addEventTab").setIndicator("Add Event"), AddEventTab.class, null);//WeeklyScheduleTab.class
		tabHost.addTab(tabHost.newTabSpec("searchEventTab").setIndicator("Search Event"), SearchEventTab.class, null);//WeeklyScheduleTab.class
		return tabHost;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		tabHost = null;
	}
}