package com.uniquefrog.dianping;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.uniquefrog.dianping.consts.Consts;
import com.uniquefrog.dianping.entity.ResponseObject;
import com.uniquefrog.dianping.entity.User;
import com.uniquefrog.dianping.myutils.MyUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends Activity {

	TextView tvRegisterVerificate;
	Button btnRegisterBack;
	EditText edtvRegisterPhone;
	EditText edtvRegisterPassword;
	EditText edtvVerificationCode;
	Button btnRegister;
	CheckBox cbAgreePro;
	EventHandler eh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initControl();
		initEvent();

	}
	private void initControl() {
		tvRegisterVerificate=(TextView) this.findViewById(R.id.tvRegisterVerificate);
		btnRegisterBack=(Button) this.findViewById(R.id.btnRegisterBack);
		edtvRegisterPhone=(EditText) this.findViewById(R.id.edtvRegisterPhone);
		edtvRegisterPassword=(EditText) this.findViewById(R.id.edtvRegisterPassword);
		edtvVerificationCode=(EditText) this.findViewById(R.id.edtvVerificationCode);
		btnRegister=(Button) this.findViewById(R.id.btnRegister);
		cbAgreePro=(CheckBox) this.findViewById(R.id.cbAgreePro);
		btnRegister.setEnabled(false);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterEventHandler(eh);
	}
	private void initEvent() {
		tvRegisterVerificate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//get the verification code of phone message
				//count the time
				new CountTimer(60000, 1000).start();
				//SMS verification process
				SMSSDK.initSDK(RegisterActivity.this, "11fdac48873d8", "77917bd4d2c710dabd79422a347d1bdd");
				eh=new EventHandler(){

					@Override
					public void afterEvent(int event, int result, Object data) {

						if (result == SMSSDK.RESULT_COMPLETE) {
							//�ص����
							if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
								//�ύ��֤��ɹ�
								Log.i("uniquefrog", "������֤�ɹ�");
							}else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
								//��ȡ��֤��ɹ�
								Log.i("uniquefrog", "��֤�뷢�ͳɹ�");
							}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
								//����֧�ַ�����֤��Ĺ����б�
							} 
						}else{                                                                 
							((Throwable)data).printStackTrace(); 
						}
					} 
				}; 
				SMSSDK.registerEventHandler(eh); //ע����Żص�
				//send the requeset to server for sms verification code
				SMSSDK.getVerificationCode("86", edtvRegisterPhone.getText().toString());

			}
		});
		btnRegisterBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SMSSDK.unregisterEventHandler(eh);
				finish();
			}
		});
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// register action,write into server
				if(edtvVerificationCode.getText().toString().trim()!=null){
					//submit the verification code to SMSSDK server
					SMSSDK.submitVerificationCode("86", edtvRegisterPhone.getText().toString(),
							edtvVerificationCode.getText().toString());
				}
				register();
			}
		});
		cbAgreePro.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// agree: enable btn
				if (isChecked) {
					btnRegister.setEnabled(true);
				}
				else{
					btnRegister.setEnabled(false);
				}
			}
		});
	}
	protected void register() {
		final String phoneNum=edtvRegisterPhone.getText().toString().trim();
		String userPassword=edtvRegisterPassword.getText().toString().trim();
		if (phoneNum==null ) {
			edtvRegisterPhone.setError(Html.fromHtml("<font color=red>�û�������Ϊ�գ�</font>"));
		}
		else if (userPassword==null) {
			edtvRegisterPassword.setError(Html.fromHtml("<font color=red>�û����벻��Ϊ�գ�</font>"));
		}else if (edtvVerificationCode==null) {
			edtvVerificationCode.setError(Html.fromHtml("<font color=red>��֤�벻��Ϊ�գ�</font>"));
		}
		else {
			RequestParams params=new RequestParams();
			params.addQueryStringParameter("username", phoneNum);
			params.addQueryStringParameter("password", userPassword);
			new HttpUtils().send(HttpMethod.GET, Consts.USER_REGISTER_URI,params, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(RegisterActivity.this, arg1, Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					// get the object which come from server
					ResponseObject<User> object=new Gson()
							.fromJson(arg0.result, 
						new TypeToken<ResponseObject<User>>(){}.getType());
					if (object.getState()==1) {
						//if register success
						Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_SHORT).show();
						Intent intent=getIntent();
						intent.putExtra("LoginName", phoneNum);
						setResult(MyUtils.RESULT_CODE, intent);
						finish();
					}
				}
			});
		}
	}
	protected class CountTimer extends CountDownTimer {

		/**
		 * 
		 * @param millisInFuture : the total time of count time
		 * @param countDownInterval��the interval time of count time 
		 */
		public CountTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tvRegisterVerificate.setClickable(false);
			tvRegisterVerificate.setText(millisUntilFinished/1000+"s�����·���");
			tvRegisterVerificate.setBackgroundResource(R.drawable.btn_light_press);

		}

		@Override
		public void onFinish() {
			tvRegisterVerificate.setClickable(true);
			tvRegisterVerificate.setText("���»�ȡ��֤��");
			tvRegisterVerificate.setBackgroundResource(R.drawable.my_register_get_check_pass);


		}

	}
}
