package org.firstinspires.ftc.robotcontroller.internal;

public class GetAllianceMiddleman {
    private static boolean isRed;

    public static void setAlliance(boolean isRedAlliance) {
        isRed = isRedAlliance;
    }

    public static boolean isRed() {
        return isRed;
    }
}
