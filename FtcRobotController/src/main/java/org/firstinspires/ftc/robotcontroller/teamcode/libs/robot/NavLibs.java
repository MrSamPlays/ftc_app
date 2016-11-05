package org.firstinspires.ftc.robotcontroller.teamcode.libs.robot;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.hardware.DcMotor;

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

    public void findLine() {

    }
}
