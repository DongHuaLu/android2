package com.dolph.twilioapp.activity.password;

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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ResetPasswordActivity extends Activity {

	private TextView resetInfo;
	private String phoneNumber;
	private ProgressDialog pDialog;
	private AppValues appValues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appValues = new AppValues(this.getApplicationContext());
		setContentView(R.layout.activity_resetpassword);
		Intent data = getIntent();
		String code = data.getExtras().getString("code");
		phoneNumber = data.getExtras().getString("mobile_phone");
		resetInfo = (TextView) findViewById(R.id.resetInfo);
		resetInfo.setText("重置密码:\n验证码为" + code + "," + "注册的号码为" + phoneNumber);

	}

	public void back(View view) {
		finish();
	}

	public void register(View view) {
		EditText editTextCode = (EditText) findViewById(R.id.editText_reset_code);
		EditText editTextPassword1 = (EditText) findViewById(R.id.editText_reset_password);
		EditText editTextPassword2 = (EditText) findViewById(R.id.editText_reset_password2);

		String code = editTextCode.getText().toString();
		String password1 = editTextPassword1.getText().toString();
		String password2 = editTextPassword2.getText().toString();

		if ("".equals(password1) || "".equals(password2) || "".equals(code) || code == null || password1 == null || password2 == null || !password1.equals(password2)) {
			Toast.makeText(this, "验证错误", 0).show();
		} else {
			pDialog = ProgressDialog.show(this, "请稍等", "正在向服务器请求");
			String url = appValues.getServerPath() + "/GetPassword?";
			RequestParams params = new RequestParams();
			params.put("code", code);
			params.put("password", password1);
			params.put("password2", password2);
			params.put("mobile_phone", phoneNumber);
			HttpUtils.get(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					pDialog.dismiss();
					if ("修改成功".equals(content)) {
						Intent intent = new Intent(ResetPasswordActivity.this, SuccessActivity.class);
						intent.putExtra("info", content);
						startActivity(intent);
						finish();
					} else {
						AlertDialog.Builder builder = new Builder(ResetPasswordActivity.this);
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
					Toast.makeText(ResetPasswordActivity.this, "连接错误", 0).show();
				}

			});
		}
	}
}
