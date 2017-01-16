package org.firstinspires.ftc.robotcontroller.teamcode.Autonomous;

import android.media.ToneGenerator;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.Working;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

/**
 * Created by sam on 15-Jan-17.
 */
@Autonomous (name = "Alt-Autonomous1")
@Working
public class AltAutonomous extends CustomLOpMode {
    final double MOTOR_TURN_CONSTANT = 0.25;
    final double MOTOR_MOVE_CONSTANT = 0.35;
    final int MOTOR_ENCODER_360_SPIN = 12527;
    Robot r;
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
    private void KnockBallDown() throws Throwable{
        r.resetEncoders();
        if (r.isRedAlliance()) {
            while (r.gyroIsWorking ? r.gyro.getHeading() > 315 || r.gyro.getHeading() < 45 : r.R.getCurrentPosition() < MOTOR_ENCODER_360_SPIN/8) {
                r.L.setPower(0);
                r.R.setPower(MOTOR_TURN_CONSTANT);
                r.BL.setPower(0);
                r.BR.setPower(MOTOR_TURN_CONSTANT);
                idle();
            }
        } else {
            while (r.gyroIsWorking ? r.gyro.getHeading() > 315 || r.gyro.getHeading() < 45 : r.L.getCurrentPosition() < MOTOR_ENCODER_360_SPIN/8) {
                r.L.setPower(MOTOR_TURN_CONSTANT);
                r.R.setPower(0);
                r.BL.setPower(MOTOR_TURN_CONSTANT);
                r.BR.setPower(0);
                idle();
            }
        }
        while (r.range.getDistance(DistanceUnit.CM) > 4.5) {
            r.moveStraight(MOTOR_MOVE_CONSTANT);
        }
    }
    SideOfLine side;
    public void advanceLineFollowRoutine() throws InterruptedException {
        loop:
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
