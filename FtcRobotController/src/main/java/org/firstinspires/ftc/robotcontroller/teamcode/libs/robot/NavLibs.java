package org.firstinspires.ftc.robotcontroller.teamcode.libs.robot;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.teamcode.libs.sensors.ColorSensor;

public class NavLibs {
    private Bitmap sides = null;
    private Bitmap beacons = null;
    private Bitmap centerVortex = null;
    private Bitmap cornerVortex = null;

    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;

    public NavLibs(DcMotor leftMotor, DcMotor rightMotor, Bitmap sides, Bitmap beacons, Bitmap centerVortex, Bitmap cornerVortex) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.sides = sides;
        this.beacons = beacons;
        this.centerVortex = centerVortex;
        this.cornerVortex = cornerVortex;
    }

    public void findColor(ColorSensor bottomSensor, float[] color) {
        findColor(bottomSensor, color, 40);
    }

    public void findColor(ColorSensor bottomSensor, float[] color, int sensitivity) {
        while (testColor(bottomSensor, color, sensitivity)) {
        }
    }

    public boolean testColor(ColorSensor sensor, float[] color, int sensitivity) {
        sensor.turnOnLight();

        float[] sensorColor = sensor.getColor();

        float[] colorAbove = sensorColor;
        colorAbove[0] += sensitivity;
        colorAbove[1] += sensitivity;
        colorAbove[2] += sensitivity;

        float[] colorBelow = sensorColor;
        colorBelow[0] -= sensitivity;
        colorBelow[1] -= sensitivity;
        colorBelow[2] -= sensitivity;

        boolean rInRange = (color[0] + sensitivity) > sensorColor[0] && sensorColor[0] > (color[0] - sensitivity);
        boolean gInRange = (color[1] + sensitivity) > sensorColor[1] && sensorColor[1] > (color[1] - sensitivity);
        boolean bInRange = (color[2] + sensitivity) > sensorColor[2] && sensorColor[2] > (color[2] - sensitivity);

        return rInRange && gInRange && bInRange;
    }
}