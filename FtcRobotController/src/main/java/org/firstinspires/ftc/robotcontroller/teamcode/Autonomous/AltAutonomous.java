package org.firstinspires.ftc.robotcontroller.teamcode.Autonomous;

import android.media.ToneGenerator;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;

import java.util.Locale;

/**
 * Created by sam on 15-Jan-17.
 */
@Autonomous(name = "Next to the line", group = "Still not guaranteed to work")
public class AltAutonomous extends CustomLOpMode {
    final double MOTOR_TURN_CONSTANT = 0.25;
    final double MOTOR_MOVE_CONSTANT = 0.35;
    final int MOTOR_ENCODER_360_SPIN = 12527;
    Robot r;
    SideOfLine side = SideOfLine.DISORIENTED;

    @Override
    public void initializeRobot() throws Throwable {
        r = new Robot(hardwareMap);
    }

    @Override
    public void runOpMode() throws Throwable {
        initializeRobot();
        waitForStart();
        delayProgram();
        KnockBallDown();
    }

    private void KnockBallDown() throws Throwable {
        r.resetEncoders();
        final int TURN_TARGET = 30;
        if (r.isRedAlliance()) {
            while (r.gyroIsWorking ? r.gyro.getHeading() > 360 - TURN_TARGET || r.gyro.getHeading() < TURN_TARGET : r.R.getCurrentPosition() < MOTOR_ENCODER_360_SPIN / 8) {
                r.L.setPower(0);
                r.R.setPower(MOTOR_TURN_CONSTANT);
                r.BL.setPower(0);
                r.BR.setPower(MOTOR_TURN_CONSTANT);
                idle();
            }
        } else {
            while (r.gyroIsWorking ? r.gyro.getHeading() > 360 - TURN_TARGET || r.gyro.getHeading() < TURN_TARGET : r.L.getCurrentPosition() < MOTOR_ENCODER_360_SPIN / 8) {
                r.L.setPower(MOTOR_TURN_CONSTANT);
                r.R.setPower(0);
                r.BL.setPower(MOTOR_TURN_CONSTANT);
                r.BR.setPower(0);
                idle();
            }
        }
        r.moveForward(6400, 1);
        while (r.distanceSensor.getLightDetected() < 0.02) {
            r.moveStraight(1);
            idle();
        }
    }

    private void findBeacon() throws InterruptedException {
        while (r.distanceSensor.getLightDetected() < 0.07) {
            // we need to move toward the beacon
            advanceLineFollowRoutine();
            idle();
        }
        side = SideOfLine.DISORIENTED;
        r.haltMotors();
        ElapsedTime colorRead = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        colorRead.reset();
        boolean isRed;
        boolean isBlue;
        do {
            isRed = r.beaconFinder.red() > 0;
            isBlue = r.beaconFinder.blue() > 0;
            idle();
        } while (colorRead.time() < 500);
        if (r.isRedAlliance()) {
            if (isRed) {
                // move on to next beacon
            } else {
                // hit it again and then move on
                r.moveBackward(250, 1);
                r.haltMotors();
                r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PING_RING);
                sleep(5200);
                r.moveForward(300, 0.5);
                sleep(100);
            }
        } else {
            if (isBlue) {
                // move on to the next beacon
            } else {
                // hit it again then move on
                r.moveBackward(250, 1);
                r.haltMotors();
                r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PING_RING);
                sleep(5200);
                r.moveForward(300, 0.5);
                sleep(100);
            }
        }
    }

    public void advanceLineFollowRoutine() throws InterruptedException {
        if (r.colorSensorL.argb() != 0 && r.colorSensorR.argb() != 0) {
            // we squared up with the line
            side = SideOfLine.CENTRE;
            r.moveStraight(MOTOR_TURN_CONSTANT);
            r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_NORMAL);
        }
        if (r.colorSensorL.argb() == 0 && r.colorSensorR.argb() != 0) {
            // turn right
            side = SideOfLine.LEFT;
            r.L.setPower(MOTOR_TURN_CONSTANT);
            r.R.setPower(-MOTOR_TURN_CONSTANT);
            r.BL.setPower(MOTOR_TURN_CONSTANT);
            r.BR.setPower(-MOTOR_TURN_CONSTANT);
        }
        if (r.colorSensorL.argb() != 0 && r.colorSensorR.argb() == 0) {
            side = SideOfLine.RIGHT;
            r.L.setPower(-MOTOR_TURN_CONSTANT);
            r.R.setPower(MOTOR_TURN_CONSTANT);
            r.BL.setPower(-MOTOR_TURN_CONSTANT);
            r.BR.setPower(MOTOR_TURN_CONSTANT);
        }

        if (r.colorSensorL.argb() == 0 && r.colorSensorR.argb() == 0) {
            // were hopelessly lost, use the last known side
            r.generator.startTone(ToneGenerator.TONE_SUP_RADIO_NOTAVAIL);
            switch (side) {
                case LEFT:
                    r.L.setPower(MOTOR_TURN_CONSTANT);
                    r.R.setPower(-MOTOR_TURN_CONSTANT);
                    r.BL.setPower(MOTOR_TURN_CONSTANT);
                    r.BR.setPower(-MOTOR_TURN_CONSTANT);
                    break;
                case RIGHT:
                    r.L.setPower(-MOTOR_TURN_CONSTANT);
                    r.R.setPower(MOTOR_TURN_CONSTANT);
                    r.BL.setPower(-MOTOR_TURN_CONSTANT);
                    r.BR.setPower(MOTOR_TURN_CONSTANT);
                    break;
                case CENTRE:
                    r.moveStraight(MOTOR_TURN_CONSTANT);
                    break;
                case DISORIENTED:
                    if (r.isRedAlliance()) {
                        r.L.setPower(MOTOR_TURN_CONSTANT);
                        r.R.setPower(-MOTOR_TURN_CONSTANT);
                        r.BL.setPower(MOTOR_TURN_CONSTANT);
                        r.BR.setPower(-MOTOR_TURN_CONSTANT);
                    } else {
                        r.L.setPower(-MOTOR_TURN_CONSTANT);
                        r.R.setPower(MOTOR_TURN_CONSTANT);
                        r.BL.setPower(-MOTOR_TURN_CONSTANT);
                        r.BR.setPower(MOTOR_TURN_CONSTANT);
                    }
                    telemetry.addLine(String.format(Locale.UK, "Days since Last Crash: %d", 0));
                    break;
            }
        }
    }

    /**
     * LEFT - To the left of the line
     * RIGHT - To the Right of the line
     * CENTRE - in the Centre of the line
     * DISORIENTED - The Robot has not found the line yet.
     */
    public enum SideOfLine {
        LEFT,
        RIGHT,
        CENTRE,
        DISORIENTED;

        @Override
        public String toString() {
            if (equals(LEFT)) {
                return "Left";
            }
            if (equals(CENTRE)) {
                return "Centre";
            }
            if (equals(RIGHT)) {
                return "Right";
            }
            return "disoriented";
        }
    }
}
