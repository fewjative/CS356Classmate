package edu.csupomona.cs.cs356.classmate;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Section;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class ClassmateService extends RemoteViewsService {
	 @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ClassRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ClassRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
   private List<Section> classes = new ArrayList<Section>();
   private Context mContext;
   private int mAppWidgetId;
   private int id;
   private Intent intent;

   public ClassRemoteViewsFactory(Context context, Intent intent) {
       mContext = context;
       mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
               AppWidgetManager.INVALID_APPWIDGET_ID);
       this.intent = intent;
       System.out.println("ClassRemoteVF constructor: " + intent.getIntExtra("userId", 0));
       id = intent.getIntExtra("userId", 0);
 	
   }

   public void onCreate() {
   	
   	System.out.println("In onCreate");
       // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
       // for example downloading or creating content etc, should be deferred to onDataSetChanged()
       // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
       
   	/*for (int i = 0; i < mCount; i++) {
           mWidgetItems.add(new WidgetItem(i + "!"));
       }*/
       
   	RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(id));
		params.put("full", "yes");
		AsyncHttpClient client = new AsyncHttpClient();

		client.get("http://www.lol-fc.com/classmate/getuserclassestoday.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				 setupSchedule(response);
				 System.out.println("ListSize: " + classes.size());
			}
		});


       // We sleep for 3 seconds here to show how the empty view appears in the interim.
       // The empty view is set in the StackWidgetProvider and should be a sibling of the
       // collection view.
       try {
           Thread.sleep(3000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }

   public void onDestroy() {
       // In onDestroy() you should tear down anything that was setup for your data source,
       // eg. cursors, connections, etc.
       classes.clear();
   }

   public int getCount() {
       return classes.size();
   }

   public RemoteViews getViewAt(int position) {
       // position will always range from 0 to getCount() - 1.

       // We construct a remote views item based on our widget item xml file, and set the
       // text based on the position.
       RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
       rv.setTextViewText(R.id.widget_item, classes.get(position).toString());

       // Next, we set a fill-intent which will be used to fill-in the pending intent template
       // which is set on the collection view in StackWidgetProvider.
       Bundle extras = new Bundle();
       extras.putInt(ClassmateProvider.EXTRA_ITEM, position);
       Intent fillInIntent = new Intent();
       fillInIntent.putExtras(extras);
       rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

       // You can do heaving lifting in here, synchronously. For example, if you need to
       // process an image, fetch something from the network, etc., it is ok to do it here,
       // synchronously. A loading view will show up in lieu of the actual contents in the
       // interim.
       try {
           System.out.println("Loading view " + position);
           id = intent.getIntExtra("userId", 0);
     		System.out.println("In loading view, UserID = " + id);
           Thread.sleep(500);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }

       // Return the remote views object.
       return rv;
   }

   public RemoteViews getLoadingView() {
       // You can create a custom loading view (for instance when getViewAt() is slow.) If you
       // return null here, you will get the default loading view.
       return null;
   }

   public int getViewTypeCount() {
       return 1;
   }

   public long getItemId(int position) {
       return position;
   }

   public boolean hasStableIds() {
       return true;
   }

   public void onDataSetChanged() {
   	//attempting to get the users id
   	
      id = intent.getIntExtra("userId", 0);
		System.out.println("Line 27: UpdateService, UserID = " + id);
		onCreate();
		
       // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
       // on the collection view corresponding to this factory. You can do heaving lifting in
       // here, synchronously. For example, if you need to process an image, fetch something
       // from the network, etc., it is ok to do it here, synchronously. The widget will remain
       // in its current state while work is being done here, so you don't need to worry about
       // locking up the widget.
   }
   
   private void setupSchedule(String response) {
      classes = new ArrayList<Section>();
      if (1 < response.length()) {
              try {
                      Section s;
                      JSONObject jObj;
                      JSONArray myjsonarray = new JSONArray(response);
                      for (int i = 0; i < myjsonarray.length(); i++) {
                              jObj = myjsonarray.getJSONObject(i);
                              s = new Section(
                                      jObj.getInt("class_id"),
                                      jObj.getString("title"),
                                      jObj.getString("time_start"),
                                      jObj.getString("time_end"),
                                      jObj.getString("weekdays"),
                                      jObj.getString("date_start"),
                                      jObj.getString("date_end"),
                                      jObj.getString("instructor"),
                                      jObj.getString("building"),
                                      jObj.getString("room"),
                                      jObj.getString("section"),
                                      jObj.getString("major_short"),
                                      jObj.getString("major_long"),
                                      jObj.getString("class_num"),
                                      jObj.getString("term")
                              );

                              classes.add(s);
                      }
              } catch (JSONException e) {
                      e.printStackTrace();
              }
      }
	}

}