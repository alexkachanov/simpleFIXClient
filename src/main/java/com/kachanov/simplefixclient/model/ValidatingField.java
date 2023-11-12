package com.kachanov.simplefixclient.model;

import quickfix.FieldNotFound;
import quickfix.Message;

public interface ValidatingField {
	boolean validate( final Message arrivedMessage ) throws FieldNotFound, Exception;
}