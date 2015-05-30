package com.example;

import android.app.ListActivity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity implements Loader.OnLoadCompleteListener<Cursor> {

	private Cursor employees;
	private MyDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = new MyDatabase(this);
		EmployeeTask task = new EmployeeTask(this);
		task.registerListener(0, this);
		task.forceLoad();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		employees.close();
		db.close();
	}

	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
		employees = data;

		ListAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1,
				employees,
				new String[]{"FirstName"},
				new int[]{android.R.id.text1});

		getListView().setAdapter(adapter);
	}

	private class EmployeeTask extends AsyncTaskLoader<Cursor> {
		public EmployeeTask(Context context) {
			super(context);
		}

		@Override
		public Cursor loadInBackground() {
			db.forceUpgradeFromNetwork("http://static.starboardland.com/northwind-network.db");
			return db.getEmployees();
		}
	}
}