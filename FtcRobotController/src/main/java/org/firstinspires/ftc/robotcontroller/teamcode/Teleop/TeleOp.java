package org.firstinspires.ftc.robotcontroller.teamcode.Teleop;


import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftccommon.ViewLogsActivity;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.sensors.ColorSensor;
import org.firstinspires.ftc.robotcore.internal.TelemetryImpl;

/**
 * Created by sam on 15-Oct-16.
 * It's a tele-op programme, and its mighty huge
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpus No. 99 in G diminished", group = "Trollin'")
public class TeleOp extends CustomLOpMode {
    DcMotor L;
    DcMotor R;
    // CRServo analog;
    GyroSensor gyro;
    ColorSensor colour;
    boolean red = GetAllianceMiddleman.isRed();
    ToneGenerator generator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    private void InitializeRobot() {
        L = hardwareMap.dcMotor.get("Left");
        L.setDirection(DcMotorSimple.Direction.REVERSE);
        R = hardwareMap.dcMotor.get("Right");
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
        colour = new ColorSensor("ColorSensor", hardwareMap, true);
        // analog = hardwareMap.crservo.get("analog");
        generator.startTone(ToneGenerator.TONE_SUP_CALL_WAITING, 200);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        InitializeRobot();
        waitForStart();

        // runs for 10 seconds
        // Shutdown s = new Shutdown(30000);
        // Thread t = new Thread(s);
        // t.start();

        /*while (gyro.isCalibrating()) {
            // wait until gyro is finished calibrating
            updateTelemetry(telemetry);
            telemetry.addData("Gyro Heading", gyro.getHeading());
            telemetry.addData("Gyro Z Angle", gyro.rawZ());
            telemetry.addData("Gyro status", gyro.status());
            generator.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY, 200);
            System.out.println("Sounds");
        }*/
        generator.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 200);
        while (opModeIsActive()) {
            telemetry.addData("Colour", colour.getColor());
            telemetry.addData("Telemetry test", "Tested OK");
            telemetry.update();
            /*if (gamepad1.left_bumper) {
                analog.setPower(1);
            } else {
                analog.setPower(0);
            }*/
            L.setMaxSpeed(2048);
            R.setMaxSpeed(2048);
            L.setPower(gamepad1.left_stick_y);
            R.setPower(gamepad1.right_stick_y);

            /*telemetry.addData("Gyro Heading", gyro.getHeading());
            telemetry.addData("Gyro Z Angle", gyro.rawZ());
            telemetry.addData("Gyro status", gyro.status());*/
            idle();
        }
    }
}