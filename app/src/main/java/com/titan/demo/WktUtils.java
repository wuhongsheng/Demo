package com.titan.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.google.gson.Gson;


public class WktUtils {

	/**
	 * 点 转换 JSON
	 * 
	 * @param wkt
	 * @param wkid
	 * @return
	 */
	public String getPOINTWktToJson(String wkt, int wkid) {

		String[] strHead = wkt.split("\\(");
		String strContent = strHead[1].substring(0, strHead[1].length() - 1);
		String[] strResult = strContent.split(" ");

		PointObject pointObject = new PointObject();
		pointObject.setX(Double.parseDouble(strResult[0]));
		pointObject.setY(Double.parseDouble(strResult[1]));

		HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
		spatialReference.put("wkid", wkid);

		pointObject.setSpatialReference(spatialReference);

		Gson gson = new Gson();

		return gson.toJson(pointObject);

	}

	/**
	 * 多点 转换 JSON
	 * 
	 * @param wkt
	 * @param wkid
	 * @return
	 */
	public String getMULTIPOINTWktToJson(String wkt, int wkid) {

		MultiIPointObject multiIPointObject = new MultiIPointObject();

		String ToTailWkt = wkt.substring(0, wkt.length() - 1);
		String[] strHead = ToTailWkt.split("\\(\\(");
		String strMiddle = strHead[1].substring(0, strHead[1].length() - 1);
		String[] strMiddles = strMiddle.split(",");

		List<Double[]> list = new ArrayList<Double[]>();

		for (int i = 0; i < strMiddles.length; i++) {

			if (i == 0) {

				String item = strMiddles[i].substring(0,
						strMiddles[i].length() - 1);
				String[] items = item.split(" ");
				Double[] listResult = new Double[] {
						Double.parseDouble(items[0]),
						Double.parseDouble(items[1]) };

				list.add(listResult);

			} else if (i == strMiddles.length) {

				String item = strMiddles[i]
						.substring(1, strMiddles[i].length());
				String[] items = item.split(" ");
				Double[] listResult = new Double[] {
						Double.parseDouble(items[0]),
						Double.parseDouble(items[1]) };

				list.add(listResult);

			} else {

				String strItem = strMiddles[i].trim();
				String item = strItem.substring(1, strItem.length() - 1);
				String[] items = item.split(" ");
				Double[] listResult = new Double[] {
						Double.parseDouble(items[0]),
						Double.parseDouble(items[1]) };

				list.add(listResult);

			}

		}

		HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
		spatialReference.put("wkid", wkid);

		multiIPointObject.setPoints(list);
		multiIPointObject.setSpatialReference(spatialReference);

		Gson gson = new Gson();

		return gson.toJson(multiIPointObject);

	}

	/**
	 * 线 转换 JSON
	 * 
	 * @param wkt
	 * @param wkid
	 * @return
	 */
	public String getLINESTRINGWktToJson(String wkt, int wkid) {

		LineStringObject lineStringObject = new LineStringObject();

		List<List<Double[]>> lists = new ArrayList<List<Double[]>>();
		List<Double[]> list = new ArrayList<Double[]>();

		String[] strHead = wkt.split("\\(");
		String strContent = strHead[1].substring(0, strHead[1].length() - 1);
		String[] strResult = strContent.split(",");

		for (int i = 0; i < strResult.length; i++) {

			String itme = strResult[i].trim();
			String[] items = itme.split(" ");
			Double[] listResult = new Double[] { Double.parseDouble(items[0]),
					Double.parseDouble(items[1]) };
			list.add(listResult);

		}

		lists.add(list);

		HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
		spatialReference.put("wkid", wkid);

		lineStringObject.setPaths(lists);
		lineStringObject.setSpatialReference(spatialReference);

		Gson gson = new Gson();

		return gson.toJson(lineStringObject);

	}

	/**
	 * 多线 转换 JSON
	 * 
	 * @param wkt
	 * @param wkid
	 * @return
	 */
	public String getMULTILINESTRINGWktToJson(String wkt, int wkid) {

		MultLinesStringObject lineStringObject = new MultLinesStringObject();

		List<List<Double[]>> lists = new ArrayList<List<Double[]>>();

		String ToTailWkt = wkt.substring(0, wkt.length() - 1);
		String[] strHead = ToTailWkt.split("\\(", 2);

		String[] strList = strHead[1].split("\\),\\(");

		for (int i = 0; i < strList.length; i++) {

			String item = strList[i].trim();
			item = item.substring(1, item.length() - 1);
			String[] items = item.split(",");

			List<Double[]> list = new ArrayList<Double[]>();

			for (int j = 0; j < items.length; j++) {

				String jItem = items[j].trim();
				String[] jItems = jItem.split(" ");

				Double[] listResult = new Double[] {
						Double.parseDouble(jItems[0]),
						Double.parseDouble(jItems[1]) };

				list.add(listResult);

			}

			lists.add(list);

		}

		HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
		spatialReference.put("wkid", wkid);

		lineStringObject.setRings(lists);
		lineStringObject.setSpatialReference(spatialReference);

		Gson gson = new Gson();

		return gson.toJson(lineStringObject);

	}

	public String getPOLYGONWktToJson(String wkt, int wkid) {

		PolygonObject polygonObject = new PolygonObject();

		List<List<Double[]>> lists = new ArrayList<List<Double[]>>();

		String ToTailWkt = wkt.substring(0, wkt.length() - 1);
		String[] strHead = ToTailWkt.split("\\(", 2);

		String[] strList = strHead[1].split("\\), \\(");

		for (int i = 0; i < strList.length; i++) {

			String item = strList[i].trim();
			item = item.substring(1, item.length() - 1);
			String[] items = item.split(",");
           /* items[0].replace("(", "");
            items[item.length() - 1].replace(")", "");*/
			List<Double[]> list = new ArrayList<Double[]>();

			for (int j = 0; j < items.length; j++) {

				String jItem = items[j].trim();
				String[] jItems = jItem.split(" ");

				Double[] listResult = new Double[] {
						Double.parseDouble(jItems[0]),
						Double.parseDouble(jItems[1]) };

				list.add(listResult);

			}

			lists.add(list);

		}

		HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
		spatialReference.put("wkid", wkid);

		polygonObject.setRings(lists);
		polygonObject.setSpatialReference(spatialReference);

		Gson gson = new Gson();

		return gson.toJson(polygonObject);
	}

	public String getMULTIPOLYGONWktToJson(String wkt, int wkid) {

		PolygonObject polygonObject = new PolygonObject();

		List<List<Double[]>> lists = new ArrayList<List<Double[]>>();

		String ToTailWkt = wkt.substring(0, wkt.length() - 1);
		String[] strHead = ToTailWkt.split("\\(", 2);
		ToTailWkt = strHead[1].substring(0, strHead[1].length() - 1);
		String[] strHeads = ToTailWkt.split("\\(", 2);

		String[] strList = strHeads[1].split("\\), \\(");

		if (strList.length == 1) {

			for (int i = 0; i < strList.length; i++) {

				String item = strList[i].trim();
				item = item.substring(1, item.length() - 1);
				String[] items = item.split(",");

				List<Double[]> list = new ArrayList<Double[]>();

				for (int j = 0; j < items.length; j++) {
					String jItem = items[j].trim();
					String[] jItems = jItem.split(" ");

					Double[] listResult = new Double[] {
							Double.parseDouble(jItems[0]),
							Double.parseDouble(jItems[1]) };

					list.add(listResult);

				}

				lists.add(list);

			}

		} else {

			for (int i = 0; i < strList.length; i++) {

				String item = strList[i].trim();
				item = item.substring(1, item.length() - 1);
				String[] items = item.split(",");

				List<Double[]> list = new ArrayList<Double[]>();

				for (int j = 1; j < items.length; j++) {
					String jItem = items[j].trim();
					String[] jItems = jItem.split(" ");

					Double[] listResult = new Double[] {
							Double.parseDouble(jItems[0]),
							Double.parseDouble(jItems[1]) };

					list.add(listResult);

				}

				lists.add(list);

			}

		}

		HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
		spatialReference.put("wkid", wkid);

		polygonObject.setRings(lists);
		polygonObject.setSpatialReference(spatialReference);

		Gson gson = new Gson();

		return gson.toJson(polygonObject);

	}

	 /**  
     *   JSON to WKT     
     */ 
	 public static String GeometryJOSNToWKT(String json) {
		 
		 try {
			JSONObject jsonObject=new JSONObject(json);
			String type=jsonObject.numberToString(0);
			String value=jsonObject.getString(type);
			String v2=value.replaceAll("[", "(");
			String v3=v2.replace("],", ",");
			String result=type+v3;
			return result;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
 
	}
	 /**  
     * 将几何对象生成wkt字符串      
     */  
    public static String GeometryToWKT(Geometry geometry){        
        if(geometry == null){  
            return null;  
        }  
        String geoStr = "";  
        Geometry.Type type = geometry.getType();  
        //spatialite 支持类型与arcgis不一致需要转换
     
        if("Point".equals(type.name())||"POINT".equals(type.name())){              
            Point pt = (Point)geometry;  
            String px = String.format("%.6f", pt.getX());
            String py = String.format("%.6f", pt.getY());
          /*  double px=Double.valueOf(new java.text.DecimalFormat("#.0000").format(pt.getX()));
            double py=Double.valueOf(new java.text.DecimalFormat("#.000000").format(pt.getY()));*/
            geoStr = type.name()+"("+px +" "+py+")";            
        }else if("POLYGON".equals(type.name()) || "POLYLINE".equals(type.name())){  
            MultiPath pg = (MultiPath)geometry;
            if("POLYLINE".equals(type.name()))
            {
            	geoStr="LINESTRING"; 
            }else {
            	geoStr = type.name()+"("+"";  
			}
           int pathSize = pg.getPathCount();  
            for(int j=0;j<pathSize;j++){  
                String temp = "(";  
                int size = pg.getPathSize(j);                 
                for(int i=0;i<size;i++){                   
                    Point pt = pg.getPoint(i);
                    String px = String.format("%.6f", pt.getX());
                    String py = String.format("%.6f", pt.getY());
                    temp +=px +" "+py+",";  
                }  
                temp = temp.substring(0, temp.length()-1)+")";  
                geoStr +=temp+",";  
            }
            if("POLYGON".equals(type.name()))
            {
            	geoStr = geoStr.substring(0, geoStr.length()-1)+")";  
            }
            else {
				geoStr = geoStr.substring(0, geoStr.length()-1);
			}
            
        }else if("Envelope".equals(type.name())){  
            Envelope env = (Envelope)geometry;  
            geoStr = type.name()+"("+ env.getXMin() +","+env.getYMin()+","+env.getXMax()+","+env.getYMax()+")";           
        }else if("MultiPoint".equals(type.name())){           
        }else{  
            geoStr = null;  
        }         
        return geoStr;  
    }  
    /**  
     * 将wkt字符串拼成几何对象       
     */  
    public static Geometry WKTToGeometry(String wkt){  
        Geometry geo = null;  
        if(wkt == null || wkt.equals("")){  
            return null;  
        }  
        String headStr = wkt.substring(0, wkt.indexOf("("));  
        String temp = wkt.substring(wkt.indexOf("(")+1, wkt.lastIndexOf(")"));  
        if(headStr.equals("Point")||headStr.equals("POINT")){  
            String[] values = temp.split(" ");  
            geo = new Point(Double.valueOf(values[0]),Double.valueOf(values[1]));  
        }else if(headStr.equals("POLYlINE") || headStr.equals("POLYGON")|| headStr.equals("LINESTRING")){  
            geo = parseWKT(temp,headStr);  
        }else if(headStr.equals("Envelope")){  
            String[] extents = temp.split(",");           
         geo = new Envelope(Double.valueOf(extents[0]),Double.valueOf(extents[1]),Double.valueOf(extents[2]),Double.valueOf(extents[3]));  
        }else if(headStr.equals("MultiPoint")){           
        }else{  
            return null;  
        }  
        return geo;  
    }         
    private static Geometry parseWKT(String multipath,String type){  
        String subMultipath = multipath.substring(1, multipath.length()-1);  
        String[] paths;  
        if(subMultipath.indexOf("),(") >=0 ){              
            paths = subMultipath.split("),(");//多个几何对象的字符串  
        }else{  
            paths = new String[]{subMultipath};  
        }  
        Point startPoint = null;  
        MultiPath path = null ;  
        if(type.equals("POLYlINE")||type.equals("LINESTRING")){  
            path = new Polyline();  
        }else{  
            path = new Polygon();  
        }         
        for(int i=0;i<paths.length;i++){  
            String[] points = paths[i].split(", ");  
            startPoint = null;  
            for(int j=0;j<points.length;j++){                  
                String[] pointStr = points[j].split(" ");  
                if(startPoint == null){  
                    startPoint = new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1]));  
                    path.startPath(startPoint);  
                }else{                    
                    path.lineTo(new Point(Double.valueOf(pointStr[0]),Double.valueOf(pointStr[1])));  
                }                 
            }             
        }  
        return path;  
    }  
    public class LineStringObject {


    	private List<List<Double[]>> paths;
    	private HashMap<String, Integer> spatialReference;

    	public List<List<Double[]>> getPaths() {
    		return paths;
    	}

    	public void setPaths(List<List<Double[]>> paths) {
    		this.paths = paths;
    	}

    	public HashMap<String, Integer> getSpatialReference() {
    		return spatialReference;
    	}

    	public void setSpatialReference(HashMap<String, Integer> spatialReference) {
    		this.spatialReference = spatialReference;
    	}

    }
    public class MultiIPointObject {

    	private List<Double[]> points;
    	private HashMap<String, Integer> spatialReference;

    	public List<Double[]> getPoints() {
    		return points;
    	}

    	public void setPoints(List<Double[]> points) {
    		this.points = points;
    	}

    	public HashMap<String, Integer> getSpatialReference() {
    		return spatialReference;
    	}

    	public void setSpatialReference(HashMap<String, Integer> spatialReference) {
    		this.spatialReference = spatialReference;
    	}

    }
    public class MultLinesStringObject {

    	private List<List<Double[]>> rings;
    	private HashMap<String, Integer> spatialReference;

    	public List<List<Double[]>> getRings() {
    		return rings;
    	}

    	public void setRings(List<List<Double[]>> rings) {
    		this.rings = rings;
    	}

    	public HashMap<String, Integer> getSpatialReference() {
    		return spatialReference;
    	}

    	public void setSpatialReference(HashMap<String, Integer> spatialReference) {
    		this.spatialReference = spatialReference;
    	}

    }
    public class PointObject {

    	private double x;
    	private double y;
    	private HashMap<String, Integer> spatialReference;

    	public double getX() {
    		return x;
    	}

    	public void setX(double x) {
    		this.x = x;
    	}

    	public double getY() {
    		return y;
    	}

    	public void setY(double y) {
    		this.y = y;
    	}

    	public HashMap<String, Integer> getSpatialReference() {
    		return spatialReference;
    	}

    	public void setSpatialReference(HashMap<String, Integer> spatialReference) {
    		this.spatialReference = spatialReference;
    	}

    }
    public class PolygonObject {

    	private List<List<Double[]>> rings;
    	private HashMap<String, Integer> spatialReference;

    	public List<List<Double[]>> getRings() {
    		return rings;
    	}

    	public void setRings(List<List<Double[]>> rings) {
    		this.rings = rings;
    	}

    	public HashMap<String, Integer> getSpatialReference() {
    		return spatialReference;
    	}

    	public void setSpatialReference(HashMap<String, Integer> spatialReference) {
    		this.spatialReference = spatialReference;
    	}

    }
}