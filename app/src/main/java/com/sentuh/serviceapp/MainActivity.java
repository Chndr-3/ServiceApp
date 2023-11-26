package com.sentuh.serviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sentuh.serviceapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SentuhService.Callback {
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1234;
    ActivityMainBinding binding;
    private SentuhService sentuhService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission();
        }
        binding.btnActivateService.setOnClickListener(view1 ->  {
            Intent serviceIntent = new Intent(this, SentuhService.class);
            startService(serviceIntent);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        });

    }
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SentuhService.SentuhBinder binder = (SentuhService.SentuhBinder) service;
            sentuhService = binder.getService();
            sentuhService.setCallback(MainActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            sentuhService = null;
        }
    };

    @Override
    public void onDataReceived(String data) {
        Toast.makeText(this, "Data from service: " + data, Toast.LENGTH_SHORT).show();

    }

    private void requestOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
    }
}