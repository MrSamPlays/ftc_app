package org.firstinspires.ftc.robotcontroller.internal;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.qualcomm.ftcrobotcontroller.R;

import org.firstinspires.ftc.robotcore.internal.AppUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class OptionsActivity extends Activity {
    private Properties prop = new Properties();
    private OutputStream out = null;
    private InputStream in = null;
    private RadioGroup allianceRadioGroup;
    private RadioButton allianceChoice;
    private String alliance;
    private Button submit;
    private EditText delay;
    private double delayInterval;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get selected radio button from radioGroup

            int selectedId = allianceRadioGroup.getCheckedRadioButtonId();
            delayInterval = Double.parseDouble(delay.getText().toString());
            // find the radiobutton by returned id
            allianceChoice = (RadioButton) findViewById(selectedId);

            alliance = allianceChoice.getText().toString();
            GetAllianceMiddleman.setDelay(delayInterval);
            if (alliance.equalsIgnoreCase("red")) {
                GetAllianceMiddleman.setAlliance(true);
                AppUtil.getInstance().showToast("Successfully set Alliance to red");
            } else {
                GetAllianceMiddleman.setAlliance(false);
                AppUtil.getInstance().showToast("Successfully set Alliance to blue");
            }
            try {
                out = new FileOutputStream("config.properties");
                prop.setProperty("AllianceChoice", alliance);
                prop.setProperty("delay", Double.toString(delayInterval));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_options);
        try {
            in = new FileInputStream("config.properties");
            prop.load(in);
            alliance = prop.getProperty("AllianceChoice");
            delay.setText(prop.getProperty("delay"));
            delayInterval = Double.parseDouble(prop.getProperty("delay"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
        }
        addListenerOnButton();
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

    public void addListenerOnButton() {
        allianceRadioGroup = (RadioGroup) findViewById(R.id.allianceChoice);
        delay = (EditText) findViewById(R.id.delay);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(onClickListener);
    }
}