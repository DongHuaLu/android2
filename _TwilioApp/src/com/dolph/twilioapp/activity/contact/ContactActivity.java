package com.dolph.twilioapp.activity.contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dolph.twilioapp.R;
import com.dolph.twilioapp.model.Contact;
import com.dolph.twilioapp.twilio.CallPhoneService;

public class ContactActivity extends Activity {
	private static final int UPDATE_SUCCESS = 2;
	private TextView contactName;
	private TextView contactNumber;
	private TextView contactAddress;
	private Contact contact;
	private CallPhoneService twilioService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		contactName = (TextView) findViewById(R.id.ContactName);
		contactNumber = (TextView) findViewById(R.id.ContactNumber);
		contactAddress = (TextView) findViewById(R.id.ContactAddress);
		Intent dataIntent = getIntent();
		Bundle data = dataIntent.getExtras();
		contact = (Contact) data.getSerializable("contact");
		contactName.setText(contact.getName());
		contactNumber.setText(contact.getNumber());
		contactAddress.setText(contact.getAddress());
		twilioService = CallPhoneService.getCallPhoneService(this.getApplicationContext());
	}

	public void modifyContact(View view) {
		Intent intent = new Intent(this, NewContactActivity.class);
		intent.putExtra("contact", contact);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == UPDATE_SUCCESS) {
			contactName.setText(data.getStringExtra("contactName"));
			contactNumber.setText(data.getStringExtra("contactNumber"));
			contactAddress.setText(data.getStringExtra("address"));
		}
	}

	public void call(View view) {
		String number = contactNumber.getText().toString();
		// if (Utils.validatePhone(number)) {
		if (true) {
			try {
				twilioService.connect(number);
				new AlertDialog.Builder(this).setTitle("拨打中").setMessage("正在拨打" + number).setNegativeButton("挂断", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						twilioService.disconnect();
					}
				}).create().show();
			} catch (Exception e) {
				new AlertDialog.Builder(this).setTitle("拨打失败").setNegativeButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create().show();
				e.printStackTrace();
			}
		} else {
		}
	}
}
