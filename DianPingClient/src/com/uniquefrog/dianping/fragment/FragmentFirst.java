package com.uniquefrog.dianping.fragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Callback;
import com.uniquefrog.dianping.AllSortsActivity;
import com.uniquefrog.dianping.CityActivity;
import com.uniquefrog.dianping.R;
import com.uniquefrog.dianping.myutils.MyUtils;
import com.uniquefrog.dianping.utils.Toolutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentFirst extends Fragment{
	@ViewInject(R.id.tvCity)
	TextView tvCity;
	@ViewInject(R.id.gridviewSort)
	GridView gridviewSort;
	Location location;
	LocationManager locationManager;
	List<Address> cityList=new ArrayList<Address>();
	String cityName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view=inflater.inflate(R.layout.fragment_home_layout, container,false);
		ViewUtils.inject(this, view);
		tvCity.setText(Toolutils.getCity(getActivity()));
		gridviewSort.setAdapter(new GridViewSortAdapter());
		return view;
	}

	@OnClick(R.id.tvCity)
	private void onClick(View view){
		Intent intent=new Intent(getActivity(),CityActivity.class);
		startActivityForResult(intent, MyUtils.REQUEST_CODE);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==MyUtils.REQUEST_CODE && resultCode==Activity.RESULT_OK) {
			String cityName=data.getStringExtra("cityName");
			tvCity.setText(cityName);
		}
		
		
		
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Log.i("uniquefrog", "�����˳��ж�λ");
				tvCity.setText(cityName);
				break;
			default:
				break;
			}

		}

	};
	@Override
	public void onStart() {
		super.onStart();
		//�ж��Ƿ��ǿ���GPS
		boolean isOpenGPS=CheckIsOpenGPS();
		if (!isOpenGPS) {
			//δ������GPS��λ����
			//��GPS��λ
			Intent intent =new Intent();
			intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivityForResult(intent, 0);
			//Log.i("uniquefrog", "δ������λ����");
		}
		//Log.i("uniquefrog", "�ѿ����˶�λ����");
		//������GPS��λ����
		//��ʼ��λ
		StartLocation();
	}
	//��ʼ��λ
	private void StartLocation() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				10000, 1000, locationListener);
		Log.i("uniquefrog", "�ѿ�ʼ��λ");
	}
	//�ж��Ƿ��ǿ���GPS
	private boolean CheckIsOpenGPS() {
		locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		boolean isOpenGPS=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return isOpenGPS;


	}
	//��ȡ����λ�ĳ�����Ϣ
	public void GetCity(Location location){
		if (location!=null) {
			Geocoder geocoder=new Geocoder(getActivity());
			//��ȡ��λ���ľ��Ⱥ�γ��
			double latitude=location.getLatitude();
			double longitude=location.getLongitude();
			try {
				//�ɾ��Ⱥ�γ�Ȼ�ȡ��λ���ĳ���
				cityList=geocoder.getFromLocation(latitude, longitude, 2);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		else{
			cityName="�޷���ȡ��λ��Ϣ";
			//tvCity.setText("�޷���ȡ��λ��Ϣ");
		}
		if (cityList!=null & cityList.size()!=0) {
			for(int i=0;i<cityList.size();i++){
				Address address=cityList.get(i);
				//��ȡ����
				cityName=address.getLocality();
			}
		}
		Log.i("uniquefrog", "�����˸���handler��Ϣ");
		handler.sendEmptyMessage(1);
	}

	LocationListener locationListener=new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			Log.i("uniquefrog", "Location �ı�");
			//��ȡ����λ�ĳ�����Ϣ
			GetCity( location);			
		}
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//���������Ϣ
		Toolutils.setCity(getActivity(), cityName);
		//�ص���λ
		StopLocation();	
	}
	//�ص���λ
	private void StopLocation() {
		if(location!=null){
			locationManager.removeUpdates(locationListener);
		}
	}
	private class GridViewSortAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return MyUtils.sortsImgs.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return MyUtils.strsSorts[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder=null;
			if (convertView==null) {
				convertView=LayoutInflater.from(getActivity())
						.inflate(R.layout.sort_gridview_item, null);
				holder=new Holder();
				ViewUtils.inject(holder, convertView);
				convertView.setTag(holder);
			}
			else{
				holder=(Holder) convertView.getTag();
			}
			holder.imgSort.setImageResource(MyUtils.sortsImgs[position]);
			holder.tvSort.setText(MyUtils.strsSorts[position]);
			if (MyUtils.strsSorts[position].equals("ȫ��")) {
				holder.imgSort.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(getActivity(),AllSortsActivity.class);
						startActivity(intent);						
					}
				});
			}
			return convertView;
		}
		class Holder{
			@ViewInject(R.id.imgSort)
			ImageView imgSort;
			@ViewInject(R.id.tvSort)
			TextView tvSort;
		}
		
	}

}
