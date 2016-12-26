package org.firstinspires.ftc.robotcontroller.internal;

public class GetAllianceMiddleman {
    private static boolean isRed;
    /**
     * Delay (in seconds)
     */
    private static double delay;
    public static void setAlliance(boolean isRedAlliance) {
        isRed = isRedAlliance;
    }

    public static boolean isRed() {
        return isRed;
    }

    /**
     *
     * @param d Delay in seconds
     */
    public static void setDelay(double d) {
        if(d >= 0 && d <= 30) {
            delay = d;
        }
        else {
            delay = 0;
        }
    }
    public static double getDelay() {
        return delay;
    }
    public static double getDelayms() {
        return delay * 1000;
    }
}
