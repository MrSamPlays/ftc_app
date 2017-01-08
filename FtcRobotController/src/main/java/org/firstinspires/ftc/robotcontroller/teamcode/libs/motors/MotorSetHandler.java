package org.firstinspires.ftc.robotcontroller.teamcode.libs.motors;

public abstract class MotorSetHandler {
    private MotorSet motors;
    private MotorSetType type;

    public MotorSetHandler(MotorSet motors, MotorSetType type) {
        this.type = type;
        this.motors = motors;
    }

    public void setAllMotorSpeed(float power) {
        for (MotorHandler motor : motors.getAllMotors()) {
            motor.speed(power);
        }
    }
}
