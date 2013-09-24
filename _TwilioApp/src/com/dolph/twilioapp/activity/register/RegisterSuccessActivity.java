package com.dolph.twilioapp.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.login.LoginActivity;

public class RegisterSuccessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_success);
		Intent data = getIntent();
		String username = data.getExtras().getString("username");
		TextView registerSuccess = (TextView) findViewById(R.id.registerSuccess);
		registerSuccess.setText("恭喜" + username + "! 注册成功");
	}

	public void backToLogin(View view) {
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			finish();
		}
	}

}
