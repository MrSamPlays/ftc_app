package org.firstinspires.ftc.robotcontroller.teamcode.libs.robot;

public class Robot {
    private static float x = 0;
    private static float y = 0;
    private static boolean redAlliance;

    public static void setX(float newX) {
        x = newX;
    }

    public static float getX() {
        return x;
    }

    public static void setY(float newY) {
        y = newY;
    }

    public static float getY() {
        return y;
    }

    public static void setAlliance(boolean isRed) {
        redAlliance = isRed;
    }

    public static boolean isRedAlliance() {
        return redAlliance;
    }
}