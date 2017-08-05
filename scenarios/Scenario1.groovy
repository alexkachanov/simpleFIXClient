connection.send nos, symbol: 'YTM7', secType: 'FUT', side: 'Buy', qty: 2, price: 0, tif: 'GTC', ordType: 'Market', exDest:'SFE', ordCapacity:'A', account:'12345'
connection.expect ack
connection.expect fill


//connection.send amend, qty: 1
//connection.send amend, qty: 10
//connection.send amend, price: 10.3
//connection.send amend, tif: 'Day'

//connection.send cancel


