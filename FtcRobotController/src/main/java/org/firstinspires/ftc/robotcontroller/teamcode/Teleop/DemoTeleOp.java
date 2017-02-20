package org.firstinspires.ftc.robotcontroller.teamcode.Teleop;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.annotation.MainThread;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.Working;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Demo Program", group = "Working")
@Working
@MainThread
public class DemoTeleOp extends CustomLOpMode {
    private ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    private boolean rTriggerPressed;
    private boolean lTriggerPressed;
    private boolean isTriggerPressed;
    private final double SPEED_MULTIPLIER = 0.75;
    private Robot robot = new Robot();

    private Gamepad master = gamepad1;

    /**
     * Left stick control runnable
     */
    private Runnable driveL = new Runnable() {
        @Override
        public synchronized void run() {
            while (opModeIsActive()) {
                if (!isTriggerPressed) {
                    robot.L.setPower(-master.left_stick_y * SPEED_MULTIPLIER);
                    robot.BL.setPower(-master.left_stick_y * SPEED_MULTIPLIER);
                }
                if (isTriggerPressed) {
                    if (rTriggerPressed) {
                        robot.moveStraight(master.right_trigger);
                    } else if (lTriggerPressed) {
                        robot.moveStraight(-master.left_trigger);
                    }
                }
            }
        }
    };

    private Runnable driveR = new Runnable() {
        @Override
        public synchronized void run() {
            while (opModeIsActive()) {
                if (!isTriggerPressed) {
                    robot.R.setPower((master.right_bumper ? master.right_stick_y : -master.right_stick_y) * SPEED_MULTIPLIER);
                    robot.BR.setPower((master.right_bumper ? master.right_stick_y : -master.right_stick_y) * SPEED_MULTIPLIER);
                }
            }
        }
    };

    private Runnable masterController = new Runnable() {
        @Override
        public void run() {
            if (gamepad2.left_bumper || gamepad1.atRest()) {
                master = gamepad2;
            } else {
                master = gamepad1;
            }
        }
    };

    private Thread backupNoise = new Thread(new Runnable() {
        @Override
        public void run() {
            while (opModeIsActive()) {
                if ((robot.BL.getPower() < -0.01 ^ robot.BR.getPower() < -0.01) || (robot.BL.getPower() < -0.01 && robot.BR.getPower() < -0.01)) {
                    toneGen.startTone(ToneGenerator.TONE_CDMA_MED_L, 500);
                    try {
                        Thread.sleep(500);
                        toneGen.stopTone();
                        Thread.sleep(500);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    });

    private Thread teleOpL = new Thread(driveL);
    private Thread teleOpR = new Thread(driveR);
    private Thread masterOp = new Thread(masterController);
    private Thread triggered = new Thread(new TriggerStraight());

    @Override
    @MainThread
    public void runOpMode() throws Throwable {
        robot.initializeRobot(hardwareMap);
        waitForStart();

        backupNoise.start();
        teleOpL.start();
        teleOpR.start();
        masterOp.start();
        triggered.start();

        while (opModeIsActive()) {
            // resetEncoders();
            telemetry.addData("Color Sensor (Left)", Integer.toHexString(robot.colorSensorL.argb()));
            telemetry.addData("Color Sensor (Right)", Integer.toHexString(robot.colorSensorR.argb()));
            telemetry.addData("Color Sensor (Front)", Integer.toHexString(robot.beaconFinder.argb()));
            telemetry.addData("Optical Distance Sensor", robot.distanceSensor.getLightDetected());
            telemetry.addData("Range Sensor", robot.range.getDistance(DistanceUnit.CM));
            telemetry.addData("Gyroscope", robot.gyro.getHeading());
            telemetry.addData("Motor Encoder (Left)", robot.L.getCurrentPosition());
            telemetry.addData("Motor Encoder (Right)", robot.R.getCurrentPosition());
            telemetry.addData("Movement Speed", SPEED_MULTIPLIER);
            telemetry.update();
            isTriggerPressed = lTriggerPressed ^ rTriggerPressed;

            idle();
        }

        backupNoise = null;
        teleOpL = null;
        teleOpR = null;
        masterOp = null;
        triggered = null;
    }

    private class TriggerStraight implements Runnable {
        @Override
        public void run() {
            while (opModeIsActive()) {
                if (master.left_trigger != 0 && master.right_trigger == 0) {
                    lTriggerPressed = true;
                    rTriggerPressed = false;
                } else if (master.left_trigger == 0 && master.right_trigger != 0) {
                    rTriggerPressed = true;
                    lTriggerPressed = false;
                } else {
                    lTriggerPressed = rTriggerPressed = false;
                }
            }
        }
    }
}