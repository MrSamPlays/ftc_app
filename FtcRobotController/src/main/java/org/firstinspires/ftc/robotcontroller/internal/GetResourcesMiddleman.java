package org.firstinspires.ftc.robotcontroller.internal;

import android.content.res.Resources;

public class GetResourcesMiddleman {
    private static Resources resources;

    public static void setResources(Resources newResources) {
        resources = newResources;
    }

    public static Resources getResources() {
        return resources;
    }
}
