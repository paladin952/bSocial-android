package com.clpstudio.bsocial.bussiness.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by clapalucian on 5/4/17.
 */

public class IOUtils {
    public static void copyUriToFile(Context context, Uri uri, File file) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            inputStream = context.getContentResolver().openInputStream(uri);
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            copy(inputStream, outputStream);
        } finally {
            closeIgnoringErrors(outputStream);
            closeIgnoringErrors(inputStream);
        }
    }

    public static void closeIgnoringErrors(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
            }
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
    }
}
