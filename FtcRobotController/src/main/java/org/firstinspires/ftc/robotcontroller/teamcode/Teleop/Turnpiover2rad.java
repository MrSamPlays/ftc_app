//package org.firstinspires.ftc.robotcontroller.teamcode.Teleop;
//
//import android.media.AudioFormat;
//import android.media.AudioManager;
//import android.media.MediaActionSound;
//import android.media.MediaFormat;
//import android.media.ToneGenerator;
//
//import com.google.gson.internal.Streams;
//import com.qualcomm.ftccommon.SoundPlayer;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.GyroSensor;
//import com.qualcomm.robotcore.hardware.HardwareDevice;
//import android.media.AsyncPlayer;
//import android.media.midi.MidiDevice;
//import android.provider.MediaStore;
//import android.view.SoundEffectConstants;
//
//import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
//
//import java.io.StreamTokenizer;
//
///**
// * Created by sam on 29-Oct-16.
// */
//@Autonomous (name="Turn Pi/2 radians", group = "testzone")
//public class Turnpiover2rad extends CustomLOpMode {
//    DcMotor L;
//    DcMotor R;
//    GyroSensor gyro;
//    ToneGenerator generator;
//    protected void InitializeRobot() {
//        generator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
//        L = hardwareMap.dcMotor.get("Left");
//        L.setDirection(DcMotorSimple.Direction.REVERSE);
//        R = hardwareMap.dcMotor.get("Right");
//        gyro = hardwareMap.gyroSensor.get("gyro");
//        gyro.calibrate();
//        telemetry.addData("Gyro Sensor Bearing", gyro.getHeading());
//        while (gyro.isCalibrating()) {
//            generator.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY, 10);
//        }
//    }
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        InitializeRobot();
//        waitForStart();
//        generator.stopTone();
//
//        // while (opModeIsActive()) {
//            telemetry.update();
//            do {
//                L.setPower(-1);
//                R.setPower(1);
//            } while (gyro.getHeading() <= 90);
//        generator.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 500);
//        // }
//    }
//    @Override
//    public void stop() {
//        super.stop();
//    }
//}