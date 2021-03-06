package com.dolph.twilioapp.activity.login;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.main.NavigationActivity;
import com.dolph.twilioapp.activity.password.GetForgetCodeActivity;
import com.dolph.twilioapp.activity.register.GetRegisterCodeActivity;
import com.dolph.utils.HttpUtils;
import com.dolph.utils.MD5;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity {
	private static final int USERNAME_CANNOT_EMPTY = 0;
	private static final int PASSWORD_CANNOT_EMPTY = 1;
	private static final int CORRECT_INPUT = 2;

	private String TAG = "LoginActivity";

	private ProgressDialog pDialog;

	private CheckBox autoLogin;
	private CheckBox rememberMe;
	private EditText usernameEdit;
	private EditText passwordEdit;
	private EditText server;

	private AppValues appValues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		appValues = new AppValues(this);
		autoLogin = (CheckBox) findViewById(R.id.autoLogin);
		rememberMe = (CheckBox) findViewById(R.id.rememberMe);
		usernameEdit = (EditText) findViewById(R.id.editTextUser);
		passwordEdit = (EditText) findViewById(R.id.editTextPassword);
		server = (EditText) findViewById(R.id.editServer);
		if (appValues.isRememberMe()) {
			rememberMe.setChecked(true);
			usernameEdit.setText(appValues.getCurrentUserName());
			passwordEdit.setText("");
			server.setText(appValues.getServerPath());
		}
		if (appValues.isAutoLogin()) {
			autoLogin.setChecked(true);
			if (!appValues.isLogouted()) {
				autoLogin(appValues.getCurrentUserName(), appValues.getCurrentPassword());
			}
		}
	}

	private void autoLogin(String username, String password) {
		switch (checkData(username, password)) {
		case USERNAME_CANNOT_EMPTY:
			return;
		case PASSWORD_CANNOT_EMPTY:
			return;
		case CORRECT_INPUT:
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String id = tm.getDeviceId();
			appValues.setLogouted(false);
			autoAssociate(username, password, id);
			break;
		}

	}

	public void login(View view) {
		switch (checkData(usernameEdit.getText().toString().trim(), passwordEdit.getText().toString().trim())) {
		case USERNAME_CANNOT_EMPTY:
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			break;
		case PASSWORD_CANNOT_EMPTY:
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			break;
		case CORRECT_INPUT:
			String serverPath = server.getText().toString().trim();
			if (serverPath == null || "".equals(serverPath)) {
				serverPath = "http://10.200.0.157:82";
			}
			appValues.setRememberMe(rememberMe.isChecked());
			appValues.setAutoLogin(autoLogin.isChecked());
			appValues.setServerPath(serverPath);
			appValues.setLogouted(false);
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String id = tm.getDeviceId();
			associate(usernameEdit.getText().toString().trim(), MD5.getMD5(passwordEdit.getText().toString().trim().getBytes()), id);
			break;

		}
	}

	private void autoAssociate(String username, String password, String id) {
		pDialog = ProgressDialog.show(this, "请稍等", "正在自动登录");
		String url = appValues.getServerPath() + "/Login?";
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", password);
		params.put("deviceId", id);
		appValues.setCurrentPassword(password);
		appValues.setDeviceId(id);
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
						Toast.makeText(LoginActivity.this, "自动登录失败", 1).show();
					} else if ("ok".equals(state)) {
						JSONTokener dataParser = new JSONTokener(json.getString("response"));
						JSONObject data = (JSONObject) dataParser.nextValue();
						String currentUserId = data.getString("userId");
						String currentUserName = data.getString("username");
						String currentMobilePhone = data.getString("mobile_phone");
						appValues.setCurrentPhoneNumber(currentMobilePhone);
						appValues.setCurrentUserName(currentUserName);
						appValues.setCurrentUserId(Integer.parseInt(currentUserId));
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
				Toast.makeText(LoginActivity.this, "自动登录失败", 0).show();
			}

		});

	}

	private void associate(String username, String password, String id) {
		pDialog = ProgressDialog.show(this, "请稍等", "正在向服务器请求");
		String url = appValues.getServerPath() + "/Login?";
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", password);
		params.put("deviceId", id);
		appValues.setCurrentPassword(password);
		appValues.setDeviceId(id);
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
						AlertDialog.Builder builder = new Builder(LoginActivity.this);
						builder.setTitle("提示");
						builder.setMessage(json.getString("response"));
						builder.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						builder.show();
					} else if ("ok".equals(state)) {
						JSONTokener dataParser = new JSONTokener(json.getString("response"));
						JSONObject data = (JSONObject) dataParser.nextValue();
						String currentUserId = data.getString("userId");
						String currentUserName = data.getString("username");
						String currentMobilePhone = data.getString("mobile_phone");
						appValues.setCurrentPhoneNumber(currentMobilePhone);
						appValues.setCurrentUserName(currentUserName);
						appValues.setCurrentUserId(Integer.parseInt(currentUserId));

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
				builder.setMessage("未能连接到服务器");
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

	public int checkData(String username, String password) {
		if (TextUtils.isEmpty(username)) {
			return USERNAME_CANNOT_EMPTY;
		} else if (TextUtils.isEmpty(password)) {
			return PASSWORD_CANNOT_EMPTY;
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

	public void forgetPassowrd(View view) {
		Intent intent = new Intent(this, GetForgetCodeActivity.class);
		startActivity(intent);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ExitDialog(LoginActivity.this).show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private Dialog ExitDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("系统信息");
		builder.setMessage("确定要退出程序吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}
}
