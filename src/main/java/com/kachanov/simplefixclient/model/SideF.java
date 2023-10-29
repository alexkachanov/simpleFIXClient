package com.kachanov.simplefixclient.model;

import static quickfix.field.Side.BUY;
import static quickfix.field.Side.*;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.Side;

/**
 * Enumeration of supported Side tag54 
 */
public enum SideF {
	  Buy(BUY)
	, Sell(SELL)
	, BuyMinus (BUY_MINUS)
	, SellPlus(SELL_PLUS)
	, SellShort(SELL_SHORT)
	, SellShortExempt (SELL_SHORT_EXEMPT)
	, Undisclosed (UNDISCLOSED)
	, Cross (CROSS)
	, CrossShort (CROSS_SHORT)
	, CrossShortExempt(CROSS_SHORT_EXEMPT)
	;

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