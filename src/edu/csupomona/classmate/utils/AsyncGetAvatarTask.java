package edu.csupomona.classmate.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class AsyncGetAvatarTask extends AsyncTask<String, Void, Bitmap> {
	private static LruCache<String, Bitmap> cache;

	private final ImageView IMAGEVIEW;

	public AsyncGetAvatarTask(ImageView iv) {
		this.IMAGEVIEW = iv;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		if (cache == null) {
			int maxSize = (int)(Runtime.getRuntime().maxMemory() / 1024);
			int cacheSize = maxSize / 4;
			cache = new LruCache<String, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getByteCount() / 1024;
				}
			};
		} else {
			Bitmap cached = cache.get(params[0]);
			if (cached != null) {
				return cached;
			}
		}

		Bitmap image = null;
		InputStream is = null;
		try {
			is = new URI(params[0]).toURL().openStream();
			image = BitmapFactory.decodeStream(is);
			cache.put(params[0], image);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
			}
		}

		return image;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			// TODO: disabled to hide image changes for now
			//Animation animFadeIn = AnimationUtils.loadAnimation(IMAGEVIEW.getContext(), R.anim.user_avatar_fadein);
			//IMAGEVIEW.startAnimation(animFadeIn);
			IMAGEVIEW.setImageBitmap(result);
		}
	}
}
