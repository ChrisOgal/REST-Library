var soap = require('soap');
var url = 'http://localhost:8080/LoanService-1.0-SNAPSHOT/LoanService?wsdl';
//var args = {name: 'alan' , email: 'alan@email.com'};

var cors = require('cors')

 
const express = require('express')
const app = express()
const port = 3000

var bodyParser = require('body-parser');
app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: true }));

app.use(cors())

app.get('/', (function(req, res){
	args = {}
	for (var propName in req.query) {
    	if (req.query.hasOwnProperty(propName)) {
        	if(propName != 'opName'){
        		args[propName] = req.query[propName];
    		}
    	}
	}
	
	if(Object.keys(args).length == 0){
 		// Your code here if x has some properties  
		args = null;
	}	

	soap.createClient(url, function(err, client) {
    			if(client){ 		
 					console.log("created client successfully")
 					console.log("making SOAP request " + req.query.opName + "with params: " + JSON.stringify(args))
	 				client[req.query.opName](args , function(err, result){
	 					if(result.return != undefined){
	 						console.log("Opertaion: " + req.query.opName + " successfull! returned: " + result.return)
	 						console.log(result)
	 						res.type('text/plain')
	 						res.status(200).send(result.return)
	 					}
	 					else{
	 						console.log(err.root.Envelope.Body.Fault['faultstring'])
	 						console.log(err)
	 						res.type('tetx/plain')
	 						res.send(err.root.Envelope.Body.Fault['faultstring'])
	 					}
	 				})
 			}	
	});
}))
app.listen(port, () => console.log(`Example app listening at http://localhost:${port}`))

  