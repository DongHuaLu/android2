package com.dolph.twilioapp.activity.contact;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.main.NavigationActivity.RefreshListener;
import com.dolph.twilioapp.model.Contact;
import com.dolph.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ContactListActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fm = getSupportFragmentManager();

		if (fm.findFragmentById(android.R.id.content) == null) {
			ContactListFragment list = new ContactListFragment();
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

	public static class ContactListFragment extends Fragment implements RefreshListener {

		private static final int REFRESH_VIEW = 1;
		private LinearLayout llContent;
		private LinearLayout linearLoading;
		private LinearLayout noData;
		private TextView mTitle;
		private EditText etSearch;
		private AppValues appValues;
		private ProgressDialog pDialog;
		private ListView contactList;
		private List<Contact> contacts;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
			appValues = new AppValues(getActivity().getApplicationContext());
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			View view = inflater.inflate(R.layout.fragment_contact, container, false);
			llContent = (LinearLayout) view.findViewById(R.id.llContent);
			contactList = (ListView) view.findViewById(R.id.contactList);
			linearLoading = (LinearLayout) view.findViewById(R.id.linearLoading);
			noData = (LinearLayout) view.findViewById(R.id.contactNoData);

			pDialog = ProgressDialog.show(getActivity(), "获取联系人", "正在获取数据...");
			getContact(appValues.getCurrentUserId(), null);

			final EditText searchEditText = (EditText) view.findViewById(R.id.etSearch);

			searchEditText.setOnKeyListener(new View.OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
						String sSearch = searchEditText.getText().toString();
						getContact(appValues.getCurrentUserId(), sSearch);
					}
					return false;
				}
			});
			return view;
		}

		public void getContact(int userId, String sSearch) {
			RequestParams params = new RequestParams();
			params.put("userId", userId + "");
			params.put("deviceId", appValues.getDeviceId());
			params.put("sSearch", sSearch);
			linearLoading.setVisibility(View.VISIBLE);
			llContent.setVisibility(View.GONE);
			HttpUtils.get("http://10.200.0.157:82/loginfilter/ContactList?", params, new AsyncHttpResponseHandler() {

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
								}
							});
							builder.show();
						} else if ("ok".equals(state)) {
							Gson gson = new Gson();
							contacts = gson.fromJson(json.getString("response"), new TypeToken<List<Contact>>() {
							}.getType());
							if (contacts != null && contacts.size() > 0) {
								contactList.setAdapter(new ContactAdapter());
								linearLoading.setVisibility(View.GONE);
								llContent.setVisibility(View.VISIBLE);
							} else {
								linearLoading.setVisibility(View.GONE);
								noData.setVisibility(View.VISIBLE);
							}
							contactList.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
									Contact contact = contacts.get(position);
									Intent contactIntent = new Intent(getActivity(), ContactActivity.class);
									contactIntent.putExtra("contact", contact);
									startActivityForResult(contactIntent, 0);
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
				getContact(appValues.getCurrentUserId(), null);
			}
		}

		private class ContactAdapter extends BaseAdapter {

			@Override
			public int getCount() {
				return contacts.size();
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
				Contact contact = contacts.get(position);
				View view = View.inflate(getActivity(), R.layout.list_contacts, null);
				TextView tvName = (TextView) view.findViewById(R.id.tvContactName);
				TextView tvAddress = (TextView) view.findViewById(R.id.tvContactAddress);
				TextView tvNumber = (TextView) view.findViewById(R.id.tvContactNumber);
				tvName.setText(contact.getName());
				tvAddress.setText(contact.getAddress());
				tvNumber.setText(contact.getNumber());
				return view;
			}
		}

		@Override
		public void refreshView() {
			getContact(appValues.getCurrentUserId(), null);
		}

		@Override
		public void forceRefreshView() {
			getContact(appValues.getCurrentUserId(), null);
		}

	}

}
