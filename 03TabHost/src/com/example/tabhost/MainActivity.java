package com.example.tabhost;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
		// 可以通过intent设置某个activity到tab中
		// .setContent(new Intent(this, MainActivity.class)));

		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("这是2")
				.setContent(R.id.ll02));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("这是3")
				.setContent(R.id.ll03));
		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("这是4")
				.setContent(R.id.ll04));

	}
}
