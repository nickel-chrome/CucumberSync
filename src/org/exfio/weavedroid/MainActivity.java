/*******************************************************************************
 * Copyright (c) 2014 Gerry Healy <nickel_chrome@mac.com>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Based on DavDroid:
 *     Richard Hirner (bitfire web engineering)
 * 
 * Contributors:
 *     Gerry Healy <nickel_chrome@mac.com> - Initial implementation
 ******************************************************************************/
package org.exfio.weavedroid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.exfio.weavedroid.R;

import org.exfio.weavedroid.util.SystemUtils;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//DEBUG only
		if ( SystemUtils.isDebuggable(this) ) {
			org.exfio.weavedroid.util.Log.init("debug");
		}
		
		setContentView(R.layout.activity_main);
		
		TextView tvWorkaround = (TextView)findViewById(R.id.text_workaround);
		if (fromPlayStore()) {
			tvWorkaround.setVisibility(View.VISIBLE);
			tvWorkaround.setText(Html.fromHtml(getString(R.string.html_main_workaround)));
		    tvWorkaround.setMovementMethod(LinkMovementMethod.getInstance());
		}
		
		TextView tvInfo = (TextView)findViewById(R.id.text_info);
		tvInfo.setText(Html.fromHtml(getString(R.string.html_main_info, Constants.APP_VERSION)));
		tvInfo.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity, menu);
	    return true;
	}

	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void addAccount(MenuItem item) {
		Intent intent = new Intent(Settings.ACTION_ADD_ACCOUNT);
		
		//EXTRA_ACCOUNT_TYPES supported in JBMR2 and above
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			intent.putExtra(
				Settings.EXTRA_ACCOUNT_TYPES,
				new String[] {
					Constants.ACCOUNT_TYPE_LEGACYV5
					,Constants.ACCOUNT_TYPE_EXFIOPEER
					,Constants.ACCOUNT_TYPE_FXACCOUNT
				}
			);
	    }
		
		startActivity(intent);
	}

	public void showSyncSettings(MenuItem item) {
		Intent intent = new Intent(Settings.ACTION_SYNC_SETTINGS);
		startActivity(intent);
	}

	public void showWebsite(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(Constants.WEB_URL_HELP + "&pk_kwd=main-activity"));
		startActivity(intent);
	}
	
	
	private boolean fromPlayStore() {
		try {
			return "com.android.vending".equals(getPackageManager().getInstallerPackageName("org.exfio.weavedroid"));
		} catch(IllegalArgumentException e) {
		}
		return false;
	}
}
