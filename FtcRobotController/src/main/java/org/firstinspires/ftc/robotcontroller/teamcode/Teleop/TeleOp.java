package org.firstinspires.ftc.robotcontroller.teamcode.Teleop;


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.media.session.MediaController;
import android.net.Uri;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.File;
import java.net.URI;

/**
 * Created by sam on 15-Oct-16.
 * It's a tele-op programme, and its mighty huge
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LinearOpMode {
    DcMotor L;
    DcMotor R;
    GyroSensor gyro;
    ToneGenerator generator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    private void InitializeRobot() {
        L = hardwareMap.dcMotor.get("Left");
        L.setDirection(DcMotorSimple.Direction.REVERSE);
        R = hardwareMap.dcMotor.get("Right");
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
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

        while (gyro.isCalibrating()) {
            // wait until gyro is finished calibrating
            updateTelemetry(telemetry);
            telemetry.addData("Gyro Heading", gyro.getHeading());
            telemetry.addData("Gyro Z Angle", gyro.rawZ());
            telemetry.addData("Gyro status", gyro.status());
            generator.startTone(ToneGenerator.TONE_SUP_BUSY, 200);
            System.out.println("Sounds");
        }
        generator.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 200);
        while (opModeIsActive()) {
            L.setMaxSpeed(2048);
            R.setMaxSpeed(2048);
            L.setPower(gamepad1.left_stick_y);
            R.setPower(gamepad1.right_stick_y);
            telemetry.addData("Gyro Heading", gyro.getHeading());
            telemetry.addData("Gyro Z Angle", gyro.rawZ());
            telemetry.addData("Gyro status", gyro.status());
        }
    }
}
