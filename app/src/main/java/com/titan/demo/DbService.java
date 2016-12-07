package com.titan.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.internal.tasks.ags.ad;
import com.esri.core.map.Graphic;
import com.otitan.android.WktUtils;

import jsqlite.Callback;
import jsqlite.Database;
import android.R.bool;
import android.content.Context;
import android.widget.TextView;

public class DbService {
	private Context context;
	private String dbString;
	private Database db;
	WktUtils wkttools;

	public DbService(Context context) {
		this.context = context;
		initdbservice();
	}

	private void initdbservice() {

		try {
			
			dbString = ResourcesManager.getInstance(context).getPath()[1] + "/Otitan/db.sqlite";
			// dbString = ResourcesManager.getInstance(context).getPath()[1] + "/Otitan/ot.db";
			Class.forName("jsqlite.JDBCDriver").newInstance();
			db = new jsqlite.Database();
			db.open(dbString, jsqlite.Constants.SQLITE_OPEN_READWRITE);
		} catch (jsqlite.Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openDB() {
		try {
			Class.forName("jsqlite.JDBCDriver").newInstance();
			db = new jsqlite.Database();
			db.open(dbString, jsqlite.Constants.SQLITE_OPEN_READWRITE);
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void closeDB(){
		try {
			db.close();
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
		}
	}
	//获取可编辑列名
	public Map<String, String>  getColumnsName(String layername) {
		openDB();
		final Map<String, String> conlumsmap=new HashMap<>();
		Callback cb = new Callback() {
			int i=0;
			
			public void columns(String[] arg0) {

			}

			public boolean newrow(String[] r) {
                i++;
                //&&!r[1].equals("shap")
                if(i>3)
                {
                	conlumsmap.put(r[1], r[2]);
                }
				return false;
			}

			public void types(String[] arg0) {

			}

		};
		try {
			String sql = "PRAGMA  table_info( '" + layername + "')";
			db.exec(sql, cb);
			closeDB();
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
			return null;
		}
		return conlumsmap;
	}
	//修改字段
		public boolean  updatefeild(String layername,String layertype,Map<String, String> cols,List<String> rcols) {
			openDB();
		    String f1="";
		    String info="";
		    String editfeild="";
		    String rfeild ="";
		    String sql4="";
			try {
				String sql = "alter table "+layername+" rename to temp_"+layername+"";
				db.exec(sql, null);
				String f0="OBJECTID" + " " + "INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+"TYPE" + " " + "TEXT";
				switch (layertype) {
				case "POINT":
					 f1= "INFO" + " " + "TEXT";
					 info="INFO";
					break;
				case "POLYLINE":
					layertype="LINESTRING";
					 f1= "LENGTH（㎞）" + " " + "DOUBLE";
					 info="LENGTH（㎞）";
					break;
				case "POLYGON":
					 f1= "AREA（k㎡）" + " " + "DOUBLE";
					 info="AREA（k㎡）";
					break;
				default:
					break;
				}
				String sql1="CREATE  TABLE "+layername+" ("+f0+","+f1+")";
				db.exec(sql1, null);
				String sql2 = "SELECT AddGeometryColumn('"+layername+"' , 'shap',4326, '" + layertype + "', 'XY')";
				db.exec(sql2, null);
				int i=0;
				if(cols.size()>0){
					for (Entry<String, String> map : cols.entrySet()) {
							
							if(i!=cols.size()-1)
							{
								if(!map.getKey().equals("shap"))
								{
								    sql4="Alter table "+layername+" add column '"+map.getKey()+"' '"+map.getValue()+"'";
								    db.exec(sql4, null);
								    editfeild+=""+map.getKey()+" ," ;
								}
							}
							else {
								if(!map.getKey().equals("shap"))
								{
									 sql4="Alter table "+layername+" add column '"+map.getKey()+"' '"+map.getValue()+"'";
									    db.exec(sql4, null);
								      editfeild+=""+map.getKey()+" " ;
								}
								else {
									editfeild=editfeild.substring(0, editfeild.length()-1);
								}
							}
							
							
						
						i++;
					}
					for (int t=0;t<rcols.size();t++)
					{
						
							if(t!=rcols.size()-1)
							{
								if(!rcols.get(t).equals("shap"))
								{
								rfeild+=""+rcols.get(t)+" ," ;
								}
							}
							else {
								if(!rcols.get(t).equals("shap"))
								{
								rfeild+=""+rcols.get(t)+" " ;
								}
								else {
									rfeild=rfeild.substring(0, rfeild.length()-1);
								}
							}
						
						
					}
				/*	for (Entry<String, String> map : rcols.entrySet()) {
						if(!map.getKey().equals("shap"))
						{
							//String sql4="Alter table "+layername+" add column '"+map.getKey()+"' '"+map.getValue()+"'";
							if(t!=rcols.size()-1)
							{
								rfeild+=""+map.getKey()+"," ;
							}
							else {
								rfeild+=""+map.getKey()+" " ;
							}
							//db.exec(sql4, null);
							
						}
						t++;
					}*/
				}
				
				/* 添加空间索引 */  
	            String sql3 = "SELECT CreateSpatialIndex('"+layername+"', 'shap')";  
	            db.exec(sql3, null);
	            if(editfeild.equals(""))
	            {
	            	String sql5="INSERT INTO '"+layername+"'     SELECT OBJECTID,TYPE,"+info+",shap FROM temp_"+layername+" ";
	            
	            	db.exec(sql5, null);
	            }
	            else {
	            
	            	String sql5="INSERT INTO '"+layername+"'  (OBJECTID,TYPE,"+info+",shap,"+editfeild+") SELECT OBJECTID,TYPE,"+info+",shap,"+rfeild+" FROM temp_"+layername+"";
	            	db.exec(sql5, null);
				}
	            String sql6="drop table temp_"+layername+" ";
	            db.exec(sql6, null);
	            //删除复制表索引
	            String sql7="select DiscardGeometryColumn('temp_"+layername+"', 'shap')";
	            db.exec(sql7, null);
				closeDB();
			} catch (jsqlite.Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
    //添加字段
	public boolean addcolumn(String layername,String name, String type) {

		openDB();
		Callback cb = new Callback() {

			public void columns(String[] arg0) {

			}

			public boolean newrow(String[] arg0) {

				return false;
			}

			public void types(String[] arg0) {

			}

		};
		try {
			String sql = "ALTER TABLE '"+layername+"' ADD COLUMN '" + name + "' '" + type + "'";
			db.exec(sql, cb);
			
			closeDB();
			return true;
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
	//删除字段
	 public boolean deleteColumn(String layername,String layertype,Map<String, String> cols) {
		String f1="";
		String info="";
		String addfeild="";
		openDB();
/*		CREATE TEMPORARY TABLE t1_backup(a,b);
		INSERT INTO t1_backup SELECT a,b FROM t1;
		DROP TABLE t1;
		CREATE TABLE t1(a,b);
		INSERT INTO t1 SELECT a,b FROM t1_backup;
		DROP TABLE t1_backup;*/
		String f0="OBJECTID" + " " + "INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+"TYPE" + " " + "TEXT";
		switch (layertype) {
		case "POINT":
			 f1= "INFO" + " " + "TEXT";
			 info="INFO";
			break;
		case "POLYLINE":
			layertype="LINESTRING";
			 f1= "LENGTH（㎞）" + " " + "DOUBLE";
			 info="LENGTH（㎞）";
			break;
		case "POLYGON":
			 f1= "AREA（k㎡）" + " " + "DOUBLE";
			 info="AREA（k㎡）";
			break;
		default:
			break;
		}
		String sql1="CREATE  TABLE "+layername+"_backup ("+f0+","+f1+")";
        try {
			db.exec(sql1, null);
			String sql2 = "SELECT AddGeometryColumn('"+layername+"_backup' , 'shap',4326, '" + layertype + "', 'XY')";
			db.exec(sql2, null);
			int i=1;
			if(cols.size()>1){
				for (Entry<String, String> map : cols.entrySet()) {
					if(!map.getKey().equals("shap"))
					{
						String sql4="Alter table "+layername+"_backup add column '"+map.getKey()+"' '"+map.getValue()+"'";
						if(i!=cols.size()-1)
						{
							addfeild+=""+map.getKey()+"," ;
						}
						else {
							addfeild+=""+map.getKey()+" " ;
						}
						db.exec(sql4, null);
						
					}
					i++;
				}
			}
			
			/* 添加空间索引 */  
            String sql3 = "SELECT CreateSpatialIndex('"+layername+"_backup', 'shap')";  
            db.exec(sql3, null);
            //copy data
            if(addfeild.equals(""))
            {
            	String sql5="INSERT INTO '"+layername+"_backup'  SELECT OBJECTID,TYPE,"+info+",shap FROM '"+layername+"';";
            
            	db.exec(sql5, null);
            }
            else {
            
            	String sql5="INSERT INTO '"+layername+"_backup'  SELECT OBJECTID,TYPE,"+info+",shap,"+addfeild+" FROM '"+layername+"';";
            	db.exec(sql5, null);
			}
            
            
            //delete 原表
            String sql6="drop table '"+layername+"'";
            db.exec(sql6, null);
            //删除复制表索引
            String sql7="select DiscardGeometryColumn('"+layername+"_backup', 'shap')";
            db.exec(sql7, null);
            //rename 复制表
            String sql8="alter  table '"+layername+"_backup' rename to '"+layername+"'";
            db.exec(sql8, null);
            closeDB();
		} catch (jsqlite.Exception e) {
			
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
    //添加图层
	public boolean newlayer(String layername, String type, List<Map<String, String>> list) {
		openDB();
		String sql = "create table '" + layername + "'" + "(";
		String sql4="";
		int i=0;
		try {
			sql += "OBJECTID" + " " + "INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,";
			sql += "TYPE" + " " + "TEXT,";
		
			switch (type) {
			case "POINT":
				sql += "INFO" + " " + "TEXT";
				break;
			case "POLYLINE":
				sql += "LENGTH（㎞）" + " " + "DOUBLE";
				type="LINESTRING";
				break;
			case "POLYGON":
				sql += "AREA（k㎡）" + " " + "DOUBLE";
				break;
			default:
				break;
			}
			
			sql += ")";
			db.exec(sql, null);
			String sql2 = "SELECT AddGeometryColumn('" + layername + "', 'shap',4326, '" + type + "', 'XY')";
			db.exec(sql2, null);
			if(list.size()>0){
				for (Map<String, String> map : list) {
					
					sql4="Alter table '"+layername+"' add column '"+map.get("name")+"' '"+map.get("type")+"'";
					db.exec(sql4, null);
				}
			}
			
			/* 添加空间索引 */  
            String sql3 = "SELECT CreateSpatialIndex('" + layername + "', 'shap')";  
            db.exec(sql3, null);
            closeDB();
			return true;

		} catch (jsqlite.Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	//更新图层
	public boolean upDateLayer(String layername, String wkt,String id) {
		openDB();
		Callback cb = new Callback() {

			@Override
			public void types(String[] arg0) {

			}

			@Override
			public boolean newrow(String[] arg0) {
				return false;
			}

			@Override
			public void columns(String[] arg0) {
				// TODO Auto-generated method stub

			}
		};
		String sql = "update '"+layername+"' set shap=GeomFromText('"+wkt+"', 4326)," +
				"AREA（k㎡）=ST_AREA(GeomFromText('" + wkt + "', 4326))/ 1000000.0 " +
				"where OBJECTID='"+id+"'";
		try {
			db.exec(sql, cb);
			closeDB();
		} catch (jsqlite.Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//更新属性数据
	public boolean upDateAttributes(String layername,String id,List<String> fieldsname,List<String> fieldsvalue) {
		
		openDB();
		
		String sql = "update '"+layername+"' " ;
		
		if(fieldsvalue.size()>0)
		{
			
				for(int i=0;i<fieldsvalue.size();i++)
				{
					if(fieldsvalue.size()==1||i==fieldsvalue.size()-1)
					{
						sql+="set '"+fieldsname.get(i+3)+"'='"+fieldsvalue.get(i)+"'";
					}
					else {
						sql+="set '"+fieldsname.get(i+3)+"'='"+fieldsvalue.get(i)+"',";
					}
					
				}
		}
				/*sql+="set shap=GeomFromText('"+wkt+"', 4326)"+",";*/
						/* +
				"AREA（k㎡）=ST_AREA(GeomFromText('" + wkt + "', 4326))/ 1000000.0 " ;*/
		sql+="where OBJECTID='"+id+"'";
		try {
			db.exec(sql, null);
			closeDB();
		} catch (jsqlite.Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//加载图层数据
		public List<TreeMap<String, Object>> loadLayerData(String layername) {
  			openDB();
			String dbFile;
			final List<Geometry> geometries = new ArrayList<Geometry>();
			final List<TreeMap<String, Object>> dataList=new ArrayList<TreeMap<String,Object>>();
			final List<String> cols=new ArrayList();
			
			try {

				Callback cb = new Callback() {

					public void columns(String[] arg0) {
						
						for (int i = 0; i < arg0.length; i++) {
							cols.add(arg0[i]);
						}

					}

					public boolean newrow(String[] row) {
						Map<String,Object> datamap=new TreeMap<String,Object>();
						for(int i=0;i<row.length;i++)
						{
							if(i==row.length-1)
							{
								Geometry geometry = WktUtils.WKTToGeometry(row[i]);
								datamap.put(cols.get(i), geometry);
							}
							else {
								//remove shap
								if (i!=3) {
									
									datamap.put(cols.get(i), row[i]);
								}
								
							}
						}
						//Geometry geometry = WktUtils.WKTToGeometry(rows[1]);
						dataList.add((TreeMap<String, Object>) datamap);
						//geometries.add(geometry);
						/* Graphic graphic = new Graphic(ggGeometry,markerSymbol); 
						 graphicsLayer.addGraphic(graphic);*/

						return false;
					}

					public void types(String[] arg0) {

					}

				};
				//Polygon
				// String query = "select t.pkuid,AsText(t.几何图形)from new_layer t";  
				//Polyline
				//String query = "select t.dd,AsText(t.shap)from line t";  
				//point
				String sql="select t.*,AsText(t.shap) from  '" + layername + "' t";
				//String query = "select t.name,AsText(t.shap) from '" + layername + "' t";
				//String query = "select t.pkuid,AStext(t.shap) from tt t"; 
				db.exec(sql, cb);
				closeDB();
				//datamap.put("Geometry", geometries);

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return dataList;
		}
	//加载图层
	public List<Geometry> loadLayer(String layername) {
		openDB();
		String dbFile;
		final List<Geometry> geometries = new ArrayList<Geometry>();
		try {

			Callback cb = new Callback() {

				public void columns(String[] arg0) {

				}

				public boolean newrow(String[] arg0) {
					wkttools = new WktUtils();
					//polygon to json
					//String json=wkttools.getMULTIPOLYGONWktToJson(arg0[1], 102100);
					//line to json
					//String json=wkttools.getMULTILINESTRINGWktToJson(arg0[1], 102100);
					//point to json
					//String json=wkttools.getPOINTWktToJson(arg0[1], 102100);
					/*String wkt=WktUtils.GeometryJOSNToWKT(json);
					  JsonFactory jsonFactory = new JsonFactory();  
					JsonParser jsonParser = null;
					try {
						jsonParser = jsonFactory.createJsonParser(json);
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					};
					 MapGeometry mapGeometry = GeometryEngine.jsonToGeometry(jsonParser);
					 
					 Geometry geometry = mapGeometry.getGeometry();*/

					// String  dString=WktUtils.GeometryToWKT(geometry);
					Geometry ggGeometry = WktUtils.WKTToGeometry(arg0[1]);

					geometries.add(ggGeometry);
					/* Graphic graphic = new Graphic(ggGeometry,markerSymbol); 
					 graphicsLayer.addGraphic(graphic);*/

					return false;
				}

				public void types(String[] arg0) {

				}

			};
			//Polygon
			// String query = "select t.pkuid,AsText(t.几何图形)from new_layer t";  
			//Polyline
			//String query = "select t.dd,AsText(t.shap)from line t";  
			//point
			String query = "select t.name,AsText(t.shap) from '" + layername + "' t";
			//String query = "select t.pkuid,AStext(t.shap) from tt t"; 
			db.exec(query, cb);
           closeDB();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return geometries;
	}

	//获取图层数据
	public List<String> getLayerData(String layername) {
		openDB();
		final List<String> layerdata = new ArrayList<String>();
		Callback cb = new Callback() {

			public void columns(String[] arg0) {

			}

			public boolean newrow(String[] arg0) {
				layerdata.add(arg0[0]);

				return false;
			}

			public void types(String[] arg0) {

			}

		};
		try {
			String sql = "select l.name  from '" + layername + "' l ";
			//String sql="select *  from '" + layername + "'";
			db.exec(sql, cb);
         closeDB();
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
			return null;
		}
		return layerdata;
	}
    //添加数据
	public boolean addData(String layername, String wkt, List<String> fieldsname,List<String> fieldsvalue,String type) {
		openDB();
		String sql = "";
		String addnames="";
	    String addvalues="";  
		//openDB();
		try {
			if(fieldsvalue.size()>0)
			{
				for(int i=0;i<fieldsvalue.size();i++)
				{
					if(i!=fieldsvalue.size()-1)
					{
						addnames+=fieldsname.get(i)+",";
						addvalues+="'"+fieldsvalue.get(i)+"'"+",";
					}
					else {
						addnames+=fieldsname.get(i);
						addvalues+="'"+fieldsvalue.get(i)+"'";
					}
					
				}
			}
			
			//insert into  point values('ww','dalj',GeomFromText('POINT(11.01 11.02)', 4326),'dd','jkd')
			switch (type) {
			case "POLYGON":
				if(fieldsvalue.size()>0)
				{
					sql = "insert into  '" + layername + "' (shap,AREA（k㎡）,TYPE,"+addnames+") values(GeomFromText('" + wkt
							+ "', 4326),ST_AREA(GeomFromText('" + wkt + "', 4326))/ 1000000.0,'POLYGON',"+addvalues+")";
				}
				else {
					sql = "insert into  '" + layername + "' (shap,AREA（k㎡）,TYPE) values(GeomFromText('" + wkt
							+ "', 4326),ST_AREA(GeomFromText('" + wkt + "', 4326))/ 1000000.0,'POLYGON')";
				}
				
				break;
			case "POLYLINE":
				if(fieldsvalue.size()>0)
				{
					sql = "insert into  '" + layername + "' (shap,LENGTH（㎞）,TYPE,"+addnames+") values(GeomFromText('" + wkt
							+ "', 4326),ST_Length(GeomFromText('" + wkt + "', 4326))/ 1000.0,'POLYLINE',"+addvalues+")";
				}
				else {
					sql = "insert into  '" + layername + "' (shap,LENGTH（㎞）,TYPE) values(GeomFromText('" + wkt
							+ "', 4326),ST_Length(GeomFromText('" + wkt + "', 4326))/ 1000.0,'POLYLINE')";
				}
				
				break;
			case "POINT":
				if(fieldsvalue.size()>0)
				{
					sql = "insert into  '" + layername + "' (shap,TYPE,"+addnames+") values(GeomFromText('"+ wkt + "', 4326),'POINT',"+addvalues+")";
				}
				else {
					sql = "insert into  '" + layername + "' (shap,TYPE) values(GeomFromText('"+ wkt + "', 4326),'POINT')";
				}
				break;
			default:
				break;
			}
			
			
			//sql = "insert into  '" + layername + "' (name,shap) values('" + feilds.get("name") + "',GeomFromText('"+ wkt + "', 4326))";
			
			//sql="insert into  'tt' (name,shap) values('"+feilds.get("name")+"',GeomFromText('"+wkt+"', 4326))";
         
			db.exec(sql, null);
			
			closeDB();
			

		} catch (jsqlite.Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//编辑数据
	public boolean editData(String layername, Map<String, String> fields) {
		openDB();
		Callback cb = new Callback() {

			public void columns(String[] arg0) {

			}

			public boolean newrow(String[] arg0) {

				return false;
			}

			public void types(String[] arg0) {

			}

		};
		try {
			//insert into  point values('ww','dalj',GeomFromText('POINT(11.01 11.02)', 4326),'dd','jkd')

			String sql = "update '" + layername + "' set name='" + fields.get("name") + "' where id ='"
					+ fields.get("id") + "'";
			db.exec(sql, cb);
      closeDB();
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
    //删除数据
	public boolean deleteData(String layername, String name) {
		openDB();
		Callback cb = new Callback() {

			public void columns(String[] arg0) {

			}

			public boolean newrow(String[] arg0) {

				return false;
			}

			public void types(String[] arg0) {

			}

		};
		String sql = "delete from '" + layername + "' where name='" + name + "'";
		try {
			db.exec(sql, cb);
			closeDB();
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
    //获取图层名称
	public List<String> getLayerName() {
		openDB();
		final List<String> layername=new ArrayList<>();
		Callback cb = new Callback() {

			public void columns(String[] arg0) {

			}

			public boolean newrow(String[] arg0) {
				if(!arg0[0].equals("point"))
				{
					layername.add(arg0[0]);
				}
				
				return false;
			}

			public void types(String[] arg0) {

			}

		};
		String sql = "SELECT f_table_name FROM geometry_columns;";
		try {
			db.exec(sql, cb);
			closeDB();
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
		}
		
		return layername;
		
	}
	public boolean testaddData() {
		String sql = "";
		Callback cb = new Callback() {

			public void columns(String[] arg0) {

			}

			public boolean newrow(String[] arg0) {

				return false;
			}

			public void types(String[] arg0) {

			}

		};
		try {
			//insert into  point values('ww','dalj',GeomFromText('POINT(11.01 11.02)', 4326),'dd','jkd')

			sql = "insert into  'ss' (shap) values(GeomFromText('(POINT(10.21 244.00))', 4326))";
			//sql="insert into  'tt' (name,shap) values('"+feilds.get("name")+"',GeomFromText('"+wkt+"', 4326))";

			db.exec(sql, cb);
            //db.close();
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


}
