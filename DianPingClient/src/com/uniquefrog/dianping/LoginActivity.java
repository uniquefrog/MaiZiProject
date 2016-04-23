package com.uniquefrog.dianping;

import java.util.HashMap;

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
import com.uniquefrog.dianping.consts.Consts;
import com.uniquefrog.dianping.entity.ResponseObject;
import com.uniquefrog.dianping.entity.User;
import com.uniquefrog.dianping.myutils.MyUtils;
import com.uniquefrog.dianping.utils.Toolutils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends Activity {

	@ViewInject(R.id.btnLoginBack)
	Button btnLoginBack;
	@ViewInject(R.id.tvRegister)
	TextView tvRegister;
	@ViewInject(R.id.edtvUserName)
	EditText edtvUserName;
	@ViewInject(R.id.edtvUserPassword)
	EditText edtvUserPassword;
	@ViewInject(R.id.edtvVerificationCode)
	EditText edtvVerificationCode;
	@ViewInject(R.id.tvVerificationCode)
	TextView tvVerificationCode;
	@ViewInject(R.id.btnLogin)
	Button btnLogin;
	@ViewInject(R.id.tvLoginWeixin)
	TextView tvLoginWeixin;
	@ViewInject(R.id.tvLoginQQ)
	TextView tvLoginQQ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ShareSDK.initSDK(LoginActivity.this);
		ViewUtils.inject(LoginActivity.this);
		ViewEvents();
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode==MyUtils.REQUEST_CODE && resultCode==MyUtils.RESULT_CODE) {
			edtvUserName.setText(data.getStringExtra("LoginName"));
		}
	}
	private void ViewEvents() {
		//generate verification code text
		tvVerificationCode.setText(Toolutils.GenerateVerificationCode());
		tvVerificationCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tvVerificationCode.setText(Toolutils.GenerateVerificationCode());				
			}
		});
		//register event
		tvRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
				startActivityForResult(intent, MyUtils.REQUEST_CODE);
				
				
				
			}
		});
		btnLoginBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//login button event
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String userNme=edtvUserName.getText().toString();
				String userPassword=edtvUserPassword.getText().toString();
				String verificationCode=edtvVerificationCode.getText().toString();
				if (!"".equals(userNme) && !"".equals(userPassword) && !"".equals(verificationCode)) {
					if (verificationCode.equals(tvVerificationCode.getText())) {
						//verificate the ueser information from server
						verificationUser(userNme,userPassword);
					}
					else{
						Toast.makeText(LoginActivity.this, "验证码不正确", Toast.LENGTH_LONG).show();
						tvVerificationCode.setText(Toolutils.GenerateVerificationCode());
					}
				}
				else{
					Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
					
				}
				
			}
		});
		
		//weChat login
		tvLoginWeixin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				weChatLogin();
			}
		});
		//QQ login
		tvLoginQQ.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				qqLogin();
			}
		});
		
	}
	protected void verificationUser(final String username,final String userPassword) {
		RequestParams params=new RequestParams();
		params.addQueryStringParameter("username", username);
		params.addQueryStringParameter("password", userPassword);
		new HttpUtils().send(HttpMethod.GET, Consts.USER_DATA_URI, params,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(LoginActivity.this, "链接失败", Toast.LENGTH_LONG).show();
				tvVerificationCode.setText(Toolutils.GenerateVerificationCode());
			}

			@Override
			public void onSuccess(ResponseInfo<String> json) {
//				Gson gson=new Gson();
				ResponseObject<User> object=new GsonBuilder().create().fromJson(json.result,
						new TypeToken<ResponseObject<User>>(){}.getType());
				if (object.getState()==1) {
					//login successed
					loginSuccessed(edtvUserName.getText().toString());
					Toast.makeText(LoginActivity.this, "登录成功", 2000).show();
					//save the user information
					Toolutils.setUser(LoginActivity.this, username, userPassword);
				}
				else{
					Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	//weChat login method
	protected void weChatLogin() {
		//1.get the platform of sharesdk
				Platform platform=ShareSDK.getPlatform(LoginActivity.this,Wechat.NAME);
				//2.set the listener
				platform.setPlatformActionListener(new PlatformActionListener() {
					
					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						// show login error
						Toast.makeText(LoginActivity.this, arg0.getName()+"授权失败，请重试", Toast.LENGTH_LONG).show();
					}
					
					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						// when login is completed
						//get the name  which the platform show
						String userName=arg0.getDb().getUserName();
						loginSuccessed(userName);
						
						
					}
					
					@Override
					public void onCancel(Platform arg0, int arg1) {
						// show the tip when cancel the login
						Toast.makeText(LoginActivity.this, arg0.getName()+"授权已取消", Toast.LENGTH_LONG).show();
					}
				});
				//3.verificate the result of platform longin
				if (platform.isValid()) {
					loginSuccessed(platform.getDb().getUserName());
					
				}
				else{
					platform.showUser(null);
				}
				
	}
	//QQ login method
	protected void qqLogin() {
		//1.get the platform of sharesdk
		Platform platform=ShareSDK.getPlatform(LoginActivity.this,QQ.NAME);
		//2.set the listener
		platform.setPlatformActionListener(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// show login error
				Toast.makeText(LoginActivity.this, arg0.getName()+"授权失败，请重试", Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// when login is completed
				//get the name  which the platform show
				String userName=arg0.getDb().getUserName();
				loginSuccessed(userName);
				
				
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// show the tip when cancel the login
				Toast.makeText(LoginActivity.this, arg0.getName()+"授权已取消", Toast.LENGTH_LONG).show();
			}
		});
		//3.verificate the result of platform longin
		if (platform.isValid()) {
			loginSuccessed(platform.getDb().getUserName());
			
		}
		else{
			platform.showUser(null);
		}
		
		
		
	}
	//login successed
	protected void loginSuccessed(String userName) {
		Intent intent=this.getIntent();
		intent.putExtra("LoninName", userName);
		setResult(MyUtils.RESULT_CODE, intent);
		//startActivity(intent);
		//finish();
		finish();
	}
}
