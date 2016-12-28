package org.firstinspires.ftc.robotcontroller.teamcode.libs.motors;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MotorHandler {
    private DcMotor motor;

    public MotorHandler(DcMotor motor) {
        this.motor = motor;
    }

    /**
     * This method sets the speed of the motor that it controls.
     *
     * @param speed The speed between 0 and 100.
     */
    public void speed(float speed) {
        motor.setPower(speed / 100);
    }

    /**
     * This method sets if the motor brakes or not.
     *
     * @param brake If true, then brake. If false, coast.
     */
    public void brakeOnStop(boolean brake) {
        if (brake == true) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else if (brake == false) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        } else {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.UNKNOWN);
        }
    }
}
