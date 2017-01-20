package org.firstinspires.ftc.robotcontroller.teamcode.Autonomous;

import android.media.ToneGenerator;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.internal.GetAllianceMiddleman;
import org.firstinspires.ftc.robotcontroller.teamcode.CustomOpMode.CustomLOpMode;
import org.firstinspires.ftc.robotcontroller.teamcode.libs.robot.Robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Locale;

@Autonomous(name = "ModularAutonomous", group = "modular")
public class ModularAutonomous extends CustomLOpMode {
    private final double MOTOR_TURN_CONSTANT = 0.25;
    private final double MOTOR_MOVE_CONSTANT = 0.35;
    private final int MOTOR_ENCODER_360_SPIN = 12527;
    private String program;
    private Robot r;
    private Thread t = null;
    private Thread beep = new Thread(new BeepBoop());
    private Thread boop = new Thread(new BoopBeep());
    private ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private SideOfLine side = SideOfLine.DISORIENTED;

    @Override
    public void runOpMode() throws Throwable {
        // We have just pressed "Init"

        // Initialize robot
        r = new Robot(hardwareMap);
        r.beaconFinder.enableLed(false);
        r.cdim.setLED(0, true);

        // Read from a file what to do later
        try (BufferedReader br = new BufferedReader(new FileReader("match_1.autonomous"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            program = sb.toString();
        }

        // Wait until we press start
        waitForStart();

        // Telemetry thread- sends back robot data for debugging purposes
        t = new Thread(new RunThread());
        if (t != null) {
            t.start();
        }

        // We created a timer so the robot knows how much time is left
        time.reset();

        // We also created a way to wait for a little bit before going
        Thread.sleep((int) Math.floor(GetAllianceMiddleman.getDelayms()));

        PrepareFirstBeacon();
        findLine();
        // followLine();
        findBeacon();
        prepareSecondBeacon();
        goToSecondBeacon();
        squareUpWithLine();
        while (opModeIsActive()) {
            idle();
        }
    }

    @Override
    public void stop() {
        if (t != null) {
            t.interrupt();
            t = null;
        }
        super.stop();
    }

    private void PrepareFirstBeacon() throws InterruptedException {
        r.resetEncoders();
        while (r.L.getCurrentPosition() < 3500 && opModeIsActive()) {
            r.L.setPower(1);
            r.R.setPower(1);
            r.BL.setPower(1);
            r.BR.setPower(1);
            idle();
        }
        r.haltMotors();
        r.resetEncoders();
        r.generator.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY_ONE_SHOT, 200);
        if (GetAllianceMiddleman.isRed()) {
            // Turn left
            r.R.setTargetPosition((MOTOR_ENCODER_360_SPIN / 4));
            while (r.gyroIsWorking ? (r.gyro.getHeading() >= 270 || r.gyro.getHeading() <= 90) : r.R.getCurrentPosition() <= (MOTOR_ENCODER_360_SPIN / 4)) {
                r.L.setPower(0);
                r.R.setPower(MOTOR_TURN_CONSTANT);
                r.BL.setPower(0);
                r.BR.setPower(MOTOR_TURN_CONSTANT);
                idle();
            }
            r.haltMotors();
        } else {
            // Turn Right
            r.L.setTargetPosition((MOTOR_ENCODER_360_SPIN / 4));
            while ((r.gyroIsWorking ? (r.gyro.getHeading() <= 90 || r.gyro.getHeading() >= 270) : (r.L.getCurrentPosition() <= (MOTOR_ENCODER_360_SPIN / 4)))) {
                r.L.setPower(MOTOR_TURN_CONSTANT);
                r.R.setPower(0);
                r.BL.setPower(MOTOR_TURN_CONSTANT);
                r.BR.setPower(0);
                idle();
            }
            r.haltMotors();
        }
        sleep(100);
        System.out.println("Hello robot");
        r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_INTERGROUP);
        r.haltMotors();
    }

    private void findLine() throws InterruptedException {
        // TODO finish adding line finder
        boolean linefound = false;
        // How many times did we have to move backward from the line
        int count = 0;

        while (!linefound && count < 1) {
            r.moveStraight(MOTOR_TURN_CONSTANT);
            if (r.colorSensorL.argb() != 0 || r.colorSensorR.argb() != 0) {
                linefound = true;
            }
            if (r.distanceSensor.getLightDetected() > 0.01 && !linefound) {
                // we did not find the line back up and try again
                r.moveBackward(1200, MOTOR_MOVE_CONSTANT);
                count++;
            }
            idle();
        }
        r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PAT5);
        r.haltMotors();
    }

    private void findBeacon() throws InterruptedException {
        while (r.distanceSensor.getLightDetected() < 0.07) {
            // we need to move toward the beacon
            advanceLineFollowRoutine();
            idle();
        }
        side = SideOfLine.DISORIENTED;
        r.haltMotors();
        ElapsedTime colorRead = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        colorRead.reset();
        boolean isRed;
        boolean isBlue;
        do {
            isRed = r.beaconFinder.red() > 0;
            isBlue = r.beaconFinder.blue() > 0;
            idle();
        } while (colorRead.time() < 500);
        if (r.isRedAlliance()) {
            if (isRed) {
                // move on to next beacon
            } else {
                // hit it again and then move on
                r.moveBackward(250, 1);
                r.haltMotors();
                r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PING_RING);
                sleep(5200);
                r.moveForward(300, 0.5);
                sleep(100);
            }
        } else {
            if (isBlue) {
                // move on to the next beacon
            } else {
                // hit it again then move on
                r.moveBackward(250, 1);
                r.haltMotors();
                r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PING_RING);
                sleep(5200);
                r.moveForward(300, 0.5);
                sleep(100);
            }
        }
    }

    private void prepareSecondBeacon() throws InterruptedException {
        r.resetEncoders();
        r.moveBackward(800, 1);
        if (r.isRedAlliance()) {
            while (r.gyroIsWorking ? (r.gyro.getHeading() > 180) : r.L.getCurrentPosition() < MOTOR_ENCODER_360_SPIN / 4) {
                r.L.setPower(MOTOR_TURN_CONSTANT);
                r.R.setPower(0);
                r.BL.setPower(MOTOR_TURN_CONSTANT);
                r.BR.setPower(0);
                idle();
            }
        } else {
            while (r.gyroIsWorking ? (r.gyro.getHeading() > 0 && r.gyro.getHeading() < 180) : r.R.getCurrentPosition() < MOTOR_ENCODER_360_SPIN / 4) {
                r.L.setPower(0);
                r.R.setPower(MOTOR_TURN_CONSTANT);
                r.BL.setPower(0);
                r.BR.setPower(MOTOR_TURN_CONSTANT);
                idle();
            }
        }
        r.haltMotors();
    }

    private void goToSecondBeacon() throws InterruptedException {
        boolean linefound = false;
        while (!linefound) {
            if (r.colorSensorL.argb() != 0 || r.colorSensorR.argb() != 0) {
                linefound = true;
            }
            r.moveStraight(1);
            idle();
        }
        r.haltMotors();
    }

    private void squareUpWithLine() throws InterruptedException {
        r.resetEncoders();
        if (GetAllianceMiddleman.isRed()) {
            // nudge left a little bit
            while (r.R.getCurrentPosition() < 550) {
                r.L.setPower(-MOTOR_TURN_CONSTANT / 2);
                r.R.setPower(MOTOR_TURN_CONSTANT);
                r.BL.setPower(-MOTOR_TURN_CONSTANT / 2);
                r.BR.setPower(MOTOR_TURN_CONSTANT);
            }

        } else {
            // nudge right a little bit
            while (r.L.getCurrentPosition() < 550) {
                r.L.setPower(MOTOR_TURN_CONSTANT);
                r.R.setPower(-MOTOR_TURN_CONSTANT / 2);
                r.BL.setPower(MOTOR_TURN_CONSTANT);
                r.BR.setPower(-MOTOR_TURN_CONSTANT / 2);
            }
        }
        r.haltMotors();
        boolean linefound = false;
        if (GetAllianceMiddleman.isRed()) {
            // turn left until it sees the white line again
            while (!linefound) {
                r.L.setPower(-MOTOR_TURN_CONSTANT);
                r.R.setPower(MOTOR_TURN_CONSTANT);
                r.BL.setPower(-MOTOR_TURN_CONSTANT);
                r.BR.setPower(MOTOR_TURN_CONSTANT);
                linefound = r.colorSensorL.argb() > 0 || r.colorSensorR.argb() > 0;
                idle();
            }
        } else {
            // Turn right until it sees the white line again
            while (!linefound) {
                r.L.setPower(MOTOR_TURN_CONSTANT);
                r.R.setPower(-MOTOR_TURN_CONSTANT);
                r.BL.setPower(MOTOR_TURN_CONSTANT);
                r.BR.setPower(-MOTOR_TURN_CONSTANT);
                linefound = r.colorSensorL.argb() > 0 || r.colorSensorR.argb() > 0;
                idle();
            }
        }
        r.haltMotors();
            /*linefound = r.colorSensorL.argb() > 0 || r.colorSensorR.argb() > 0;
            r.moveStraight(MOTOR_MOVE_CONSTANT);
            if (!linefound) {
                if (GetAllianceMiddleman.isRed()) {
                    if (GetAllianceMiddleman.isRed()) {
                        // turn left until it sees the white line again
                        while (!linefound) {
                            r.L.setPower(-MOTOR_MOVE_CONSTANT);
                            r.R.setPower(MOTOR_MOVE_CONSTANT);
                            r.BL.setPower(-MOTOR_MOVE_CONSTANT);
                            r.BR.setPower(MOTOR_MOVE_CONSTANT);
                            linefound = r.colorSensorL.argb() > 0 || r.colorSensorR.argb() > 0;
                        }
                    } else {
                        // Turn right until it sees the white line again
                        while (!linefound) {
                            r.L.setPower(MOTOR_MOVE_CONSTANT);
                            r.R.setPower(-MOTOR_MOVE_CONSTANT);
                            r.BL.setPower(MOTOR_MOVE_CONSTANT);
                            r.BR.setPower(-MOTOR_MOVE_CONSTANT);
                            linefound = r.colorSensorL.argb() > 0 || r.colorSensorR.argb() > 0;
                        }
                    }
                }
            }*/
        side = SideOfLine.DISORIENTED;
        while (r.distanceSensor.getLightDetected() < 0.02) {
            advanceLineFollowRoutine();
            idle();
        }
        r.haltMotors();
    }

    private void advanceLineFollowRoutine() throws InterruptedException {
        loop:
        if (r.colorSensorL.argb() != 0 && r.colorSensorR.argb() != 0) {
            // we squared up with the line
            side = SideOfLine.CENTRE;
            r.moveStraight(MOTOR_TURN_CONSTANT);
            r.generator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_NORMAL);
        }
        if (r.colorSensorL.argb() == 0 && r.colorSensorR.argb() != 0) {
            // turn right
            side = SideOfLine.LEFT;
            r.L.setPower(MOTOR_TURN_CONSTANT);
            r.R.setPower(-MOTOR_TURN_CONSTANT);
            r.BL.setPower(MOTOR_TURN_CONSTANT);
            r.BR.setPower(-MOTOR_TURN_CONSTANT);
        }
        if (r.colorSensorL.argb() != 0 && r.colorSensorR.argb() == 0) {
            side = SideOfLine.RIGHT;
            r.L.setPower(-MOTOR_TURN_CONSTANT);
            r.R.setPower(MOTOR_TURN_CONSTANT);
            r.BL.setPower(-MOTOR_TURN_CONSTANT);
            r.BR.setPower(MOTOR_TURN_CONSTANT);
        }

        if (r.colorSensorL.argb() == 0 && r.colorSensorR.argb() == 0) {
            // were hopelessly lost, use the last known side
            r.generator.startTone(ToneGenerator.TONE_SUP_RADIO_NOTAVAIL);
            switch (side) {
                case LEFT:
                    r.L.setPower(MOTOR_TURN_CONSTANT);
                    r.R.setPower(-MOTOR_TURN_CONSTANT);
                    r.BL.setPower(MOTOR_TURN_CONSTANT);
                    r.BR.setPower(-MOTOR_TURN_CONSTANT);
                    break;
                case RIGHT:
                    r.L.setPower(-MOTOR_TURN_CONSTANT);
                    r.R.setPower(MOTOR_TURN_CONSTANT);
                    r.BL.setPower(-MOTOR_TURN_CONSTANT);
                    r.BR.setPower(MOTOR_TURN_CONSTANT);
                    break;
                case CENTRE:
                    r.moveStraight(MOTOR_TURN_CONSTANT);
                    break;
                case DISORIENTED:
                    if (r.isRedAlliance()) {
                        r.L.setPower(MOTOR_TURN_CONSTANT);
                        r.R.setPower(-MOTOR_TURN_CONSTANT);
                        r.BL.setPower(MOTOR_TURN_CONSTANT);
                        r.BR.setPower(-MOTOR_TURN_CONSTANT);
                    } else {
                        r.L.setPower(-MOTOR_TURN_CONSTANT);
                        r.R.setPower(MOTOR_TURN_CONSTANT);
                        r.BL.setPower(-MOTOR_TURN_CONSTANT);
                        r.BR.setPower(MOTOR_TURN_CONSTANT);
                    }
                    telemetry.addLine(String.format(Locale.UK, "Days since Last Crash: %d", 0));
                    break;
            }
        }

    }

    enum SideOfLine {
        LEFT,
        RIGHT,
        CENTRE,
        DISORIENTED;

        @Override
        public String toString() {
            if (equals(LEFT)) {
                return "Left";
            }
            if (equals(CENTRE)) {
                return "Centre";
            }
            if (equals(RIGHT)) {
                return "Right";
            }
            return "disoriented";
        }
    }

    class BoopBeep implements Runnable {
        @Override
        public void run() {
            r.generator.startTone(ToneGenerator.TONE_CDMA_ANSWER, 1000);
        }
    }

    class BeepBoop implements Runnable {
        @Override
        public void run() {
            r.generator.startTone(ToneGenerator.TONE_PROP_BEEP);
        }
    }

    class RunThread implements Runnable {
        File f = null;
        Writer out = null;

        @Override
        public void run() {
            r.generator.startTone(ToneGenerator.TONE_DTMF_4, 1000);
            try {
                f = new File(Environment.getExternalStorageDirectory().getPath() + "/logs.log");
                if (!f.exists()) {
                    f.createNewFile();
                }
                out = new FileWriter(f);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            while (opModeIsActive()) {
                try {
                    out.write(String.format(Locale.UK, "Motor Encoder Position: %d\n", r.L.getCurrentPosition()));
                } catch (Exception e) {
                    stop();
                }
                telemetry.addData("Left Color", Integer.toHexString(r.colorSensorL.argb()));
                telemetry.addData("Right Color", Integer.toHexString(r.colorSensorR.argb()));
                telemetry.addData("Beacon Color", Integer.toHexString(r.beaconFinder.argb()));
                telemetry.addData("Left Color sensor I2c address", r.colorSensorL.getI2cAddress().get8Bit());
                telemetry.addData("Right Color sensor I2c address", r.colorSensorR.getI2cAddress().get8Bit());
                telemetry.addData("Beacon finder color sensor I2c address", r.beaconFinder.getI2cAddress().get8Bit());
                telemetry.addData("Optical distance reading", r.distanceSensor.getLightDetected());
                telemetry.addData("Optical distance reading raw", r.distanceSensor.getRawLightDetected());
                telemetry.addData("Gyro", r.gyro.getHeading());
                telemetry.addData("Alliance color", (GetAllianceMiddleman.isRed() ? "Red" : "Blue"));
                telemetry.addData("Encoder L", r.L.getCurrentPosition());
                telemetry.addData("Encoder R", r.R.getCurrentPosition());
                telemetry.addData("Side of line", side.toString());
                telemetry.update();
                if (isStopRequested()) {
                    break;
                }
                try {
                    idle();
                } catch (Exception r) {
                    // YOOOOOOOOOOOOU'RE OUT!!!!!!!!!!!
                }
            }
        }
    }
}