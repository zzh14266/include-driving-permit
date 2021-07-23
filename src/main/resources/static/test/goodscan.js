function goodscan(options) {
	var ws = null;
	var target = "ws://127.0.0.1:8880/";
	var that = this;
	this.options = {
		errorCallBack : null,
		successCallBack : null
	};
	for (i in options)
		this.options[i] = options[i];

	this.errorMessage = function(code, message) {
		if (this.options.errorCallBack) {
			this.options.errorCallBack(code, message)
		}
	};

	this.init = function() {
		if (!this.connect()) {
			console.log("can not connect " + target + ".")
		}
	}

	this.connect = function() {
		if (ws == null) {
			if ('WebSocket' in window) {
				ws = new WebSocket(target);
			} else if ('MozWebSocket' in window) {
				ws = new MozWebSocket(target);
			} else {
				this.errorMessage(-1,
						'浏览器不支持.')
				return false;
			}
			ws.onopen = function() {
			};
			ws.onmessage = function(event) {
				that.onMessage(event)
			};
			ws.onclose = function(event) {
				ws = null;
				if (event.code !== 1000) {
					that.errorMessage(-2, "与GoodScan测量程序连接中断");
				}
			};
			return true;
		}
	};
	this.goodscanMeasure = function(code) {
		this.send({
			"command" : "measure",
			"uuid" : code
		});
	}
	this.send = function(data) {
		if (ws == null) {
			if (!this.connect()) {
				return;
			}
			setTimeout(function(){
				var s = JSON.stringify(data);
				ws.send(s);
			},1000)
			return;
		}
		var s = JSON.stringify(data);
		ws.send(s);
	};
	this.onMessage = function(event) {
		var m = JSON.parse(event.data);
			if (m.errorCode == 0) {
				if (this.options.successCallBack) {
				this.options.successCallBack(m.uuid, m.L, m.W, m.H,m.weight,m.picture);
			} else {
				this.errorMessage(-3, m.errorString)
			}
		}
	};
	this.disconnect = function() {
		if (ws != null) {
			ws.close();
			ws = null;
		}
	};
	this.init();
}

