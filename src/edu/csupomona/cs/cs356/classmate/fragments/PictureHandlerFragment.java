package edu.csupomona.cs.cs356.classmate.fragments;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

// abstract fragment that allow for browsing pictures on photo or taking picture

public abstract class PictureHandlerFragment extends Fragment {
	private static final String TAG = "PicHandlerFragment";
	public static final int CODE_CAMERA_REQUEST = 0x048;
	public static final int CODE_GALLERY_REQUEST = 0x058;
	
	protected void startCamera() {
		Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, CODE_CAMERA_REQUEST);
	}
	
	protected void startGallery() {
		Intent gallery_intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(gallery_intent, CODE_GALLERY_REQUEST);
	}
	
   /**
	* return Bitmap from resource id
	* @param resource resource id of the picture to be converted 
	*/
	protected Bitmap getBitmap(int resource) {
		// get drawable resource from icon and convert to bitmap form
		Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getBaseContext().getResources(),
				 resource);
		return bitmap;
	}
	
	public Uri getPhotoFileUri(String fileName) {
	    // Get safe storage directory for photos
	    File mediaStorageDir = new File(
	        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), TAG);

	    // Create the storage directory if it does not exist
	    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
	        Log.d(TAG, "failed to create directory");
	    }

	    // Return the file target for the photo based on filename
	    return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
	}
	
	public void galleryAddPic(String fileName) {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(fileName);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    getActivity().sendBroadcast(mediaScanIntent);
	}
	
/**for references only
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    super.onActivityResult(requestCode, resultCode, data);
//	    switch(requestCode){
//	    case CODE_CAMERA_REQUEST:
//	        if(resultCode == getActivity().RESULT_OK) {
//	           Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//	           imgView.setImageBitmap(thumbnail);
//	        }
//	        break;
//	    }
//	}
  */
}