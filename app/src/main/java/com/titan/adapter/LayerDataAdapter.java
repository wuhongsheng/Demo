package com.titan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.titan.demo.R;

import java.util.HashMap;
import java.util.List;

public class LayerDataAdapter extends BaseAdapter {
	
	public static HashMap<Integer, Boolean> isSelected;
	private LayoutInflater Inflater=null;
	private List<String> Alarmlist;
	public  LayerDataAdapter(Context context,List<String> alarmlist) {
		isSelected = new HashMap<Integer, Boolean>();
		Inflater = LayoutInflater.from(context);
		Alarmlist=alarmlist;
		initdata(Alarmlist);//初始化数据
	}

	public static void initdata(List<String> Alarmlist) {
		if(Alarmlist!=null){
			for (int i = 0; i < Alarmlist.size(); i++) {
				isSelected.put(i, false);
			}
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
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder=null;
		if (view == null) {
			// ���ViewHolder����
			holder = new ViewHolder();
			// ���벼�ֲ���ֵ��convertview
			view = Inflater.inflate(R.layout.item_display, null);
			holder.cb = (CheckBox) view.findViewById(R.id.cb);
		    holder.tv_id=(TextView) view.findViewById(R.id.tv_id);
		    holder.tv_name=(TextView) view.findViewById(R.id.tv_name);
			view.setTag(holder);
		} else {
			// ȡ��holder
			holder = (ViewHolder) view.getTag();
		}
		//holder.xuhao.setText((position+1)+"");
	
		holder.tv_name.setText(Alarmlist.get(position).toString());
		int id =position+1;
		holder.tv_id.setText(id+"");
		
		// ����isSelected������checkbox��ѡ��״��
		holder.cb.setChecked(getIsSelected().get(position));
		return view;
	}
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		LayerDataAdapter.isSelected = isSelected;
	}
	public final class ViewHolder {
		//public TextView xuhao;
		public CheckBox cb;
		public TextView tv_id;
		public TextView tv_name;
	
	}
//	public void notifyDataSetChanged(List<ReceiveAlarmInfo> receiveAlarmInfos) {
//		if(receiveAlarmInfos != null && receiveAlarmInfos.size()>0){
//			Alarmlist.addAll(receiveAlarmInfos);
//		}
//		initdata(Alarmlist);
//	}

}
