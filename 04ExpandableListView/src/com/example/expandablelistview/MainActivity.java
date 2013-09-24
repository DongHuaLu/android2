package com.example.expandablelistview;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.LinearGradient;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ExpandableListAdapter adapter = new BaseExpandableListAdapter() {

			private String gropu[] = { "组1", "组2", "组3" };

			private String items[][] = { { "组1-1", "组1-2" },
					{ "组2-1", "组2-2", "组2-3" },
					{ "组3-1", "组3-2", "组3-3", "组3-4" } };

			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				return true;
			}

			@Override
			public boolean hasStableIds() {
				return true;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				LinearLayout ll = new LinearLayout(MainActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				TextView textView = getTextView();
				textView.setText(getGroup(groupPosition).toString());
				ll.addView(textView);
				return ll;
			}

			@Override
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}

			@Override
			public int getGroupCount() {
				return gropu.length;
			}

			@Override
			public Object getGroup(int groupPosition) {
				return gropu[groupPosition];
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				return items[groupPosition].length;
			}

			private TextView getTextView() {
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, 64);
				TextView textView = new TextView(MainActivity.this);
				textView.setLayoutParams(lp);
				textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				textView.setPadding(36, 0, 0, 0);
				textView.setTextSize(20);
				return textView;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				TextView textView = getTextView();
				textView.setText(getChild(groupPosition, childPosition)
						.toString());
				return textView;
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return childPosition;
			}

			// 获取groupPosition,childPosition位置的对象
			@Override
			public Object getChild(int groupPosition, int childPosition) {
				return items[groupPosition][childPosition];
			}
		};

		ExpandableListView elv = (ExpandableListView) findViewById(R.id.elv);
		elv.setAdapter(adapter);
	}
}
