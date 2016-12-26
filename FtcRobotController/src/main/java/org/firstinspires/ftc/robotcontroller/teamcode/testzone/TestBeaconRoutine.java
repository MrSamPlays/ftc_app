package org.firstinspires.ftc.robotcontroller.teamcode.testzone;

import android.media.ToneGenerator;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;

/**
 * Created by sam on 20-Dec-16.
 */
@Autonomous(name = "Beacon", group = "sorta working")
public class TestBeaconRoutine extends CustomLOpMode {
    final double MOTOR_MOVE_CONSTANT = 0.5;
    Robot r = new Robot();
    Thread t = null;

    @Override
    public void runOpMode() throws Throwable {
        r.initializeRobot(hardwareMap);
        r.beaconFinder.enableLed(false);
        waitForStart();
        t = new Thread(new RunThread()); // comment out for telemetry only.
        r.cdim.setLED(0, true);
        if (t != null) { // Debug code do not remove!!
            t.start();
        }
        waitForStart();
        Thread.sleep((int) Math.floor(GetAllianceMiddleman.getDelayms()));
        KnockBallDown();
        findBeacon();
        followLine();
        idle();

    }

    @Override
    public void stop() {
        if (t != null) {
            t.interrupt();
            t = null;
        }
        super.stop();
    }

    private void followLine() {
        // TODO add line following routine
        while (r.distanceSensor.getLightDetected() < 0.1 && opModeIsActive()) {
            if (r.colorSensorL.argb() > 0x3030304 && r.colorSensorR.argb() == 0x00) {
                if (r.gyro.getHeading() > 10 && r.gyro.getHeading() < 90) {
                    r.L.setPower(0);
                    r.R.setPower(MOTOR_MOVE_CONSTANT);
                    r.BL.setPower(0);
                    r.BR.setPower(MOTOR_MOVE_CONSTANT);
                    while (r.gyro.getHeading() > 0 && r.gyro.getHeading() < 90) {
                        telemetry.update();
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
    }

    private boolean beaconIsRightColour() {
        return true;
    }

    private void KnockBallDown() {
        r.resetEncoders();
        while (r.L.getCurrentPosition() < 4050) {
            r.L.setPower(1);
            r.R.setPower(1);
            r.BL.setPower(1);
            r.BR.setPower(1);
        }
        if (GetAllianceMiddleman.isRed()) {
            // Turn left
            while (r.gyro.getHeading() >= 270 && r.gyro.getHeading() < 90) {
                r.L.setPower(0);
                r.R.setPower(MOTOR_MOVE_CONSTANT);
                r.BL.setPower(0);
                r.BR.setPower(MOTOR_MOVE_CONSTANT);
            }
        } else {
            // Turn Right
            r.L.setPower(MOTOR_MOVE_CONSTANT);
            r.L.setPower(0);
            r.BL.setPower(MOTOR_MOVE_CONSTANT);
            r.BR.setPower(0);
        }
    }

    private void findBeacon() {
        //TODO add some code to get to the beacon
        if (r.isRedAlliance()) {

        }
    }

    class RunThread implements Runnable {
        @Override
        public void run() {
            r.generator.startTone(ToneGenerator.TONE_DTMF_4, 1000);
            while (opModeIsActive()) {
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
                telemetry.update();
                try {
                    idle();
                } catch (Exception r) {
                    // YOOOOOOOOOOOOU'RE OUT!!!!!!!!!!!
                }
            }
        }
    }
}