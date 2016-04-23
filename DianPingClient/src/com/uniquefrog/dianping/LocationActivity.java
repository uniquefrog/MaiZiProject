package com.uniquefrog.dianping;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.uniquefrog.dianping.consts.Consts;
import com.uniquefrog.dianping.entity.Goods;
import com.uniquefrog.dianping.entity.ResponseObject;
import com.uniquefrog.dianping.entity.Shop;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;

public class LocationActivity extends Activity 
implements LocationSource,AMapLocationListener,OnMarkerClickListener,OnMapLoadedListener,
OnInfoWindowClickListener,InfoWindowAdapter{

	@ViewInject(R.id.discoverMap)
	MapView discoverMap;
	@ViewInject(R.id.imgDiscoveryRefresh)
	ImageView imgDiscoveryRefresh;
	@ViewInject(R.id.imgDiscoveryBack)
	ImageView imgDiscoveryBack;
	AMap amap;
	private double mlongtitude;
	private double mlatitude;
	private OnLocationChangedListener mListener;
	final int LOCATION_Start=0;
	final int LOCATION_FINISH=1;
	AMapLocation mlocation;
	AMapLocationClient locationClient;
	AMapLocationClientOption locationClientOption;
	List<Goods> listGoods;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
		setContentView(R.layout.activity_location);
		ViewUtils.inject(this);
		discoverMap.onCreate(savedInstanceState);// 此方法必须重写
		//		locationClient=new AMapLocationClient(LocationActivity.this);
		//		locationClient.setLocationListener(this);
		//		locationClientOption=new AMapLocationClientOption();
		if (amap==null) {
			amap=discoverMap.getMap();
			//set the source of amap
			amap.setLocationSource(this);
			// 设置默认定位按钮是否显示
			amap.getUiSettings().setMyLocationButtonEnabled(true);
			//show the loction layer and location is triggled
			amap.setMyLocationEnabled(true);
			//show the type of location
			amap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			amap.setOnMarkerClickListener(this);
			amap.setOnInfoWindowClickListener(this);
			amap.setOnMapLoadedListener(this);
			amap.setInfoWindowAdapter(this);
		}

		//		//use Hight_Accuracy mode to location,both of Internet and GPS is used
		//		locationClientOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		//		// 设置是否需要显示地址信息
		//		locationClientOption.setNeedAddress(true);
		//		locationClient.setLocationListener(this);
		//		locationClient.setLocationOption(locationClientOption);
		//		locationClient.startLocation();

	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOCATION_FINISH:
				//handler the result of location
				mlocation = (AMapLocation) msg.obj;
				locationClient.stopLocation();
				break;
			default:
				break;
			}
		};
	};
	@OnClick({R.id.imgDiscoveryBack,R.id.imgDiscoveryRefresh})
	private void OnClick(View view){
		switch (view.getId()) {
		case R.id.imgDiscoveryBack:
			finish();
			break;
		case R.id.imgDiscoveryRefresh:
			//load data of the realted information nearby
			loadNearbyInformation(String.valueOf(mlongtitude),
					String.valueOf(mlatitude),"1000");
			break;
		default:
			break;
		}
	}

	private void loadNearbyInformation( String longtitude,
			String latitude, String radius) {
		RequestParams params=new RequestParams();
		params.addQueryStringParameter("lat", latitude);
		params.addQueryStringParameter("lon", longtitude);
		params.addQueryStringParameter("radius", radius);
		new HttpUtils().send(HttpMethod.GET, Consts.NEARBY_DATA_URI,
				params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				Log.i("uniquefrog", "读取数据库失败："+arg1);

				Toast.makeText(LocationActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();						
			}

			@Override
			public void onSuccess(ResponseInfo<String> json) {
				//parase the Json result of server
				ResponseObject<List<Goods>> object=new Gson()
						.fromJson(json.result, new TypeToken<ResponseObject<List<Goods>>>(){}.getType());
//				Log.i("uniquefrog", "获取到的经度："+mlongtitude+" 维度："+mlatitude);
//				
//				Log.i("uniquefrog",json.result);
				listGoods=object.getDatas();
				if (listGoods!=null) {
//					Log.i("uniquefrog", "size of ListGoods:"+listGoods.size());
//					Log.i("uniquefrog", "精度："+mlongtitude+" 维度："+mlatitude);
					//set the zoom of the map
					//params details :http://lbs.amap.com/api/android-sdk/guide/camera/
					amap.animateCamera(CameraUpdateFactory
							.newCameraPosition(new CameraPosition(new LatLng
									(mlatitude,mlongtitude ), 
									16f, 0f, 30f)));

					//Log.i("uniquefrog", "listGoods is not null");
					//mark the data to the map
					markData(listGoods);
				}
				else{
					Toast.makeText(LocationActivity.this, "对不起，附近没有卖家信息，奴家会加快为您更新哒~", Toast.LENGTH_LONG).show();
				}

			}
		});
	}
	private void markData(List<Goods> listGoods) {
		MarkerOptions markerOptions=null;
		for(Goods goods:listGoods){
			markerOptions=new MarkerOptions();
			Shop shop=goods.getShop();
			//set the mark title and snippet on the shop of latlng
			markerOptions.position(new LatLng(Double.parseDouble(shop.getLat()),
					Double.parseDouble(shop.getLon())))
			.title(shop.getName()).snippet("¥"+goods.getPrice());
			//set the icon of the shop according to the categoryID
			int goodsCategoryId=Integer.parseInt(goods.getCategoryId());
			switch (goodsCategoryId) {
			case 3:
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_landmark_chi));
				break;

			case 4:
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_landmark_wan));
				break;
			case 5:
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_landmark_movie));
				break;
			case 6:
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_landmark_life));
				break;
			case 8:
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_landmark_hotel));
				break;
			default:
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_landmark_default));
				break;
			}
			//show the mark in the map 
			amap.addMarker(markerOptions).setObject(goods);


		}


	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//		if (discoverMap!=null) {
		discoverMap.onResume();
		//			locationClient=null;
		//			locationClientOption=null;
		//		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		//		if (discoverMap!=null) {
		discoverMap.onPause();
		//			locationClient=null;
		//			locationClientOption=null;
		//		}

	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		discoverMap.onSaveInstanceState(outState);
		//		locationClient=null;
		//		locationClientOption=null;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		discoverMap.onDestroy();
		if (locationClient!=null) {
			locationClient.onDestroy();;
		}
	}
	@Override
	public void onLocationChanged(AMapLocation location) {

		if ( mListener != null &&location != null) {
			if (location != null
					&& location.getErrorCode() == 0) {
				//self-define Latitude and Longitude because of little data
				location.setLatitude(39.983456);
				location.setLongitude(116.3154950);
				mlongtitude=location.getLongitude();
				mlatitude=location.getLatitude();
				//				Message message=new Message();
				//				message.obj=location;
				//				message.what=LOCATION_FINISH;
				//				handler.sendMessage(message);
				locationClient.stopLocation();
				
				mListener.onLocationChanged(location);// 显示系统小蓝点
				//Log.i("uniquefrog", "定位成功");
				//Log.i("uniquefrog", "精度："+mlongtitude+" 维度："+mlatitude);
				loadNearbyInformation(String.valueOf(mlongtitude),String.valueOf(mlatitude),"1000");

			} else {
				String errText = "定位失败," + location.getErrorCode()+ ": " + location.getErrorInfo();
				Toast.makeText(LocationActivity.this, errText, Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// click the shop window
		//get the shop name of marker
		String shopName=arg0.getTitle();
		//get the goods information according to the shopName
		for(Goods goods:listGoods){
			if (goods.getShop().getName().equals(shopName)) {
				if (goods!=null) {
					Intent intent=new Intent(LocationActivity.this,GoodsDetailsActivity.class);
					intent.putExtra("goods", goods);
					startActivity(intent);
				}
			}
		}

	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (locationClient == null) {
			locationClient = new AMapLocationClient(this);
			locationClientOption = new AMapLocationClientOption();
			//设置定位监听
			locationClient.setLocationListener(this);
			// 设置是否需要显示地址信息
			locationClientOption.setNeedAddress(true);
			//设置为高精度定位模式
			locationClientOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			locationClientOption.setOnceLocation(true);
			//locate per one minute 
			locationClientOption.setInterval(60000);
			//设置定位参数
			locationClient.setLocationOption(locationClientOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			locationClient.startLocation();
		}		
	}

	@Override
	public void deactivate() {
		mlocation=null;
		if (locationClient!=null) {
			locationClient.stopLocation();
			locationClient.onDestroy();
		}
		locationClient=null;
	}
}
