package com.hyipc.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("NewApi")
public class FileUtilsPi {
	private static final String TAG = "FileUtilsPi";
	
	public static String createFolder(String folderName) {
		File appDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				folderName);
		if (!appDir.exists()) {
			appDir.mkdir();
		}

		return appDir.getAbsolutePath();
	}
	
	public static String createFolder2(String path) {
		File appDir = new File(path);
		if (!appDir.exists()) {
			appDir.mkdir();
		}

		return appDir.getAbsolutePath();
	}
	
	public static String createFile(String folderName) {
		File appDir = new File(folderName);
		if (!appDir.exists()) {
			try {
				appDir.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return appDir.getAbsolutePath();
	}

	public static boolean externalMemoryAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}
	
	public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }
	
	public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

	public static String getFilePrefix() {
		return "";
	}

	public static List<String> getFileList(String folder, String filter) {
		File fc = null;
		List<String> fileList = new ArrayList<String>();

		try {
			fc = new File(folder);

			if(!fc.exists()){
				return fileList;
			}
			
			if (!fc.isDirectory()) {
				return fileList;
			}

			File[] fileArray = fc.listFiles();
			for (File f : fileArray) {
				if (f.getAbsolutePath().endsWith(filter) || (filter == null)) {
					fileList.add(f.getAbsolutePath());
				}
			}

		} catch (Exception ioe) {
			ioe.printStackTrace();
		}

		return fileList;
	}

	public static void removeFile(String absFileName) throws Exception {
		File fc = null;

		try {
			fc = new File(absFileName);

			if (fc.isFile()) {
				fc.delete();
				return;
			}

			if (fc.isDirectory()) {
				File[] childFile = fc.listFiles();
				if (childFile == null || childFile.length == 0) {
					fc.delete();
					return;
				}
				for (File f : childFile) {
					f.delete();
				}
				fc.delete();
			}
		} catch (Exception ioe) {
			throw new Exception("delete file fail" + ioe.getMessage() + " abs:"
					+ absFileName);
		}
	}
	
	public static final String TENCENT_LOG = "/sdcard/tencent/imsdklogs/com/ajx/app/device/";
	public static void removeTencentLog() throws Exception {
		File fc = null;

		try {
			fc = new File(TENCENT_LOG);
			if (fc.isDirectory()) {
				File[] childFile = fc.listFiles();
				if (childFile == null || childFile.length == 0) {
					fc.delete();
					return;
				}
				for (int i = 0;i<childFile.length;i++) {
					if(i == (childFile.length-1)){
						return;
					}
					File f = childFile[i];
					f.delete();
				}
			}
		} catch (Exception ioe) {
			throw new Exception("delete TencentLog fail" + ioe.getMessage());
		}
	}
 
	public static void removeFile(String absFileName,String suffix) throws Exception {
		File fc = null;

		try {
			fc = new File(absFileName);
			if (fc.isDirectory()) {
				File[] childFile = fc.listFiles();
				for (File f : childFile) {
					if (f.getAbsolutePath().endsWith(suffix)) {
						f.delete();
					}
				}
			}
		} catch (Exception ioe) {
			throw new Exception("delete file fail" + ioe.getMessage() + " abs:"
					+ absFileName);
		}
	}

	public final static String JPG_SUFFIX = ".jpg";
	public final static String PNG_SUFFIX = ".png";
	public static void saveFile(String path, String fileName, byte[] bData) {
		Bitmap bm = BitmapFactory.decodeByteArray(bData, 0, bData.length);
		File f = new File(path, fileName);

		// this.deleteFile(TEMP_IMG_FILE_NAME);

		// File f = new File(this.getFilesDir(),TEMP_IMG_FILE_NAME);

		try {
			f.deleteOnExit();

			f.createNewFile();

			// BufferedOutputStream bos = new
			// BufferedOutputStream(this.openFileOutput(TEMP_IMG_FILE_NAME,
			// Context.MODE_PRIVATE));
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(f));

			//bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			//bm.compress(Bitmap.CompressFormat.JPEG, 70, bos);
			
			if(fileName.endsWith(JPG_SUFFIX)){
				bm.compress(Bitmap.CompressFormat.JPEG, 70, bos);
			
			}else if(fileName.endsWith(PNG_SUFFIX)){
				bm.compress(Bitmap.CompressFormat.PNG, 70, bos);
			}

			bos.flush();

			bos.close();

			fileName = f.getAbsolutePath();
			Log.d(TAG, "saveFile succ " + f.getAbsolutePath());

			Log.d(TAG,"file size:" + f.length());

			f = new File(fileName);

			Log.d(TAG, "f2 size:" + f.length());
			
			bm.recycle();
			
			bm = null;
		} catch (IOException e) {
			e.printStackTrace();
			fileName = null;
		}
	}

	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
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
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	public static synchronized void CreateDirectory(String absDirName)
			throws Exception {
		File fc = null;

		try {
			fc = new File(absDirName);
			if (!fc.exists()) {
				fc.mkdir();
				Log.d(TAG,"create new Directory:" + absDirName);
			}
		} catch (Exception e) {
			throw new Exception("Create Directory Fail:" + e.getMessage()
					+ " abs:" + absDirName);
		}
	}

	public static synchronized void CreateFile(String absDirName)
			throws Exception {
		File fc = null;
		try {
			fc = new File(absDirName);
			if (!fc.exists()) {
				fc.createNewFile();
				Log.d(TAG,"create new file:" + absDirName);
			}
		} catch (Exception e) {
			throw new Exception("Create file Fail:" + e.getMessage() + " abs:"
					+ absDirName);
		}
	}
	
	public static synchronized void removeDir(String dir){
		List<String> fileList = getFileList(dir,".jpg");
		
		for(String fileName : fileList){
			try {
				RemoveFile(fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static synchronized void RemoveFile(String absFileName)
			throws Exception {
		File fc = null;

		try {
			fc = new File(absFileName);

			if (fc.isFile()) {
				fc.delete();
				return;
			}

			if (fc.isDirectory()) {
				File[] childFile = fc.listFiles();
				if (childFile == null || childFile.length == 0) {
					fc.delete();
					return;
				}
				for (File f : childFile) {
					f.delete();
				}
				fc.delete();
			}
		} catch (Exception ioe) {
			throw new Exception("delete file fail" + ioe.getMessage() + " abs:"
					+ absFileName);
		}
	}


	public static synchronized void RemoveFileInTimeout(String absFileName,long deleteTimeoutInMs)
			throws Exception {
		File fc = null;

		long curremtMs = System.currentTimeMillis();

		try {
			fc = new File(absFileName);

			if (fc.isFile()) {
				long modifyMs = fc.lastModified();

				if(curremtMs - modifyMs >= deleteTimeoutInMs){
					fc.delete();
				}
				return;
			}

			if (fc.isDirectory()) {
				File[] childFile = fc.listFiles();
				if (childFile == null || childFile.length == 0) {
					fc.delete();
					return;
				}
				for (File f : childFile) {
					long lastModifyMs = f.lastModified();
					if(curremtMs - lastModifyMs >= deleteTimeoutInMs){
						f.delete();
					}
				}
			}
		} catch (Exception ioe) {
			throw new Exception("delete file fail" + ioe.getMessage() + " abs:"
					+ absFileName);
		}
	}

	public static synchronized List<String> GetFilePath(String absFileName)
			throws Exception {
		File fc = null;
		List<String> fileList = new ArrayList<String>();
		try {
			fc = new File(absFileName);
			File[] fileArray = fc.listFiles();
			for (File f : fileArray) {
				if (f.getAbsolutePath().endsWith(".mp4")) {
					fileList.add(f.getAbsolutePath());
				}
			}

		} catch (Exception ioe) {
		}
		return fileList;
	}

	public static synchronized List<Bitmap> GetImageADList(String absFileName)
			throws Exception {
		File fc = null;
		List<Bitmap> BitmapList = new ArrayList<Bitmap>();
		try {
			fc = new File(absFileName);
			File[] fileArray = fc.listFiles();
			for (File f : fileArray) {
				if (f.getAbsolutePath().endsWith(".jpg")) {
					FileInputStream fis = new FileInputStream(
							f.getAbsolutePath());
					Bitmap bitmap = BitmapFactory.decodeStream(fis);
					BitmapList.add(bitmap);
				}
			}

		} catch (Exception ioe) {
		}
		return BitmapList;
	}
	
	public static synchronized Bitmap getLocalBitmap(String absFileName) throws Exception {
		Bitmap bitmap = null;
		try {
			FileInputStream fis = new FileInputStream(absFileName);
			bitmap = BitmapFactory.decodeStream(fis);

		} catch (Exception ioe) {
		}
		return bitmap;
	}
	

	public static synchronized Bitmap GetImageAD(String absFileName,
			String adname) throws Exception {
		File fc = null;
		Bitmap bitmap = null;
		try {
			fc = new File(absFileName);
			File[] fileArray = fc.listFiles();
			for (File f : fileArray) {
				if (f.getAbsolutePath().endsWith(adname)) {
					FileInputStream fis = new FileInputStream(
							f.getAbsolutePath());
					bitmap = BitmapFactory.decodeStream(fis);
				}
			}

		} catch (Exception ioe) {
		}
		return bitmap;
	}
	
	public static boolean exists(String path){
		File f = new File(path);
		if(f.exists()){
			return true;
		}
		return false;
	}

	public static synchronized byte[] readFile(String absFileName)
			throws Exception {
		File fc = null;
		FileInputStream is = null;
		int iFileSize;
		int iRead;
		int iOffset;
		// open file
		try {
			fc = new File(absFileName);

			if (!fc.exists()) {
				throw new Exception("File not exist");
			}
			is = new FileInputStream(fc);
			iFileSize = is.available();

			if (iFileSize <= 0) {
				is.close();

				if (iFileSize == 0) {
					return new byte[1];
				}

				throw new Exception("File Size smaller than zero");
			}

		} catch (IOException ioe) {
			throw new Exception("open file fail" + ioe.getMessage() + " abs:"
					+ absFileName);
		}

		byte[] bData = new byte[iFileSize];

		try {
			iOffset = 0;

			while (iFileSize - iOffset > 0) {
				iRead = is.read(bData, iOffset, iFileSize - iOffset);

				if (iRead >= 0) {
					iOffset = iOffset + iRead;
				} else {
					throw new Exception("read file imcomplete");
				}
			}

			is.close();

		} catch (IOException ioe) {
			throw new Exception("read file fail:" + ioe.getMessage());
		}

		return bData;
	}
	
	public static synchronized String readStringFile(String absFileName){
		try {
			byte[] bData = readFile(absFileName);
			
			if(bData == null){
				return null;
			}
			if(bData.length == 1){
				return null;
			}
			return new String(bData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized void AppendLogFile(String absFileName,
			byte[] bContent) {
		/*
		 * try{ FileOperator.AppendFile(absFileName, bContent); }catch(Exception
		 * huayuE){}
		 */
	}

	public static synchronized void AppendFile(String absFileName,
			byte[] bContent, int iWrite) throws Exception {
		File fc = null;
		FileOutputStream os = null;

		// open file
		try {
			fc = new File(absFileName);

			if (!fc.exists()) {
				// fc.create();
			}

			os = new FileOutputStream(fc);
		} catch (IOException ioe) {
			throw new Exception("open file fail" + ioe.getMessage() + " abs:"
					+ absFileName);
		}

		// write file
		try {
			os.write(bContent, 0, iWrite);
		} catch (IOException ioe) {
			if (os != null) {
				try {
					os.close();
				} catch (IOException ioe2) {
				}
			}
			throw new Exception("write file fail:" + ioe.getMessage());
		}

		// close file
		try {
			os.close();
		} catch (IOException ioe) {
		}
	}

	public static synchronized void writeFile(String absFileName,
			byte[] bContent,boolean bAppend) throws Exception {
		File fc = null;
		FileOutputStream os = null;

		// open file
		try {
			fc = new File(absFileName);

			if (!fc.exists()) {
				//fc.createNewFile();
			}

			os = new FileOutputStream(fc,bAppend);
		} catch (IOException ioe) {
			throw new Exception("open file fail" + ioe.getMessage() + " abs:"
					+ absFileName);
		}

		// write file
		try {
			os.write(bContent);
		} catch (IOException ioe) {
			if (os != null) {
				try {
					os.close();
				} catch (IOException ioe2) {
				}
			}
			throw new Exception("write file fail:" + ioe.getMessage());
		}

		// close file
		try {
			os.close();
		} catch (IOException ioe) {
		}
	}
	
	public static synchronized void saveStringFile(String absFileName,String text,boolean bAppend) throws Exception{
		writeFile(absFileName,text.getBytes(),bAppend);
	}

	public static String readTxtFile(String strFilePath) {
		String path = strFilePath;
		String content = ""; 
		File file = new File(path);
		
		if(!file.exists()){
			return null;
		}
		try {
			InputStream instream = new FileInputStream(file);
			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				while ((line = buffreader.readLine()) != null) {
					content += line + "\n";
				}
				instream.close();
			}
		} catch (Exception e) {

		}
		return content;
	}
	
	public static String saveBitmap(String folder,Bitmap bmp){
		if((bmp == null) || (folder == null)){
			return null;
		}
		
		File folderFile = new File(folder);
		if(!folderFile.exists()){
			return null;
		}
		
		String fileName = System.currentTimeMillis()+".jpg";
		
		File f = new File(folderFile,fileName);
		if(f.exists()){
			f.delete();
		}
		
		try{
			FileOutputStream out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.JPEG, 70, out);
			out.flush();
			out.close();
		}catch(IOException e){
			return null;
		}
		
		return folder+"/"+fileName;
	}
	
	public static List<String> savePictureList(String folder,Bitmap bmp,Rect[] rcList){
		if((rcList == null) || (bmp == null) || (folder == null)){
			return null;
		}
		
		File folderFile = new File(folder);
		if(!folderFile.exists()){
			return null;
		}
		
		List<String> fileList = new ArrayList<String>();
		
		for(Rect rc : rcList){							
			Bitmap bitmap = Bitmap.createBitmap(bmp, rc.left, rc.top, rc.width(), rc.height());
			
			Bitmap mBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

			final String fileName = System.currentTimeMillis() + ".jpg";
			
			File f = new File(folder,fileName);
			if (f.exists()) {
				f.delete();
			}
			try {
				FileOutputStream out = new FileOutputStream(f);
				mBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			
			fileList.add(folder+"/"+fileName);
		}
		
		return fileList;
	}
	
	public static void superaddition(String file, String conent) {  
		File fc = new File(file);
		if (!fc.exists()) {
			 try {
				fc.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
        BufferedWriter out = null;  
        try {  
            out = new BufferedWriter(new OutputStreamWriter(  
                    new FileOutputStream(file, true)));  
            out.write(conent);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                out.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    } 
	
	public static boolean isStrArrayEqual(List<String> aList,List<String> bList){
		HashSet<String> htListA = new HashSet<String>();
		
		if((aList == null) || (bList == null)){
			return false;
		}
		
		if(aList.size() != bList.size()){
			return false;
		}
		
		for(String str : aList){
			htListA.add(str);
		}
		
		for(String str : bList){
			if(htListA.contains(str)){
				Log.d(TAG,"ok:"+str);
			}else{
				Log.e(TAG,"fail:"+str);
				return false;
			}
		}
		
		HashSet<String> htListB = new HashSet<String>();
		
		for(String str : bList){
			htListB.add(str);
		}
		
		for(String str : aList){
			if(htListB.contains(str)){
				Log.d(TAG,"ok:"+str);
			}else{
				Log.e(TAG,"fail:"+str);
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean matchNumber(String number){
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(number);
		
		return m.matches();
	}
	
	public static void moveFile(String srcFileName,String destFileName){
		File fileSrc = new File(srcFileName);
		File fileDest = new File(destFileName);
		
		if(!fileSrc.exists()){
			return;
		}
		if(!fileSrc.isFile()){
			return;
		}
		if(fileDest.exists()){
			fileDest.deleteOnExit();
		}
		
		InputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(fileSrc);
			fos = new FileOutputStream(fileDest);
			
			byte[] buffer = new byte[1024];
			int byteRead = 0;
			
			while((byteRead = fis.read(buffer)) != -1){
				fos.write(buffer,0,byteRead);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
	}
}
