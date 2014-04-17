package edu.csupomona.cs.cs356.classmate.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.fragment.friends.FriendRequestsTab;
import edu.csupomona.cs.cs356.classmate.fragment.friends.FriendsListTab;

public class FriendsFragment extends Fragment {
	private FragmentTabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		tabHost = new FragmentTabHost(getActivity());
		tabHost.setup(getActivity(), getChildFragmentManager(), R.id.flTabContentPane);
		tabHost.addTab(tabHost.newTabSpec("friendsListTab").setIndicator(getResources().getString(R.string.friends_tab_list)), FriendsListTab.class, null);
		tabHost.addTab(tabHost.newTabSpec("friendRequestsTab").setIndicator(getResources().getString(R.string.friends_tab_requests)), FriendRequestsTab.class, null);
		return tabHost;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		tabHost = null;
	}
}
