package com.uniquefrog.dianping;

import java.security.PublicKey;
import java.util.Timer;
import java.util.TimerTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.ViewInjectInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.uniquefrog.dianping.fragment.FragmentDiscovery;
import com.uniquefrog.dianping.fragment.FragmentFirst;
import com.uniquefrog.dianping.fragment.FragmentMine;
import com.uniquefrog.dianping.fragment.FragmentTuan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import okhttp3.Challenge;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener{

	@ViewInject(R.id.rdGroupBottom)
	RadioGroup rdGroupBottom;
	@ViewInject(R.id.rdBtnFirst)
	RadioButton rdBtnFirst;
	@ViewInject(R.id.rdBtnTuan)
	RadioButton rdBtnTuan;
	@ViewInject(R.id.rdBtnDiscovery)
	RadioButton rdBtnDiscovery;
	@ViewInject(R.id.rdBtnMine)
	RadioButton rdBtnMine;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(MainActivity.this);
		rdBtnFirst.setChecked(true);
		rdGroupBottom.setOnCheckedChangeListener(this);
		ChangeFragment(new FragmentFirst(),false);


	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rdBtnFirst:
			ChangeFragment(new FragmentFirst(),true);
			break;
		case R.id.rdBtnTuan:
			ChangeFragment(new FragmentTuan(),true);
			break;
		case R.id.rdBtnDiscovery:
			ChangeFragment(new FragmentDiscovery(),true);
			break;
		case R.id.rdBtnMine:
			ChangeFragment(new FragmentMine(),true);
			break;
		}
	}

	public void ChangeFragment(Fragment fragment,boolean init){
		FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.mainContentFrame, fragment);
		
		if (!init) {
			transaction.addToBackStack(null);
			
		}
		transaction.commit();




	}




}
