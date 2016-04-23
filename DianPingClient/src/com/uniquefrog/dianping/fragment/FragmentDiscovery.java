package com.uniquefrog.dianping.fragment;


import com.uniquefrog.dianping.LocationActivity;
import com.uniquefrog.dianping.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDiscovery extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_discovery_layout, container,false);
		startActivity(new Intent(getActivity(),LocationActivity.class));
		return view;
	}

}
