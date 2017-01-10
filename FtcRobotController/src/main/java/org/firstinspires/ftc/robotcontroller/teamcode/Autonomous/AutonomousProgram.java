package org.firstinspires.ftc.robotcontroller.teamcode.Autonomous;

import android.media.ToneGenerator;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * Created by sam on 20-Dec-16.
 * Its working I guess
 */
@Autonomous(name = "Autonomous", group = "not guaranteed to work")
public class AutonomousProgram extends CustomLOpMode {
    final double MOTOR_MOVE_CONSTANT = 0.35;
    final int MOTOR_ENCODER_360_SPIN = 12527;
    Robot r = new Robot();
    Thread t = null;
    Thread beep = new Thread(new BeepBoop());
    Thread boop = new Thread(new BoopBeep());
    ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    @Override
    public void runOpMode() throws Throwable {
        r.initializeRobot(hardwareMap);
        r.beaconFinder.enableLed(false);
        t = new Thread(new RunThread()); // comment out for telemetry only.
        if (t != null) { // Debug code do not remove!!
            t.start();
        }

        r.cdim.setLED(0, true);

        waitForStart();

        time.reset();

        Thread.sleep((int) Math.floor(GetAllianceMiddleman.getDelayms()));
        KnockBallDown();
        findLine();
        // followLine();
        findBeacon();
        prepareSecondBeacon();
        goToSecondBeacon();
        squareUpWithLine();
        while (opModeIsActive()) {
            idle();
        }
    }

    @Override
    public void stop() {
        if (t != null) {
            t.interrupt();
            t = null;
        }
        super.stop();
    }

    private void KnockBallDown() throws InterruptedException {
        r.resetEncoders();
        while (r.L.getCurrentPosition() < 3500 && opModeIsActive()) {
            r.L.setPower(1);
            r.R.setPower(1);
            r.BL.setPower(1);
            r.BR.setPower(1);
            idle();
        }
        r.haltMotors();
        sleep(100);
        r.resetEncoders();
        sleep(200);
        r.generator.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY_ONE_SHOT, 200);
        if (GetAllianceMiddleman.isRed()) {
            // Turn left
            r.R.setTargetPosition((MOTOR_ENCODER_360_SPIN / 4));
            while (r.gyroIsWorking ? (r.gyro.getHeading() >= 270 || r.gyro.getHeading() <= 90) : r.R.getCurrentPosition() <= (MOTOR_ENCODER_360_SPIN / 4)) {
                r.L.setPower(0);
                r.R.setPower(MOTOR_MOVE_CONSTANT);
                r.BL.setPower(0);
                r.BR.setPower(MOTOR_MOVE_CONSTANT);
                idle();
            }
            r.haltMotors();
        } else {
            // Turn Right
            r.L.setTargetPosition((MOTOR_ENCODER_360_SPIN / 4));
            while ((r.gyroIsWorking ? (r.gyro.getHeading() <= 90 || r.gyro.getHeading() >= 270) : (r.L.getCurrentPosition() <= (MOTOR_ENCODER_360_SPIN / 4)))) {
                r.L.setPower(MOTOR_MOVE_CONSTANT);
                r.R.setPower(0);
                r.BL.setPower(MOTOR_MOVE_CONSTANT);
                r.BR.setPower(0);
                idle();
            }
            r.haltMotors();
        }
        sleep(100);
        r.resetEncoders();
        while (r.L.getCurrentPosition() > -1100) {
            r.L.setPower(-1);
            r.R.setPower(-1);
            r.BL.setPower(-1);
            r.BR.setPower(-1);
            idle();
        }
        RobotLog.i("Hello robot");
        r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_INTERGROUP);
        r.haltMotors();
    }

    private void findLine() throws InterruptedException {
        // TODO finish adding line finder
        boolean linefound = false;
        // How many times did we have to move backward from the line
        int count = 0;

        while (!linefound && count < 2) {
            // change the first parameter to check at this interval, lower number means increased checking interval thus increasing robot stutter movement
            r.moveStraight(MOTOR_MOVE_CONSTANT);
            if (r.colorSensorL.argb() != 0 || r.colorSensorR.argb() != 0) {
                linefound = true;
            }
            if (r.distanceSensor.getLightDetected() > 0.01 && !linefound) {
                // we did not find the line back up and do something else
                r.moveBackward(1200, MOTOR_MOVE_CONSTANT);
                count++;
            }
            idle();
        }
        r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PAT5);
        r.haltMotors();
    }
    /*
    private void followLine() throws InterruptedException {
        r.resetEncoders();
        while (r.distanceSensor.getLightDetected() < 0.05 && opModeIsActive()) {
            if (r.colorSensorL.argb() > 0x3030304 && r.colorSensorR.argb() == 0x00) {
                if (r.gyro.getHeading() > 10 && r.gyro.getHeading() < 90) {
                    r.L.setPower(0);
                    r.R.setPower(MOTOR_MOVE_CONSTANT);
                    r.BL.setPower(0);
                    r.BR.setPower(MOTOR_MOVE_CONSTANT);
                    while (r.gyro.getHeading() > 0 && r.gyro.getHeading() < 90) {
                        telemetry.update();
                        idle();
                    }
                    continue;
                }
                r.L.setPower(MOTOR_MOVE_CONSTANT);
                r.R.setPower(0);
                r.BL.setPower(MOTOR_MOVE_CONSTANT);
                r.BR.setPower(0);
            } else if (r.colorSensorL.argb() == 0x00 && r.colorSensorR.argb() > 0x3030304) {
                if (r.gyro.getHeading() < 350 && r.gyro.getHeading() > 270) {
                    r.L.setPower(MOTOR_MOVE_CONSTANT);
                    r.R.setPower(0);
                    r.BL.setPower(MOTOR_MOVE_CONSTANT);
                    r.BR.setPower(0);
                    while (r.gyro.getHeading() > 270 && r.gyro.getHeading() < 360) {
                        telemetry.update();
                        idle();
                    }
                    continue;
                }
                r.L.setPower(0);
                r.R.setPower(MOTOR_MOVE_CONSTANT);
                r.BL.setPower(0);
                r.BR.setPower(MOTOR_MOVE_CONSTANT);
            } else { //if (r.colorSensorL.argb() >= 0x3030304 && r.colorSensorR.argb() >= 0x3030304) {
                r.L.setPower(MOTOR_MOVE_CONSTANT / 2);
                r.R.setPower(MOTOR_MOVE_CONSTANT / 2);
                r.BL.setPower(MOTOR_MOVE_CONSTANT / 2);
                r.BR.setPower(MOTOR_MOVE_CONSTANT / 2);
            }
        }
        r.generator.startTone(ToneGenerator.TONE_CDMA_HIGH_SS_2);
        r.L.setPower(0);
        r.R.setPower(0);
        r.BL.setPower(0);
        r.BR.setPower(0);
        sleep(400);
        r.generator.startTone(ToneGenerator.TONE_CDMA_HIGH_SS_2);
        idle();
    }*/

    private void findBeacon() throws InterruptedException {
        //TODO add some code to get to the beacon
        while (r.distanceSensor.getLightDetected() < 0.02) {
            // we need to move toward the beacon
            r.L.setPower(MOTOR_MOVE_CONSTANT / 2);
            r.R.setPower(MOTOR_MOVE_CONSTANT / 2);
            r.BL.setPower(MOTOR_MOVE_CONSTANT / 2);
            r.BR.setPower(MOTOR_MOVE_CONSTANT / 2);
        }
        r.haltMotors();
        boolean isRed = r.beaconFinder.red() > 0;
        boolean isBlue = r.beaconFinder.blue() > 0;
        if (r.isRedAlliance()) {
            if (isRed) {
                // move on to next beacon
            } else {
                // hit it again and then move on
                r.moveBackward(250, 1);
                r.haltMotors();
                r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PING_RING);
                sleep(5000);
                r.moveForward(250, 0.5);
            }
        } else {
            if (isBlue) {
                // move on to the next beacon
            } else {
                // hit it again then move on
                r.moveBackward(250, 1);
                r.haltMotors();
                r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PING_RING);
                sleep(5000);
                r.moveForward(250, 0.5);
            }
        }
    }

    private void prepareSecondBeacon() throws InterruptedException {
        boolean gyroIsWorkingBefore = r.gyroIsWorking;
        r.resetEncoders();
        r.moveBackward(800, 1);
        // r.gyroIsWorking = false;
        if (r.isRedAlliance()) {
            while (r.gyroIsWorking ? (r.gyro.getHeading() > 180) : r.L.getCurrentPosition() < MOTOR_ENCODER_360_SPIN / 4) {
                r.L.setPower(MOTOR_MOVE_CONSTANT);
                r.R.setPower(0);
                r.BL.setPower(MOTOR_MOVE_CONSTANT);
                r.BR.setPower(0);
                idle();
            }
        } else {
            while (r.gyroIsWorking ? (r.gyro.getHeading() > 0 || r.gyro.getHeading() < 180) : r.R.getCurrentPosition() < MOTOR_ENCODER_360_SPIN / 4) {
                r.L.setPower(0);
                r.R.setPower(MOTOR_MOVE_CONSTANT);
                r.BL.setPower(0);
                r.BR.setPower(MOTOR_MOVE_CONSTANT);
                idle();
            }
        }
        r.haltMotors();
        // r.gyroIsWorking = gyroIsWorkingBefore;
    }
    private void goToSecondBeacon() {
        boolean linefound = false;
        while (!linefound) {
            if (r.colorSensorL.argb() != 0 || r.colorSensorR.argb() != 0) {
                linefound = true;
            }
            r.moveStraight(1);
        }
        r.haltMotors();
    }
    private void squareUpWithLine() {
        if (GetAllianceMiddleman.isRed()) {
            // turn left a little bit
            while (r.R.getCurrentPosition() < 550) {
                r.L.setPower(-MOTOR_MOVE_CONSTANT/2);
                r.R.setPower(MOTOR_MOVE_CONSTANT);
                r.BL.setPower(-MOTOR_MOVE_CONSTANT/2);
                r.BR.setPower(MOTOR_MOVE_CONSTANT);
            }

        } else {
            // Turn right a little bit
            while (r.L.getCurrentPosition() < 550) {
                r.L.setPower(MOTOR_MOVE_CONSTANT);
                r.R.setPower(-MOTOR_MOVE_CONSTANT/2);
                r.BL.setPower(MOTOR_MOVE_CONSTANT);
                r.BR.setPower(-MOTOR_MOVE_CONSTANT/2);
            }
        }
        r.haltMotors();
        boolean linefound = false;
        if (GetAllianceMiddleman.isRed()) {
            // turn left until it sees the white line again
            while (!linefound) {
                r.L.setPower(-MOTOR_MOVE_CONSTANT/2);
                r.R.setPower(MOTOR_MOVE_CONSTANT);
                r.BL.setPower(-MOTOR_MOVE_CONSTANT/2);
                r.BR.setPower(MOTOR_MOVE_CONSTANT);
                linefound = r.colorSensorL.argb() > 0 || r.colorSensorR.argb() > 0;
            }
        } else {
            // Turn right until it sees the white line again
            while (!linefound) {
                r.L.setPower(MOTOR_MOVE_CONSTANT);
                r.R.setPower(-MOTOR_MOVE_CONSTANT/2);
                r.BL.setPower(MOTOR_MOVE_CONSTANT);
                r.BR.setPower(-MOTOR_MOVE_CONSTANT/2);
                linefound = r.colorSensorL.argb() > 0 || r.colorSensorR.argb() > 0;
            }
        }

    }

    class BoopBeep implements Runnable {
        @Override
        public void run() {
            r.generator.startTone(ToneGenerator.TONE_CDMA_ANSWER, 1000);
        }
    }

    class BeepBoop implements Runnable {
        @Override
        public void run() {
            r.generator.startTone(ToneGenerator.TONE_PROP_BEEP);
        }
    }

    class RunThread implements Runnable {
        File f = null;
        Writer out = null;

        @Override
        public void run() {
            r.generator.startTone(ToneGenerator.TONE_DTMF_4, 1000);
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
                    out.write(String.format(Locale.UK, "Motor Encoder Position: %d\n", r.L.getCurrentPosition()));
                } catch (Exception e) {
                    stop();
                }
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
                telemetry.addData("Encoder L", r.L.getCurrentPosition());
                telemetry.addData("Encoder R", r.R.getCurrentPosition());
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