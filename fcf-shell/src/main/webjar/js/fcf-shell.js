'use strict';

define('fcf-shell', ['fcf-core', 'fujion-core', 'fcf-shell-css'], function(fcf, fujion) { 
	
	fcf.widget = fcf.widget || {};
	
	/**
	 * Widgets for shell and extended shell.
	 */
	fcf.widget.Shell = fujion.widget.Div.extend({});
	
	fcf.widget.ShellEx = fcf.widget.Shell.extend({});
	
	return fcf.widget;
});