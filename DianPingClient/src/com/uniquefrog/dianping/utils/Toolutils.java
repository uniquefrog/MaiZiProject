package com.uniquefrog.dianping.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Toolutils {
	private static final String FILE_NAME="dianping";
	private static final String MODE_NAME="welcome";
	private static final String MODE_NAME_LOCATION="location";
	private static Context mcontext;
	//首次进入一定是false，第二次就被判断后，设置成true
	public static boolean getIsUsed(Context context){
		mcontext=context;
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
		.getBoolean(MODE_NAME, false);
	}
	//对于不是第一次的这里进行设置为true
	public static void setIsUsed(boolean isFirst){
		Editor editor=mcontext.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
		editor.putBoolean(MODE_NAME, isFirst);
		editor.commit();
		
	}
	public static String getCity(Context context){
		mcontext=context;
		return mcontext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
				.getString(MODE_NAME_LOCATION, "选择城市");
	}
	public static void setCity(Context context,String cityName){
		Editor editor=context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
		editor.putString(MODE_NAME_LOCATION, cityName);
		editor.commit();
	}
	//generate four bit verification code
	public static String GenerateVerificationCode(){
		String strRandom="123456789qazwsxedcrfvtgbyhnujmiklopQAZPLMOKIJNBHUYGVFTXCSDWRE";
		StringBuilder verificationCode=new StringBuilder();
		for(int i=0;i<4;i++){
			int random=
					(int)((Math.random())*(strRandom.length()-1));
			//Log.i("uniquefrog", "the random char is "+strRandom.charAt(random));
			verificationCode.append(strRandom.charAt(random));
		}
		return verificationCode.toString();
		
	}
	//save User information 
	public static String[] getUser(Context context){
		String username=context.getSharedPreferences
				(FILE_NAME, Context.MODE_APPEND).getString("username", "点击登录");
		String userPassword=context.getSharedPreferences
				(FILE_NAME, Context.MODE_APPEND).getString("password", "123456");
		return new String[]{username,userPassword};
		
	}
	//set User information
	public static void setUser(Context context,String userName,String userPassword){
		Editor editor=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
		editor.putString("username", userName);
		editor.putString("password", userPassword);
		editor.commit();
	}
	
}
