package com.titan.demo;


import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 
 * @author https://github.com/younfor
 * 
 */
public class DragDelListView extends ListView {

	//�Ƿ�����϶�
	private boolean moveable=false;
	//�Ƿ�ȫ���ر�
	private boolean closed=true;
	private float mDownX,mDownY;
	private int mTouchPosition,oldPosition=-1;
	private DragDelItem mTouchView,oldView;
	private Context context;
	public DragDelListView(Context context) {
		super(context);
		init(context);
	}
	public DragDelListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	public DragDelListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private void init(Context context) {
		this.context=context;
	}


	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//��ȡ�����position
			mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
			//��ȡ�����view (�ǿɼ���Χ�����)
			mTouchView=(DragDelItem)getChildAt(mTouchPosition - getFirstVisiblePosition());
			//��ȡ����ĵط�
			mDownX = ev.getX();
			mDownY=ev.getY();
			//�������ͬһ������ȫ����ʼ
			if(oldPosition==mTouchPosition ||closed)
			{
				//�����ƶ�
				moveable=true;
				mTouchView.mDownX =(int)mDownX;
			}else
			{
				moveable=false;
				if(oldView!=null)
				{	//�ɵĹ�λ
					oldView.smoothCloseMenu();
				}
			}
			oldPosition=mTouchPosition;
			oldView=mTouchView;
			break;
		case MotionEvent.ACTION_MOVE:
			//�ж��Ƿ�໬
			if (Math.abs(mDownX-ev.getX()) < Math.abs(mDownY-ev.getY()) * dp2px(2)) {  
                break;  
            }  
			if (moveable) 
			{
				int dis = (int) (mTouchView.mDownX -ev.getX());
				//��ʱ������
				if(mTouchView.state==mTouchView.STATE_OPEN)
					dis+=mTouchView.mMenuView.getWidth();
			    mTouchView.swipe(dis);
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			
			break;
		case MotionEvent.ACTION_UP:

			if (moveable) 
			{
				if ((mTouchView.mDownX -ev.getX()) > (mTouchView.mMenuView.getWidth()/2)) {
					// open
					mTouchView.smoothOpenMenu();
					closed=false;
				} else {
					// close
					mTouchView.smoothCloseMenu();
					closed=true;
				}
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	//��dpת��Ϊpx
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}


}
