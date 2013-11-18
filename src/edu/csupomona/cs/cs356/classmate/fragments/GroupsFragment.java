package edu.csupomona.cs.cs356.classmate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.fragments.groups.AddGroupTab;
import edu.csupomona.cs.cs356.classmate.fragments.groups.MyGroupsTab;

public class GroupsFragment extends Fragment {
	public static final String INTENT_KEY_GROUP = "group";

	private FragmentTabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		tabHost = new FragmentTabHost(getActivity());
		tabHost.setup(getActivity(), getChildFragmentManager(), R.id.flTabContentPane);
		tabHost.addTab(tabHost.newTabSpec("myGroupsTab").setIndicator("My Groups"), MyGroupsTab.class, null);
		tabHost.addTab(tabHost.newTabSpec("addGroupTab").setIndicator("Search"), AddGroupTab.class, null);
		return tabHost;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		tabHost = null;
	}
}
