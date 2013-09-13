package com.dolph.twilioapp.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.login.LoginActivity;

public class SuccessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_success);
		Intent data = getIntent();
		String info = data.getExtras().getString("info");
		TextView successinfo = (TextView) findViewById(R.id.successinfo);
		successinfo.setText("恭喜" + info);
	}

	public void backToLogin(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

}
