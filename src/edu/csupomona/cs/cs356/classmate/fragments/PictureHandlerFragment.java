package edu.csupomona.cs.cs356.classmate.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;

// fragment that allow for browsing pictures on photo or taking picture

public abstract class PictureHandlerFragment extends Fragment {
	public static final int CODE_CAMERA_REQUEST = 0x048;
	
	protected void startCamera() {
		Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, CODE_CAMERA_REQUEST);
	}
	
	//return Bitmap from resource id of pictures 
	protected Bitmap getBitmap(int resource) {
		Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getBaseContext().getResources(),
				 resource);
		return bitmap;
	}
	
	//for references only
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    super.onActivityResult(requestCode, resultCode, data);
//	    switch(requestCode){
//	    case CODE_CAMERA_REQUEST:
//	        if(resultCode == getActivity().RESULT_OK) {
//	           Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
////	           imgView.setImageBitmap(thumbnail);
//	        }
//	        break;
//	    }
//	}
}