package com.example.drawview;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

public class MainActivity extends Activity {
	DrawView dv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LinearLayout ll = (LinearLayout) findViewById(R.id.root);
		dv = new DrawView(this);
		dv.setMinimumHeight(400);
		dv.setMinimumWidth(500);
		
		
		dv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dv.currentX = event.getX();
				dv.currentY = event.getY();

				dv.invalidate();
				return true;
			}
		});
		ll.addView(dv);
	}
}
