package org.firstinspires.ftc.robotcontroller.teamcode.Teleop;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.annotation.MainThread;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.Working;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;
import org.firstinspires.ftc.robotcore.internal.AppUtil;

/**
 * Created by sam on 25-Nov-16.
 * Don'delayThread forget hardwareMap!!
 */
@TeleOp(name = "Tele Op Program", group = "Working")
@Working
@MainThread
public class TeleOpProgram extends CustomLOpMode {
    ToneGenerator generator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

    Robot r = new Robot();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (opModeIsActive()) {
                if ((r.BL.getPower() < -0.01 ^ r.BR.getPower() < -0.01) || (r.BL.getPower() < -0.01 && r.BR.getPower() < -0.01)) {
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
    Runnable driveL = new Runnable() {
        @Override
        public void run() {
            while (opModeIsActive()) {
                r.L.setPower(-gamepad1.left_stick_y);
                r.BL.setPower(-gamepad1.left_stick_y);
            }
        }
    };
    Runnable driveR = new Runnable() {
        @Override
        public void run() {
            while (opModeIsActive()) {
                r.R.setPower(-gamepad1.right_stick_y);
                r.BR.setPower(-gamepad1.right_stick_y);
            }
        }
    };
    Runnable wincher = new Runnable() {
        @Override
        public void run() {
            r.Winch.setPower(gamepad2.left_stick_y);
        }
    };
    Thread teleOpL = new Thread(driveL);
    Thread teleOpR = new Thread(driveR);
    Thread teleOpBM = new Thread(new BMer());
    Thread teleOpLifter = new Thread(wincher);

    @Override
    @MainThread
    public void runOpMode() throws Throwable {
        r.initializeRobot(hardwareMap);
        waitForStart();
        t.start();
        teleOpL.start();
        teleOpR.start();
        teleOpBM.start();
        teleOpLifter.start();
        while (opModeIsActive()) {
            // resetEncoders();
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
        teleOpL = null;
        teleOpR = null;
        teleOpBM = null;
        teleOpLifter = null;
    }

    class BMer implements Runnable {
        private boolean done;

        @Override
        public void run() {
            while (opModeIsActive()) {
                if (gamepad2.a) {
                    try {
                        r.resetEncoders();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                    /*try {
                        Thread.sleep(1000);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }*/
                if (gamepad2.b) {
                    generator.startTone(ToneGenerator.TONE_SUP_RADIO_ACK, 1000);
                }
            }
        }
    }
}