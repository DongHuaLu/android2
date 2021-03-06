package com.dolph.twilioapp;

import android.content.Context;

public class AppValues {
	private Context mContext;
	private AppSettings setting;

	public AppValues(Context context) {
		this.mContext = context;
		setting = new AppSettings(context);
	}

	private static int currentUserId;
	private static String currentUserName;
	private static String currentPhoneNumber;
	private static String currentPassword;
	private static boolean logouted = false;
	private static String deviceId;
	private static String serverPath;
	private static boolean rememberMe;
	private static boolean autoLogin;

	public String getServerPath() {
		if (serverPath == null || "".equals(serverPath)) {
			serverPath = setting.getSettingString("serverPath", "");
		}
		return serverPath;
	}

	public void setServerPath(String deviceId) {
		setting.setSettingsString("serverPath", deviceId);
		AppValues.serverPath = deviceId;
	}

	public String getDeviceId() {
		if (deviceId == null || "".equals(deviceId)) {
			deviceId = setting.getSettingString("deviceId", "");
		}
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		setting.setSettingsString("deviceId", deviceId);
		AppValues.deviceId = deviceId;
	}

	public String getCurrentPassword() {
		if (currentPassword == null || "".equals(currentPassword)) {
			currentPassword = setting.getSettingString("password", "");
		}
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		setting.setSettingsString("password", currentPassword);
		AppValues.currentPassword = currentPassword;
	}

	public String getCurrentUserName() {
		if (currentUserName == null || "".equals(currentUserName)) {
			currentUserName = setting.getSettingString("username", "");
		}
		return currentUserName;
	}

	public void setCurrentUserName(String currentUserName) {
		setting.setSettingsString("username", currentUserName);
		AppValues.currentUserName = currentUserName;
	}

	public String getCurrentPhoneNumber() {
		if (currentPhoneNumber == null || "".equals(currentPhoneNumber)) {
			currentPhoneNumber = setting.getSettingString("phonenumber", "");
		}
		return currentUserName;
	}

	public void setCurrentPhoneNumber(String currentPhoneNumber) {
		setting.setSettingsString("phonenumber", currentPhoneNumber);
		AppValues.currentPhoneNumber = currentPhoneNumber;
	}

	public int getCurrentUserId() {
		if (currentUserId == 0)
			currentUserId = setting.getSettingInt("user_id", 0);
		return currentUserId;
	}

	public void setCurrentUserId(int currentUserId) {
		setting.setSettingsInt("user_id", currentUserId);
		AppValues.currentUserId = currentUserId;
	}

	public boolean isLogouted() {
		logouted = setting.getSettingBoolean("logouted", false);
		return logouted;
	}

	public void setLogouted(boolean logouted) {
		setting.setSettingsBoolean("logouted", logouted);
	}

	public boolean isRememberMe() {
		rememberMe = setting.getSettingBoolean("rememberMe", false);
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		setting.setSettingsBoolean("rememberMe", rememberMe);
	}

	public boolean isAutoLogin() {
		autoLogin = setting.getSettingBoolean("autoLogin", false);
		return autoLogin;
	}

	public void setAutoLogin(boolean autoLogin) {
		setting.setSettingsBoolean("autoLogin", autoLogin);
	}

}
