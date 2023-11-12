package com.kachanov.simplefixclient.model;

import static quickfix.field.TimeInForce.AT_THE_CLOSE;
import static quickfix.field.TimeInForce.AT_THE_OPENING;
import static quickfix.field.TimeInForce.DAY;
import static quickfix.field.TimeInForce.FILL_OR_KILL;
import static quickfix.field.TimeInForce.GOOD_TILL_CANCEL;
import static quickfix.field.TimeInForce.GOOD_TILL_CROSSING;
import static quickfix.field.TimeInForce.GOOD_TILL_DATE;
import static quickfix.field.TimeInForce.IMMEDIATE_OR_CANCEL;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.TimeInForce;

/**
 * Enumeration of supported TimeInForce tag59
 */
public enum TimeInForceF implements ValidatingField {
	  Day		   (DAY)
	, GTC		   (GOOD_TILL_CANCEL)
	, AtTheOpening (AT_THE_OPENING)
	, IOC		   (IMMEDIATE_OR_CANCEL)
	, FOK		   (FILL_OR_KILL)
	, GoodTillCrossing (GOOD_TILL_CROSSING)
	, GTD		   (GOOD_TILL_DATE)
	, AtTheClose   (AT_THE_CLOSE)
	;
	
	private char _value;

	TimeInForceF( char value ) {
		_value = value;
	}

	public static int getField() {
		return TimeInForce.FIELD;
	}

	public char getValue() {
		return _value;
	}

	@Override
	public boolean validate( final Message arrivedMessage ) throws FieldNotFound {
		return arrivedMessage.getChar( getField() ) == getValue();
	}
}