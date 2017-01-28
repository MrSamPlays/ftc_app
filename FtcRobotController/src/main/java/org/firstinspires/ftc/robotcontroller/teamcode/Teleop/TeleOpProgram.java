package org.firstinspires.ftc.robotcontroller.teamcode.Teleop;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.annotation.MainThread;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.Working;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.sound.SoundPlayer;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by sam on 25-Nov-16.
 * Don'delayThread forget hardwareMap!!
 */
@TeleOp(name = "Tele Op Program", group = "Working")
@Working
@MainThread
public class TeleOpProgram extends CustomLOpMode {
    ToneGenerator generator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    boolean xPressed = false;
    boolean yPressed = false;
    boolean rTriggerPressed;
    boolean lTriggerPressed;
    boolean isTriggerPressed;
    double topSpeed;
    Robot r = new Robot();
    MovementSpeed s = MovementSpeed.VERY_FAST;
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
        public synchronized void run() {
            while (opModeIsActive()) {
                if (!isTriggerPressed) {
                    r.L.setPower(Range.clip(-gamepad1.left_stick_y, -topSpeed, topSpeed));
                    r.BL.setPower(Range.clip(-gamepad1.left_stick_y, -topSpeed, topSpeed));
                }
                if (isTriggerPressed) {
                    if (rTriggerPressed) {
                        r.moveStraight(gamepad1.right_trigger);
                    } else if (lTriggerPressed) {
                        r.moveStraight(-gamepad1.left_trigger);
                    }
                }
            }
        }
    };
    Thread throttle = new Thread(new Runnable() {
        @Override
        public void run() {
            while (opModeIsActive()) {
                switch (s) {
                    case SLOW:
                        topSpeed = 0.25;
                        break;
                    case MEDIUM:
                        topSpeed = 0.5;
                        break;
                    case FAST:
                        topSpeed = 0.75;
                        break;
                    case VERY_FAST:
                        topSpeed = 1;
                        break;
                }
                if (gamepad1.left_bumper) {
                    switchState();
                    while (gamepad1.left_bumper) {
                        try {
                            idle();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        public void switchState() {
            if (s == MovementSpeed.SLOW) {
                s = MovementSpeed.MEDIUM;
            } else if (s == MovementSpeed.MEDIUM) {
                s = MovementSpeed.FAST;
            } else if (s == MovementSpeed.FAST) {
                s = MovementSpeed.VERY_FAST;
            } else if (s == MovementSpeed.VERY_FAST) {
                s = MovementSpeed.SLOW;
            }
        }
    });
    Runnable driveR = new Runnable() {
        @Override
        public synchronized void run() {
            while (opModeIsActive()) {
                if (!isTriggerPressed) {
                    r.R.setPower(Range.clip((gamepad1.right_bumper ? gamepad1.right_stick_y : -gamepad1.right_stick_y), -topSpeed, topSpeed));
                    r.BR.setPower(Range.clip((gamepad1.right_bumper ? gamepad1.right_stick_y : -gamepad1.right_stick_y), -topSpeed, topSpeed));
                }
            }
        }
    };
    Runnable wincher = new Runnable() {
        @Override
        public void run() {
            while (opModeIsActive()) {
                r.Winch.setPower(-gamepad2.left_stick_y);
            }
        }
    };
    FlashMode mode = FlashMode.OFF;
    Runnable flasher = new Runnable() {
        @Override
        public void run() {
            while (opModeIsActive()) {
                /*switch (mode) {
                    *//**
                 * Led off
                 *//*
                    case OFF:
                        r.beaconFinder.enableLed(false);
                        break;
                    *//**
                 * LED on
                 *//*
                    case STEADY:
                    default:
                        r.beaconFinder.enableLed(true);
                        break;
                }*/
                switch (mode) {
                    /**
                     * LED Pulse
                     */
                    case FLASHING_1:
                        r.beaconFinder.enableLed(true);
                        try {
                            sleep(500);
                        } catch (Exception e) {

                        }
                        r.beaconFinder.enableLed(false);
                        try {
                            sleep(500);
                            idle();
                        } catch (Exception e) {

                        }
                        break;
                    /**
                     * LED Heartbeat
                     */
                    case FLASHING_2:
                        for (int i = 0; i < 3; i++) {
                            r.beaconFinder.enableLed(true);
                            try {
                                sleep(100);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            r.beaconFinder.enableLed(false);
                            try {
                                sleep(100);
                                idle();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            sleep(600);
                            idle();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    /**
                     * LED Strobe
                     */
                    case FLASHING_3:
                        r.beaconFinder.enableLed(true);
                        try {
                            sleep(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        r.beaconFinder.enableLed(false);
                        try {
                            sleep(50);
                            idle();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }

            }
        }
    };
    Thread teleOpL = new Thread(driveL);
    Thread teleOpR = new Thread(driveR);
    Thread teleOpBM = new Thread(new BMer());
    Thread flash = new Thread(flasher);
    Thread teleOpLifter = new Thread(wincher);
    Thread triggered = new Thread(new TriggerStraight());
    boolean reversed = false;

    @Override
    @MainThread
    public void runOpMode() throws Throwable {
        r.initializeRobot(hardwareMap);
        waitForStart();
        t.start();
        teleOpL.start();
        teleOpR.start();
        teleOpBM.start();
        throttle.start();
        flash.start();
        triggered.start();
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
            telemetry.addData("Range Sensor Distance", r.range.getDistance(DistanceUnit.CM));
            telemetry.addData("Alliance color", (GetAllianceMiddleman.isRed() ? "Red" : "Blue"));
            telemetry.addData("EncoderL", r.L.getCurrentPosition());
            telemetry.addData("EncoderR", r.R.getCurrentPosition());
            telemetry.addData("Flash pattern", mode);
            telemetry.addData("Movement speed", s);
            telemetry.update();
            idle();
            isTriggerPressed = lTriggerPressed ^ rTriggerPressed;
        }
        t = null;
        teleOpL = null;
        teleOpR = null;
        teleOpBM = null;
        teleOpLifter = null;
        triggered = null;
    }

    enum MovementSpeed {
        SLOW,
        MEDIUM,
        FAST,
        VERY_FAST
    }

    enum FlashMode {
        OFF,
        FLASHING_1,
        FLASHING_2,
        FLASHING_3,
        STEADY
    }

    class Reverser implements Runnable {
        @Override
        public void run() {
            while (opModeIsActive()) {
                if (gamepad1.guide) {
                    reversed = !reversed;
                    while (gamepad1.guide) {
                        try {
                            idle();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    class TriggerStraight implements Runnable {
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

    class BMer implements Runnable {
        private boolean done;

        @Override
        public void run() {
            while (opModeIsActive()) {
                    /*try {
                        Thread.sleep(1000);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }*/
                if (gamepad1.a) {
                    generator.startTone(ToneGenerator.TONE_SUP_ERROR);
                }
                if (gamepad2.b) {
                    generator.startTone(ToneGenerator.TONE_SUP_RADIO_ACK, 1000);
                }
                if (gamepad2.a) {
                    SoundPlayer.playSound(hardwareMap.appContext, R.raw.siren);
                }
                if (gamepad2.x) {
                    switch (mode) {
                        case FLASHING_1:
                            mode = FlashMode.FLASHING_2;
                            break;
                        case FLASHING_2:
                            mode = FlashMode.FLASHING_3;
                            break;
                        case FLASHING_3:
                        default:
                            mode = FlashMode.FLASHING_1;
                            break;
                    }
                    while (gamepad2.x) {
                        try {
                            idle();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (gamepad2.y) {
                    switch (mode) {
                        case STEADY:
                            mode = FlashMode.OFF;
                            r.beaconFinder.enableLed(false);
                            break;
                        case OFF:
                        default:
                            mode = FlashMode.STEADY;
                            r.beaconFinder.enableLed(true);
                            break;
                    }
                    while (gamepad2.y) {
                        try {
                            idle();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
                Thread.yield();
            }
        }
    }
}