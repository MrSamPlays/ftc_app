package org.firstinspires.ftc.robotcontroller.teamcode.Autonomous;

import android.media.ToneGenerator;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;

/**
 * Created by sam on 06-Jan-17.
 * It works if it works
 */
@Autonomous(name = "Autonomous v2", group = "Z not working")
public class AutonomousBeacon extends CustomLOpMode {
    final double MOTOR_MOVE_CONSTANT = 0.35;
    final int MOTOR_ENCODER_360_SPIN = 5106;
    boolean isRedAlliance;
    int relposL = 0;
    int relposR = 0;
    Robot r = null;

    @Override
    public void initializeRobot() throws Throwable {
        r = new Robot(hardwareMap);
        isRedAlliance = GetAllianceMiddleman.isRed();
    }

    @Override
    public void runOpMode() throws Throwable {
        initializeRobot();
        waitForStart();
        delayProgram();
        findLine();
    }

    private void findLine() throws InterruptedException {
        if (isRedAlliance) {
            // Turn Left
            while ((r.gyroIsWorking ? r.gyro.getHeading() > 330 || r.gyro.getHeading() < 90 : r.R.getCurrentPosition() == MOTOR_ENCODER_360_SPIN / 4)) {
                r.L.setPower(0);
                r.R.setPower(MOTOR_MOVE_CONSTANT);
                r.BL.setPower(0);
                r.BR.setPower(MOTOR_MOVE_CONSTANT);
                idle();
            }
        } else {
            // Turn Right
            while ((r.gyroIsWorking ? r.gyro.getHeading() < 30 || r.gyro.getHeading() > 270 : r.L.getCurrentPosition() == MOTOR_ENCODER_360_SPIN / 4)) {
                r.L.setPower(MOTOR_MOVE_CONSTANT);
                r.R.setPower(0);
                r.BL.setPower(MOTOR_MOVE_CONSTANT);
                r.BR.setPower(0);
                idle();
            }
        }
        while (r.colorSensorL.argb() == 0 && r.colorSensorR.argb() == 0) {
            r.moveStraight(1);
            idle();
        }
        r.haltMotors();
        if (isRedAlliance) {
            // Turn Left to find line
            while (r.colorSensorR.argb() == 0 || r.colorSensorL.argb() == 0) {
                r.L.setPower(0);
                r.R.setPower(MOTOR_MOVE_CONSTANT);
                r.BL.setPower(0);
                r.BR.setPower(MOTOR_MOVE_CONSTANT);
            }
        } else {
            // Turn Right to find line
            while (r.colorSensorL.argb() == 0 || r.colorSensorR.argb() == 0) {
                r.L.setPower(MOTOR_MOVE_CONSTANT);
                r.R.setPower(0);
                r.BL.setPower(MOTOR_MOVE_CONSTANT);
                r.BR.setPower(0);
            }
        }
    }

    private void findBeacon() throws InterruptedException {
        //TODO add some code to get to the beacon
        while (r.distanceSensor.getLightDetected() < 0.02) {
            // we need to move toward the beacon
            r.L.setPower(MOTOR_MOVE_CONSTANT);
            r.R.setPower(MOTOR_MOVE_CONSTANT);
            r.BL.setPower(MOTOR_MOVE_CONSTANT);
            r.BR.setPower(MOTOR_MOVE_CONSTANT);
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
                r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PING_RING);
                sleep(5000);
                r.moveForward(250, 0.5);
            }
        }
    }
}
