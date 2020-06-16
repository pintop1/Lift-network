package d.pintoptech.liftnetwork.utils;

import android.app.Application;
import android.graphics.Color;

import d.pintoptech.liftnetwork.R;
import io.customerly.Customerly;

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Customerly.configure(this, getResources().getString(R.string.customerly_app_id), getResources().getColor(R.color.colorPrimaryDark));
    }
}