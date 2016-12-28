package org.firstinspires.ftc.robotcontroller.teamcode.testzone;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;

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
                if ((!(r.BL.getPower() < -0.01) && r.BR.getPower() < -0.01) || (r.BL.getPower() < -0.01 && !(r.BR.getPower() < -0.01))) {
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
            telemetry.addData("Left Color", Integer.toHexString(r.colorSensorL.argb()));
            telemetry.addData("Right Color", Integer.toHexString(r.colorSensorR.argb()));
            telemetry.addData("Beacon Color", Integer.toHexString(r.beaconFinder.argb()));
            telemetry.addData("Left Color sensor I2c address", r.colorSensorL.getI2cAddress().get8Bit());
            telemetry.addData("Right Color sensor I2c address", r.colorSensorR.getI2cAddress().get8Bit());
            telemetry.addData("Beacon finder color sensor I2c address", r.beaconFinder.getI2cAddress().get8Bit());
            telemetry.addData("Optical distance reading", r.distanceSensor.getLightDetected());
            telemetry.addData("Optical distance reading raw", r.distanceSensor.getRawLightDetected());
            telemetry.addData("Gyro", r.gyro.getHeading());
            telemetry.addData("Alliance color", (GetAllianceMiddleman.isRed() ? "Red" : "Blue"));

            telemetry.addData("EncoderL", r.L.getCurrentPosition());
            telemetry.addData("EncoderR", r.R.getCurrentPosition());
            telemetry.update();
            idle();
        }
        t = null;
    }
}