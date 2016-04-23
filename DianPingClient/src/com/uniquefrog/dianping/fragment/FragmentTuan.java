package com.uniquefrog.dianping.fragment;


import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.uniquefrog.dianping.AllSortsActivity;
import com.uniquefrog.dianping.GoodsDetailsActivity;
import com.uniquefrog.dianping.R;
import com.uniquefrog.dianping.consts.Consts;
import com.uniquefrog.dianping.entity.Category;
import com.uniquefrog.dianping.entity.Goods;
import com.uniquefrog.dianping.entity.ResponseObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentTuan extends Fragment {
	@ViewInject(R.id.goodsListView)
	PullToRefreshListView goodsListView;
	//定义网址中的page和size
	private int page=1,size=10;
	List<Goods> listGoods;
	boolean reflush=true;
	GoodsListViewAdapter goodsListViewAdapter=new GoodsListViewAdapter();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_tuan_layout, container,false);
		ViewUtils.inject(this, view);
		goodsListView.setMode(Mode.BOTH);
		//利用Xutils中的方法实现异步获取服务器中的信息
		goodsListView.setScrollingWhileRefreshingEnabled(true);
		ILoadingLayout loadingLayout=new ILoadingLayout() {
			
			@Override
			public void setTextTypeface(Typeface tf) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			public void setReleaseLabel(CharSequence releaseLabel) {
				if (reflush) {
					releaseLabel="放开刷新";	
				}
				else{
					releaseLabel="放开加载数据";	
				}
							
			}
			
			@Override
			public void setRefreshingLabel(CharSequence refreshingLabel) {
				if (reflush) {
					refreshingLabel="正在拼命刷新中...";	
				}
				else{
					refreshingLabel="正在拼命加载中...";	
				}				
			}
			
			@Override
			public void setPullLabel(CharSequence pullLabel) {
				if (reflush) {
					pullLabel="下拉刷新";	
				}
				else{
					pullLabel="上拉加载";	
				}					
			}
			
			@Override
			public void setLoadingDrawable(Drawable drawable) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setLastUpdatedLabel(CharSequence label) {
				// TODO Auto-generated method stub
				
			}
		};
		goodsListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//y小于0，说明下滑
				reflush=goodsListView.getScrollY()<0;
				GetGoods();
			}
		});
		new Handler().postDelayed(new Runnable() {
			
			public void run() {
				goodsListView.setRefreshing();
			}
		}, 300);
		goodsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent(getActivity(),GoodsDetailsActivity.class);
				intent.putExtra("goods",listGoods.get(position-1));
				startActivity(intent);					
			}
		});
		
		
		return view;
	}
	
	private void GetGoods() {
		if (reflush) {
			//如果是下滑，则刷新页面
			page=1;
		}
		else{
			//如果不是下滑，则为加载更多
			page++;
		}
		
		//use Xutils send params
		HttpUtils httpUtils=new HttpUtils();
		RequestParams params=new RequestParams();
		params.addQueryStringParameter("page", page+"");
		params.addQueryStringParameter("size", size+"");
		//xUtils send the http request
		httpUtils.send(HttpMethod.GET, Consts.Goods_DATA_URI, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						goodsListView.onRefreshComplete();
						//request failed
						Toast.makeText(getActivity(), arg1, Toast.LENGTH_LONG).show();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						goodsListView.onRefreshComplete();
						//request successed
						Gson gson=new Gson();
						ResponseObject<List<Goods>> responseObject= gson.fromJson(arg0.result,
								new TypeToken<ResponseObject<List<Goods>>>(){}.getType());
						
						//get the information of the object from server
						page=responseObject.getPage();
						size=responseObject.getSize();
						if (reflush) {
							//如果是下滑，则刷新页面
							listGoods=responseObject.getDatas();
							goodsListView.setAdapter(goodsListViewAdapter);
						}
						else{
							//如果不是下滑，则为加载更多
							listGoods.addAll(responseObject.getDatas());
							goodsListViewAdapter.notifyDataSetChanged();
						}
						if (page==responseObject.getCount()) {
							//当前加载的页面等于获得到的总页面数
							//则不再提供加载功能
							goodsListView.setMode(Mode.PULL_FROM_START);
						}
					}
					
				});
	}
	
	
	class GoodsListViewAdapter extends BaseAdapter{

		Holder holder=null;
		@Override
		public int getCount() {
			return listGoods.size();
		}

		@Override
		public Object getItem(int position) {
			return listGoods.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView==null) {
				holder=new Holder();
				convertView=LayoutInflater.from(getActivity()).inflate(R.layout.goods_list_item_layout,
						null);
				ViewUtils.inject(holder, convertView);;
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			Goods goods=listGoods.get(position);
			String goodsImgUrl=goods.getImgUrl();
			//在文字上加上中划线
			StringBuffer sbOriPrice=new StringBuffer("￥"+goods.getValue());
			SpannableString spannableString=new SpannableString(sbOriPrice);
			spannableString.setSpan(new StrikethroughSpan(), 0, sbOriPrice.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			holder.tvOrigPrice.setText(spannableString);
			holder.tvShortTitle.setText(goods.getSortTitle());
			holder.tvDetailTitle.setText(goods.getTitle());
			holder.tvCurPrice.setText("￥"+goods.getPrice());
			holder.tvGoodsNum.setText(goods.getBought()+"份");
			//利用异步的操作，大量的加载图片会导致内存溢出
//			HttpUtils httpUtils=new HttpUtils();
//			httpUtils.send(HttpMethod.GET, goodsImgUrl, new RequestCallBack<byte[]>() {
//
//				@Override
//				public void onSuccess(ResponseInfo<byte[]> arg0) {
//					byte[] buffer= arg0.result;
//					Bitmap img=BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
//					holder.imgGoodsItem.setImageBitmap(img);
//				}
//				@Override
//				public void onFailure(HttpException arg0, String arg1) {
//					Toast.makeText(getActivity(), arg1, Toast.LENGTH_LONG).show();
//				}
//			});
			//利用picaso进行小图片加载，
			//可以避免内存溢出以及图片错位，可以随时取消，可以自动保存到内存或者硬盘中
			//如果加载发生错误会重复三次请求，三次都失败才会显示erro Place holder
			Picasso.with(getActivity()).load(goodsImgUrl).placeholder(R.drawable.ic_empty_dish).into(holder.imgGoodsItem);
			
			
			return convertView;
		}
		class Holder{
			@ViewInject(R.id.imgGoodsItem)
			ImageView imgGoodsItem;
			@ViewInject(R.id.tvShortTitle)
			TextView tvShortTitle;
			@ViewInject(R.id.tvDetailTitle)
			TextView tvDetailTitle;
			@ViewInject(R.id.tvOrigPrice)
			TextView tvOrigPrice;
			@ViewInject(R.id.tvCurPrice)
			TextView tvCurPrice;
			@ViewInject(R.id.tvGoodsNum)
			TextView tvGoodsNum;
			
		}
	}
}
