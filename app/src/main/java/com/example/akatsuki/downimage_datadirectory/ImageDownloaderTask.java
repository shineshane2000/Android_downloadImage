package com.example.akatsuki.downimage_datadirectory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private ImageCompleteListener imageCompleteListener;
    private String path;
    private String fileName;

    public ImageDownloaderTask(String path, String fileName, ImageCompleteListener imageCompleteListener) {
        this.path = path;
        this.fileName = fileName;
        this.imageCompleteListener = imageCompleteListener;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap == null) imageCompleteListener.onImageSaveFailed(new Exception());
        imageCompleteListener.onImageSaveComplete(bitmap);

        if (isCancelled()) bitmap = null;

    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                urlConnection.disconnect();

                // 取得外部儲存裝置路徑
                // 開啟檔案
                File file = new File(path, fileName);
                // 開啟檔案串流
                FileOutputStream out = null;

                out = new FileOutputStream(file);
                // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                // 刷新串流 關閉檔案
                out.flush();
                out.close();

                return bitmap;
            } else {
                return null;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}