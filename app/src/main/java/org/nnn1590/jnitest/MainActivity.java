package org.nnn1590.jnitest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity {

	private Bundle dialogBundle = new Bundle();
	private native int addNative(int x, int y);
	private static native int addStaticNative(int x, int y);
	private int add(int x, int y) { return x + y; }
	private static int addStatic(int x, int y) { return x + y; }
	private static final int DIALOG_DEBUG = 1;
	private static final int DIALOG_FAILED_TO_LOAD_LIBRARY = 2;
	private static int[] adds(int x, int[] y) {
		int[] result = new int[y.length];
		for (int i = 0; i < y.length; i++) result[i] = x + y[i];
		return result;
	}
	private static native int[] addsNative(int x, int[] y);
	private static String errorLibraryName = null;
	private static String errorLibraryMessage = null;

	static {
		String libraryName = (BuildCompat.getSDKVersion() >= BuildCompat.VERSION_CODES.LOLLIPOP) ? "addnativev21" : "addnativev1";

		try {
			System.loadLibrary(libraryName);
		} catch (Exception|UnsatisfiedLinkError e) {
			errorLibraryName = libraryName;
			errorLibraryMessage = e.getMessage();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (errorLibraryName != null) {
			if (errorLibraryMessage != null) Log.e(getString(R.string.app_name), errorLibraryMessage);
			dialogBundle = new Bundle();
			dialogBundle.putString("libraryName", errorLibraryName);
			if (BuildCompat.getSDKVersion() >= BuildCompat.VERSION_CODES.FROYO) {
				Bundle bundle = new Bundle();
				bundle.putString("libraryName", errorLibraryName);

				//showDialog(DIALOG_FAILED_TO_LOAD_LIBRARY, bundle);
				try {
					getClass().getMethod("showDialog", int.class, Bundle.class).invoke(this, DIALOG_FAILED_TO_LOAD_LIBRARY, bundle);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				showDialog(DIALOG_FAILED_TO_LOAD_LIBRARY);
			}
		}

		findViewById(R.id.button).setOnClickListener(view -> {
			int[] array = new int[1_1_4_5_1_4];
			for (int i = 0; i < array.length; i++) array[i] = new Random().nextInt(1__1__4__5__1__4);

			long startTime = System.currentTimeMillis();
			String resultJava = Arrays.toString(adds(2, array));
			long endJavaTime = System.currentTimeMillis();
			String resultNative = Arrays.toString(addsNative(2, array));
			long endNativeTime = System.currentTimeMillis();

			Boolean isResultSame = (resultJava.equals(resultNative));
			((Button)view).setText(String.format(Locale.ENGLISH, "Java: %dms, Native: %dms, isResultSame: %b", endJavaTime - startTime, endNativeTime - endJavaTime, isResultSame));
		});
		findViewById(R.id.checkBox).setOnClickListener(view -> Toast.makeText(this, String.format(Locale.ENGLISH, "2 + 3 = %d", addNative(2, 3)), Toast.LENGTH_SHORT).show());
		findViewById(R.id.textView2).setOnClickListener(view -> {
			long startTime = System.currentTimeMillis();
			for (int i = 1; i < 114514 * 19; i++) add(810, i);
			long endJavaTime = System.currentTimeMillis();
			for (int i = 1; i < 114514 * 19; i++) addStatic(810, i);
			long endJavaStaticTime = System.currentTimeMillis();
			for (int i = 1; i < 114514 * 19; i++) addNative(810, i);
			long endNativeTime = System.currentTimeMillis();
			for (int i = 1; i < 114514 * 19; i++) addStaticNative(810, i);
			long endStaticNativeTime = System.currentTimeMillis();
			((TextView)view).setText(String.format(Locale.ENGLISH, "Java(non-static): %dms, Java(static): %dms, Native(non-static): %dms, Native(static): %dm",
					endJavaTime - startTime, endJavaStaticTime - endJavaTime, endNativeTime - endJavaStaticTime, endStaticNativeTime - endNativeTime));
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return getDialog(id, dialogBundle);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		return getDialog(id, args);
	}

	private Dialog getDialog(int id, Bundle args) {
		switch (id) {
			case (DIALOG_DEBUG):
				return new AlertDialog.Builder(this)
						.setTitle(R.string.debug)
						.setCancelable(true)
						.setPositiveButton(R.string.ok, null)
						.create();
			case (DIALOG_FAILED_TO_LOAD_LIBRARY):
				AlertDialog.Builder builder = new AlertDialog.Builder(this)
						.setTitle(R.string.error)
						.setMessage(R.string.failed_to_load_library)
						.setCancelable(false)
						.setOnCancelListener(dialogInterface -> this.finish())
						.setPositiveButton(R.string.ok, (dialogInterface, i) -> this.finish());
				if (args != null && args.containsKey("libraryName"))
					builder.setMessage(String.format(Locale.getDefault(), getString(R.string.failed_to_load_library_with_library_name), args.getString("libraryName")));
				return builder.create();
			default:
				return null;
		}
	}
}
