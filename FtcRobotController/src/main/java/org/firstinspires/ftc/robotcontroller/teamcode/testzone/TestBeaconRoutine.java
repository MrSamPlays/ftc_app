package org.firstinspires.ftc.robotcontroller.teamcode.testzone;

import android.media.ToneGenerator;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Locale;

/*
 * Created by sam on 20-Dec-16.
 * Its working I guess
 */

/**
 * This class will navigate to beacons, push them, and move to the corner vortex.
 */
@Autonomous(name = "Beacon", group = "sorta working")
public class TestBeaconRoutine extends CustomLOpMode {
    // Default power level of the motors (from 0 - 1).
    final double MOTOR_MOVE_CONSTANT = 0.5;

    // The second variable is the number of motor encoder clicks in a full rotation.
    final int MOTOR_ENCODER_360_SPIN = 5106;

    // This is our access to Robot, a class that holds information on the robot and enables us to
    // quickly get to sensors and motors.
    Robot robot = new Robot();

    // This is the thread that we use to delay the program.
    Thread delayThread = null;

    @Override
    public void runOpMode() throws Throwable {
        // Set all motors and sensors so that we can get to them.
        robot.initializeRobot(hardwareMap);

        // Turn on and off some lights
        robot.beaconFinder.enableLed(false);
        robot.cdim.setLED(0, true);

        // Set the thread.
        delayThread = new Thread(new RunThread()); // comment out for telemetry only.

        // Wait until go.
        waitForStart();

        // Do the actual delay.
        Thread.sleep((int) Math.floor(GetAllianceMiddleman.getDelayms()));
        if (delayThread != null) { // Debug code do not remove!!
            delayThread.start();
        }

        // Steps in the routine.
        knockBallDown();
        findLine();
        followLine();
        findBeacon();

        // Stay alive until the end of the autonomous period.
        while (opModeIsActive()) {
            idle();
        }
    }

    @Override
    public void stop() {
        // Just stop the thread if it's running, then continue with the stop.
        if (delayThread != null) {
            delayThread.interrupt();
            delayThread = null;
        }
        super.stop();
    }

    /**
     * This will knock the ball off of the center vortex platform.
     *
     * @throws InterruptedException
     */
    private void knockBallDown() throws InterruptedException {
        // Set the motor rotations to 0.
        robot.resetEncoders();

        // Move at full speed for a distance
        while (robot.L.getCurrentPosition() < 3200 && opModeIsActive()) {
            robot.L.setPower(1);
            robot.R.setPower(1);
            robot.BL.setPower(1);
            robot.BR.setPower(1);
            idle();
        }

        // If we are on the Red Alliance...
        if (GetAllianceMiddleman.isRed()) {

            // Turn left, to face the beacons.
            while (((robot.gyro.getHeading() >= 270 || robot.gyro.getHeading() < 90) || robot.L.getCurrentPosition() >= -MOTOR_ENCODER_360_SPIN / 4) && opModeIsActive()) {
                robot.L.setPower(-MOTOR_MOVE_CONSTANT);
                robot.R.setPower(MOTOR_MOVE_CONSTANT);
                robot.BL.setPower(-MOTOR_MOVE_CONSTANT);
                robot.BR.setPower(MOTOR_MOVE_CONSTANT);
                idle();
            }

            // If we are on the Blue Alliance...
        } else {

            // Turn right, to face the beacons.
            while ((robot.gyro.getHeading() <= 90 || robot.gyro.getHeading() >= 270) || robot.L.getCurrentPosition() <= MOTOR_ENCODER_360_SPIN / 4) {
                robot.L.setPower(MOTOR_MOVE_CONSTANT);
                robot.R.setPower(0);
                robot.BL.setPower(MOTOR_MOVE_CONSTANT);
                robot.BR.setPower(0);
                idle();
            }
        }

        // Stop facing the right way.
        robot.haltMotors();
    }

    /**
     * This method will drive forward until the line has been found.
     */
    private void findLine() {
        // TODO finish adding line finder

        // Store if we have found the beacon yet.
        boolean lineFound = false;

        // Continue until the line is found.
        while (!lineFound) {

            // Move forward for 600 milliseconds
            // change the first parameter to check at this interval, lower number means increased checking interval
            robot.moveStraight(600, MOTOR_MOVE_CONSTANT);
            if (robot.colorSensorL.argb() != 0 || robot.colorSensorR.argb() != 0) {
                lineFound = true;
            }
            if (robot.distanceSensor.getLightDetected() > 0.01 && !lineFound) {
                // we did not find the line
                robot.moveStraight(-1200, MOTOR_MOVE_CONSTANT);
            }
        }
        robot.moveStraight(0, 0);
    }
    private void followLine() throws InterruptedException {
        // TODO add line following routine
        robot.resetEncoders();
        while (robot.distanceSensor.getLightDetected() < 0.1 && opModeIsActive()) {
            if (robot.colorSensorL.argb() > 0x3030304 && robot.colorSensorR.argb() == 0x00) {
                if (robot.gyro.getHeading() > 10 && robot.gyro.getHeading() < 90) {
                    robot.L.setPower(0);
                    robot.R.setPower(MOTOR_MOVE_CONSTANT);
                    robot.BL.setPower(0);
                    robot.BR.setPower(MOTOR_MOVE_CONSTANT);
                    while (robot.gyro.getHeading() > 0 && robot.gyro.getHeading() < 90) {
                        telemetry.update();
                        idle();
                    }
                    continue;
                }
                robot.L.setPower(MOTOR_MOVE_CONSTANT);
                robot.R.setPower(0);
                robot.BL.setPower(MOTOR_MOVE_CONSTANT);
                robot.BR.setPower(0);
            } else if (robot.colorSensorL.argb() == 0x00 && robot.colorSensorR.argb() > 0x3030304) {
                if (robot.gyro.getHeading() < 350 && robot.gyro.getHeading() > 270) {
                    robot.L.setPower(MOTOR_MOVE_CONSTANT);
                    robot.R.setPower(0);
                    robot.BL.setPower(MOTOR_MOVE_CONSTANT);
                    robot.BR.setPower(0);
                    while (robot.gyro.getHeading() > 270 && robot.gyro.getHeading() < 360) {
                        telemetry.update();
                        idle();
                    }
                    continue;
                }
                robot.L.setPower(0);
                robot.R.setPower(MOTOR_MOVE_CONSTANT);
                robot.BL.setPower(0);
                robot.BR.setPower(MOTOR_MOVE_CONSTANT);
            } else { //if (r.colorSensorL.argb() >= 0x3030304 && r.colorSensorR.argb() >= 0x3030304) {
                robot.L.setPower(MOTOR_MOVE_CONSTANT / 2);
                robot.R.setPower(MOTOR_MOVE_CONSTANT / 2);
                robot.BL.setPower(MOTOR_MOVE_CONSTANT / 2);
                robot.BR.setPower(MOTOR_MOVE_CONSTANT / 2);
            }
        }
        robot.generator.startTone(ToneGenerator.TONE_CDMA_HIGH_SS_2);
        robot.L.setPower(0);
        robot.R.setPower(0);
        robot.BL.setPower(0);
        robot.BR.setPower(0);
        idle();
    }

    private void findBeacon() throws InterruptedException {
        //TODO add some code to get to the beacon
        boolean isRed = robot.beaconFinder.red() > 0;
        boolean isBlue = robot.beaconFinder.blue() > 0;
        if (robot.isRedAlliance()) {
            if (isRed) {
                // move on to next beacon
            } else {
                // hit it again and then move on
                robot.moveStraight(-500, 1);
                sleep(5000);
                robot.moveStraight(500, 0.5);
            }
        } else {
            if (isBlue) {
                // move on to the next beacon
            } else {
                // hit it again then move on
                robot.moveStraight(-500, 1);
                robot.moveStraight(500, 0.5);
            }
        }
    }

    class RunThread implements Runnable {
        File f = null;
        Writer out = null;

        @Override
        public void run() {
            robot.generator.startTone(ToneGenerator.TONE_DTMF_4, 1000);
            try {
                f = new File(Environment.getExternalStorageDirectory().getPath() + "/logs.log");
                if (!f.exists()) {
                    f.createNewFile();
                }
                out = new FileWriter(f);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            while (opModeIsActive()) {
                try {
                    out.write(String.format(Locale.UK, "Motor Encoder Position: %d\n", robot.L.getCurrentPosition()));
                } catch (Exception e) {
                    stop();
                }
                telemetry.addData("Left Color", Integer.toHexString(robot.colorSensorL.argb()));
                telemetry.addData("Right Color", Integer.toHexString(robot.colorSensorR.argb()));
                telemetry.addData("Beacon Color", Integer.toHexString(robot.beaconFinder.argb()));
                telemetry.addData("Left Color sensor I2c address", robot.colorSensorL.getI2cAddress().get8Bit());
                telemetry.addData("Right Color sensor I2c address", robot.colorSensorR.getI2cAddress().get8Bit());
                telemetry.addData("Beacon finder color sensor I2c address", robot.beaconFinder.getI2cAddress().get8Bit());
                telemetry.addData("Optical distance reading", robot.distanceSensor.getLightDetected());
                telemetry.addData("Optical distance reading raw", robot.distanceSensor.getRawLightDetected());
                telemetry.addData("Gyro", robot.gyro.getHeading());
                telemetry.addData("Alliance color", (GetAllianceMiddleman.isRed() ? "Red" : "Blue"));
                telemetry.addData("Encoders", robot.L.getCurrentPosition());
                telemetry.update();
                if (isStopRequested()) {
                    break;
                }
                try {
                    idle();
                } catch (Exception r) {
                    // YOOOOOOOOOOOOU'RE OUT!!!!!!!!!!!
                }
            }
        }
    }
}