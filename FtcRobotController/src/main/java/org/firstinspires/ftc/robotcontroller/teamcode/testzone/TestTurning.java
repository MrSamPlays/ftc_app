package org.firstinspires.ftc.robotcontroller.teamcode.testzone;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;

/**
 * Created by sam on 07-Jan-17.
 */
@Autonomous (name = "testTurning", group = "tests")
public class TestTurning extends CustomLOpMode {
    Robot r = new Robot();
    final double MOTOR_MOVE_CONSTANT =0.4;
    final int MOTOR_ENCODER_360_SPIN = 12527;
    @Override
    public void initializeRobot() throws Throwable {
        r = new Robot(hardwareMap);
    }

    @Override
    public void runOpMode() throws Throwable {
        initializeRobot();
        waitForStart();
        r.resetEncoders();
        while (r.gyroIsWorking ? (r.gyro.getHeading() > 180) : r.L.getCurrentPosition() < MOTOR_ENCODER_360_SPIN / 4) {
            r.L.setPower(MOTOR_MOVE_CONSTANT);
            r.R.setPower(0);
            r.BL.setPower(MOTOR_MOVE_CONSTANT);
            r.BR.setPower(0);
            idle();
        }
        while (r.gyroIsWorking ? (r.gyro.getHeading() > 0 || r.gyro.getHeading() < 180) : r.R.getCurrentPosition() < MOTOR_ENCODER_360_SPIN / 4) {
            r.L.setPower(0);
            r.R.setPower(MOTOR_MOVE_CONSTANT);
            r.BL.setPower(0);
            r.BR.setPower(MOTOR_MOVE_CONSTANT);
            idle();
        }
    }
}
