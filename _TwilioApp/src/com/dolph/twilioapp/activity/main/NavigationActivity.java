package com.dolph.twilioapp.activity.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.dolph.twilioapp.R;
import com.dolph.twilioapp.activity.call.CallActivity;
import com.dolph.twilioapp.activity.contact.ContactListActivity;
import com.dolph.twilioapp.activity.contact.NewContactActivity;
import com.dolph.twilioapp.activity.more.MoreActivity;
import com.dolph.twilioapp.activity.pay.PayActivity;
import com.dolph.twilioapp.activity.record.RecordListActivity;
import com.dolph.twilioapp.twilio.CallPhoneService;

public class NavigationActivity extends FragmentActivity {

	private final int CALL_FRAGMENT = 1;
	private final int RECORD_FRAGMENT = 2;
	private final int CONTACTS_FRAGMENT = 3;
	private final int PAY_FRAGMENT = 4;
	private final int MORE_FRAGMENT = 5;
	private static final int SESSION_ERR = 50;
	private static final int UPDATE_SUCCESS = 52;

	private Fragment lastFragment;
	private Button btCall, btRecord, btContacts, btPay, btMore;
	private RelativeLayout callLinearLayout, recordLinearLayout, contactLinearLayout, payLinearLayout, moreLinearLayout;
	private FrameLayout callFrameLayout, recordFrameLayout, contactFrameLayout, payFrameLayout, moreFrameLayout;

	public void addContact(View view) {
		Intent intent = new Intent(this, NewContactActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		CallPhoneService callService = CallPhoneService.getCallPhoneService(getApplicationContext());
		btCall = (Button) findViewById(R.id.btCall);
		btRecord = (Button) findViewById(R.id.btRecord);
		btContacts = (Button) findViewById(R.id.btContacts);
		btPay = (Button) findViewById(R.id.btPay);
		btMore = (Button) findViewById(R.id.btMore);

		btCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchFragment(CALL_FRAGMENT);
			}
		});
		btRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchFragment(RECORD_FRAGMENT);
			}
		});
		btContacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchFragment(CONTACTS_FRAGMENT);
			}
		});
		btPay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchFragment(PAY_FRAGMENT);
			}
		});
		btMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchFragment(MORE_FRAGMENT);
			}
		});

		callFrameLayout = (FrameLayout) findViewById(R.id.fCall);
		recordFrameLayout = (FrameLayout) findViewById(R.id.fRecord);
		contactFrameLayout = (FrameLayout) findViewById(R.id.fContacts);
		payFrameLayout = (FrameLayout) findViewById(R.id.fPay);
		moreFrameLayout = (FrameLayout) findViewById(R.id.fMore);

		callLinearLayout = (RelativeLayout) findViewById(R.id.rlCall);
		recordLinearLayout = (RelativeLayout) findViewById(R.id.rlRecord);
		contactLinearLayout = (RelativeLayout) findViewById(R.id.rlContacts);
		payLinearLayout = (RelativeLayout) findViewById(R.id.rlPay);
		moreLinearLayout = (RelativeLayout) findViewById(R.id.rlMore);

		callLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btCall.performClick();
			}
		});
		recordLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btRecord.performClick();
			}
		});
		contactLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btContacts.performClick();
			}
		});
		payLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btPay.performClick();
			}
		});
		moreLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btMore.performClick();
			}
		});

		btCall.performClick();

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
				fCall = Fragment.instantiate(this, CallActivity.CallFragment.class.getName());
				Bundle args = new Bundle();
				args.putString("number", "");
				args.putBoolean("isActivity", false);
				fCall.setArguments(args);
				ft.add(R.id.fCall, fCall);
			} else {
				ft.show(fCall);
			}
			ft.commit();
			lastFragment = null;
			callFrameLayout.setVisibility(View.VISIBLE);
			recordFrameLayout.setVisibility(View.GONE);
			contactFrameLayout.setVisibility(View.GONE);
			payFrameLayout.setVisibility(View.GONE);
			moreFrameLayout.setVisibility(View.GONE);
			btCall.setBackgroundColor(Color.GREEN);
			btContacts.setBackgroundColor(Color.BLACK);
			btRecord.setBackgroundColor(Color.BLACK);
			btPay.setBackgroundColor(Color.BLACK);
			btMore.setBackgroundColor(Color.BLACK);
			callLinearLayout.setBackgroundResource(R.drawable.footer_menu_bg_select);
			contactLinearLayout.setBackgroundDrawable(null);
			payLinearLayout.setBackgroundDrawable(null);
			moreLinearLayout.setBackgroundDrawable(null);
			recordLinearLayout.setBackgroundDrawable(null);
			break;

		case CONTACTS_FRAGMENT:
			if (fRecord != null) {
				ft.hide(fRecord);
			}
			if (fCall != null) {
				ft.hide(fCall);
			}
			if (fPay != null) {
				ft.hide(fPay);
			}
			if (fMore != null) {
				ft.hide(fMore);
			}
			if (fContacts == null) {
				fContacts = Fragment.instantiate(this, ContactListActivity.ContactListFragment.class.getName());
				ft.add(R.id.fContacts, fContacts);
			} else {
				ft.show(fContacts);
				RefreshListener f = (RefreshListener) fContacts;
				f.refreshView();

			}
			ft.commit();
			lastFragment = fContacts;
			contactFrameLayout.setVisibility(View.VISIBLE);
			callFrameLayout.setVisibility(View.GONE);
			recordFrameLayout.setVisibility(View.GONE);
			payFrameLayout.setVisibility(View.GONE);
			moreFrameLayout.setVisibility(View.GONE);
			btContacts.setBackgroundColor(Color.GREEN);
			btCall.setBackgroundColor(Color.BLACK);
			btRecord.setBackgroundColor(Color.BLACK);
			btPay.setBackgroundColor(Color.BLACK);
			btMore.setBackgroundColor(Color.BLACK);
			contactLinearLayout.setBackgroundResource(R.drawable.footer_menu_bg_select);
			callLinearLayout.setBackgroundDrawable(null);
			payLinearLayout.setBackgroundDrawable(null);
			moreLinearLayout.setBackgroundDrawable(null);
			recordLinearLayout.setBackgroundDrawable(null);
			break;

		case RECORD_FRAGMENT:
			if (fCall != null) {
				ft.hide(fCall);
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
			if (fRecord == null) {
				fRecord = Fragment.instantiate(this, RecordListActivity.RecordListFragment.class.getName());
				ft.add(R.id.fRecord, fRecord);
			} else {
				ft.show(fRecord);
				RefreshListener f = (RefreshListener) fRecord;
				f.refreshView();
			}
			ft.commit();
			lastFragment = fRecord;
			recordFrameLayout.setVisibility(View.VISIBLE);
			callFrameLayout.setVisibility(View.GONE);
			contactFrameLayout.setVisibility(View.GONE);
			payFrameLayout.setVisibility(View.GONE);
			moreFrameLayout.setVisibility(View.GONE);
			btRecord.setBackgroundColor(Color.GREEN);
			btCall.setBackgroundColor(Color.BLACK);
			btContacts.setBackgroundColor(Color.BLACK);
			btPay.setBackgroundColor(Color.BLACK);
			btMore.setBackgroundColor(Color.BLACK);
			recordLinearLayout.setBackgroundResource(R.drawable.footer_menu_bg_select);
			contactLinearLayout.setBackgroundDrawable(null);
			payLinearLayout.setBackgroundDrawable(null);
			moreLinearLayout.setBackgroundDrawable(null);
			callLinearLayout.setBackgroundDrawable(null);
			break;

		case MORE_FRAGMENT:
			if (fRecord != null) {
				ft.hide(fRecord);
			}
			if (fContacts != null) {
				ft.hide(fContacts);
			}
			if (fPay != null) {
				ft.hide(fPay);
			}
			if (fCall != null) {
				ft.hide(fCall);
			}
			if (fMore == null) {
				fMore = Fragment.instantiate(this, MoreActivity.MoreFragment.class.getName());
				ft.add(R.id.fMore, fMore);
			} else {
				ft.show(fMore);
			}
			ft.commit();
			lastFragment = fMore;
			moreFrameLayout.setVisibility(View.VISIBLE);
			callFrameLayout.setVisibility(View.GONE);
			recordFrameLayout.setVisibility(View.GONE);
			contactFrameLayout.setVisibility(View.GONE);
			payFrameLayout.setVisibility(View.GONE);
			btMore.setBackgroundColor(Color.GREEN);
			btCall.setBackgroundColor(Color.BLACK);
			btContacts.setBackgroundColor(Color.BLACK);
			btRecord.setBackgroundColor(Color.BLACK);
			btPay.setBackgroundColor(Color.BLACK);
			moreLinearLayout.setBackgroundResource(R.drawable.footer_menu_bg_select);
			contactLinearLayout.setBackgroundDrawable(null);
			payLinearLayout.setBackgroundDrawable(null);
			callLinearLayout.setBackgroundDrawable(null);
			recordLinearLayout.setBackgroundDrawable(null);
			break;

		case PAY_FRAGMENT:
			if (fRecord != null) {
				ft.hide(fRecord);
			}
			if (fContacts != null) {
				ft.hide(fContacts);
			}
			if (fMore != null) {
				ft.hide(fMore);
			}
			if (fCall != null) {
				ft.hide(fCall);
			}
			if (fPay == null) {
				fPay = Fragment.instantiate(this, PayActivity.PayFragment.class.getName());
				ft.add(R.id.fPay, fPay);
			} else {
				ft.show(fPay);
				RefreshListener f = (RefreshListener) fPay;
				f.refreshView();
			}
			ft.commit();
			lastFragment = fPay;
			payFrameLayout.setVisibility(View.VISIBLE);
			callFrameLayout.setVisibility(View.GONE);
			recordFrameLayout.setVisibility(View.GONE);
			contactFrameLayout.setVisibility(View.GONE);
			moreFrameLayout.setVisibility(View.GONE);
			btPay.setBackgroundColor(Color.GREEN);
			btCall.setBackgroundColor(Color.BLACK);
			btContacts.setBackgroundColor(Color.BLACK);
			btRecord.setBackgroundColor(Color.BLACK);
			btMore.setBackgroundColor(Color.BLACK);
			payLinearLayout.setBackgroundResource(R.drawable.footer_menu_bg_select);
			contactLinearLayout.setBackgroundDrawable(null);
			moreLinearLayout.setBackgroundDrawable(null);
			callLinearLayout.setBackgroundDrawable(null);
			recordLinearLayout.setBackgroundDrawable(null);
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == UPDATE_SUCCESS) {
			btContacts.performClick();
		} else if (resultCode == SESSION_ERR) {
			finish();
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
