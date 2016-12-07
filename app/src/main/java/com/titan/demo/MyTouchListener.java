package com.titan.demo;

import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import abc.abc.abc.R.color;
import abc.abc.abc.R.layout;
import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.esri.android.map.Callout;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.internal.tasks.ags.v;
import com.esri.core.internal.widget.InfiniteGallery;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.otitan.adapter.LayerDataAdapter;
import com.otitan.android.WktUtils;
import com.otitan.drawtools.DrawEvent;
import com.otitan.drawtools.DrawEventListener;
import com.otitan.drawtools.DrawTool;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

class MyTouchListener extends MapOnTouchListener implements OnClickListener{
	
	MapView map;
	Context mContext;
	private Callout callout;
	GraphicsLayer layer;
	GraphicsLayer editgraphicsLayer;
	Graphic result,editgraphic,addGraphic,updateGraphic;
	List<String> dataList;
	ArrayList<Point> points=new ArrayList<Point>();
	ArrayList<Point> addPoints=new ArrayList<>();
	ArrayList<Point> mMidPoints = new ArrayList<Point>();
	Map<String, String> fields;
	List<Point> midpoints=new ArrayList<Point>();
	String layertype,wkt,layername;
	int graphficindex=0;
	int[] sgraphicid;
	int[] idsArr;//grphficids
	boolean isedit=false;//是否开启编辑
	boolean additem=false;//是否添加要素
	DbService dbService;
	ListView lv_display;
	//添加数据时弹出属性编辑表单
	AlertDialog addDialog;
	TextView tv_addname;
	DrawTool drawTool;
	

	//图层所有数据
	List<TreeMap<String, Object>> dataArrayList;
	TreeMap<String, Object> editTreeMap=null;//修改对象的属性数据

	//编辑模式
	 private enum EditMode {
		    NONE, POINT, POLYLINE, POLYGON, SAVING
		  }
	 EditMode mEditMode;
	 
	  boolean mMidPointSelected = false;

	  boolean mVertexSelected = false;

	  int mInsertingIndex;
	  
	 SimpleMarkerSymbol mRedMarkerSymbol = new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.CIRCLE);

	  SimpleMarkerSymbol mBlackMarkerSymbol = new SimpleMarkerSymbol(Color.BLACK, 20, SimpleMarkerSymbol.STYLE.CIRCLE);

	  SimpleMarkerSymbol mGreenMarkerSymbol = new SimpleMarkerSymbol(Color.GREEN, 15, SimpleMarkerSymbol.STYLE.CIRCLE);
	  
	  SimpleFillSymbol fillSymbol =new SimpleFillSymbol(color.ccc_red);
      SimpleLineSymbol lineSymbol=new SimpleLineSymbol(Color.RED, 20);
       SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(Color.RED, 10, STYLE.CIRCLE);
	public MyTouchListener(Context context,ListView lv, MapView view,GraphicsLayer graphicsLayer,List<String> layerdata,String layertype,boolean isadd ,String layername,List<TreeMap<String,Object>> data) {
		super(context, view);
		this.map = view;
		this.mContext = context;
		this.layer=graphicsLayer;
		
		this.dataList=layerdata;
		this.layertype=layertype;
		this.additem=isadd;
		this.layername=layername;
		dbService=new DbService(mContext);
		this.drawTool = new DrawTool(this.map);
		//this.drawTool.addEventListener(this);
		this.lv_display=lv;
		dataArrayList=data;
	}
	@Override
	public boolean onDoubleTap (MotionEvent point)
	{
		
		if(editgraphicsLayer!=null)
		{
			new AlertDialog.Builder(mContext)
			.setTitle("提示")
			.setMessage("您确定保存编辑吗?")
			.setPositiveButton("确定" , new  DialogInterface.OnClickListener  () {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					isedit=false;
					fillSymbol =new SimpleFillSymbol(color.ccc_red);
					int[] editlayeridsArr = editgraphicsLayer.getGraphicIDs();
					Graphic newgraphfic=editgraphicsLayer.getGraphic(editlayeridsArr[editlayeridsArr.length-1]);
					Geometry geometry=newgraphfic.getGeometry();
				    
					//修改graphic 样式
					Graphic g=new Graphic(geometry, fillSymbol);
					String wkt= WktUtils.GeometryToWKT(geometry);
					String id=(String) editgraphic.getAttributes().get("id");
					// final String id =(String) result.getAttributes().get("id");
					//String name=dataList.get(graphficindex).toString();
					//String name = (String) g.getAttributeValue("name");
					boolean isupdate=dbService.upDateLayer(layername,wkt,id);
					
					
					if(isupdate)
					{
						layer.removeGraphic(idsArr[graphficindex]);
						layer.addGraphic(g);
						//layer.updateGraphic(idsArr[graphficindex], g);
						editgraphicsLayer.removeAll();
						Toast.makeText(mContext, "更新数据成功", 0).show();
					}else {
						editgraphicsLayer.removeAll();
						Toast.makeText(mContext, "保存数据失败", 0).show();
					}
				}

				
			  }
			)
            .setNegativeButton("取消" , null)  
            .show();  
			
		   //  layer.updateGraphic(idsArr[graphficindex], fillSymbol);
		}
		
		return true;
	}
	@Override
	public boolean onSingleTap(MotionEvent point) {
		Point p = map.toMapPoint(new Point(point.getX(), point.getY()));
		
		//编辑模式
		if(isedit)
		{
			//isedit=false;
			//mVertexSelected=true;
			 if ( mVertexSelected||mMidPointSelected) {
				  //int idx1 = getSelectedIndex(point.getX(), point.getY(), points, map);
				  //mInsertingIndex = points.size()-1;
		          movePoint(p);
		          //points.remove(mInsertingIndex);
		      } 
			 else {
				 int idx1 = getSelectedIndex(point.getX(), point.getY(), mMidPoints, map);
			        if (idx1 != -1) {
			          mMidPointSelected = true;
			          mInsertingIndex = idx1;
			        } 
			        else {
			        	int idx2 = getSelectedIndex(point.getX(), point.getY(), points, map);
					    if (idx2 != -1) {
							//如果选中节点
							mVertexSelected = true;
							mInsertingIndex = idx2;
						}
						else {
				            // No matching point above, add new vertex at tap point
							 int f=points.size()-1;
							 points.remove(0);
				             points.remove(points.size()-1);
				             points.add(p);
				             
				            //mEditingStates.add(new EditingStates(mPoints, mMidPointSelected, mVertexSelected, mInsertingIndex));
				          }
					}
				    
			}
			refresh();
			/*SimpleFillSymbol fillSymbol_ccc_red =new SimpleFillSymbol(color.ccc_red);
			List<Point> list=new ArrayList<Point>();
			Polygon editresultPolygon=new Polygon();
		    Polygon editPolygon=(Polygon) editgraphic.getGeometry();
		    double x=p.getX();
			double y=p.getY();
		    editresultPolygon.setXY(0,x, y);
		    //editresultPolygon.setXY(0,p.getX(), p.getY());
			for(int i=1;i<=editPolygon.getPointCount();i++)
			{
				editresultPolygon.setXY(i, editPolygon.getPoint(i).getX(),editPolygon.getPoint(i).getY());
				//list.add(editPolygon.getPoint(i));
			}
			
		    Graphic editresultGraphic=new Graphic(editPolygon, fillSymbol_ccc_red);
		    layer.updateGraphic(idsArr[graphficindex], editresultGraphic);
			return true;*/
		}
		else {
			//MapViewHelper mv=new MapViewHelper(map);
			 result = GetGraphicsFromLayer(p, layer);
			 sgraphicid=layer.getGraphicIDs(point.getX(),point.getY(),10,1);
			 Graphic result1=null;
			 if(sgraphicid.length>0)
			 {
				 result1=layer.getGraphic(sgraphicid[0]);
			 }
			// Graphic result1=layer.getGraphic(sgraphicid[0]);
			 
				Point Point2=new Point(p.getX(), p.getY());
				callout = map.getCallout();
				if (result1 != null) {
					//List<ChildEntity> list = getListEntity(result);
					// 显示提示callout
					// Create callout from MapView
					//callout.setPassTouchEventsToMapView(passToMapView)
					callout.setCoordinates(Point2);
					callout.setOffset(0, -3);
					callout.setStyle(R.xml.calloutstyle);
					// populate callout with results from IdentifyTask
					
					callout.setContent(CalloutView(result1));
					// show callout
					callout.show();
				} else {
					callout.hide();
				}// end if
				
		}
		return true;
	}
	@Override
	public void onLongPress(MotionEvent point) {
		//mEditMode=EditMode.POLYGON;
		editgraphicsLayer=new GraphicsLayer();
		Point p = map.toMapPoint(new Point(point.getX(), point.getY()));
		map.addLayer(editgraphicsLayer);
		
			isedit=true;
		    editgraphic = GetGraphicsFromLayer(p, layer);
		   // final String id =(String) result.getAttributes().get("id");
		    //SimpleFillSymbol fillSymbol_blue =new SimpleFillSymbol(Color.BLUE);
		    //layer.updateGraphic(idsArr[graphficindex], fillSymbol_blue);
		    //editgraphicsLayer.addGraphic(editgraphic);
		    Polygon editPolygon=(Polygon) editgraphic.getGeometry();
		    for (int i = 0; i < editPolygon.getPointCount(); i++) {
		    	points.add(editPolygon.getPoint(i));
		    }
			refresh();
		   
		
	    
	}
	/*
	 *判断点是否在polygon范围内
	 */
	public Graphic GetGraphicsFromLayer(Point pt, GraphicsLayer layer) {
		Graphic result = null;
		try {
			 idsArr = layer.getGraphicIDs();
			
			for (int i = 0; i < idsArr.length; i++) {
				Graphic gpVar = layer.getGraphic(idsArr[i]);
				if (gpVar != null) {
					Polygon polygonVar = (Polygon) gpVar.getGeometry();
					
					List<Point> list=new ArrayList<Point>();
					for(int c=0;c<polygonVar.getPointCount();c++)
					{
						double x=polygonVar.getPoint(c).getX();
						double y=polygonVar.getPoint(c).getY();
						list.add(polygonVar.getPoint(c));
						
					}
					/// 判断坐标点是否落在指定的多边形区域内  
					boolean IsWithIn=GISUtils.IsWithIn(pt, list);
					if(IsWithIn)
					{
						graphficindex=i;
						result=gpVar;
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	public ViewGroup CalloutView(Graphic getGraphic) {
		
		 View view = View.inflate(mContext,R.layout.calloutview, null);
		 
	     TextView tv = (TextView) view.findViewById(R.id.tv_content);
	     Button btn_edit=(Button) view.findViewById(R.id.btn_edit);
	     final String id =(String) getGraphic.getAttributes().get("id");
	     String idString="";
	     btn_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showEditDialog(id);
			}
		});
	     int[] idsArr = layer.getGraphicIDs();
	     
	     String dataString="";
	     //判断是否编辑了
	     if(editTreeMap!=null)
	     {
	    	 for(Entry<String, Object> entry: editTreeMap.entrySet()) {
    			
    				 dataString+=entry.getKey() + ":" + entry.getValue()+"\n";

    		}
	    	 editTreeMap=null;
	     }else {
	    	  for(TreeMap<String, Object> m:dataArrayList)
	 	     {
	 	    	 if(m.get("OBJECTID").toString().equals(id))
	 	    	 {
	 	    		 //idString=m.get("OBJECTID").toString();
	 	    		 for(Entry<String, Object> entry: m.entrySet()) {
	 	    			 if(!entry.getKey().equals("AsText(t.shap)"))
	 	    			 {
	 	    				 dataString+=entry.getKey() + ":" + entry.getValue()+"\n";
	 	    			 }
	 	    			

	 	    		}
	 	    	
	 	    	 }
	 	     }
		}
	   
	    // int id=result.getUid();
	  /*   int index=0;
	     for (int i = 0; i < idsArr.length; i++) {
	    	 
		   if(idsArr[i]==sgraphicid[0])
		   {
			   graphficindex=idsArr.length-i-1;
		   }
		   
		}    
	     
	     String dataString=dataList.get(graphficindex).toString();*/
	     tv.setText(dataString);
	     //String ddString=result.getAttributeValue("id").toString();
	     //tv.setText(result.getAttributeValue("id").toString()+"\n"+result.getAttributeValue("name").toString());

		
		return (ViewGroup) view;
	}
	  void refresh() {
		   /* if (layer != null) {
		      layer.removeGraphic(graphficindex);
		    }*/
		    if (editgraphicsLayer != null) {
		    	editgraphicsLayer.removeAll();
			    }
		    drawPolylineOrPolygon();
		    drawMidPoints();
		    drawVertices();

		    //updateActionBar();
		  }
	 /**
	   * Draws point for each vertex in mPoints.
	   */
	  private void drawVertices() {
	    int index = 0;
	    SimpleMarkerSymbol symbol;

	    for (Point pt : points) {
	      if (mVertexSelected && index == mInsertingIndex) {
	        // This vertex is currently selected so make it red
	        symbol = mRedMarkerSymbol;
	      } else if (index == points.size() - 1  && !mVertexSelected) {
	        // Last vertex and none currently selected so make it red
	        symbol = mRedMarkerSymbol;
	      } else {
	        // Otherwise make it black
	        symbol = mBlackMarkerSymbol;
	      }
	      Graphic graphic = new Graphic(pt, symbol);
	      editgraphicsLayer.addGraphic(graphic);
	      index++;
	    }
	  }
	  /**
	     * Checks if a given location coincides (within a tolerance) with a point in a given array.
	     * 
	     * @param x Screen coordinate of location to check.
	     * @param y Screen coordinate of location to check.
	     * @param points Array of points to check.
	     * @param map MapView containing the points.
	     * @return Index within points of matching point, or -1 if none.
	     */
	    private int getSelectedIndex(double x, double y, ArrayList<Point> points, MapView map) {
	      final int TOLERANCE = 40; // Tolerance in pixels

	      if (points == null || points.size() == 0) {
	        return -1;
	      }

	      // Find closest point
	      int index = -1;
	      double distSQ_Small = Double.MAX_VALUE;
	      for (int i = 0; i < points.size(); i++) {
	        Point p = map.toScreenPoint(points.get(i));
	        double diffx = p.getX() - x;
	        double diffy = p.getY() - y;
	        double distSQ = diffx * diffx + diffy * diffy;
	        if (distSQ < distSQ_Small) {
	          index = i;
	          distSQ_Small = distSQ;
	        }
	      }

	      // Check if it's close enough
	      if (distSQ_Small < (TOLERANCE * TOLERANCE)) {
	        return index;
	      }
	      return -1;
	    }
	    /**
	     * Draws mid-point half way between each pair of vertices in mPoints.
	     */
	    private void drawMidPoints() {
	      int index;
	      Graphic graphic;

	      mMidPoints.clear();
	      if (points.size() > 1) {

	        // Build new list of mid-points
	        for (int i = 1; i < points.size(); i++) {
	          Point p1 = points.get(i - 1);
	          Point p2 = points.get(i);
	          mMidPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
	        }
	        if (mEditMode == EditMode.POLYGON && points.size() > 2) {
	          // Complete the circle
	          Point p1 = points.get(0);
	          Point p2 = points.get(points.size() - 1);
	          mMidPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
	        }

	        // Draw the mid-points
	        index = 0;
	        for (Point pt : mMidPoints) {
	          if (mMidPointSelected && mInsertingIndex == index) {
	            graphic = new Graphic(pt, mRedMarkerSymbol);
	          } else {
	            graphic = new Graphic(pt, mGreenMarkerSymbol);
	          }
	          editgraphicsLayer.addGraphic(graphic);
	          index++;
	        }
	      }
	    }
	    /**
	     * Moves the currently selected point to a given location.
	     * 
	     * @param point Location to move the point to.
	     */
	    private void movePoint(Point point) {
	      if (mMidPointSelected) {
	        // Move mid-point to the new location and make it a vertex
	        points.add(mInsertingIndex + 1, point);
	      } else {
	        // Must be a vertex: move it to the new location
	        ArrayList<Point> temp = new ArrayList<Point>();
	        for (int i = 0; i < points.size(); i++) {
	          if (i == mInsertingIndex) {
	            temp.add(point);
	          } else {
	            temp.add(points.get(i));
	          }
	        }
	        points.clear();
	        points.addAll(temp);
	      }
	      // Go back to the normal drawing mode and save the new editing state
	      mMidPointSelected = false;
	      mVertexSelected = false;
	      //mEditingStates.add(new EditingStates(mPoints, mMidPointSelected, mVertexSelected, mInsertingIndex));
	    }
	    /**
	     * Draws polyline or polygon (dependent on current mEditMode) between the vertices in mPoints.
	     */
	    private Graphic drawPolylineOrPolygon() {
	      Graphic graphic;
	      MultiPath multipath;

	      // Create and add graphics layer if it doesn't already exist
	      if (editgraphicsLayer == null) {
	    	  editgraphicsLayer = new GraphicsLayer();
	        map.addLayer(editgraphicsLayer);
	      }

	      if (points.size() > 1) {

	        // Build a MultiPath containing the vertices
	        if (mEditMode == EditMode.POLYLINE) {
	          multipath = new Polyline();
	        } else {
	          multipath = new Polygon();
	        }
	        multipath.startPath(points.get(0));
	        for (int i = 1; i < points.size(); i++) {
	          multipath.lineTo(points.get(i));
	        }

	        // Draw it using a line or fill symbol
	        if (mEditMode == EditMode.POLYLINE) {
	          graphic = new Graphic(multipath, new SimpleLineSymbol(Color.BLACK, 4));
	        } else {
	          SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(Color.BLUE);
	          simpleFillSymbol.setAlpha(60);
	          simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.BLACK, 4));
	          graphic = new Graphic(multipath, (simpleFillSymbol));
	        }
	        editgraphicsLayer.addGraphic(graphic);
	        return graphic;
	      }
		return null;
	    }

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//通过弹出callout添加属性数据
			case R.id.btn_edit:
				fields=new HashMap<String, String>();
			    String nameString =tv_addname.getText().toString();
			    if(nameString==null)
			    {
			    	nameString="";
			    }
			    dataList.add(nameString);
			    lv_display.setAdapter(new LayerDataAdapter(mContext,dataList ));
				fields.put("name",nameString);
				
				addDialog.dismiss();
			/*	
				boolean isadd=dbService.addData(layername,wkt,fields,layertype);
				if(isadd)
				{
					Toast.makeText(mContext, "添加成功", 0).show();
				} else {
					Toast.makeText(mContext, "添加失败", 0).show();
				}*/
				
				break;

			default:
				break;
			}
			
		}

		//显示属性编辑对话框
		public void showEditDialog(final String id) {
			
			 addDialog=new AlertDialog.Builder(mContext).create();
			 addDialog.setCanceledOnTouchOutside(true);
				//LinearLayout layout =new LinearLayout(mContext);
			// 创建LinearLayout对象  
			 
				final View v = View.inflate(mContext, R.layout.edit, null);
				LinearLayout mLinearLayout = (LinearLayout) v.findViewById(R.id.add_LinearLayout); 
				final List<String> fieldsname=new ArrayList<String>();
				
				//String [][] fieldsmap=new String [][]{};
				TreeMap<String, Object> m=dataArrayList.get(0);
				int i=0;
				 float density = mContext.getResources().getDisplayMetrics().density;//获取屏幕密度
				 int rowheight=(int) (40*density);
				 
				for(TreeMap<String, Object> tm:dataArrayList)
					{
						if(tm.get("OBJECTID").toString().equals(id))
						{
							editTreeMap=tm;
							 for(Entry<String, Object> entry: m.entrySet()) 
							 {
								 LinearLayout row = new LinearLayout(mContext); 
								 row.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT,(int) (40*density)));
								 row.setOrientation(LinearLayout.HORIZONTAL);
								 TextView tv_name=new TextView(mContext);
								  
								 tv_name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,rowheight));
								 tv_name.setText(entry.getKey());
								 tv_name.setGravity(Gravity.CENTER);
								 fieldsname.add(entry.getKey());
								/// fieldvalue.add((String) entry.getValue());
								 row.addView(tv_name);
								 if(i>2)
								 {
									 EditText et_value=new EditText(mContext);
									 et_value.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, rowheight));
									 et_value.setId(i);
									 et_value.setGravity(Gravity.CENTER);
									 et_value.setBackgroundColor(color.white);
									 et_value.setTextColor(color.balck);
									 et_value.setText((CharSequence) entry.getValue());
									 row.addView(et_value);
								 }
								 else {
									 TextView tv_value=new TextView(mContext);
									 tv_value.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,rowheight));
									 tv_value.setGravity(Gravity.CENTER);
									 tv_value.setTextColor(color.balck);
									 tv_value.setText((CharSequence) entry.getValue());
									 row.addView(tv_value);
								}
								 
								 mLinearLayout.addView(row);
								 i++;
								 //entry.getKey().toString();
							 }
						}
					}
				
				
				
				
				//TextView tv_addname=(TextView) v.findViewById(R.id.tv_editname);
				
				//mLinearLayout.addv
				addDialog.setView(v);
				addDialog.show();
				
				Button btn_addpolygon=(Button) v.findViewById(R.id.btn_edit);
				 tv_addname=(TextView) v.findViewById(R.id.tv_editname);
				 btn_addpolygon.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
					   List<String> fieldsvalue=new ArrayList<String>();
					   for(int i=3;i<fieldsname.size();i++)
						{
							EditText  et_value=(EditText) v.findViewById(i);
							String value=et_value.getText().toString();
							if(value.equals("")||value==null)
							{
								editTreeMap.put(fieldsname.get(i), value);
								fieldsvalue.add("");
							}
							else {
								fieldsvalue.add(et_value.getText().toString());
								editTreeMap.put(fieldsname.get(i), value);
							}
						}
						
						addDialog.dismiss();
						
						boolean isadd=dbService.upDateAttributes(layername, id,fieldsname,fieldsvalue);
						if(isadd)
						{
							callout.refresh();
							Toast.makeText(mContext, "编辑成功", 0).show();
						} else {
							Toast.makeText(mContext, "编辑失败", 0).show();
						}
					}
				});
		}
	  
}
