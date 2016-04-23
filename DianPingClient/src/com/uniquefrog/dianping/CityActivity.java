package com.uniquefrog.dianping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemSelected;
import com.uniquefrog.dianping.consts.Consts;
import com.uniquefrog.dianping.entity.City;
import com.uniquefrog.dianping.entity.ResponseObject;
import com.uniquefrog.dianping.view.CitySideIndexView;
import com.uniquefrog.dianping.view.CitySideIndexView.OnSidePositionClickListener;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CityActivity extends Activity {

	@ViewInject(R.id.btnCityBarBack)
	Button btnCityBarBack;
	@ViewInject(R.id.btnCityBarRefresh)
	Button btnCityBarRefresh;
	@ViewInject(R.id.listViewCity)
	ListView cityListView;
	@ViewInject(R.id.citySideIndexView)
	CitySideIndexView citySideIndexView;
	//��ų�����Ϣ���б�
	List<City> cityListData=new ArrayList<City>();
	//�������ĸ��ͬ�ĳ�����Ϣ���б�
	List<String> sortCitiesList=new ArrayList<String>();
	//�������ĸ���ַ���
	StringBuilder sortStrBuidler=new StringBuilder();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		ViewUtils.inject(this);
		View view=LayoutInflater.from(this).inflate(R.layout.city_list_search, null);
		cityListView.addHeaderView(view);
		new GetCityDataTask().execute();
		cityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String cityName=cityListData.get(position).getName();
				Intent intent=getIntent();
				intent.putExtra("cityName", cityName);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		citySideIndexView.SetOnSidePositionClickListener(new OnSidePositionClickListener() {

			@Override
			public void onsidePositionClickListener(String strIndex) {
				int index=-1;
				for(int i=0;i<cityListData.size();i++){
					City city=cityListData.get(i);
					if (city.getSortKey().equals(strIndex)) {
						index=i;
						break;
					}
				}
				if (index!=-1) {
					cityListView.setSelection(index);
				}
				else{
					Toast.makeText(CityActivity.this, "������Ϣ", 3000).show();
				}
			}
		});


	}
	@OnClick(R.id.btnCityBarBack)
	private void btnCityBarBackOnClick(View view){
		finish();

	}
	@OnClick(R.id.btnCityBarRefresh)
	private void btnCityBarRefreshOnClick(View view){
		new GetCityDataTask().execute();

	}

	public class GetCityDataTask extends AsyncTask<Void, Void, List<City>>{

		@Override
		protected List<City> doInBackground(Void... params) {
			HttpClient client=new DefaultHttpClient();
			HttpGet httpGet=new HttpGet(Consts.CITY_DATA_URI);
			try {
				HttpResponse response=client.execute(httpGet);
				if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
					Log.i("uniquefrog", "��Ӧ�룺"+200);
					//��÷��صĳ�������--�ַ�����ʽ
					String jsonResult=EntityUtils.toString(response.getEntity(),"UTF-8");
					//�������������ַ���
					return ParseJsonResult(jsonResult);

				}
				else{
					Log.i("uniquefrog", "��Ӧ�룺"+response.getStatusLine().getStatusCode());
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			return null;
		}

		//����Json��ʽ���ַ���
		private List<City> ParseJsonResult(String jsonResult) {
			//����Gson����Json�ַ�������
			Gson gson=new Gson();
			ResponseObject<List<City>> responseObject=gson.fromJson(jsonResult, 
					new TypeToken<ResponseObject<List<City>>>(){}.getType());
			return responseObject.getDatas();
		}

		@Override
		protected void onPostExecute(List<City> result) {
			super.onPostExecute(result);
			
			if (result!=null) {
				cityListData=result;
				//����Listview
				CityListAdapter adapter=new CityListAdapter();
				cityListView.setAdapter(adapter);
			}
			else{
				Toast.makeText(CityActivity.this, "δ�ܻ�ȡ���ݣ���ȷ���������!",
						Toast.LENGTH_LONG).show();
			}
			
		}

	}
	public class CityListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return cityListData.size();
		}

		@Override
		public Object getItem(int position) {
			return cityListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView==null) {
				holder=new Holder();
				convertView=LayoutInflater.from(CityActivity.this)
						.inflate(R.layout.city_list_item_layout, null);
				ViewUtils.inject(holder,convertView);
				convertView.setTag(holder);
			}
			else{
				holder=(Holder) convertView.getTag();

			}
			//��������TextView
			City city=cityListData.get(position);
			String sortKey=city.getSortKey();
			String cityName=city.getName();
			//����ó��е�����ĸû�г��ֹ�������ӵ�sortStrBuidler��������ӳ�����
			if (sortStrBuidler.indexOf(sortKey)==-1) {
				sortStrBuidler.append(sortKey);
				sortCitiesList.add(cityName);

			}
			//������Ѿ�����ӵ��б��еĳ��У�˵���ǵ�һ������ĸ��ӽ�ȥ����tvCitySortKeyӦ�ÿɼ�
			if (sortCitiesList.contains(cityName)) {
				holder.tvCitySortKey.setText(sortKey);
				holder.tvCitySortKey.setVisibility(View.VISIBLE);

			}
			//�б��в����ڸó��е����֣�˵������ĸ���ǵ�һ�γ��֣���tvCitySortKey���ɼ�
			else{

				holder.tvCitySortKey.setVisibility(View.GONE);
			}
			holder.tvListCity.setText(cityName);
			return convertView;
		}

		private class Holder{
			@ViewInject(R.id.tvCitySortKey)
			TextView tvCitySortKey;
			@ViewInject(R.id.tvListCity)
			TextView tvListCity;
		}


	}


}
