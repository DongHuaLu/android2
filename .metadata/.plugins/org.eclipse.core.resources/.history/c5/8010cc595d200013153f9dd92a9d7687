package com.dolph.twilioapp.activity.register;

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

import com.dolph.twilioapp.R;
import com.dolph.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GetRegisterCodeActivity extends Activity {
	private ProgressDialog pDialog;
	String phoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getregistercode);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void back(View view) {
		finish();
	}

	public void getRegistorCode(View view) {
		EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);
		phoneNumber = editTextPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			Toast.makeText(this, "电话号码不能为空", 0).show();
			return;
		} else {
			String url = "http://10.200.0.157:82/GetRegisterCode?"
					+ phoneNumber;
			RequestParams params = new RequestParams();
			params.put("mobile_phone", phoneNumber);
			pDialog = ProgressDialog.show(this, "请稍等", "正在向服务器请求");
			HttpUtils.get(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					pDialog.dismiss();
					if ("电话已经被注册".equals(content) || "电话不能为空".equals(content)) {
						AlertDialog.Builder builder = new Builder(
								GetRegisterCodeActivity.this);
						builder.setTitle("提示");
						builder.setMessage(content);
						builder.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
						builder.show();
					} else {
						Intent intent = new Intent(
								GetRegisterCodeActivity.this,
								RegisterActivity.class);
						intent.putExtra("code", content);
						intent.putExtra("mobile_phone", phoneNumber);
						startActivity(intent);
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					pDialog.dismiss();
					Toast.makeText(GetRegisterCodeActivity.this, "连接错误", 0)
							.show();
				}
			});
		}
	}
}
