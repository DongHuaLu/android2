package com.dolph.twilioapp.activity.more;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.dolph.twilioapp.AppValues;
import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.login.LoginActivity;
import com.dolph.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MoreActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fm = getSupportFragmentManager();

		if (fm.findFragmentById(android.R.id.content) == null) {
			MoreFragment more = new MoreFragment();
			fm.beginTransaction().add(android.R.id.content, more).commit();
		}
	}

	public static class MoreFragment extends Fragment {

		private AppValues appValues;
		private ProgressDialog pDialog;
		private Button logout;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
			appValues = new AppValues(getActivity().getApplicationContext());
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_more, container, false);
			logout = (Button) view.findViewById(R.id.logout);
			logout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RequestParams params = new RequestParams();
					params.put("deviceId", appValues.getDeviceId());
					params.put("userId", appValues.getCurrentUserId() + "");
					pDialog = ProgressDialog.show(getActivity(), "登出", "正在登出...");
					appValues.setLogouted(true);
					HttpUtils.setTimeout(5000);
					HttpUtils.get(appValues.getServerPath() + "/loginfilter/Logout?", params, new AsyncHttpResponseHandler() {

						@Override
						public void onFinish() {
							super.onFinish();
							HttpUtils.setTimeout(15000);
							startActivity(new Intent(getActivity(), LoginActivity.class));
							getActivity().finish();
						}
					});
				}
			});
			return view;
		}
	}

}
