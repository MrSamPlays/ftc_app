/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions ans in bnd the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.robotcontroller.teamcode.Autonomous;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.internal.GetResourcesMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.imagenav.ImageReader;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.NavLibs;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.sensors.ColorSensor;

@Autonomous(name = "General Automaticness", group = "Nathan's Test Routines")
public class AutoOpGeneral extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    private Bitmap sides = null;
    private Bitmap beacons = null;
    private Bitmap centerVortex = null;
    private Bitmap cornerVortex = null;
    private ColorSensor bottomSensor = null;

    private NavLibs navigator = null;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Resources resources = GetResourcesMiddleman.getResources();
        ImageReader.addResources(resources);

        sides = ImageReader.loadImage(R.drawable.sides, 36, 36);
        beacons = ImageReader.loadImage(R.drawable.beacons, 36, 36);
        centerVortex = ImageReader.loadImage(R.drawable.center_vortex, 36, 36);
        cornerVortex = ImageReader.loadImage(R.drawable.corner_vortex, 36, 36);

        Robot.initialize(hardwareMap);

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).

        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        bottomSensor = new ColorSensor("ColorSensor", hardwareMap);

        navigator = new NavLibs(leftMotor, rightMotor, sides, beacons, centerVortex, cornerVortex);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)

        runNav();

        /*while (opModeIsActive()) {
            boolean isRed = Robot.isRedAlliance();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Color R", bottomSensor.getColor()[0]);
            telemetry.addData("Color G", bottomSensor.getColor()[1]);
            telemetry.addData("Color B", bottomSensor.getColor()[2]);
            telemetry.addData("Found line?", navigator.testColor(bottomSensor, new float[]{255, 0, 0}, 50));
            telemetry.update();

            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }*/
    }

    public void runNav() throws InterruptedException {
        Robot.drive.setAllMotorSpeed(100);

        // EventDoer foundLine = navigator.findColor(bottomSensor, new float[]{150, 0, 0}, 75);

        // while(!foundLine.isTriggered() && opModeIsActive()) {
        while (opModeIsActive()) {
            idle();
        }

        /**
         * if(!opModeIsActive()) {
         return;
         }
         */
    }
}