package com.uniquefrog.dianping;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeGuideActivity extends Activity {

	@ViewInject(R.id.guideViewpager)
	ViewPager guideViewpager;
	@ViewInject(R.id.btnEnterMain)
	Button btnEnterMain;
	ArrayList<View> viewpagerData=new ArrayList<View>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_guide);
		//Activity初始化
		ViewUtils.inject(WelcomeGuideActivity.this);
		//设置ViewPager里面的Item
		ImageView imageView1=new ImageView(WelcomeGuideActivity.this);
		imageView1.setImageResource(R.drawable.guide_01);
		viewpagerData.add(imageView1);
		ImageView imageView2=new ImageView(WelcomeGuideActivity.this);
		imageView2.setImageResource(R.drawable.guide_02);
		viewpagerData.add(imageView2);
		ImageView imageView3=new ImageView(WelcomeGuideActivity.this);
		imageView3.setImageResource(R.drawable.guide_03);
		viewpagerData.add(imageView3);
		guideViewpager.setAdapter(new ViewpagerAdapter());
		
		guideViewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				//当翻到第三页时，button显现
				if (arg0==2) {
					btnEnterMain.setVisibility(View.VISIBLE);
				}else{
					btnEnterMain.setVisibility(View.GONE);
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		
	}
	@OnClick(R.id.btnEnterMain)
	public void click(View view){
		startActivity(new Intent(WelcomeGuideActivity.this,MainActivity.class));
	}
	class ViewpagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewpagerData.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewpagerData.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewpagerData.get(position));
			return viewpagerData.get(position);
		}
		
		
		
		
		
	}
		
}
