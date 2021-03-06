package com.example.gridviewimageswitcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.color;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity {
	private ImageSwitcher switcher;
	private GridView grid;
	private int[] imgIds = { R.drawable.a1, R.drawable.a2, R.drawable.a3,
			R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7,
			R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11,
			R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15,
			R.drawable.a16 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < imgIds.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imgIds[i]);
			listItems.add(listItem);
		}

		switcher = (ImageSwitcher) findViewById(R.id.sitcher);
		// 设置更换的动画效果
		switcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		switcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		// 设置切换动画
		switcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				ImageView iv = new ImageView(MainActivity.this);
				iv.setBackgroundColor(color.black);
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setLayoutParams(new ImageSwitcher.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				return iv;
			}
		});

		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.cell, new String[] { "image" }, new int[] { R.id.iv });
		grid = (GridView) findViewById(R.id.grid01);
		grid.setAdapter(adapter);
		// grid.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// switcher.setImageResource(imgIds[position]);
		//
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		//
		// }
		// });
		
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switcher.setImageResource(imgIds[position]);
			}
		});
	}
}
