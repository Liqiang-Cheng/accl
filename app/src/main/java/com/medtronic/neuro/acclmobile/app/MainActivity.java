package com.medtronic.neuro.acclmobile.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.nio.ByteBuffer;
import java.util.UUID;


public class MainActivity extends Activity {

    private static final UUID WATCHAPP_UUID = UUID.fromString("7f521c24-563f-45a5-be92-a9c2e4d7f76c");
    private static final int ACCL_DATA_TAG = 42;
    private static final int ACCL_DATA_SIZE_KEY =  0;
    private static final int ACCL_DATA_PAYLOAD_KEY = 1;
    private ListView mAcclList;
    private Handler mHandler = new Handler();
    PebbleKit.PebbleDataLogReceiver mDataLogReceiver = new PebbleKit.PebbleDataLogReceiver(WATCHAPP_UUID) {
        @Override
        public void receiveData(Context context, UUID logUuid, Long timestamp, Long tag, byte[] data) {
            if (tag.intValue() == ACCL_DATA_TAG) {
                AcclData acclData = AcclData.decodeByte(data);
                AcclListAdapter adapter = (AcclListAdapter) mAcclList.getAdapter();
                adapter.add(acclData);
            }
        }
    };
    PebbleKit.PebbleDataReceiver mDataReceiver = new PebbleKit.PebbleDataReceiver(WATCHAPP_UUID) {
        @Override
        public void receiveData(Context context, int transactionId, PebbleDictionary pebbleDictionary) {
            if (pebbleDictionary.size()==0) {
                PebbleKit.sendNackToPebble(getApplicationContext(), transactionId);
                return;
            }
            int sampleSize = pebbleDictionary.getInteger(ACCL_DATA_SIZE_KEY).intValue();
            byte[] bytes = pebbleDictionary.getBytes(ACCL_DATA_PAYLOAD_KEY);
            int dataSize = bytes.length / sampleSize;
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            for (int i=0; i < sampleSize; i++) {
                byte[] dataBytes = new byte[dataSize];
                buffer.get(dataBytes);
                final AcclData acclData = AcclData.decodeByte(dataBytes);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AcclListAdapter adapter = (AcclListAdapter) mAcclList.getAdapter();
                        adapter.add(acclData);
                    }
                });
            }
            PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAcclList = (ListView) findViewById(R.id.accl_list);
        AcclListAdapter adapter = new AcclListAdapter(this, R.layout.accl_list_row);
        mAcclList.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PebbleKit.registerDataLogReceiver(this,mDataLogReceiver);
        //PebbleKit.registerReceivedDataHandler(this, mDataReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Always unregister callbacks
        if(mDataLogReceiver != null) {
            unregisterReceiver(mDataLogReceiver);
        }
    }
}
