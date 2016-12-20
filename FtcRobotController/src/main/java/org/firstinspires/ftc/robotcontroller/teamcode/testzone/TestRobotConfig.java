package org.firstinspires.ftc.robotcontroller.teamcode.testzone;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;
/**
 * Important!!! It is important that you import static
 */


/**
 * Created by sam on 25-Nov-16.
 * Don't forget hardwareMap!!
 */
@TeleOp(name = "Test Robot configs", group = "Working")
public class TestRobotConfig extends CustomLOpMode {
    ToneGenerator generator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    Robot r = new Robot();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (opModeIsActive()) {
                if (r.BL.getPower() < -0.01 && r.BR.getPower() < -0.01) {
                    generator.startTone(ToneGenerator.TONE_CDMA_MED_L, 500);
                    try {
                        Thread.sleep(500);
                        generator.stopTone();
                        Thread.sleep(500);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    };
    Thread t = new Thread(runnable);

    public void runOpMode() throws Throwable {
        r.initializeRobot(hardwareMap);
        waitForStart();
        t.start();
        while (opModeIsActive()) {
            // resetEncoders();
            r.L.setPower(-gamepad1.left_stick_y);
            r.BL.setPower(-gamepad1.left_stick_y);
            r.R.setPower(-gamepad1.right_stick_y);
            r.BR.setPower(-gamepad1.right_stick_y);
            idle();
        }
        t = null;
    }
}