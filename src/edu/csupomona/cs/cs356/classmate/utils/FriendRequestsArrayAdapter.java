package edu.csupomona.cs.cs356.classmate.utils;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.csupomona.cs.cs356.classmate.R;

public class FriendRequestsArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final List<String> values;

	public FriendRequestsArrayAdapter(Context context, List<String> values) {
		super(context, R.layout.tab_friends_requests, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.tab_friends_requests, parent, false);

		TextView textView = (TextView)rowView.findViewById(R.id.label);
		textView.setText(values.get(position));

		/*Button btnAccept = (Button)rowView.findViewById(R.id.button1);
		btnAccept.setOnClickListener(context);

		Button btnDecline = (Button)rowView.findViewById(R.id.button2);
		btnDecline.setOnClickListener(context);*/

		return rowView;
	}
}
