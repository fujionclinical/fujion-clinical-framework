'use strict';

define('fcf-login', ['jquery', 'lodash', 'fcf-login-css', 'bootstrap-css'], function($) {
	return {
	
		init: function(timeout, logoutUrl, required, disabled) {
			this._timeout = timeout;
			this._logoutUrl = logoutUrl;
			this._required = required;
			this._logo = $('#fcf-title-img').attr('src');
			this._infoTitle = $('#fcf-info-title').text();
			this._infoContent = $('#fcf-info-content').text();
			var body$ = $('body');
			
			body$.on('click keydown', this.resetTimeout.bind(this));
			$('#fcf-form').on('submit', this.submitHandler.bind(this));
			$('#fcf-domain').on('change', this.domainHandler.bind(this));
			$('#fcf-alternate').one('click', this.alternateHandler.bind(this));
			this.domainHandler();
			this.resetTimeout();
			
			if (disabled) {
				$('#fcf-login-root').addClass('fcf-login-disabled');
				$('#fcf-error').text(disabled);
			}
			
			body$.show();
		},
		
		resetTimeout: function() {
			var self = this;
			
			if (this._timer) {
				clearTimeout(this._timer);
			}
			
			this._timer = setTimeout(function() {
				$(location).attr('href', self._logoutUrl);				
			}, this._timeout);
		},
		
		submitHandler: function(event) {
			var username = this.validateInput('#fcf-username', event),
				password = this.validateInput('#fcf-password', event);
				
			if (!username || !password) {
				return;
			}
			
			$('#fcf-submit').prop('disabled', true);
			var domain = $('#fcf-domain').val();
			username = domain ? domain + '\\' + username : username;
			$('#fcf-username-real').val(username);
		},
		
		domainHandler: function() {
			var domain = $('#fcf-domain option:selected'),
				logo = domain.attr('data-logo') || this._logo,
				info = domain.attr('data-info') || this._infoContent,
				header = domain.attr('data-header') || this._infoTitle;
			
			$('#fcf-title-img').attr('src', logo);
			$('#fcf-domain-name').text(domain.text());
			$('#fcf-info-title').text(header);
			$('#fcf-info-content').text(info);
		},
		
		alternateHandler: function(event) {
			$(event.target).parent().hide();
			$('#fcf-domains').show();
		},
		
		validateInput: function(id, event) {
			if (event.isDefaultPrevented()) {
				return;
			}
			
			var input = $(id),
				value = input.val();
			
			if (!value) {
				event.preventDefault();
				this.showError(this._required);
				input.focus();
			}
			
			return value;
		},
		
		showError: function(message) {
			$('#fcf-error').text(message);
		}
	};
});
