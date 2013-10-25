package edu.csupomona.cs.cs356.classmate;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Fragment;
import android.app.ListFragment;
 
public class FragmentTab2 extends ListFragment {
   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment2, container, false);
        return rootView;
    }*/
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
	        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
	        "Linux", "OS/2" };
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
	        android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	  }

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	    // do something with the data
		  String item = (String) getListAdapter().getItem(position);
		    Toast.makeText(getActivity(), item + " selected", Toast.LENGTH_SHORT).show();

	  }
 
}