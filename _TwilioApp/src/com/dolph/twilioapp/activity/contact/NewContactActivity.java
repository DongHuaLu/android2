package com.dolph.twilioapp.activity.contact;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.login.LoginActivity;
import com.dolph.twilioapp.model.Contact;
import com.dolph.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NewContactActivity extends Activity {
	private static final int UPDATE_SUCCESS = 52;
	private static final int SESSION_ERR = 50;
	private EditText newContactName;
	private EditText newContactNumber;
	private EditText newContactAddress;
	private AppValues appValues;
	private Contact contact = null;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_contact);
		Intent dataIntent = getIntent();
		appValues = new AppValues(this.getApplicationContext());

		newContactName = (EditText) findViewById(R.id.newContactName);
		newContactNumber = (EditText) findViewById(R.id.newContactNumber);
		newContactAddress = (EditText) findViewById(R.id.newContactAddress);
		if (dataIntent.hasExtra("contact")) {
			Bundle data = dataIntent.getExtras();
			contact = (Contact) data.getSerializable("contact");
			newContactName.setText(contact.getName());
			newContactNumber.setText(contact.getNumber());
			newContactAddress.setText(contact.getAddress());
		}
	}

	public void saveContact(View view) {
		pDialog = ProgressDialog.show(this, "正在保存", "请等待服务器响应");
		RequestParams params = new RequestParams();
		if (contact != null) {
			params.put("contactId", contact.getId() + "");
		}
		params.put("contactName", newContactName.getText().toString());
		params.put("contactNumber", newContactNumber.getText().toString());
		params.put("address", newContactAddress.getText().toString());
		params.put("userId", appValues.getCurrentUserId() + "");
		params.put("deviceId", appValues.getDeviceId() + "");
		HttpUtils.get(appValues.getServerPath()+"/loginfilter/AddContact?", params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				pDialog.dismiss();
				JSONTokener jsonParser = new JSONTokener(content);
				JSONObject json;
				try {
					json = (JSONObject) jsonParser.nextValue();
					String state = json.getString("state");
					if ("sessionerr".equals(state)) {
						AlertDialog.Builder builder = new Builder(NewContactActivity.this);
						builder.setTitle("提示");
						builder.setMessage(json.getString("response"));
						builder.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								startActivity(new Intent(NewContactActivity.this, LoginActivity.class));
								setResult(SESSION_ERR);
								finish();
							}
						});
						builder.show();
					} else if ("err".equals(state)) {
						AlertDialog.Builder builder = new Builder(NewContactActivity.this);
						builder.setTitle("提示");
						builder.setMessage(json.getString("response"));
						builder.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						builder.show();
					} else if ("ok".equals(state)) {
						new AlertDialog.Builder(NewContactActivity.this).setTitle("成功").setMessage(json.getString("response")).setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent data = new Intent();
								data.putExtra("contactName", newContactName.getText().toString());
								data.putExtra("contactNumber", newContactNumber.getText().toString());
								data.putExtra("address", newContactAddress.getText().toString());
								setResult(UPDATE_SUCCESS, data);
								finish();
							}
						}).create().show();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				pDialog.dismiss();
				new AlertDialog.Builder(NewContactActivity.this).setTitle("失败").setMessage("保存失败").create().show();
			}

		});

	}

	public void back(View view) {
		finish();
	}

}
