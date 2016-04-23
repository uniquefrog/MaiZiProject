package com.uniquefrog.dianping;

import java.util.Timer;
import java.util.TimerTask;

import com.uniquefrog.dianping.utils.Toolutils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		Timer timer=new Timer();
		//3s后处理task任务
		timer.schedule(new Task(), 3000);
		
	}
	class Task extends TimerTask{

		@Override
		public void run() {
			//在此进行3s后的页面跳转动作
			if (Toolutils.getIsUsed(getBaseContext())) {
				//已经被设置成true，不是第一次
				startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
				
			}
			else{
				//是第一次，未被设置成true
				Toolutils.setIsUsed(true);
				startActivity(new Intent(WelcomeActivity.this,WelcomeGuideActivity.class));
			}
			finish();
		}
		
	}
}
