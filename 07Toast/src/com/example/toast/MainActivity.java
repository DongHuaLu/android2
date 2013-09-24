package com.example.toast;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Configuration cfg = getResources().getConfiguration();
	}

	public void click(View view) {
		Toast toast = Toast.makeText(this, "带图片的toast", 1);
		toast.setGravity(Gravity.CENTER, 0, 0);
		View toastView = toast.getView();
		ImageView iv = new ImageView(this);
		iv.setImageResource(R.drawable.ic_launcher);
		LinearLayout ll = new LinearLayout(this);
		ll.addView(iv);
		ll.addView(toastView);
		toast.setView(ll);
		toast.show();

	}

}
