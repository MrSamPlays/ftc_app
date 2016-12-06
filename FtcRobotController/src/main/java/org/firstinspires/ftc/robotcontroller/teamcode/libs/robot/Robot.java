package org.firstinspires.ftc.robotcontroller.teamcode.libs.robot;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * <h1>Notes</h1>
 * <p>This program is designed to be a robot initializer, thus making the <code>initializeRobot()</code> method 99.9% useless because all the robot initialization is done by calling <code>Robot.initialize()</code></p>
 */
public class Robot {
    public static DcMotor L;
    public static DcMotor R;
    public static DcMotor BL;
    public static DcMotor BR;
    public static DcMotor Winch;
    public static DcMotorController Front;
    public static DcMotorController Back;
    public static DcMotorController Lift;
    public static DeviceInterfaceModule cdim;
    public static GyroSensor gyro;
    public static ServoController servctrl;
    public static CRServo mtrsrv;
    private static float x = 0;
    private static float y = 0;
    private static double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); //The magnitude of the robot from the origin
    private static boolean redAlliance;

    public static void initialize(HardwareMap hardwareMap) {
        Front = hardwareMap.dcMotorController.get("Front");
        Back = hardwareMap.dcMotorController.get("Back");
        Lift = hardwareMap.dcMotorController.get("Lift");
        cdim = hardwareMap.deviceInterfaceModule.get("cdim");
        gyro = new ModernRoboticsI2cGyro(cdim, 1);
        servctrl = hardwareMap.servoController.get("Servo Controller 1");
        L = new DcMotorImpl(Front, 1, DcMotor.Direction.REVERSE);
        R = new DcMotorImpl(Front, 2);
        BL = new DcMotorImpl(Back, 1, DcMotor.Direction.REVERSE);
        BR = new DcMotorImpl(Back, 2);
        Winch = new DcMotorImpl(Lift, 1);
        mtrsrv = new CRServoImpl(servctrl, 1, CRServo.Direction.REVERSE);
        resetEncoders();
    }
    public static float getX() {
        return x;
    }

    public static void setX(float newX) {
        x = newX;
    }

    public static float getY() {
        return y;
    }

    public static void setY(float newY) {
        y = newY;
    }

    public static void setAlliance(boolean isRed) {
        redAlliance = isRed;
    }

    public static boolean isRedAlliance() {
        return redAlliance;
    }

    /**
     *
     */
    public static void resetEncoders() {
        L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}