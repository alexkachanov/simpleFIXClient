package com.kachanov.simplefixclient.model;

import java.util.Map;

import quickfix.FieldNotFound;
import quickfix.Message;

public interface Validator {
	boolean validate( final Map<?, ?> m, final Message arrivedMessage ) throws FieldNotFound, Exception;
}