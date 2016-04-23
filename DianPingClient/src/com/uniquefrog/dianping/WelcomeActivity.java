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
		//3s����task����
		timer.schedule(new Task(), 3000);
		
	}
	class Task extends TimerTask{

		@Override
		public void run() {
			//�ڴ˽���3s���ҳ����ת����
			if (Toolutils.getIsUsed(getBaseContext())) {
				//�Ѿ������ó�true�����ǵ�һ��
				startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
				
			}
			else{
				//�ǵ�һ�Σ�δ�����ó�true
				Toolutils.setIsUsed(true);
				startActivity(new Intent(WelcomeActivity.this,WelcomeGuideActivity.class));
			}
			finish();
		}
		
	}
}
