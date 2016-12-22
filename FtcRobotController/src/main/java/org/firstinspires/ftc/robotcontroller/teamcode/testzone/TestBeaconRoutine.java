package org.firstinspires.ftc.robotcontroller.teamcode.testzone;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;

/**
 * Created by sam on 20-Dec-16.
 */
@Autonomous(name = "Beacon", group = "not working")
public class TestBeaconRoutine extends CustomLOpMode {
    Robot r = new Robot();
    @Override
    public void runOpMode() throws Throwable {
        r.initializeRobot(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            r.cdim.setLED(1, true);
            telemetry.addData("Left Color", Integer.toHexString(r.colorSensorL.argb()));
            telemetry.addData("Right Color", Integer.toHexString(r.colorSensorR.argb()));
            telemetry.addData("Beacon Color", Integer.toHexString(r.beaconFinder.argb()));
            telemetry.addData("Left Color sensor I2c address", r.colorSensorL.getI2cAddress().get8Bit());
            telemetry.addData("Right Color sensor I2c address", r.colorSensorR.getI2cAddress().get8Bit());
            telemetry.addData("Beacon finder color sensor I2c address", r.beaconFinder.getI2cAddress().get8Bit());
            telemetry.addData("Gyro", r.gyro.getHeading());
            r.generator.startTone(ToneGenerator.TONE_DTMF_4, 1000);
            telemetry.update();
            idle();
        }
    }
}
