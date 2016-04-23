package com.uniquefrog.dianping.view;


import com.uniquefrog.dianping.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class CitySideIndexView extends View {

	OnSidePositionClickListener SidePositionClickListener ;
	Paint paint=new Paint();
	String[] strs=new String[]{"热门","A","B","C","D","E",
			"F","G","H","I","J",
			"K","L","M","N",
			"O","P","Q","R","S","T",
			"U","V","W","X","Y","Z"};
	public CitySideIndexView(Context context) {
		super(context);
	}
	//在View的Xml转换成java中的控件的时候必须实现
	public CitySideIndexView(Context context,AttributeSet arr){
		super(context, arr);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//获取该控件的宽和高
		int hight=getHeight();
		int width=getWidth();
		paint.setColor(Color.GRAY);
		int eachHight=hight/strs.length;
		paint.setTextSize(20);
		//字体采用粗体
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		for(int i=0;i<strs.length;i++){
			float x=(width-paint.measureText(strs[i]))/2;
			float y=(1+i)*eachHight;
			canvas.drawText(strs[i], x, y, paint);
		}
	}
	public interface OnSidePositionClickListener{
		public void onsidePositionClickListener(String strIndex);
	}
	public void SetOnSidePositionClickListener(OnSidePositionClickListener sidePosClkListener){
		this.SidePositionClickListener=sidePosClkListener;
		
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		String strIndex;
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP://手指抬起
			
			//获得当前索引对应的字符串
			strIndex=getPositionIndex(event.getY()) ;
			//设置该view的背景
			setBackgroundResource(android.R.color.transparent);
//			//自定义控件的执行
//			invalidate();
			break;
		default://其他动作，包括滑动，按下，滑过...
			Log.i("uniquefrog", "Y值："+event.getY());
			//获得当前索引对应的字符串
			strIndex=getPositionIndex(event.getY()) ;
			//设置该view的背景
			setBackgroundResource(R.drawable.sideview_shape_bg);
			//自定义控件执行
			break;
		}
		if (!strIndex.equals("")) {
			SidePositionClickListener.onsidePositionClickListener(strIndex);
			invalidate();
		}
		Log.i("uniquefrog", "索引字符串："+strIndex);
		return true;
	}
	private String getPositionIndex(float y) {
		//获得当前动作抬起的大致位置，并计算出在字符串中的索引
		int index=(int)((y/getHeight())*strs.length);
		//获得当前索引对应的字符串
		return strs[index];
		 
	}
	


}
