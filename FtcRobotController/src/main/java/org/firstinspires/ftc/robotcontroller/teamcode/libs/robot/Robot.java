package org.firstinspires.ftc.robotcontroller.teamcode.libs.robot;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.qualcomm.ftccommon.FtcRobotControllerService;
import com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * <p>
 * I'm too lazy to document this code<br>
 * </p>
 * <p>
 * Alpha: Shift 24 bits left<br>
 * Red: Shift 16 bits<br>
 * Green: Shift 8 bits<br>
 * Blue: do not shift.<br>
 * </p>
 */
public class Robot extends CustomLOpMode {
    public DcMotor L;
    public DcMotor R;
    public DcMotor BL;
    public DcMotor BR;
    public DcMotor Winch;
    public DcMotorController Front;
    public DcMotorController Back;
    public DcMotorController Lift;
    public DeviceInterfaceModule cdim;
    public GyroSensor gyro; // I2C address 0x20, physical port 2
    public ColorSensor colorSensorL; // I2C address 0x3c, physical port 0
    public ColorSensor colorSensorR; // I2C address 0x6a, physical port 1
    public ColorSensor beaconFinder; // I2C address 0x66, physical port 3
    public ServoController servctrl;
    public CRServo mtrsrv;
    public OpticalDistanceSensor distanceSensor;
    public ModernRoboticsI2cRangeSensor range;
    public ToneGenerator generator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    private ElapsedTime period = new ElapsedTime();
    private HardwareMap hwmap;
    // private boolean initialized = false;
    public boolean gyroIsWorking = true;
    private float x = 0;
    private float y = 0;
    private double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); //The magnitude of the robot from the origin
    private boolean redAlliance;
    private ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

    /**
     * <code>Robot()</code> does not automatically initialize robot call <code>initializeRobot()</code> to initialize
     */
    public Robot() {

    }

    /**
     * This constructor automatically initializes the robot
     * Instantiate the class before calling <code>waitForStart()</code>
     *
     * @param map - the <code>HardwareMap</code> to use
     */
    public Robot(HardwareMap map) throws Throwable {
        initializeRobot(map);
    }

    public void initializeRobot(HardwareMap aHwMap) throws Throwable {
        hwmap = aHwMap;
        Front = hwmap.dcMotorController.get("Front");
        Back = hwmap.dcMotorController.get("Back");
        Lift = hwmap.dcMotorController.get("Lift");
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
        Winch = hwmap.dcMotor.get("Winch");
        servctrl = hwmap.servoController.get("Servos");
        // mtrsrv = new CRServoImpl(servctrl, 1, CRServo.Direction.REVERSE);
        range = hwmap.get(ModernRoboticsI2cRangeSensor.class, "range");
        // colorSensorL = hwmap.colorSensor.get("colorSensorL");
        colorSensorL = new ModernRoboticsI2cColorSensor(cdim, 0);
        // colorSensorR = hwmap.colorSensor.get("colorSensorR");
        colorSensorR = new ModernRoboticsI2cColorSensor(cdim, 1);
        // gyro = hwmap.gyroSensor.get("gyro");
        gyro = new ModernRoboticsI2cGyro(cdim, 2);
        //beaconFinder = hwmap.colorSensor.get("beaconFinder");
        beaconFinder = new ModernRoboticsI2cColorSensor(cdim, 3);
        colorSensorL.setI2cAddress(I2cAddr.create8bit(0x3C));
        colorSensorR.setI2cAddress(I2cAddr.create8bit(0x6a));
        beaconFinder.setI2cAddress(I2cAddr.create8bit(0x66));
        range.enableLed(true);
        distanceSensor = new ModernRoboticsAnalogOpticalDistanceSensor(cdim, 0);
        R.setDirection(DcMotor.Direction.REVERSE);
        BR.setDirection(DcMotor.Direction.REVERSE);
        resetEncoders();
        redAlliance = GetAllianceMiddleman.isRed();
        colorSensorL.enableLed(true);
        colorSensorR.enableLed(true);
        beaconFinder.enableLed(true);
        distanceSensor.enableLed(true);
        L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        timer.reset();
        gyro.calibrate();
        while (gyro.isCalibrating()) {
            generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_NORMAL, 1000);
            if (timer.seconds() > 20) {
                generator.startTone(ToneGenerator.TONE_SUP_ERROR, 1000);
                gyroIsWorking = false;
                // Warn the user that the gyro sensor calibration is taking longer than usual
                break;
            }
            idle();
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

    public void haltMotors() {
        L.setPower(0);
        R.setPower(0);
        BL.setPower(0);
        BR.setPower(0);
    }

    public void haltMotors(boolean coast) {
        if (!coast) {
            L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        L.setPower(0);
        R.setPower(0);
        BL.setPower(0);
        BR.setPower(0);
    }
    public void resetEncoders() throws InterruptedException {
        do {
            BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            idle();
        } while (BL.getCurrentPosition() != 0 || BR.getCurrentPosition() != 0 || L.getCurrentPosition() != 0 || R.getCurrentPosition() != 0);
    }

    public void waitForTick(long periodMs) throws InterruptedException {

        long remaining = periodMs - (long) period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    public int getColourRGB(ColorSensor sensor) {
        int color = (sensor.red() << 16 | sensor.green() << 8 | sensor.blue());
        return color;
    }

    public void moveForward(int distance, double power) throws InterruptedException {
        resetEncoders();
        if (distance < 0 || power < 0) {
            System.err.println("you are such a bad person");
            throw new IllegalArgumentException();
        } else {
            while (L.getCurrentPosition() < distance) {
                L.setPower(power);
                R.setPower(power);
                BL.setPower(power);
                BR.setPower(power);
            }
        }
    }
    public void moveStraight(double power) {
        L.setPower(power);
        R.setPower(power);
        BL.setPower(power);
        BR.setPower(power);
    }
    public void moveBackward(int distance, double power) throws InterruptedException {
        resetEncoders();
        if (distance < 0 || power < 0) {
            System.err.println("you are such a bad person");
            throw new IllegalArgumentException();
        } else {
            while (L.getCurrentPosition() > -distance) {
                L.setPower(-power);
                R.setPower(-power);
                BL.setPower(-power);
                BR.setPower(-power);
            }
        }
    }
}
