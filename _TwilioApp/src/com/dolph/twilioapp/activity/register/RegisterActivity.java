package com.dolph.twilioapp.activity.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.main.SuccessActivity;
import com.dolph.utils.HttpUtils;
import com.dolph.utils.MD5;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterActivity extends Activity {
	private TextView registerInfo;
	private String phoneNumber;
	private ProgressDialog pDialog;
	private String username;
	private AppValues appValues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		Intent data = getIntent();
		appValues = new AppValues(this.getApplicationContext());
		String code = data.getExtras().getString("code");
		phoneNumber = data.getExtras().getString("mobile_phone");
		registerInfo = (TextView) findViewById(R.id.registerInfo);
		registerInfo.setText("验证码为" + code + "," + "注册的号码为" + phoneNumber);

	}

	public void back(View view) {
		finish();
	}

	public void register(View view) {
		EditText editTextCode = (EditText) findViewById(R.id.editText_register_code);
		EditText editTextUsername = (EditText) findViewById(R.id.editText_register_username);
		EditText editTextPassword1 = (EditText) findViewById(R.id.editText_register_password);
		EditText editTextPassword2 = (EditText) findViewById(R.id.editText_register_password2);

		String code = editTextCode.getText().toString();
		username = editTextUsername.getText().toString();
		String password1 = editTextPassword1.getText().toString();
		String password2 = editTextPassword2.getText().toString();

		if ("".equals(username) || "".equals(password1) || "".equals(password2) || "".equals(code) || code == null || password1 == null || password2 == null || !password1.equals(password2)) {
			Toast.makeText(this, "验证错误", 0).show();
		} else {
			pDialog = ProgressDialog.show(this, "请稍等", "正在向服务器请求");
			String url = appValues.getServerPath() + "/Register?";
			RequestParams params = new RequestParams();
			params.put("code", code);
			params.put("user_name", username);
			params.put("password", MD5.getMD5(password1.getBytes()));
			params.put("password2", MD5.getMD5(password2.getBytes()));
			params.put("mobile_phone", phoneNumber);
			HttpUtils.get(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					pDialog.dismiss();
					if ("成功".equals(content)) {
						Intent intent = new Intent(RegisterActivity.this, SuccessActivity.class);
						intent.putExtra("info", username + "注册成功");
						startActivity(intent);
						finish();
					} else {
						AlertDialog.Builder builder = new Builder(RegisterActivity.this);
						builder.setTitle("提示");
						builder.setMessage(content);
						builder.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						builder.show();
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					pDialog.dismiss();
					Toast.makeText(RegisterActivity.this, "连接错误", 0).show();
				}

			});
		}
	}
}
