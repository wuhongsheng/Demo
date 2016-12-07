package com.titan.adapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.titan.demo.DbService;
import com.titan.demo.DragDelItem;
import com.titan.demo.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*import com.esri.android.map.GraphicsLayer;
import com.esri.core.map.Graphic;
import com.otitan.android.DragDelItem;*/

public class NewLayerDataAdapter extends BaseAdapter {
	
	public static HashMap<Integer, Boolean> isSelected;
	private LayoutInflater Inflater=null;
	private List<String> Alarmlist;
	Context mContext;
	AlertDialog editeDialog;
	String layername;
	DbService dbService;
	Layer layer;
	List<TreeMap<String,Object>> layerdata;
	public  NewLayerDataAdapter(Context context,List<String> alarmlist,String layername,Layer gl,List<TreeMap<String,Object>> data) {
		isSelected = new HashMap<Integer, Boolean>();
		Inflater = LayoutInflater.from(context);
		this.mContext=context;
		Alarmlist=alarmlist;
		this.layername=layername;
		layer=gl;
		dbService=new DbService(mContext);
		layerdata=data;
		//initdata(Alarmlist);//初始化数据
	}

	public static void initdata(List<String> Alarmlist) {
		for (int i = 0; i < Alarmlist.size(); i++) {
			isSelected.put(i, false);
		}
	}

	@Override
	public int getCount() {
		return Alarmlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder=null;
		final int loc=position;
		View menuView=null;
		if (view == null) {
			view = View.inflate(mContext, R.layout.item_display, null);
			menuView =View.inflate(mContext,R.layout.swipemenu, null); 
			//合成内容与菜单
			view = new DragDelItem(view,menuView);
		
			// ���ViewHolder����
			holder = new ViewHolder(view);
			// ���벼�ֲ���ֵ��convertview
		/*	view = Inflater.inflate(R.layout.item_display, null);
			holder.cb = (CheckBox) view.findViewById(R.id.cb);
		    holder.tv_id=(TextView) view.findViewById(R.id.tv_id);
		    holder.tv_name=(TextView) view.findViewById(R.id.tv_name);
			view.setTag(holder);*/
		} else {
			// ȡ��holder
			holder = (ViewHolder) view.getTag();
		}
		//holder.xuhao.setText((position+1)+"");
	
		holder.tv_name.setText(Alarmlist.get(position).toString());
		int id =position+1;
		holder.tv_id.setText(id+"");
		
		// ����isSelected������checkbox��ѡ��״��
		//holder.cb.setChecked(getIsSelected().get(position));
		holder.tv_open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				editeDialog=new Builder(mContext).create();
				editeDialog.setCanceledOnTouchOutside(true);
				View view = View.inflate(mContext, R.layout.edit, null);
				editeDialog.setView(view);
				editeDialog.show();
				final TextView tv_editname=(TextView) view.findViewById(R.id.tv_editname);
				Button btn_edit=(Button) view.findViewById(R.id.btn_edit);
			
				btn_edit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						String editname=tv_editname.getText().toString();
						Alarmlist.remove(loc);
						Alarmlist.add(loc, editname);
						Map<String, String> fields=new HashMap<>();
						fields.put("name", editname);
						fields.put("id", loc+"");
						dbService.editData(layername, fields);
						notifyDataSetChanged();
						
						editeDialog.dismiss();
					}
				});
				Toast.makeText(mContext, "编辑:"+loc, Toast.LENGTH_SHORT).show();
			}
		});
		holder.tv_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dbService.deleteData(layername, Alarmlist.get(loc));
				Alarmlist.remove(loc);
				//int id=layer.getId();
				/*int[] ids=graphicsLayer.getGraphicIDs();
				Graphic ggGraphic=graphicsLayer.getGraphic(ids[loc]);
				graphicsLayer.removeGraphic(ids[loc]);*/
				FeatureLayer featureLayer = (FeatureLayer) layer;
				notifyDataSetChanged();
				
				
				Toast.makeText(mContext, "删除:"+loc, Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		NewLayerDataAdapter.isSelected = isSelected;
	}
	public final class ViewHolder {
		//public TextView xuhao;
		public CheckBox cb;
		public TextView tv_id;
		public TextView tv_name;
		public TextView tv_open,tv_del;
		public ViewHolder(View view) {
			tv_id = (TextView) view.findViewById(R.id.tv_id);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_open=(TextView)view.findViewById(R.id.tv_open);
			tv_del=(TextView)view.findViewById(R.id.tv_del);
			view.setTag(this);
		}
	
	}
//	public void notifyDataSetChanged(List<ReceiveAlarmInfo> receiveAlarmInfos) {
//		if(receiveAlarmInfos != null && receiveAlarmInfos.size()>0){
//			Alarmlist.addAll(receiveAlarmInfos);
//		}
//		initdata(Alarmlist);
//	}
	
}
