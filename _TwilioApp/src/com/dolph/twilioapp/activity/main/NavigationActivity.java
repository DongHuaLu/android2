package com.dolph.twilioapp.activity.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;

import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.call.CallActivity;

public class NavigationActivity extends FragmentActivity {

	private final int CALL_FRAGMENT = 1;
	private final int RECORD_FRAGMENT = 2;
	private final int CONTACTS_FRAGMENT = 3;
	private final int PAY_FRAGMENT = 4;
	private final int MORE_FRAGMENT = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		switchFragment(CALL_FRAGMENT);
	}
	
	
	public interface RefreshListener {
		void refreshView();

		void forceRefreshView();
	}

	FragmentManager fm;

	public void switchFragment(int position) {

		fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fCall = fm.findFragmentById(R.id.fCall);
		Fragment fRecord = fm.findFragmentById(R.id.fRecord);
		Fragment fContacts = fm.findFragmentById(R.id.fContacts);
		Fragment fPay = fm.findFragmentById(R.id.fPay);
		Fragment fMore = fm.findFragmentById(R.id.fMore);

		switch (position) {
		case CALL_FRAGMENT:
			if (fRecord != null) {
				ft.hide(fRecord);
			}
			if (fContacts != null) {
				ft.hide(fContacts);
			}
			if (fPay != null) {
				ft.hide(fPay);
			}
			if (fMore != null) {
				ft.hide(fMore);
			}
			if (fCall == null) {
				fCall = Fragment.instantiate(this,
						CallActivity.CallFragment.class.getName());
				Bundle args = new Bundle();
				args.putString("number", "");
				args.putBoolean("isActivity", false);
				fCall.setArguments(args);
				ft.add(R.id.fCall, fCall);
			} else {
				ft.show(fCall);
			}
			ft.commit();
			// lastFragment = null;
			// messageFrameLayout.setVisibility(View.GONE);
			// doctorFrameLayout.setVisibility(View.GONE);
			// taskFrameLayout.setVisibility(View.GONE);
			// callFrameLayout.setVisibility(View.VISIBLE);
			// moreFrameLayout.setVisibility(View.GONE);
			// messagesButton.setBackgroundResource(R.drawable.tab_messages);
			// doctorsButton.setBackgroundResource(R.drawable.tab_doctors);
			// tasksButton.setBackgroundResource(R.drawable.tab_tasks);
			// callButton.setBackgroundResource(R.drawable.tab_call_press);
			// moreButton.setBackgroundResource(R.drawable.tab_more);
			// messageLinearLayout.setBackgroundDrawable(null);
			// doctorLinearLayout.setBackgroundDrawable(null);
			// taskLinearLayout.setBackgroundDrawable(null);
			// callLinearLayout
			// .setBackgroundResource(R.drawable.footer_menu_bg_select);
			// moreLinearLayout.setBackgroundDrawable(null);

			break;

		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ExitDialog(NavigationActivity.this).show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private Dialog ExitDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("系统信息");
		builder.setMessage("确定要退出程序吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}
}
