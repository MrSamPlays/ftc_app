package org.firstinspires.ftc.robotcontroller.teamcode.testzone;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;

/**
 * Created by sam on 09-Nov-16.
 * Comment out whatever is not being tested
 */
public class AutonomousTesting extends CustomLOpMode {
    DcMotor L,R;
    ToneGenerator gen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    @Override
    public void initializeRobot() {
        // Starting code
        L = hardwareMap.dcMotor.get("Left");
        R = hardwareMap.dcMotor.get("Right");
        L.setDirection(DcMotor.Direction.REVERSE);
        do {
            gen.startTone(ToneGenerator.TONE_SUP_CALL_WAITING);
            L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } while (L.getCurrentPosition() != 0 && R.getCurrentPosition() != 0);
        gen.startTone(ToneGenerator.TONE_SUP_CONFIRM);
    }
    @Override
    public void runOpMode() throws InterruptedException {
        initializeRobot();
        waitForStart();
        wait();
    }
}
