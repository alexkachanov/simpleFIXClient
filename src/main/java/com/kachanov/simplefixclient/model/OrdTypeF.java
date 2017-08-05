package com.kachanov.simplefixclient.model;

import static quickfix.field.OrdType.LIMIT;
import static quickfix.field.OrdType.MARKET;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.OrdType;

public enum OrdTypeF {
	  Limit(LIMIT)
	, Market(MARKET)
	;
	
	private char _value;

	OrdTypeF( char value ) {
		_value = value;
	}

	public char getValue() {
		return _value;
	}

	public static int getField() {
		return OrdType.FIELD;
	}

	public boolean validate( Message arrivedMessage ) throws FieldNotFound {
		return arrivedMessage.getChar( getField() ) == getValue();
	}

}