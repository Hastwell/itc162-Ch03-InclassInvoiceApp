package com.murach.invoice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.NumberFormat;

public class InvoiceTotalActivity extends Activity
implements TextView.OnEditorActionListener, TextWatcher
{

	private EditText subTotalEditText;
	private TextView discountPercentTextView;
	private TextView discountAmountTextView;
	private TextView totalTextView;

	private String subtotalString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoice_total);

		this.subTotalEditText = (EditText) this.findViewById(R.id.subtotalEditText);
		this.discountPercentTextView = (TextView) this.findViewById(R.id.discountPercentTextView);
		this.discountAmountTextView = (TextView) this.findViewById(R.id.discountAmountTextView);
		this.totalTextView = (TextView) this.findViewById(R.id.totalTextView);

		this.subTotalEditText.setOnEditorActionListener(this);
		this.subTotalEditText.addTextChangedListener(this);
	}

	private void updateTotalView()
	{
		// Parse subtotal amount
		this.subtotalString = subTotalEditText.getText().toString();

		float subtotal = subtotalString.equals("") ? 0.0f : Float.parseFloat(this.subtotalString);

		// Determine discount percentage
		float discountPercent;

		if(subtotal >= 200)
		{
			discountPercent = 0.2f;
		}
		else if(subtotal >= 100)
		{
			discountPercent = 0.1f;
		}
		else
		{
			discountPercent = 0.0f;
		}

		// Determine discount amount
		float discountAmount = subtotal * discountPercent;
		float total = subtotal - discountAmount;

		NumberFormat percentFormatter = NumberFormat.getPercentInstance();
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();

		this.discountPercentTextView.setText(percentFormatter.format(discountPercent));
		this.discountAmountTextView.setText(currencyFormatter.format(discountAmount));
		this.totalTextView.setText(currencyFormatter.format(total));


	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
		{
			this.updateTotalView();
		}

		return false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// don't care; ignore
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		this.updateTotalView();
	}

	@Override
	public void afterTextChanged(Editable s) {
		// don't care; ignore
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.subTotalEditText.setText(this.getSharedPreferences("SavedValues", MODE_PRIVATE).getString("subtotalValue", ""));
		this.updateTotalView();
	}

	@Override
	protected void onPause() {

		SharedPreferences.Editor editor = this.getSharedPreferences("SavedValues", MODE_PRIVATE).edit();
		editor.putString("subtotalValue", this.subTotalEditText.getText().toString());
		editor.commit();

		super.onPause();
	}
}