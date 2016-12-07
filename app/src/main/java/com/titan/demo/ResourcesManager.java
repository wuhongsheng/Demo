package com.titan.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

public class ResourcesManager implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String PATH_MAPS = "/maps";
	public static String Code_Path = "";
	private Context context;
	private static String packageName;
	private static String image = "/image";
	private static String otms = "/otms";
	private static String shape = "/shape";
	private static String otitan_map = "/otitan.map";
	private static String excel = "/excel";
	private static String sqlite = "/sqlite";
	private static String lhjj = "/缁垮寲鍩洪噾";

	private static ResourcesManager resourcesManager;

	public synchronized static ResourcesManager getInstance(Context context)
			throws Exception {
		if (resourcesManager == null) {
			resourcesManager = new ResourcesManager(context);
		}
		packageName = context.getPackageName();
		Code_Path = PATH_MAPS + packageName + "/maps";
		return resourcesManager;
	}

	/**
	 * Reset the {@link ResourcesManager}.
	 */
	public static void resetManager() {
		resourcesManager = null;
	}

	private ResourcesManager(Context context) throws Exception {
		this.context = context;
	}

	/**
	 * 鑾峰彇sd鍗″湴鍧�
	 * @return
	 */
	public String[] getPath() {

		StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
		// 
		String[] paths = null;
		try {
			paths = (String[]) sm.getClass().getMethod("getVolumePaths", null).invoke(sm, null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}		
		return paths;
	}

	/**
	 * 鍒涘缓鏂囦欢澶�
	 */
	/*public void createFolder() {
		Util.createFolder(getPath()[1] + PATH_MAPS);
		Util.createFolder(getPath()[1] + PATH_MAPS + excel);
		Util.createFolder(getPath()[1] + PATH_MAPS + otitan_map);
		Util.createFolder(getPath()[1] + PATH_MAPS + otms);
		Util.createFolder(getPath()[1] + PATH_MAPS + otms+lhjj);
		Util.createFolder(getPath()[1] + PATH_MAPS + sqlite);
		Util.createFolder(getPath()[1] + PATH_MAPS + image);
	}
*/
	/**
	 * 鍒犻櫎鏂囦欢
	 */
	public void deleteFile(File file) {

		if (file.exists()) { // 锟叫讹拷锟侥硷拷锟角凤拷锟斤拷锟�
			if (file.isFile()) { // 锟叫讹拷锟角凤拷锟斤拷锟侥硷拷
				file.delete(); // delete()锟斤拷锟斤拷 锟斤拷应锟斤拷知锟斤拷 锟斤拷删锟斤拷锟斤拷锟剿�;
			} else if (file.isDirectory()) { // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟揭伙拷锟侥柯�
				File files[] = file.listFiles(); // 锟斤拷锟斤拷目录锟斤拷锟斤拷锟叫碉拷锟侥硷拷 files[];
				for (int i = 0; i < files.length; i++) { // 锟斤拷锟斤拷目录锟斤拷锟斤拷锟叫碉拷锟侥硷拷
					this.deleteFile(files[i]); // 锟斤拷每锟斤拷锟侥硷拷 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷械锟斤拷
				}
			}
			file.delete();
		}
	}

	/**
	 * 鑾峰彇鏁版嵁鍦板潃
	 * @return
	 */
	public String getDataPath(String path) {
		String dataPath = "";
		for (int i = 0; i < getPath().length; i++) {
			File file = new File(getPath()[i] + PATH_MAPS + path);
			if (file.exists()) {
				dataPath = getPath()[i] + PATH_MAPS + path;
				break;
			}
		}
		return dataPath;
	}
	
	public String getXBDataPath(String path) {
		String dataPath = "";
		for (int i = 0; i < getPath().length; i++) {
			File file = new File(getPath()[i] + PATH_MAPS + path);
			if (file.exists()) {
				dataPath = getPath()[i] + PATH_MAPS + path;
				File[] files = new File(dataPath).listFiles();
				if (files.length > 0) {
					return dataPath;
				}
			}
		}
		return dataPath;
	}

	public String getExcelPath() {
		String excelPath = "";
		excelPath = getDataPath(excel);
		return excelPath;
	}

	public String getArcGISLocalTiledLayerPath() {
		String arcGISLocalTiledLayerPath = "";
		String str = otitan_map + "/gy_xian80.tpk";
		arcGISLocalTiledLayerPath = getDataPath(str);
		return arcGISLocalTiledLayerPath;
	}

	public String getArcGISLocalImageLayerPath() {
		String arcGISLocalTiledLayerPath = "";
		String str = otitan_map + "/image.tpk";
		arcGISLocalTiledLayerPath = getDataPath(str);
		return arcGISLocalTiledLayerPath;
	}

	public List<Map<String, Object>> getGeodatabaseName(String name) {
		String mapPath = otms + lhjj;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		File[] files = new File(getXBDataPath(mapPath)).listFiles();
		for (int i = 0; i < files.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (files[i].getName().endsWith(".otms")) {
				String str = files[i].getName().replace(".otms", "");
				if (str.equals(name)) {
					map.put("cbbox", false);
					map.put("path", files[i].toString());
					map.put("filename", files[i].getName().replace(".otms", ""));
					list.add(map);
					break;
				}
			}
		}
		return list;
	}

	public String getDataBase(Activity ctx, String filename)
			throws FileNotFoundException {
		File db = null;
		db = new File(getDataPath(sqlite), filename);
		if (db.exists()) {
			return db.toString();
		}
		throw new FileNotFoundException("鏂囦欢涓嶅瓨鍦�");
	}

	public boolean saveTxt(Context context, String SBH) {

		String path = "/txt";
		String str = getDataPath(path);
		if (str.equals("")) {
			path = getPath()[1] + PATH_MAPS + "/maps" + path;
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
		}

		String p = str + File.separator + "SBH.txt";
		FileOutputStream outputStream = null;
		try {
			// 锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷写锟斤拷锟斤拷锟斤拷
			outputStream = new FileOutputStream(new File(p));
			String msg = new String("锟借备锟斤拷:" + SBH + "\n");
			outputStream.write(msg.getBytes("UTF-8"));// "UTF-8"
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public void saveSBH(Context context, String SBH, String XLH) {

		File path = Environment.getExternalStorageDirectory();

		File file = new File(path, SBH + ".PUID");
		if (file.exists())
			return;
		FileOutputStream outputStream = null;
		try {
			// 锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷写锟斤拷锟斤拷锟斤拷
			outputStream = new FileOutputStream(file);
			String msg = new String("锟借备锟斤拷:" + SBH + "\n" + "锟斤拷锟叫号ｏ拷" + XLH);
			outputStream.write(msg.getBytes("UTF-8"));// "UTF-8"
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @param folder
	 */
	public File[] getImage(String path) {
		String mapPath = "/image/" + path;
		File[] files = new File(getDataPath(mapPath)).listFiles();
		return files;
	}

	public List<File> getShapeLayer() {
		String mapPath = shape;
		List<File> list = new ArrayList<File>();
		File[] files = new File(getXBDataPath(mapPath)).listFiles();
		int m = files.length;
		for (int i = 0; i < m; i++) {
			if(files[i].getName().endsWith(".shp")){
				list.add(files[i]);
			}
		}
		return list;
	}
}
