package edu.csupomona.cs.cs356.classmate;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
	    
	    String userName = getActivity().getIntent().getExtras().getString(LoginActivity.KEY_USERNAME);
	    System.out.println(userName);
	    //So we have the correct user
	    //Need to get the correct 'friend' id tomorrow and set them to invisible fields
	    
	    //final TextView hidden = (TextView)getListView().findViewById(R.id.hidden);
	    //hidden.setText(userName);

	    
	    
	    String[] values = new String[] { 
	   		 "Kimberely Dollinger",
	   		 "Sydney Atwell",  
	   		 "Gisele Klenk", 
	   		 "Tisa Levron", 
	   		 "Cathie Banuelos", 
	   		 "Glynda Sell", 
	   		 "Teodora Bernhard", 
	   		 "Macie Mcfarlain", 
	   		 "Kaylene Joo", 
	   		 "Valentin Petrie", 
	   		 "Delia Councill",
	   		 "Benjamin Garton", 
	   		 "Wanda Willette", 
	   		 "Hong Zuber", 
	   		 "Charla Pfeil", 
	   		 "Karisa Ordonez", 
	   		 "Shavonda Donohue", 
	   		 "Charity Cullins", 
	   		 "Tamica Hayworth", 
	   		 "Lupita Mcclean" };
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.fragment2,
	       R.id.label, values);
	    setListAdapter(adapter);
	    
	  }

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	    // do something with the data
		  String item = (String) getListAdapter().getItem(position);
		    Toast.makeText(getActivity(), item + " selected", Toast.LENGTH_SHORT).show();

	  }
 
}