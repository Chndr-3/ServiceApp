package com.sentuh.serviceapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import com.sentuh.serviceapp.databinding.LayoutServiceBinding;
public class SentuhService extends Service{
    private WindowManager windowManager;
    private Callback callback;
    private View floatingView;
    private final IBinder binder = new SentuhBinder();
    public class SentuhBinder extends Binder {
        SentuhService getService() {
            return SentuhService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        com.sentuh.serviceapp.databinding.LayoutServiceBinding binding = LayoutServiceBinding.inflate(LayoutInflater.from(this));
         floatingView = binding.getRoot();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.CENTER | Gravity.TOP;
        windowManager.addView(floatingView, params);
        binding.button.setOnClickListener(view -> {
            callback.onDataReceived("Tes Chandra");
        });
    }
    public interface Callback {
        void onDataReceived(String data);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }
    }
    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}