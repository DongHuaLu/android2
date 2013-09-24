package com.dolph.twilioapp.activity.call;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.main.NavigationActivity;
import com.dolph.twilioapp.twilio.CallPhoneService;
import com.dolph.utils.HttpUtils;
import com.dolph.utils.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CallingActivity extends Activity {
	private static final int ARREARAGE = 66;
	protected static final int CHANGE_DURATION = 44;
	private static final int CALL_SERVER = 56;
	private TextView durationTv;
	private TextView phoneNumberTv;
	private AppValues appValues;
	private CallPhoneService twilioService;
	private int times = 0;
	private int duration = 0;
	private String number = null;
	private boolean monthly = false;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == CHANGE_DURATION) {
				durationTv.setText(Utils.durationFormat((Integer) msg.obj));
			} else if (msg.what == ARREARAGE) {
				twilioService.disconnect();
				RequestParams params = new RequestParams();
				params.put("deviceId", appValues.getDeviceId());
				params.put("userId", appValues.getCurrentUserId() + "");
				params.put("duration", (duration / 60) + "");
				HttpUtils.get(appValues.getServerPath() + "/loginfilter/updateTimes?", params, new AsyncHttpResponseHandler());
				new AlertDialog.Builder(CallingActivity.this).setTitle("欠费").setMessage("你的账户费用不足").setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CallingActivity.this.finish();
					}
				}).create().show();
			} else if (msg.what == CALL_SERVER) {
				twilioService.connect(number);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calling);
		durationTv = (TextView) findViewById(R.id.druationTv);
		phoneNumberTv = (TextView) findViewById(R.id.phoneNumberTv);
		Intent data = getIntent();
		number = data.getStringExtra("number");
		phoneNumberTv.setText(number);
		durationTv.setText("00:00");
		appValues = new AppValues(getApplicationContext());
		twilioService = CallPhoneService.getCallPhoneService(getApplicationContext());
		call();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void hangup(View view) {
		twilioService.disconnect();
		if (!monthly) {
			RequestParams params = new RequestParams();
			params.put("deviceId", appValues.getDeviceId());
			params.put("userId", appValues.getCurrentUserId() + "");
			params.put("duration", (int) (Math.ceil(duration / 60.0)) + "");
			HttpUtils.get(appValues.getServerPath() + "/loginfilter/updateTimes?", params, new AsyncHttpResponseHandler());
		}
		finish();
	}

	public void call() {
		RequestParams params = new RequestParams();
		params.put("userId", appValues.getCurrentUserId() + "");
		params.put("deviceId", appValues.getDeviceId());
		HttpUtils.get(appValues.getServerPath() + "/loginfilter/getAccountInfo?", params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				JSONTokener jsonParser = new JSONTokener(content);
				JSONObject json;
				try {
					json = (JSONObject) jsonParser.nextValue();
					String state = json.getString("state");
					if ("sessionerr".equals(state)) {
						new AlertDialog.Builder(CallingActivity.this).setTitle("失败").setMessage(json.getString("response")).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								CallingActivity.this.finish();
							}
						}).create().show();
					} else if ("err".equals(state)) {
						new AlertDialog.Builder(CallingActivity.this).setTitle("失败").setMessage(json.getString("response")).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								CallingActivity.this.finish();
							}
						}).create().show();
					} else if ("ok".equals(state)) {
						JSONTokener accountInfoT = new JSONTokener(json.getString("response"));
						JSONObject accountInfoJson = (JSONObject) accountInfoT.nextValue();
						if (monthly = accountInfoJson.getBoolean("monthly")) {
							DurationCounter counter = new DurationCounter();
							counter.start();
						} else {
							times = Integer.parseInt(accountInfoJson.getString("times"));
							DurationCounter counter = new DurationCounter();
							counter.start();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			class DurationCounter extends Thread {
				@Override
				public void run() {
					super.run();
					if ((times - duration / 60) > 0 || monthly) {
						Message msg = new Message();
						msg.what = CALL_SERVER;
						handler.sendMessage(msg);
					}
					while ((times - duration / 60) > 0 || monthly) {
						duration++;
						Message msg = new Message();
						msg.what = CHANGE_DURATION;
						msg.obj = duration;
						handler.sendMessage(msg);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Message arrearage = new Message();
					arrearage.what = ARREARAGE;
					handler.sendMessage(arrearage);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				new AlertDialog.Builder(CallingActivity.this).setTitle("失败").setMessage("认证账户信息失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CallingActivity.this.finish();
					}
				});
			}
		});
	}
}
