package org.firstinspires.ftc.robotcontroller.teamcode.Teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by sam on 15-Oct-16.
 * It's a tele-op programme, and its mighty huge
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LinearOpMode {
    boolean LedEnabled = false;
    DcMotor L;
    DcMotor R;
    /*class Shutdown implements Runnable {
        private long starttime = System.currentTimeMillis();
        private int time;
        Shutdown(int s) {
            this.time = s / 1000;
        }
        @Override
        public void run() {
            starttime = System.currentTimeMillis();
            while (System.currentTimeMillis() - starttime < time) {
                telemetry.addData("what", System.currentTimeMillis() - starttime);
                try {
                    Thread.sleep(10);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            stop();
        }
    }*/
    private void InitializeRobot() {
        L = hardwareMap.dcMotor.get("L");
        L.setDirection(DcMotorSimple.Direction.REVERSE);
        R = hardwareMap.dcMotor.get("R");
    }
    @Override
    public void runOpMode() throws InterruptedException {
        InitializeRobot();
        waitForStart();
        // runs for 10 seconds
        // Shutdown s = new Shutdown(30000);
        // Thread t = new Thread(s);
        // t.start();
        while (opModeIsActive()) {
            L.setMaxSpeed(2048);
            R.setMaxSpeed(2048);
            L.setPower(gamepad1.left_stick_y);
            R.setPower(gamepad1.right_stick_y);
            if (gamepad1.dpad_left) {
                toggleLED();
            }
        }
    }
    private void toggleLED() {
        LedEnabled = !LedEnabled;

    }
}
