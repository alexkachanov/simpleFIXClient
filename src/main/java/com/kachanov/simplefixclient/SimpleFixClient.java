package com.kachanov.simplefixclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kachanov.simplefixclient.model.MessageF;

import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.examples.banzai.BanzaiApplication;
import quickfix.examples.banzai.ExecutionTableModel;
import quickfix.examples.banzai.OrderTableModel;

public class SimpleFixClient extends BanzaiApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger( SimpleFixClient.class );

	private static final String DEFINITIONS_FILE = "definitions.dsl";
	private static final String GROOVY_ENGINE_NAME = "groovy";
	private static final String CONFIG_FILE_NAME = "simplefixclient.cfg";

	private static Session _session = null;
	private static Connection _connection = null;
	private static ScriptEngine _groovyEngine = null;

	public SimpleFixClient( OrderTableModel orderTableModel, ExecutionTableModel executionTableModel ) {
		super( orderTableModel, executionTableModel );
	}

	public static void main( String[] args ) throws InterruptedException, IOException {
		LOGGER.info( "Starting up ..." );

		if (!Paths.get( CONFIG_FILE_NAME ).toFile().exists()) {
			LOGGER.error( "config file does not exist: " + CONFIG_FILE_NAME );
			return;
		}

		if (args == null || args.length > 1) {
			LOGGER.error( "only one parameter is allowed: script name" );
			return;
		}

		String scriptName = args[0].trim();
		if (scriptName.isEmpty()) {
			LOGGER.error( "script name is not valid" );
			return;
		}
		
		LOGGER.info( "script name: "  + scriptName);

		final SimpleFixClient app = new SimpleFixClient(new OrderTableModel(), new ExecutionTableModel());
		SocketInitiator initiator = null;
		try {
			SessionSettings sessionSettings = new SessionSettings( new FileInputStream( CONFIG_FILE_NAME ) );

			MessageStoreFactory storeFactory = new FileStoreFactory( sessionSettings );
			FileLogFactory logFactory = new FileLogFactory( sessionSettings );
			MessageFactory messageFactory = new DefaultMessageFactory();
			initiator = new SocketInitiator( app, storeFactory, sessionSettings, logFactory, messageFactory );
			initiator.start();
			
			final ScriptEngineManager factory = new ScriptEngineManager();
			_groovyEngine = factory.getEngineByName( GROOVY_ENGINE_NAME );

			Thread.sleep( 6000 );

			loadDSLDefinitions();

			LOGGER.info( "searching for session declaration "+scriptName + " in config file " + CONFIG_FILE_NAME );
			for ( SessionID sessionId : initiator.getSessions() ) {
				
				String qualifier = sessionId.getSessionQualifier();
				
				if (!scriptName.equals( qualifier )) {
					continue;
				}

				LOGGER.info( "found: " + sessionId.toString() + " in config file " + CONFIG_FILE_NAME );
				
				_session = Session.lookupSession( sessionId );
				_session.setRejectInvalidMessage( false );

				_connection = new Connection( _session );
				
				LOGGER.info( "Session tries to log on" );
				_session.logon();

				if (_session.isLoggedOn()) {

					final File scenarioFile = new File( "scenarios/" + qualifier + ".groovy" );

					try (final FileReader fileReader = new FileReader( scenarioFile )) {
						LOGGER.info( "Session is logged on" );

						Thread.sleep( 5_000 );

						final Bindings bindings = new SimpleBindings();
						bindings.put( "connection", _connection );
						for ( MessageF msgType : MessageF.values() ) {
							bindings.put( msgType.name(), msgType );
						}

						LOGGER.info( "executing script for " + qualifier );

						_groovyEngine.setBindings( bindings, ScriptContext.ENGINE_SCOPE );
						_groovyEngine.eval( fileReader );

					} finally {
						_session.logout();
					}

				} else {
					LOGGER.info( "Session was not able to logged on" );
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ConfigError e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} finally {
			if (initiator != null) {
				initiator.stop();
			}

			LOGGER.info( "Finished ..." );

		}
	}

	@Override
	public void fromApp( final Message message, final SessionID sessionID ) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		// LOGGER.info( message.toString().replaceAll( "", "; " ) );
		if (_connection == null) {
			// connection instance is not yet initialized but counterparty is
			// already sending messages to us
			LOGGER.info( "connection is not yet initialized" );
		} else {
			_connection.addResponse( message );
		}
	}

	private static void loadDSLDefinitions() throws IOException, ScriptException {
		LOGGER.info( "reading definitions from " + DEFINITIONS_FILE );
		final List<String> lines = Files.readAllLines( Paths.get( DEFINITIONS_FILE ) );
		_groovyEngine.eval( String.join( "\n", lines ) );
	}

}
