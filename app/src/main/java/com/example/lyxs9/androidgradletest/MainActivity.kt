package com.example.lyxs9.androidgradletest

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.lyxs9.androidgradletest.R.id.fab
import com.example.lyxs9.androidgradletest.R.id.toolbar

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	val TAG = "tag"
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)


		fab.setOnClickListener { view ->
			Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show()

			Log.i(TAG,"8888")

		}
	}

}
