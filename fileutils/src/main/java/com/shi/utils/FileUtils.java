package com.shi.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lsq on 2018/2/3.
 */

public class FileUtils {
    /**
     * 下载文件
     *
     * @param downloadUrl
     * @param savePath
     * @return
     */
    public static boolean download(String downloadUrl, String savePath) {
        File file = new File(savePath);
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(0);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read()) != -1) {
                outputStream.write(bytes, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public static File creatFile(String path) {
        File file = createParent(path);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File createParent(String path) {
        File file = new File(path);
        File parent = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)));
        if (!parent.exists()) {
            createParent(parent.getPath());
        }
        return file;
    }

    /**
     * 保存图片文件
     *
     * @param file
     * @param bitmap
     * @return
     */
    public static boolean saveBitmap(File file, Bitmap bitmap) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(bitmap.hasAlpha() ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取文件内容
     *
     * @param file
     * @param encoding
     * @return
     */
    public static String getFileText(File file, String encoding) {
        Long length = file.length();
        byte[] bytes = new byte[length.intValue()];
        try {
            InputStream inputStream = new FileInputStream(file);
            inputStream.read(bytes);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (TextUtils.isEmpty(encoding))
                return new String(bytes, "ISO-8859-1");
            else
                return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字符串输出到文件
     *
     * @param file
     * @param message
     * @param isAppend
     * @return
     */
    public static boolean fileWrite(File file, String message, boolean isAppend) {
        try {
            FileWriter writer = new FileWriter(file, isAppend);
            writer.write(message);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 复制文件
     *
     * @param target
     * @param original
     */
    public static void copyFile(File target, File original) {
        FileOutputStream outputStream = null;
        FileInputStream inputStream = null;
        try {
            outputStream = new FileOutputStream(target);
            inputStream = new FileInputStream(original);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        }
    }

    /**
     * 删除文件or文件夹下所有文件
     *
     * @param file      文件or文件夹
     * @param delParent false:只删除文件夹下所有文件,true:删除文件夹及文件夹下的所有文件
     */
    public static void delFile(File file, boolean delParent) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                delFile(child, true);
            }
        }
        if (delParent) {
            file.delete();
        }
    }
}
