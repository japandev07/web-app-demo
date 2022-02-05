
const wffGlobalListeners = new function() {

    this.onSetURI = function(event) {
        console.log('wffGlobalListeners > onSetURI', event);
        loadingIcon.hidden = false;
    };
    this.afterSetURI = function(event) {
        console.log('wffGlobalListeners > afterSetURI', event);
        loadingIcon.hidden = true;
    };

};

