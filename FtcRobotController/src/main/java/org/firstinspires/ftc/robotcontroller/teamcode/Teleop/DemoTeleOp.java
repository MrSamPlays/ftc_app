package org.firstinspires.ftc.robotcontroller.teamcode.Teleop;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.annotation.MainThread;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.Working;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.sound.SoundPlayer;
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

    /**
     * Left stick control runnable
     */
    private Runnable driveL = new Runnable() {
        @Override
        public synchronized void run() {
            while (opModeIsActive()) {
                if (!isTriggerPressed) {
                    robot.L.setPower(-gamepad1.left_stick_y * SPEED_MULTIPLIER);
                    robot.BL.setPower(-gamepad1.left_stick_y * SPEED_MULTIPLIER);
                }
                if (isTriggerPressed) {
                    if (rTriggerPressed) {
                        robot.moveStraight(gamepad1.right_trigger);
                    } else if (lTriggerPressed) {
                        robot.moveStraight(-gamepad1.left_trigger);
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
                    robot.R.setPower((gamepad1.right_bumper ? gamepad1.right_stick_y : -gamepad1.right_stick_y) * SPEED_MULTIPLIER);
                    robot.BR.setPower((gamepad1.right_bumper ? gamepad1.right_stick_y : -gamepad1.right_stick_y) * SPEED_MULTIPLIER);
                }
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
    private Thread teleOpBM = new Thread(new FunStuff());
    private Thread triggered = new Thread(new TriggerStraight());

    @Override
    @MainThread
    public void runOpMode() throws Throwable {
        robot.initializeRobot(hardwareMap);
        waitForStart();
        backupNoise.start();
        teleOpL.start();
        teleOpR.start();
        teleOpBM.start();
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
        teleOpBM = null;
        triggered = null;
    }

    private class TriggerStraight implements Runnable {
        @Override
        public void run() {
            while (opModeIsActive()) {
                if (gamepad1.left_trigger != 0 && gamepad1.right_trigger == 0) {
                    lTriggerPressed = true;
                    rTriggerPressed = false;
                } else if (gamepad1.left_trigger == 0 && gamepad1.right_trigger != 0) {
                    rTriggerPressed = true;
                    lTriggerPressed = false;
                } else {
                    lTriggerPressed = rTriggerPressed = false;
                }
            }
        }
    }

    /**
     * This has all the bells and whistles- sound effects are controlled from here.
     */
    private class FunStuff implements Runnable {
        @Override
        public void run() {
            while (opModeIsActive()) {
                if (gamepad1.a) {
                    toneGen.startTone(ToneGenerator.TONE_SUP_ERROR);
                }
                if (gamepad2.b) {
                    toneGen.startTone(ToneGenerator.TONE_SUP_RADIO_ACK, 1000);
                }
                if (gamepad2.a) {
                    SoundPlayer.playSound(hardwareMap.appContext, R.raw.siren);
                }
                Thread.yield();
            }
        }
    }
}