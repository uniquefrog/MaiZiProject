package com.uniquefrog.dianping.myutils;

import com.uniquefrog.dianping.R;

public class MyUtils {
	//cityҳ�����ת��ķ�����
	public static final int REQUEST_CODE=2;
	//result code
	public static final int RESULT_CODE=123;
	//��ҳ��������Դ
	//��ҳ������ַ���
	public static String[] strsSorts=new String[]
			{"��ʳ","��Ӱ","�Ƶ�","KTV","������",
					"��������","����","����","��������","ĸӤ",
					"Ůװ","��ױ","�����˶�","�������","ȫ��"};
	//��ҳ�����ͼ��
	public static int[] sortsImgs=new int[]{R.drawable.icon_home_food_99,R.drawable.icon_home_movie_29,
			R.drawable.icon_home_hotel_300,R.drawable.icon_home_ktv_31,R.drawable.icon_home_self_189,
			R.drawable.icon_home_happy_2,R.drawable.icon_home_flight_400,R.drawable.icon_home_shopping_3,
			R.drawable.icon_home_liren_442,R.drawable.icon_home_child_13,R.drawable.icon_home_nvzhuang_84,
			R.drawable.icon_home_meizhuang_173,R.drawable.icon_home_yundong_20,R.drawable.icon_home_life_46,
			R.drawable.icon_home_all_0};
	//������Ŀҳ���е�listview��
	//������Ŀ�е��ַ���
	public static String[] strsAllSorts=new String[]
			{"ȫ������","�����µ�","��ʳ","��������",
			"��Ӱ","�������","д������","�Ƶ�","����","��������",
			"������ѵ","�齱����","����"};
	//������Ŀ�е�ͼƬ
	public static int[] imgsAllSorts=new int[]
			{R.drawable.ic_all,R.drawable.ic_newest,
			R.drawable.ic_food,R.drawable.ic_entertain,
			R.drawable.ic_movie,R.drawable.ic_life,R.drawable.ic_photo,
			R.drawable.ic_hotel,R.drawable.ic_travel,R.drawable.ic_beauty,
			R.drawable.ic_edu,R.drawable.ic_luck,R.drawable.ic_shopping};
	
	//�������ÿ�����ĸ���
	public static long[] allSortsNum=new long[strsAllSorts.length+5];

}
