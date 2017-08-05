package com.kachanov.simplefixclient.model;

import java.util.Map;

import quickfix.FieldNotFound;
import quickfix.Message;

public interface Validator {
	boolean validate( Map<?, ?> m, Message arrivedMessage ) throws FieldNotFound, Exception;
}