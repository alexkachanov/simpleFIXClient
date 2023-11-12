package com.kachanov.simplefixclient.model;

import quickfix.field.Currency;
import quickfix.field.*;
import quickfix.field.HandlInst;
import quickfix.field.ManualOrderIndicator;
import quickfix.field.MaturityMonthYear;
import quickfix.field.OrderCapacity;
import quickfix.field.SecurityExchange;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.field.SecuritySettlAgentAcctNum;
import quickfix.field.SecurityType;
import quickfix.field.SenderLocationID;

/** 
 * Enumeration of custom FIX Protocol fields. 
 * The tags can be either supported by FIX Protocol or your Custom tags.
 */
public enum CustomF {

	// Named Standard FIX tags supported by QuickFIX
	  ordCapacity(OrderCapacity.FIELD)
    , secType( SecurityType.FIELD)
	
	// Standard FIX tags supported by QuickFIX
	, tag178(SecuritySettlAgentAcctNum.FIELD)
	, tag142(SenderLocationID.FIELD)
	, tag1028(ManualOrderIndicator.FIELD)
	, tag200(MaturityMonthYear.FIELD)
	, tag204(CustomerOrFirm.FIELD)
	, tag21(HandlInst.FIELD)
	
	, tag22(SecurityIDSource.FIELD)
	, tag48(SecurityID.FIELD)
	, tag15(Currency.FIELD)
	, tag207(SecurityExchange.FIELD)
	
	// your custom tags that are outside FIX Protocol
	, tag1111(1111)
	;
	
	private int _value;
	
	private CustomF(int value ){
		_value = value;
	}
	
	public int getValue(){
		return _value;
	}
	
}