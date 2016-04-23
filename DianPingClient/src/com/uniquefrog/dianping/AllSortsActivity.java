package com.uniquefrog.dianping;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Downloader.Response;
import com.uniquefrog.dianping.consts.Consts;
import com.uniquefrog.dianping.entity.Category;
import com.uniquefrog.dianping.entity.ResponseObject;
import com.uniquefrog.dianping.myutils.MyUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AllSortsActivity extends Activity {

	@ViewInject(R.id.btnAllSortsBack)
	Button btnAllSortsBack;
	@ViewInject(R.id.listviewAllSorts)
	ListView listviewAllSorts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_sorts);
		ViewUtils.inject(this);
		new AllSortsAsyncTask().execute();
		btnAllSortsBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	class AllSortsAsyncTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient client=new DefaultHttpClient();
			HttpPost post=new HttpPost(Consts.Sorts_DATA_URI);
			HttpResponse response=null;
			try {
				response=client.execute(post);
				if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
					String httpResult=EntityUtils.toString(response.getEntity(),
							"UTF-8");
					//解析返回的Json格式的数据,
					PareseResult(httpResult);
				}
				//Log.i("uniquefrog", "分类响应码："+response.getStatusLine().getStatusCode());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		private void PareseResult(String httpResult) {
			Gson gson=new Gson();
			ResponseObject<List<Category>> respObject=
					gson.fromJson(httpResult, 
					new TypeToken<ResponseObject<List<Category>>>(){}.getType());
			List<Category> datas=respObject.getDatas();
			for (int i = 0; i < datas.size(); i++) {
				Category category=datas.get(i);
				int positon=Integer.parseInt(category.getCategoryId());
				MyUtils.allSortsNum[positon-1]=category.getCategeryNumber();
			}
			
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (MyUtils.allSortsNum!=null) {
				listviewAllSorts.setAdapter(new AllSortsListViewAdapter());
			}
			else{
				Toast.makeText(AllSortsActivity.this, "获取分类信息失败，请检查网络", 2000).show();
			}
		}
		
	}
	
	class AllSortsListViewAdapter extends BaseAdapter{
	

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MyUtils.strsAllSorts.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return MyUtils.strsAllSorts[position];
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
				convertView=LayoutInflater.from(AllSortsActivity.this)
						.inflate(R.layout.allsorts_list_item_layout, null);
				holder=new Holder();
				ViewUtils.inject(holder,convertView);
				convertView.setTag(holder);
			}
			else{
				holder=(Holder) convertView.getTag();
			}
			holder.imgAllSorts.setImageResource(MyUtils.imgsAllSorts[position]);
			holder.tvAllSortsName.setText(MyUtils.strsAllSorts[position]);
			holder.tvAllSortsNum.setText(String.valueOf(MyUtils.allSortsNum[position]));
			return convertView;
		}
		class Holder{
			@ViewInject(R.id.imgAllSorts)
			ImageView imgAllSorts;
			@ViewInject(R.id.tvAllSortsName)
			TextView tvAllSortsName;
			@ViewInject(R.id.tvAllSortsNum)
			TextView tvAllSortsNum;
		}
		
	}
	
	
}
