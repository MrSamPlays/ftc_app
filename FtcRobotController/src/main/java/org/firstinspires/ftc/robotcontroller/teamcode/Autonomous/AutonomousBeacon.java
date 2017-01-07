package org.firstinspires.ftc.robotcontroller.teamcode.Autonomous;

import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;

import java.nio.DoubleBuffer;

/**
 * Created by sam on 06-Jan-17.
 */

public class AutonomousBeacon extends CustomLOpMode {
    Robot r;
    boolean isRedAlliance;
    @Override
    public void initializeRobot() throws Throwable{
        r = new Robot(hardwareMap);
    }
    @Override
    public void runOpMode() throws Throwable {
        initializeRobot();
        waitForStart();
        delayProgram();
    }
    public void KnockDownBall() {

    }
}
