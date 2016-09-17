package org.firstinspires.ftc.teamcode.libs.imagenav;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

public class ImageReader {
    private static Resources resources;

    public static void addResources(Resources resources1) {
        resources = resources1;
    }

    public static void loadImage(int image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, image, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
    }
}
