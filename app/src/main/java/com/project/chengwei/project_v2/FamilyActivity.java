package com.project.chengwei.project_v2;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class FamilyActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private ImageButton btn_video;
    private SQLiteDBHelper dbHelper;
    private Cursor cursor;
    private String groupNum;
    final int RequestCameraCode = 1;
    final int RequestCallCode = 2;
    final int RequestExternalStorageCode = 3;
    final int RequestLocationCode = 4;
    final int RequestPermissionCode = 999;
    private final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }
        findViews();
        setToolbar();
        setListeners();
        initDB();
        closeDB();
    }
    //--------------------------------------------------------------------------------------------//
    //--------------------------------------- Database -------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    //Database : initial database
    private void initDB(){
        dbHelper = new SQLiteDBHelper(getApplicationContext());
        cursor = dbHelper.getProfileData();
        cursor.moveToPosition(0);
        groupNum = cursor.getString(cursor.getColumnIndex("room"));
    }
    //Database : close database
    private void closeDB(){
        dbHelper.close();
    }
    //--------------------------------------------------------------------------------------------//
    //---------------------------------- OnClick Listeners ---------------------------------------//
    //--------------------------------------------------------------------------------------------//
    private void setListeners() {
        btn_video.setOnClickListener(ImageBtnListener);
    }
    //--------------------------------------------------------------------------------------------//
    //------------------------------------ ImageBtnListener --------------------------------------//
    //--------------------------------------------------------------------------------------------//
    private android.view.View.OnClickListener ImageBtnListener = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_video:
                    //startActivity(new Intent(HomeActivity.this, WatchVideoActivity.class));
                    Intent intent = new Intent(getApplicationContext(),VideoFamilyActivity.class);
                    intent.putExtra("room_name",groupNum);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    //--------------------------------------------------------------------------------------------//
    //-------------------------- Version and Permission ------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    public void checkPermission() {
        int cameraPermission = ActivityCompat.checkSelfPermission(FamilyActivity.this, android.Manifest.permission.CAMERA);
        int readPermission = ActivityCompat.checkSelfPermission(FamilyActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ActivityCompat.checkSelfPermission(FamilyActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int callPermission = ActivityCompat.checkSelfPermission(FamilyActivity.this, android.Manifest.permission.CALL_PHONE);
        int locationPermission = ActivityCompat.checkSelfPermission(FamilyActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (readPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED ||
                callPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED ||
                locationPermission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            RequestRuntimePermission();
        }
    }
    private void RequestRuntimePermission() {
        //拒絕相機
        if (ActivityCompat.shouldShowRequestPermissionRationale(FamilyActivity.this, android.Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(FamilyActivity.this)
                    .setMessage("此應用程式需要CAMERA功能，請接受權限要求!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(FamilyActivity.this,
                                    new String[]{android.Manifest.permission.CAMERA},
                                    RequestCameraCode);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(FamilyActivity.this,"Camera Permission Canceled",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
        //拒絕電話
        else if (ActivityCompat.shouldShowRequestPermissionRationale(FamilyActivity.this,
                android.Manifest.permission.CALL_PHONE)) {
            new AlertDialog.Builder(FamilyActivity.this)
                    .setMessage("此應用程式需要CALL_PHONE功能，請接受權限要求!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(FamilyActivity.this,
                                    new String[]{android.Manifest.permission.CALL_PHONE},
                                    RequestCallCode);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(FamilyActivity.this,"Call Permission Canceled",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
        //拒絕讀取及寫入
        else if (ActivityCompat.shouldShowRequestPermissionRationale(FamilyActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(FamilyActivity.this)
                    .setMessage("此應用程式需要READ及WRITE_EXTERNAL_STORAGE功能，請接受權限要求!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(FamilyActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    RequestExternalStorageCode);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(FamilyActivity.this,"External Storage Permission Canceled",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
        //拒絕位置
        else if (ActivityCompat.shouldShowRequestPermissionRationale(FamilyActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(FamilyActivity.this)
                    .setMessage("此應用程式需要ACCESS_FINE_LOCATION功能，請接受權限要求!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(FamilyActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    RequestCallCode);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(FamilyActivity.this,"Location Permission Canceled",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
        //接受
        else{
            ActivityCompat.requestPermissions(FamilyActivity.this,new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.CALL_PHONE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    RequestPermissionCode);
        }
    }
    //跳出權限要求時，按允許或拒絕
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(FamilyActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FamilyActivity.this,"Permission Canceled",Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case RequestCameraCode: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(FamilyActivity.this,"Camera Permission Granted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FamilyActivity.this,"Camera Permission Canceled",Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case RequestCallCode: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(FamilyActivity.this,"Call Permission Granted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FamilyActivity.this,"Call Permission Canceled",Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case RequestExternalStorageCode: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(FamilyActivity.this,"External Storage Permission Granted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FamilyActivity.this,"External Storage Permission Canceled",Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case RequestLocationCode: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(FamilyActivity.this,"Location Permission Granted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FamilyActivity.this,"Location Permission Canceled",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    //--------------------------------------------------------------------------------------------//
    //-------------------------------------- initial Views ---------------------------------------//
    //--------------------------------------------------------------------------------------------//
    private void findViews() {
        myToolbar = (Toolbar) findViewById(R.id.toolbar_home);
        btn_video = (ImageButton) findViewById(R.id.btn_video);
    }
    //--------------------------------------------------------------------------------------------//
    //--------------------------------------- Toolbar --------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    private void setToolbar(){
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationIcon(R.drawable.ic_person_white);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FamilyActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }
    //--------------------------------------------------------------------------------------------//
    //----------------------------------- Options Item -------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
