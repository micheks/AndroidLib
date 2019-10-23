package com.alliky.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * 图片选择工具类
 */
public class PhotoUtils {

    public static final int TAKE_PHOTO = 10001;
    public static final int SELECT_PHOTO = 10002;
    public static final int CROP_PHOTO = 10003;

    private Activity mActivity;
    //拍照或剪切后图片的存放位置(参考file_provider_paths.xml中的路径)
    private String imgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
    //FileProvider的主机名：一般是包名+".fileprovider"，严格上是build.gradle中defaultConfig{}中applicationId对应的值+".fileprovider"
    private String AUTHORITIES = "packageName" + ".fileprovider";
    private boolean mShouldCrop = false;//是否要裁剪（默认不裁剪）
    private Uri mOutputUri = null;
    private File mInputFile;
    private File mOutputFile = null;

    //剪裁图片宽高比
    private int mAspectX = 1;
    private int mAspectY = 1;
    //剪裁图片大小
    private int mOutputX = 500;
    private int mOutputY = 500;

    OnSelectListener mListener;

    private static class Holder {
        private static final PhotoUtils instance = new PhotoUtils();
    }

    public static PhotoUtils getInstance() {
        return Holder.instance;
    }

    /**
     * 可指定是否在拍照或从图库选取照片后进行裁剪
     * <p>
     * 默认裁剪比例1:1，宽度为500，高度为500
     *
     * @param shouldCrop 是否裁剪
     * @param activity   上下文
     * @param listener   选取图片监听
     */
    public void init(Activity activity, boolean shouldCrop, OnSelectListener listener) {
        mActivity = activity;
        mListener = listener;
        mShouldCrop = shouldCrop;
        AUTHORITIES = activity.getPackageName() + ".fileprovider";
        imgPath = generateImgePath();
    }

    /**
     * 可以拍照或从图库选取照片后裁剪的比例及宽高
     *
     * @param activity 上下文
     * @param aspectX  图片裁剪时的宽度比例
     * @param aspectY  图片裁剪时的高度比例
     * @param outputX  图片裁剪后的宽度
     * @param outputY  图片裁剪后的高度
     * @param listener 选取图片监听
     */
    public void initParm(Activity activity, int aspectX, int aspectY, int outputX, int outputY, OnSelectListener listener) {
        mActivity = activity;
        mListener = listener;
        mShouldCrop = true;
        AUTHORITIES = activity.getPackageName() + ".fileprovider";
        imgPath = generateImgePath();

        mAspectX = aspectX;
        mAspectY = aspectY;
        mOutputX = outputX;
        mOutputY = outputY;
    }

    /**
     * 设置FileProvider的主机名：一般是包名+".fileprovider"，严格上是build.gradle中defaultConfig{}中applicationId对应的值+".fileprovider"
     * <p>
     * 该工具默认是应用的包名+".fileprovider"，如项目build.gradle中defaultConfig{}中applicationId不是包名，则必须调用此方法对FileProvider的主机名进行设置，否则Android7.0以上使用异常
     *
     * @param authorities FileProvider的主机名
     */
    public void setAuthorities(String authorities) {
        this.AUTHORITIES = authorities;
    }

    /**
     * 修改图片的存储路径（默认的图片存储路径是SD卡上 Android/data/应用包名/时间戳.jpg）
     *
     * @param imgPath 图片的存储路径（包括文件名和后缀）
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    /**
     * 拍照获取
     */
    public void camera() {
        File imgFile = new File(imgPath);
        if (!imgFile.getParentFile().exists()) {
            imgFile.getParentFile().mkdirs();
        }
        Uri imgUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imgUri = FileProvider.getUriForFile(mActivity, AUTHORITIES, imgFile);
        } else {
            imgUri = Uri.fromFile(imgFile);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        mActivity.startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 从图库获取gallery
     */
    public void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(intent, SELECT_PHOTO);
    }

    private void zoomPhoto(File inputFile, File outputFile) {
        File parentFile = outputFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getImageContentUri(mActivity, inputFile), "image/*");
        } else {
            intent.setDataAndType(Uri.fromFile(inputFile), "image/*");
        }

        intent.putExtra("crop", "true");
        //设置剪裁框宽高比
        intent.putExtra("aspectX", mAspectX);
        intent.putExtra("aspectY", mAspectY);
        //设置剪裁图片大小(图片质量的大小)
        intent.putExtra("outputX", mOutputX);
        intent.putExtra("outputY", mOutputY);
        // 是否返回uri
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        mActivity.startActivityForResult(intent, CROP_PHOTO);
    }

    /**
     * @param activity    当前activity
     * @param orgUri      剪裁原图的Uri
     * @param desUri      剪裁后的图片的Uri
     * @param requestCode 剪裁图片的请求码
     */
    public  void cropImageUri(Activity activity, Uri orgUri, Uri desUri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(orgUri, "image/*");
        //发送裁剪信号
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", width);
//        intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        //将剪切的图片保存到目标Uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        //1-false用uri返回图片
        //2-true直接用bitmap返回图片（此种只适用于小图片，返回图片过大会报错）
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, requestCode);
    }


    public void bindForResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO://拍照
                    mInputFile = new File(imgPath);
                    if (mShouldCrop) {
                        mOutputFile = new File(generateImgePath());
                        mOutputUri = Uri.fromFile(mOutputFile);
                        zoomPhoto(mInputFile, mOutputFile);
                    } else {
                        mOutputUri = Uri.fromFile(mInputFile);
                        if (mListener != null) {
                            mListener.onFinish(mInputFile, mOutputUri);
                        }
                    }
                    break;
                case SELECT_PHOTO://图库
                    if (data != null) {
                        Uri uri = data.getData();
                        String imgPath = getPath(mActivity, uri);  // 获取图片路径的方法调用
                        mInputFile = new File(imgPath);

                        if (mShouldCrop) {
                            mOutputFile = new File(generateImgePath());
                            mOutputUri = Uri.fromFile(mOutputFile);
                            zoomPhoto(mInputFile, mOutputFile);
                        } else {
                            mOutputUri = Uri.fromFile(mInputFile);
                            if (mListener != null) {
                                mListener.onFinish(mInputFile, mOutputUri);
                            }
                        }
                    }
                    break;
                case CROP_PHOTO://裁剪
                    if (data != null) {
                        if (mOutputUri != null) {
                            //删除拍照的临时照片
                            File tmpFile = new File(imgPath);
                            if (tmpFile.exists())
                                tmpFile.delete();
                            if (mListener != null) {
                                mListener.onFinish(mOutputFile, mOutputUri);
                            }
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 安卓7.0裁剪根据文件路径获取uri
     */
    private Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 产生图片的路径，带文件夹和文件名，文件名为当前毫秒数
     */
    private String generateImgePath() {
        return getExternalStoragePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
    }


    /**
     * 获取SD下的应用目录
     */
    private String getExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        String ROOT_DIR = "Android/data/" + mActivity.getPackageName();
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }

    public interface OnSelectListener {
        void onFinish(File outputFile, Uri outputUri);
    }

    /**
     * @param context 上下文对象
     * @param uri     当前相册照片的Uri
     * @return 解析后的Uri对应的String
     */
    @SuppressLint("NewApi")
    public  String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String pathHead = "file:///";
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return pathHead + Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }

            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return pathHead + getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return pathHead + getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return pathHead + getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return pathHead + uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private  String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private  boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private  boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private  boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}
