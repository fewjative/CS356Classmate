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
		tabHost = new FragmentTabHost(getActivity());
		tabHost.setup(getActivity(), getChildFragmentManager(), R.id.flTabContentPane);
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("List"), FriendsListTab.class, null);
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Requests"), FriendRequestsTab.class, null);
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Add"), AddFriendTab.class, null);

		return tabHost;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		tabHost = null;
	}
}