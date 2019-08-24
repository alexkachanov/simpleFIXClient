package com.kachanov.simplefixclient.model;

import quickfix.field.CustomerOrFirm;
import quickfix.field.HandlInst;
import quickfix.field.ManualOrderIndicator;
import quickfix.field.MaturityMonthYear;
import quickfix.field.OrderCapacity;
import quickfix.field.SecuritySettlAgentAcctNum;
import quickfix.field.SenderLocationID;

public enum CustomF {
	
	  tag178(SecuritySettlAgentAcctNum.FIELD)
	, tag142(SenderLocationID.FIELD)
	, tag1028(ManualOrderIndicator.FIELD)
	, tag200(MaturityMonthYear.FIELD)
	, tag204(CustomerOrFirm.FIELD)
	, tag21(HandlInst.FIELD)
	, ordCapacity(OrderCapacity.FIELD)
	;
	
	private int _value;
	
	private CustomF(int value ){
		_value = value;
	}
	
	public int getValue(){
		return _value;
	}
	
}