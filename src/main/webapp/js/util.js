function invokeServerMethod() {

	// using wff utf-8 encoder which is cross browser
	var stringBytes = wffGlobal.encoder.encode("こんにちは webfirmframework");
	//if the bytes are some row bytes like bytes from some binary file
	//then use Int8Array
	var int8Array = new Int8Array(stringBytes);
	
	// log to see the object type
	// console.log('object type', Object.prototype.toString
	// 		.call(stringBytes));
	
	// log to see the bytes of string
	// console.log(stringBytes);
	
	var argument = {
		'nullArray' : [null, null],
		'undefinedArray' : [undefined, undefined],
		'nullVal' : null,
		'undefinedVal' : undefined,
		'somekey' : 'some value ',
		'string' : 'string value こんにちは',
		'numb' : 55555,
		'bool' : true,
		'regex' : /ab+c/,
		'anObj' : {
			'key' : 'val'
		},
		'byteArray' : int8Array,
		'funcArray' : function() {console.log('m function');},
		'numberArray' : [ 5, 55, 555, 55, 5555 ]
	};

	wffAsync.serverMethod('testServerMethod', argument).invoke(
			function(obj) {

				console.log('callback obj ', obj);

				for (key in obj) {
					console.log('key is ' + key + ' ', obj[key]);
				}
				
				obj.testFun('check out browser console to see values received from server');

				console.log('callback byteArray row bytes', obj.byteArray);

				// using wff utf-8 decoder which is cross browser
				console.log('callback byteArray as string type'
						+ wffGlobal.decoder.decode(obj.byteArray));
				
			});
};
