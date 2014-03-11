package com.sunny.zipper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author Sahitya
 * 
 */
public class Zipper {

	/**
	 * Input Source file to Zip and Destination to save.
	 * 
	 * 
	 * @param srcFolder
	 * @param destZipFile
	 * @throws IOException
	 * 
	 */
	public boolean zipFolder(String srcFolder, String destZipFile,
			int splitVolumeSize) {

		boolean result = true;
		try {
			ZipOutputStream zip = null;
			FileOutputStream fileWriter = null;

			fileWriter = new FileOutputStream(destZipFile);

			zip = new ZipOutputStream(fileWriter);
			addFolderToZip("", srcFolder, zip);
			zip.flush();
			zip.close();
			splitFile(destZipFile, Environment.getExternalStorageDirectory()
					+ File.separator, splitVolumeSize);
			new File(destZipFile).delete();
		} catch (FileNotFoundException e) {
			result = false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	private void addFolderToZip(String path, String srcFolder,
			ZipOutputStream zip) throws IOException {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/"
						+ fileName, zip);
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @param srcFile
	 * @param zip
	 * @throws IOException
	 */
	private boolean addFileToZip(String path, String srcFile,
			ZipOutputStream zip) {
		boolean result = true;
		try {
			File folder = new File(srcFile);
			if (folder.isDirectory()) {
				printLog("GOING TO SUB FOLDER " + folder);

				addFolderToZip(path, srcFile, zip);

			} else {
				printLog("CREATING ZIP FILE");
				byte[] buf = new byte[1024];
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * <p>
	 * Checks for file size in Bytes, Kb, Mb, Gb. If file name is <b>{@code}null
	 * or empty</b> ,method returns 0
	 * </p>
	 * <br>
	 * If all are false default is bytes.</br>
	 * 
	 * 
	 * @param file
	 * @param inbytes
	 * @param inKb
	 * @param inMb
	 * @param inGb
	 * @return
	 */
	public double checkFileSize(String file, boolean inbytes, boolean inKb,
			boolean inMb, boolean inGb) {
		if (file == null || file.trim().length() == 0)
			return 0;

		File sourceFile = new File(file);

		double bytes = sourceFile.length();
		double kilobytes = (bytes / 1024);
		double megabytes = (kilobytes / 1024);
		double gigabytes = (megabytes / 1024);

		if (inKb)
			return kilobytes;
		else if (inMb)
			return megabytes;
		else if (inGb)
			return gigabytes;
		else
			return bytes;
	}

	/**
	 * Default part size is 1 MB
	 * 
	 * 
	 * @param sourceFolder
	 * @param destination
	 * @param partZize
	 * @throws IOException
	 */
	public void splitFile(String sourceFolder, String destination, int partZize)
			throws IOException {
		boolean result = true;
		printLog("NOW SPLITTING FILE");
		BufferedInputStream bis;
		bis = new BufferedInputStream(new FileInputStream(sourceFolder));

		FileOutputStream out;

		File f = new File(sourceFolder);
		String name = f.getName()+".yrf";
		int partCounter = 0;

		if (partZize == 0) {
			partCounter = 1;
		}
		int sizeOfFiles = (1024 * 1024) * partZize;
		byte[] buffer = new byte[sizeOfFiles];
		int tmp = 0;
		while ((tmp = bis.read(buffer)) > 0) {
			File newFile = new File(destination + File.separator + name + "."
					+ String.format("%03d", partCounter++));
			printLog("CREATING SPLIT FILE " + newFile.getName());
			newFile.createNewFile();
			out = new FileOutputStream(newFile);
			out.write(buffer, 0, tmp);
			out.close();
		}
		bis.close();

	}

	private void printLog(String message) {
		Log.i(getClass().getSimpleName(), "--------" + message);
	}

}
