package org.firstinspires.ftc.robotcontroller.teamcode.libs.robot;

import java.util.ArrayList;
import java.util.TimerTask;

public abstract class EventDoer extends TimerTask {
    private ArrayList<EventHandler> handlers = new ArrayList<EventHandler>();
    private boolean triggered;

    public void addHandler(EventHandler handler) {
        handlers.add(handler);
    }

    public abstract boolean testEvent();

    public void run() {
        if (testEvent()) {
            handleEvent();
            cancel();
        }
    }

    public boolean isTriggered() {
        return triggered;
    }

    protected void handleEvent() {
        triggered = true;

        for (EventHandler handler : handlers) {
            handler.handleEvent();
        }
    }
}