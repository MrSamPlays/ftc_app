package org.firstinspires.ftc.robotcontroller.internal;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.qualcomm.ftcrobotcontroller.R;

import org.firstinspires.ftc.robotcore.internal.AppUtil;

public class OptionsActivity extends Activity {
    private RadioGroup allianceRadioGroup;
    private RadioButton allianceChoice;
    private String alliance;
    private Button submit;

    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_options);
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
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get selected radio button from radioGroup
            int selectedId = allianceRadioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            allianceChoice = (RadioButton) findViewById(selectedId);
            alliance = allianceChoice.getText().toString();

            if (alliance.equalsIgnoreCase("red")) {
                GetAllianceMiddleman.setAlliance(true);
            } else {
                GetAllianceMiddleman.setAlliance(false);
            }
            AppUtil.getInstance().showToast("Successfully set Alliance");
        }
    };
}
