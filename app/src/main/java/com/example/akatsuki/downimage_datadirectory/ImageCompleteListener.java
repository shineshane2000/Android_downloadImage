package com.example.akatsuki.downimage_datadirectory;

import android.graphics.Bitmap;

/**
 * Created by Akatsuki on 2017/10/26.
 */

//下載完成後回傳
public interface ImageCompleteListener {
	public void onImageSaveComplete(Bitmap bitmap);

	public void onImageSaveFailed(Exception exception);
}
