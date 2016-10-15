package org.firstinspires.ftc.robotcontroller.teamcode.Teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by sam on 15-Oct-16.
 * It's a tele-op programme, and its mighty huge
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LinearOpMode {
    DcMotor L;
    DcMotor R;
    private void InitializeRobot() {
        L = hardwareMap.dcMotor.get("L");
        L.setDirection(DcMotorSimple.Direction.REVERSE);
        R = hardwareMap.dcMotor.get("R");
    }
    @Override
    public void runOpMode() throws InterruptedException {
        InitializeRobot();
        waitForStart();
        while (opModeIsActive()) {
            L.setMaxSpeed(2048);
            R.setMaxSpeed(2048);
            L.setPower(gamepad1.left_stick_y);
            R.setPower(gamepad1.right_stick_y);
        }
    }
}
