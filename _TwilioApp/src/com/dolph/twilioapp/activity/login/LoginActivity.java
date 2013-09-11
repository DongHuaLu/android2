package com.dolph.twilioapp.activity.login;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.main.NavigationActivity;
import com.dolph.twilioapp.activity.register.GetRegisterCodeActivity;
import com.dolph.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity {
	private static final int USERNAME_CANNOT_EMPTY = 0;
	private static final int PASSWORD_CANNOT_EMPTY = 1;
	private static final int SERVER_CANNOT_EMPTY = 2;
	private static final int CORRECT_INPUT = 3;

	private String TAG = "LoginActivity";

	private ProgressDialog pDialog;

	private AppValues appValues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		appValues = new AppValues(this);
	}

	public void login(View view) {
		EditText usernameEdit = (EditText) findViewById(R.id.editTextUser);
		EditText passwordEdit = (EditText) findViewById(R.id.editTextPassword);
		EditText serverEdit = (EditText) findViewById(R.id.editTextServer);
		switch (checkData(usernameEdit.getText().toString().trim(),
				passwordEdit.getText().toString().trim(), serverEdit.getText()
						.toString().trim())) {
		case USERNAME_CANNOT_EMPTY:
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			break;
		case PASSWORD_CANNOT_EMPTY:
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			break;
		case SERVER_CANNOT_EMPTY:
			Toast.makeText(this, "服务器不能为空", Toast.LENGTH_SHORT).show();
			break;
		case CORRECT_INPUT:
			associate(usernameEdit.getText().toString().trim(), passwordEdit
					.getText().toString().trim());
			break;

		}
	}

	private void associate(String username, String password) {
		pDialog = ProgressDialog.show(this, "请稍等", "正在向服务器请求");
		String url = "http://10.200.0.157:8080/TwilioServer01/Login?";
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", password);
		HttpUtils.get(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				pDialog.dismiss();
				try {
					JSONTokener jsonParser = new JSONTokener(content);
					JSONObject json = (JSONObject) jsonParser.nextValue();
					String state = json.getString("state");
					if ("err".equals(state)) {
						AlertDialog.Builder builder = new Builder(
								LoginActivity.this);
						builder.setTitle("提示");
						builder.setMessage(json.getString("response"));
						builder.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
						builder.show();
					} else if ("ok".equals(state)) {
						JSONTokener dataParser = new JSONTokener(json
								.getString("response"));
						JSONObject data = (JSONObject) dataParser.nextValue();
						String currentUserId = data.getString("userId");
						String currentUserName = data.getString("username");
						String currentMobilePhone = data
								.getString("mobile_phone");
						appValues.setCurrentPhoneNumber(currentMobilePhone);
						appValues.setCurrentUserName(currentUserName);
						appValues.setCurrentUserId(Integer
								.parseInt(currentUserId));

						appValues.setLogined(true);
						startNavigation();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				pDialog.dismiss();
				AlertDialog.Builder builder = new Builder(LoginActivity.this);
				builder.setTitle("提示");
				builder.setMessage("连接错误");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.show();
			}

		});

	}

	public void register(View view) {
		Intent intent = new Intent(this, GetRegisterCodeActivity.class);
		startActivity(intent);
	}

	public int checkData(String username, String password, String server) {
		if (TextUtils.isEmpty(username)) {
			return USERNAME_CANNOT_EMPTY;
		} else if (TextUtils.isEmpty(password)) {
			return PASSWORD_CANNOT_EMPTY;
		} else if (TextUtils.isEmpty(server)) {
			return SERVER_CANNOT_EMPTY;
		} else {
			return CORRECT_INPUT;
		}
	}

	public void startNavigation() {
		Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
		startActivityForResult(intent, 0);
		// finish();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			if (requestCode == 0) {
				finish();
			}
		}
	}
}
