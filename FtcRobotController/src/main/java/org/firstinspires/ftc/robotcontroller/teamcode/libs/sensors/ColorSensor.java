package org.firstinspires.ftc.robotcontroller.teamcode.libs.sensors;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This class is a wrapper for the ColorSensor class (check your imports!) and handles a lot of the
 * dirty work for you. It contains many methods, so check each ont to make sure that it's right.
 *
 * @author medude
 */
public class ColorSensor {
    /*--------------------------------------------------------------/
    /  INPUT VARIABLES- deal with how the color sensor reads input  /
    /--------------------------------------------------------------*/
    // There is already a ColorSensor class iplemented; this is a wrapper for it to extend the
    // functionality and make it easier to use.
    private com.qualcomm.robotcore.hardware.ColorSensor colorSensor;

    // Used to access sensor
    private HardwareMap hardwareMap;

    /*--------------------------------------------------------------------/
    /  OUTPUT VARIABLES- deal with how the color sensor outputs its data  /
    /--------------------------------------------------------------------*/
    // The alternative is HSV
    private boolean rgb;

    // This keeps track of if the LED light is on or not.
    private boolean lightOn = true;

    /*----------------------------------------------------------------/
    /  CONSTRUCTION- all constructors and constructor helper methods  /
    /----------------------------------------------------------------*/

    /**
     * This method is called by all constructors to the same work for all of them- instead of
     * editing the constructors, edit here.
     *
     * @param name        This is the name given to the color sensor from the setup.
     * @param hardwareMap This is a refrence to the hardware map.
     * @param rgb         This is if you want the output mode to be in RGB or HSV.
     */
    private void fullConstructor(String name, HardwareMap hardwareMap, boolean rgb) {
        this.hardwareMap = hardwareMap;
        this.colorSensor = hardwareMap.colorSensor.get(name);
        this.rgb = rgb;

        turnOffLight();
    }

    /**
     * This is a constructor- don't edit it, just fullConstructor.
     *
     * @param name        This is the name given to the color sensor from the setup.
     * @param hardwareMap This is a reference to the hardware map.
     * @param rgb         This is if you want the output mode to be in RGB or HSV.
     */
    public ColorSensor(String name, HardwareMap hardwareMap, boolean rgb) {
        fullConstructor(name, hardwareMap, rgb);
    }

    /**
     * This is a constructor- don't edit it, just fullConstructor.
     *
     * @param name This is the name given to the color sensor from the setup.
     * @param hardwareMap This is a refrence to the hardware map.
     */
    public ColorSensor(String name, HardwareMap hardwareMap) {
        fullConstructor(name, hardwareMap, true);
    }

    /**
     * This is a constructor- don't edit it, just fullConstructor.
     *
     * @param hardwareMap This is a refrence to the hardware map.
     * @param rgb This is if you want the output mode to be in RGB or HSV.
     */
    public ColorSensor(HardwareMap hardwareMap, boolean rgb) {
        fullConstructor("colorSensor", hardwareMap, rgb);
    }

    /**
     * This is a constructor- don't edit it, just fullConstructor.
     *
     * @param hardwareMap This is a refrence to the hardware map.
     */
    public ColorSensor(HardwareMap hardwareMap) {
        fullConstructor("colorSensor", hardwareMap, true);
    }

    /*------------------------------------------------------------------------------/
    /  RGB/HSV- deals with changing and receiving info about the RGB or HSV status  /
    /------------------------------------------------------------------------------*/
        public void setRgb() {
            rgb = true;
        }

    public void setRgb(boolean rgb) {
        this.rgb = rgb;
    }

    public void setHsv() {
        rgb = false;
    }

    public void setHsv(boolean hsv) {
        this.rgb = !hsv;
    }

    public boolean isRgb() {
        return rgb;
    }

    public boolean isHsv() {
        return !rgb;
    }

    /*-----------------------------------------------------/
    /  I/O- deals with getting input and providing output  /
    /-----------------------------------------------------*/
        public float[] getColor() {
            float[] results = new float[3];

            if (rgb) {
                results[0] = convertRawToRgb(colorSensor.red());
                results[1] = convertRawToRgb(colorSensor.green());
                results[2] = convertRawToRgb(colorSensor.blue());

                return results;
            } else {
                Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, results);

                return results;
            }
        }

    public float getValue() {
        return getValue(ColorChannel.ALPHA, true);
    }

    public float getValue(ColorChannel channel) {
        return getValue(channel, false);
    }

    public float getValue(ColorChannel channel, boolean average) {
        if (average) {
            float averageValue = 0;
            averageValue += convertRawToRgb(colorSensor.red());
            averageValue += convertRawToRgb(colorSensor.green());
            averageValue += convertRawToRgb(colorSensor.blue());
            averageValue /= 3;

            return averageValue;
        }

        switch (channel) {
            case RED:
                return convertRawToRgb(colorSensor.red());
            case GREEN:
                return convertRawToRgb(colorSensor.green());
            case BLUE:
                return convertRawToRgb(colorSensor.blue());
            default:
                return 0;
        }
    }

    public void turnOnLight() {
        lightOn = true;
        colorSensor.enableLed(lightOn);
    }

    public void turnOffLight() {
        lightOn = false;
        colorSensor.enableLed(lightOn);
    }

    public void setLight(boolean lightOn) {
        this.lightOn = lightOn;
        colorSensor.enableLed(lightOn);
    }

    public void toggleLight() {
        lightOn = !lightOn;
    }

    public boolean isLightOn() {
        return lightOn;
    }

    private float convertRawToRgb(float raw) {
        return (raw * 6.4f) - 1f;
    }
}
