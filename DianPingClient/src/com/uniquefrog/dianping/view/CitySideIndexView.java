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
	String[] strs=new String[]{"����","A","B","C","D","E",
			"F","G","H","I","J",
			"K","L","M","N",
			"O","P","Q","R","S","T",
			"U","V","W","X","Y","Z"};
	public CitySideIndexView(Context context) {
		super(context);
	}
	//��View��Xmlת����java�еĿؼ���ʱ�����ʵ��
	public CitySideIndexView(Context context,AttributeSet arr){
		super(context, arr);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//��ȡ�ÿؼ��Ŀ�͸�
		int hight=getHeight();
		int width=getWidth();
		paint.setColor(Color.GRAY);
		int eachHight=hight/strs.length;
		paint.setTextSize(20);
		//������ô���
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
		case MotionEvent.ACTION_UP://��ָ̧��
			
			//��õ�ǰ������Ӧ���ַ���
			strIndex=getPositionIndex(event.getY()) ;
			//���ø�view�ı���
			setBackgroundResource(android.R.color.transparent);
//			//�Զ���ؼ���ִ��
//			invalidate();
			break;
		default://�����������������������£�����...
			Log.i("uniquefrog", "Yֵ��"+event.getY());
			//��õ�ǰ������Ӧ���ַ���
			strIndex=getPositionIndex(event.getY()) ;
			//���ø�view�ı���
			setBackgroundResource(R.drawable.sideview_shape_bg);
			//�Զ���ؼ�ִ��
			break;
		}
		if (!strIndex.equals("")) {
			SidePositionClickListener.onsidePositionClickListener(strIndex);
			invalidate();
		}
		Log.i("uniquefrog", "�����ַ�����"+strIndex);
		return true;
	}
	private String getPositionIndex(float y) {
		//��õ�ǰ����̧��Ĵ���λ�ã�����������ַ����е�����
		int index=(int)((y/getHeight())*strs.length);
		//��õ�ǰ������Ӧ���ַ���
		return strs[index];
		 
	}
	


}
