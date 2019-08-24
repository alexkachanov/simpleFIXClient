package com.kachanov.simplefixclient;

import static com.kachanov.simplefixclient.model.MessageF.ack;
import static com.kachanov.simplefixclient.model.MessageF.amend;
import static com.kachanov.simplefixclient.model.MessageF.amended;
import static com.kachanov.simplefixclient.model.MessageF.cancel;
import static com.kachanov.simplefixclient.model.MessageF.canceled;
import static com.kachanov.simplefixclient.model.MessageF.fill;
import static com.kachanov.simplefixclient.model.MessageF.nos;
import static com.kachanov.simplefixclient.model.MessageF.pfill;
import static com.kachanov.simplefixclient.model.MessageF.rejected;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kachanov.simplefixclient.model.CustomF;
import com.kachanov.simplefixclient.model.MessageF;
import com.kachanov.simplefixclient.model.OrdTypeF;
import com.kachanov.simplefixclient.model.SideF;
import com.kachanov.simplefixclient.model.TimeInForceF;

import quickfix.CharField;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.ExDestination;
import quickfix.field.HandlInst;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.SecurityType;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;

public class Connection {

	private static final Logger LOGGER = LoggerFactory.getLogger( Connection.class );

	private static final String tag11 = "tag11";
	private static final String tag41 = "tag41";

	private Map _context = new HashMap<>();
	private Map<String, LinkedBlockingQueue<Message>> _responses = new HashMap<String, LinkedBlockingQueue<Message>>();
	private quickfix.Session _session;
	

	public Connection( quickfix.Session session ) {
		this._session = session;
	}
	
	public void addResponse( Message message ) throws FieldNotFound {
		String clorderid = message.getString( ClOrdID.FIELD );
		LinkedBlockingQueue<Message> queue = _responses.get( clorderid );
		if (queue == null) {
			queue = new LinkedBlockingQueue<Message>();
			_responses.put( clorderid, queue );
		}
		queue.add( message );
	}
	
	public void reset(){
		_responses.clear();
	}
	
	public void defaults(Map<?,?> m) {
		_context.putAll( m );
	}

	void expect( MessageF expectedOrdStatus ) throws Exception {
		expect(Collections.EMPTY_MAP, expectedOrdStatus);
	}
	
	void expect( Map<?, ?> m, MessageF expectedMessage ) throws Exception {
		LOGGER.info( "\nreceiving " + expectedMessage );

		String clorderid = (String) m.get( tag11 );
		if (clorderid == null) clorderid = (String) _context.get( tag11 );

		Message arrivedMessage = _responses.get( clorderid ).poll( 20, TimeUnit.SECONDS );
		LOGGER.info( arrivedMessage.toString().replaceAll( "", "; " ) );

		char ordStatus = arrivedMessage.getChar( OrdStatus.FIELD );

		boolean rv = false;
		if (!arrivedMessage.getHeader().getString( MsgType.FIELD ).equals( MsgType.EXECUTION_REPORT )) throw new Exception();

		for ( MessageF element : Arrays.asList( ack, fill, pfill, amended, canceled, rejected ) ) {
			if (ordStatus == element.getOrdStatus() && expectedMessage == element) {
				rv = expectedMessage.validate( m, arrivedMessage );
			}
		}

		if (!rv) throw new Exception( "message " + expectedMessage + " is not valid" );
	}
	
	void send( MessageF messageType ) throws SessionNotFound {
		send( Collections.EMPTY_MAP, messageType );
	}

	void send( Map<?, ?> m, MessageF messageType ) throws SessionNotFound {
		LOGGER.info( "\nsending " + messageType +
				": symbol: "+m.get( "symbol" )+
				": secType: "+m.get( "secType" )+
				"; side: "+m.get( "side" )+
				"; ordType: "+m.get( "ordType" )+
				"; tif: "+m.get( "tif" )+
				"; qty: "+m.get( "qty" )+
				"; price: " + m.get( "price" )
				
				);
		
		_context.putAll( m );
		
		Message message = new Message();
		message.getHeader().setField( new StringField( messageType.getField(), messageType.getValue() ) );
		
		if (messageType != nos) {
			message.setField( new StringField( OrigClOrdID.FIELD, String.valueOf( _context.get( tag41 ) ) ) );
		}
		
		String clorderid = (String) m.get( tag11 );
		if (clorderid == null) clorderid = String.valueOf( System.nanoTime() );
		message.setField( new StringField( ClOrdID.FIELD, clorderid ) );
		if (messageType == nos || messageType == amend || messageType == cancel) {
			_context.put( tag11, clorderid );
			_context.put( tag41, clorderid );
			_responses.put( clorderid, new LinkedBlockingQueue<Message>() );
		}
		
		message.setField( new CharField( OrdTypeF.getField(), 	OrdTypeF.valueOf( String.valueOf( _context.get( "ordType" ) ) ).getValue() ));
		message.setField( new CharField( TimeInForceF.getField(), TimeInForceF.valueOf( String.valueOf( _context.get( "tif" ) ) ).getValue() ) );
		message.setField( new CharField( SideF.getField(), 		SideF.valueOf( String.valueOf( _context.get( "side" ) ) ).getValue() ) );
		
		message.setField( new StringField( OrderQty.FIELD, String.valueOf( _context.get( "qty" ) ) ));
		message.setField( new StringField( Price.FIELD, String.valueOf( _context.get( "price" ) ) ));
		
		message.setField( new StringField( Symbol.FIELD, String.valueOf( _context.get( "symbol" ) ) ));
		message.setField( new StringField( SecurityType.FIELD, String.valueOf( _context.get( "secType" ) ) ));
		message.setField( new StringField( ExDestination.FIELD, String.valueOf( _context.get( "exDest" ) ) ));
		message.setField( new StringField( Account.FIELD, String.valueOf( _context.get( "account" ) ) ));
		
		// ...... exchange specific and custom tags
		for ( CustomF element : CustomF.values() ) {
			if (_context.get( element.name() ) != null) {
				message.setField( new StringField( element.getValue(), String.valueOf( _context.get( element.name() ) ) ) );
			}
		}

		message.setField( new TransactTime() );

		LOGGER.info( message.toString().replaceAll( "", "; " ) );
		Session.sendToTarget( message, _session.getSessionID() );

	}

}