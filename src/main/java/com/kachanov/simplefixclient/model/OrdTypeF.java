package com.kachanov.simplefixclient.model;

import static quickfix.field.OrdType.LIMIT;
import static quickfix.field.OrdType.*;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.OrdType;

/**
 * Enumeration of supported values of OrdType tag40
 */
public enum OrdTypeF {
	  Market(MARKET) 
	, Limit(LIMIT)
	, Stop(STOP_STOP_LOSS)
	, Stop_limit(STOP_LIMIT)
	, Market_on_close(MARKET_ON_CLOSE)
	, With_or_without(WITH_OR_WITHOUT)
	, Limit_or_better(LIMIT_OR_BETTER)
	, Limit_with_or_without(LIMIT_WITH_OR_WITHOUT)
	, On_basis(ON_BASIS)
	, On_close(ON_CLOSE)
	, Limit_ob_close(LIMIT_ON_CLOSE)
	, Funari(FUNARI)
	, Pegged(PEGGED)
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

	public boolean validate( final Message arrivedMessage ) throws FieldNotFound {
		return arrivedMessage.getChar( getField() ) == getValue();
	}

}