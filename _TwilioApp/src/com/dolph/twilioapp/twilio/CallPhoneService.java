package com.dolph.twilioapp.twilio;

/*
 *  Copyright (c) 2011 by Twilio, Inc., all rights reserved.
 *
 *  Use of this software is subject to the terms and conditions of 
 *  the Twilio Terms of Service located at http://www.twilio.com/legal/tos
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.activity.login.LoginActivity;
import com.dolph.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.twilio.client.Connection;
import com.twilio.client.ConnectionListener;
import com.twilio.client.Device;
import com.twilio.client.Twilio;

public class CallPhoneService implements Twilio.InitListener {
	private static final String TAG = "MonkeyPhone";

	private static CallPhoneService service = null;

	private Device device;
	private Connection connection;
	private AppValues appValues;
	private String callNumber;
	private Date startDate;

	private CallPhoneService(Context context) {
		appValues = new AppValues(context.getApplicationContext());
		Twilio.initialize(context, this /* Twilio.InitListener */);
	}

	public static CallPhoneService getCallPhoneService(Context context) {
		if (service == null) {
			service = new CallPhoneService(context);
		}
		return service;
	}

	/* Twilio.InitListener method */
	@Override
	public void onInitialized() {
		Log.d(TAG, "Twilio SDK is ready");
		try {
			RequestParams params = new RequestParams();
			params.put("deviceId", appValues.getDeviceId());
			params.put("userId", appValues.getCurrentUserId() + "");
			HttpUtils.get(appValues.getServerPath() + "/loginfilter/TwilioAuth?", params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					device = Twilio.createDevice(content, null /* DeviceListener */);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Twilio.InitListener method */
	@Override
	public void onError(Exception e) {
		Log.e(TAG, "Twilio SDK couldn't start: " + e.getLocalizedMessage());
	}

	public void connect(String phoneNumber) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("PhoneNumber", phoneNumber);
		callNumber = phoneNumber;
		connection = device.connect(parameters, null /* ConnectionListener */);
		connection.setConnectionListener(new TwilioConnectionListener());
		if (connection == null)
			Log.w(TAG, "Failed to create new connection");
	}

	@Override
	protected void finalize() {
		if (connection != null)
			connection.disconnect();
		if (device != null)
			device.release();
	}

	public void disconnect() {
		if (connection != null) {
			connection.disconnect();
			connection = null;
		}

	}

	public class TwilioConnectionListener implements ConnectionListener {

		@Override
		public void onConnected(Connection inConnection) {

		}

		@Override
		public void onConnecting(Connection inConnection) {
			startDate = new Date();
		}

		@Override
		public void onDisconnected(Connection inConnection) {
			Date endDate = new Date();
			long duration = endDate.getTime() - startDate.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime = df.format(startDate);
			RequestParams params = new RequestParams();
			params.put("startTime", startTime);
			params.put("duration", duration + "");
			params.put("phoneNumber", callNumber);
			params.put("userId", appValues.getCurrentUserId() + "");
			params.put("deviceId", appValues.getDeviceId());
			HttpUtils.get(appValues.getServerPath() + "/loginfilter/AddRecord", params, new AsyncHttpResponseHandler());
		}

		@Override
		public void onDisconnected(Connection inConnection, int inErrorCode, String inErrorMessage) {

		}

	}
}
