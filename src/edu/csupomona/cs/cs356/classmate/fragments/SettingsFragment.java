package edu.csupomona.cs.cs356.classmate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.cs.cs356.classmate.R;

public class SettingsFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.settings_fragment, null);
		return root;
	}
}
