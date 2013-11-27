package edu.csupomona.cs.cs356.classmate;

import edu.csupomona.cs.cs356.classmate.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ClassmateProvider extends AppWidgetProvider {
    public static final String TOAST_ACTION = "edu.csupomona.cs.cs356.classmate.TOAST_ACTION";
    public static final String EXTRA_ITEM = "edu.csupomona.cs.cs356.classmate.EXTRA_ITEM";
    public static final String UPDATE_ID = "edu.csupomona.cs.cs356.classmate.ClassmateProvider.UPDATE_ID";
    static int id;


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
   	 
   		System.out.println("Provider: called onReceive");
   		super.onReceive(context, intent);
   		System.out.println("getAction() = " + intent.getAction().toString());
   		if(intent.getAction().equals(UPDATE_ID)){
   			id = intent.getIntExtra(LoginActivity.INTENT_KEY_USERID, 0);
   			System.out.println("Provider: THE USER ID = " + id); 	
   		}else{
   			System.out.println("Provider: UPDATE_ID is not working");
   		}
   		
   		if(id!=0)
   		{
   			System.out.println("The user id is: " + id);
   			AppWidgetManager mgr = AppWidgetManager.getInstance(context);
   			 ComponentName thisAppWidget = new ComponentName(context.getPackageName(), ClassmateProvider.class.getName());
 	          int[] appWidgetIds = mgr.getAppWidgetIds(thisAppWidget);
	           System.out.println("Valid id received now needs to update");
 	            onUpdate(context,mgr,appWidgetIds);
   			
 	        if (intent.getAction().equals(TOAST_ACTION)) {
 	            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
 	                    AppWidgetManager.INVALID_APPWIDGET_ID);
 	            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
 	            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
 	            
 	        }
 	      
   		}
   		
        
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
   	 System.out.println("Inside onUpdate");
        // update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {

            // Here we setup the intent which points to the StackViewService which will
            // provide the views for this collection.
            Intent intent = new Intent(context, ClassmateService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);          
            intent.putExtra("userId",id);
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rv.setRemoteAdapter(R.id.list_view, intent);

            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            rv.setEmptyView(R.id.list_view, R.id.empty_view);
                      

            // Here we setup the a pending intent template. Individuals items of a collection
            // cannot setup their own pending intents, instead, the collection as a whole can
            // setup a pending intent template, and the individual items can set a fillInIntent
            // to create unique before on an item to item basis.
            Intent toastIntent = new Intent(context, ClassmateProvider.class);
            toastIntent.setAction(ClassmateProvider.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.list_view, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}