package com.kachanov.simpleclient.model;

import static quickfix.field.Side.BUY;
import static quickfix.field.Side.SELL;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.Side;

public enum SideF {
	Buy(BUY), Sell(SELL);

	private char _value;

	SideF( char value ) {
		_value = value;
	}

	public static int getField() {
		return Side.FIELD;
	}

	public char getValue() {
		return _value;
	}

	public boolean validate( Message arrivedMessage ) throws FieldNotFound {
		return arrivedMessage.getChar( getField() ) == getValue();
	}
}