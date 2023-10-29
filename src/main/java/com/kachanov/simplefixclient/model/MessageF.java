package com.kachanov.simplefixclient.model;

import static quickfix.field.MsgType.EXECUTION_REPORT;
import static quickfix.field.MsgType.ORDER_CANCEL_REPLACE_REQUEST;
import static quickfix.field.MsgType.ORDER_CANCEL_REQUEST;
import static quickfix.field.MsgType.ORDER_SINGLE;
import static quickfix.field.OrdStatus.CANCELED;
import static quickfix.field.OrdStatus.FILLED;
import static quickfix.field.OrdStatus.NEW;
import static quickfix.field.OrdStatus.PARTIALLY_FILLED;
import static quickfix.field.OrdStatus.REJECTED;
import static quickfix.field.OrdStatus.REPLACED;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import quickfix.field.Text;

public enum MessageF implements Validator {

	  nos(ORDER_SINGLE)
	, amend(ORDER_CANCEL_REPLACE_REQUEST)
	, cancel(ORDER_CANCEL_REQUEST)

	, ack(EXECUTION_REPORT, NEW)
	, amended(EXECUTION_REPORT, REPLACED)
	, canceled(EXECUTION_REPORT, CANCELED)
	, rejected(EXECUTION_REPORT, REJECTED)
	, pfill(EXECUTION_REPORT, PARTIALLY_FILLED)
	, fill(EXECUTION_REPORT, FILLED)
	;

	private static final Logger LOGGER = LoggerFactory.getLogger( MessageF.class );

	private String _value;
	private char _ordStatus;

	private MessageF( String value ) {
		_value = value;
	}

	private MessageF( final String value, final char orderStatus ) {
		_value = value;
		_ordStatus = orderStatus;
	}

	public int getField() {
		return MsgType.FIELD;
	}

	public String getValue() {
		return _value;
	}

	public char getOrdStatus() {
		return _ordStatus;
	}

	@Override
	public boolean validate( final Map<?, ?> m, Message arrivedMessage ) throws FieldNotFound, Exception {
		if (isReject( arrivedMessage )) {
			LOGGER.info( "expected smth but received reject" );
			throw new Exception();
		}

		if (arrivedMessage.getChar( OrdStatus.FIELD ) != getOrdStatus()) throw new Exception();
		boolean rv = true;
		for ( Entry<?, ?> entry : m.entrySet() ) {
			LOGGER.debug( "entry.getKey(): " + entry.getKey() );
			final String key = (String) entry.getKey();
			final String value = (String) entry.getValue();
			switch (key) {
				case "side": 	rv = SideF.valueOf( value ).validate(arrivedMessage); break;
				case "tif": 	rv = TimeInForceF.valueOf( value ).validate(arrivedMessage); break;
				case "ordType": rv = OrdTypeF.valueOf( value ).validate(arrivedMessage); break;
				case "tag58": 	rv = arrivedMessage.getString( Text.FIELD ).equals( m.get( "tag58" ) ); break;
			}
		}

		return rv;
	}

	private static boolean isReject( final Message arrivedMessage ) throws FieldNotFound {
		return arrivedMessage.getChar( OrdStatus.FIELD ) == OrdStatus.REJECTED;
	}
}