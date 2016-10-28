package org.firstinspires.ftc.robotcontroller.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.qualcomm.ftcrobotcontroller.R;

/**
 * Created by sam on 25-Oct-16.
 */
public class FTCJokesActivity extends Activity {
    TextView textView;
    public static final String launchIntent = "org.firstinspires.ftc.robotcontroller.internal.FTCJokesActivity";
    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
