package edu.csupomona.cs.cs356.classmate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.fragments.friends.AddFriendTab;
import edu.csupomona.cs.cs356.classmate.fragments.friends.FriendRequestsTab;
import edu.csupomona.cs.cs356.classmate.fragments.friends.FriendsListTab;

public class FriendsFragment extends Fragment {
	private FragmentTabHost tabHost;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.friends_fragment, null);

		tabHost = (FragmentTabHost)root.findViewById(android.R.id.tabhost);
		tabHost.setup(root.getContext(), getFragmentManager(), R.id.realtabcontent);
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("List"), FriendsListTab.class, null);
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Requests"), FriendRequestsTab.class, null);
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Add"), AddFriendTab.class, null);
		tabHost.setCurrentTab(0);

		return root;
	}

	@Override
	public void onResume() {
		if (tabHost != null) {
			tabHost.setCurrentTab(0);
		}
		
		super.onResume();
	}
}