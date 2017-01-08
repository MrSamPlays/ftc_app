package org.firstinspires.ftc.robotcontroller.teamcode.libs.motors;

public class DriveSetHandler extends MotorSetHandler {
    MotorSet motors;

    public DriveSetHandler(MotorSet motors, MotorSetType type) {
        super(motors, type);

        if (type != MotorSetType.DRIVE) {
            System.err.println("Warning: DriveSetHandler with type " + type.toString());
        }
    }
}
