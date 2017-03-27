package com.example.chatheads;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements ChatHeadService.onClickCustomListner {
    Button startService, stopService;
    private ChatHeadService myService;
    private boolean bound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService = (Button) findViewById(R.id.startService);
        stopService = (Button) findViewById(R.id.stopService);
        startService.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // startService(new Intent(getApplication(), ChatHeadService.class));
                Intent intent = new Intent(MainActivity.this, ChatHeadService.class);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        });
        stopService.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //stopService(new Intent(getApplication(), ChatHeadService.class));
                if (bound) {
                    myService.setListner(MainActivity.this); // unregister
                    unbindService(serviceConnection);
                    bound = false;
                }

            }
        });
    }

    @Override
    public void viewClicked() {
        Toast.makeText(MainActivity.this, "view clicked", Toast.LENGTH_SHORT).show();
    }


    /**
     * Callbacks for service binding, passed to bindService()
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // cast the IBinder and get MyService instance
            ChatHeadService.LocalBinder binder = (ChatHeadService.LocalBinder) service;
            myService = binder.getService();
            bound = true;
            myService.setListner(MainActivity.this); // register
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // bind to Service
        Intent intent = new Intent(this, ChatHeadService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        if (bound) {
            myService.setListner(this); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
    }
}
