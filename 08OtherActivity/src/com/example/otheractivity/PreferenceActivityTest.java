/**
 * 
 */
package com.example.otheractivity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferenceActivityTest extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
