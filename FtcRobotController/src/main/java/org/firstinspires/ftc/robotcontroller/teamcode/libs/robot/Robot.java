package org.firstinspires.ftc.robotcontroller.teamcode.libs.robot;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.qualcomm.hardware.ams.AMSColorSensorImpl;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;


/**
 * <p>
 *     I'm too lazy to document this code
 * </p>
 */
@TeleOp(name = "Robot Class")
@Disabled
public class Robot {
    public DcMotor L;
    public DcMotor R;
    public DcMotor BL;
    public DcMotor BR;
    public DcMotor Winch;
    public DcMotorController Front;
    public DcMotorController Back;
    public DcMotorController Lift;
    public DeviceInterfaceModule cdim;
    public GyroSensor gyro;
    public ColorSensor colorSensorL;
    public ColorSensor colorSensorR;
    public ColorSensor beaconFinder;
    public ServoController servctrl;
    public CRServo mtrsrv;
    HardwareMap hwmap;
    ToneGenerator generator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    ElapsedTime period = new ElapsedTime();
    boolean initialized = false;
    private float x = 0;
    private float y = 0;
    private double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); //The magnitude of the robot from the origin
    private boolean redAlliance;

    public Robot() {

    }

    public void initializeRobot(HardwareMap aHwMap) {
        while (!initialized) {
            hwmap = aHwMap;
            Front = hwmap.dcMotorController.get("Front");
            Back = hwmap.dcMotorController.get("Back");
            // Lift = hwmap.dcMotorController.get("Lift");
            cdim = hwmap.deviceInterfaceModule.get("cdim");
            L = hwmap.dcMotor.get("frontLeft");
            // L = new DcMotorImpl(Front, 1, DcMotor.Direction.REVERSE);
            // R = new DcMotorImpl(Front, 2);
            R = hwmap.dcMotor.get("frontRight");
            // BL = new DcMotorImpl(Back, 1, DcMotor.Direction.REVERSE);
            BL = hwmap.dcMotor.get("backLeft");
            // BR = new DcMotorImpl(Back, 2);
            BR = hwmap.dcMotor.get("backRight");
            // Winch = new DcMotorImpl(Lift, 1);
            // servctrl = hwmap.servoController.get("Servo Controller 1");
            // mtrsrv = new CRServoImpl(servctrl, 1, CRServo.Direction.REVERSE);
            gyro = new ModernRoboticsI2cGyro(cdim, 2);
            colorSensorL = new ModernRoboticsI2cColorSensor(cdim, 0);
            colorSensorR = new ModernRoboticsI2cColorSensor(cdim, 1);
            beaconFinder = new ModernRoboticsI2cColorSensor(cdim, 3);
            R.setDirection(DcMotor.Direction.REVERSE);
            BR.setDirection(DcMotor.Direction.REVERSE);
            resetEncoders();
            redAlliance = GetAllianceMiddleman.isRed();
            initialized = true;
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float newX) {
        x = newX;
    }

    public float getY() {
        return y;
    }

    public void setY(float newY) {
        y = newY;
    }

    public void setAlliance(boolean isRed) {
        redAlliance = isRed;
    }

    public boolean isRedAlliance() {
        return redAlliance;
    }

    public void resetEncoders() {
        while (BL.getCurrentPosition() != 0 && BR.getCurrentPosition() != 0) {
            BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    public void waitForTick(long periodMs) throws InterruptedException {

        long remaining = periodMs - (long) period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}
