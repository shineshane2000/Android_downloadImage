package com.example.akatsuki.downimage_datadirectory;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_EXTERNAL_STORAGE = 1000;

    private ImageView imageView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) MainActivity.this.findViewById(R.id.imageView);
        //取得權限
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET},
                    REQUEST_EXTERNAL_STORAGE);
        } else {
            startImageDownload();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //取得權限，進行檔案存取
                    startImageDownload();
                } else {
                    //使用者拒絕權限，停用檔案存取功能
                    Toast.makeText(this, "請開啟權限", Toast.LENGTH_SHORT).show();

                }
                return;
        }
    }

    private void startImageDownload() {

        //這裡處理儲存位置
        String path = Environment.getExternalStorageDirectory().toString();
        //這裡設置
        String filename = "myFileName.jpg";
        //設置下載地址
        String downloadUrl = "https://i.imgur.com/GTMs4pm.jpg";

        ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask(path, filename, new ImageCompleteListener() {
            @Override
            public void onImageSaveComplete(Bitmap bitmap) {
                Log.d(this.getClass().getSimpleName(), "success!!");
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onImageSaveFailed(Exception exception) {
                Log.e(this.getClass().getSimpleName(), "Failed!!");
            }
        });

        imageDownloaderTask.execute(downloadUrl);
    }
}
