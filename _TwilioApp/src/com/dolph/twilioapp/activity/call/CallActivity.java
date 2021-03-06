package com.dolph.twilioapp.activity.call;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.login.LoginActivity;
import com.dolph.twilioapp.twilio.CallPhoneService;
import com.dolph.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CallActivity extends FragmentActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fm = getSupportFragmentManager();

		// Create the list fragment and add it as our sole content.
		if (fm.findFragmentById(android.R.id.content) == null) {
			CallFragment setting = new CallFragment();
			Bundle args = new Bundle();
			args.putString("number", getIntent().getStringExtra("number"));
			args.putBoolean("isActivity", true);
			setting.setArguments(args);
			fm.beginTransaction().add(android.R.id.content, setting).commit();
		}
	}

	public static class CallFragment extends Fragment {

		private String number = "";
		private EditText screenEditText;
		private ProgressDialog pDialog;
		private boolean isActivity;
		public static AlertDialog dialDialog;


		private AppValues appValues;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Bundle bundle = getArguments();
			number = bundle.getString("number");
			isActivity = bundle.getBoolean("isActivity");
			appValues = new AppValues(getActivity().getApplicationContext());
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_call, container, false);
			screenEditText = (EditText) view.findViewById(R.id.tvScreen);
			screenEditText.setInputType(InputType.TYPE_NULL);
			if (number != null) {
				setScreen(number);
			}
			final ToneGenerator tonePlayer = new ToneGenerator(AudioManager.STREAM_MUSIC, 70);
			Button m0Button = (Button) view.findViewById(R.id.button0);
			m0Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_0, 100);
					setScreen("0");

				}
			});
			m0Button.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					setScreen("+");
					return true;
				}
			});
			Button m1Button = (Button) view.findViewById(R.id.button1);
			m1Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_1, 100);
					setScreen("1");

				}
			});
			Button m2Button = (Button) view.findViewById(R.id.button2);
			m2Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_2, 100);
					setScreen("2");

				}
			});
			Button m3Button = (Button) view.findViewById(R.id.button3);
			m3Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_3, 100);
					setScreen("3");

				}
			});
			Button m4Button = (Button) view.findViewById(R.id.button4);
			m4Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_4, 100);
					setScreen("4");

				}
			});
			Button m5Button = (Button) view.findViewById(R.id.button5);
			m5Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_5, 100);
					setScreen("5");

				}
			});
			Button m6Button = (Button) view.findViewById(R.id.button6);
			m6Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_6, 100);
					setScreen("6");

				}
			});
			Button m7Button = (Button) view.findViewById(R.id.button7);
			m7Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_7, 100);
					setScreen("7");

				}
			});
			Button m8Button = (Button) view.findViewById(R.id.button8);
			m8Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_8, 100);
					setScreen("8");

				}
			});
			Button m9Button = (Button) view.findViewById(R.id.button9);
			m9Button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_9, 100);
					setScreen("9");

				}
			});
			Button mxButton = (Button) view.findViewById(R.id.buttonx);
			mxButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_S, 100);
					setScreen("*");

				}
			});
			Button mnButton = (Button) view.findViewById(R.id.buttonn);
			mnButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					tonePlayer.startTone(ToneGenerator.TONE_DTMF_P, 100);
					setScreen("#");

				}
			});
			Button callButton = (Button) view.findViewById(R.id.btCall);
			callButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					number = screenEditText.getText().toString();
					if (number == null || "".equals(number.trim())) {
						Toast.makeText(getActivity(), "电话号码不能为空", 0).show();
					} else {
						Intent calling = new Intent(getActivity(), CallingActivity.class);
						calling.putExtra("number", number);
						startActivity(calling);
					}
				}
			});
			Button backSpaceButton = (Button) view.findViewById(R.id.btBackspace);
			backSpaceButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!screenEditText.getText().toString().equals("")) {
						int cur = screenEditText.getSelectionStart();
						if (cur > 0) {
							screenEditText.getText().delete(cur - 1, cur);
						}
					}

				}
			});
			backSpaceButton.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					screenEditText.setText("");
					return true;
				}
			});

			Button addCotactButton = (Button) view.findViewById(R.id.btAddContact);

			/************************* 保存联系人 *******************************/
			addCotactButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (screenEditText.getText().toString().trim() != null && !"".equals(screenEditText.getText().toString().trim())) {
						AlertDialog.Builder builder = new Builder(getActivity());
						LinearLayout ll = new LinearLayout(getActivity());
						ll.setOrientation(LinearLayout.VERTICAL);
						final EditText editTextContactName = new EditText(getActivity().getApplicationContext());
						final EditText editTextContactAddress = new EditText(getActivity().getApplicationContext());
						editTextContactName.setTextColor(Color.WHITE);
						editTextContactAddress.setTextColor(Color.WHITE);
						editTextContactName.setHint("请输入联系人名称");
						editTextContactAddress.setHint("联系人地址");
						ll.addView(editTextContactName);
						ll.addView(editTextContactAddress);
						builder.setTitle("保存联系人");
						builder.setMessage("请输入联系人信息");
						builder.setView(ll);
						builder.setPositiveButton("保存", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								pDialog = ProgressDialog.show(getActivity(), "保存联系人", "正在保存...");
								RequestParams params = new RequestParams();
								params.put("userId", appValues.getCurrentUserId() + "");
								params.put("contactName", editTextContactName.getText().toString().trim());
								params.put("contactNumber", screenEditText.getText().toString().trim());
								params.put("address", editTextContactAddress.getText().toString().trim());
								params.put("deviceId", appValues.getDeviceId());
								HttpUtils.get(appValues.getServerPath() + "/loginfilter/AddContact?", params, new AsyncHttpResponseHandler() {

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
												builder.setPositiveButton("确定", new OnClickListener() {

													@Override
													public void onClick(DialogInterface dialog, int which) {
														startActivity(new Intent(getActivity(), LoginActivity.class));
														getActivity().finish();
													}
												});
												builder.show();
											} else if ("ok".equals(state)) {
												Toast.makeText(getActivity(), json.getString("response"), 0).show();
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}

									@Override
									protected void sendFailureMessage(Throwable e, String responseBody) {
										super.sendFailureMessage(e, responseBody);
										pDialog.dismiss();
										Toast.makeText(getActivity(), "网络错误", 0).show();
									}

								});

							}
						});
						builder.setNegativeButton("取消", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						});
						builder.create().show();
					} else {
						Toast.makeText(getActivity(), "保存的电话不能为空", 0).show();
					}
				}
			});
			/************************* 保存联系人 *******************************/
			return view;
		}

		protected void setScreen(String num) {
			screenEditText.getText().insert(screenEditText.getSelectionStart(), num);
		}

	}

}
