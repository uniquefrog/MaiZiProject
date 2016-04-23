package com.uniquefrog.dianping.fragment;


import com.amap.api.maps.model.Text;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.uniquefrog.dianping.LoginActivity;
import com.uniquefrog.dianping.R;
import com.uniquefrog.dianping.myutils.MyUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentMine extends Fragment {

	@ViewInject(R.id.tvClickLogin)
	TextView tvClickLogin;
	@ViewInject(R.id.imgMyPhoto)
	ImageView imgMyPhoto;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_mine_layout, container,false);
		ViewUtils.inject(this,view);
		initEvent();
		
		
		
		return view;
		
	}
	private void initEvent() {
		tvClickLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),LoginActivity.class);
				startActivityForResult(intent, MyUtils.REQUEST_CODE);
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==MyUtils.REQUEST_CODE && resultCode==MyUtils.RESULT_CODE) {
			tvClickLogin.setText(data.getStringExtra("LoninName"));
			imgMyPhoto.setImageResource(R.drawable.profile_default);
			Log.i("uniquefrog", "获取到了登录信息");
			
		}
		else{
			Log.i("uniquefrog", "未获取到登录信息");
		}
		
		
		
		
	}

}
