package com.example.android.popularmovies.utility;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by dhaval on 2017/08/31.
 * Image related operations performed here.
 */

public class ImageUtil {

    public static byte[] getByteArrayFromImage(Bitmap bitmap) {
        byte[] image;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        image = bos.toByteArray();

        return image;
    }
}
