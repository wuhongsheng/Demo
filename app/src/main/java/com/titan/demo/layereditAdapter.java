package com.titan.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.otitan.adapter.NewLayerDataAdapter.ViewHolder;
import com.otitan.android.DragDelItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**   
* @Title: layereditAdapter.java 
* @Package abc.abc.abc 
* @Description: 
* @author whs HDstudio  
* @date 2015-9-24 上午11:10:13 
* @version V1.0   
*/
public class layereditAdapter extends BaseAdapter implements ListAdapter {
	
	

	Context mContext;
	Map<String, String> colMap;
	List<String> fidldsname;
	List<String> fidldstype;
	String layername;
	String layertype;
	DbService dbService;
     public  layereditAdapter(Context context,Map<String, String> columnsmap,String lname,String ltype) {
    	 mContext=context;
    	 colMap=columnsmap;
    	 layername=lname;
    	 layertype=ltype;
    	 dbService=new DbService(mContext);
    	 intidata();
	}
	private void intidata() {
		fidldsname=new ArrayList<>();
		fidldstype=new ArrayList<>();
		for(Entry<String, String> e:colMap.entrySet())
		{
			fidldsname.add(e.getKey());
			fidldstype.add(e.getValue());
			/*if(!e.getKey().equals("shap"))
			{
				fidldsname.add(e.getKey());
				fidldstype.add(e.getValue());
			}*/
		}
	}
	
	public  void mnotifyDataSetChanged(Map<String, String> columnsmap) {
		this.colMap=columnsmap;
    	 intidata();
	}
	@Override
	public int getCount() {
		return colMap.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder=null;
		final int loc=position;
		//View menuView=null;//滑动删除菜单
		if (view == null) {
			view = View.inflate(mContext,R.layout.item_fieldsinfo, null);
			//合成内容与菜单
			//menuView =View.inflate(mContext,R.layout.swipemenu, null); 
			//view = new DragDelItem(view,menuView);
			
			// ���ViewHolder����
			holder = new ViewHolder();
			holder.tv_fieldname=(TextView)view.findViewById(R.id.tv_fname);
			holder.tv_fieldtype=(TextView)view.findViewById(R.id.tv_ftype);
			holder.tv_edit=(TextView)view.findViewById(R.id.tv_fedit);
			holder.tv_fdelete=(TextView) view.findViewById(R.id.tv_fdelete);
			view.setTag(holder);
		} else {
			// ȡ��holder
			holder = (ViewHolder) view.getTag();
		}
		//String dd=fidldsname.get(position);
		holder.tv_fieldname.setText(fidldsname.get(position));
	
		if(fidldstype.get(position).equals("LINESTRING"))
		{
			holder.tv_fieldtype.setText("POLYLINE");
		}else {
			holder.tv_fieldtype.setText(fidldstype.get(position));
		}
		//holder.tv_fieldtype.setText(fidldstype.get(position));
		holder.tv_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final AlertDialog editeDialog=new AlertDialog.Builder(mContext).create();
				editeDialog.setCanceledOnTouchOutside(true);
				View view = View.inflate(mContext, R.layout.fieldedit, null);
				editeDialog.setView(view);
				editeDialog.show();
				final TextView feditname=(TextView) view.findViewById(R.id.tv_feditname);
				Button fedit_confirm=(Button) view.findViewById(R.id.btn_fedit_confirm);
				fedit_confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						editeDialog.dismiss();
						String newname=feditname.getText().toString();
						//List<String> re_colmap=(List<String>) colMap.values();
						List<String> re_colmap=new ArrayList<>();
						for (Entry<String, String> entry:colMap.entrySet()) {
							
							re_colmap.add(entry.getKey());
							
						}
						colMap.put(newname, colMap.get(fidldsname.get(loc)));
						colMap.remove(fidldsname.get(loc));
						//colMap.put(fidldsname.get(loc), newname);
						
						boolean isedit=dbService.updatefeild(layername,layertype,colMap,re_colmap);
						colMap=dbService.getColumnsName(layername);
						if(isedit)
						{
							mnotifyDataSetChanged(colMap);
							notifyDataSetChanged();
							Toast.makeText(mContext, "编辑成功:"+loc, Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(mContext, "编辑失败:"+loc, Toast.LENGTH_SHORT).show();
						}
						
					}
				});
				
			}
			
		});
		//字段删除
		holder.tv_fdelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(mContext)
				.setTitle("提示")
				.setMessage("你确定要删除这个字段？")
				.setPositiveButton("确定" , new  DialogInterface.OnClickListener  () {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						
						
						colMap.remove(fidldsname.get(loc));
						boolean isdelete=dbService.deleteColumn(layername, layertype, colMap);
						if(isdelete)
						{
						
							mnotifyDataSetChanged(colMap);
							notifyDataSetChanged();
							Toast.makeText(mContext, "删除列成功", Toast.LENGTH_SHORT).show();
						}
						else {
							colMap.put(fidldsname.get(loc), fidldstype.get(loc));
							mnotifyDataSetChanged(colMap);
							notifyDataSetChanged();
							Toast.makeText(mContext, "删除列失败", Toast.LENGTH_SHORT).show();
						}
						
					}
				  })
	            .setNegativeButton("取消" , null)  
	            .show(); 
			}
		});
		return view;
	}
	
	public final class ViewHolder {
		//public TextView xuhao;
		
		public TextView tv_fieldname;
		public TextView tv_fieldtype;
		public TextView tv_edit;
		public TextView tv_fdelete;
		/*public ViewHolder(View view) {
			
			tv_fieldname=(TextView)view.findViewById(R.id.tv_fname);
			tv_fieldtype=(TextView)view.findViewById(R.id.tv_ftype);
			tv_edit=(TextView)view.findViewById(R.id.tv_fedit);
			//tv_del=(TextView)view.findViewById(R.id.tv_del);
			view.setTag(this);
		}*/
	
	}
  
}
