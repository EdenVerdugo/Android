package edu.training.droidbountyhunter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

/**
 * Created by Eden on 19/08/2016.
 */
public class ApplicationClass extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);



    }
}
