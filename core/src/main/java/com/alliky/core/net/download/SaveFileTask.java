package com.alliky.core.net.download;

import android.os.AsyncTask;

import com.alliky.core.net.callback.ILoading;
import com.alliky.core.net.callback.IRequest;
import com.alliky.core.net.callback.ISuccess;
import com.alliky.core.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Author wxianing
 * date 2018/6/26
 */
final class SaveFileTask  extends AsyncTask<Object, Void, File> {


    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final ILoading ILOADING;

    SaveFileTask(IRequest REQUEST, ISuccess SUCCESS, ILoading ILOADING) {
        this.REQUEST = REQUEST;
        this.SUCCESS = SUCCESS;
        this.ILOADING = ILOADING;
    }

    @Override
    protected File doInBackground(Object... params) {
        String downloadDir = (String) params[0];
        String extension = (String) params[1];
        final ResponseBody body = (ResponseBody) params[2];
        final String name = (String) params[3];

        if (downloadDir == null || downloadDir.equals("")) {
            downloadDir = "down_loads";
        }
        if (extension == null || extension.equals("")) {
            extension = "";
        }

        if (name == null) {
            return writeToDisk(body, downloadDir, extension.toUpperCase(), extension,ILOADING);
        } else {
            return writeToDisk(body, downloadDir, name,ILOADING);
        }

//        return writeToDisk(body, downloadDir, ILOADING);

    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (SUCCESS != null) {
            SUCCESS.onSuccess(file.getPath());
        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }
    }


    public static File writeToDisk(ResponseBody body, String dir, String prefix, String extension, ILoading ILOADING) {
        final File file = FileUtil.createFileByTime(dir, prefix, extension);
        saveToDisk(body, file, ILOADING);
        return file;
    }


    public static File writeToDisk(ResponseBody body, String dir, String name, ILoading ILOADING) {
        final File file = FileUtil.createFile(dir, name);
        saveToDisk(body, file, ILOADING);
        return file;
    }

    public static File writeToDisk(ResponseBody body, String dir, ILoading ILOADING) {
//        final File file = FileUtil.createFile(dir, name);
        final File file = new File(dir);
        saveToDisk(body, file, ILOADING);
        return file;
    }

    private static void saveToDisk(ResponseBody body, File file, ILoading ILOADING) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        InputStream is = null;

        try {
            long total = body.contentLength();
            is = body.byteStream();
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte data[] = new byte[1024 * 4];

            int len;
            long sum = 0;
            while ((len = bis.read(data)) != -1) {
                bos.write(data, 0, len);
                sum += len;
                ILOADING.onLoading(total, sum);
            }

            bos.flush();
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
