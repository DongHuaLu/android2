package com.dolph.twilioapp.twilio;

/*
 *  Copyright (c) 2011 by Twilio, Inc., all rights reserved.
 *
 *  Use of this software is subject to the terms and conditions of 
 *  the Twilio Terms of Service located at http://www.twilio.com/legal/tos
 */

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.dolph.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.twilio.client.Connection;
import com.twilio.client.Device;
import com.twilio.client.Twilio;
import com.twilio.client.TwilioClientService;
import com.twilio.client.TwilioClientService.TwilioBinder;

public class CallPhoneService implements Twilio.InitListener {
	private static final String TAG = "MonkeyPhone";

	private Device device;
	private Connection connection;

	public CallPhoneService(Context context) {
		Twilio.initialize(context, this /* Twilio.InitListener */);
	}

	/* Twilio.InitListener method */
	@Override
	public void onInitialized() {
		Log.d(TAG, "Twilio SDK is ready");

		try {
			HttpUtils.get(
					"http://122.193.29.102:82/TwilioServer01/TwilioAuth",
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							device = Twilio
									.createDevice(content, null /* DeviceListener */);
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
		connection = device.connect(parameters, null /* ConnectionListener */);
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
}
