/*
 * Fujion Clinical Framework JavaScript Library
 */
define('fcf-core', ['fujion-core', 'jquery', 'fcf-core-css'], function(fujion, $) { 

var fcf = {

	/**
	 * Fire a local event at the server.
	 *
	 * @param eventName The event name.
	 * @param eventData The event data.
	 */
	fireLocalEvent: function(eventName, eventData) {
		fcf.fireEvent(eventName, eventData, true);
	},
	
	/**
	 * Fire a remote event at the server.
	 *
	 * @param eventName The event name.
	 * @param eventData The event data.
	 */
	fireRemoteEvent: function(eventName, eventData) {
		fcf.fireEvent(eventName, eventData, false);
	},
	
	/**
	 * Fire a local or remote event at the server.
	 *
	 * @param eventName The event name.
	 * @param eventData The event data.
	 * @param asLocal If true, fire as local event; otherwise as remote.
	 */
	fireEvent: function(eventName, eventData, asLocal) {
		var params = {
			eventName: eventName,
			eventData: eventData,
			asLocal: asLocal
		};
	
		fujion.event.sendToServer($.Event('genericEvent', params));
	},
	
	/**
	 * Simple stopwatch.
	 */
	stopwatch: function(tag, evt, fnc) {
		this.tag = tag || '';
		this.evt = evt || 'STATUS.TIMING';
		this.fnc = fnc || fcf.stopwatch.format;
	}
}

fcf.stopwatch.prototype.start = function() {
	this.begin = new Date();
	this.elapsed = null;
},

fcf.stopwatch.prototype.stop = function() {
	this.elapsed = new Date() - this.begin;
	fcf.fireLocalEvent(this.evt, this.fnc(this));
},

fcf.stopwatch.format = function(sw) {
	var tm = sw.elapsed;
	var units = 'ms';

	if (tm >= 1000) {
		tm /= 1000;
		units = 's';

		if (tm >= 60) {
			tm /= 60;
			units = 'm';

			if (tm >= 60) {
				tm /= 60;
				units = 'h';
			}
		}
	}

	return sw.tag + tm + ' ' + units;
};

return fcf;

});
