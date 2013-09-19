package com.dolph.twilioapp.activity.contact;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.login.LoginActivity;
import com.dolph.twilioapp.model.Contact;
import com.dolph.twilioapp.twilio.CallPhoneService;
import com.dolph.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ContactActivity extends Activity {
	private static final int REFRESH_VIEW = 1;
	private static final int UPDATE_SUCCESS = 2;
	private static final int SESSION_ERR = 50;
	private TextView contactName;
	private TextView contactNumber;
	private TextView contactAddress;
	private Contact contact;
	private CallPhoneService twilioService;
	private AppValues appValues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		appValues = new AppValues(this.getApplicationContext());
		contactName = (TextView) findViewById(R.id.ContactName);
		contactNumber = (TextView) findViewById(R.id.ContactNumber);
		contactAddress = (TextView) findViewById(R.id.ContactAddress);
		Intent dataIntent = getIntent();
		Bundle data = dataIntent.getExtras();
		contact = (Contact) data.getSerializable("contact");
		contactName.setText(contact.getName());
		contactNumber.setText(contact.getNumber());
		contactAddress.setText(contact.getAddress());
		twilioService = CallPhoneService.getCallPhoneService(this.getApplicationContext());
	}

	public void modifyContact(View view) {
		Intent intent = new Intent(this, NewContactActivity.class);
		intent.putExtra("contact", contact);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == UPDATE_SUCCESS) {
			contactName.setText(data.getStringExtra("contactName"));
			contactNumber.setText(data.getStringExtra("contactNumber"));
			contactAddress.setText(data.getStringExtra("address"));
		} else if (resultCode == SESSION_ERR) {
			setResult(SESSION_ERR);
			finish();
		}
	}

	public void call(View view) {
		String number = contactNumber.getText().toString();
		// if (Utils.validatePhone(number)) {
		if (true) {
			try {
				twilioService.connect(number);
				new AlertDialog.Builder(this).setTitle("拨打中").setMessage("正在拨打" + number).setNegativeButton("挂断", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						twilioService.disconnect();
					}
				}).create().show();
			} catch (Exception e) {
				new AlertDialog.Builder(this).setTitle("拨打失败").setNegativeButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create().show();
				e.printStackTrace();
			}
		}
	}

	public void deleteContact(View view) {
		RequestParams params = new RequestParams();
		params.put("userId", appValues.getCurrentUserId() + "");
		params.put("deviceId", appValues.getDeviceId());
		params.put("contactId", contact.getId() + "");

		HttpUtils.get(appValues.getServerPath() + "/loginfilter/DeleteContact?", params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				JSONTokener jsonParser = new JSONTokener(content);
				JSONObject json;
				try {
					json = (JSONObject) jsonParser.nextValue();
					String state = json.getString("state");
					if ("sessionerr".equals(state)) {
						AlertDialog.Builder builder = new Builder(ContactActivity.this);
						builder.setTitle("提示");
						builder.setMessage(json.getString("response"));
						builder.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								startActivity(new Intent(ContactActivity.this, LoginActivity.class));
								setResult(SESSION_ERR);
								finish();
							}
						});
						builder.show();
					} else if ("err".equals(state)) {
						AlertDialog.Builder builder = new Builder(ContactActivity.this);
						builder.setTitle("提示");
						builder.setMessage(json.getString("response"));
						builder.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						builder.show();
					} else if ("ok".equals(state)) {
						new AlertDialog.Builder(ContactActivity.this).setTitle("成功").setMessage(json.getString("response")).setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								setResult(REFRESH_VIEW);
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
				new AlertDialog.Builder(ContactActivity.this).setTitle("失败").setMessage("删除失败,可能网络有错误").create().show();
			}

		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(REFRESH_VIEW);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
