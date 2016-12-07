package com.titan.demo;

import java.util.List;

import com.esri.core.geometry.Point;

public class GISUtils {
	/**   
	* @Title: GISUtils.java 
	* @Package abc.abc.abc 
	* @Description: 
	* @author whs HDstudio  
	* @date 2015-8-25 下午4:03:50 
	* @version V1.0   
	*/
    /// <summary>  
    /// 判断坐标点是否落在指定的多边形区域内  
    /// </summary>  
    /// <param name="point">指定的坐标点</param>  
    /// <param name="list">多变形区域的节点集合</param>  
    /// <returns>True 落在范围内 False 不在范围内</returns>  
    public static boolean IsWithIn(Point pt, List<Point> list)  
    {  
        double x = pt.getX();  
        double y = pt.getY();  
      
        int isum, icount, index;  
        double dLon1 = 0, dLon2 = 0, dLat1 = 0, dLat2 = 0, dLon;  
      
        if (list.size() < 3)  
        {  
            return false;  
        }  
      
        isum = 0;  
        icount = list.size();  
      
      
            for (index = 0; index < icount - 1; index++)  
            {  
                if (index == icount - 1)  
                {  
                    dLon1 = list.get(index).getX();  
                    dLat1 = list.get(index).getY();  
                    dLon2 = list.get(0).getX();  
                    dLat2 = list.get(0).getY();  
                }  
                else  
                {  
                    dLon1 = list.get(index).getX();  
                    dLat1 = list.get(index).getY();  
                    dLon2 = list.get(index+1).getX();  
                    dLat2 = list.get(index+1).getY();  
                }  
      
                if (((y >= dLat1) && (y < dLat2)) || ((y >= dLat2) && (y < dLat1)))  
                {  
                    if (Math.abs(dLat1 - dLat2) > 0)  
                    {  
                        dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - y)) / (dLat1 - dLat2);  
                        if (dLon < x)  
                            isum++;  
                    }  
                }  
            }  
          
       
      
        if ((isum % 2) != 0)  
        {  
            return true;  
        }  
        else  
        {  
            return false;  
        }  
    }
}
