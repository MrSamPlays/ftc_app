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

/**
 * Created by sam on 20-Dec-16.
 * Its working I guess
 */
@Autonomous(name = "Beacon", group = "sorta working")
public class TestBeaconRoutine extends CustomLOpMode {
    final double MOTOR_MOVE_CONSTANT = 0.5;
    final int MOTOR_ENCODER_360_SPIN = 5106;
    Robot r = new Robot();
    Thread t = null;

    @Override
    public void runOpMode() throws Throwable {
        r.initializeRobot(hardwareMap);
        r.beaconFinder.enableLed(false);
        waitForStart();
        t = new Thread(new RunThread()); // comment out for telemetry only.

        r.cdim.setLED(0, true);
        waitForStart();
        Thread.sleep((int) Math.floor(GetAllianceMiddleman.getDelayms()));
        if (t != null) { // Debug code do not remove!!
            t.start();
        }
        KnockBallDown();
        findLine();
        followLine();
        findBeacon();
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
        while (r.L.getCurrentPosition() < 3200 && opModeIsActive()) {
            r.L.setPower(1);
            r.R.setPower(1);
            r.BL.setPower(1);
            r.BR.setPower(1);
            idle();
        }
        if (GetAllianceMiddleman.isRed()) {
            // Turn left
            while (((r.gyro.getHeading() >= 270 || r.gyro.getHeading() < 90) || r.L.getCurrentPosition() >= -MOTOR_ENCODER_360_SPIN/4) && opModeIsActive()) {
                r.L.setPower(-MOTOR_MOVE_CONSTANT);
                r.R.setPower(MOTOR_MOVE_CONSTANT);
                r.BL.setPower(-MOTOR_MOVE_CONSTANT);
                r.BR.setPower(MOTOR_MOVE_CONSTANT);
                idle();
            }
            r.haltMotors();
        } else {
            // Turn Right
            while ((r.gyro.getHeading() <= 90 || r.gyro.getHeading() >= 270) || r.L.getCurrentPosition() <= MOTOR_ENCODER_360_SPIN/4) {
                r.L.setPower(MOTOR_MOVE_CONSTANT);
                r.R.setPower(0);
                r.BL.setPower(MOTOR_MOVE_CONSTANT);
                r.BR.setPower(0);
                idle();
            }
        }
    }
    private void findLine() {
        // TODO finish adding line finder
        boolean linefound = false;
        while (!linefound) {
            // change the first parameter to check at this interval, lower number means increased checking interval
            r.moveStraight(600, MOTOR_MOVE_CONSTANT);
            if (r.colorSensorL.argb() != 0 || r.colorSensorR.argb() != 0) {
                linefound = true;
            }
            if (r.distanceSensor.getLightDetected() > 0.01 && !linefound) {
                // we did not find the line
                r.moveStraight(-1200, MOTOR_MOVE_CONSTANT);
            }
        }
        r.moveStraight(0, 0);
    }
    private void followLine() throws InterruptedException {
        // TODO add line following routine
        r.resetEncoders();
        while (r.distanceSensor.getLightDetected() < 0.1 && opModeIsActive()) {
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
        idle();
    }

    private void findBeacon() throws InterruptedException {
        //TODO add some code to get to the beacon
        boolean isRed = r.beaconFinder.red() > 0;
        boolean isBlue = r.beaconFinder.blue() > 0;
        if (r.isRedAlliance()) {
            if (isRed) {
                // move on to next beacon
            } else {
                // hit it again and then move on
                r.moveStraight(-500, 1);
                sleep(5000);
                r.moveStraight(500, 0.5);
            }
        } else {
            if (isBlue) {
                // move on to the next beacon
            } else {
                // hit it again then move on
                r.moveStraight(-500, 1);
                r.moveStraight(500, 0.5);
            }
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
                telemetry.addData("Encoders", r.L.getCurrentPosition());
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