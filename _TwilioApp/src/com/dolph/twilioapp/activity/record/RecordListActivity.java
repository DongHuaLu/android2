package com.dolph.twilioapp.activity.record;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.login.LoginActivity;
import com.dolph.twilioapp.activity.main.NavigationActivity.RefreshListener;
import com.dolph.twilioapp.model.Record;
import com.dolph.utils.HttpUtils;
import com.dolph.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RecordListActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fm = getSupportFragmentManager();

		if (fm.findFragmentById(android.R.id.content) == null) {
			RecordListFragment list = new RecordListFragment();
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

	public static class RecordListFragment extends Fragment implements RefreshListener {

		private static final int REFRESH_VIEW = 51;
		private LinearLayout llRecordContent;
		private LinearLayout linearLoading;
		private LinearLayout noData;
		private TextView mTitle;
		private AppValues appValues;
		private ProgressDialog pDialog;
		private ListView recordList;
		private List<Record> records;
		private Record deleteRecord;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
			appValues = new AppValues(getActivity().getApplicationContext());
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			View view = inflater.inflate(R.layout.fragment_record, container, false);
			llRecordContent = (LinearLayout) view.findViewById(R.id.llRecordContent);
			recordList = (ListView) view.findViewById(R.id.recordList);
			linearLoading = (LinearLayout) view.findViewById(R.id.linearLoading);
			noData = (LinearLayout) view.findViewById(R.id.noData);

			pDialog = ProgressDialog.show(getActivity(), "获取通话记录", "正在获取数据...");
			getRecord(appValues.getCurrentUserId());

			return view;
		}

		public void getRecord(int userId) {
			RequestParams params = new RequestParams();
			params.put("userId", userId + "");
			params.put("deviceId", appValues.getDeviceId());
			linearLoading.setVisibility(View.VISIBLE);
			llRecordContent.setVisibility(View.GONE);
			HttpUtils.get(appValues.getServerPath() + "/loginfilter/RecordList?", params, new AsyncHttpResponseHandler() {

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
							Gson gson = new Gson();
							records = gson.fromJson(json.getString("response"), new TypeToken<List<Record>>() {
							}.getType());
							if (records != null && records.size() > 0) {
								recordList.setAdapter(new RecordAdapter());
								linearLoading.setVisibility(View.GONE);
								llRecordContent.setVisibility(View.VISIBLE);
							} else {
								linearLoading.setVisibility(View.GONE);
								noData.setVisibility(View.VISIBLE);
							}

							recordList.setOnItemLongClickListener(new OnItemLongClickListener() {

								@Override
								public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

									deleteRecord = records.get(position);
									new AlertDialog.Builder(getActivity()).setTitle("删除记录").setMessage("是否删除这条记录").setPositiveButton("确定", new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											RequestParams params = new RequestParams();
											params.put("userId", appValues.getCurrentUserId() + "");
											params.put("deviceId", appValues.getDeviceId());
											params.put("recordId", deleteRecord.getId() + "");
											pDialog = ProgressDialog.show(getActivity(), "删除", "正在删除...");
											HttpUtils.get(appValues.getServerPath() + "/loginfilter/DeleteRecord?", params, new AsyncHttpResponseHandler() {

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
														} else if ("err".equals(state)) {
															AlertDialog.Builder builder = new Builder(getActivity());
															builder.setTitle("提示");
															builder.setMessage(json.getString("response"));
															builder.setPositiveButton("确定", new OnClickListener() {

																@Override
																public void onClick(DialogInterface dialog, int which) {
																}
															});
															builder.show();
														} else if ("ok".equals(state)) {
															new AlertDialog.Builder(getActivity()).setTitle("成功").setMessage(json.getString("response")).setPositiveButton("确定", new OnClickListener() {

																@Override
																public void onClick(DialogInterface dialog, int which) {
																	getRecord(appValues.getCurrentUserId());
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
													new AlertDialog.Builder(getActivity()).setTitle("失败").setMessage("网络错误").setPositiveButton("确定", new OnClickListener() {
														@Override
														public void onClick(DialogInterface dialog, int which) {
														}
													}).create().show();
												}

											});
										}
									}).setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

										}
									}).create().show();
									return false;

								}
							});
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					pDialog.dismiss();
					Toast.makeText(getActivity(), "网络错误", 0).show();
				}
			});

		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == REFRESH_VIEW) {
				getRecord(appValues.getCurrentUserId());
			}
		}

		private class RecordAdapter extends BaseAdapter {

			@Override
			public int getCount() {
				return records.size();
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Record record = records.get(position);
				View view = View.inflate(getActivity(), R.layout.list_record, null);
				TextView recordStartTime = (TextView) view.findViewById(R.id.recordStartTime);
				TextView recordName = (TextView) view.findViewById(R.id.recordName);
				TextView recordNumber = (TextView) view.findViewById(R.id.recordNumber);
				TextView recordDuration = (TextView) view.findViewById(R.id.recordDuration);
				recordStartTime.setText(record.getStartTime());
				recordName.setText(record.getName());
				recordNumber.setText(record.getNumber());
				recordDuration.setText(Utils.durationFormat(record.getDuration()/1000));
				return view;
			}


		}

		@Override
		public void refreshView() {
			getRecord(appValues.getCurrentUserId());
		}

		@Override
		public void forceRefreshView() {
			getRecord(appValues.getCurrentUserId());
		}

	}

}
