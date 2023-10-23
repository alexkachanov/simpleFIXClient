// FIX 4.2 specification
// these are default values of main tags of FIX messages sent by SimpleFIXClient 

connection.defaults symbol: 	'IBM' 
connection.defaults secType: 	'FUT' 
connection.defaults side: 		'Buy'
connection.defaults exDest:		'CME'
connection.defaults account:	'12345'
connection.defaults tag22:		'5', tag48: 'IBM.T', tag58:	'testMessage'
connection.defaults tag21:		1

// sends NOSes, waits 2 seconds after each for an ACK and Fill and then disconnects
for(x in 1..2) {
	connection.send nos, qty: x, price: 0, tif: 'GTC', ordType: 'Market'
	
	sleep 2.seconds
	
	connection.expect ack
	connection.expect fill
}

for(x in 1..2) {
	connection.send nos, side: 'Sell', qty: x, price: 0, ordType: 'Market'
	
	sleep 2.seconds
	
	connection.expect ack
	connection.expect fill
}

//connection.send amend, qty: 1
//connection.send amend, qty: 10
//connection.send amend, price: 10.3
//connection.send amend, tif: 'Day'

//connection.send cancel


