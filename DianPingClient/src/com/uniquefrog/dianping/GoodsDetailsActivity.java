package com.uniquefrog.dianping;

import org.w3c.dom.Text;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.uniquefrog.dianping.entity.Goods;
import com.uniquefrog.dianping.entity.Shop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GoodsDetailsActivity extends Activity {

	@ViewInject(R.id.btnGoodsTopBack)
	Button btnGoodsTopBack;
	@ViewInject(R.id.imgGoodsDetail)
	ImageView imgGoodsDetail;
	@ViewInject(R.id.tvGoodsShortTitle)
	TextView tvGoodsShortTitle;
	@ViewInject(R.id.tvGoodsDetail)
	TextView tvGoodsDetail;
	@ViewInject(R.id.tvGoodsShopName)
	TextView tvGoodsShopName;
	@ViewInject(R.id.tvGoodsShopPhoneNum)
	TextView tvGoodsShopPhoneNum;
	@ViewInject(R.id.tvGoodsShopAddress)
	TextView tvGoodsShopAddress;
	@ViewInject(R.id.tvBottomOrigPrice)
	TextView tvBottomOrigPrice;
	@ViewInject(R.id.tvBottomCurPrice)
	TextView tvBottomCurPrice;
	@ViewInject(R.id.goodsWarnWebView)
	WebView goodsWarnWebView;
	@ViewInject(R.id.goodsDetailWebView)
	WebView goodsDetailWebView;
	@ViewInject(R.id.imgGoodsCall)
	ImageView imgGoodsCall;
	Goods goods;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuan_good_detail);
		ViewUtils.inject(this);
		//draw the strike on the original price
		tvBottomOrigPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		// make the webview self-adaptable to the screen px
		WebSettings goodsDetailWebViewSettings=goodsDetailWebView.getSettings();
		goodsDetailWebViewSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		WebSettings goodsWarnWebViewSettings=goodsWarnWebView.getSettings();
		goodsWarnWebViewSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		Intent intent=getIntent();
		Bundle bundle=(Bundle) intent.getExtras();
		if (bundle!=null) {
			goods=(Goods) bundle.get("goods");
		}
		if (goods!=null) {
			//set the goods information 
			SetGoodsInfromation();
			//set shop information
			SetShopInformation();
			//set the goods details
			SetGoodsActivityDetails();
//			//set the warn
//			SetGoodsWarn();
//			//set the comment
//			SetGoodsComment();
		}
		btnGoodsTopBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private void SetGoodsInfromation() {
		Picasso.with(this).load(goods.getImgUrl())
		.placeholder(R.drawable.ic_empty_dish).into(imgGoodsDetail);
		// the title of goods
		tvGoodsShortTitle.setText(goods.getSortTitle());
		// the description of goods
		tvGoodsDetail.setText(goods.getTitle());
		//the current price of goods
		tvBottomCurPrice.setText("¥"+goods.getPrice());
		//the original price of goods
		tvBottomOrigPrice.setText("¥"+goods.getValue());
	}
	private void SetShopInformation() {
		final Shop shop=goods.getShop();
		//the name of shop
		tvGoodsShopName.setText(shop.getName());
		tvGoodsShopAddress.setText(shop.getAddress());
		tvGoodsShopPhoneNum.setText(shop.getTel());
		imgGoodsCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//into dial intent
				Intent intent=new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+shop.getTel()));
				startActivity(intent);
			}
		});
		
		
	}
	private void SetGoodsActivityDetails() {
		String[] data=HtmlDataParse(goods.getDetail());
		goodsDetailWebView.loadDataWithBaseURL("", data[0], "text/html",
				"UTF-8", "");
		goodsWarnWebView.loadDataWithBaseURL("", data[1],  "text/html",
				"UTF-8", "");
		
	}
	private void SetGoodsWarn() {
		// TODO Auto-generated method stub
		
	}
	private void SetGoodsComment() {
		// TODO Auto-generated method stub
		
	}
	private String[] HtmlDataParse(String html){
		String[] data=new String[3];
		char[] dataChar=html.toCharArray();
		int oneIndex=0,twoIndex=1,threeIndex=2;
		int n=0;
		for(int i=0;i<dataChar.length;i++){
			if (dataChar[i]=='【') {
				n++;
				if (n==1) {
					oneIndex=i;
				}
				if (n==2) {
					twoIndex=i;
				}
				if (n==3) {
					threeIndex=i;
				}
			}
			
		}
		data[0]=html.substring(oneIndex, twoIndex);
		data[1]=html.substring(twoIndex,threeIndex);
		data[2]=html.substring(threeIndex,html.length()-6);
		return data;
	}
	
	
}
