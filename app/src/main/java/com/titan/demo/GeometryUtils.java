/*
package com.titan.demo;


import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.Multipart;
import com.esri.arcgisruntime.geometry.Part;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;

public class GeometryUtils {
    */
/**
     * 将几何对象生成wkt字符串      
     *//*

    public static String GeometryToWKT(Geometry geometry){
        if(geometry == null){  
            return null;  
        }  
        String geoStr = "";  
        GeometryType type = geometry.getGeometryType();
        if("Point".equals(type.name())||"POINT".equals(type.name())){              
            Point pt = (Point)geometry;
            geoStr = type.name()+"("+pt.getX() +" "+pt.getY()+")";            
        }else if("POLYGON".equals(type.name()) || "Polyline".equals(type.name())|| "LINESTRING".equals(type.name())){
            //MultiPath pg = (MultiPath)geometry;
            Multipart mp=(Multipart)geometry;
            geoStr = type.name()+"("+"";
            int pathSize=mp.getParts().getPartsAsPoints().size();
            int pathSize =pg.get pg.getPathCount();
            for(int j=0;j<pathSize;j++){
                String temp = "(";
                int size = pg.getPathSize(j);
                for(int i=0;i<size;i++){
                    Point pt = pg.getPoint(i);
                    temp +=pt.getX() +" "+pt.getY()+",";
                }
                temp = temp.substring(0, temp.length()-1)+")";
                geoStr +=temp+",";
            }
            geoStr = geoStr.substring(0, geoStr.length()-1)+")";
        }else if("Envelope".equals(type.name())){
            Envelope env = (Envelope)geometry;
            geoStr = type.name()+"("+ env.getXMin() +","+env.getYMin()+","+env.getXMax()+","+env.getYMax()+")";
        }else if("MultiPoint".equals(type.name())){
        }else{
            geoStr = null;
        }
        return geoStr;  
    }  
  
      
    */
/**
     * 将wkt字符串拼成几何对象       
     *//*

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
        }else if(headStr.equals("Polyline") || headStr.equals("POLYGON")|| headStr.equals("LINESTRING")){  
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
        if(type.equals("Polyline")||type.equals("LINESTRING")){  
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
  
  
} 
*/
