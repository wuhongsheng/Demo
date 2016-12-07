package com.titan.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageFragmentAdapter extends FragmentPagerAdapter{
	/**   
	* @Title: PageFragmentAdapter.java 
	* @Package com.otitan.adapter 
	* @Description: 
	* @author whs HDstudio  
	* @date 2015-8-20 上午11:14:55 
	* @version V1.0   
	*/
    private List<Fragment> fragmentList;
    private FragmentManager fm;
    public PageFragmentAdapter(FragmentManager fm,List<Fragment> fragmentList){
        super(fm);
        this.fragmentList=fragmentList;
        this.fm=fm;
    }
    @Override
    public Fragment getItem(int idx) {
        return fragmentList.get(idx%fragmentList.size());
    }
    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override  
    public int getItemPosition(Object object) {  
       return POSITION_NONE;  //没有找到child要求重新加载
    }  
}
