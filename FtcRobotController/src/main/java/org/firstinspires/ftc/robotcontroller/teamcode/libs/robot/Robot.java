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

import org.firstinspires.ftc.robotcontroller.teamcode.libs.motors.DriveSetHandler;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.motors.MotorHandler;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.motors.MotorSet;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.motors.MotorSetType;

import java.util.HashMap;

/**
 * <h1>Notes</h1>
 * <p>This program is designed to be a robot initializer, thus making the <code>initializeRobot()</code> method 99.9% useless because all the robot initialization is done by calling <code>Robot.initialize()</code></p>
 */
public class Robot {
    private static float x = 0;
    private static float y = 0;

    /**
     * This is the orientation in degrees
     **/
    private static double orientation;

    private static boolean redAlliance = true;

    public static DriveSetHandler drive;

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

    public static void initialize(HardwareMap hardwareMap) {
        Front = hardwareMap.dcMotorController.get("Front");
        Back = hardwareMap.dcMotorController.get("Back");
        //Lift = hardwareMap.dcMotorController.get("Lift");
        //dcdim = hardwareMap.deviceInterfaceModule.get("cdim");
        gyro = new ModernRoboticsI2cGyro(cdim, 1);
        servctrl = hardwareMap.servoController.get("Servo Controller 1");

        HashMap<String, MotorHandler> driveMotorSet = new HashMap<String, MotorHandler>();

        L = new DcMotorImpl(Front, 1, DcMotor.Direction.REVERSE);
        MotorHandler frontLeft = new MotorHandler(L);
        driveMotorSet.put("frontLeft", frontLeft);

        R = new DcMotorImpl(Front, 2);
        MotorHandler frontRight = new MotorHandler(R);
        driveMotorSet.put("frontRight", frontRight);

        BL = new DcMotorImpl(Back, 1, DcMotor.Direction.REVERSE);
        MotorHandler backLeft = new MotorHandler(BL);
        driveMotorSet.put("backLeft", backLeft);

        BR = new DcMotorImpl(Back, 2);
        MotorHandler backRight = new MotorHandler(BR);
        driveMotorSet.put("backRight", backRight);

        MotorSet driveMotors = new MotorSet(driveMotorSet);
        drive = new DriveSetHandler(driveMotors, MotorSetType.DRIVE);

        Winch = new DcMotorImpl(Lift, 1);
        mtrsrv = new CRServoImpl(servctrl, 1, CRServo.Direction.REVERSE);
        resetEncoders();
    }

    public static void setX(float newX) {
        x = newX;
    }

    public static float getX() {
        return x;
    }

    public static void setY(float newY) {
        y = newY;
    }

    public static float getY() {
        return y;
    }

    public static void setOrientation(double newOrientation) {
        orientation = newOrientation;
    }

    public static double getOrientation() {
        return orientation;
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
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}