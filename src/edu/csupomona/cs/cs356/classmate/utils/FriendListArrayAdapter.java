package edu.csupomona.cs.cs356.classmate.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.csupomona.cs.cs356.classmate.R;
import java.util.List;

public class FriendListArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final List<String> values;

	public FriendListArrayAdapter(Context context, List<String> values) {
		super(context, R.layout.tab_friends_list, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.tab_friends_list, parent, false);

		TextView textView = (TextView)rowView.findViewById(R.id.label);
		textView.setText(values.get(position));

		return rowView;
	}

}
