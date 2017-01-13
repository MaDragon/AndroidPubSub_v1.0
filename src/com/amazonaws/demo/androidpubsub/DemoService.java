package com.amazonaws.demo.androidpubsub;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by zhizhong on 2017-01-09.
 */

public class DemoService extends IntentService {

    public DemoService() {
        super("DemoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String data="111";
    }


}
