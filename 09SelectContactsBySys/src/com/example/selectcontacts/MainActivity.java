package com.example.selectcontacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	protected static final int PICK_CONTACT = 0;
	private Button btn;
	private TextView tv_name;
	private TextView tv_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.button1);
		tv_name = (TextView) findViewById(R.id.textView2);
		tv_phone = (TextView) findViewById(R.id.textView1);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("vnd.android.cursor.item/phone");
				startActivityForResult(intent, PICK_CONTACT);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PICK_CONTACT:
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor cursor = managedQuery(contactData, null, null, null,
						null);
				if (cursor.moveToFirst()) {
					String contactId = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts._ID));

					String name = cursor
							.getString(cursor
									.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

					String phoneNumber = "初始显示未输入号码";

					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);

					if (phones.moveToFirst()) {
						phoneNumber = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					phones.close();
					tv_name.setText(name);
					tv_phone.setText(phoneNumber);
				}
				cursor.close();
			}
			break;
		}
	}
}
