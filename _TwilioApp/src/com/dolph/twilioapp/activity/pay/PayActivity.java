package com.dolph.twilioapp.activity.pay;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.login.LoginActivity;
import com.dolph.twilioapp.activity.main.NavigationActivity.RefreshListener;
import com.dolph.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class PayActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fm = getSupportFragmentManager();

		if (fm.findFragmentById(android.R.id.content) == null) {
			PayFragment more = new PayFragment();
			fm.beginTransaction().add(android.R.id.content, more).commit();
		}
	}

	public static class PayFragment extends Fragment implements RefreshListener {

		// set to PaymentActivity.ENVIRONMENT_PRODUCTION to move real money.
		// set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test
		// credentials
		// from https://developer.paypal.com
		// set to PaymentActivity.ENVIRONMENT_NO_NETWORK to kick the tires
		// without
		// communicating to PayPal's servers.
		private static final String CONFIG_ENVIRONMENT = PaymentActivity.ENVIRONMENT_SANDBOX;

		// note that these credentials will differ between live & sandbox
		// environments.
		private static final String CONFIG_CLIENT_ID = "AYGhABDwnjS7rz609rWFpFVA1DWmdA1PilKGHjSTP2jgf6QUWI0bAtYhbSzG";
		// when testing in sandbox, this is likely the -facilitator email
		// address.
		private static final String CONFIG_RECEIVER_EMAIL = "dlu-facilitator@suzhoukada.com";

		private AppValues appValues;
		private ProgressDialog pDialog;
		private Button pay_1;
		private Button pay_5;
		private Button pay_9;
		private Button pay_20;
		private TextView accountInfoTV;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
			appValues = new AppValues(getActivity().getApplicationContext());

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_pay, container, false);
			pay_1 = (Button) view.findViewById(R.id.pay_1);
			pay_5 = (Button) view.findViewById(R.id.pay_5);
			pay_9 = (Button) view.findViewById(R.id.pay_9);
			pay_20 = (Button) view.findViewById(R.id.pay_20);
			accountInfoTV = (TextView) view.findViewById(R.id.accountInfo);

			Intent intent = new Intent(getActivity(), PayPalService.class);

			intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
			intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
			intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, CONFIG_RECEIVER_EMAIL);

			getActivity().startService(intent);
			getAccountInfo();

			pay_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal("1.00"), "USD", "pay 1$ to twilio app");

					Intent intent = new Intent(getActivity(), PaymentActivity.class);

					intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
					intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
					intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, CONFIG_RECEIVER_EMAIL);

					// It's important to repeat the clientId here so that the
					// SDK has it if
					// Android restarts your
					// app midway through the payment UI flow.
					intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "credential-from-developer.paypal.com");
					intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "your-customer-id-in-your-system");
					intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

					startActivityForResult(intent, 1);
				}
			});

			pay_5.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal("5.00"), "USD", "pay 5$ to twilio app");

					Intent intent = new Intent(getActivity(), PaymentActivity.class);

					intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
					intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
					intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, CONFIG_RECEIVER_EMAIL);

					// It's important to repeat the clientId here so that the
					// SDK has it if
					// Android restarts your
					// app midway through the payment UI flow.
					intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "credential-from-developer.paypal.com");
					intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "your-customer-id-in-your-system");
					intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

					startActivityForResult(intent, 2);
				}
			});

			pay_9.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal("9.00"), "USD", "pay 9$ to twilio app");

					Intent intent = new Intent(getActivity(), PaymentActivity.class);

					intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
					intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
					intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, CONFIG_RECEIVER_EMAIL);

					// It's important to repeat the clientId here so that the
					// SDK has it if
					// Android restarts your
					// app midway through the payment UI flow.
					intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "credential-from-developer.paypal.com");
					intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "your-customer-id-in-your-system");
					intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

					startActivityForResult(intent, 9);
				}
			});

			pay_20.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal("20.00"), "USD", "pay 20$ to twilio app");

					Intent intent = new Intent(getActivity(), PaymentActivity.class);

					intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
					intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
					intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, CONFIG_RECEIVER_EMAIL);

					// It's important to repeat the clientId here so that the
					// SDK has it if
					// Android restarts your
					// app midway through the payment UI flow.
					intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "credential-from-developer.paypal.com");
					intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "your-customer-id-in-your-system");
					intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

					startActivityForResult(intent, 9);
				}
			});

			return view;
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						pDialog = ProgressDialog.show(getActivity(), "付款", "正在像服务器请求...");
						RequestParams params = new RequestParams();
						params.put("userId", appValues.getCurrentUserId() + "");
						params.put("deviceId", appValues.getDeviceId());
						params.put("payInfo", confirm.toJSONObject().toString(4));
						HttpUtils.post(appValues.getServerPath() + "/loginfilter/payInPaypal?", params, new AsyncHttpResponseHandler() {

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
										AlertDialog.Builder builder = new Builder(getActivity());
										builder.setTitle("提示");
										builder.setMessage(json.getString("response"));
										builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {
												startActivity(new Intent(getActivity(), LoginActivity.class));
												getActivity().finish();
											}
										});
										builder.show();
									} else if ("err".equals(state)) {
										AlertDialog.Builder builder = new Builder(getActivity());
										builder.setTitle("提示");
										builder.setMessage(json.getString("response"));
										builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {
											}
										});
										builder.show();
									} else if ("ok".equals(state)) {
										new AlertDialog.Builder(getActivity()).setTitle("成功").setMessage(json.getString("response")).setPositiveButton("确定", new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {
											}
										}).create().show();
										getAccountInfo();
									}

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(Throwable error, String content) {
								super.onFailure(error, content);
								pDialog.dismiss();
								new AlertDialog.Builder(getActivity()).setTitle("失败").setMessage("网络错误,付款失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								}).create().show();
							}

						});
					} catch (JSONException e) {
						Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i("paymentExample", "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
				Log.i("paymentExample", "An invalid payment was submitted. Please see the docs.");
			}
		}

		@Override
		public void onDestroy() {
			getActivity().stopService(new Intent(getActivity(), PayPalService.class));
			super.onDestroy();
		}

		public void getAccountInfo() {
			RequestParams params = new RequestParams();
			params.put("userId", appValues.getCurrentUserId() + "");
			params.put("deviceId", appValues.getDeviceId());
			accountInfoTV.setText("正在获取账户信息....");
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
							AlertDialog.Builder builder = new Builder(getActivity());
							builder.setTitle("提示");
							builder.setMessage(json.getString("response"));

							builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									startActivity(new Intent(getActivity(), LoginActivity.class));
									getActivity().finish();
								}
							});
							builder.show();
						} else if ("err".equals(state)) {
							accountInfoTV.setText(json.getString("response"));
						} else if ("ok".equals(state)) {
							JSONTokener accountInfoT = new JSONTokener(json.getString("response"));
							JSONObject accountInfoJson = (JSONObject) accountInfoT.nextValue();
							if (accountInfoJson.getBoolean("monthly")) {
								accountInfoTV.setText("包月用户,过期时间为" + accountInfoJson.getString("endtime"));
							} else {
								accountInfoTV.setText("剩余时间:" + accountInfoJson.getString("times") + "分钟");
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					accountInfoTV.setText("获取账户信息失败");
				}
			});
		}

		@Override
		public void refreshView() {
			getAccountInfo();
		}

		@Override
		public void forceRefreshView() {
			getAccountInfo();
		}
	}

}
