package com.example.tabhost;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TabHost tabHost = getTabHost();

		LayoutInflater.from(this).inflate(R.layout.activity_main,
				tabHost.getTabContentView(), true);

		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("这是1",
						getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(R.id.ll01));

		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("这是2")
				.setContent(R.id.ll02));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("这是3")
				.setContent(R.id.ll03));
		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("这是4")
				.setContent(R.id.ll04));


	}
}