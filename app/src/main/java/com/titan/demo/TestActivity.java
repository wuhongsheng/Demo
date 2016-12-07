package com.titan.demo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.json.JSONObject;

import jsqlite.Callback;
import jsqlite.Database;
import abc.abc.abc.R.color;
import abc.abc.abc.R.style;
import android.R.integer;
import android.R.transition;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification.Style;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.TabWidget;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;


import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.google.gson.Gson;
import com.mylib.Util.ToastUtil;
import com.otitan.adapter.ChannelDb;
import com.otitan.adapter.LayerDataAdapter;
import com.otitan.adapter.NewLayerDataAdapter;
import com.otitan.android.DragDelListView;
import com.otitan.android.GeometryUtils;
import com.otitan.android.HdListView;
import com.otitan.android.HdListView.RemoveDirection;
import com.otitan.android.HdListView.RemoveListener;
import com.otitan.android.SpatialiteUtil;
import com.otitan.android.WktUtils;
import com.otitan.drawtools.DrawEvent;
import com.otitan.drawtools.DrawEventListener;
import com.otitan.drawtools.DrawTool;

public class TestActivity extends Activity implements View.OnClickListener,DrawEventListener{

	protected static final int LAYER_LOAD = 1;
	MapView mapView;
	MyTouchListener myTouchListener,myTouchListener1;
	//ArcGISTiledMapServiceLayer tilemap;
	GraphicsLayer graphicsLayer;
	private GraphicsLayer drawLayer;
    SimpleFillSymbol fillSymbol;
    SimpleLineSymbol lineSymbol;
    SimpleMarkerSymbol markerSymbol;
    WktUtils wkttools;
    Button addButton,displayButton,addlayer,addfield,newlayer,btn_confirm,btn_additem,testButton,addfeild_confirm;
    EditText et_name,et_fieldname,et_newlayername,et_newfieldname;
    TextView tv_fieldlist,tv_addname;
    
    Spinner sp_type,sp_fieldtype,sp_newlayertype,sp_newfieldtype,layertab;
    ListView lv_display;
    TabHost tabhost;
    ViewPager viewPager;
   // DragDelListView draglv;
    ListView draglv;
    AlertDialog editeDialog;
    
    Graphic addGraphic;
    
    String [] fields;
    RadioGroup rgChannel;
    HorizontalScrollView hvChannel;
    List<Map<String, String>> fieldsList;
    List<String> attriList;
    List<TreeMap<String, Object>> layerdatalist;
    Map<String, Object>  attribute;
    //Map<String, String> 
    //保存添加图层的类型
    Map<String, String> addlayertype;
    //String 
    String name,type,newlayername,newlayertype,currentlayer;
    PopupWindow popupwindow;
    //db service
    DbService dbService;
    Future<FeatureResult> resultfetures;
    FeatureLayer fl = null;
    int lastid;
    boolean additem=false;
    //图层数据
    List<Geometry> geometries;
    List<String> layerdata,layersname;
    Map<String, String> columnsnameMap;//字段表
    String layertype,wkt;//图层类型
    List<String> fieldsname;//图层字段名
    //本地TPK路径
    String titlePath;
    AlertDialog addDialog;
     DrawTool drawtools;
     //
     View view;
    //Adapter
     ArrayAdapter<String> layertabAdapter;
     layereditAdapter mlayereditAdapter;
     //Render
     UniqueValueRenderer uvrenderer=null;
     UniqueValue uv1, uv2;
     // The set of unique attributes for rendering
     String[] uniqueAttribute1 = new String[1];
     String[] uniqueAttribute2 = new String[1];
     //控制图层数据循环
     int i=0;
     //程序一加载，直接在主线程中创建Handler
     private Handler handler = new Handler() {
         public void handleMessage(Message msg) {
             switch (msg.what) {
             case LAYER_LOAD:
            	 Toast.makeText(TestActivity.this, "图层已加载", 0).show();
                 // text.setText("Nice to meet you");
                 break;
             default:
                break;
             }
         }
     };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 		setContentView(R.layout.main);
 		
 	    // Unique Values are objects containing the unique properties
 		 uvrenderer = new UniqueValueRenderer();
 	     // setting the field for the unique values
        uvrenderer.setField1("name");

        uv1 = new UniqueValue();
        uv2 = new UniqueValue();
        
        // Defining the Unique attributes for classifier
        uniqueAttribute1[0] = "kaj";
        uv1.setDescription("jkkd");
        uv1.setValue(uniqueAttribute1);
        uniqueAttribute2[0] = "kaj";
        uv2.setDescription("jkkd");
        uv2.setValue(uniqueAttribute2);
        
        // The symbol definition for each region
        final SimpleMarkerSymbol symbol1 = new SimpleMarkerSymbol(Color.BLUE, 3, STYLE.CIRCLE);
        final SimpleMarkerSymbol symbol2 = new SimpleMarkerSymbol(Color.RED, 3, STYLE.CIRCLE);
        // Setting the symbol to the unique values defined
        uv1.setSymbol(symbol1);
        uv2.setSymbol(symbol2);
        
        uvrenderer.addUniqueValue(uv1);
        uvrenderer.addUniqueValue(uv2);

		initView();
		//mapView.setMapBackground(0xffffff, 0xffffff, 3, 3);
		
		//viewPager=(ViewPager) findViewById(R.id.viewpager);
		//mapView.setBackgroundColor(color.blue);
		//mapView.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgis.com/P3ePLMYs2RVChkJx/arcgis/rest/services/Wildfire/FeatureServer"));
        ArcGISTiledMapServiceLayer tiledMapServiceLayer=new ArcGISTiledMapServiceLayer("http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer");
        mapView.addLayer(tiledMapServiceLayer);
    	this.drawLayer = new GraphicsLayer();
		this.mapView.addLayer(this.drawLayer);
        try {
			titlePath = ResourcesManager.getInstance(TestActivity.this).getArcGISLocalTiledLayerPath();
			String fileDir = ResourcesManager.getInstance(TestActivity.this).getPath()[1] + "/Otitan";
			if(!checkDataBase(fileDir))
			{
				CopyDatabase(fileDir);
			}
			//CopyDatabase(fileDir); 
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
       /* ArcGISLocalTiledLayer localTiledLayer = new ArcGISLocalTiledLayer(titlePath);
        mapView.addLayer(localTiledLayer);*/
        /*Point point = new Point(117.000, 27.222);
		Point gpoint = (Point) GeometryEngine.project(point,SpatialReference.create(4326),SpatialReference.create(2343));
		mapView.setExtent(gpoint);*/
        
      /*  ArcGISLocalTiledLayer arcGISLocalTiledLayer = new ArcGISLocalTiledLayer(titlePath);
         //arcGISLocalTiledLayer.setVisible(true);
       
        
		mapView.addLayer(arcGISLocalTiledLayer);*/
       // mapView.setExtent(new Envelope(190603.98210,2699083.86405,1126998.427,3261862.41984));
        graphicsLayer = new GraphicsLayer();  
        mapView.addLayer(graphicsLayer);  
        //获取图层名称
        this.drawtools = new DrawTool(this.mapView);
		// ����ʵ��DawEventListener�ӿ�
		this.drawtools.addEventListener(this);
         
        // symbol  
        fillSymbol =new SimpleFillSymbol(color.ccc_red);
        lineSymbol=new SimpleLineSymbol(Color.RED, 20);
        markerSymbol = new SimpleMarkerSymbol(Color.RED, 10, STYLE.CIRCLE);
      
       /* markerSymbol = new SimpleMarkerSymbol(Color.RED, 16, SimpleMarkerSymbol.STYLE.CIRCLE);  
        markerSymbol.setOutline(new SimpleLineSymbol(Color.BLACK, 1)); */
        //fillSymbol =new fi
        //loadDataFromSpatialite(); 
        //获取设备位置
        LocationDisplayManager ls =mapView.getLocationDisplayManager();
        //ls.setLocationListener(new MyLocationListener());
        ls.start();
        ls.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION );
        Location location=ls.getLocation();
	}
	   private void initView(){
		   mapView=(MapView) findViewById(R.id.map);
		   tabhost=(TabHost) findViewById(R.id.tabhost);
			addButton=(Button) findViewById(R.id.btn_add);
			displayButton=(Button) findViewById(R.id.btn_layerdisplay);
			
			addfield=(Button) findViewById(R.id.btn_addfield);
			newlayer=(Button) findViewById(R.id.btn_newlayer);
			addfeild_confirm =(Button)findViewById(R.id.btn_addfeild_confirm);
			btn_additem=(Button) findViewById(R.id.btn_additem);
			testButton=(Button) findViewById(R.id.btn_test);
			
			
			et_fieldname=(EditText) findViewById(R.id.et_fieldname);
			et_newlayername=(EditText) findViewById(R.id.et_newlayername);
			et_newfieldname=(EditText) findViewById(R.id.et_newfieldname);
			 et_name =(EditText) findViewById(R.id.et_fieldname);
			 sp_type =(Spinner)findViewById(R.id.sp_fieldtype);
			
			sp_fieldtype=(Spinner) findViewById(R.id.sp_fieldtype);
			sp_newlayertype=(Spinner) findViewById(R.id.sp_newlayertype);
			sp_newfieldtype=(Spinner) findViewById(R.id.sp_newfieldtype);
			layertab=(Spinner) findViewById(R.id.sp_layertab);
			
			tv_fieldlist=(TextView) findViewById(R.id.tv_fieldtable);
			
			lv_display=(ListView) findViewById(R.id.lv_dispaly);
	        rgChannel=(RadioGroup)super.findViewById(R.id.rgChannel);
	        viewPager=(ViewPager)super.findViewById(R.id.vpNewsList);
	        hvChannel=(HorizontalScrollView)super.findViewById(R.id.hvChannel);
	       // draglv=(DragDelListView) findViewById(R.id.hd_lv);
	        draglv=(ListView) findViewById(R.id.hd_lv);
	        //临时数据存储
	        addlayertype=new HashMap<>();
	      
	      
	        dbService=new DbService(this);
	        fieldsList=new ArrayList<Map<String,String>>();
	        addButton.setOnClickListener(this);
			displayButton.setOnClickListener(this);
			//addlayer.setOnClickListener(this);
			addfield.setOnClickListener(this);
			newlayer.setOnClickListener(this);
			addfeild_confirm.setOnClickListener(this);
			btn_additem.setOnClickListener(this);
			testButton.setOnClickListener(this);
			
			
			 //调用 TabHost.setup()
	        tabhost.setup();
	        //创建Tab标签
	        /*tabhost.addTab(tabhost.newTabSpec("one").setIndicator("红色").setContent(R.id.widget_layout_red));
	        tabhost.addTab(tabhost.newTabSpec("two").setIndicator("黄色").setContent(R.id.widget_layout_yellow));*/
	        tabhost.addTab(tabhost.newTabSpec("one").setIndicator("图层编辑").setContent(R.id.layout_diapaly));
	        tabhost.addTab(tabhost.newTabSpec("three").setIndicator("添加图层").setContent(R.id.layout_addlayer));
	        tabhost.addTab(tabhost.newTabSpec("two").setIndicator("添加字段").setContent(R.id.layout_addcolumn));
	        
	        tabhost.setOnTabChangedListener(new OnTabChangeListener() {
				
				public void onTabChanged(String id) {
					if(id.equals("one"))
					{
						//List<String> layerdata = dbService.getLayerData(currentlayer);
						Toast.makeText(TestActivity.this, "one", 0).show();
					}
				}
			});
	        /*
			draglv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
					LayerDataAdapter.ViewHolder holder = (LayerDataAdapter.ViewHolder) arg1.getTag();
					holder.cb.toggle();
					// 将CheckBox的选中状况记录下来
					LayerDataAdapter.getIsSelected().put(position, true);
					int dd=position;
					
					//fl.clearSelection();
					graphicsLayer.clearSelection();
					
					int[] idsArr = graphicsLayer.getGraphicIDs();
					int [] index={idsArr[dd]};
					graphicsLayer.setSelectionColor(Color.BLUE);
					graphicsLayer.setSelectedGraphics(index, true);
					//graphicsLayer.updateGraphic(idsArr[lastid], fillSymbol);
					SimpleFillSymbol fillSymbol2 =new SimpleFillSymbol(Color.BLUE);
					graphicsLayer.updateGraphic(idsArr[dd], fillSymbol2);
					lastid=position;
					Graphic graphic = graphicsLayer.getGraphic(idsArr[position]);
					Geometry gg = (Geometry) graphic.getGeometry();
					
					//mapView.centerAt(centerPt, true);
					
					QueryParameters q = new QueryParameters();//定义一个Query对象用于下面的查询操作
					// 定义它的相关参数及其条件
					q.setWhere("id='"+index+""+"'");
					
				   // q.setWhere("SHAPE_Length>4000");
					q.setReturnGeometry(true);
					q.setInSpatialReference(mapView.getSpatialReference());
					q.setGeometry(mapView.getExtent());
					//q.setOutFields(new String[]{"SHAPE_Length","OBJECTID"});
					//q.setGeometry(p);
					q.setSpatialRelationship(SpatialRelationship.INTERSECTS);
					//执行选择查询操作
					resultfetures = graphicsLayer.se.selectFeatures(q, FeatureLayer.SelectionMode.NEW, new CallbackListener<FeatureResult>() {
						@Override
						public void onCallback(FeatureResult arg0) {
							Log.e("","enter:getName:"+Thread.currentThread().getName());
							
							Log.e("","enter:成功");
						}
						@Override
						public void onError(Throwable arg0) {
							Log.e("","enter:getName:"+Thread.currentThread().getName());
							
							Log.e("","enter:失败");
						}
					});
				}
			});*/
			//图层切换
			layersname=dbService.getLayerName();
	        layertabAdapter=new ArrayAdapter<>(this, R.drawable.otitan_spinner_item, layersname);
	        layertab.setAdapter(layertabAdapter);
	      
			layertab.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
					/*new Thread(){
						@Override
						public void run() {
							Log.e("threadname:", Thread.currentThread().getName());
							additem = false;
							graphicsLayer.removeAll();
							currentlayer= layertab.getSelectedItem().toString();
							Graphic graphic=null;
							
						    layerdata = dbService.getLayerData(currentlayer);
                           
						    geometries=dbService.loadLayer(currentlayer);
							if(geometries!=null&&geometries.size()!=0&&layerdata!=null)
							{
								String ddString=layerdata.get(i);
							   layertype=geometries.get(0).getType().toString();
							   
							 for(Geometry g:geometries)
							  {
								
								
								switch (layertype) {
								case "POLYGON":
									//new Graphic(g, fillSymbol, attribute);
									 attribute.put("name", layerdata.get(i));
									 i++;
									 graphic = new Graphic(g,fillSymbol,attribute); 
									 graphicsLayer.addGraphic(graphic);
									break;
								case "POINT":
									 graphic = new Graphic(g,markerSymbol,attribute); 
									 graphicsLayer.addGraphic(graphic);
									break;
								case "POLYLINE":
									 graphic = new Graphic(g,lineSymbol,attribute); 
									 graphicsLayer.addGraphic(graphic);
									break;
								default:
									break;
								}
							  }
							// Toast.makeText(TestActivity.this, "图层已加载", 0).show();
							}else {
								layertype=addlayertype.get(currentlayer);
							   // Toast.makeText(TestActivity.this, "图层为空", 0).show();
							}
							mapView.setOnTouchListener(new MyTouchListener(TestActivity.this, lv_display,mapView,graphicsLayer,layerdata,layertype,additem,currentlayer));
					       // lv_display.setAdapter(new LayerDataAdapter(TestActivity.this,layerdata));
						}
					};*/
					 runOnUiThread(new  Runnable() {
						public void run() {
							Log.e("threadname:", Thread.currentThread().getName());
							//mapView.recycle();
							additem = false;
							layerdatalist=new ArrayList<TreeMap<String,Object>>() ;
							graphicsLayer.removeAll();
							currentlayer= layertab.getSelectedItem().toString();
							columnsnameMap=new HashMap<>();
							columnsnameMap=dbService.getColumnsName(currentlayer);//获取可图层可编辑字段
							layerdatalist=dbService.loadLayerData(currentlayer);
							Graphic graphic=null;
							
						    //layerdata = dbService.getLayerData(currentlayer);
						    
                           
						    //geometries=dbService.loadLayer(currentlayer);
						    List<Geometry> gs=new ArrayList<Geometry>();
						    List<Map<String, Object>> attributeList=new ArrayList<>();
						    attriList=new ArrayList<>();
						    //解析图层数据
						   for(TreeMap<String, Object> m:layerdatalist)
						   {
							   gs.add((Geometry) m.get("AsText(t.shap)"));//获取图层空间数据
							   m.remove("AsText(t.shap)");
							   attriList.add( (String) m.get("OBJECTID"));
							   //Set<String> keySet = m.keySet();
							   
						   }
						layertype = columnsnameMap.get("shap");
						switch (columnsnameMap.get("shap")) {
						case "POINT":
							layertype = "POINT";
							break;
						case "LINESTRING":
							layertype = "POLYLINE";
							break;
						case "POLYGON":
							layertype = "POLYGON";
						default:
							break;
						}
							if(gs!=null&&gs.size()!=0&&layerdatalist!=null)
							{
								//String ddString=layerdata.get(i);
							  // layertype=gs.get(0).getType().toString();
							 int y=0;
							 for(Geometry g:gs)
							  {
								
								 attribute=new HashMap<>();
								 attribute.put("id", attriList.get(y));
								switch (layertype) {
								case "POLYGON":
									//new Graphic(g, fillSymbol, attribute);
									 
									 graphic = new Graphic(g,fillSymbol,attribute); 
									 graphicsLayer.addGraphic(graphic);
									break;
								case "POINT":
									 graphic = new Graphic(g,markerSymbol,attribute); 
									 graphicsLayer.addGraphic(graphic);
									break;
								case "POLYLINE":
									 graphic = new Graphic(g,lineSymbol,attribute); 
									 graphicsLayer.addGraphic(graphic);
									break;
								default:
									break;
								}
								y++;
							  }
							 //graphicsLayer.setRenderer(uvrenderer);
							 Toast.makeText(TestActivity.this, "图层已加载", 0).show();
							}else {
								//layertype=addlayertype.get(currentlayer);
							    Toast.makeText(TestActivity.this, "图层为空", 0).show();
							}
						     myTouchListener=new MyTouchListener(TestActivity.this, lv_display,mapView,graphicsLayer,layerdata,layertype,additem,currentlayer,layerdatalist);
							mapView.setOnTouchListener(myTouchListener);
					       // lv_display.setAdapter(new LayerDataAdapter(TestActivity.this,layerdata));
					        //draglv.setAdapter(new NewLayerDataAdapter(TestActivity.this,layerdata ,currentlayer,graphicsLayer,layerdatalist));
							 mlayereditAdapter=new layereditAdapter(TestActivity.this,columnsnameMap,currentlayer,layertype);
							draglv.setAdapter(mlayereditAdapter);
						}
					});

					
				}
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
	        rgChannel.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
	            public void onCheckedChanged(RadioGroup group, int checkedId) {
	                viewPager.setCurrentItem(checkedId);        
	            }
	        });
	        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				
	        	  
				public void onPageSelected(int arg0) {
					
				}
				
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					
				}
				
				public void onPageScrollStateChanged(int position) {
					   setTab(position);
				}
			});
	       /* initTab();//动态产生RadioButton
	        initViewPager();*/
	        rgChannel.check(0);
	    }
	  
	    private void setTab(int idx){
	        RadioButton rb=(RadioButton)rgChannel.getChildAt(idx);
	        rb.setChecked(true);
	        int left=rb.getLeft();
	        int width=rb.getMeasuredWidth();
	        DisplayMetrics metrics=new DisplayMetrics();
	        super.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        int screenWidth=metrics.widthPixels;
	        int len=left+width/2-screenWidth/2;
	        hvChannel.smoothScrollTo(len, 0);//滑动ScroollView
	    }
	private boolean loadDataFromSpatialite() {
		 String dbFile;
		try {
 			dbFile = ResourcesManager.getInstance(TestActivity.this).getPath()[0] + "/Otitan/polygon.sqlite";
			//getResources().getAssets()
         Class.forName("jsqlite.JDBCDriver").newInstance();  
         final Database db = new jsqlite.Database();
         db.open(dbFile, jsqlite.Constants.SQLITE_OPEN_READWRITE);  
          
         Callback cb = new Callback() {

			public void columns(String[] arg0) {
				
			}

			public boolean newrow(String[] arg0) {
				wkttools=new WktUtils();
				//polygon to json
				//String json=wkttools.getMULTIPOLYGONWktToJson(arg0[1], 102100);
				//line to json
				String json=wkttools.getMULTILINESTRINGWktToJson(arg0[1], 102100);
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
				 Geometry ggGeometry=WktUtils.WKTToGeometry(arg0[1]);
				
				 Graphic graphic = new Graphic(ggGeometry,lineSymbol); 
				 graphicsLayer.addGraphic(graphic);
              
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
         String query = "select t.name,AsText(t.shap)from '"+currentlayer+"' t";  
        //String query = "select t.pkuid,AStext(t.shap) from tt t"; 
         db.exec(query, cb); 
        
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	// �������ݿ⵽SD��
		private void CopyDatabase(String fileDir) {
			try {
 				InputStream db = getResources().getAssets().open("db.sqlite");
				FileOutputStream fos = new FileOutputStream(fileDir + "/db.sqlite");
				byte[] buffer = new byte[8129];
				int count = 0;
				// ��ʼ�������ݿ�

				while ((count = db.read(buffer)) >= 0) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				db.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		// 检查本地数据库是否存在
		private boolean checkDataBase(String fileDir) {
			SQLiteDatabase checkDB = null;

			String myPath = fileDir + "/db.sqlite";
			try {
				checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			} catch (SQLiteException e) {
				// database does't exist yet.
			}
			if (checkDB != null) {
				checkDB.close();
			}
			return checkDB != null ? true : false;
		}
		public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			runOnUiThread(new Runnable() {

				public void run() {
					initmPopupWindowView();
					//popupwindow.showAtLocation(mapView, Gravity.RIGHT, 0, 200);
					popupwindow.showAsDropDown(addButton, 0, 0);

				}
			});
			break;
		case R.id.btn_addfeild_confirm:
			name = et_name.getText().toString();
			type = sp_type.getSelectedItem().toString();
			
			
			boolean isadd = dbService.addcolumn(currentlayer,name, type);
			if (isadd) {
				Toast.makeText(TestActivity.this, "添加字段成功", 0).show();
				columnsnameMap.put(name, type);
				mlayereditAdapter.mnotifyDataSetChanged(columnsnameMap);
				//mlayereditAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(TestActivity.this, "添加字段失败", 0).show();
				//graphicsLayer.removeGraphic((int) addGraphic.getUid());
			}
			break;
		case R.id.btn_layerdisplay:
			runOnUiThread(new Runnable() {

				public void run() {
					initdisplayPopWindow();
					//popupwindow.showAtLocation(mapView, Gravity.RIGHT, 0, 200);
					popupwindow.showAsDropDown(addButton, 0, 0);

				}

			});
			break;
		
		case R.id.layout_diapaly:
			Toast.makeText(TestActivity.this, "图层创建成功", 0).show();
			
			//lv_display.setAdapter(adapter);
			break;
		case R.id.btn_addfield:
			String fieldname =et_newfieldname.getText().toString();
			String fieldtype  =sp_newfieldtype.getSelectedItem().toString();
			String fieldlist="";
			boolean isrepeat=false;
			if(fieldname.equals("")||fieldname==null)
			{
				Toast.makeText(TestActivity.this, "字段不能为空", 0).show();
				
			}else {
				for(Entry<String, String> m:columnsnameMap.entrySet())
				{
					if(m.getKey().equals(name))
					{
						Toast.makeText(TestActivity.this, "字段名重复", 0).show();
						isrepeat=true;
					}
					
				}
				if(!isrepeat)
				{
					Map<String, String> fieldMap=new HashMap<String, String>();
					fieldMap.put("name", fieldname);
					fieldMap.put("type", fieldtype);
					fieldsList.add(fieldMap);
					
					for(Map<String, String> field:fieldsList)
					{
						 fieldlist+="字段名:"+field.get("name")+"  类型："+field.get("type")+"\n";
						
					}
					tv_fieldlist.setText(fieldlist);
					et_newfieldname.getText().clear();
					sp_newfieldtype.setSelection(0);
				}
			
				
			}		
			break;
		case R.id.btn_additem:
			if(additem)
			{  additem = false;
			   drawtools.deactivate();
			   layerdatalist=dbService.loadLayerData(currentlayer);
			  // myTouchListener.refresh();
			  // myTouchListener.
			   myTouchListener1=new MyTouchListener(TestActivity.this, lv_display,mapView,graphicsLayer,layerdata,layertype,additem,currentlayer,layerdatalist);
			   mapView.setOnTouchListener(myTouchListener1);
			   Toast.makeText(this, "添加数据已功能关闭", 0).show();
			  
			   //mapView.setOnTouchListener(new MyTouchListener(TestActivity.this,lv_display, mapView,graphicsLayer,layerdata,layertype,additem,currentlayer));
			}
			else {
				Toast.makeText(this, "添加数据功能打开", 0).show();
				additem = true;
				//layertype =columnsnameMap.get("shap"); //获取图层类型
				//mapView.setOnTouchListener(new MyTouchListener(TestActivity.this,lv_display, mapView,graphicsLayer,layerdata,layertype,additem,currentlayer));
				switch (layertype) {
				case "POLYGON":
					Toast.makeText(this, "当前绘制模式为：POLYGON", 0).show();
					drawtools.setFillSymbol(fillSymbol);
					drawtools.activate(DrawTool.POLYGON);
					
					break;
				case "POLYLINE":
					Toast.makeText(this, "当前绘制模式为：POLYLINE", 0).show();
					drawtools.setLineSymbol(lineSymbol);
					drawtools.activate(DrawTool.POLYLINE);
					
					break;
				case "POINT":
					Toast.makeText(this, "当前绘制模式为：POINT", 0).show();
					drawtools.setMarkerSymbol(markerSymbol);
					drawtools.activate(DrawTool.POINT);
					
					break;
				default:
					Toast.makeText(this, "图层类型不存在", 0).show();
					break;
				}
			
				
			   
			    
			}
			
			break;
		case R.id.btn_edit:
			List<String> fieldsvalue=new ArrayList<String>();
			for(int i=3;i<fieldsname.size();i++)
			{
				EditText  et_value=(EditText) view.findViewById(i);
				String value=et_value.getText().toString();
				if(value.equals("")||value==null)
				{
					fieldsvalue.add("");
				}
				else {
					fieldsvalue.add(et_value.getText().toString());
				}
			}
			HashMap<String, String> listfields=new HashMap<String, String>();
		    String nameString =tv_addname.getText().toString();
		    
		    if(nameString==null)
		    {
		    	nameString="";
		    }
		    //listview 填充数据
		    //layerdata.add(nameString);
		    //lv_display.setAdapter(new LayerDataAdapter(this,layerdata));
		    listfields.put("name",nameString);
			
			addDialog.dismiss();
			
			//boolean isadd2=dbService.addData(currentlayer,wkt,listfields,layertype);
			boolean isadd2=dbService.addData(currentlayer,wkt,fieldsname,fieldsvalue,layertype);
			if(isadd2)
			{
				Toast.makeText(this, "添加成功", 0).show();
				 attribute=new HashMap<>();
				 attribute.put("id", attriList.size()+"");
				graphicsLayer.updateGraphic((int) addGraphic.getUid(), attribute);
			} else {
				Toast.makeText(this, "添加失败", 0).show();
				graphicsLayer.removeGraphic((int) addGraphic.getUid());
			}
			break;
		//additem	
		case R.id.btn_addconfirm:
			List<String> fieldvalues=new ArrayList<String>();
			for(int i=0;i<fieldsname.size();i++)
			{
				EditText  et_value=(EditText) view.findViewById(i);
				String value=et_value.getText().toString();
				if(value.equals("")||value==null)
				{
					fieldvalues.add("");
				}
				else {
					fieldvalues.add(value);
				}
			}
			Map<String, String> fieldsmap=new HashMap<String, String>();
		    String name =tv_addname.getText().toString();
		    
		    if(name==null||name.equals(""))
		    {
		    	name="";
		    }
		    //listview 填充数据
		    //layerdata.add(nameString);
		    //lv_display.setAdapter(new LayerDataAdapter(this,layerdata));
		    fieldsmap.put("name",name);
		    
			addDialog.dismiss();
			
			//boolean isadd2=dbService.addData(currentlayer,wkt,listfields,layertype);
			boolean isadditem=dbService.addData(currentlayer,wkt,fieldsname,fieldvalues,layertype);
			if(isadditem)
			{
				Toast.makeText(this, "添加数据成功", 0).show();
				
				 attribute=new HashMap<>();
				 attribute.put("id", layerdatalist.size()+1+"");
				 Graphic newaddGraphic=new Graphic(addGraphic.getGeometry(), fillSymbol, attribute);
				 int[] dd=graphicsLayer.getGraphicIDs();
				 long ff=addGraphic.getId();
				 int f=addGraphic.getUid();
				 graphicsLayer.updateGraphic(dd[0], newaddGraphic);
				// graphicsLayer.updateGraphic((int) addGraphic.getUid(), attribute);
			} else {
				Toast.makeText(this, "添加数据失败", 0).show();
				graphicsLayer.removeGraphic(graphicsLayer.getGraphicIDs()[0]);
			}
			
			break;
		case R.id.btn_newlayer:
			
			
			newlayername=et_newlayername.getText().toString();
			//添加图层类型
			newlayertype=sp_newlayertype.getSelectedItem().toString();
			if(newlayername.equals("")||newlayername==null)
			{
				Toast.makeText(TestActivity.this, "图层名称不能为空", 0).show();
				
			}else {
				switch (newlayertype) {
				case "面":
	            	newlayertype="POLYGON";
	            	addlayertype.put(newlayername, "POLYGON");
					break;
				case "点":
					newlayertype="POINT";
					addlayertype.put(newlayername, "POINT");
					break;
		        case "线":
		        	newlayertype="POLYLINE";
		        	addlayertype.put(newlayername, "POLYLINE");
					break;           
				default:
					break;
				}
				boolean isnew=dbService.newlayer(newlayername,newlayertype,fieldsList);
				if (isnew) {
					Toast.makeText(TestActivity.this, "图层创建成功", 0).show();
					layersname=dbService.getLayerName();
					//layertabAdapter.notifyDataSetChanged();
					//动态添加数据

					layertabAdapter.insert(newlayername, 0);
					//layertabAdapter.add(newlayername);
					layertab.setAdapter(layertabAdapter);
					
				} else {
					Toast.makeText(TestActivity.this, "图层创建失败", 0).show();
				}
			}
			break;
		//取消添加数据
		case R.id.btn_addconcel:
			addDialog.dismiss();
			int[] idsArr = graphicsLayer.getGraphicIDs();
			int t=idsArr[idsArr.length-1];
			
			
			graphicsLayer.removeGraphic(idsArr[0]);
			Toast.makeText(TestActivity.this, "已取消添加数据", 0).show();
			break;
		case R.id.btn_test:
			dbService.testaddData();
			break;
		/*	case R.id.sp_layertab:
				graphicsLayer.removeAll();
				currentlayer= layertab.getSelectedItem().toString();
				Graphic graphic=null;
				List<Geometry> geometries=dbService.loadLayer(currentlayer);
				for(Geometry g:geometries)
				{
					switch (currentlayer) {
					case "POINT":
						 graphic = new Graphic(g,markerSymbol); 
						break;
					case "LINESTRING":
						 graphic = new Graphic(g,lineSymbol); 
						break;
					case "POLYGON":
						 graphic = new Graphic(g,fillSymbol); 
						break;
					default:
						break;
					}
					
					 
					 graphicsLayer.addGraphic(graphic);
				}
				break;*/
		default:
			break;
		}
		}
		private void initdisplayPopWindow() {
			if(popupwindow!=null)
			{
				popupwindow.dismiss();
			}
			View displayView= LayoutInflater.from(TestActivity.this).inflate(R.layout.display, null);
            popupwindow =new PopupWindow(displayView,300, 700);
			
			popupwindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.white));
			
			//popupwindow.setAnimationStyle(R.style.AnimationFade);
			popupwindow.setFocusable(true);
			popupwindow.setOutsideTouchable(true);
			
		}
		private void initmPopupWindowView() {
			if(popupwindow!=null)
			{
				popupwindow.dismiss();
			}
			LayoutInflater inflater= LayoutInflater.from(TestActivity.this);
			View popView=inflater.inflate(R.layout.addcolumn, null);
			 et_name =(EditText) popView.findViewById(R.id.et_fieldname);
			 sp_type =(Spinner) popView.findViewById(R.id.sp_fieldtype);
			 
			Button btn_confirm =(Button) popView.findViewById(R.id.btn_addfeild_confirm);		
			btn_confirm.setOnClickListener(this);
			popupwindow =new PopupWindow(popView,300, 700);
			
			popupwindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.white));
			
			//popupwindow.setAnimationStyle(R.style.AnimationFade);
			popupwindow.setFocusable(true);
			popupwindow.setOutsideTouchable(true);
		}
		@Override
		public void handleDrawEvent(DrawEvent event) {
			addGraphic=event.getDrawGraphic();
			/* attribute=new HashMap<>();
			 attribute.put("id", attriList.get(y));
			 Graphic g=new Graphic(geometry, symbol)*/
			this.graphicsLayer.addGraphic(addGraphic);
			//this.drawLayer.addGraphic(addGraphic);
			int[] tt = graphicsLayer.getGraphicIDs();
			 addDialog=new AlertDialog.Builder(TestActivity.this).create();
			 addDialog.setCanceledOnTouchOutside(false);
				//LinearLayout layout =new LinearLayout(mContext);
			    wkt=WktUtils.GeometryToWKT(addGraphic.getGeometry());
			      view = View.inflate(TestActivity.this, R.layout.additem, null);
				LinearLayout mLinearLayout = (LinearLayout) view.findViewById(R.id.add_LinearLayout); 
				 fieldsname=new ArrayList<String>();
				
				//String [][] fieldsmap=new String [][]{};
				//TreeMap<String, Object> m=layerdatalist.get(0);
				 
				int j=0;
				 for(Entry<String, String> entry: columnsnameMap.entrySet()) 
				 {
					 float density = getResources().getDisplayMetrics().density;//获取屏幕密度
					 LinearLayout row = new LinearLayout(this); 
					 if(j!=columnsnameMap.size()-1)//去除shap列
					 {
					 
					 row.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT,(int) (40*density)));
					 row.setOrientation(LinearLayout.HORIZONTAL);
					 TextView tv_name=new TextView(this);
					  
					 tv_name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, (int) (40*density)));
					 tv_name.setText(entry.getKey());
					 tv_name.setWidth(LayoutParams.WRAP_CONTENT+(int) (60*density));
					 tv_name.setGravity(Gravity.CENTER);
					 fieldsname.add(entry.getKey());
					/// fieldvalue.add((String) entry.getValue());
					 row.addView(tv_name);
					 EditText et_value=new EditText(this);
					 et_value.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) (35*density)));
					 et_value.setId(j);
					 et_value.setGravity(Gravity.CENTER);
					 et_value.setBackgroundColor(color.white);
					 et_value.setTextColor(color.balck);
					 row.addView(et_value);
					 
					 mLinearLayout.addView(row);
						
					 }
				
					/* if(i>2)
					 {
						 EditText et_value=new EditText(this);
						 et_value.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) (35*density)));
						 et_value.setId(i);
						 et_value.setGravity(Gravity.CENTER);
						 et_value.setBackgroundColor(color.white);
						 et_value.setTextColor(color.balck);
						 row.addView(et_value);
						 
						 mLinearLayout.addView(row);
					 }
					 else {
						 TextView tv_value=new TextView(this);
						 tv_value.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,(int) (40*density)));
						 tv_value.setGravity(Gravity.CENTER);
						 tv_value.setTextColor(color.balck);
						 row.addView(tv_value);
						 mLinearLayout.addView(row);
					}*/
					 j++;
					 //entry.getKey().toString();
				 }
				addDialog.setView(view);
				addDialog.show();
				Button btn_add=(Button) view.findViewById(R.id.btn_addconfirm);
				Button btn_addconcle = (Button) view.findViewById(R.id.btn_addconcel);
				 tv_addname=(TextView) view.findViewById(R.id.tv_addname);
			    btn_add.setOnClickListener(this);
				btn_addconcle.setOnClickListener(this);
			
		}
	

	
}