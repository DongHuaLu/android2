package com.dolph.twilioapp.activity.contact;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dolph.twilioapp.activity.main.NavigationActivity.RefreshListener;

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

	public static class ContactListFragment extends ListFragment implements
			RefreshListener {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			
			return super.onCreateView(inflater, container, savedInstanceState);
		}

		@Override
		public void refreshView() {

		}

		@Override
		public void forceRefreshView() {

		}

	}

}
