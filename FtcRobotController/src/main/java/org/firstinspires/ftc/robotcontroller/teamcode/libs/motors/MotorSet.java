package org.firstinspires.ftc.robotcontroller.teamcode.libs.motors;

import java.util.HashMap;

public class MotorSet {
    private HashMap<String, MotorHandler> motors = new HashMap<String, MotorHandler>();

    public MotorSet(HashMap<String, MotorHandler> motors) {
        this.motors = motors;
    }

    public MotorSet() {
    }

    public void addMotor(MotorHandler motor, String motorPosition) {
        motors.put(motorPosition, motor);
        MotorHandler[] motorst = new MotorHandler[5];

    }

    public MotorHandler getMotor(String motorPosition) {
        return motors.get(motorPosition);
    }

    public MotorHandler[] getAllMotors() {
        MotorHandler[] allMotors = new MotorHandler[motors.values().size()];
        motors.values().toArray(allMotors);

        return allMotors;
    }
}
