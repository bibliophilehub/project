/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// identity function for calling harmony imports with the correct context
/******/ 	__webpack_require__.i = function(value) { return value; };
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "./";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 39);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var bind = __webpack_require__(8);
var isBuffer = __webpack_require__(44);

/*global toString:true*/

// utils is a library of generic helper functions non-specific to axios

var toString = Object.prototype.toString;

/**
 * Determine if a value is an Array
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is an Array, otherwise false
 */
function isArray(val) {
  return toString.call(val) === '[object Array]';
}

/**
 * Determine if a value is an ArrayBuffer
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is an ArrayBuffer, otherwise false
 */
function isArrayBuffer(val) {
  return toString.call(val) === '[object ArrayBuffer]';
}

/**
 * Determine if a value is a FormData
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is an FormData, otherwise false
 */
function isFormData(val) {
  return (typeof FormData !== 'undefined') && (val instanceof FormData);
}

/**
 * Determine if a value is a view on an ArrayBuffer
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a view on an ArrayBuffer, otherwise false
 */
function isArrayBufferView(val) {
  var result;
  if ((typeof ArrayBuffer !== 'undefined') && (ArrayBuffer.isView)) {
    result = ArrayBuffer.isView(val);
  } else {
    result = (val) && (val.buffer) && (val.buffer instanceof ArrayBuffer);
  }
  return result;
}

/**
 * Determine if a value is a String
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a String, otherwise false
 */
function isString(val) {
  return typeof val === 'string';
}

/**
 * Determine if a value is a Number
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a Number, otherwise false
 */
function isNumber(val) {
  return typeof val === 'number';
}

/**
 * Determine if a value is undefined
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if the value is undefined, otherwise false
 */
function isUndefined(val) {
  return typeof val === 'undefined';
}

/**
 * Determine if a value is an Object
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is an Object, otherwise false
 */
function isObject(val) {
  return val !== null && typeof val === 'object';
}

/**
 * Determine if a value is a Date
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a Date, otherwise false
 */
function isDate(val) {
  return toString.call(val) === '[object Date]';
}

/**
 * Determine if a value is a File
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a File, otherwise false
 */
function isFile(val) {
  return toString.call(val) === '[object File]';
}

/**
 * Determine if a value is a Blob
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a Blob, otherwise false
 */
function isBlob(val) {
  return toString.call(val) === '[object Blob]';
}

/**
 * Determine if a value is a Function
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a Function, otherwise false
 */
function isFunction(val) {
  return toString.call(val) === '[object Function]';
}

/**
 * Determine if a value is a Stream
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a Stream, otherwise false
 */
function isStream(val) {
  return isObject(val) && isFunction(val.pipe);
}

/**
 * Determine if a value is a URLSearchParams object
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a URLSearchParams object, otherwise false
 */
function isURLSearchParams(val) {
  return typeof URLSearchParams !== 'undefined' && val instanceof URLSearchParams;
}

/**
 * Trim excess whitespace off the beginning and end of a string
 *
 * @param {String} str The String to trim
 * @returns {String} The String freed of excess whitespace
 */
function trim(str) {
  return str.replace(/^\s*/, '').replace(/\s*$/, '');
}

/**
 * Determine if we're running in a standard browser environment
 *
 * This allows axios to run in a web worker, and react-native.
 * Both environments support XMLHttpRequest, but not fully standard globals.
 *
 * web workers:
 *  typeof window -> undefined
 *  typeof document -> undefined
 *
 * react-native:
 *  navigator.product -> 'ReactNative'
 */
function isStandardBrowserEnv() {
  if (typeof navigator !== 'undefined' && navigator.product === 'ReactNative') {
    return false;
  }
  return (
    typeof window !== 'undefined' &&
    typeof document !== 'undefined'
  );
}

/**
 * Iterate over an Array or an Object invoking a function for each item.
 *
 * If `obj` is an Array callback will be called passing
 * the value, index, and complete array for each item.
 *
 * If 'obj' is an Object callback will be called passing
 * the value, key, and complete object for each property.
 *
 * @param {Object|Array} obj The object to iterate
 * @param {Function} fn The callback to invoke for each item
 */
function forEach(obj, fn) {
  // Don't bother if no value provided
  if (obj === null || typeof obj === 'undefined') {
    return;
  }

  // Force an array if not already something iterable
  if (typeof obj !== 'object') {
    /*eslint no-param-reassign:0*/
    obj = [obj];
  }

  if (isArray(obj)) {
    // Iterate over array values
    for (var i = 0, l = obj.length; i < l; i++) {
      fn.call(null, obj[i], i, obj);
    }
  } else {
    // Iterate over object keys
    for (var key in obj) {
      if (Object.prototype.hasOwnProperty.call(obj, key)) {
        fn.call(null, obj[key], key, obj);
      }
    }
  }
}

/**
 * Accepts varargs expecting each argument to be an object, then
 * immutably merges the properties of each object and returns result.
 *
 * When multiple objects contain the same key the later object in
 * the arguments list will take precedence.
 *
 * Example:
 *
 * ```js
 * var result = merge({foo: 123}, {foo: 456});
 * console.log(result.foo); // outputs 456
 * ```
 *
 * @param {Object} obj1 Object to merge
 * @returns {Object} Result of all merge properties
 */
function merge(/* obj1, obj2, obj3, ... */) {
  var result = {};
  function assignValue(val, key) {
    if (typeof result[key] === 'object' && typeof val === 'object') {
      result[key] = merge(result[key], val);
    } else {
      result[key] = val;
    }
  }

  for (var i = 0, l = arguments.length; i < l; i++) {
    forEach(arguments[i], assignValue);
  }
  return result;
}

/**
 * Extends object a by mutably adding to it the properties of object b.
 *
 * @param {Object} a The object to be extended
 * @param {Object} b The object to copy properties from
 * @param {Object} thisArg The object to bind function to
 * @return {Object} The resulting value of object a
 */
function extend(a, b, thisArg) {
  forEach(b, function assignValue(val, key) {
    if (thisArg && typeof val === 'function') {
      a[key] = bind(val, thisArg);
    } else {
      a[key] = val;
    }
  });
  return a;
}

module.exports = {
  isArray: isArray,
  isArrayBuffer: isArrayBuffer,
  isBuffer: isBuffer,
  isFormData: isFormData,
  isArrayBufferView: isArrayBufferView,
  isString: isString,
  isNumber: isNumber,
  isObject: isObject,
  isUndefined: isUndefined,
  isDate: isDate,
  isFile: isFile,
  isBlob: isBlob,
  isFunction: isFunction,
  isStream: isStream,
  isURLSearchParams: isURLSearchParams,
  isStandardBrowserEnv: isStandardBrowserEnv,
  forEach: forEach,
  merge: merge,
  extend: extend,
  trim: trim
};


/***/ }),
/* 1 */
/***/ (function(module, exports) {

var g;

// This works in non-strict mode
g = (function() {
	return this;
})();

try {
	// This works if eval is allowed (see CSP)
	g = g || Function("return this")() || (1,eval)("this");
} catch(e) {
	// This works if the window reference is available
	if(typeof window === "object")
		g = window;
}

// g can still be undefined, but nothing to do about it...
// We return undefined, instead of nothing here, so it's
// easier to handle this case. if(!global) { ...}

module.exports = g;


/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(process) {

var utils = __webpack_require__(0);
var normalizeHeaderName = __webpack_require__(36);

var DEFAULT_CONTENT_TYPE = {
  'Content-Type': 'application/x-www-form-urlencoded'
};

function setContentTypeIfUnset(headers, value) {
  if (!utils.isUndefined(headers) && utils.isUndefined(headers['Content-Type'])) {
    headers['Content-Type'] = value;
  }
}

function getDefaultAdapter() {
  var adapter;
  if (typeof XMLHttpRequest !== 'undefined') {
    // For browsers use XHR adapter
    adapter = __webpack_require__(4);
  } else if (typeof process !== 'undefined') {
    // For node use HTTP adapter
    adapter = __webpack_require__(4);
  }
  return adapter;
}

var defaults = {
  adapter: getDefaultAdapter(),

  transformRequest: [function transformRequest(data, headers) {
    normalizeHeaderName(headers, 'Content-Type');
    if (utils.isFormData(data) ||
      utils.isArrayBuffer(data) ||
      utils.isBuffer(data) ||
      utils.isStream(data) ||
      utils.isFile(data) ||
      utils.isBlob(data)
    ) {
      return data;
    }
    if (utils.isArrayBufferView(data)) {
      return data.buffer;
    }
    if (utils.isURLSearchParams(data)) {
      setContentTypeIfUnset(headers, 'application/x-www-form-urlencoded;charset=utf-8');
      return data.toString();
    }
    if (utils.isObject(data)) {
      setContentTypeIfUnset(headers, 'application/json;charset=utf-8');
      return JSON.stringify(data);
    }
    return data;
  }],

  transformResponse: [function transformResponse(data) {
    /*eslint no-param-reassign:0*/
    if (typeof data === 'string') {
      try {
        data = JSON.parse(data);
      } catch (e) { /* Ignore */ }
    }
    return data;
  }],

  /**
   * A timeout in milliseconds to abort a request. If set to 0 (default) a
   * timeout is not created.
   */
  timeout: 0,

  xsrfCookieName: 'XSRF-TOKEN',
  xsrfHeaderName: 'X-XSRF-TOKEN',

  maxContentLength: -1,

  validateStatus: function validateStatus(status) {
    return status >= 200 && status < 300;
  }
};

defaults.headers = {
  common: {
    'Accept': 'application/json, text/plain, */*'
  }
};

utils.forEach(['delete', 'get', 'head'], function forEachMethodNoData(method) {
  defaults.headers[method] = {};
});

utils.forEach(['post', 'put', 'patch'], function forEachMethodWithData(method) {
  defaults.headers[method] = utils.merge(DEFAULT_CONTENT_TYPE);
});

module.exports = defaults;

/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(3)))

/***/ }),
/* 3 */
/***/ (function(module, exports) {

// shim for using process in browser
var process = module.exports = {};

// cached from whatever global is present so that test runners that stub it
// don't break things.  But we need to wrap it in a try catch in case it is
// wrapped in strict mode code which doesn't define any globals.  It's inside a
// function because try/catches deoptimize in certain engines.

var cachedSetTimeout;
var cachedClearTimeout;

function defaultSetTimout() {
    throw new Error('setTimeout has not been defined');
}
function defaultClearTimeout () {
    throw new Error('clearTimeout has not been defined');
}
(function () {
    try {
        if (typeof setTimeout === 'function') {
            cachedSetTimeout = setTimeout;
        } else {
            cachedSetTimeout = defaultSetTimout;
        }
    } catch (e) {
        cachedSetTimeout = defaultSetTimout;
    }
    try {
        if (typeof clearTimeout === 'function') {
            cachedClearTimeout = clearTimeout;
        } else {
            cachedClearTimeout = defaultClearTimeout;
        }
    } catch (e) {
        cachedClearTimeout = defaultClearTimeout;
    }
} ())
function runTimeout(fun) {
    if (cachedSetTimeout === setTimeout) {
        //normal enviroments in sane situations
        return setTimeout(fun, 0);
    }
    // if setTimeout wasn't available but was latter defined
    if ((cachedSetTimeout === defaultSetTimout || !cachedSetTimeout) && setTimeout) {
        cachedSetTimeout = setTimeout;
        return setTimeout(fun, 0);
    }
    try {
        // when when somebody has screwed with setTimeout but no I.E. maddness
        return cachedSetTimeout(fun, 0);
    } catch(e){
        try {
            // When we are in I.E. but the script has been evaled so I.E. doesn't trust the global object when called normally
            return cachedSetTimeout.call(null, fun, 0);
        } catch(e){
            // same as above but when it's a version of I.E. that must have the global object for 'this', hopfully our context correct otherwise it will throw a global error
            return cachedSetTimeout.call(this, fun, 0);
        }
    }


}
function runClearTimeout(marker) {
    if (cachedClearTimeout === clearTimeout) {
        //normal enviroments in sane situations
        return clearTimeout(marker);
    }
    // if clearTimeout wasn't available but was latter defined
    if ((cachedClearTimeout === defaultClearTimeout || !cachedClearTimeout) && clearTimeout) {
        cachedClearTimeout = clearTimeout;
        return clearTimeout(marker);
    }
    try {
        // when when somebody has screwed with setTimeout but no I.E. maddness
        return cachedClearTimeout(marker);
    } catch (e){
        try {
            // When we are in I.E. but the script has been evaled so I.E. doesn't  trust the global object when called normally
            return cachedClearTimeout.call(null, marker);
        } catch (e){
            // same as above but when it's a version of I.E. that must have the global object for 'this', hopfully our context correct otherwise it will throw a global error.
            // Some versions of I.E. have different rules for clearTimeout vs setTimeout
            return cachedClearTimeout.call(this, marker);
        }
    }



}
var queue = [];
var draining = false;
var currentQueue;
var queueIndex = -1;

function cleanUpNextTick() {
    if (!draining || !currentQueue) {
        return;
    }
    draining = false;
    if (currentQueue.length) {
        queue = currentQueue.concat(queue);
    } else {
        queueIndex = -1;
    }
    if (queue.length) {
        drainQueue();
    }
}

function drainQueue() {
    if (draining) {
        return;
    }
    var timeout = runTimeout(cleanUpNextTick);
    draining = true;

    var len = queue.length;
    while(len) {
        currentQueue = queue;
        queue = [];
        while (++queueIndex < len) {
            if (currentQueue) {
                currentQueue[queueIndex].run();
            }
        }
        queueIndex = -1;
        len = queue.length;
    }
    currentQueue = null;
    draining = false;
    runClearTimeout(timeout);
}

process.nextTick = function (fun) {
    var args = new Array(arguments.length - 1);
    if (arguments.length > 1) {
        for (var i = 1; i < arguments.length; i++) {
            args[i - 1] = arguments[i];
        }
    }
    queue.push(new Item(fun, args));
    if (queue.length === 1 && !draining) {
        runTimeout(drainQueue);
    }
};

// v8 likes predictible objects
function Item(fun, array) {
    this.fun = fun;
    this.array = array;
}
Item.prototype.run = function () {
    this.fun.apply(null, this.array);
};
process.title = 'browser';
process.browser = true;
process.env = {};
process.argv = [];
process.version = ''; // empty string to avoid regexp issues
process.versions = {};

function noop() {}

process.on = noop;
process.addListener = noop;
process.once = noop;
process.off = noop;
process.removeListener = noop;
process.removeAllListeners = noop;
process.emit = noop;
process.prependListener = noop;
process.prependOnceListener = noop;

process.listeners = function (name) { return [] }

process.binding = function (name) {
    throw new Error('process.binding is not supported');
};

process.cwd = function () { return '/' };
process.chdir = function (dir) {
    throw new Error('process.chdir is not supported');
};
process.umask = function() { return 0; };


/***/ }),
/* 4 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var utils = __webpack_require__(0);
var settle = __webpack_require__(28);
var buildURL = __webpack_require__(31);
var parseHeaders = __webpack_require__(37);
var isURLSameOrigin = __webpack_require__(35);
var createError = __webpack_require__(7);
var btoa = (typeof window !== 'undefined' && window.btoa && window.btoa.bind(window)) || __webpack_require__(30);

module.exports = function xhrAdapter(config) {
  return new Promise(function dispatchXhrRequest(resolve, reject) {
    var requestData = config.data;
    var requestHeaders = config.headers;

    if (utils.isFormData(requestData)) {
      delete requestHeaders['Content-Type']; // Let the browser set it
    }

    var request = new XMLHttpRequest();
    var loadEvent = 'onreadystatechange';
    var xDomain = false;

    // For IE 8/9 CORS support
    // Only supports POST and GET calls and doesn't returns the response headers.
    // DON'T do this for testing b/c XMLHttpRequest is mocked, not XDomainRequest.
    if ("production" !== 'test' &&
        typeof window !== 'undefined' &&
        window.XDomainRequest && !('withCredentials' in request) &&
        !isURLSameOrigin(config.url)) {
      request = new window.XDomainRequest();
      loadEvent = 'onload';
      xDomain = true;
      request.onprogress = function handleProgress() {};
      request.ontimeout = function handleTimeout() {};
    }

    // HTTP basic authentication
    if (config.auth) {
      var username = config.auth.username || '';
      var password = config.auth.password || '';
      requestHeaders.Authorization = 'Basic ' + btoa(username + ':' + password);
    }

    request.open(config.method.toUpperCase(), buildURL(config.url, config.params, config.paramsSerializer), true);

    // Set the request timeout in MS
    request.timeout = config.timeout;

    // Listen for ready state
    request[loadEvent] = function handleLoad() {
      if (!request || (request.readyState !== 4 && !xDomain)) {
        return;
      }

      // The request errored out and we didn't get a response, this will be
      // handled by onerror instead
      // With one exception: request that using file: protocol, most browsers
      // will return status as 0 even though it's a successful request
      if (request.status === 0 && !(request.responseURL && request.responseURL.indexOf('file:') === 0)) {
        return;
      }

      // Prepare the response
      var responseHeaders = 'getAllResponseHeaders' in request ? parseHeaders(request.getAllResponseHeaders()) : null;
      var responseData = !config.responseType || config.responseType === 'text' ? request.responseText : request.response;
      var response = {
        data: responseData,
        // IE sends 1223 instead of 204 (https://github.com/axios/axios/issues/201)
        status: request.status === 1223 ? 204 : request.status,
        statusText: request.status === 1223 ? 'No Content' : request.statusText,
        headers: responseHeaders,
        config: config,
        request: request
      };

      settle(resolve, reject, response);

      // Clean up request
      request = null;
    };

    // Handle low level network errors
    request.onerror = function handleError() {
      // Real errors are hidden from us by the browser
      // onerror should only fire if it's a network error
      reject(createError('Network Error', config, null, request));

      // Clean up request
      request = null;
    };

    // Handle timeout
    request.ontimeout = function handleTimeout() {
      reject(createError('timeout of ' + config.timeout + 'ms exceeded', config, 'ECONNABORTED',
        request));

      // Clean up request
      request = null;
    };

    // Add xsrf header
    // This is only done if running in a standard browser environment.
    // Specifically not if we're in a web worker, or react-native.
    if (utils.isStandardBrowserEnv()) {
      var cookies = __webpack_require__(33);

      // Add xsrf header
      var xsrfValue = (config.withCredentials || isURLSameOrigin(config.url)) && config.xsrfCookieName ?
          cookies.read(config.xsrfCookieName) :
          undefined;

      if (xsrfValue) {
        requestHeaders[config.xsrfHeaderName] = xsrfValue;
      }
    }

    // Add headers to the request
    if ('setRequestHeader' in request) {
      utils.forEach(requestHeaders, function setRequestHeader(val, key) {
        if (typeof requestData === 'undefined' && key.toLowerCase() === 'content-type') {
          // Remove Content-Type if data is undefined
          delete requestHeaders[key];
        } else {
          // Otherwise add header to the request
          request.setRequestHeader(key, val);
        }
      });
    }

    // Add withCredentials to request if needed
    if (config.withCredentials) {
      request.withCredentials = true;
    }

    // Add responseType to request if needed
    if (config.responseType) {
      try {
        request.responseType = config.responseType;
      } catch (e) {
        // Expected DOMException thrown by browsers not compatible XMLHttpRequest Level 2.
        // But, this can be suppressed for 'json' type as it can be parsed by default 'transformResponse' function.
        if (config.responseType !== 'json') {
          throw e;
        }
      }
    }

    // Handle progress if needed
    if (typeof config.onDownloadProgress === 'function') {
      request.addEventListener('progress', config.onDownloadProgress);
    }

    // Not all browsers support upload events
    if (typeof config.onUploadProgress === 'function' && request.upload) {
      request.upload.addEventListener('progress', config.onUploadProgress);
    }

    if (config.cancelToken) {
      // Handle cancellation
      config.cancelToken.promise.then(function onCanceled(cancel) {
        if (!request) {
          return;
        }

        request.abort();
        reject(cancel);
        // Clean up request
        request = null;
      });
    }

    if (requestData === undefined) {
      requestData = null;
    }

    // Send the request
    request.send(requestData);
  });
};


/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


/**
 * A `Cancel` is an object that is thrown when an operation is canceled.
 *
 * @class
 * @param {string=} message The message.
 */
function Cancel(message) {
  this.message = message;
}

Cancel.prototype.toString = function toString() {
  return 'Cancel' + (this.message ? ': ' + this.message : '');
};

Cancel.prototype.__CANCEL__ = true;

module.exports = Cancel;


/***/ }),
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


module.exports = function isCancel(value) {
  return !!(value && value.__CANCEL__);
};


/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var enhanceError = __webpack_require__(27);

/**
 * Create an Error with the specified message, config, error code, request and response.
 *
 * @param {string} message The error message.
 * @param {Object} config The config.
 * @param {string} [code] The error code (for example, 'ECONNABORTED').
 * @param {Object} [request] The request.
 * @param {Object} [response] The response.
 * @returns {Error} The created error.
 */
module.exports = function createError(message, config, code, request, response) {
  var error = new Error(message);
  return enhanceError(error, config, code, request, response);
};


/***/ }),
/* 8 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


module.exports = function bind(fn, thisArg) {
  return function wrap() {
    var args = new Array(arguments.length);
    for (var i = 0; i < args.length; i++) {
      args[i] = arguments[i];
    }
    return fn.apply(thisArg, args);
  };
};


/***/ }),
/* 9 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var hexTable = (function () {
    var array = new Array(256);
    for (var i = 0; i < 256; ++i) {
        array[i] = '%' + ((i < 16 ? '0' : '') + i.toString(16)).toUpperCase();
    }

    return array;
}());

var has = Object.prototype.hasOwnProperty;

exports.arrayToObject = function (source, options) {
    var obj = options.plainObjects ? Object.create(null) : {};
    for (var i = 0; i < source.length; ++i) {
        if (typeof source[i] !== 'undefined') {
            obj[i] = source[i];
        }
    }

    return obj;
};

exports.merge = function (target, source, options) {
    if (!source) {
        return target;
    }

    if (typeof source !== 'object') {
        if (Array.isArray(target)) {
            target.push(source);
        } else if (typeof target === 'object') {
            if (options.plainObjects || options.allowPrototypes || !has.call(Object.prototype, source)) {
                target[source] = true;
            }
        } else {
            return [target, source];
        }

        return target;
    }

    if (typeof target !== 'object') {
        return [target].concat(source);
    }

    var mergeTarget = target;
    if (Array.isArray(target) && !Array.isArray(source)) {
        mergeTarget = exports.arrayToObject(target, options);
    }

    return Object.keys(source).reduce(function (acc, key) {
        var value = source[key];

        if (has.call(acc, key)) {
            acc[key] = exports.merge(acc[key], value, options);
        } else {
            acc[key] = value;
        }
        return acc;
    }, mergeTarget);
};

exports.decode = function (str) {
    try {
        return decodeURIComponent(str.replace(/\+/g, ' '));
    } catch (e) {
        return str;
    }
};

exports.encode = function (str) {
    // This code was originally written by Brian White (mscdex) for the io.js core querystring library.
    // It has been adapted here for stricter adherence to RFC 3986
    if (str.length === 0) {
        return str;
    }

    var string = typeof str === 'string' ? str : String(str);

    var out = '';
    for (var i = 0; i < string.length; ++i) {
        var c = string.charCodeAt(i);

        if (
            c === 0x2D || // -
            c === 0x2E || // .
            c === 0x5F || // _
            c === 0x7E || // ~
            (c >= 0x30 && c <= 0x39) || // 0-9
            (c >= 0x41 && c <= 0x5A) || // a-z
            (c >= 0x61 && c <= 0x7A) // A-Z
        ) {
            out += string.charAt(i);
            continue;
        }

        if (c < 0x80) {
            out = out + hexTable[c];
            continue;
        }

        if (c < 0x800) {
            out = out + (hexTable[0xC0 | (c >> 6)] + hexTable[0x80 | (c & 0x3F)]);
            continue;
        }

        if (c < 0xD800 || c >= 0xE000) {
            out = out + (hexTable[0xE0 | (c >> 12)] + hexTable[0x80 | ((c >> 6) & 0x3F)] + hexTable[0x80 | (c & 0x3F)]);
            continue;
        }

        i += 1;
        c = 0x10000 + (((c & 0x3FF) << 10) | (string.charCodeAt(i) & 0x3FF));
        out += hexTable[0xF0 | (c >> 18)] + hexTable[0x80 | ((c >> 12) & 0x3F)] + hexTable[0x80 | ((c >> 6) & 0x3F)] + hexTable[0x80 | (c & 0x3F)];
    }

    return out;
};

exports.compact = function (obj, references) {
    if (typeof obj !== 'object' || obj === null) {
        return obj;
    }

    var refs = references || [];
    var lookup = refs.indexOf(obj);
    if (lookup !== -1) {
        return refs[lookup];
    }

    refs.push(obj);

    if (Array.isArray(obj)) {
        var compacted = [];

        for (var i = 0; i < obj.length; ++i) {
            if (obj[i] && typeof obj[i] === 'object') {
                compacted.push(exports.compact(obj[i], refs));
            } else if (typeof obj[i] !== 'undefined') {
                compacted.push(obj[i]);
            }
        }

        return compacted;
    }

    var keys = Object.keys(obj);
    for (var j = 0; j < keys.length; ++j) {
        var key = keys[j];
        obj[key] = exports.compact(obj[key], refs);
    }

    return obj;
};

exports.isRegExp = function (obj) {
    return Object.prototype.toString.call(obj) === '[object RegExp]';
};

exports.isBuffer = function (obj) {
    if (obj === null || typeof obj === 'undefined') {
        return false;
    }

    return !!(obj.constructor && obj.constructor.isBuffer && obj.constructor.isBuffer(obj));
};


/***/ }),
/* 10 */
/***/ (function(module, exports) {

/* globals __VUE_SSR_CONTEXT__ */

// this module is a runtime utility for cleaner component module output and will
// be included in the final webpack user bundle

module.exports = function normalizeComponent (
  rawScriptExports,
  compiledTemplate,
  injectStyles,
  scopeId,
  moduleIdentifier /* server only */
) {
  var esModule
  var scriptExports = rawScriptExports = rawScriptExports || {}

  // ES6 modules interop
  var type = typeof rawScriptExports.default
  if (type === 'object' || type === 'function') {
    esModule = rawScriptExports
    scriptExports = rawScriptExports.default
  }

  // Vue.extend constructor export interop
  var options = typeof scriptExports === 'function'
    ? scriptExports.options
    : scriptExports

  // render functions
  if (compiledTemplate) {
    options.render = compiledTemplate.render
    options.staticRenderFns = compiledTemplate.staticRenderFns
  }

  // scopedId
  if (scopeId) {
    options._scopeId = scopeId
  }

  var hook
  if (moduleIdentifier) { // server build
    hook = function (context) {
      // 2.3 injection
      context =
        context || // cached call
        (this.$vnode && this.$vnode.ssrContext) || // stateful
        (this.parent && this.parent.$vnode && this.parent.$vnode.ssrContext) // functional
      // 2.2 with runInNewContext: true
      if (!context && typeof __VUE_SSR_CONTEXT__ !== 'undefined') {
        context = __VUE_SSR_CONTEXT__
      }
      // inject component styles
      if (injectStyles) {
        injectStyles.call(this, context)
      }
      // register component module identifier for async chunk inferrence
      if (context && context._registeredComponents) {
        context._registeredComponents.add(moduleIdentifier)
      }
    }
    // used by ssr in case component is cached and beforeCreate
    // never gets called
    options._ssrRegister = hook
  } else if (injectStyles) {
    hook = injectStyles
  }

  if (hook) {
    var functional = options.functional
    var existing = functional
      ? options.render
      : options.beforeCreate
    if (!functional) {
      // inject component registration as beforeCreate hook
      options.beforeCreate = existing
        ? [].concat(existing, hook)
        : [hook]
    } else {
      // register for functioal component in vue file
      options.render = function renderWithStyleInjection (h, context) {
        hook.call(context)
        return existing(h, context)
      }
    }
  }

  return {
    esModule: esModule,
    exports: scriptExports,
    options: options
  }
}


/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(22);

/***/ }),
/* 12 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";

const index = __webpack_require__(54);
/* harmony default export */ __webpack_exports__["a"] = ({
    // mode: 'history',
    scrollBehavior: () => ({ y: 0 }),
    routes: [{
        path: '/index',
        component: index,
        alias: "下载页"
    },
    //默认加载页
    { path: '/', redirect: '/index' }]
});

/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(process, global) {/*!
 * @overview es6-promise - a tiny implementation of Promises/A+.
 * @copyright Copyright (c) 2014 Yehuda Katz, Tom Dale, Stefan Penner and contributors (Conversion to ES6 API by Jake Archibald)
 * @license   Licensed under MIT license
 *            See https://raw.githubusercontent.com/stefanpenner/es6-promise/master/LICENSE
 * @version   v4.2.5+7f2b526d
 */

(function (global, factory) {
	 true ? module.exports = factory() :
	typeof define === 'function' && define.amd ? define(factory) :
	(global.ES6Promise = factory());
}(this, (function () { 'use strict';

function objectOrFunction(x) {
  var type = typeof x;
  return x !== null && (type === 'object' || type === 'function');
}

function isFunction(x) {
  return typeof x === 'function';
}



var _isArray = void 0;
if (Array.isArray) {
  _isArray = Array.isArray;
} else {
  _isArray = function (x) {
    return Object.prototype.toString.call(x) === '[object Array]';
  };
}

var isArray = _isArray;

var len = 0;
var vertxNext = void 0;
var customSchedulerFn = void 0;

var asap = function asap(callback, arg) {
  queue[len] = callback;
  queue[len + 1] = arg;
  len += 2;
  if (len === 2) {
    // If len is 2, that means that we need to schedule an async flush.
    // If additional callbacks are queued before the queue is flushed, they
    // will be processed by this flush that we are scheduling.
    if (customSchedulerFn) {
      customSchedulerFn(flush);
    } else {
      scheduleFlush();
    }
  }
};

function setScheduler(scheduleFn) {
  customSchedulerFn = scheduleFn;
}

function setAsap(asapFn) {
  asap = asapFn;
}

var browserWindow = typeof window !== 'undefined' ? window : undefined;
var browserGlobal = browserWindow || {};
var BrowserMutationObserver = browserGlobal.MutationObserver || browserGlobal.WebKitMutationObserver;
var isNode = typeof self === 'undefined' && typeof process !== 'undefined' && {}.toString.call(process) === '[object process]';

// test for web worker but not in IE10
var isWorker = typeof Uint8ClampedArray !== 'undefined' && typeof importScripts !== 'undefined' && typeof MessageChannel !== 'undefined';

// node
function useNextTick() {
  // node version 0.10.x displays a deprecation warning when nextTick is used recursively
  // see https://github.com/cujojs/when/issues/410 for details
  return function () {
    return process.nextTick(flush);
  };
}

// vertx
function useVertxTimer() {
  if (typeof vertxNext !== 'undefined') {
    return function () {
      vertxNext(flush);
    };
  }

  return useSetTimeout();
}

function useMutationObserver() {
  var iterations = 0;
  var observer = new BrowserMutationObserver(flush);
  var node = document.createTextNode('');
  observer.observe(node, { characterData: true });

  return function () {
    node.data = iterations = ++iterations % 2;
  };
}

// web worker
function useMessageChannel() {
  var channel = new MessageChannel();
  channel.port1.onmessage = flush;
  return function () {
    return channel.port2.postMessage(0);
  };
}

function useSetTimeout() {
  // Store setTimeout reference so es6-promise will be unaffected by
  // other code modifying setTimeout (like sinon.useFakeTimers())
  var globalSetTimeout = setTimeout;
  return function () {
    return globalSetTimeout(flush, 1);
  };
}

var queue = new Array(1000);
function flush() {
  for (var i = 0; i < len; i += 2) {
    var callback = queue[i];
    var arg = queue[i + 1];

    callback(arg);

    queue[i] = undefined;
    queue[i + 1] = undefined;
  }

  len = 0;
}

function attemptVertx() {
  try {
    var vertx = Function('return this')().require('vertx');
    vertxNext = vertx.runOnLoop || vertx.runOnContext;
    return useVertxTimer();
  } catch (e) {
    return useSetTimeout();
  }
}

var scheduleFlush = void 0;
// Decide what async method to use to triggering processing of queued callbacks:
if (isNode) {
  scheduleFlush = useNextTick();
} else if (BrowserMutationObserver) {
  scheduleFlush = useMutationObserver();
} else if (isWorker) {
  scheduleFlush = useMessageChannel();
} else if (browserWindow === undefined && "function" === 'function') {
  scheduleFlush = attemptVertx();
} else {
  scheduleFlush = useSetTimeout();
}

function then(onFulfillment, onRejection) {
  var parent = this;

  var child = new this.constructor(noop);

  if (child[PROMISE_ID] === undefined) {
    makePromise(child);
  }

  var _state = parent._state;


  if (_state) {
    var callback = arguments[_state - 1];
    asap(function () {
      return invokeCallback(_state, child, callback, parent._result);
    });
  } else {
    subscribe(parent, child, onFulfillment, onRejection);
  }

  return child;
}

/**
  `Promise.resolve` returns a promise that will become resolved with the
  passed `value`. It is shorthand for the following:

  ```javascript
  let promise = new Promise(function(resolve, reject){
    resolve(1);
  });

  promise.then(function(value){
    // value === 1
  });
  ```

  Instead of writing the above, your code now simply becomes the following:

  ```javascript
  let promise = Promise.resolve(1);

  promise.then(function(value){
    // value === 1
  });
  ```

  @method resolve
  @static
  @param {Any} value value that the returned promise will be resolved with
  Useful for tooling.
  @return {Promise} a promise that will become fulfilled with the given
  `value`
*/
function resolve$1(object) {
  /*jshint validthis:true */
  var Constructor = this;

  if (object && typeof object === 'object' && object.constructor === Constructor) {
    return object;
  }

  var promise = new Constructor(noop);
  resolve(promise, object);
  return promise;
}

var PROMISE_ID = Math.random().toString(36).substring(2);

function noop() {}

var PENDING = void 0;
var FULFILLED = 1;
var REJECTED = 2;

var TRY_CATCH_ERROR = { error: null };

function selfFulfillment() {
  return new TypeError("You cannot resolve a promise with itself");
}

function cannotReturnOwn() {
  return new TypeError('A promises callback cannot return that same promise.');
}

function getThen(promise) {
  try {
    return promise.then;
  } catch (error) {
    TRY_CATCH_ERROR.error = error;
    return TRY_CATCH_ERROR;
  }
}

function tryThen(then$$1, value, fulfillmentHandler, rejectionHandler) {
  try {
    then$$1.call(value, fulfillmentHandler, rejectionHandler);
  } catch (e) {
    return e;
  }
}

function handleForeignThenable(promise, thenable, then$$1) {
  asap(function (promise) {
    var sealed = false;
    var error = tryThen(then$$1, thenable, function (value) {
      if (sealed) {
        return;
      }
      sealed = true;
      if (thenable !== value) {
        resolve(promise, value);
      } else {
        fulfill(promise, value);
      }
    }, function (reason) {
      if (sealed) {
        return;
      }
      sealed = true;

      reject(promise, reason);
    }, 'Settle: ' + (promise._label || ' unknown promise'));

    if (!sealed && error) {
      sealed = true;
      reject(promise, error);
    }
  }, promise);
}

function handleOwnThenable(promise, thenable) {
  if (thenable._state === FULFILLED) {
    fulfill(promise, thenable._result);
  } else if (thenable._state === REJECTED) {
    reject(promise, thenable._result);
  } else {
    subscribe(thenable, undefined, function (value) {
      return resolve(promise, value);
    }, function (reason) {
      return reject(promise, reason);
    });
  }
}

function handleMaybeThenable(promise, maybeThenable, then$$1) {
  if (maybeThenable.constructor === promise.constructor && then$$1 === then && maybeThenable.constructor.resolve === resolve$1) {
    handleOwnThenable(promise, maybeThenable);
  } else {
    if (then$$1 === TRY_CATCH_ERROR) {
      reject(promise, TRY_CATCH_ERROR.error);
      TRY_CATCH_ERROR.error = null;
    } else if (then$$1 === undefined) {
      fulfill(promise, maybeThenable);
    } else if (isFunction(then$$1)) {
      handleForeignThenable(promise, maybeThenable, then$$1);
    } else {
      fulfill(promise, maybeThenable);
    }
  }
}

function resolve(promise, value) {
  if (promise === value) {
    reject(promise, selfFulfillment());
  } else if (objectOrFunction(value)) {
    handleMaybeThenable(promise, value, getThen(value));
  } else {
    fulfill(promise, value);
  }
}

function publishRejection(promise) {
  if (promise._onerror) {
    promise._onerror(promise._result);
  }

  publish(promise);
}

function fulfill(promise, value) {
  if (promise._state !== PENDING) {
    return;
  }

  promise._result = value;
  promise._state = FULFILLED;

  if (promise._subscribers.length !== 0) {
    asap(publish, promise);
  }
}

function reject(promise, reason) {
  if (promise._state !== PENDING) {
    return;
  }
  promise._state = REJECTED;
  promise._result = reason;

  asap(publishRejection, promise);
}

function subscribe(parent, child, onFulfillment, onRejection) {
  var _subscribers = parent._subscribers;
  var length = _subscribers.length;


  parent._onerror = null;

  _subscribers[length] = child;
  _subscribers[length + FULFILLED] = onFulfillment;
  _subscribers[length + REJECTED] = onRejection;

  if (length === 0 && parent._state) {
    asap(publish, parent);
  }
}

function publish(promise) {
  var subscribers = promise._subscribers;
  var settled = promise._state;

  if (subscribers.length === 0) {
    return;
  }

  var child = void 0,
      callback = void 0,
      detail = promise._result;

  for (var i = 0; i < subscribers.length; i += 3) {
    child = subscribers[i];
    callback = subscribers[i + settled];

    if (child) {
      invokeCallback(settled, child, callback, detail);
    } else {
      callback(detail);
    }
  }

  promise._subscribers.length = 0;
}

function tryCatch(callback, detail) {
  try {
    return callback(detail);
  } catch (e) {
    TRY_CATCH_ERROR.error = e;
    return TRY_CATCH_ERROR;
  }
}

function invokeCallback(settled, promise, callback, detail) {
  var hasCallback = isFunction(callback),
      value = void 0,
      error = void 0,
      succeeded = void 0,
      failed = void 0;

  if (hasCallback) {
    value = tryCatch(callback, detail);

    if (value === TRY_CATCH_ERROR) {
      failed = true;
      error = value.error;
      value.error = null;
    } else {
      succeeded = true;
    }

    if (promise === value) {
      reject(promise, cannotReturnOwn());
      return;
    }
  } else {
    value = detail;
    succeeded = true;
  }

  if (promise._state !== PENDING) {
    // noop
  } else if (hasCallback && succeeded) {
    resolve(promise, value);
  } else if (failed) {
    reject(promise, error);
  } else if (settled === FULFILLED) {
    fulfill(promise, value);
  } else if (settled === REJECTED) {
    reject(promise, value);
  }
}

function initializePromise(promise, resolver) {
  try {
    resolver(function resolvePromise(value) {
      resolve(promise, value);
    }, function rejectPromise(reason) {
      reject(promise, reason);
    });
  } catch (e) {
    reject(promise, e);
  }
}

var id = 0;
function nextId() {
  return id++;
}

function makePromise(promise) {
  promise[PROMISE_ID] = id++;
  promise._state = undefined;
  promise._result = undefined;
  promise._subscribers = [];
}

function validationError() {
  return new Error('Array Methods must be provided an Array');
}

var Enumerator = function () {
  function Enumerator(Constructor, input) {
    this._instanceConstructor = Constructor;
    this.promise = new Constructor(noop);

    if (!this.promise[PROMISE_ID]) {
      makePromise(this.promise);
    }

    if (isArray(input)) {
      this.length = input.length;
      this._remaining = input.length;

      this._result = new Array(this.length);

      if (this.length === 0) {
        fulfill(this.promise, this._result);
      } else {
        this.length = this.length || 0;
        this._enumerate(input);
        if (this._remaining === 0) {
          fulfill(this.promise, this._result);
        }
      }
    } else {
      reject(this.promise, validationError());
    }
  }

  Enumerator.prototype._enumerate = function _enumerate(input) {
    for (var i = 0; this._state === PENDING && i < input.length; i++) {
      this._eachEntry(input[i], i);
    }
  };

  Enumerator.prototype._eachEntry = function _eachEntry(entry, i) {
    var c = this._instanceConstructor;
    var resolve$$1 = c.resolve;


    if (resolve$$1 === resolve$1) {
      var _then = getThen(entry);

      if (_then === then && entry._state !== PENDING) {
        this._settledAt(entry._state, i, entry._result);
      } else if (typeof _then !== 'function') {
        this._remaining--;
        this._result[i] = entry;
      } else if (c === Promise$1) {
        var promise = new c(noop);
        handleMaybeThenable(promise, entry, _then);
        this._willSettleAt(promise, i);
      } else {
        this._willSettleAt(new c(function (resolve$$1) {
          return resolve$$1(entry);
        }), i);
      }
    } else {
      this._willSettleAt(resolve$$1(entry), i);
    }
  };

  Enumerator.prototype._settledAt = function _settledAt(state, i, value) {
    var promise = this.promise;


    if (promise._state === PENDING) {
      this._remaining--;

      if (state === REJECTED) {
        reject(promise, value);
      } else {
        this._result[i] = value;
      }
    }

    if (this._remaining === 0) {
      fulfill(promise, this._result);
    }
  };

  Enumerator.prototype._willSettleAt = function _willSettleAt(promise, i) {
    var enumerator = this;

    subscribe(promise, undefined, function (value) {
      return enumerator._settledAt(FULFILLED, i, value);
    }, function (reason) {
      return enumerator._settledAt(REJECTED, i, reason);
    });
  };

  return Enumerator;
}();

/**
  `Promise.all` accepts an array of promises, and returns a new promise which
  is fulfilled with an array of fulfillment values for the passed promises, or
  rejected with the reason of the first passed promise to be rejected. It casts all
  elements of the passed iterable to promises as it runs this algorithm.

  Example:

  ```javascript
  let promise1 = resolve(1);
  let promise2 = resolve(2);
  let promise3 = resolve(3);
  let promises = [ promise1, promise2, promise3 ];

  Promise.all(promises).then(function(array){
    // The array here would be [ 1, 2, 3 ];
  });
  ```

  If any of the `promises` given to `all` are rejected, the first promise
  that is rejected will be given as an argument to the returned promises's
  rejection handler. For example:

  Example:

  ```javascript
  let promise1 = resolve(1);
  let promise2 = reject(new Error("2"));
  let promise3 = reject(new Error("3"));
  let promises = [ promise1, promise2, promise3 ];

  Promise.all(promises).then(function(array){
    // Code here never runs because there are rejected promises!
  }, function(error) {
    // error.message === "2"
  });
  ```

  @method all
  @static
  @param {Array} entries array of promises
  @param {String} label optional string for labeling the promise.
  Useful for tooling.
  @return {Promise} promise that is fulfilled when all `promises` have been
  fulfilled, or rejected if any of them become rejected.
  @static
*/
function all(entries) {
  return new Enumerator(this, entries).promise;
}

/**
  `Promise.race` returns a new promise which is settled in the same way as the
  first passed promise to settle.

  Example:

  ```javascript
  let promise1 = new Promise(function(resolve, reject){
    setTimeout(function(){
      resolve('promise 1');
    }, 200);
  });

  let promise2 = new Promise(function(resolve, reject){
    setTimeout(function(){
      resolve('promise 2');
    }, 100);
  });

  Promise.race([promise1, promise2]).then(function(result){
    // result === 'promise 2' because it was resolved before promise1
    // was resolved.
  });
  ```

  `Promise.race` is deterministic in that only the state of the first
  settled promise matters. For example, even if other promises given to the
  `promises` array argument are resolved, but the first settled promise has
  become rejected before the other promises became fulfilled, the returned
  promise will become rejected:

  ```javascript
  let promise1 = new Promise(function(resolve, reject){
    setTimeout(function(){
      resolve('promise 1');
    }, 200);
  });

  let promise2 = new Promise(function(resolve, reject){
    setTimeout(function(){
      reject(new Error('promise 2'));
    }, 100);
  });

  Promise.race([promise1, promise2]).then(function(result){
    // Code here never runs
  }, function(reason){
    // reason.message === 'promise 2' because promise 2 became rejected before
    // promise 1 became fulfilled
  });
  ```

  An example real-world use case is implementing timeouts:

  ```javascript
  Promise.race([ajax('foo.json'), timeout(5000)])
  ```

  @method race
  @static
  @param {Array} promises array of promises to observe
  Useful for tooling.
  @return {Promise} a promise which settles in the same way as the first passed
  promise to settle.
*/
function race(entries) {
  /*jshint validthis:true */
  var Constructor = this;

  if (!isArray(entries)) {
    return new Constructor(function (_, reject) {
      return reject(new TypeError('You must pass an array to race.'));
    });
  } else {
    return new Constructor(function (resolve, reject) {
      var length = entries.length;
      for (var i = 0; i < length; i++) {
        Constructor.resolve(entries[i]).then(resolve, reject);
      }
    });
  }
}

/**
  `Promise.reject` returns a promise rejected with the passed `reason`.
  It is shorthand for the following:

  ```javascript
  let promise = new Promise(function(resolve, reject){
    reject(new Error('WHOOPS'));
  });

  promise.then(function(value){
    // Code here doesn't run because the promise is rejected!
  }, function(reason){
    // reason.message === 'WHOOPS'
  });
  ```

  Instead of writing the above, your code now simply becomes the following:

  ```javascript
  let promise = Promise.reject(new Error('WHOOPS'));

  promise.then(function(value){
    // Code here doesn't run because the promise is rejected!
  }, function(reason){
    // reason.message === 'WHOOPS'
  });
  ```

  @method reject
  @static
  @param {Any} reason value that the returned promise will be rejected with.
  Useful for tooling.
  @return {Promise} a promise rejected with the given `reason`.
*/
function reject$1(reason) {
  /*jshint validthis:true */
  var Constructor = this;
  var promise = new Constructor(noop);
  reject(promise, reason);
  return promise;
}

function needsResolver() {
  throw new TypeError('You must pass a resolver function as the first argument to the promise constructor');
}

function needsNew() {
  throw new TypeError("Failed to construct 'Promise': Please use the 'new' operator, this object constructor cannot be called as a function.");
}

/**
  Promise objects represent the eventual result of an asynchronous operation. The
  primary way of interacting with a promise is through its `then` method, which
  registers callbacks to receive either a promise's eventual value or the reason
  why the promise cannot be fulfilled.

  Terminology
  -----------

  - `promise` is an object or function with a `then` method whose behavior conforms to this specification.
  - `thenable` is an object or function that defines a `then` method.
  - `value` is any legal JavaScript value (including undefined, a thenable, or a promise).
  - `exception` is a value that is thrown using the throw statement.
  - `reason` is a value that indicates why a promise was rejected.
  - `settled` the final resting state of a promise, fulfilled or rejected.

  A promise can be in one of three states: pending, fulfilled, or rejected.

  Promises that are fulfilled have a fulfillment value and are in the fulfilled
  state.  Promises that are rejected have a rejection reason and are in the
  rejected state.  A fulfillment value is never a thenable.

  Promises can also be said to *resolve* a value.  If this value is also a
  promise, then the original promise's settled state will match the value's
  settled state.  So a promise that *resolves* a promise that rejects will
  itself reject, and a promise that *resolves* a promise that fulfills will
  itself fulfill.


  Basic Usage:
  ------------

  ```js
  let promise = new Promise(function(resolve, reject) {
    // on success
    resolve(value);

    // on failure
    reject(reason);
  });

  promise.then(function(value) {
    // on fulfillment
  }, function(reason) {
    // on rejection
  });
  ```

  Advanced Usage:
  ---------------

  Promises shine when abstracting away asynchronous interactions such as
  `XMLHttpRequest`s.

  ```js
  function getJSON(url) {
    return new Promise(function(resolve, reject){
      let xhr = new XMLHttpRequest();

      xhr.open('GET', url);
      xhr.onreadystatechange = handler;
      xhr.responseType = 'json';
      xhr.setRequestHeader('Accept', 'application/json');
      xhr.send();

      function handler() {
        if (this.readyState === this.DONE) {
          if (this.status === 200) {
            resolve(this.response);
          } else {
            reject(new Error('getJSON: `' + url + '` failed with status: [' + this.status + ']'));
          }
        }
      };
    });
  }

  getJSON('/posts.json').then(function(json) {
    // on fulfillment
  }, function(reason) {
    // on rejection
  });
  ```

  Unlike callbacks, promises are great composable primitives.

  ```js
  Promise.all([
    getJSON('/posts'),
    getJSON('/comments')
  ]).then(function(values){
    values[0] // => postsJSON
    values[1] // => commentsJSON

    return values;
  });
  ```

  @class Promise
  @param {Function} resolver
  Useful for tooling.
  @constructor
*/

var Promise$1 = function () {
  function Promise(resolver) {
    this[PROMISE_ID] = nextId();
    this._result = this._state = undefined;
    this._subscribers = [];

    if (noop !== resolver) {
      typeof resolver !== 'function' && needsResolver();
      this instanceof Promise ? initializePromise(this, resolver) : needsNew();
    }
  }

  /**
  The primary way of interacting with a promise is through its `then` method,
  which registers callbacks to receive either a promise's eventual value or the
  reason why the promise cannot be fulfilled.
   ```js
  findUser().then(function(user){
    // user is available
  }, function(reason){
    // user is unavailable, and you are given the reason why
  });
  ```
   Chaining
  --------
   The return value of `then` is itself a promise.  This second, 'downstream'
  promise is resolved with the return value of the first promise's fulfillment
  or rejection handler, or rejected if the handler throws an exception.
   ```js
  findUser().then(function (user) {
    return user.name;
  }, function (reason) {
    return 'default name';
  }).then(function (userName) {
    // If `findUser` fulfilled, `userName` will be the user's name, otherwise it
    // will be `'default name'`
  });
   findUser().then(function (user) {
    throw new Error('Found user, but still unhappy');
  }, function (reason) {
    throw new Error('`findUser` rejected and we're unhappy');
  }).then(function (value) {
    // never reached
  }, function (reason) {
    // if `findUser` fulfilled, `reason` will be 'Found user, but still unhappy'.
    // If `findUser` rejected, `reason` will be '`findUser` rejected and we're unhappy'.
  });
  ```
  If the downstream promise does not specify a rejection handler, rejection reasons will be propagated further downstream.
   ```js
  findUser().then(function (user) {
    throw new PedagogicalException('Upstream error');
  }).then(function (value) {
    // never reached
  }).then(function (value) {
    // never reached
  }, function (reason) {
    // The `PedgagocialException` is propagated all the way down to here
  });
  ```
   Assimilation
  ------------
   Sometimes the value you want to propagate to a downstream promise can only be
  retrieved asynchronously. This can be achieved by returning a promise in the
  fulfillment or rejection handler. The downstream promise will then be pending
  until the returned promise is settled. This is called *assimilation*.
   ```js
  findUser().then(function (user) {
    return findCommentsByAuthor(user);
  }).then(function (comments) {
    // The user's comments are now available
  });
  ```
   If the assimliated promise rejects, then the downstream promise will also reject.
   ```js
  findUser().then(function (user) {
    return findCommentsByAuthor(user);
  }).then(function (comments) {
    // If `findCommentsByAuthor` fulfills, we'll have the value here
  }, function (reason) {
    // If `findCommentsByAuthor` rejects, we'll have the reason here
  });
  ```
   Simple Example
  --------------
   Synchronous Example
   ```javascript
  let result;
   try {
    result = findResult();
    // success
  } catch(reason) {
    // failure
  }
  ```
   Errback Example
   ```js
  findResult(function(result, err){
    if (err) {
      // failure
    } else {
      // success
    }
  });
  ```
   Promise Example;
   ```javascript
  findResult().then(function(result){
    // success
  }, function(reason){
    // failure
  });
  ```
   Advanced Example
  --------------
   Synchronous Example
   ```javascript
  let author, books;
   try {
    author = findAuthor();
    books  = findBooksByAuthor(author);
    // success
  } catch(reason) {
    // failure
  }
  ```
   Errback Example
   ```js
   function foundBooks(books) {
   }
   function failure(reason) {
   }
   findAuthor(function(author, err){
    if (err) {
      failure(err);
      // failure
    } else {
      try {
        findBoooksByAuthor(author, function(books, err) {
          if (err) {
            failure(err);
          } else {
            try {
              foundBooks(books);
            } catch(reason) {
              failure(reason);
            }
          }
        });
      } catch(error) {
        failure(err);
      }
      // success
    }
  });
  ```
   Promise Example;
   ```javascript
  findAuthor().
    then(findBooksByAuthor).
    then(function(books){
      // found books
  }).catch(function(reason){
    // something went wrong
  });
  ```
   @method then
  @param {Function} onFulfilled
  @param {Function} onRejected
  Useful for tooling.
  @return {Promise}
  */

  /**
  `catch` is simply sugar for `then(undefined, onRejection)` which makes it the same
  as the catch block of a try/catch statement.
  ```js
  function findAuthor(){
  throw new Error('couldn't find that author');
  }
  // synchronous
  try {
  findAuthor();
  } catch(reason) {
  // something went wrong
  }
  // async with promises
  findAuthor().catch(function(reason){
  // something went wrong
  });
  ```
  @method catch
  @param {Function} onRejection
  Useful for tooling.
  @return {Promise}
  */


  Promise.prototype.catch = function _catch(onRejection) {
    return this.then(null, onRejection);
  };

  /**
    `finally` will be invoked regardless of the promise's fate just as native
    try/catch/finally behaves
  
    Synchronous example:
  
    ```js
    findAuthor() {
      if (Math.random() > 0.5) {
        throw new Error();
      }
      return new Author();
    }
  
    try {
      return findAuthor(); // succeed or fail
    } catch(error) {
      return findOtherAuther();
    } finally {
      // always runs
      // doesn't affect the return value
    }
    ```
  
    Asynchronous example:
  
    ```js
    findAuthor().catch(function(reason){
      return findOtherAuther();
    }).finally(function(){
      // author was either found, or not
    });
    ```
  
    @method finally
    @param {Function} callback
    @return {Promise}
  */


  Promise.prototype.finally = function _finally(callback) {
    var promise = this;
    var constructor = promise.constructor;

    if (isFunction(callback)) {
      return promise.then(function (value) {
        return constructor.resolve(callback()).then(function () {
          return value;
        });
      }, function (reason) {
        return constructor.resolve(callback()).then(function () {
          throw reason;
        });
      });
    }

    return promise.then(callback, callback);
  };

  return Promise;
}();

Promise$1.prototype.then = then;
Promise$1.all = all;
Promise$1.race = race;
Promise$1.resolve = resolve$1;
Promise$1.reject = reject$1;
Promise$1._setScheduler = setScheduler;
Promise$1._setAsap = setAsap;
Promise$1._asap = asap;

/*global self*/
function polyfill() {
  var local = void 0;

  if (typeof global !== 'undefined') {
    local = global;
  } else if (typeof self !== 'undefined') {
    local = self;
  } else {
    try {
      local = Function('return this')();
    } catch (e) {
      throw new Error('polyfill failed because global object is unavailable in this environment');
    }
  }

  var P = local.Promise;

  if (P) {
    var promiseToString = null;
    try {
      promiseToString = Object.prototype.toString.call(P.resolve());
    } catch (e) {
      // silently ignored
    }

    if (promiseToString === '[object Promise]' && !P.cast) {
      return;
    }
  }

  local.Promise = Promise$1;
}

// Strange compat..
Promise$1.polyfill = polyfill;
Promise$1.Promise = Promise$1;

return Promise$1;

})));



//# sourceMappingURL=es6-promise.map

/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(3), __webpack_require__(1)))

/***/ }),
/* 14 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_RESULT__;;(function () {
	'use strict';

	/**
	 * @preserve FastClick: polyfill to remove click delays on browsers with touch UIs.
	 *
	 * @codingstandard ftlabs-jsv2
	 * @copyright The Financial Times Limited [All Rights Reserved]
	 * @license MIT License (see LICENSE.txt)
	 */

	/*jslint browser:true, node:true*/
	/*global define, Event, Node*/


	/**
	 * Instantiate fast-clicking listeners on the specified layer.
	 *
	 * @constructor
	 * @param {Element} layer The layer to listen on
	 * @param {Object} [options={}] The options to override the defaults
	 */
	function FastClick(layer, options) {
		var oldOnClick;

		options = options || {};

		/**
		 * Whether a click is currently being tracked.
		 *
		 * @type boolean
		 */
		this.trackingClick = false;


		/**
		 * Timestamp for when click tracking started.
		 *
		 * @type number
		 */
		this.trackingClickStart = 0;


		/**
		 * The element being tracked for a click.
		 *
		 * @type EventTarget
		 */
		this.targetElement = null;


		/**
		 * X-coordinate of touch start event.
		 *
		 * @type number
		 */
		this.touchStartX = 0;


		/**
		 * Y-coordinate of touch start event.
		 *
		 * @type number
		 */
		this.touchStartY = 0;


		/**
		 * ID of the last touch, retrieved from Touch.identifier.
		 *
		 * @type number
		 */
		this.lastTouchIdentifier = 0;


		/**
		 * Touchmove boundary, beyond which a click will be cancelled.
		 *
		 * @type number
		 */
		this.touchBoundary = options.touchBoundary || 10;


		/**
		 * The FastClick layer.
		 *
		 * @type Element
		 */
		this.layer = layer;

		/**
		 * The minimum time between tap(touchstart and touchend) events
		 *
		 * @type number
		 */
		this.tapDelay = options.tapDelay || 200;

		/**
		 * The maximum time for a tap
		 *
		 * @type number
		 */
		this.tapTimeout = options.tapTimeout || 700;

		if (FastClick.notNeeded(layer)) {
			return;
		}

		// Some old versions of Android don't have Function.prototype.bind
		function bind(method, context) {
			return function() { return method.apply(context, arguments); };
		}


		var methods = ['onMouse', 'onClick', 'onTouchStart', 'onTouchMove', 'onTouchEnd', 'onTouchCancel'];
		var context = this;
		for (var i = 0, l = methods.length; i < l; i++) {
			context[methods[i]] = bind(context[methods[i]], context);
		}

		// Set up event handlers as required
		if (deviceIsAndroid) {
			layer.addEventListener('mouseover', this.onMouse, true);
			layer.addEventListener('mousedown', this.onMouse, true);
			layer.addEventListener('mouseup', this.onMouse, true);
		}

		layer.addEventListener('click', this.onClick, true);
		layer.addEventListener('touchstart', this.onTouchStart, false);
		layer.addEventListener('touchmove', this.onTouchMove, false);
		layer.addEventListener('touchend', this.onTouchEnd, false);
		layer.addEventListener('touchcancel', this.onTouchCancel, false);

		// Hack is required for browsers that don't support Event#stopImmediatePropagation (e.g. Android 2)
		// which is how FastClick normally stops click events bubbling to callbacks registered on the FastClick
		// layer when they are cancelled.
		if (!Event.prototype.stopImmediatePropagation) {
			layer.removeEventListener = function(type, callback, capture) {
				var rmv = Node.prototype.removeEventListener;
				if (type === 'click') {
					rmv.call(layer, type, callback.hijacked || callback, capture);
				} else {
					rmv.call(layer, type, callback, capture);
				}
			};

			layer.addEventListener = function(type, callback, capture) {
				var adv = Node.prototype.addEventListener;
				if (type === 'click') {
					adv.call(layer, type, callback.hijacked || (callback.hijacked = function(event) {
						if (!event.propagationStopped) {
							callback(event);
						}
					}), capture);
				} else {
					adv.call(layer, type, callback, capture);
				}
			};
		}

		// If a handler is already declared in the element's onclick attribute, it will be fired before
		// FastClick's onClick handler. Fix this by pulling out the user-defined handler function and
		// adding it as listener.
		if (typeof layer.onclick === 'function') {

			// Android browser on at least 3.2 requires a new reference to the function in layer.onclick
			// - the old one won't work if passed to addEventListener directly.
			oldOnClick = layer.onclick;
			layer.addEventListener('click', function(event) {
				oldOnClick(event);
			}, false);
			layer.onclick = null;
		}
	}

	/**
	* Windows Phone 8.1 fakes user agent string to look like Android and iPhone.
	*
	* @type boolean
	*/
	var deviceIsWindowsPhone = navigator.userAgent.indexOf("Windows Phone") >= 0;

	/**
	 * Android requires exceptions.
	 *
	 * @type boolean
	 */
	var deviceIsAndroid = navigator.userAgent.indexOf('Android') > 0 && !deviceIsWindowsPhone;


	/**
	 * iOS requires exceptions.
	 *
	 * @type boolean
	 */
	var deviceIsIOS = /iP(ad|hone|od)/.test(navigator.userAgent) && !deviceIsWindowsPhone;


	/**
	 * iOS 4 requires an exception for select elements.
	 *
	 * @type boolean
	 */
	var deviceIsIOS4 = deviceIsIOS && (/OS 4_\d(_\d)?/).test(navigator.userAgent);


	/**
	 * iOS 6.0-7.* requires the target element to be manually derived
	 *
	 * @type boolean
	 */
	var deviceIsIOSWithBadTarget = deviceIsIOS && (/OS [6-7]_\d/).test(navigator.userAgent);

	/**
	 * BlackBerry requires exceptions.
	 *
	 * @type boolean
	 */
	var deviceIsBlackBerry10 = navigator.userAgent.indexOf('BB10') > 0;

	/**
	 * Determine whether a given element requires a native click.
	 *
	 * @param {EventTarget|Element} target Target DOM element
	 * @returns {boolean} Returns true if the element needs a native click
	 */
	FastClick.prototype.needsClick = function(target) {
		switch (target.nodeName.toLowerCase()) {

		// Don't send a synthetic click to disabled inputs (issue #62)
		case 'button':
		case 'select':
		case 'textarea':
			if (target.disabled) {
				return true;
			}

			break;
		case 'input':

			// File inputs need real clicks on iOS 6 due to a browser bug (issue #68)
			if ((deviceIsIOS && target.type === 'file') || target.disabled) {
				return true;
			}

			break;
		case 'label':
		case 'iframe': // iOS8 homescreen apps can prevent events bubbling into frames
		case 'video':
			return true;
		}

		return (/\bneedsclick\b/).test(target.className);
	};


	/**
	 * Determine whether a given element requires a call to focus to simulate click into element.
	 *
	 * @param {EventTarget|Element} target Target DOM element
	 * @returns {boolean} Returns true if the element requires a call to focus to simulate native click.
	 */
	FastClick.prototype.needsFocus = function(target) {
		switch (target.nodeName.toLowerCase()) {
		case 'textarea':
			return true;
		case 'select':
			return !deviceIsAndroid;
		case 'input':
			switch (target.type) {
			case 'button':
			case 'checkbox':
			case 'file':
			case 'image':
			case 'radio':
			case 'submit':
				return false;
			}

			// No point in attempting to focus disabled inputs
			return !target.disabled && !target.readOnly;
		default:
			return (/\bneedsfocus\b/).test(target.className);
		}
	};


	/**
	 * Send a click event to the specified element.
	 *
	 * @param {EventTarget|Element} targetElement
	 * @param {Event} event
	 */
	FastClick.prototype.sendClick = function(targetElement, event) {
		var clickEvent, touch;

		// On some Android devices activeElement needs to be blurred otherwise the synthetic click will have no effect (#24)
		if (document.activeElement && document.activeElement !== targetElement) {
			document.activeElement.blur();
		}

		touch = event.changedTouches[0];

		// Synthesise a click event, with an extra attribute so it can be tracked
		clickEvent = document.createEvent('MouseEvents');
		clickEvent.initMouseEvent(this.determineEventType(targetElement), true, true, window, 1, touch.screenX, touch.screenY, touch.clientX, touch.clientY, false, false, false, false, 0, null);
		clickEvent.forwardedTouchEvent = true;
		targetElement.dispatchEvent(clickEvent);
	};

	FastClick.prototype.determineEventType = function(targetElement) {

		//Issue #159: Android Chrome Select Box does not open with a synthetic click event
		if (deviceIsAndroid && targetElement.tagName.toLowerCase() === 'select') {
			return 'mousedown';
		}

		return 'click';
	};


	/**
	 * @param {EventTarget|Element} targetElement
	 */
	FastClick.prototype.focus = function(targetElement) {
		var length;

		// Issue #160: on iOS 7, some input elements (e.g. date datetime month) throw a vague TypeError on setSelectionRange. These elements don't have an integer value for the selectionStart and selectionEnd properties, but unfortunately that can't be used for detection because accessing the properties also throws a TypeError. Just check the type instead. Filed as Apple bug #15122724.
		if (deviceIsIOS && targetElement.setSelectionRange && targetElement.type.indexOf('date') !== 0 && targetElement.type !== 'time' && targetElement.type !== 'month') {
			length = targetElement.value.length;
			targetElement.setSelectionRange(length, length);
		} else {
			targetElement.focus();
		}
	};


	/**
	 * Check whether the given target element is a child of a scrollable layer and if so, set a flag on it.
	 *
	 * @param {EventTarget|Element} targetElement
	 */
	FastClick.prototype.updateScrollParent = function(targetElement) {
		var scrollParent, parentElement;

		scrollParent = targetElement.fastClickScrollParent;

		// Attempt to discover whether the target element is contained within a scrollable layer. Re-check if the
		// target element was moved to another parent.
		if (!scrollParent || !scrollParent.contains(targetElement)) {
			parentElement = targetElement;
			do {
				if (parentElement.scrollHeight > parentElement.offsetHeight) {
					scrollParent = parentElement;
					targetElement.fastClickScrollParent = parentElement;
					break;
				}

				parentElement = parentElement.parentElement;
			} while (parentElement);
		}

		// Always update the scroll top tracker if possible.
		if (scrollParent) {
			scrollParent.fastClickLastScrollTop = scrollParent.scrollTop;
		}
	};


	/**
	 * @param {EventTarget} targetElement
	 * @returns {Element|EventTarget}
	 */
	FastClick.prototype.getTargetElementFromEventTarget = function(eventTarget) {

		// On some older browsers (notably Safari on iOS 4.1 - see issue #56) the event target may be a text node.
		if (eventTarget.nodeType === Node.TEXT_NODE) {
			return eventTarget.parentNode;
		}

		return eventTarget;
	};


	/**
	 * On touch start, record the position and scroll offset.
	 *
	 * @param {Event} event
	 * @returns {boolean}
	 */
	FastClick.prototype.onTouchStart = function(event) {
		var targetElement, touch, selection;

		// Ignore multiple touches, otherwise pinch-to-zoom is prevented if both fingers are on the FastClick element (issue #111).
		if (event.targetTouches.length > 1) {
			return true;
		}

		targetElement = this.getTargetElementFromEventTarget(event.target);
		touch = event.targetTouches[0];

		if (deviceIsIOS) {

			// Only trusted events will deselect text on iOS (issue #49)
			selection = window.getSelection();
			if (selection.rangeCount && !selection.isCollapsed) {
				return true;
			}

			if (!deviceIsIOS4) {

				// Weird things happen on iOS when an alert or confirm dialog is opened from a click event callback (issue #23):
				// when the user next taps anywhere else on the page, new touchstart and touchend events are dispatched
				// with the same identifier as the touch event that previously triggered the click that triggered the alert.
				// Sadly, there is an issue on iOS 4 that causes some normal touch events to have the same identifier as an
				// immediately preceeding touch event (issue #52), so this fix is unavailable on that platform.
				// Issue 120: touch.identifier is 0 when Chrome dev tools 'Emulate touch events' is set with an iOS device UA string,
				// which causes all touch events to be ignored. As this block only applies to iOS, and iOS identifiers are always long,
				// random integers, it's safe to to continue if the identifier is 0 here.
				if (touch.identifier && touch.identifier === this.lastTouchIdentifier) {
					event.preventDefault();
					return false;
				}

				this.lastTouchIdentifier = touch.identifier;

				// If the target element is a child of a scrollable layer (using -webkit-overflow-scrolling: touch) and:
				// 1) the user does a fling scroll on the scrollable layer
				// 2) the user stops the fling scroll with another tap
				// then the event.target of the last 'touchend' event will be the element that was under the user's finger
				// when the fling scroll was started, causing FastClick to send a click event to that layer - unless a check
				// is made to ensure that a parent layer was not scrolled before sending a synthetic click (issue #42).
				this.updateScrollParent(targetElement);
			}
		}

		this.trackingClick = true;
		this.trackingClickStart = event.timeStamp;
		this.targetElement = targetElement;

		this.touchStartX = touch.pageX;
		this.touchStartY = touch.pageY;

		// Prevent phantom clicks on fast double-tap (issue #36)
		if ((event.timeStamp - this.lastClickTime) < this.tapDelay) {
			event.preventDefault();
		}

		return true;
	};


	/**
	 * Based on a touchmove event object, check whether the touch has moved past a boundary since it started.
	 *
	 * @param {Event} event
	 * @returns {boolean}
	 */
	FastClick.prototype.touchHasMoved = function(event) {
		var touch = event.changedTouches[0], boundary = this.touchBoundary;

		if (Math.abs(touch.pageX - this.touchStartX) > boundary || Math.abs(touch.pageY - this.touchStartY) > boundary) {
			return true;
		}

		return false;
	};


	/**
	 * Update the last position.
	 *
	 * @param {Event} event
	 * @returns {boolean}
	 */
	FastClick.prototype.onTouchMove = function(event) {
		if (!this.trackingClick) {
			return true;
		}

		// If the touch has moved, cancel the click tracking
		if (this.targetElement !== this.getTargetElementFromEventTarget(event.target) || this.touchHasMoved(event)) {
			this.trackingClick = false;
			this.targetElement = null;
		}

		return true;
	};


	/**
	 * Attempt to find the labelled control for the given label element.
	 *
	 * @param {EventTarget|HTMLLabelElement} labelElement
	 * @returns {Element|null}
	 */
	FastClick.prototype.findControl = function(labelElement) {

		// Fast path for newer browsers supporting the HTML5 control attribute
		if (labelElement.control !== undefined) {
			return labelElement.control;
		}

		// All browsers under test that support touch events also support the HTML5 htmlFor attribute
		if (labelElement.htmlFor) {
			return document.getElementById(labelElement.htmlFor);
		}

		// If no for attribute exists, attempt to retrieve the first labellable descendant element
		// the list of which is defined here: http://www.w3.org/TR/html5/forms.html#category-label
		return labelElement.querySelector('button, input:not([type=hidden]), keygen, meter, output, progress, select, textarea');
	};


	/**
	 * On touch end, determine whether to send a click event at once.
	 *
	 * @param {Event} event
	 * @returns {boolean}
	 */
	FastClick.prototype.onTouchEnd = function(event) {
		var forElement, trackingClickStart, targetTagName, scrollParent, touch, targetElement = this.targetElement;

		if (!this.trackingClick) {
			return true;
		}

		// Prevent phantom clicks on fast double-tap (issue #36)
		if ((event.timeStamp - this.lastClickTime) < this.tapDelay) {
			this.cancelNextClick = true;
			return true;
		}

		if ((event.timeStamp - this.trackingClickStart) > this.tapTimeout) {
			return true;
		}

		// Reset to prevent wrong click cancel on input (issue #156).
		this.cancelNextClick = false;

		this.lastClickTime = event.timeStamp;

		trackingClickStart = this.trackingClickStart;
		this.trackingClick = false;
		this.trackingClickStart = 0;

		// On some iOS devices, the targetElement supplied with the event is invalid if the layer
		// is performing a transition or scroll, and has to be re-detected manually. Note that
		// for this to function correctly, it must be called *after* the event target is checked!
		// See issue #57; also filed as rdar://13048589 .
		if (deviceIsIOSWithBadTarget) {
			touch = event.changedTouches[0];

			// In certain cases arguments of elementFromPoint can be negative, so prevent setting targetElement to null
			targetElement = document.elementFromPoint(touch.pageX - window.pageXOffset, touch.pageY - window.pageYOffset) || targetElement;
			targetElement.fastClickScrollParent = this.targetElement.fastClickScrollParent;
		}

		targetTagName = targetElement.tagName.toLowerCase();
		if (targetTagName === 'label') {
			forElement = this.findControl(targetElement);
			if (forElement) {
				this.focus(targetElement);
				if (deviceIsAndroid) {
					return false;
				}

				targetElement = forElement;
			}
		} else if (this.needsFocus(targetElement)) {

			// Case 1: If the touch started a while ago (best guess is 100ms based on tests for issue #36) then focus will be triggered anyway. Return early and unset the target element reference so that the subsequent click will be allowed through.
			// Case 2: Without this exception for input elements tapped when the document is contained in an iframe, then any inputted text won't be visible even though the value attribute is updated as the user types (issue #37).
			if ((event.timeStamp - trackingClickStart) > 100 || (deviceIsIOS && window.top !== window && targetTagName === 'input')) {
				this.targetElement = null;
				return false;
			}

			this.focus(targetElement);
			this.sendClick(targetElement, event);

			// Select elements need the event to go through on iOS 4, otherwise the selector menu won't open.
			// Also this breaks opening selects when VoiceOver is active on iOS6, iOS7 (and possibly others)
			if (!deviceIsIOS || targetTagName !== 'select') {
				this.targetElement = null;
				event.preventDefault();
			}

			return false;
		}

		if (deviceIsIOS && !deviceIsIOS4) {

			// Don't send a synthetic click event if the target element is contained within a parent layer that was scrolled
			// and this tap is being used to stop the scrolling (usually initiated by a fling - issue #42).
			scrollParent = targetElement.fastClickScrollParent;
			if (scrollParent && scrollParent.fastClickLastScrollTop !== scrollParent.scrollTop) {
				return true;
			}
		}

		// Prevent the actual click from going though - unless the target node is marked as requiring
		// real clicks or if it is in the whitelist in which case only non-programmatic clicks are permitted.
		if (!this.needsClick(targetElement)) {
			event.preventDefault();
			this.sendClick(targetElement, event);
		}

		return false;
	};


	/**
	 * On touch cancel, stop tracking the click.
	 *
	 * @returns {void}
	 */
	FastClick.prototype.onTouchCancel = function() {
		this.trackingClick = false;
		this.targetElement = null;
	};


	/**
	 * Determine mouse events which should be permitted.
	 *
	 * @param {Event} event
	 * @returns {boolean}
	 */
	FastClick.prototype.onMouse = function(event) {

		// If a target element was never set (because a touch event was never fired) allow the event
		if (!this.targetElement) {
			return true;
		}

		if (event.forwardedTouchEvent) {
			return true;
		}

		// Programmatically generated events targeting a specific element should be permitted
		if (!event.cancelable) {
			return true;
		}

		// Derive and check the target element to see whether the mouse event needs to be permitted;
		// unless explicitly enabled, prevent non-touch click events from triggering actions,
		// to prevent ghost/doubleclicks.
		if (!this.needsClick(this.targetElement) || this.cancelNextClick) {

			// Prevent any user-added listeners declared on FastClick element from being fired.
			if (event.stopImmediatePropagation) {
				event.stopImmediatePropagation();
			} else {

				// Part of the hack for browsers that don't support Event#stopImmediatePropagation (e.g. Android 2)
				event.propagationStopped = true;
			}

			// Cancel the event
			event.stopPropagation();
			event.preventDefault();

			return false;
		}

		// If the mouse event is permitted, return true for the action to go through.
		return true;
	};


	/**
	 * On actual clicks, determine whether this is a touch-generated click, a click action occurring
	 * naturally after a delay after a touch (which needs to be cancelled to avoid duplication), or
	 * an actual click which should be permitted.
	 *
	 * @param {Event} event
	 * @returns {boolean}
	 */
	FastClick.prototype.onClick = function(event) {
		var permitted;

		// It's possible for another FastClick-like library delivered with third-party code to fire a click event before FastClick does (issue #44). In that case, set the click-tracking flag back to false and return early. This will cause onTouchEnd to return early.
		if (this.trackingClick) {
			this.targetElement = null;
			this.trackingClick = false;
			return true;
		}

		// Very odd behaviour on iOS (issue #18): if a submit element is present inside a form and the user hits enter in the iOS simulator or clicks the Go button on the pop-up OS keyboard the a kind of 'fake' click event will be triggered with the submit-type input element as the target.
		if (event.target.type === 'submit' && event.detail === 0) {
			return true;
		}

		permitted = this.onMouse(event);

		// Only unset targetElement if the click is not permitted. This will ensure that the check for !targetElement in onMouse fails and the browser's click doesn't go through.
		if (!permitted) {
			this.targetElement = null;
		}

		// If clicks are permitted, return true for the action to go through.
		return permitted;
	};


	/**
	 * Remove all FastClick's event listeners.
	 *
	 * @returns {void}
	 */
	FastClick.prototype.destroy = function() {
		var layer = this.layer;

		if (deviceIsAndroid) {
			layer.removeEventListener('mouseover', this.onMouse, true);
			layer.removeEventListener('mousedown', this.onMouse, true);
			layer.removeEventListener('mouseup', this.onMouse, true);
		}

		layer.removeEventListener('click', this.onClick, true);
		layer.removeEventListener('touchstart', this.onTouchStart, false);
		layer.removeEventListener('touchmove', this.onTouchMove, false);
		layer.removeEventListener('touchend', this.onTouchEnd, false);
		layer.removeEventListener('touchcancel', this.onTouchCancel, false);
	};


	/**
	 * Check whether FastClick is needed.
	 *
	 * @param {Element} layer The layer to listen on
	 */
	FastClick.notNeeded = function(layer) {
		var metaViewport;
		var chromeVersion;
		var blackberryVersion;
		var firefoxVersion;

		// Devices that don't support touch don't need FastClick
		if (typeof window.ontouchstart === 'undefined') {
			return true;
		}

		// Chrome version - zero for other browsers
		chromeVersion = +(/Chrome\/([0-9]+)/.exec(navigator.userAgent) || [,0])[1];

		if (chromeVersion) {

			if (deviceIsAndroid) {
				metaViewport = document.querySelector('meta[name=viewport]');

				if (metaViewport) {
					// Chrome on Android with user-scalable="no" doesn't need FastClick (issue #89)
					if (metaViewport.content.indexOf('user-scalable=no') !== -1) {
						return true;
					}
					// Chrome 32 and above with width=device-width or less don't need FastClick
					if (chromeVersion > 31 && document.documentElement.scrollWidth <= window.outerWidth) {
						return true;
					}
				}

			// Chrome desktop doesn't need FastClick (issue #15)
			} else {
				return true;
			}
		}

		if (deviceIsBlackBerry10) {
			blackberryVersion = navigator.userAgent.match(/Version\/([0-9]*)\.([0-9]*)/);

			// BlackBerry 10.3+ does not require Fastclick library.
			// https://github.com/ftlabs/fastclick/issues/251
			if (blackberryVersion[1] >= 10 && blackberryVersion[2] >= 3) {
				metaViewport = document.querySelector('meta[name=viewport]');

				if (metaViewport) {
					// user-scalable=no eliminates click delay.
					if (metaViewport.content.indexOf('user-scalable=no') !== -1) {
						return true;
					}
					// width=device-width (or less than device-width) eliminates click delay.
					if (document.documentElement.scrollWidth <= window.outerWidth) {
						return true;
					}
				}
			}
		}

		// IE10 with -ms-touch-action: none or manipulation, which disables double-tap-to-zoom (issue #97)
		if (layer.style.msTouchAction === 'none' || layer.style.touchAction === 'manipulation') {
			return true;
		}

		// Firefox version - zero for other browsers
		firefoxVersion = +(/Firefox\/([0-9]+)/.exec(navigator.userAgent) || [,0])[1];

		if (firefoxVersion >= 27) {
			// Firefox 27+ does not have tap delay if the content is not zoomable - https://bugzilla.mozilla.org/show_bug.cgi?id=922896

			metaViewport = document.querySelector('meta[name=viewport]');
			if (metaViewport && (metaViewport.content.indexOf('user-scalable=no') !== -1 || document.documentElement.scrollWidth <= window.outerWidth)) {
				return true;
			}
		}

		// IE11: prefixed -ms-touch-action is no longer supported and it's recomended to use non-prefixed version
		// http://msdn.microsoft.com/en-us/library/windows/apps/Hh767313.aspx
		if (layer.style.touchAction === 'none' || layer.style.touchAction === 'manipulation') {
			return true;
		}

		return false;
	};


	/**
	 * Factory method for creating a FastClick object
	 *
	 * @param {Element} layer The layer to listen on
	 * @param {Object} [options={}] The options to override the defaults
	 */
	FastClick.attach = function(layer, options) {
		return new FastClick(layer, options);
	};


	if (true) {

		// AMD. Register as an anonymous module.
		!(__WEBPACK_AMD_DEFINE_RESULT__ = function() {
			return FastClick;
		}.call(exports, __webpack_require__, exports, module),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
	} else if (typeof module !== 'undefined' && module.exports) {
		module.exports = FastClick.attach;
		module.exports.FastClick = FastClick;
	} else {
		window.FastClick = FastClick;
	}
}());


/***/ }),
/* 16 */
/***/ (function(module, exports) {


;(function(win, lib) {
    var doc = win.document;
    var docEl = doc.documentElement;
    var metaEl = doc.querySelector('meta[name="viewport"]');
    var flexibleEl = doc.querySelector('meta[name="flexible"]');
    var dpr = 0;
    var scale = 0;
    var tid;
    var flexible = lib.flexible || (lib.flexible = {});

    if (metaEl) {
        console.warn('将根据已有的meta标签来设置缩放比例');
        var match = metaEl.getAttribute('content').match(/initial\-scale=([\d\.]+)/);
        if (match) {
            scale = parseFloat(match[1]);
            dpr = parseInt(1 / scale);
        }
    } else if (flexibleEl) {
        var content = flexibleEl.getAttribute('content');
        if (content) {
            var initialDpr = content.match(/initial\-dpr=([\d\.]+)/);
            var maximumDpr = content.match(/maximum\-dpr=([\d\.]+)/);
            if (initialDpr) {
                dpr = parseFloat(initialDpr[1]);
                scale = parseFloat((1 / dpr).toFixed(2));
            }
            if (maximumDpr) {
                dpr = parseFloat(maximumDpr[1]);
                scale = parseFloat((1 / dpr).toFixed(2));
            }
        }
    }

    if (!dpr && !scale) {
        var isAndroid = win.navigator.appVersion.match(/android/gi);
        var isIPhone = win.navigator.appVersion.match(/iphone/gi);
        var devicePixelRatio = win.devicePixelRatio;
        if (isIPhone) {
            // iOS下，对于2和3的屏，用2倍的方案，其余的用1倍方案
            if (devicePixelRatio >= 3 && (!dpr || dpr >= 3)) {
                dpr = 3;
            } else if (devicePixelRatio >= 2 && (!dpr || dpr >= 2)){
                dpr = 2;
            } else {
                dpr = 1;
            }
        } else {
            // 其他设备下，仍旧使用1倍的方案
            dpr = 1;
        }
        scale = 1 / dpr;
    }

    docEl.setAttribute('data-dpr', dpr);
    if (!metaEl) {
        metaEl = doc.createElement('meta');
        metaEl.setAttribute('name', 'viewport');
        metaEl.setAttribute('content', 'initial-scale=' + scale + ', maximum-scale=' + scale + ', minimum-scale=' + scale + ', user-scalable=no');
        if (docEl.firstElementChild) {
            docEl.firstElementChild.appendChild(metaEl);
        } else {
            var wrap = doc.createElement('div');
            wrap.appendChild(metaEl);
            doc.write(wrap.innerHTML);
        }
    }

    function refreshRem(){
        var width = docEl.getBoundingClientRect().width;
        if (width / dpr > 540) {
            width = 540 * dpr;
        }
        var rem = width / 10;
        docEl.style.fontSize = rem + 'px';
        flexible.rem = win.rem = rem;
    }

    win.addEventListener('resize', function() {
        clearTimeout(tid);
        tid = setTimeout(refreshRem, 300);
    }, false);
    win.addEventListener('pageshow', function(e) {
        if (e.persisted) {
            clearTimeout(tid);
            tid = setTimeout(refreshRem, 300);
        }
    }, false);

    if (doc.readyState === 'complete') {
        doc.body.style.fontSize = 12 * dpr + 'px';
    } else {
        doc.addEventListener('DOMContentLoaded', function(e) {
            doc.body.style.fontSize = 12 * dpr + 'px';
        }, false);
    }


    refreshRem();

    flexible.dpr = win.dpr = dpr;
    flexible.refreshRem = refreshRem;
    flexible.rem2px = function(d) {
        var val = parseFloat(d) * this.rem;
        if (typeof d === 'string' && d.match(/rem$/)) {
            val += 'px';
        }
        return val;
    }
    flexible.px2rem = function(d) {
        var val = parseFloat(d) / this.rem;
        if (typeof d === 'string' && d.match(/px$/)) {
            val += 'rem';
        }
        return val;
    }

})(window, window['lib'] || (window['lib'] = {}));


/***/ }),
/* 17 */
/***/ (function(module, exports, __webpack_require__) {

/*!
 * Vue-Lazyload.js v1.2.6
 * (c) 2018 Awe <hilongjw@gmail.com>
 * Released under the MIT License.
 */
!function(t,e){ true?module.exports=e():"function"==typeof define&&define.amd?define(e):t.VueLazyload=e()}(this,function(){"use strict";function t(t){return t.constructor&&"function"==typeof t.constructor.isBuffer&&t.constructor.isBuffer(t)}function e(t){t=t||{};var e=arguments.length,i=0;if(1===e)return t;for(;++i<e;){var o=arguments[i];g(t)&&(t=o),r(o)&&n(t,o)}return t}function n(t,n){m(t,n);for(var o in n)if("__proto__"!==o&&i(n,o)){var a=n[o];r(a)?("undefined"===L(t[o])&&"function"===L(a)&&(t[o]=a),t[o]=e(t[o]||{},a)):t[o]=a}return t}function r(t){return"object"===L(t)||"function"===L(t)}function i(t,e){return Object.prototype.hasOwnProperty.call(t,e)}function o(t,e){if(t.length){var n=t.indexOf(e);return n>-1?t.splice(n,1):void 0}}function a(t,e){for(var n=!1,r=0,i=t.length;r<i;r++)if(e(t[r])){n=!0;break}return n}function s(t,e){if("IMG"===t.tagName&&t.getAttribute("data-srcset")){var n=t.getAttribute("data-srcset"),r=[],i=t.parentNode,o=i.offsetWidth*e,a=void 0,s=void 0,u=void 0;n=n.trim().split(","),n.map(function(t){t=t.trim(),a=t.lastIndexOf(" "),-1===a?(s=t,u=999998):(s=t.substr(0,a),u=parseInt(t.substr(a+1,t.length-a-2),10)),r.push([u,s])}),r.sort(function(t,e){if(t[0]<e[0])return-1;if(t[0]>e[0])return 1;if(t[0]===e[0]){if(-1!==e[1].indexOf(".webp",e[1].length-5))return 1;if(-1!==t[1].indexOf(".webp",t[1].length-5))return-1}return 0});for(var l="",d=void 0,c=r.length,h=0;h<c;h++)if(d=r[h],d[0]>=o){l=d[1];break}return l}}function u(t,e){for(var n=void 0,r=0,i=t.length;r<i;r++)if(e(t[r])){n=t[r];break}return n}function l(){if(!E)return!1;var t=!0,e=document;try{var n=e.createElement("object");n.type="image/webp",n.style.visibility="hidden",n.innerHTML="!",e.body.appendChild(n),t=!n.offsetWidth,e.body.removeChild(n)}catch(e){t=!1}return t}function d(t,e){var n=null,r=0;return function(){if(!n){var i=Date.now()-r,o=this,a=arguments,s=function(){r=Date.now(),n=!1,t.apply(o,a)};i>=e?s():n=setTimeout(s,e)}}}function c(t){return null!==t&&"object"===(void 0===t?"undefined":p(t))}function h(t){if(!(t instanceof Object))return[];if(Object.keys)return Object.keys(t);var e=[];for(var n in t)t.hasOwnProperty(n)&&e.push(n);return e}function f(t){for(var e=t.length,n=[],r=0;r<e;r++)n.push(t[r]);return n}function v(){}var p="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(t){return typeof t}:function(t){return t&&"function"==typeof Symbol&&t.constructor===Symbol&&t!==Symbol.prototype?"symbol":typeof t},b=function(t,e){if(!(t instanceof e))throw new TypeError("Cannot call a class as a function")},y=function(){function t(t,e){for(var n=0;n<e.length;n++){var r=e[n];r.enumerable=r.enumerable||!1,r.configurable=!0,"value"in r&&(r.writable=!0),Object.defineProperty(t,r.key,r)}}return function(e,n,r){return n&&t(e.prototype,n),r&&t(e,r),e}}(),g=function(t){return null==t||"function"!=typeof t&&"object"!==(void 0===t?"undefined":p(t))},m=function(t,e){if(null===t||void 0===t)throw new TypeError("expected first argument to be an object.");if(void 0===e||"undefined"==typeof Symbol)return t;if("function"!=typeof Object.getOwnPropertySymbols)return t;for(var n=Object.prototype.propertyIsEnumerable,r=Object(t),i=arguments.length,o=0;++o<i;)for(var a=Object(arguments[o]),s=Object.getOwnPropertySymbols(a),u=0;u<s.length;u++){var l=s[u];n.call(a,l)&&(r[l]=a[l])}return r},w=Object.prototype.toString,L=function(e){var n=void 0===e?"undefined":p(e);return"undefined"===n?"undefined":null===e?"null":!0===e||!1===e||e instanceof Boolean?"boolean":"string"===n||e instanceof String?"string":"number"===n||e instanceof Number?"number":"function"===n||e instanceof Function?void 0!==e.constructor.name&&"Generator"===e.constructor.name.slice(0,9)?"generatorfunction":"function":void 0!==Array.isArray&&Array.isArray(e)?"array":e instanceof RegExp?"regexp":e instanceof Date?"date":(n=w.call(e),"[object RegExp]"===n?"regexp":"[object Date]"===n?"date":"[object Arguments]"===n?"arguments":"[object Error]"===n?"error":"[object Promise]"===n?"promise":t(e)?"buffer":"[object Set]"===n?"set":"[object WeakSet]"===n?"weakset":"[object Map]"===n?"map":"[object WeakMap]"===n?"weakmap":"[object Symbol]"===n?"symbol":"[object Map Iterator]"===n?"mapiterator":"[object Set Iterator]"===n?"setiterator":"[object String Iterator]"===n?"stringiterator":"[object Array Iterator]"===n?"arrayiterator":"[object Int8Array]"===n?"int8array":"[object Uint8Array]"===n?"uint8array":"[object Uint8ClampedArray]"===n?"uint8clampedarray":"[object Int16Array]"===n?"int16array":"[object Uint16Array]"===n?"uint16array":"[object Int32Array]"===n?"int32array":"[object Uint32Array]"===n?"uint32array":"[object Float32Array]"===n?"float32array":"[object Float64Array]"===n?"float64array":"object")},_=e,E="undefined"!=typeof window,A=E&&"IntersectionObserver"in window,k={event:"event",observer:"observer"},z=function(){function t(t,e){e=e||{bubbles:!1,cancelable:!1,detail:void 0};var n=document.createEvent("CustomEvent");return n.initCustomEvent(t,e.bubbles,e.cancelable,e.detail),n}if(E)return"function"==typeof window.CustomEvent?window.CustomEvent:(t.prototype=window.Event.prototype,t)}(),j=function(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:1;return E?window.devicePixelRatio||t:t},T=function(){if(E){var t=!1;try{var e=Object.defineProperty({},"passive",{get:function(){t=!0}});window.addEventListener("test",null,e)}catch(t){}return t}}(),S={on:function(t,e,n){var r=arguments.length>3&&void 0!==arguments[3]&&arguments[3];T?t.addEventListener(e,n,{capture:r,passive:!0}):t.addEventListener(e,n,r)},off:function(t,e,n){var r=arguments.length>3&&void 0!==arguments[3]&&arguments[3];t.removeEventListener(e,n,r)}},I=function(t,e,n){var r=new Image;r.src=t.src,r.onload=function(){e({naturalHeight:r.naturalHeight,naturalWidth:r.naturalWidth,src:r.src})},r.onerror=function(t){n(t)}},O=function(t,e){return"undefined"!=typeof getComputedStyle?getComputedStyle(t,null).getPropertyValue(e):t.style[e]},$=function(t){return O(t,"overflow")+O(t,"overflow-y")+O(t,"overflow-x")},x=function(t){if(E){if(!(t instanceof HTMLElement))return window;for(var e=t;e&&e!==document.body&&e!==document.documentElement&&e.parentNode;){if(/(scroll|auto)/.test($(e)))return e;e=e.parentNode}return window}},H={},Q=function(){function t(e){var n=e.el,r=e.src,i=e.error,o=e.loading,a=e.bindType,s=e.$parent,u=e.options,l=e.elRenderer;b(this,t),this.el=n,this.src=r,this.error=i,this.loading=o,this.bindType=a,this.attempt=0,this.naturalHeight=0,this.naturalWidth=0,this.options=u,this.rect=null,this.$parent=s,this.elRenderer=l,this.performanceData={init:Date.now(),loadStart:0,loadEnd:0},this.filter(),this.initState(),this.render("loading",!1)}return y(t,[{key:"initState",value:function(){"dataset"in this.el?this.el.dataset.src=this.src:this.el.setAttribute("data-src",this.src),this.state={error:!1,loaded:!1,rendered:!1}}},{key:"record",value:function(t){this.performanceData[t]=Date.now()}},{key:"update",value:function(t){var e=t.src,n=t.loading,r=t.error,i=this.src;this.src=e,this.loading=n,this.error=r,this.filter(),i!==this.src&&(this.attempt=0,this.initState())}},{key:"getRect",value:function(){this.rect=this.el.getBoundingClientRect()}},{key:"checkInView",value:function(){return this.getRect(),this.rect.top<window.innerHeight*this.options.preLoad&&this.rect.bottom>this.options.preLoadTop&&this.rect.left<window.innerWidth*this.options.preLoad&&this.rect.right>0}},{key:"filter",value:function(){var t=this;h(this.options.filter).map(function(e){t.options.filter[e](t,t.options)})}},{key:"renderLoading",value:function(t){var e=this;I({src:this.loading},function(n){e.render("loading",!1),t()},function(){t(),e.options.silent||console.warn("VueLazyload log: load failed with loading image("+e.loading+")")})}},{key:"load",value:function(){var t=this,e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:v;return this.attempt>this.options.attempt-1&&this.state.error?(this.options.silent||console.log("VueLazyload log: "+this.src+" tried too more than "+this.options.attempt+" times"),void e()):this.state.loaded||H[this.src]?(this.state.loaded=!0,e(),this.render("loaded",!0)):void this.renderLoading(function(){t.attempt++,t.record("loadStart"),I({src:t.src},function(n){t.naturalHeight=n.naturalHeight,t.naturalWidth=n.naturalWidth,t.state.loaded=!0,t.state.error=!1,t.record("loadEnd"),t.render("loaded",!1),H[t.src]=1,e()},function(e){!t.options.silent&&console.error(e),t.state.error=!0,t.state.loaded=!1,t.render("error",!1)})})}},{key:"render",value:function(t,e){this.elRenderer(this,t,e)}},{key:"performance",value:function(){var t="loading",e=0;return this.state.loaded&&(t="loaded",e=(this.performanceData.loadEnd-this.performanceData.loadStart)/1e3),this.state.error&&(t="error"),{src:this.src,state:t,time:e}}},{key:"destroy",value:function(){this.el=null,this.src=null,this.error=null,this.loading=null,this.bindType=null,this.attempt=0}}]),t}(),C="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7",R=["scroll","wheel","mousewheel","resize","animationend","transitionend","touchmove"],W={rootMargin:"0px",threshold:0},B=function(t){return function(){function e(t){var n=t.preLoad,r=t.error,i=t.throttleWait,o=t.preLoadTop,a=t.dispatchEvent,s=t.loading,u=t.attempt,c=t.silent,h=void 0===c||c,f=t.scale,v=t.listenEvents,p=(t.hasbind,t.filter),y=t.adapter,g=t.observer,m=t.observerOptions;b(this,e),this.version="1.2.6",this.mode=k.event,this.ListenerQueue=[],this.TargetIndex=0,this.TargetQueue=[],this.options={silent:h,dispatchEvent:!!a,throttleWait:i||200,preLoad:n||1.3,preLoadTop:o||0,error:r||C,loading:s||C,attempt:u||3,scale:f||j(f),ListenEvents:v||R,hasbind:!1,supportWebp:l(),filter:p||{},adapter:y||{},observer:!!g,observerOptions:m||W},this._initEvent(),this.lazyLoadHandler=d(this._lazyLoadHandler.bind(this),this.options.throttleWait),this.setMode(this.options.observer?k.observer:k.event)}return y(e,[{key:"config",value:function(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{};_(this.options,t)}},{key:"performance",value:function(){var t=[];return this.ListenerQueue.map(function(e){t.push(e.performance())}),t}},{key:"addLazyBox",value:function(t){this.ListenerQueue.push(t),E&&(this._addListenerTarget(window),this._observer&&this._observer.observe(t.el),t.$el&&t.$el.parentNode&&this._addListenerTarget(t.$el.parentNode))}},{key:"add",value:function(e,n,r){var i=this;if(a(this.ListenerQueue,function(t){return t.el===e}))return this.update(e,n),t.nextTick(this.lazyLoadHandler);var o=this._valueFormatter(n.value),u=o.src,l=o.loading,d=o.error;t.nextTick(function(){u=s(e,i.options.scale)||u,i._observer&&i._observer.observe(e);var o=Object.keys(n.modifiers)[0],a=void 0;o&&(a=r.context.$refs[o],a=a?a.$el||a:document.getElementById(o)),a||(a=x(e));var c=new Q({bindType:n.arg,$parent:a,el:e,loading:l,error:d,src:u,elRenderer:i._elRenderer.bind(i),options:i.options});i.ListenerQueue.push(c),E&&(i._addListenerTarget(window),i._addListenerTarget(a)),i.lazyLoadHandler(),t.nextTick(function(){return i.lazyLoadHandler()})})}},{key:"update",value:function(e,n){var r=this,i=this._valueFormatter(n.value),o=i.src,a=i.loading,l=i.error;o=s(e,this.options.scale)||o;var d=u(this.ListenerQueue,function(t){return t.el===e});d&&d.update({src:o,loading:a,error:l}),this._observer&&(this._observer.unobserve(e),this._observer.observe(e)),this.lazyLoadHandler(),t.nextTick(function(){return r.lazyLoadHandler()})}},{key:"remove",value:function(t){if(t){this._observer&&this._observer.unobserve(t);var e=u(this.ListenerQueue,function(e){return e.el===t});e&&(this._removeListenerTarget(e.$parent),this._removeListenerTarget(window),o(this.ListenerQueue,e)&&e.destroy())}}},{key:"removeComponent",value:function(t){t&&(o(this.ListenerQueue,t),this._observer&&this._observer.unobserve(t.el),t.$parent&&t.$el.parentNode&&this._removeListenerTarget(t.$el.parentNode),this._removeListenerTarget(window))}},{key:"setMode",value:function(t){var e=this;A||t!==k.observer||(t=k.event),this.mode=t,t===k.event?(this._observer&&(this.ListenerQueue.forEach(function(t){e._observer.unobserve(t.el)}),this._observer=null),this.TargetQueue.forEach(function(t){e._initListen(t.el,!0)})):(this.TargetQueue.forEach(function(t){e._initListen(t.el,!1)}),this._initIntersectionObserver())}},{key:"_addListenerTarget",value:function(t){if(t){var e=u(this.TargetQueue,function(e){return e.el===t});return e?e.childrenCount++:(e={el:t,id:++this.TargetIndex,childrenCount:1,listened:!0},this.mode===k.event&&this._initListen(e.el,!0),this.TargetQueue.push(e)),this.TargetIndex}}},{key:"_removeListenerTarget",value:function(t){var e=this;this.TargetQueue.forEach(function(n,r){n.el===t&&(--n.childrenCount||(e._initListen(n.el,!1),e.TargetQueue.splice(r,1),n=null))})}},{key:"_initListen",value:function(t,e){var n=this;this.options.ListenEvents.forEach(function(r){return S[e?"on":"off"](t,r,n.lazyLoadHandler)})}},{key:"_initEvent",value:function(){var t=this;this.Event={listeners:{loading:[],loaded:[],error:[]}},this.$on=function(e,n){t.Event.listeners[e]||(t.Event.listeners[e]=[]),t.Event.listeners[e].push(n)},this.$once=function(e,n){function r(){i.$off(e,r),n.apply(i,arguments)}var i=t;t.$on(e,r)},this.$off=function(e,n){if(!n){if(!t.Event.listeners[e])return;return void(t.Event.listeners[e].length=0)}o(t.Event.listeners[e],n)},this.$emit=function(e,n,r){t.Event.listeners[e]&&t.Event.listeners[e].forEach(function(t){return t(n,r)})}}},{key:"_lazyLoadHandler",value:function(){var t=this,e=[];this.ListenerQueue.forEach(function(t,n){if(!t.state.error&&t.state.loaded)return e.push(t);t.checkInView()&&t.load()}),e.forEach(function(e){return o(t.ListenerQueue,e)})}},{key:"_initIntersectionObserver",value:function(){var t=this;A&&(this._observer=new IntersectionObserver(this._observerHandler.bind(this),this.options.observerOptions),this.ListenerQueue.length&&this.ListenerQueue.forEach(function(e){t._observer.observe(e.el)}))}},{key:"_observerHandler",value:function(t,e){var n=this;t.forEach(function(t){t.isIntersecting&&n.ListenerQueue.forEach(function(e){if(e.el===t.target){if(e.state.loaded)return n._observer.unobserve(e.el);e.load()}})})}},{key:"_elRenderer",value:function(t,e,n){if(t.el){var r=t.el,i=t.bindType,o=void 0;switch(e){case"loading":o=t.loading;break;case"error":o=t.error;break;default:o=t.src}if(i?r.style[i]='url("'+o+'")':r.getAttribute("src")!==o&&r.setAttribute("src",o),r.setAttribute("lazy",e),this.$emit(e,t,n),this.options.adapter[e]&&this.options.adapter[e](t,this.options),this.options.dispatchEvent){var a=new z(e,{detail:t});r.dispatchEvent(a)}}}},{key:"_valueFormatter",value:function(t){var e=t,n=this.options.loading,r=this.options.error;return c(t)&&(t.src||this.options.silent||console.error("Vue Lazyload warning: miss src with "+t),e=t.src,n=t.loading||this.options.loading,r=t.error||this.options.error),{src:e,loading:n,error:r}}}]),e}()},D=function(t){return{props:{tag:{type:String,default:"div"}},render:function(t){return!1===this.show?t(this.tag):t(this.tag,null,this.$slots.default)},data:function(){return{el:null,state:{loaded:!1},rect:{},show:!1}},mounted:function(){this.el=this.$el,t.addLazyBox(this),t.lazyLoadHandler()},beforeDestroy:function(){t.removeComponent(this)},methods:{getRect:function(){this.rect=this.$el.getBoundingClientRect()},checkInView:function(){return this.getRect(),E&&this.rect.top<window.innerHeight*t.options.preLoad&&this.rect.bottom>0&&this.rect.left<window.innerWidth*t.options.preLoad&&this.rect.right>0},load:function(){this.show=!0,this.state.loaded=!0,this.$emit("show",this)}}}},V=function(){function t(e){var n=e.lazy;b(this,t),this.lazy=n,n.lazyContainerMananger=this,this._queue=[]}return y(t,[{key:"bind",value:function(t,e,n){var r=new N({el:t,binding:e,vnode:n,lazy:this.lazy});this._queue.push(r)}},{key:"update",value:function(t,e,n){var r=u(this._queue,function(e){return e.el===t});r&&r.update({el:t,binding:e,vnode:n})}},{key:"unbind",value:function(t,e,n){var r=u(this._queue,function(e){return e.el===t});r&&(r.clear(),o(this._queue,r))}}]),t}(),M={selector:"img"},N=function(){function t(e){var n=e.el,r=e.binding,i=e.vnode,o=e.lazy;b(this,t),this.el=null,this.vnode=i,this.binding=r,this.options={},this.lazy=o,this._queue=[],this.update({el:n,binding:r})}return y(t,[{key:"update",value:function(t){var e=this,n=t.el,r=t.binding;this.el=n,this.options=_({},M,r.value),this.getImgs().forEach(function(t){e.lazy.add(t,_({},e.binding,{value:{src:"dataset"in t?t.dataset.src:t.getAttribute("data-src"),error:"dataset"in t?t.dataset.error:t.getAttribute("data-error"),loading:"dataset"in t?t.dataset.loading:t.getAttribute("data-loading")}}),e.vnode)})}},{key:"getImgs",value:function(){return f(this.el.querySelectorAll(this.options.selector))}},{key:"clear",value:function(){var t=this;this.getImgs().forEach(function(e){return t.lazy.remove(e)}),this.vnode=null,this.binding=null,this.lazy=null}}]),t}(),P=function(t){return{props:{src:[String,Object],tag:{type:String,default:"img"}},render:function(t){return t(this.tag,{attrs:{src:this.renderSrc}},this.$slots.default)},data:function(){return{el:null,options:{src:"",error:"",loading:"",attempt:t.options.attempt},state:{loaded:!1,error:!1,attempt:0},rect:{},renderSrc:""}},watch:{src:function(){this.init(),t.addLazyBox(this),t.lazyLoadHandler()}},created:function(){this.init(),this.renderSrc=this.options.loading},mounted:function(){this.el=this.$el,t.addLazyBox(this),t.lazyLoadHandler()},beforeDestroy:function(){t.removeComponent(this)},methods:{init:function(){var e=t._valueFormatter(this.src),n=e.src,r=e.loading,i=e.error;this.state.loaded=!1,this.options.src=n,this.options.error=i,this.options.loading=r,this.renderSrc=this.options.loading},getRect:function(){this.rect=this.$el.getBoundingClientRect()},checkInView:function(){return this.getRect(),E&&this.rect.top<window.innerHeight*t.options.preLoad&&this.rect.bottom>0&&this.rect.left<window.innerWidth*t.options.preLoad&&this.rect.right>0},load:function(){var e=this,n=arguments.length>0&&void 0!==arguments[0]?arguments[0]:v;if(this.state.attempt>this.options.attempt-1&&this.state.error)return t.options.silent||console.log("VueLazyload log: "+this.options.src+" tried too more than "+this.options.attempt+" times"),void n();var r=this.options.src;I({src:r},function(t){var n=t.src;e.renderSrc=n,e.state.loaded=!0},function(t){e.state.attempt++,e.renderSrc=e.options.error,e.state.error=!0})}}}};return{install:function(t){var e=arguments.length>1&&void 0!==arguments[1]?arguments[1]:{},n=B(t),r=new n(e),i=new V({lazy:r}),o="2"===t.version.split(".")[0];t.prototype.$Lazyload=r,e.lazyComponent&&t.component("lazy-component",D(r)),e.lazyImage&&t.component("lazy-image",P(r)),o?(t.directive("lazy",{bind:r.add.bind(r),update:r.update.bind(r),componentUpdated:r.lazyLoadHandler.bind(r),unbind:r.remove.bind(r)}),t.directive("lazy-container",{bind:i.bind.bind(i),update:i.update.bind(i),unbind:i.unbind.bind(i)})):(t.directive("lazy",{bind:r.lazyLoadHandler.bind(r),update:function(t,e){_(this.vm.$refs,this.vm.$els),r.add(this.el,{modifiers:this.modifiers||{},arg:this.arg,value:t,oldValue:e},{context:this.vm})},unbind:function(){r.remove(this.el)}}),t.directive("lazy-container",{update:function(t,e){i.update(this.el,{modifiers:this.modifiers||{},arg:this.arg,value:t,oldValue:e},{context:this.vm})},unbind:function(){i.unbind(this.el)}}))}}});


/***/ }),
/* 18 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* unused harmony export Url */
/* unused harmony export Http */
/* unused harmony export Resource */
/*!
 * vue-resource v1.5.1
 * https://github.com/pagekit/vue-resource
 * Released under the MIT License.
 */

/**
 * Promises/A+ polyfill v1.1.4 (https://github.com/bramstein/promis)
 */

var RESOLVED = 0;
var REJECTED = 1;
var PENDING = 2;

function Promise$1(executor) {

    this.state = PENDING;
    this.value = undefined;
    this.deferred = [];

    var promise = this;

    try {
        executor(function (x) {
            promise.resolve(x);
        }, function (r) {
            promise.reject(r);
        });
    } catch (e) {
        promise.reject(e);
    }
}

Promise$1.reject = function (r) {
    return new Promise$1(function (resolve, reject) {
        reject(r);
    });
};

Promise$1.resolve = function (x) {
    return new Promise$1(function (resolve, reject) {
        resolve(x);
    });
};

Promise$1.all = function all(iterable) {
    return new Promise$1(function (resolve, reject) {
        var count = 0, result = [];

        if (iterable.length === 0) {
            resolve(result);
        }

        function resolver(i) {
            return function (x) {
                result[i] = x;
                count += 1;

                if (count === iterable.length) {
                    resolve(result);
                }
            };
        }

        for (var i = 0; i < iterable.length; i += 1) {
            Promise$1.resolve(iterable[i]).then(resolver(i), reject);
        }
    });
};

Promise$1.race = function race(iterable) {
    return new Promise$1(function (resolve, reject) {
        for (var i = 0; i < iterable.length; i += 1) {
            Promise$1.resolve(iterable[i]).then(resolve, reject);
        }
    });
};

var p = Promise$1.prototype;

p.resolve = function resolve(x) {
    var promise = this;

    if (promise.state === PENDING) {
        if (x === promise) {
            throw new TypeError('Promise settled with itself.');
        }

        var called = false;

        try {
            var then = x && x['then'];

            if (x !== null && typeof x === 'object' && typeof then === 'function') {
                then.call(x, function (x) {
                    if (!called) {
                        promise.resolve(x);
                    }
                    called = true;

                }, function (r) {
                    if (!called) {
                        promise.reject(r);
                    }
                    called = true;
                });
                return;
            }
        } catch (e) {
            if (!called) {
                promise.reject(e);
            }
            return;
        }

        promise.state = RESOLVED;
        promise.value = x;
        promise.notify();
    }
};

p.reject = function reject(reason) {
    var promise = this;

    if (promise.state === PENDING) {
        if (reason === promise) {
            throw new TypeError('Promise settled with itself.');
        }

        promise.state = REJECTED;
        promise.value = reason;
        promise.notify();
    }
};

p.notify = function notify() {
    var promise = this;

    nextTick(function () {
        if (promise.state !== PENDING) {
            while (promise.deferred.length) {
                var deferred = promise.deferred.shift(),
                    onResolved = deferred[0],
                    onRejected = deferred[1],
                    resolve = deferred[2],
                    reject = deferred[3];

                try {
                    if (promise.state === RESOLVED) {
                        if (typeof onResolved === 'function') {
                            resolve(onResolved.call(undefined, promise.value));
                        } else {
                            resolve(promise.value);
                        }
                    } else if (promise.state === REJECTED) {
                        if (typeof onRejected === 'function') {
                            resolve(onRejected.call(undefined, promise.value));
                        } else {
                            reject(promise.value);
                        }
                    }
                } catch (e) {
                    reject(e);
                }
            }
        }
    });
};

p.then = function then(onResolved, onRejected) {
    var promise = this;

    return new Promise$1(function (resolve, reject) {
        promise.deferred.push([onResolved, onRejected, resolve, reject]);
        promise.notify();
    });
};

p.catch = function (onRejected) {
    return this.then(undefined, onRejected);
};

/**
 * Promise adapter.
 */

if (typeof Promise === 'undefined') {
    window.Promise = Promise$1;
}

function PromiseObj(executor, context) {

    if (executor instanceof Promise) {
        this.promise = executor;
    } else {
        this.promise = new Promise(executor.bind(context));
    }

    this.context = context;
}

PromiseObj.all = function (iterable, context) {
    return new PromiseObj(Promise.all(iterable), context);
};

PromiseObj.resolve = function (value, context) {
    return new PromiseObj(Promise.resolve(value), context);
};

PromiseObj.reject = function (reason, context) {
    return new PromiseObj(Promise.reject(reason), context);
};

PromiseObj.race = function (iterable, context) {
    return new PromiseObj(Promise.race(iterable), context);
};

var p$1 = PromiseObj.prototype;

p$1.bind = function (context) {
    this.context = context;
    return this;
};

p$1.then = function (fulfilled, rejected) {

    if (fulfilled && fulfilled.bind && this.context) {
        fulfilled = fulfilled.bind(this.context);
    }

    if (rejected && rejected.bind && this.context) {
        rejected = rejected.bind(this.context);
    }

    return new PromiseObj(this.promise.then(fulfilled, rejected), this.context);
};

p$1.catch = function (rejected) {

    if (rejected && rejected.bind && this.context) {
        rejected = rejected.bind(this.context);
    }

    return new PromiseObj(this.promise.catch(rejected), this.context);
};

p$1.finally = function (callback) {

    return this.then(function (value) {
        callback.call(this);
        return value;
    }, function (reason) {
        callback.call(this);
        return Promise.reject(reason);
    }
    );
};

/**
 * Utility functions.
 */

var ref = {};
var hasOwnProperty = ref.hasOwnProperty;
var ref$1 = [];
var slice = ref$1.slice;
var debug = false, ntick;

var inBrowser = typeof window !== 'undefined';

function Util (ref) {
    var config = ref.config;
    var nextTick = ref.nextTick;

    ntick = nextTick;
    debug = config.debug || !config.silent;
}

function warn(msg) {
    if (typeof console !== 'undefined' && debug) {
        console.warn('[VueResource warn]: ' + msg);
    }
}

function error(msg) {
    if (typeof console !== 'undefined') {
        console.error(msg);
    }
}

function nextTick(cb, ctx) {
    return ntick(cb, ctx);
}

function trim(str) {
    return str ? str.replace(/^\s*|\s*$/g, '') : '';
}

function trimEnd(str, chars) {

    if (str && chars === undefined) {
        return str.replace(/\s+$/, '');
    }

    if (!str || !chars) {
        return str;
    }

    return str.replace(new RegExp(("[" + chars + "]+$")), '');
}

function toLower(str) {
    return str ? str.toLowerCase() : '';
}

function toUpper(str) {
    return str ? str.toUpperCase() : '';
}

var isArray = Array.isArray;

function isString(val) {
    return typeof val === 'string';
}

function isFunction(val) {
    return typeof val === 'function';
}

function isObject(obj) {
    return obj !== null && typeof obj === 'object';
}

function isPlainObject(obj) {
    return isObject(obj) && Object.getPrototypeOf(obj) == Object.prototype;
}

function isBlob(obj) {
    return typeof Blob !== 'undefined' && obj instanceof Blob;
}

function isFormData(obj) {
    return typeof FormData !== 'undefined' && obj instanceof FormData;
}

function when(value, fulfilled, rejected) {

    var promise = PromiseObj.resolve(value);

    if (arguments.length < 2) {
        return promise;
    }

    return promise.then(fulfilled, rejected);
}

function options(fn, obj, opts) {

    opts = opts || {};

    if (isFunction(opts)) {
        opts = opts.call(obj);
    }

    return merge(fn.bind({$vm: obj, $options: opts}), fn, {$options: opts});
}

function each(obj, iterator) {

    var i, key;

    if (isArray(obj)) {
        for (i = 0; i < obj.length; i++) {
            iterator.call(obj[i], obj[i], i);
        }
    } else if (isObject(obj)) {
        for (key in obj) {
            if (hasOwnProperty.call(obj, key)) {
                iterator.call(obj[key], obj[key], key);
            }
        }
    }

    return obj;
}

var assign = Object.assign || _assign;

function merge(target) {

    var args = slice.call(arguments, 1);

    args.forEach(function (source) {
        _merge(target, source, true);
    });

    return target;
}

function defaults(target) {

    var args = slice.call(arguments, 1);

    args.forEach(function (source) {

        for (var key in source) {
            if (target[key] === undefined) {
                target[key] = source[key];
            }
        }

    });

    return target;
}

function _assign(target) {

    var args = slice.call(arguments, 1);

    args.forEach(function (source) {
        _merge(target, source);
    });

    return target;
}

function _merge(target, source, deep) {
    for (var key in source) {
        if (deep && (isPlainObject(source[key]) || isArray(source[key]))) {
            if (isPlainObject(source[key]) && !isPlainObject(target[key])) {
                target[key] = {};
            }
            if (isArray(source[key]) && !isArray(target[key])) {
                target[key] = [];
            }
            _merge(target[key], source[key], deep);
        } else if (source[key] !== undefined) {
            target[key] = source[key];
        }
    }
}

/**
 * Root Prefix Transform.
 */

function root (options$$1, next) {

    var url = next(options$$1);

    if (isString(options$$1.root) && !/^(https?:)?\//.test(url)) {
        url = trimEnd(options$$1.root, '/') + '/' + url;
    }

    return url;
}

/**
 * Query Parameter Transform.
 */

function query (options$$1, next) {

    var urlParams = Object.keys(Url.options.params), query = {}, url = next(options$$1);

    each(options$$1.params, function (value, key) {
        if (urlParams.indexOf(key) === -1) {
            query[key] = value;
        }
    });

    query = Url.params(query);

    if (query) {
        url += (url.indexOf('?') == -1 ? '?' : '&') + query;
    }

    return url;
}

/**
 * URL Template v2.0.6 (https://github.com/bramstein/url-template)
 */

function expand(url, params, variables) {

    var tmpl = parse(url), expanded = tmpl.expand(params);

    if (variables) {
        variables.push.apply(variables, tmpl.vars);
    }

    return expanded;
}

function parse(template) {

    var operators = ['+', '#', '.', '/', ';', '?', '&'], variables = [];

    return {
        vars: variables,
        expand: function expand(context) {
            return template.replace(/\{([^{}]+)\}|([^{}]+)/g, function (_, expression, literal) {
                if (expression) {

                    var operator = null, values = [];

                    if (operators.indexOf(expression.charAt(0)) !== -1) {
                        operator = expression.charAt(0);
                        expression = expression.substr(1);
                    }

                    expression.split(/,/g).forEach(function (variable) {
                        var tmp = /([^:*]*)(?::(\d+)|(\*))?/.exec(variable);
                        values.push.apply(values, getValues(context, operator, tmp[1], tmp[2] || tmp[3]));
                        variables.push(tmp[1]);
                    });

                    if (operator && operator !== '+') {

                        var separator = ',';

                        if (operator === '?') {
                            separator = '&';
                        } else if (operator !== '#') {
                            separator = operator;
                        }

                        return (values.length !== 0 ? operator : '') + values.join(separator);
                    } else {
                        return values.join(',');
                    }

                } else {
                    return encodeReserved(literal);
                }
            });
        }
    };
}

function getValues(context, operator, key, modifier) {

    var value = context[key], result = [];

    if (isDefined(value) && value !== '') {
        if (typeof value === 'string' || typeof value === 'number' || typeof value === 'boolean') {
            value = value.toString();

            if (modifier && modifier !== '*') {
                value = value.substring(0, parseInt(modifier, 10));
            }

            result.push(encodeValue(operator, value, isKeyOperator(operator) ? key : null));
        } else {
            if (modifier === '*') {
                if (Array.isArray(value)) {
                    value.filter(isDefined).forEach(function (value) {
                        result.push(encodeValue(operator, value, isKeyOperator(operator) ? key : null));
                    });
                } else {
                    Object.keys(value).forEach(function (k) {
                        if (isDefined(value[k])) {
                            result.push(encodeValue(operator, value[k], k));
                        }
                    });
                }
            } else {
                var tmp = [];

                if (Array.isArray(value)) {
                    value.filter(isDefined).forEach(function (value) {
                        tmp.push(encodeValue(operator, value));
                    });
                } else {
                    Object.keys(value).forEach(function (k) {
                        if (isDefined(value[k])) {
                            tmp.push(encodeURIComponent(k));
                            tmp.push(encodeValue(operator, value[k].toString()));
                        }
                    });
                }

                if (isKeyOperator(operator)) {
                    result.push(encodeURIComponent(key) + '=' + tmp.join(','));
                } else if (tmp.length !== 0) {
                    result.push(tmp.join(','));
                }
            }
        }
    } else {
        if (operator === ';') {
            result.push(encodeURIComponent(key));
        } else if (value === '' && (operator === '&' || operator === '?')) {
            result.push(encodeURIComponent(key) + '=');
        } else if (value === '') {
            result.push('');
        }
    }

    return result;
}

function isDefined(value) {
    return value !== undefined && value !== null;
}

function isKeyOperator(operator) {
    return operator === ';' || operator === '&' || operator === '?';
}

function encodeValue(operator, value, key) {

    value = (operator === '+' || operator === '#') ? encodeReserved(value) : encodeURIComponent(value);

    if (key) {
        return encodeURIComponent(key) + '=' + value;
    } else {
        return value;
    }
}

function encodeReserved(str) {
    return str.split(/(%[0-9A-Fa-f]{2})/g).map(function (part) {
        if (!/%[0-9A-Fa-f]/.test(part)) {
            part = encodeURI(part);
        }
        return part;
    }).join('');
}

/**
 * URL Template (RFC 6570) Transform.
 */

function template (options) {

    var variables = [], url = expand(options.url, options.params, variables);

    variables.forEach(function (key) {
        delete options.params[key];
    });

    return url;
}

/**
 * Service for URL templating.
 */

function Url(url, params) {

    var self = this || {}, options$$1 = url, transform;

    if (isString(url)) {
        options$$1 = {url: url, params: params};
    }

    options$$1 = merge({}, Url.options, self.$options, options$$1);

    Url.transforms.forEach(function (handler) {

        if (isString(handler)) {
            handler = Url.transform[handler];
        }

        if (isFunction(handler)) {
            transform = factory(handler, transform, self.$vm);
        }

    });

    return transform(options$$1);
}

/**
 * Url options.
 */

Url.options = {
    url: '',
    root: null,
    params: {}
};

/**
 * Url transforms.
 */

Url.transform = {template: template, query: query, root: root};
Url.transforms = ['template', 'query', 'root'];

/**
 * Encodes a Url parameter string.
 *
 * @param {Object} obj
 */

Url.params = function (obj) {

    var params = [], escape = encodeURIComponent;

    params.add = function (key, value) {

        if (isFunction(value)) {
            value = value();
        }

        if (value === null) {
            value = '';
        }

        this.push(escape(key) + '=' + escape(value));
    };

    serialize(params, obj);

    return params.join('&').replace(/%20/g, '+');
};

/**
 * Parse a URL and return its components.
 *
 * @param {String} url
 */

Url.parse = function (url) {

    var el = document.createElement('a');

    if (document.documentMode) {
        el.href = url;
        url = el.href;
    }

    el.href = url;

    return {
        href: el.href,
        protocol: el.protocol ? el.protocol.replace(/:$/, '') : '',
        port: el.port,
        host: el.host,
        hostname: el.hostname,
        pathname: el.pathname.charAt(0) === '/' ? el.pathname : '/' + el.pathname,
        search: el.search ? el.search.replace(/^\?/, '') : '',
        hash: el.hash ? el.hash.replace(/^#/, '') : ''
    };
};

function factory(handler, next, vm) {
    return function (options$$1) {
        return handler.call(vm, options$$1, next);
    };
}

function serialize(params, obj, scope) {

    var array = isArray(obj), plain = isPlainObject(obj), hash;

    each(obj, function (value, key) {

        hash = isObject(value) || isArray(value);

        if (scope) {
            key = scope + '[' + (plain || hash ? key : '') + ']';
        }

        if (!scope && array) {
            params.add(value.name, value.value);
        } else if (hash) {
            serialize(params, value, key);
        } else {
            params.add(key, value);
        }
    });
}

/**
 * XDomain client (Internet Explorer).
 */

function xdrClient (request) {
    return new PromiseObj(function (resolve) {

        var xdr = new XDomainRequest(), handler = function (ref) {
                var type = ref.type;


                var status = 0;

                if (type === 'load') {
                    status = 200;
                } else if (type === 'error') {
                    status = 500;
                }

                resolve(request.respondWith(xdr.responseText, {status: status}));
            };

        request.abort = function () { return xdr.abort(); };

        xdr.open(request.method, request.getUrl());

        if (request.timeout) {
            xdr.timeout = request.timeout;
        }

        xdr.onload = handler;
        xdr.onabort = handler;
        xdr.onerror = handler;
        xdr.ontimeout = handler;
        xdr.onprogress = function () {};
        xdr.send(request.getBody());
    });
}

/**
 * CORS Interceptor.
 */

var SUPPORTS_CORS = inBrowser && 'withCredentials' in new XMLHttpRequest();

function cors (request) {

    if (inBrowser) {

        var orgUrl = Url.parse(location.href);
        var reqUrl = Url.parse(request.getUrl());

        if (reqUrl.protocol !== orgUrl.protocol || reqUrl.host !== orgUrl.host) {

            request.crossOrigin = true;
            request.emulateHTTP = false;

            if (!SUPPORTS_CORS) {
                request.client = xdrClient;
            }
        }
    }

}

/**
 * Form data Interceptor.
 */

function form (request) {

    if (isFormData(request.body)) {
        request.headers.delete('Content-Type');
    } else if (isObject(request.body) && request.emulateJSON) {
        request.body = Url.params(request.body);
        request.headers.set('Content-Type', 'application/x-www-form-urlencoded');
    }

}

/**
 * JSON Interceptor.
 */

function json (request) {

    var type = request.headers.get('Content-Type') || '';

    if (isObject(request.body) && type.indexOf('application/json') === 0) {
        request.body = JSON.stringify(request.body);
    }

    return function (response) {

        return response.bodyText ? when(response.text(), function (text) {

            var type = response.headers.get('Content-Type') || '';

            if (type.indexOf('application/json') === 0 || isJson(text)) {

                try {
                    response.body = JSON.parse(text);
                } catch (e) {
                    response.body = null;
                }

            } else {
                response.body = text;
            }

            return response;

        }) : response;

    };
}

function isJson(str) {

    var start = str.match(/^\s*(\[|\{)/);
    var end = {'[': /]\s*$/, '{': /}\s*$/};

    return start && end[start[1]].test(str);
}

/**
 * JSONP client (Browser).
 */

function jsonpClient (request) {
    return new PromiseObj(function (resolve) {

        var name = request.jsonp || 'callback', callback = request.jsonpCallback || '_jsonp' + Math.random().toString(36).substr(2), body = null, handler, script;

        handler = function (ref) {
            var type = ref.type;


            var status = 0;

            if (type === 'load' && body !== null) {
                status = 200;
            } else if (type === 'error') {
                status = 500;
            }

            if (status && window[callback]) {
                delete window[callback];
                document.body.removeChild(script);
            }

            resolve(request.respondWith(body, {status: status}));
        };

        window[callback] = function (result) {
            body = JSON.stringify(result);
        };

        request.abort = function () {
            handler({type: 'abort'});
        };

        request.params[name] = callback;

        if (request.timeout) {
            setTimeout(request.abort, request.timeout);
        }

        script = document.createElement('script');
        script.src = request.getUrl();
        script.type = 'text/javascript';
        script.async = true;
        script.onload = handler;
        script.onerror = handler;

        document.body.appendChild(script);
    });
}

/**
 * JSONP Interceptor.
 */

function jsonp (request) {

    if (request.method == 'JSONP') {
        request.client = jsonpClient;
    }

}

/**
 * Before Interceptor.
 */

function before (request) {

    if (isFunction(request.before)) {
        request.before.call(this, request);
    }

}

/**
 * HTTP method override Interceptor.
 */

function method (request) {

    if (request.emulateHTTP && /^(PUT|PATCH|DELETE)$/i.test(request.method)) {
        request.headers.set('X-HTTP-Method-Override', request.method);
        request.method = 'POST';
    }

}

/**
 * Header Interceptor.
 */

function header (request) {

    var headers = assign({}, Http.headers.common,
        !request.crossOrigin ? Http.headers.custom : {},
        Http.headers[toLower(request.method)]
    );

    each(headers, function (value, name) {
        if (!request.headers.has(name)) {
            request.headers.set(name, value);
        }
    });

}

/**
 * XMLHttp client (Browser).
 */

function xhrClient (request) {
    return new PromiseObj(function (resolve) {

        var xhr = new XMLHttpRequest(), handler = function (event) {

                var response = request.respondWith(
                'response' in xhr ? xhr.response : xhr.responseText, {
                    status: xhr.status === 1223 ? 204 : xhr.status, // IE9 status bug
                    statusText: xhr.status === 1223 ? 'No Content' : trim(xhr.statusText)
                });

                each(trim(xhr.getAllResponseHeaders()).split('\n'), function (row) {
                    response.headers.append(row.slice(0, row.indexOf(':')), row.slice(row.indexOf(':') + 1));
                });

                resolve(response);
            };

        request.abort = function () { return xhr.abort(); };

        xhr.open(request.method, request.getUrl(), true);

        if (request.timeout) {
            xhr.timeout = request.timeout;
        }

        if (request.responseType && 'responseType' in xhr) {
            xhr.responseType = request.responseType;
        }

        if (request.withCredentials || request.credentials) {
            xhr.withCredentials = true;
        }

        if (!request.crossOrigin) {
            request.headers.set('X-Requested-With', 'XMLHttpRequest');
        }

        // deprecated use downloadProgress
        if (isFunction(request.progress) && request.method === 'GET') {
            xhr.addEventListener('progress', request.progress);
        }

        if (isFunction(request.downloadProgress)) {
            xhr.addEventListener('progress', request.downloadProgress);
        }

        // deprecated use uploadProgress
        if (isFunction(request.progress) && /^(POST|PUT)$/i.test(request.method)) {
            xhr.upload.addEventListener('progress', request.progress);
        }

        if (isFunction(request.uploadProgress) && xhr.upload) {
            xhr.upload.addEventListener('progress', request.uploadProgress);
        }

        request.headers.forEach(function (value, name) {
            xhr.setRequestHeader(name, value);
        });

        xhr.onload = handler;
        xhr.onabort = handler;
        xhr.onerror = handler;
        xhr.ontimeout = handler;
        xhr.send(request.getBody());
    });
}

/**
 * Http client (Node).
 */

function nodeClient (request) {

    var client = __webpack_require__(55);

    return new PromiseObj(function (resolve) {

        var url = request.getUrl();
        var body = request.getBody();
        var method = request.method;
        var headers = {}, handler;

        request.headers.forEach(function (value, name) {
            headers[name] = value;
        });

        client(url, {body: body, method: method, headers: headers}).then(handler = function (resp) {

            var response = request.respondWith(resp.body, {
                status: resp.statusCode,
                statusText: trim(resp.statusMessage)
            });

            each(resp.headers, function (value, name) {
                response.headers.set(name, value);
            });

            resolve(response);

        }, function (error$$1) { return handler(error$$1.response); });
    });
}

/**
 * Base client.
 */

function Client (context) {

    var reqHandlers = [sendRequest], resHandlers = [];

    if (!isObject(context)) {
        context = null;
    }

    function Client(request) {
        while (reqHandlers.length) {

            var handler = reqHandlers.pop();

            if (isFunction(handler)) {

                var response = (void 0), next = (void 0);

                response = handler.call(context, request, function (val) { return next = val; }) || next;

                if (isObject(response)) {
                    return new PromiseObj(function (resolve, reject) {

                        resHandlers.forEach(function (handler) {
                            response = when(response, function (response) {
                                return handler.call(context, response) || response;
                            }, reject);
                        });

                        when(response, resolve, reject);

                    }, context);
                }

                if (isFunction(response)) {
                    resHandlers.unshift(response);
                }

            } else {
                warn(("Invalid interceptor of type " + (typeof handler) + ", must be a function"));
            }
        }
    }

    Client.use = function (handler) {
        reqHandlers.push(handler);
    };

    return Client;
}

function sendRequest(request) {

    var client = request.client || (inBrowser ? xhrClient : nodeClient);

    return client(request);
}

/**
 * HTTP Headers.
 */

var Headers = function Headers(headers) {
    var this$1 = this;


    this.map = {};

    each(headers, function (value, name) { return this$1.append(name, value); });
};

Headers.prototype.has = function has (name) {
    return getName(this.map, name) !== null;
};

Headers.prototype.get = function get (name) {

    var list = this.map[getName(this.map, name)];

    return list ? list.join() : null;
};

Headers.prototype.getAll = function getAll (name) {
    return this.map[getName(this.map, name)] || [];
};

Headers.prototype.set = function set (name, value) {
    this.map[normalizeName(getName(this.map, name) || name)] = [trim(value)];
};

Headers.prototype.append = function append (name, value) {

    var list = this.map[getName(this.map, name)];

    if (list) {
        list.push(trim(value));
    } else {
        this.set(name, value);
    }
};

Headers.prototype.delete = function delete$1 (name) {
    delete this.map[getName(this.map, name)];
};

Headers.prototype.deleteAll = function deleteAll () {
    this.map = {};
};

Headers.prototype.forEach = function forEach (callback, thisArg) {
        var this$1 = this;

    each(this.map, function (list, name) {
        each(list, function (value) { return callback.call(thisArg, value, name, this$1); });
    });
};

function getName(map, name) {
    return Object.keys(map).reduce(function (prev, curr) {
        return toLower(name) === toLower(curr) ? curr : prev;
    }, null);
}

function normalizeName(name) {

    if (/[^a-z0-9\-#$%&'*+.^_`|~]/i.test(name)) {
        throw new TypeError('Invalid character in header field name');
    }

    return trim(name);
}

/**
 * HTTP Response.
 */

var Response = function Response(body, ref) {
    var url = ref.url;
    var headers = ref.headers;
    var status = ref.status;
    var statusText = ref.statusText;


    this.url = url;
    this.ok = status >= 200 && status < 300;
    this.status = status || 0;
    this.statusText = statusText || '';
    this.headers = new Headers(headers);
    this.body = body;

    if (isString(body)) {

        this.bodyText = body;

    } else if (isBlob(body)) {

        this.bodyBlob = body;

        if (isBlobText(body)) {
            this.bodyText = blobText(body);
        }
    }
};

Response.prototype.blob = function blob () {
    return when(this.bodyBlob);
};

Response.prototype.text = function text () {
    return when(this.bodyText);
};

Response.prototype.json = function json () {
    return when(this.text(), function (text) { return JSON.parse(text); });
};

Object.defineProperty(Response.prototype, 'data', {

    get: function get() {
        return this.body;
    },

    set: function set(body) {
        this.body = body;
    }

});

function blobText(body) {
    return new PromiseObj(function (resolve) {

        var reader = new FileReader();

        reader.readAsText(body);
        reader.onload = function () {
            resolve(reader.result);
        };

    });
}

function isBlobText(body) {
    return body.type.indexOf('text') === 0 || body.type.indexOf('json') !== -1;
}

/**
 * HTTP Request.
 */

var Request = function Request(options$$1) {

    this.body = null;
    this.params = {};

    assign(this, options$$1, {
        method: toUpper(options$$1.method || 'GET')
    });

    if (!(this.headers instanceof Headers)) {
        this.headers = new Headers(this.headers);
    }
};

Request.prototype.getUrl = function getUrl () {
    return Url(this);
};

Request.prototype.getBody = function getBody () {
    return this.body;
};

Request.prototype.respondWith = function respondWith (body, options$$1) {
    return new Response(body, assign(options$$1 || {}, {url: this.getUrl()}));
};

/**
 * Service for sending network requests.
 */

var COMMON_HEADERS = {'Accept': 'application/json, text/plain, */*'};
var JSON_CONTENT_TYPE = {'Content-Type': 'application/json;charset=utf-8'};

function Http(options$$1) {

    var self = this || {}, client = Client(self.$vm);

    defaults(options$$1 || {}, self.$options, Http.options);

    Http.interceptors.forEach(function (handler) {

        if (isString(handler)) {
            handler = Http.interceptor[handler];
        }

        if (isFunction(handler)) {
            client.use(handler);
        }

    });

    return client(new Request(options$$1)).then(function (response) {

        return response.ok ? response : PromiseObj.reject(response);

    }, function (response) {

        if (response instanceof Error) {
            error(response);
        }

        return PromiseObj.reject(response);
    });
}

Http.options = {};

Http.headers = {
    put: JSON_CONTENT_TYPE,
    post: JSON_CONTENT_TYPE,
    patch: JSON_CONTENT_TYPE,
    delete: JSON_CONTENT_TYPE,
    common: COMMON_HEADERS,
    custom: {}
};

Http.interceptor = {before: before, method: method, jsonp: jsonp, json: json, form: form, header: header, cors: cors};
Http.interceptors = ['before', 'method', 'jsonp', 'json', 'form', 'header', 'cors'];

['get', 'delete', 'head', 'jsonp'].forEach(function (method$$1) {

    Http[method$$1] = function (url, options$$1) {
        return this(assign(options$$1 || {}, {url: url, method: method$$1}));
    };

});

['post', 'put', 'patch'].forEach(function (method$$1) {

    Http[method$$1] = function (url, body, options$$1) {
        return this(assign(options$$1 || {}, {url: url, method: method$$1, body: body}));
    };

});

/**
 * Service for interacting with RESTful services.
 */

function Resource(url, params, actions, options$$1) {

    var self = this || {}, resource = {};

    actions = assign({},
        Resource.actions,
        actions
    );

    each(actions, function (action, name) {

        action = merge({url: url, params: assign({}, params)}, options$$1, action);

        resource[name] = function () {
            return (self.$http || Http)(opts(action, arguments));
        };
    });

    return resource;
}

function opts(action, args) {

    var options$$1 = assign({}, action), params = {}, body;

    switch (args.length) {

        case 2:

            params = args[0];
            body = args[1];

            break;

        case 1:

            if (/^(POST|PUT|PATCH)$/i.test(options$$1.method)) {
                body = args[0];
            } else {
                params = args[0];
            }

            break;

        case 0:

            break;

        default:

            throw 'Expected up to 2 arguments [params, body], got ' + args.length + ' arguments';
    }

    options$$1.body = body;
    options$$1.params = assign({}, options$$1.params, params);

    return options$$1;
}

Resource.actions = {

    get: {method: 'GET'},
    save: {method: 'POST'},
    query: {method: 'GET'},
    update: {method: 'PUT'},
    remove: {method: 'DELETE'},
    delete: {method: 'DELETE'}

};

/**
 * Install plugin.
 */

function plugin(Vue) {

    if (plugin.installed) {
        return;
    }

    Util(Vue);

    Vue.url = Url;
    Vue.http = Http;
    Vue.resource = Resource;
    Vue.Promise = PromiseObj;

    Object.defineProperties(Vue.prototype, {

        $url: {
            get: function get() {
                return options(Vue.url, this, this.$options.url);
            }
        },

        $http: {
            get: function get() {
                return options(Vue.http, this, this.$options.http);
            }
        },

        $resource: {
            get: function get() {
                return Vue.resource.bind(this);
            }
        },

        $promise: {
            get: function get() {
                var this$1 = this;

                return function (executor) { return new Vue.Promise(executor, this$1); };
            }
        }

    });
}

if (typeof window !== 'undefined' && window.Vue) {
    window.Vue.use(plugin);
}

/* harmony default export */ __webpack_exports__["a"] = (plugin);



/***/ }),
/* 19 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/**
  * vue-router v2.8.1
  * (c) 2017 Evan You
  * @license MIT
  */
/*  */

function assert (condition, message) {
  if (!condition) {
    throw new Error(("[vue-router] " + message))
  }
}

function warn (condition, message) {
  if (false) {
    typeof console !== 'undefined' && console.warn(("[vue-router] " + message));
  }
}

function isError (err) {
  return Object.prototype.toString.call(err).indexOf('Error') > -1
}

var View = {
  name: 'router-view',
  functional: true,
  props: {
    name: {
      type: String,
      default: 'default'
    }
  },
  render: function render (_, ref) {
    var props = ref.props;
    var children = ref.children;
    var parent = ref.parent;
    var data = ref.data;

    data.routerView = true;

    // directly use parent context's createElement() function
    // so that components rendered by router-view can resolve named slots
    var h = parent.$createElement;
    var name = props.name;
    var route = parent.$route;
    var cache = parent._routerViewCache || (parent._routerViewCache = {});

    // determine current view depth, also check to see if the tree
    // has been toggled inactive but kept-alive.
    var depth = 0;
    var inactive = false;
    while (parent && parent._routerRoot !== parent) {
      if (parent.$vnode && parent.$vnode.data.routerView) {
        depth++;
      }
      if (parent._inactive) {
        inactive = true;
      }
      parent = parent.$parent;
    }
    data.routerViewDepth = depth;

    // render previous view if the tree is inactive and kept-alive
    if (inactive) {
      return h(cache[name], data, children)
    }

    var matched = route.matched[depth];
    // render empty node if no matched route
    if (!matched) {
      cache[name] = null;
      return h()
    }

    var component = cache[name] = matched.components[name];

    // attach instance registration hook
    // this will be called in the instance's injected lifecycle hooks
    data.registerRouteInstance = function (vm, val) {
      // val could be undefined for unregistration
      var current = matched.instances[name];
      if (
        (val && current !== vm) ||
        (!val && current === vm)
      ) {
        matched.instances[name] = val;
      }
    }

    // also register instance in prepatch hook
    // in case the same component instance is reused across different routes
    ;(data.hook || (data.hook = {})).prepatch = function (_, vnode) {
      matched.instances[name] = vnode.componentInstance;
    };

    // resolve props
    var propsToPass = data.props = resolveProps(route, matched.props && matched.props[name]);
    if (propsToPass) {
      // clone to prevent mutation
      propsToPass = data.props = extend({}, propsToPass);
      // pass non-declared props as attrs
      var attrs = data.attrs = data.attrs || {};
      for (var key in propsToPass) {
        if (!component.props || !(key in component.props)) {
          attrs[key] = propsToPass[key];
          delete propsToPass[key];
        }
      }
    }

    return h(component, data, children)
  }
};

function resolveProps (route, config) {
  switch (typeof config) {
    case 'undefined':
      return
    case 'object':
      return config
    case 'function':
      return config(route)
    case 'boolean':
      return config ? route.params : undefined
    default:
      if (false) {
        warn(
          false,
          "props in \"" + (route.path) + "\" is a " + (typeof config) + ", " +
          "expecting an object, function or boolean."
        );
      }
  }
}

function extend (to, from) {
  for (var key in from) {
    to[key] = from[key];
  }
  return to
}

/*  */

var encodeReserveRE = /[!'()*]/g;
var encodeReserveReplacer = function (c) { return '%' + c.charCodeAt(0).toString(16); };
var commaRE = /%2C/g;

// fixed encodeURIComponent which is more conformant to RFC3986:
// - escapes [!'()*]
// - preserve commas
var encode = function (str) { return encodeURIComponent(str)
  .replace(encodeReserveRE, encodeReserveReplacer)
  .replace(commaRE, ','); };

var decode = decodeURIComponent;

function resolveQuery (
  query,
  extraQuery,
  _parseQuery
) {
  if ( extraQuery === void 0 ) extraQuery = {};

  var parse = _parseQuery || parseQuery;
  var parsedQuery;
  try {
    parsedQuery = parse(query || '');
  } catch (e) {
    "production" !== 'production' && warn(false, e.message);
    parsedQuery = {};
  }
  for (var key in extraQuery) {
    parsedQuery[key] = extraQuery[key];
  }
  return parsedQuery
}

function parseQuery (query) {
  var res = {};

  query = query.trim().replace(/^(\?|#|&)/, '');

  if (!query) {
    return res
  }

  query.split('&').forEach(function (param) {
    var parts = param.replace(/\+/g, ' ').split('=');
    var key = decode(parts.shift());
    var val = parts.length > 0
      ? decode(parts.join('='))
      : null;

    if (res[key] === undefined) {
      res[key] = val;
    } else if (Array.isArray(res[key])) {
      res[key].push(val);
    } else {
      res[key] = [res[key], val];
    }
  });

  return res
}

function stringifyQuery (obj) {
  var res = obj ? Object.keys(obj).map(function (key) {
    var val = obj[key];

    if (val === undefined) {
      return ''
    }

    if (val === null) {
      return encode(key)
    }

    if (Array.isArray(val)) {
      var result = [];
      val.forEach(function (val2) {
        if (val2 === undefined) {
          return
        }
        if (val2 === null) {
          result.push(encode(key));
        } else {
          result.push(encode(key) + '=' + encode(val2));
        }
      });
      return result.join('&')
    }

    return encode(key) + '=' + encode(val)
  }).filter(function (x) { return x.length > 0; }).join('&') : null;
  return res ? ("?" + res) : ''
}

/*  */


var trailingSlashRE = /\/?$/;

function createRoute (
  record,
  location,
  redirectedFrom,
  router
) {
  var stringifyQuery$$1 = router && router.options.stringifyQuery;

  var query = location.query || {};
  try {
    query = clone(query);
  } catch (e) {}

  var route = {
    name: location.name || (record && record.name),
    meta: (record && record.meta) || {},
    path: location.path || '/',
    hash: location.hash || '',
    query: query,
    params: location.params || {},
    fullPath: getFullPath(location, stringifyQuery$$1),
    matched: record ? formatMatch(record) : []
  };
  if (redirectedFrom) {
    route.redirectedFrom = getFullPath(redirectedFrom, stringifyQuery$$1);
  }
  return Object.freeze(route)
}

function clone (value) {
  if (Array.isArray(value)) {
    return value.map(clone)
  } else if (value && typeof value === 'object') {
    var res = {};
    for (var key in value) {
      res[key] = clone(value[key]);
    }
    return res
  } else {
    return value
  }
}

// the starting route that represents the initial state
var START = createRoute(null, {
  path: '/'
});

function formatMatch (record) {
  var res = [];
  while (record) {
    res.unshift(record);
    record = record.parent;
  }
  return res
}

function getFullPath (
  ref,
  _stringifyQuery
) {
  var path = ref.path;
  var query = ref.query; if ( query === void 0 ) query = {};
  var hash = ref.hash; if ( hash === void 0 ) hash = '';

  var stringify = _stringifyQuery || stringifyQuery;
  return (path || '/') + stringify(query) + hash
}

function isSameRoute (a, b) {
  if (b === START) {
    return a === b
  } else if (!b) {
    return false
  } else if (a.path && b.path) {
    return (
      a.path.replace(trailingSlashRE, '') === b.path.replace(trailingSlashRE, '') &&
      a.hash === b.hash &&
      isObjectEqual(a.query, b.query)
    )
  } else if (a.name && b.name) {
    return (
      a.name === b.name &&
      a.hash === b.hash &&
      isObjectEqual(a.query, b.query) &&
      isObjectEqual(a.params, b.params)
    )
  } else {
    return false
  }
}

function isObjectEqual (a, b) {
  if ( a === void 0 ) a = {};
  if ( b === void 0 ) b = {};

  // handle null value #1566
  if (!a || !b) { return a === b }
  var aKeys = Object.keys(a);
  var bKeys = Object.keys(b);
  if (aKeys.length !== bKeys.length) {
    return false
  }
  return aKeys.every(function (key) {
    var aVal = a[key];
    var bVal = b[key];
    // check nested equality
    if (typeof aVal === 'object' && typeof bVal === 'object') {
      return isObjectEqual(aVal, bVal)
    }
    return String(aVal) === String(bVal)
  })
}

function isIncludedRoute (current, target) {
  return (
    current.path.replace(trailingSlashRE, '/').indexOf(
      target.path.replace(trailingSlashRE, '/')
    ) === 0 &&
    (!target.hash || current.hash === target.hash) &&
    queryIncludes(current.query, target.query)
  )
}

function queryIncludes (current, target) {
  for (var key in target) {
    if (!(key in current)) {
      return false
    }
  }
  return true
}

/*  */

// work around weird flow bug
var toTypes = [String, Object];
var eventTypes = [String, Array];

var Link = {
  name: 'router-link',
  props: {
    to: {
      type: toTypes,
      required: true
    },
    tag: {
      type: String,
      default: 'a'
    },
    exact: Boolean,
    append: Boolean,
    replace: Boolean,
    activeClass: String,
    exactActiveClass: String,
    event: {
      type: eventTypes,
      default: 'click'
    }
  },
  render: function render (h) {
    var this$1 = this;

    var router = this.$router;
    var current = this.$route;
    var ref = router.resolve(this.to, current, this.append);
    var location = ref.location;
    var route = ref.route;
    var href = ref.href;

    var classes = {};
    var globalActiveClass = router.options.linkActiveClass;
    var globalExactActiveClass = router.options.linkExactActiveClass;
    // Support global empty active class
    var activeClassFallback = globalActiveClass == null
            ? 'router-link-active'
            : globalActiveClass;
    var exactActiveClassFallback = globalExactActiveClass == null
            ? 'router-link-exact-active'
            : globalExactActiveClass;
    var activeClass = this.activeClass == null
            ? activeClassFallback
            : this.activeClass;
    var exactActiveClass = this.exactActiveClass == null
            ? exactActiveClassFallback
            : this.exactActiveClass;
    var compareTarget = location.path
      ? createRoute(null, location, null, router)
      : route;

    classes[exactActiveClass] = isSameRoute(current, compareTarget);
    classes[activeClass] = this.exact
      ? classes[exactActiveClass]
      : isIncludedRoute(current, compareTarget);

    var handler = function (e) {
      if (guardEvent(e)) {
        if (this$1.replace) {
          router.replace(location);
        } else {
          router.push(location);
        }
      }
    };

    var on = { click: guardEvent };
    if (Array.isArray(this.event)) {
      this.event.forEach(function (e) { on[e] = handler; });
    } else {
      on[this.event] = handler;
    }

    var data = {
      class: classes
    };

    if (this.tag === 'a') {
      data.on = on;
      data.attrs = { href: href };
    } else {
      // find the first <a> child and apply listener and href
      var a = findAnchor(this.$slots.default);
      if (a) {
        // in case the <a> is a static node
        a.isStatic = false;
        var extend = _Vue.util.extend;
        var aData = a.data = extend({}, a.data);
        aData.on = on;
        var aAttrs = a.data.attrs = extend({}, a.data.attrs);
        aAttrs.href = href;
      } else {
        // doesn't have <a> child, apply listener to self
        data.on = on;
      }
    }

    return h(this.tag, data, this.$slots.default)
  }
};

function guardEvent (e) {
  // don't redirect with control keys
  if (e.metaKey || e.altKey || e.ctrlKey || e.shiftKey) { return }
  // don't redirect when preventDefault called
  if (e.defaultPrevented) { return }
  // don't redirect on right click
  if (e.button !== undefined && e.button !== 0) { return }
  // don't redirect if `target="_blank"`
  if (e.currentTarget && e.currentTarget.getAttribute) {
    var target = e.currentTarget.getAttribute('target');
    if (/\b_blank\b/i.test(target)) { return }
  }
  // this may be a Weex event which doesn't have this method
  if (e.preventDefault) {
    e.preventDefault();
  }
  return true
}

function findAnchor (children) {
  if (children) {
    var child;
    for (var i = 0; i < children.length; i++) {
      child = children[i];
      if (child.tag === 'a') {
        return child
      }
      if (child.children && (child = findAnchor(child.children))) {
        return child
      }
    }
  }
}

var _Vue;

function install (Vue) {
  if (install.installed && _Vue === Vue) { return }
  install.installed = true;

  _Vue = Vue;

  var isDef = function (v) { return v !== undefined; };

  var registerInstance = function (vm, callVal) {
    var i = vm.$options._parentVnode;
    if (isDef(i) && isDef(i = i.data) && isDef(i = i.registerRouteInstance)) {
      i(vm, callVal);
    }
  };

  Vue.mixin({
    beforeCreate: function beforeCreate () {
      if (isDef(this.$options.router)) {
        this._routerRoot = this;
        this._router = this.$options.router;
        this._router.init(this);
        Vue.util.defineReactive(this, '_route', this._router.history.current);
      } else {
        this._routerRoot = (this.$parent && this.$parent._routerRoot) || this;
      }
      registerInstance(this, this);
    },
    destroyed: function destroyed () {
      registerInstance(this);
    }
  });

  Object.defineProperty(Vue.prototype, '$router', {
    get: function get () { return this._routerRoot._router }
  });

  Object.defineProperty(Vue.prototype, '$route', {
    get: function get () { return this._routerRoot._route }
  });

  Vue.component('router-view', View);
  Vue.component('router-link', Link);

  var strats = Vue.config.optionMergeStrategies;
  // use the same hook merging strategy for route hooks
  strats.beforeRouteEnter = strats.beforeRouteLeave = strats.beforeRouteUpdate = strats.created;
}

/*  */

var inBrowser = typeof window !== 'undefined';

/*  */

function resolvePath (
  relative,
  base,
  append
) {
  var firstChar = relative.charAt(0);
  if (firstChar === '/') {
    return relative
  }

  if (firstChar === '?' || firstChar === '#') {
    return base + relative
  }

  var stack = base.split('/');

  // remove trailing segment if:
  // - not appending
  // - appending to trailing slash (last segment is empty)
  if (!append || !stack[stack.length - 1]) {
    stack.pop();
  }

  // resolve relative path
  var segments = relative.replace(/^\//, '').split('/');
  for (var i = 0; i < segments.length; i++) {
    var segment = segments[i];
    if (segment === '..') {
      stack.pop();
    } else if (segment !== '.') {
      stack.push(segment);
    }
  }

  // ensure leading slash
  if (stack[0] !== '') {
    stack.unshift('');
  }

  return stack.join('/')
}

function parsePath (path) {
  var hash = '';
  var query = '';

  var hashIndex = path.indexOf('#');
  if (hashIndex >= 0) {
    hash = path.slice(hashIndex);
    path = path.slice(0, hashIndex);
  }

  var queryIndex = path.indexOf('?');
  if (queryIndex >= 0) {
    query = path.slice(queryIndex + 1);
    path = path.slice(0, queryIndex);
  }

  return {
    path: path,
    query: query,
    hash: hash
  }
}

function cleanPath (path) {
  return path.replace(/\/\//g, '/')
}

var isarray = Array.isArray || function (arr) {
  return Object.prototype.toString.call(arr) == '[object Array]';
};

/**
 * Expose `pathToRegexp`.
 */
var pathToRegexp_1 = pathToRegexp;
var parse_1 = parse;
var compile_1 = compile;
var tokensToFunction_1 = tokensToFunction;
var tokensToRegExp_1 = tokensToRegExp;

/**
 * The main path matching regexp utility.
 *
 * @type {RegExp}
 */
var PATH_REGEXP = new RegExp([
  // Match escaped characters that would otherwise appear in future matches.
  // This allows the user to escape special characters that won't transform.
  '(\\\\.)',
  // Match Express-style parameters and un-named parameters with a prefix
  // and optional suffixes. Matches appear as:
  //
  // "/:test(\\d+)?" => ["/", "test", "\d+", undefined, "?", undefined]
  // "/route(\\d+)"  => [undefined, undefined, undefined, "\d+", undefined, undefined]
  // "/*"            => ["/", undefined, undefined, undefined, undefined, "*"]
  '([\\/.])?(?:(?:\\:(\\w+)(?:\\(((?:\\\\.|[^\\\\()])+)\\))?|\\(((?:\\\\.|[^\\\\()])+)\\))([+*?])?|(\\*))'
].join('|'), 'g');

/**
 * Parse a string for the raw tokens.
 *
 * @param  {string}  str
 * @param  {Object=} options
 * @return {!Array}
 */
function parse (str, options) {
  var tokens = [];
  var key = 0;
  var index = 0;
  var path = '';
  var defaultDelimiter = options && options.delimiter || '/';
  var res;

  while ((res = PATH_REGEXP.exec(str)) != null) {
    var m = res[0];
    var escaped = res[1];
    var offset = res.index;
    path += str.slice(index, offset);
    index = offset + m.length;

    // Ignore already escaped sequences.
    if (escaped) {
      path += escaped[1];
      continue
    }

    var next = str[index];
    var prefix = res[2];
    var name = res[3];
    var capture = res[4];
    var group = res[5];
    var modifier = res[6];
    var asterisk = res[7];

    // Push the current path onto the tokens.
    if (path) {
      tokens.push(path);
      path = '';
    }

    var partial = prefix != null && next != null && next !== prefix;
    var repeat = modifier === '+' || modifier === '*';
    var optional = modifier === '?' || modifier === '*';
    var delimiter = res[2] || defaultDelimiter;
    var pattern = capture || group;

    tokens.push({
      name: name || key++,
      prefix: prefix || '',
      delimiter: delimiter,
      optional: optional,
      repeat: repeat,
      partial: partial,
      asterisk: !!asterisk,
      pattern: pattern ? escapeGroup(pattern) : (asterisk ? '.*' : '[^' + escapeString(delimiter) + ']+?')
    });
  }

  // Match any characters still remaining.
  if (index < str.length) {
    path += str.substr(index);
  }

  // If the path exists, push it onto the end.
  if (path) {
    tokens.push(path);
  }

  return tokens
}

/**
 * Compile a string to a template function for the path.
 *
 * @param  {string}             str
 * @param  {Object=}            options
 * @return {!function(Object=, Object=)}
 */
function compile (str, options) {
  return tokensToFunction(parse(str, options))
}

/**
 * Prettier encoding of URI path segments.
 *
 * @param  {string}
 * @return {string}
 */
function encodeURIComponentPretty (str) {
  return encodeURI(str).replace(/[\/?#]/g, function (c) {
    return '%' + c.charCodeAt(0).toString(16).toUpperCase()
  })
}

/**
 * Encode the asterisk parameter. Similar to `pretty`, but allows slashes.
 *
 * @param  {string}
 * @return {string}
 */
function encodeAsterisk (str) {
  return encodeURI(str).replace(/[?#]/g, function (c) {
    return '%' + c.charCodeAt(0).toString(16).toUpperCase()
  })
}

/**
 * Expose a method for transforming tokens into the path function.
 */
function tokensToFunction (tokens) {
  // Compile all the tokens into regexps.
  var matches = new Array(tokens.length);

  // Compile all the patterns before compilation.
  for (var i = 0; i < tokens.length; i++) {
    if (typeof tokens[i] === 'object') {
      matches[i] = new RegExp('^(?:' + tokens[i].pattern + ')$');
    }
  }

  return function (obj, opts) {
    var path = '';
    var data = obj || {};
    var options = opts || {};
    var encode = options.pretty ? encodeURIComponentPretty : encodeURIComponent;

    for (var i = 0; i < tokens.length; i++) {
      var token = tokens[i];

      if (typeof token === 'string') {
        path += token;

        continue
      }

      var value = data[token.name];
      var segment;

      if (value == null) {
        if (token.optional) {
          // Prepend partial segment prefixes.
          if (token.partial) {
            path += token.prefix;
          }

          continue
        } else {
          throw new TypeError('Expected "' + token.name + '" to be defined')
        }
      }

      if (isarray(value)) {
        if (!token.repeat) {
          throw new TypeError('Expected "' + token.name + '" to not repeat, but received `' + JSON.stringify(value) + '`')
        }

        if (value.length === 0) {
          if (token.optional) {
            continue
          } else {
            throw new TypeError('Expected "' + token.name + '" to not be empty')
          }
        }

        for (var j = 0; j < value.length; j++) {
          segment = encode(value[j]);

          if (!matches[i].test(segment)) {
            throw new TypeError('Expected all "' + token.name + '" to match "' + token.pattern + '", but received `' + JSON.stringify(segment) + '`')
          }

          path += (j === 0 ? token.prefix : token.delimiter) + segment;
        }

        continue
      }

      segment = token.asterisk ? encodeAsterisk(value) : encode(value);

      if (!matches[i].test(segment)) {
        throw new TypeError('Expected "' + token.name + '" to match "' + token.pattern + '", but received "' + segment + '"')
      }

      path += token.prefix + segment;
    }

    return path
  }
}

/**
 * Escape a regular expression string.
 *
 * @param  {string} str
 * @return {string}
 */
function escapeString (str) {
  return str.replace(/([.+*?=^!:${}()[\]|\/\\])/g, '\\$1')
}

/**
 * Escape the capturing group by escaping special characters and meaning.
 *
 * @param  {string} group
 * @return {string}
 */
function escapeGroup (group) {
  return group.replace(/([=!:$\/()])/g, '\\$1')
}

/**
 * Attach the keys as a property of the regexp.
 *
 * @param  {!RegExp} re
 * @param  {Array}   keys
 * @return {!RegExp}
 */
function attachKeys (re, keys) {
  re.keys = keys;
  return re
}

/**
 * Get the flags for a regexp from the options.
 *
 * @param  {Object} options
 * @return {string}
 */
function flags (options) {
  return options.sensitive ? '' : 'i'
}

/**
 * Pull out keys from a regexp.
 *
 * @param  {!RegExp} path
 * @param  {!Array}  keys
 * @return {!RegExp}
 */
function regexpToRegexp (path, keys) {
  // Use a negative lookahead to match only capturing groups.
  var groups = path.source.match(/\((?!\?)/g);

  if (groups) {
    for (var i = 0; i < groups.length; i++) {
      keys.push({
        name: i,
        prefix: null,
        delimiter: null,
        optional: false,
        repeat: false,
        partial: false,
        asterisk: false,
        pattern: null
      });
    }
  }

  return attachKeys(path, keys)
}

/**
 * Transform an array into a regexp.
 *
 * @param  {!Array}  path
 * @param  {Array}   keys
 * @param  {!Object} options
 * @return {!RegExp}
 */
function arrayToRegexp (path, keys, options) {
  var parts = [];

  for (var i = 0; i < path.length; i++) {
    parts.push(pathToRegexp(path[i], keys, options).source);
  }

  var regexp = new RegExp('(?:' + parts.join('|') + ')', flags(options));

  return attachKeys(regexp, keys)
}

/**
 * Create a path regexp from string input.
 *
 * @param  {string}  path
 * @param  {!Array}  keys
 * @param  {!Object} options
 * @return {!RegExp}
 */
function stringToRegexp (path, keys, options) {
  return tokensToRegExp(parse(path, options), keys, options)
}

/**
 * Expose a function for taking tokens and returning a RegExp.
 *
 * @param  {!Array}          tokens
 * @param  {(Array|Object)=} keys
 * @param  {Object=}         options
 * @return {!RegExp}
 */
function tokensToRegExp (tokens, keys, options) {
  if (!isarray(keys)) {
    options = /** @type {!Object} */ (keys || options);
    keys = [];
  }

  options = options || {};

  var strict = options.strict;
  var end = options.end !== false;
  var route = '';

  // Iterate over the tokens and create our regexp string.
  for (var i = 0; i < tokens.length; i++) {
    var token = tokens[i];

    if (typeof token === 'string') {
      route += escapeString(token);
    } else {
      var prefix = escapeString(token.prefix);
      var capture = '(?:' + token.pattern + ')';

      keys.push(token);

      if (token.repeat) {
        capture += '(?:' + prefix + capture + ')*';
      }

      if (token.optional) {
        if (!token.partial) {
          capture = '(?:' + prefix + '(' + capture + '))?';
        } else {
          capture = prefix + '(' + capture + ')?';
        }
      } else {
        capture = prefix + '(' + capture + ')';
      }

      route += capture;
    }
  }

  var delimiter = escapeString(options.delimiter || '/');
  var endsWithDelimiter = route.slice(-delimiter.length) === delimiter;

  // In non-strict mode we allow a slash at the end of match. If the path to
  // match already ends with a slash, we remove it for consistency. The slash
  // is valid at the end of a path match, not in the middle. This is important
  // in non-ending mode, where "/test/" shouldn't match "/test//route".
  if (!strict) {
    route = (endsWithDelimiter ? route.slice(0, -delimiter.length) : route) + '(?:' + delimiter + '(?=$))?';
  }

  if (end) {
    route += '$';
  } else {
    // In non-ending mode, we need the capturing groups to match as much as
    // possible by using a positive lookahead to the end or next path segment.
    route += strict && endsWithDelimiter ? '' : '(?=' + delimiter + '|$)';
  }

  return attachKeys(new RegExp('^' + route, flags(options)), keys)
}

/**
 * Normalize the given path string, returning a regular expression.
 *
 * An empty array can be passed in for the keys, which will hold the
 * placeholder key descriptions. For example, using `/user/:id`, `keys` will
 * contain `[{ name: 'id', delimiter: '/', optional: false, repeat: false }]`.
 *
 * @param  {(string|RegExp|Array)} path
 * @param  {(Array|Object)=}       keys
 * @param  {Object=}               options
 * @return {!RegExp}
 */
function pathToRegexp (path, keys, options) {
  if (!isarray(keys)) {
    options = /** @type {!Object} */ (keys || options);
    keys = [];
  }

  options = options || {};

  if (path instanceof RegExp) {
    return regexpToRegexp(path, /** @type {!Array} */ (keys))
  }

  if (isarray(path)) {
    return arrayToRegexp(/** @type {!Array} */ (path), /** @type {!Array} */ (keys), options)
  }

  return stringToRegexp(/** @type {string} */ (path), /** @type {!Array} */ (keys), options)
}

pathToRegexp_1.parse = parse_1;
pathToRegexp_1.compile = compile_1;
pathToRegexp_1.tokensToFunction = tokensToFunction_1;
pathToRegexp_1.tokensToRegExp = tokensToRegExp_1;

/*  */

// $flow-disable-line
var regexpCompileCache = Object.create(null);

function fillParams (
  path,
  params,
  routeMsg
) {
  try {
    var filler =
      regexpCompileCache[path] ||
      (regexpCompileCache[path] = pathToRegexp_1.compile(path));
    return filler(params || {}, { pretty: true })
  } catch (e) {
    if (false) {
      warn(false, ("missing param for " + routeMsg + ": " + (e.message)));
    }
    return ''
  }
}

/*  */

function createRouteMap (
  routes,
  oldPathList,
  oldPathMap,
  oldNameMap
) {
  // the path list is used to control path matching priority
  var pathList = oldPathList || [];
  // $flow-disable-line
  var pathMap = oldPathMap || Object.create(null);
  // $flow-disable-line
  var nameMap = oldNameMap || Object.create(null);

  routes.forEach(function (route) {
    addRouteRecord(pathList, pathMap, nameMap, route);
  });

  // ensure wildcard routes are always at the end
  for (var i = 0, l = pathList.length; i < l; i++) {
    if (pathList[i] === '*') {
      pathList.push(pathList.splice(i, 1)[0]);
      l--;
      i--;
    }
  }

  return {
    pathList: pathList,
    pathMap: pathMap,
    nameMap: nameMap
  }
}

function addRouteRecord (
  pathList,
  pathMap,
  nameMap,
  route,
  parent,
  matchAs
) {
  var path = route.path;
  var name = route.name;
  if (false) {
    assert(path != null, "\"path\" is required in a route configuration.");
    assert(
      typeof route.component !== 'string',
      "route config \"component\" for path: " + (String(path || name)) + " cannot be a " +
      "string id. Use an actual component instead."
    );
  }

  var pathToRegexpOptions = route.pathToRegexpOptions || {};
  var normalizedPath = normalizePath(
    path,
    parent,
    pathToRegexpOptions.strict
  );

  if (typeof route.caseSensitive === 'boolean') {
    pathToRegexpOptions.sensitive = route.caseSensitive;
  }

  var record = {
    path: normalizedPath,
    regex: compileRouteRegex(normalizedPath, pathToRegexpOptions),
    components: route.components || { default: route.component },
    instances: {},
    name: name,
    parent: parent,
    matchAs: matchAs,
    redirect: route.redirect,
    beforeEnter: route.beforeEnter,
    meta: route.meta || {},
    props: route.props == null
      ? {}
      : route.components
        ? route.props
        : { default: route.props }
  };

  if (route.children) {
    // Warn if route is named, does not redirect and has a default child route.
    // If users navigate to this route by name, the default child will
    // not be rendered (GH Issue #629)
    if (false) {
      if (route.name && !route.redirect && route.children.some(function (child) { return /^\/?$/.test(child.path); })) {
        warn(
          false,
          "Named Route '" + (route.name) + "' has a default child route. " +
          "When navigating to this named route (:to=\"{name: '" + (route.name) + "'\"), " +
          "the default child route will not be rendered. Remove the name from " +
          "this route and use the name of the default child route for named " +
          "links instead."
        );
      }
    }
    route.children.forEach(function (child) {
      var childMatchAs = matchAs
        ? cleanPath((matchAs + "/" + (child.path)))
        : undefined;
      addRouteRecord(pathList, pathMap, nameMap, child, record, childMatchAs);
    });
  }

  if (route.alias !== undefined) {
    var aliases = Array.isArray(route.alias)
      ? route.alias
      : [route.alias];

    aliases.forEach(function (alias) {
      var aliasRoute = {
        path: alias,
        children: route.children
      };
      addRouteRecord(
        pathList,
        pathMap,
        nameMap,
        aliasRoute,
        parent,
        record.path || '/' // matchAs
      );
    });
  }

  if (!pathMap[record.path]) {
    pathList.push(record.path);
    pathMap[record.path] = record;
  }

  if (name) {
    if (!nameMap[name]) {
      nameMap[name] = record;
    } else if (false) {
      warn(
        false,
        "Duplicate named routes definition: " +
        "{ name: \"" + name + "\", path: \"" + (record.path) + "\" }"
      );
    }
  }
}

function compileRouteRegex (path, pathToRegexpOptions) {
  var regex = pathToRegexp_1(path, [], pathToRegexpOptions);
  if (false) {
    var keys = Object.create(null);
    regex.keys.forEach(function (key) {
      warn(!keys[key.name], ("Duplicate param keys in route with path: \"" + path + "\""));
      keys[key.name] = true;
    });
  }
  return regex
}

function normalizePath (path, parent, strict) {
  if (!strict) { path = path.replace(/\/$/, ''); }
  if (path[0] === '/') { return path }
  if (parent == null) { return path }
  return cleanPath(((parent.path) + "/" + path))
}

/*  */


function normalizeLocation (
  raw,
  current,
  append,
  router
) {
  var next = typeof raw === 'string' ? { path: raw } : raw;
  // named target
  if (next.name || next._normalized) {
    return next
  }

  // relative params
  if (!next.path && next.params && current) {
    next = assign({}, next);
    next._normalized = true;
    var params = assign(assign({}, current.params), next.params);
    if (current.name) {
      next.name = current.name;
      next.params = params;
    } else if (current.matched.length) {
      var rawPath = current.matched[current.matched.length - 1].path;
      next.path = fillParams(rawPath, params, ("path " + (current.path)));
    } else if (false) {
      warn(false, "relative params navigation requires a current route.");
    }
    return next
  }

  var parsedPath = parsePath(next.path || '');
  var basePath = (current && current.path) || '/';
  var path = parsedPath.path
    ? resolvePath(parsedPath.path, basePath, append || next.append)
    : basePath;

  var query = resolveQuery(
    parsedPath.query,
    next.query,
    router && router.options.parseQuery
  );

  var hash = next.hash || parsedPath.hash;
  if (hash && hash.charAt(0) !== '#') {
    hash = "#" + hash;
  }

  return {
    _normalized: true,
    path: path,
    query: query,
    hash: hash
  }
}

function assign (a, b) {
  for (var key in b) {
    a[key] = b[key];
  }
  return a
}

/*  */


function createMatcher (
  routes,
  router
) {
  var ref = createRouteMap(routes);
  var pathList = ref.pathList;
  var pathMap = ref.pathMap;
  var nameMap = ref.nameMap;

  function addRoutes (routes) {
    createRouteMap(routes, pathList, pathMap, nameMap);
  }

  function match (
    raw,
    currentRoute,
    redirectedFrom
  ) {
    var location = normalizeLocation(raw, currentRoute, false, router);
    var name = location.name;

    if (name) {
      var record = nameMap[name];
      if (false) {
        warn(record, ("Route with name '" + name + "' does not exist"));
      }
      if (!record) { return _createRoute(null, location) }
      var paramNames = record.regex.keys
        .filter(function (key) { return !key.optional; })
        .map(function (key) { return key.name; });

      if (typeof location.params !== 'object') {
        location.params = {};
      }

      if (currentRoute && typeof currentRoute.params === 'object') {
        for (var key in currentRoute.params) {
          if (!(key in location.params) && paramNames.indexOf(key) > -1) {
            location.params[key] = currentRoute.params[key];
          }
        }
      }

      if (record) {
        location.path = fillParams(record.path, location.params, ("named route \"" + name + "\""));
        return _createRoute(record, location, redirectedFrom)
      }
    } else if (location.path) {
      location.params = {};
      for (var i = 0; i < pathList.length; i++) {
        var path = pathList[i];
        var record$1 = pathMap[path];
        if (matchRoute(record$1.regex, location.path, location.params)) {
          return _createRoute(record$1, location, redirectedFrom)
        }
      }
    }
    // no match
    return _createRoute(null, location)
  }

  function redirect (
    record,
    location
  ) {
    var originalRedirect = record.redirect;
    var redirect = typeof originalRedirect === 'function'
        ? originalRedirect(createRoute(record, location, null, router))
        : originalRedirect;

    if (typeof redirect === 'string') {
      redirect = { path: redirect };
    }

    if (!redirect || typeof redirect !== 'object') {
      if (false) {
        warn(
          false, ("invalid redirect option: " + (JSON.stringify(redirect)))
        );
      }
      return _createRoute(null, location)
    }

    var re = redirect;
    var name = re.name;
    var path = re.path;
    var query = location.query;
    var hash = location.hash;
    var params = location.params;
    query = re.hasOwnProperty('query') ? re.query : query;
    hash = re.hasOwnProperty('hash') ? re.hash : hash;
    params = re.hasOwnProperty('params') ? re.params : params;

    if (name) {
      // resolved named direct
      var targetRecord = nameMap[name];
      if (false) {
        assert(targetRecord, ("redirect failed: named route \"" + name + "\" not found."));
      }
      return match({
        _normalized: true,
        name: name,
        query: query,
        hash: hash,
        params: params
      }, undefined, location)
    } else if (path) {
      // 1. resolve relative redirect
      var rawPath = resolveRecordPath(path, record);
      // 2. resolve params
      var resolvedPath = fillParams(rawPath, params, ("redirect route with path \"" + rawPath + "\""));
      // 3. rematch with existing query and hash
      return match({
        _normalized: true,
        path: resolvedPath,
        query: query,
        hash: hash
      }, undefined, location)
    } else {
      if (false) {
        warn(false, ("invalid redirect option: " + (JSON.stringify(redirect))));
      }
      return _createRoute(null, location)
    }
  }

  function alias (
    record,
    location,
    matchAs
  ) {
    var aliasedPath = fillParams(matchAs, location.params, ("aliased route with path \"" + matchAs + "\""));
    var aliasedMatch = match({
      _normalized: true,
      path: aliasedPath
    });
    if (aliasedMatch) {
      var matched = aliasedMatch.matched;
      var aliasedRecord = matched[matched.length - 1];
      location.params = aliasedMatch.params;
      return _createRoute(aliasedRecord, location)
    }
    return _createRoute(null, location)
  }

  function _createRoute (
    record,
    location,
    redirectedFrom
  ) {
    if (record && record.redirect) {
      return redirect(record, redirectedFrom || location)
    }
    if (record && record.matchAs) {
      return alias(record, location, record.matchAs)
    }
    return createRoute(record, location, redirectedFrom, router)
  }

  return {
    match: match,
    addRoutes: addRoutes
  }
}

function matchRoute (
  regex,
  path,
  params
) {
  var m = path.match(regex);

  if (!m) {
    return false
  } else if (!params) {
    return true
  }

  for (var i = 1, len = m.length; i < len; ++i) {
    var key = regex.keys[i - 1];
    var val = typeof m[i] === 'string' ? decodeURIComponent(m[i]) : m[i];
    if (key) {
      params[key.name] = val;
    }
  }

  return true
}

function resolveRecordPath (path, record) {
  return resolvePath(path, record.parent ? record.parent.path : '/', true)
}

/*  */


var positionStore = Object.create(null);

function setupScroll () {
  // Fix for #1585 for Firefox
  window.history.replaceState({ key: getStateKey() }, '');
  window.addEventListener('popstate', function (e) {
    saveScrollPosition();
    if (e.state && e.state.key) {
      setStateKey(e.state.key);
    }
  });
}

function handleScroll (
  router,
  to,
  from,
  isPop
) {
  if (!router.app) {
    return
  }

  var behavior = router.options.scrollBehavior;
  if (!behavior) {
    return
  }

  if (false) {
    assert(typeof behavior === 'function', "scrollBehavior must be a function");
  }

  // wait until re-render finishes before scrolling
  router.app.$nextTick(function () {
    var position = getScrollPosition();
    var shouldScroll = behavior(to, from, isPop ? position : null);

    if (!shouldScroll) {
      return
    }

    if (typeof shouldScroll.then === 'function') {
      shouldScroll.then(function (shouldScroll) {
        scrollToPosition((shouldScroll), position);
      }).catch(function (err) {
        if (false) {
          assert(false, err.toString());
        }
      });
    } else {
      scrollToPosition(shouldScroll, position);
    }
  });
}

function saveScrollPosition () {
  var key = getStateKey();
  if (key) {
    positionStore[key] = {
      x: window.pageXOffset,
      y: window.pageYOffset
    };
  }
}

function getScrollPosition () {
  var key = getStateKey();
  if (key) {
    return positionStore[key]
  }
}

function getElementPosition (el, offset) {
  var docEl = document.documentElement;
  var docRect = docEl.getBoundingClientRect();
  var elRect = el.getBoundingClientRect();
  return {
    x: elRect.left - docRect.left - offset.x,
    y: elRect.top - docRect.top - offset.y
  }
}

function isValidPosition (obj) {
  return isNumber(obj.x) || isNumber(obj.y)
}

function normalizePosition (obj) {
  return {
    x: isNumber(obj.x) ? obj.x : window.pageXOffset,
    y: isNumber(obj.y) ? obj.y : window.pageYOffset
  }
}

function normalizeOffset (obj) {
  return {
    x: isNumber(obj.x) ? obj.x : 0,
    y: isNumber(obj.y) ? obj.y : 0
  }
}

function isNumber (v) {
  return typeof v === 'number'
}

function scrollToPosition (shouldScroll, position) {
  var isObject = typeof shouldScroll === 'object';
  if (isObject && typeof shouldScroll.selector === 'string') {
    var el = document.querySelector(shouldScroll.selector);
    if (el) {
      var offset = shouldScroll.offset && typeof shouldScroll.offset === 'object' ? shouldScroll.offset : {};
      offset = normalizeOffset(offset);
      position = getElementPosition(el, offset);
    } else if (isValidPosition(shouldScroll)) {
      position = normalizePosition(shouldScroll);
    }
  } else if (isObject && isValidPosition(shouldScroll)) {
    position = normalizePosition(shouldScroll);
  }

  if (position) {
    window.scrollTo(position.x, position.y);
  }
}

/*  */

var supportsPushState = inBrowser && (function () {
  var ua = window.navigator.userAgent;

  if (
    (ua.indexOf('Android 2.') !== -1 || ua.indexOf('Android 4.0') !== -1) &&
    ua.indexOf('Mobile Safari') !== -1 &&
    ua.indexOf('Chrome') === -1 &&
    ua.indexOf('Windows Phone') === -1
  ) {
    return false
  }

  return window.history && 'pushState' in window.history
})();

// use User Timing api (if present) for more accurate key precision
var Time = inBrowser && window.performance && window.performance.now
  ? window.performance
  : Date;

var _key = genKey();

function genKey () {
  return Time.now().toFixed(3)
}

function getStateKey () {
  return _key
}

function setStateKey (key) {
  _key = key;
}

function pushState (url, replace) {
  saveScrollPosition();
  // try...catch the pushState call to get around Safari
  // DOM Exception 18 where it limits to 100 pushState calls
  var history = window.history;
  try {
    if (replace) {
      history.replaceState({ key: _key }, '', url);
    } else {
      _key = genKey();
      history.pushState({ key: _key }, '', url);
    }
  } catch (e) {
    window.location[replace ? 'replace' : 'assign'](url);
  }
}

function replaceState (url) {
  pushState(url, true);
}

/*  */

function runQueue (queue, fn, cb) {
  var step = function (index) {
    if (index >= queue.length) {
      cb();
    } else {
      if (queue[index]) {
        fn(queue[index], function () {
          step(index + 1);
        });
      } else {
        step(index + 1);
      }
    }
  };
  step(0);
}

/*  */

function resolveAsyncComponents (matched) {
  return function (to, from, next) {
    var hasAsync = false;
    var pending = 0;
    var error = null;

    flatMapComponents(matched, function (def, _, match, key) {
      // if it's a function and doesn't have cid attached,
      // assume it's an async component resolve function.
      // we are not using Vue's default async resolving mechanism because
      // we want to halt the navigation until the incoming component has been
      // resolved.
      if (typeof def === 'function' && def.cid === undefined) {
        hasAsync = true;
        pending++;

        var resolve = once(function (resolvedDef) {
          if (isESModule(resolvedDef)) {
            resolvedDef = resolvedDef.default;
          }
          // save resolved on async factory in case it's used elsewhere
          def.resolved = typeof resolvedDef === 'function'
            ? resolvedDef
            : _Vue.extend(resolvedDef);
          match.components[key] = resolvedDef;
          pending--;
          if (pending <= 0) {
            next();
          }
        });

        var reject = once(function (reason) {
          var msg = "Failed to resolve async component " + key + ": " + reason;
          "production" !== 'production' && warn(false, msg);
          if (!error) {
            error = isError(reason)
              ? reason
              : new Error(msg);
            next(error);
          }
        });

        var res;
        try {
          res = def(resolve, reject);
        } catch (e) {
          reject(e);
        }
        if (res) {
          if (typeof res.then === 'function') {
            res.then(resolve, reject);
          } else {
            // new syntax in Vue 2.3
            var comp = res.component;
            if (comp && typeof comp.then === 'function') {
              comp.then(resolve, reject);
            }
          }
        }
      }
    });

    if (!hasAsync) { next(); }
  }
}

function flatMapComponents (
  matched,
  fn
) {
  return flatten(matched.map(function (m) {
    return Object.keys(m.components).map(function (key) { return fn(
      m.components[key],
      m.instances[key],
      m, key
    ); })
  }))
}

function flatten (arr) {
  return Array.prototype.concat.apply([], arr)
}

var hasSymbol =
  typeof Symbol === 'function' &&
  typeof Symbol.toStringTag === 'symbol';

function isESModule (obj) {
  return obj.__esModule || (hasSymbol && obj[Symbol.toStringTag] === 'Module')
}

// in Webpack 2, require.ensure now also returns a Promise
// so the resolve/reject functions may get called an extra time
// if the user uses an arrow function shorthand that happens to
// return that Promise.
function once (fn) {
  var called = false;
  return function () {
    var args = [], len = arguments.length;
    while ( len-- ) args[ len ] = arguments[ len ];

    if (called) { return }
    called = true;
    return fn.apply(this, args)
  }
}

/*  */

var History = function History (router, base) {
  this.router = router;
  this.base = normalizeBase(base);
  // start with a route object that stands for "nowhere"
  this.current = START;
  this.pending = null;
  this.ready = false;
  this.readyCbs = [];
  this.readyErrorCbs = [];
  this.errorCbs = [];
};

History.prototype.listen = function listen (cb) {
  this.cb = cb;
};

History.prototype.onReady = function onReady (cb, errorCb) {
  if (this.ready) {
    cb();
  } else {
    this.readyCbs.push(cb);
    if (errorCb) {
      this.readyErrorCbs.push(errorCb);
    }
  }
};

History.prototype.onError = function onError (errorCb) {
  this.errorCbs.push(errorCb);
};

History.prototype.transitionTo = function transitionTo (location, onComplete, onAbort) {
    var this$1 = this;

  var route = this.router.match(location, this.current);
  this.confirmTransition(route, function () {
    this$1.updateRoute(route);
    onComplete && onComplete(route);
    this$1.ensureURL();

    // fire ready cbs once
    if (!this$1.ready) {
      this$1.ready = true;
      this$1.readyCbs.forEach(function (cb) { cb(route); });
    }
  }, function (err) {
    if (onAbort) {
      onAbort(err);
    }
    if (err && !this$1.ready) {
      this$1.ready = true;
      this$1.readyErrorCbs.forEach(function (cb) { cb(err); });
    }
  });
};

History.prototype.confirmTransition = function confirmTransition (route, onComplete, onAbort) {
    var this$1 = this;

  var current = this.current;
  var abort = function (err) {
    if (isError(err)) {
      if (this$1.errorCbs.length) {
        this$1.errorCbs.forEach(function (cb) { cb(err); });
      } else {
        warn(false, 'uncaught error during route navigation:');
        console.error(err);
      }
    }
    onAbort && onAbort(err);
  };
  if (
    isSameRoute(route, current) &&
    // in the case the route map has been dynamically appended to
    route.matched.length === current.matched.length
  ) {
    this.ensureURL();
    return abort()
  }

  var ref = resolveQueue(this.current.matched, route.matched);
    var updated = ref.updated;
    var deactivated = ref.deactivated;
    var activated = ref.activated;

  var queue = [].concat(
    // in-component leave guards
    extractLeaveGuards(deactivated),
    // global before hooks
    this.router.beforeHooks,
    // in-component update hooks
    extractUpdateHooks(updated),
    // in-config enter guards
    activated.map(function (m) { return m.beforeEnter; }),
    // async components
    resolveAsyncComponents(activated)
  );

  this.pending = route;
  var iterator = function (hook, next) {
    if (this$1.pending !== route) {
      return abort()
    }
    try {
      hook(route, current, function (to) {
        if (to === false || isError(to)) {
          // next(false) -> abort navigation, ensure current URL
          this$1.ensureURL(true);
          abort(to);
        } else if (
          typeof to === 'string' ||
          (typeof to === 'object' && (
            typeof to.path === 'string' ||
            typeof to.name === 'string'
          ))
        ) {
          // next('/') or next({ path: '/' }) -> redirect
          abort();
          if (typeof to === 'object' && to.replace) {
            this$1.replace(to);
          } else {
            this$1.push(to);
          }
        } else {
          // confirm transition and pass on the value
          next(to);
        }
      });
    } catch (e) {
      abort(e);
    }
  };

  runQueue(queue, iterator, function () {
    var postEnterCbs = [];
    var isValid = function () { return this$1.current === route; };
    // wait until async components are resolved before
    // extracting in-component enter guards
    var enterGuards = extractEnterGuards(activated, postEnterCbs, isValid);
    var queue = enterGuards.concat(this$1.router.resolveHooks);
    runQueue(queue, iterator, function () {
      if (this$1.pending !== route) {
        return abort()
      }
      this$1.pending = null;
      onComplete(route);
      if (this$1.router.app) {
        this$1.router.app.$nextTick(function () {
          postEnterCbs.forEach(function (cb) { cb(); });
        });
      }
    });
  });
};

History.prototype.updateRoute = function updateRoute (route) {
  var prev = this.current;
  this.current = route;
  this.cb && this.cb(route);
  this.router.afterHooks.forEach(function (hook) {
    hook && hook(route, prev);
  });
};

function normalizeBase (base) {
  if (!base) {
    if (inBrowser) {
      // respect <base> tag
      var baseEl = document.querySelector('base');
      base = (baseEl && baseEl.getAttribute('href')) || '/';
      // strip full URL origin
      base = base.replace(/^https?:\/\/[^\/]+/, '');
    } else {
      base = '/';
    }
  }
  // make sure there's the starting slash
  if (base.charAt(0) !== '/') {
    base = '/' + base;
  }
  // remove trailing slash
  return base.replace(/\/$/, '')
}

function resolveQueue (
  current,
  next
) {
  var i;
  var max = Math.max(current.length, next.length);
  for (i = 0; i < max; i++) {
    if (current[i] !== next[i]) {
      break
    }
  }
  return {
    updated: next.slice(0, i),
    activated: next.slice(i),
    deactivated: current.slice(i)
  }
}

function extractGuards (
  records,
  name,
  bind,
  reverse
) {
  var guards = flatMapComponents(records, function (def, instance, match, key) {
    var guard = extractGuard(def, name);
    if (guard) {
      return Array.isArray(guard)
        ? guard.map(function (guard) { return bind(guard, instance, match, key); })
        : bind(guard, instance, match, key)
    }
  });
  return flatten(reverse ? guards.reverse() : guards)
}

function extractGuard (
  def,
  key
) {
  if (typeof def !== 'function') {
    // extend now so that global mixins are applied.
    def = _Vue.extend(def);
  }
  return def.options[key]
}

function extractLeaveGuards (deactivated) {
  return extractGuards(deactivated, 'beforeRouteLeave', bindGuard, true)
}

function extractUpdateHooks (updated) {
  return extractGuards(updated, 'beforeRouteUpdate', bindGuard)
}

function bindGuard (guard, instance) {
  if (instance) {
    return function boundRouteGuard () {
      return guard.apply(instance, arguments)
    }
  }
}

function extractEnterGuards (
  activated,
  cbs,
  isValid
) {
  return extractGuards(activated, 'beforeRouteEnter', function (guard, _, match, key) {
    return bindEnterGuard(guard, match, key, cbs, isValid)
  })
}

function bindEnterGuard (
  guard,
  match,
  key,
  cbs,
  isValid
) {
  return function routeEnterGuard (to, from, next) {
    return guard(to, from, function (cb) {
      next(cb);
      if (typeof cb === 'function') {
        cbs.push(function () {
          // #750
          // if a router-view is wrapped with an out-in transition,
          // the instance may not have been registered at this time.
          // we will need to poll for registration until current route
          // is no longer valid.
          poll(cb, match.instances, key, isValid);
        });
      }
    })
  }
}

function poll (
  cb, // somehow flow cannot infer this is a function
  instances,
  key,
  isValid
) {
  if (instances[key]) {
    cb(instances[key]);
  } else if (isValid()) {
    setTimeout(function () {
      poll(cb, instances, key, isValid);
    }, 16);
  }
}

/*  */


var HTML5History = (function (History$$1) {
  function HTML5History (router, base) {
    var this$1 = this;

    History$$1.call(this, router, base);

    var expectScroll = router.options.scrollBehavior;

    if (expectScroll) {
      setupScroll();
    }

    var initLocation = getLocation(this.base);
    window.addEventListener('popstate', function (e) {
      var current = this$1.current;

      // Avoiding first `popstate` event dispatched in some browsers but first
      // history route not updated since async guard at the same time.
      var location = getLocation(this$1.base);
      if (this$1.current === START && location === initLocation) {
        return
      }

      this$1.transitionTo(location, function (route) {
        if (expectScroll) {
          handleScroll(router, route, current, true);
        }
      });
    });
  }

  if ( History$$1 ) HTML5History.__proto__ = History$$1;
  HTML5History.prototype = Object.create( History$$1 && History$$1.prototype );
  HTML5History.prototype.constructor = HTML5History;

  HTML5History.prototype.go = function go (n) {
    window.history.go(n);
  };

  HTML5History.prototype.push = function push (location, onComplete, onAbort) {
    var this$1 = this;

    var ref = this;
    var fromRoute = ref.current;
    this.transitionTo(location, function (route) {
      pushState(cleanPath(this$1.base + route.fullPath));
      handleScroll(this$1.router, route, fromRoute, false);
      onComplete && onComplete(route);
    }, onAbort);
  };

  HTML5History.prototype.replace = function replace (location, onComplete, onAbort) {
    var this$1 = this;

    var ref = this;
    var fromRoute = ref.current;
    this.transitionTo(location, function (route) {
      replaceState(cleanPath(this$1.base + route.fullPath));
      handleScroll(this$1.router, route, fromRoute, false);
      onComplete && onComplete(route);
    }, onAbort);
  };

  HTML5History.prototype.ensureURL = function ensureURL (push) {
    if (getLocation(this.base) !== this.current.fullPath) {
      var current = cleanPath(this.base + this.current.fullPath);
      push ? pushState(current) : replaceState(current);
    }
  };

  HTML5History.prototype.getCurrentLocation = function getCurrentLocation () {
    return getLocation(this.base)
  };

  return HTML5History;
}(History));

function getLocation (base) {
  var path = window.location.pathname;
  if (base && path.indexOf(base) === 0) {
    path = path.slice(base.length);
  }
  return (path || '/') + window.location.search + window.location.hash
}

/*  */


var HashHistory = (function (History$$1) {
  function HashHistory (router, base, fallback) {
    History$$1.call(this, router, base);
    // check history fallback deeplinking
    if (fallback && checkFallback(this.base)) {
      return
    }
    ensureSlash();
  }

  if ( History$$1 ) HashHistory.__proto__ = History$$1;
  HashHistory.prototype = Object.create( History$$1 && History$$1.prototype );
  HashHistory.prototype.constructor = HashHistory;

  // this is delayed until the app mounts
  // to avoid the hashchange listener being fired too early
  HashHistory.prototype.setupListeners = function setupListeners () {
    var this$1 = this;

    var router = this.router;
    var expectScroll = router.options.scrollBehavior;
    var supportsScroll = supportsPushState && expectScroll;

    if (supportsScroll) {
      setupScroll();
    }

    window.addEventListener(supportsPushState ? 'popstate' : 'hashchange', function () {
      var current = this$1.current;
      if (!ensureSlash()) {
        return
      }
      this$1.transitionTo(getHash(), function (route) {
        if (supportsScroll) {
          handleScroll(this$1.router, route, current, true);
        }
        if (!supportsPushState) {
          replaceHash(route.fullPath);
        }
      });
    });
  };

  HashHistory.prototype.push = function push (location, onComplete, onAbort) {
    var this$1 = this;

    var ref = this;
    var fromRoute = ref.current;
    this.transitionTo(location, function (route) {
      pushHash(route.fullPath);
      handleScroll(this$1.router, route, fromRoute, false);
      onComplete && onComplete(route);
    }, onAbort);
  };

  HashHistory.prototype.replace = function replace (location, onComplete, onAbort) {
    var this$1 = this;

    var ref = this;
    var fromRoute = ref.current;
    this.transitionTo(location, function (route) {
      replaceHash(route.fullPath);
      handleScroll(this$1.router, route, fromRoute, false);
      onComplete && onComplete(route);
    }, onAbort);
  };

  HashHistory.prototype.go = function go (n) {
    window.history.go(n);
  };

  HashHistory.prototype.ensureURL = function ensureURL (push) {
    var current = this.current.fullPath;
    if (getHash() !== current) {
      push ? pushHash(current) : replaceHash(current);
    }
  };

  HashHistory.prototype.getCurrentLocation = function getCurrentLocation () {
    return getHash()
  };

  return HashHistory;
}(History));

function checkFallback (base) {
  var location = getLocation(base);
  if (!/^\/#/.test(location)) {
    window.location.replace(
      cleanPath(base + '/#' + location)
    );
    return true
  }
}

function ensureSlash () {
  var path = getHash();
  if (path.charAt(0) === '/') {
    return true
  }
  replaceHash('/' + path);
  return false
}

function getHash () {
  // We can't use window.location.hash here because it's not
  // consistent across browsers - Firefox will pre-decode it!
  var href = window.location.href;
  var index = href.indexOf('#');
  return index === -1 ? '' : href.slice(index + 1)
}

function getUrl (path) {
  var href = window.location.href;
  var i = href.indexOf('#');
  var base = i >= 0 ? href.slice(0, i) : href;
  return (base + "#" + path)
}

function pushHash (path) {
  if (supportsPushState) {
    pushState(getUrl(path));
  } else {
    window.location.hash = path;
  }
}

function replaceHash (path) {
  if (supportsPushState) {
    replaceState(getUrl(path));
  } else {
    window.location.replace(getUrl(path));
  }
}

/*  */


var AbstractHistory = (function (History$$1) {
  function AbstractHistory (router, base) {
    History$$1.call(this, router, base);
    this.stack = [];
    this.index = -1;
  }

  if ( History$$1 ) AbstractHistory.__proto__ = History$$1;
  AbstractHistory.prototype = Object.create( History$$1 && History$$1.prototype );
  AbstractHistory.prototype.constructor = AbstractHistory;

  AbstractHistory.prototype.push = function push (location, onComplete, onAbort) {
    var this$1 = this;

    this.transitionTo(location, function (route) {
      this$1.stack = this$1.stack.slice(0, this$1.index + 1).concat(route);
      this$1.index++;
      onComplete && onComplete(route);
    }, onAbort);
  };

  AbstractHistory.prototype.replace = function replace (location, onComplete, onAbort) {
    var this$1 = this;

    this.transitionTo(location, function (route) {
      this$1.stack = this$1.stack.slice(0, this$1.index).concat(route);
      onComplete && onComplete(route);
    }, onAbort);
  };

  AbstractHistory.prototype.go = function go (n) {
    var this$1 = this;

    var targetIndex = this.index + n;
    if (targetIndex < 0 || targetIndex >= this.stack.length) {
      return
    }
    var route = this.stack[targetIndex];
    this.confirmTransition(route, function () {
      this$1.index = targetIndex;
      this$1.updateRoute(route);
    });
  };

  AbstractHistory.prototype.getCurrentLocation = function getCurrentLocation () {
    var current = this.stack[this.stack.length - 1];
    return current ? current.fullPath : '/'
  };

  AbstractHistory.prototype.ensureURL = function ensureURL () {
    // noop
  };

  return AbstractHistory;
}(History));

/*  */

var VueRouter = function VueRouter (options) {
  if ( options === void 0 ) options = {};

  this.app = null;
  this.apps = [];
  this.options = options;
  this.beforeHooks = [];
  this.resolveHooks = [];
  this.afterHooks = [];
  this.matcher = createMatcher(options.routes || [], this);

  var mode = options.mode || 'hash';
  this.fallback = mode === 'history' && !supportsPushState && options.fallback !== false;
  if (this.fallback) {
    mode = 'hash';
  }
  if (!inBrowser) {
    mode = 'abstract';
  }
  this.mode = mode;

  switch (mode) {
    case 'history':
      this.history = new HTML5History(this, options.base);
      break
    case 'hash':
      this.history = new HashHistory(this, options.base, this.fallback);
      break
    case 'abstract':
      this.history = new AbstractHistory(this, options.base);
      break
    default:
      if (false) {
        assert(false, ("invalid mode: " + mode));
      }
  }
};

var prototypeAccessors = { currentRoute: { configurable: true } };

VueRouter.prototype.match = function match (
  raw,
  current,
  redirectedFrom
) {
  return this.matcher.match(raw, current, redirectedFrom)
};

prototypeAccessors.currentRoute.get = function () {
  return this.history && this.history.current
};

VueRouter.prototype.init = function init (app /* Vue component instance */) {
    var this$1 = this;

  "production" !== 'production' && assert(
    install.installed,
    "not installed. Make sure to call `Vue.use(VueRouter)` " +
    "before creating root instance."
  );

  this.apps.push(app);

  // main app already initialized.
  if (this.app) {
    return
  }

  this.app = app;

  var history = this.history;

  if (history instanceof HTML5History) {
    history.transitionTo(history.getCurrentLocation());
  } else if (history instanceof HashHistory) {
    var setupHashListener = function () {
      history.setupListeners();
    };
    history.transitionTo(
      history.getCurrentLocation(),
      setupHashListener,
      setupHashListener
    );
  }

  history.listen(function (route) {
    this$1.apps.forEach(function (app) {
      app._route = route;
    });
  });
};

VueRouter.prototype.beforeEach = function beforeEach (fn) {
  return registerHook(this.beforeHooks, fn)
};

VueRouter.prototype.beforeResolve = function beforeResolve (fn) {
  return registerHook(this.resolveHooks, fn)
};

VueRouter.prototype.afterEach = function afterEach (fn) {
  return registerHook(this.afterHooks, fn)
};

VueRouter.prototype.onReady = function onReady (cb, errorCb) {
  this.history.onReady(cb, errorCb);
};

VueRouter.prototype.onError = function onError (errorCb) {
  this.history.onError(errorCb);
};

VueRouter.prototype.push = function push (location, onComplete, onAbort) {
  this.history.push(location, onComplete, onAbort);
};

VueRouter.prototype.replace = function replace (location, onComplete, onAbort) {
  this.history.replace(location, onComplete, onAbort);
};

VueRouter.prototype.go = function go (n) {
  this.history.go(n);
};

VueRouter.prototype.back = function back () {
  this.go(-1);
};

VueRouter.prototype.forward = function forward () {
  this.go(1);
};

VueRouter.prototype.getMatchedComponents = function getMatchedComponents (to) {
  var route = to
    ? to.matched
      ? to
      : this.resolve(to).route
    : this.currentRoute;
  if (!route) {
    return []
  }
  return [].concat.apply([], route.matched.map(function (m) {
    return Object.keys(m.components).map(function (key) {
      return m.components[key]
    })
  }))
};

VueRouter.prototype.resolve = function resolve (
  to,
  current,
  append
) {
  var location = normalizeLocation(
    to,
    current || this.history.current,
    append,
    this
  );
  var route = this.match(location, current);
  var fullPath = route.redirectedFrom || route.fullPath;
  var base = this.history.base;
  var href = createHref(base, fullPath, this.mode);
  return {
    location: location,
    route: route,
    href: href,
    // for backwards compat
    normalizedTo: location,
    resolved: route
  }
};

VueRouter.prototype.addRoutes = function addRoutes (routes) {
  this.matcher.addRoutes(routes);
  if (this.history.current !== START) {
    this.history.transitionTo(this.history.getCurrentLocation());
  }
};

Object.defineProperties( VueRouter.prototype, prototypeAccessors );

function registerHook (list, fn) {
  list.push(fn);
  return function () {
    var i = list.indexOf(fn);
    if (i > -1) { list.splice(i, 1); }
  }
}

function createHref (base, fullPath, mode) {
  var path = mode === 'hash' ? '#' + fullPath : fullPath;
  return base ? cleanPath(base + '/' + path) : path
}

VueRouter.install = install;
VueRouter.version = '2.8.1';

if (inBrowser && window.Vue) {
  window.Vue.use(VueRouter);
}

/* harmony default export */ __webpack_exports__["a"] = (VueRouter);


/***/ }),
/* 20 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(global, setImmediate) {/*!
 * Vue.js v2.5.17
 * (c) 2014-2018 Evan You
 * Released under the MIT License.
 */
/*  */

var emptyObject = Object.freeze({});

// these helpers produces better vm code in JS engines due to their
// explicitness and function inlining
function isUndef (v) {
  return v === undefined || v === null
}

function isDef (v) {
  return v !== undefined && v !== null
}

function isTrue (v) {
  return v === true
}

function isFalse (v) {
  return v === false
}

/**
 * Check if value is primitive
 */
function isPrimitive (value) {
  return (
    typeof value === 'string' ||
    typeof value === 'number' ||
    // $flow-disable-line
    typeof value === 'symbol' ||
    typeof value === 'boolean'
  )
}

/**
 * Quick object check - this is primarily used to tell
 * Objects from primitive values when we know the value
 * is a JSON-compliant type.
 */
function isObject (obj) {
  return obj !== null && typeof obj === 'object'
}

/**
 * Get the raw type string of a value e.g. [object Object]
 */
var _toString = Object.prototype.toString;

function toRawType (value) {
  return _toString.call(value).slice(8, -1)
}

/**
 * Strict object type check. Only returns true
 * for plain JavaScript objects.
 */
function isPlainObject (obj) {
  return _toString.call(obj) === '[object Object]'
}

function isRegExp (v) {
  return _toString.call(v) === '[object RegExp]'
}

/**
 * Check if val is a valid array index.
 */
function isValidArrayIndex (val) {
  var n = parseFloat(String(val));
  return n >= 0 && Math.floor(n) === n && isFinite(val)
}

/**
 * Convert a value to a string that is actually rendered.
 */
function toString (val) {
  return val == null
    ? ''
    : typeof val === 'object'
      ? JSON.stringify(val, null, 2)
      : String(val)
}

/**
 * Convert a input value to a number for persistence.
 * If the conversion fails, return original string.
 */
function toNumber (val) {
  var n = parseFloat(val);
  return isNaN(n) ? val : n
}

/**
 * Make a map and return a function for checking if a key
 * is in that map.
 */
function makeMap (
  str,
  expectsLowerCase
) {
  var map = Object.create(null);
  var list = str.split(',');
  for (var i = 0; i < list.length; i++) {
    map[list[i]] = true;
  }
  return expectsLowerCase
    ? function (val) { return map[val.toLowerCase()]; }
    : function (val) { return map[val]; }
}

/**
 * Check if a tag is a built-in tag.
 */
var isBuiltInTag = makeMap('slot,component', true);

/**
 * Check if a attribute is a reserved attribute.
 */
var isReservedAttribute = makeMap('key,ref,slot,slot-scope,is');

/**
 * Remove an item from an array
 */
function remove (arr, item) {
  if (arr.length) {
    var index = arr.indexOf(item);
    if (index > -1) {
      return arr.splice(index, 1)
    }
  }
}

/**
 * Check whether the object has the property.
 */
var hasOwnProperty = Object.prototype.hasOwnProperty;
function hasOwn (obj, key) {
  return hasOwnProperty.call(obj, key)
}

/**
 * Create a cached version of a pure function.
 */
function cached (fn) {
  var cache = Object.create(null);
  return (function cachedFn (str) {
    var hit = cache[str];
    return hit || (cache[str] = fn(str))
  })
}

/**
 * Camelize a hyphen-delimited string.
 */
var camelizeRE = /-(\w)/g;
var camelize = cached(function (str) {
  return str.replace(camelizeRE, function (_, c) { return c ? c.toUpperCase() : ''; })
});

/**
 * Capitalize a string.
 */
var capitalize = cached(function (str) {
  return str.charAt(0).toUpperCase() + str.slice(1)
});

/**
 * Hyphenate a camelCase string.
 */
var hyphenateRE = /\B([A-Z])/g;
var hyphenate = cached(function (str) {
  return str.replace(hyphenateRE, '-$1').toLowerCase()
});

/**
 * Simple bind polyfill for environments that do not support it... e.g.
 * PhantomJS 1.x. Technically we don't need this anymore since native bind is
 * now more performant in most browsers, but removing it would be breaking for
 * code that was able to run in PhantomJS 1.x, so this must be kept for
 * backwards compatibility.
 */

/* istanbul ignore next */
function polyfillBind (fn, ctx) {
  function boundFn (a) {
    var l = arguments.length;
    return l
      ? l > 1
        ? fn.apply(ctx, arguments)
        : fn.call(ctx, a)
      : fn.call(ctx)
  }

  boundFn._length = fn.length;
  return boundFn
}

function nativeBind (fn, ctx) {
  return fn.bind(ctx)
}

var bind = Function.prototype.bind
  ? nativeBind
  : polyfillBind;

/**
 * Convert an Array-like object to a real Array.
 */
function toArray (list, start) {
  start = start || 0;
  var i = list.length - start;
  var ret = new Array(i);
  while (i--) {
    ret[i] = list[i + start];
  }
  return ret
}

/**
 * Mix properties into target object.
 */
function extend (to, _from) {
  for (var key in _from) {
    to[key] = _from[key];
  }
  return to
}

/**
 * Merge an Array of Objects into a single Object.
 */
function toObject (arr) {
  var res = {};
  for (var i = 0; i < arr.length; i++) {
    if (arr[i]) {
      extend(res, arr[i]);
    }
  }
  return res
}

/**
 * Perform no operation.
 * Stubbing args to make Flow happy without leaving useless transpiled code
 * with ...rest (https://flow.org/blog/2017/05/07/Strict-Function-Call-Arity/)
 */
function noop (a, b, c) {}

/**
 * Always return false.
 */
var no = function (a, b, c) { return false; };

/**
 * Return same value
 */
var identity = function (_) { return _; };

/**
 * Generate a static keys string from compiler modules.
 */
function genStaticKeys (modules) {
  return modules.reduce(function (keys, m) {
    return keys.concat(m.staticKeys || [])
  }, []).join(',')
}

/**
 * Check if two values are loosely equal - that is,
 * if they are plain objects, do they have the same shape?
 */
function looseEqual (a, b) {
  if (a === b) { return true }
  var isObjectA = isObject(a);
  var isObjectB = isObject(b);
  if (isObjectA && isObjectB) {
    try {
      var isArrayA = Array.isArray(a);
      var isArrayB = Array.isArray(b);
      if (isArrayA && isArrayB) {
        return a.length === b.length && a.every(function (e, i) {
          return looseEqual(e, b[i])
        })
      } else if (!isArrayA && !isArrayB) {
        var keysA = Object.keys(a);
        var keysB = Object.keys(b);
        return keysA.length === keysB.length && keysA.every(function (key) {
          return looseEqual(a[key], b[key])
        })
      } else {
        /* istanbul ignore next */
        return false
      }
    } catch (e) {
      /* istanbul ignore next */
      return false
    }
  } else if (!isObjectA && !isObjectB) {
    return String(a) === String(b)
  } else {
    return false
  }
}

function looseIndexOf (arr, val) {
  for (var i = 0; i < arr.length; i++) {
    if (looseEqual(arr[i], val)) { return i }
  }
  return -1
}

/**
 * Ensure a function is called only once.
 */
function once (fn) {
  var called = false;
  return function () {
    if (!called) {
      called = true;
      fn.apply(this, arguments);
    }
  }
}

var SSR_ATTR = 'data-server-rendered';

var ASSET_TYPES = [
  'component',
  'directive',
  'filter'
];

var LIFECYCLE_HOOKS = [
  'beforeCreate',
  'created',
  'beforeMount',
  'mounted',
  'beforeUpdate',
  'updated',
  'beforeDestroy',
  'destroyed',
  'activated',
  'deactivated',
  'errorCaptured'
];

/*  */

var config = ({
  /**
   * Option merge strategies (used in core/util/options)
   */
  // $flow-disable-line
  optionMergeStrategies: Object.create(null),

  /**
   * Whether to suppress warnings.
   */
  silent: false,

  /**
   * Show production mode tip message on boot?
   */
  productionTip: "production" !== 'production',

  /**
   * Whether to enable devtools
   */
  devtools: "production" !== 'production',

  /**
   * Whether to record perf
   */
  performance: false,

  /**
   * Error handler for watcher errors
   */
  errorHandler: null,

  /**
   * Warn handler for watcher warns
   */
  warnHandler: null,

  /**
   * Ignore certain custom elements
   */
  ignoredElements: [],

  /**
   * Custom user key aliases for v-on
   */
  // $flow-disable-line
  keyCodes: Object.create(null),

  /**
   * Check if a tag is reserved so that it cannot be registered as a
   * component. This is platform-dependent and may be overwritten.
   */
  isReservedTag: no,

  /**
   * Check if an attribute is reserved so that it cannot be used as a component
   * prop. This is platform-dependent and may be overwritten.
   */
  isReservedAttr: no,

  /**
   * Check if a tag is an unknown element.
   * Platform-dependent.
   */
  isUnknownElement: no,

  /**
   * Get the namespace of an element
   */
  getTagNamespace: noop,

  /**
   * Parse the real tag name for the specific platform.
   */
  parsePlatformTagName: identity,

  /**
   * Check if an attribute must be bound using property, e.g. value
   * Platform-dependent.
   */
  mustUseProp: no,

  /**
   * Exposed for legacy reasons
   */
  _lifecycleHooks: LIFECYCLE_HOOKS
})

/*  */

/**
 * Check if a string starts with $ or _
 */
function isReserved (str) {
  var c = (str + '').charCodeAt(0);
  return c === 0x24 || c === 0x5F
}

/**
 * Define a property.
 */
function def (obj, key, val, enumerable) {
  Object.defineProperty(obj, key, {
    value: val,
    enumerable: !!enumerable,
    writable: true,
    configurable: true
  });
}

/**
 * Parse simple path.
 */
var bailRE = /[^\w.$]/;
function parsePath (path) {
  if (bailRE.test(path)) {
    return
  }
  var segments = path.split('.');
  return function (obj) {
    for (var i = 0; i < segments.length; i++) {
      if (!obj) { return }
      obj = obj[segments[i]];
    }
    return obj
  }
}

/*  */

// can we use __proto__?
var hasProto = '__proto__' in {};

// Browser environment sniffing
var inBrowser = typeof window !== 'undefined';
var inWeex = typeof WXEnvironment !== 'undefined' && !!WXEnvironment.platform;
var weexPlatform = inWeex && WXEnvironment.platform.toLowerCase();
var UA = inBrowser && window.navigator.userAgent.toLowerCase();
var isIE = UA && /msie|trident/.test(UA);
var isIE9 = UA && UA.indexOf('msie 9.0') > 0;
var isEdge = UA && UA.indexOf('edge/') > 0;
var isAndroid = (UA && UA.indexOf('android') > 0) || (weexPlatform === 'android');
var isIOS = (UA && /iphone|ipad|ipod|ios/.test(UA)) || (weexPlatform === 'ios');
var isChrome = UA && /chrome\/\d+/.test(UA) && !isEdge;

// Firefox has a "watch" function on Object.prototype...
var nativeWatch = ({}).watch;

var supportsPassive = false;
if (inBrowser) {
  try {
    var opts = {};
    Object.defineProperty(opts, 'passive', ({
      get: function get () {
        /* istanbul ignore next */
        supportsPassive = true;
      }
    })); // https://github.com/facebook/flow/issues/285
    window.addEventListener('test-passive', null, opts);
  } catch (e) {}
}

// this needs to be lazy-evaled because vue may be required before
// vue-server-renderer can set VUE_ENV
var _isServer;
var isServerRendering = function () {
  if (_isServer === undefined) {
    /* istanbul ignore if */
    if (!inBrowser && !inWeex && typeof global !== 'undefined') {
      // detect presence of vue-server-renderer and avoid
      // Webpack shimming the process
      _isServer = global['process'].env.VUE_ENV === 'server';
    } else {
      _isServer = false;
    }
  }
  return _isServer
};

// detect devtools
var devtools = inBrowser && window.__VUE_DEVTOOLS_GLOBAL_HOOK__;

/* istanbul ignore next */
function isNative (Ctor) {
  return typeof Ctor === 'function' && /native code/.test(Ctor.toString())
}

var hasSymbol =
  typeof Symbol !== 'undefined' && isNative(Symbol) &&
  typeof Reflect !== 'undefined' && isNative(Reflect.ownKeys);

var _Set;
/* istanbul ignore if */ // $flow-disable-line
if (typeof Set !== 'undefined' && isNative(Set)) {
  // use native Set when available.
  _Set = Set;
} else {
  // a non-standard Set polyfill that only works with primitive keys.
  _Set = (function () {
    function Set () {
      this.set = Object.create(null);
    }
    Set.prototype.has = function has (key) {
      return this.set[key] === true
    };
    Set.prototype.add = function add (key) {
      this.set[key] = true;
    };
    Set.prototype.clear = function clear () {
      this.set = Object.create(null);
    };

    return Set;
  }());
}

/*  */

var warn = noop;
var tip = noop;
var generateComponentTrace = (noop); // work around flow check
var formatComponentName = (noop);

if (false) {
  var hasConsole = typeof console !== 'undefined';
  var classifyRE = /(?:^|[-_])(\w)/g;
  var classify = function (str) { return str
    .replace(classifyRE, function (c) { return c.toUpperCase(); })
    .replace(/[-_]/g, ''); };

  warn = function (msg, vm) {
    var trace = vm ? generateComponentTrace(vm) : '';

    if (config.warnHandler) {
      config.warnHandler.call(null, msg, vm, trace);
    } else if (hasConsole && (!config.silent)) {
      console.error(("[Vue warn]: " + msg + trace));
    }
  };

  tip = function (msg, vm) {
    if (hasConsole && (!config.silent)) {
      console.warn("[Vue tip]: " + msg + (
        vm ? generateComponentTrace(vm) : ''
      ));
    }
  };

  formatComponentName = function (vm, includeFile) {
    if (vm.$root === vm) {
      return '<Root>'
    }
    var options = typeof vm === 'function' && vm.cid != null
      ? vm.options
      : vm._isVue
        ? vm.$options || vm.constructor.options
        : vm || {};
    var name = options.name || options._componentTag;
    var file = options.__file;
    if (!name && file) {
      var match = file.match(/([^/\\]+)\.vue$/);
      name = match && match[1];
    }

    return (
      (name ? ("<" + (classify(name)) + ">") : "<Anonymous>") +
      (file && includeFile !== false ? (" at " + file) : '')
    )
  };

  var repeat = function (str, n) {
    var res = '';
    while (n) {
      if (n % 2 === 1) { res += str; }
      if (n > 1) { str += str; }
      n >>= 1;
    }
    return res
  };

  generateComponentTrace = function (vm) {
    if (vm._isVue && vm.$parent) {
      var tree = [];
      var currentRecursiveSequence = 0;
      while (vm) {
        if (tree.length > 0) {
          var last = tree[tree.length - 1];
          if (last.constructor === vm.constructor) {
            currentRecursiveSequence++;
            vm = vm.$parent;
            continue
          } else if (currentRecursiveSequence > 0) {
            tree[tree.length - 1] = [last, currentRecursiveSequence];
            currentRecursiveSequence = 0;
          }
        }
        tree.push(vm);
        vm = vm.$parent;
      }
      return '\n\nfound in\n\n' + tree
        .map(function (vm, i) { return ("" + (i === 0 ? '---> ' : repeat(' ', 5 + i * 2)) + (Array.isArray(vm)
            ? ((formatComponentName(vm[0])) + "... (" + (vm[1]) + " recursive calls)")
            : formatComponentName(vm))); })
        .join('\n')
    } else {
      return ("\n\n(found in " + (formatComponentName(vm)) + ")")
    }
  };
}

/*  */


var uid = 0;

/**
 * A dep is an observable that can have multiple
 * directives subscribing to it.
 */
var Dep = function Dep () {
  this.id = uid++;
  this.subs = [];
};

Dep.prototype.addSub = function addSub (sub) {
  this.subs.push(sub);
};

Dep.prototype.removeSub = function removeSub (sub) {
  remove(this.subs, sub);
};

Dep.prototype.depend = function depend () {
  if (Dep.target) {
    Dep.target.addDep(this);
  }
};

Dep.prototype.notify = function notify () {
  // stabilize the subscriber list first
  var subs = this.subs.slice();
  for (var i = 0, l = subs.length; i < l; i++) {
    subs[i].update();
  }
};

// the current target watcher being evaluated.
// this is globally unique because there could be only one
// watcher being evaluated at any time.
Dep.target = null;
var targetStack = [];

function pushTarget (_target) {
  if (Dep.target) { targetStack.push(Dep.target); }
  Dep.target = _target;
}

function popTarget () {
  Dep.target = targetStack.pop();
}

/*  */

var VNode = function VNode (
  tag,
  data,
  children,
  text,
  elm,
  context,
  componentOptions,
  asyncFactory
) {
  this.tag = tag;
  this.data = data;
  this.children = children;
  this.text = text;
  this.elm = elm;
  this.ns = undefined;
  this.context = context;
  this.fnContext = undefined;
  this.fnOptions = undefined;
  this.fnScopeId = undefined;
  this.key = data && data.key;
  this.componentOptions = componentOptions;
  this.componentInstance = undefined;
  this.parent = undefined;
  this.raw = false;
  this.isStatic = false;
  this.isRootInsert = true;
  this.isComment = false;
  this.isCloned = false;
  this.isOnce = false;
  this.asyncFactory = asyncFactory;
  this.asyncMeta = undefined;
  this.isAsyncPlaceholder = false;
};

var prototypeAccessors = { child: { configurable: true } };

// DEPRECATED: alias for componentInstance for backwards compat.
/* istanbul ignore next */
prototypeAccessors.child.get = function () {
  return this.componentInstance
};

Object.defineProperties( VNode.prototype, prototypeAccessors );

var createEmptyVNode = function (text) {
  if ( text === void 0 ) text = '';

  var node = new VNode();
  node.text = text;
  node.isComment = true;
  return node
};

function createTextVNode (val) {
  return new VNode(undefined, undefined, undefined, String(val))
}

// optimized shallow clone
// used for static nodes and slot nodes because they may be reused across
// multiple renders, cloning them avoids errors when DOM manipulations rely
// on their elm reference.
function cloneVNode (vnode) {
  var cloned = new VNode(
    vnode.tag,
    vnode.data,
    vnode.children,
    vnode.text,
    vnode.elm,
    vnode.context,
    vnode.componentOptions,
    vnode.asyncFactory
  );
  cloned.ns = vnode.ns;
  cloned.isStatic = vnode.isStatic;
  cloned.key = vnode.key;
  cloned.isComment = vnode.isComment;
  cloned.fnContext = vnode.fnContext;
  cloned.fnOptions = vnode.fnOptions;
  cloned.fnScopeId = vnode.fnScopeId;
  cloned.isCloned = true;
  return cloned
}

/*
 * not type checking this file because flow doesn't play well with
 * dynamically accessing methods on Array prototype
 */

var arrayProto = Array.prototype;
var arrayMethods = Object.create(arrayProto);

var methodsToPatch = [
  'push',
  'pop',
  'shift',
  'unshift',
  'splice',
  'sort',
  'reverse'
];

/**
 * Intercept mutating methods and emit events
 */
methodsToPatch.forEach(function (method) {
  // cache original method
  var original = arrayProto[method];
  def(arrayMethods, method, function mutator () {
    var args = [], len = arguments.length;
    while ( len-- ) args[ len ] = arguments[ len ];

    var result = original.apply(this, args);
    var ob = this.__ob__;
    var inserted;
    switch (method) {
      case 'push':
      case 'unshift':
        inserted = args;
        break
      case 'splice':
        inserted = args.slice(2);
        break
    }
    if (inserted) { ob.observeArray(inserted); }
    // notify change
    ob.dep.notify();
    return result
  });
});

/*  */

var arrayKeys = Object.getOwnPropertyNames(arrayMethods);

/**
 * In some cases we may want to disable observation inside a component's
 * update computation.
 */
var shouldObserve = true;

function toggleObserving (value) {
  shouldObserve = value;
}

/**
 * Observer class that is attached to each observed
 * object. Once attached, the observer converts the target
 * object's property keys into getter/setters that
 * collect dependencies and dispatch updates.
 */
var Observer = function Observer (value) {
  this.value = value;
  this.dep = new Dep();
  this.vmCount = 0;
  def(value, '__ob__', this);
  if (Array.isArray(value)) {
    var augment = hasProto
      ? protoAugment
      : copyAugment;
    augment(value, arrayMethods, arrayKeys);
    this.observeArray(value);
  } else {
    this.walk(value);
  }
};

/**
 * Walk through each property and convert them into
 * getter/setters. This method should only be called when
 * value type is Object.
 */
Observer.prototype.walk = function walk (obj) {
  var keys = Object.keys(obj);
  for (var i = 0; i < keys.length; i++) {
    defineReactive(obj, keys[i]);
  }
};

/**
 * Observe a list of Array items.
 */
Observer.prototype.observeArray = function observeArray (items) {
  for (var i = 0, l = items.length; i < l; i++) {
    observe(items[i]);
  }
};

// helpers

/**
 * Augment an target Object or Array by intercepting
 * the prototype chain using __proto__
 */
function protoAugment (target, src, keys) {
  /* eslint-disable no-proto */
  target.__proto__ = src;
  /* eslint-enable no-proto */
}

/**
 * Augment an target Object or Array by defining
 * hidden properties.
 */
/* istanbul ignore next */
function copyAugment (target, src, keys) {
  for (var i = 0, l = keys.length; i < l; i++) {
    var key = keys[i];
    def(target, key, src[key]);
  }
}

/**
 * Attempt to create an observer instance for a value,
 * returns the new observer if successfully observed,
 * or the existing observer if the value already has one.
 */
function observe (value, asRootData) {
  if (!isObject(value) || value instanceof VNode) {
    return
  }
  var ob;
  if (hasOwn(value, '__ob__') && value.__ob__ instanceof Observer) {
    ob = value.__ob__;
  } else if (
    shouldObserve &&
    !isServerRendering() &&
    (Array.isArray(value) || isPlainObject(value)) &&
    Object.isExtensible(value) &&
    !value._isVue
  ) {
    ob = new Observer(value);
  }
  if (asRootData && ob) {
    ob.vmCount++;
  }
  return ob
}

/**
 * Define a reactive property on an Object.
 */
function defineReactive (
  obj,
  key,
  val,
  customSetter,
  shallow
) {
  var dep = new Dep();

  var property = Object.getOwnPropertyDescriptor(obj, key);
  if (property && property.configurable === false) {
    return
  }

  // cater for pre-defined getter/setters
  var getter = property && property.get;
  if (!getter && arguments.length === 2) {
    val = obj[key];
  }
  var setter = property && property.set;

  var childOb = !shallow && observe(val);
  Object.defineProperty(obj, key, {
    enumerable: true,
    configurable: true,
    get: function reactiveGetter () {
      var value = getter ? getter.call(obj) : val;
      if (Dep.target) {
        dep.depend();
        if (childOb) {
          childOb.dep.depend();
          if (Array.isArray(value)) {
            dependArray(value);
          }
        }
      }
      return value
    },
    set: function reactiveSetter (newVal) {
      var value = getter ? getter.call(obj) : val;
      /* eslint-disable no-self-compare */
      if (newVal === value || (newVal !== newVal && value !== value)) {
        return
      }
      /* eslint-enable no-self-compare */
      if (false) {
        customSetter();
      }
      if (setter) {
        setter.call(obj, newVal);
      } else {
        val = newVal;
      }
      childOb = !shallow && observe(newVal);
      dep.notify();
    }
  });
}

/**
 * Set a property on an object. Adds the new property and
 * triggers change notification if the property doesn't
 * already exist.
 */
function set (target, key, val) {
  if (false
  ) {
    warn(("Cannot set reactive property on undefined, null, or primitive value: " + ((target))));
  }
  if (Array.isArray(target) && isValidArrayIndex(key)) {
    target.length = Math.max(target.length, key);
    target.splice(key, 1, val);
    return val
  }
  if (key in target && !(key in Object.prototype)) {
    target[key] = val;
    return val
  }
  var ob = (target).__ob__;
  if (target._isVue || (ob && ob.vmCount)) {
    "production" !== 'production' && warn(
      'Avoid adding reactive properties to a Vue instance or its root $data ' +
      'at runtime - declare it upfront in the data option.'
    );
    return val
  }
  if (!ob) {
    target[key] = val;
    return val
  }
  defineReactive(ob.value, key, val);
  ob.dep.notify();
  return val
}

/**
 * Delete a property and trigger change if necessary.
 */
function del (target, key) {
  if (false
  ) {
    warn(("Cannot delete reactive property on undefined, null, or primitive value: " + ((target))));
  }
  if (Array.isArray(target) && isValidArrayIndex(key)) {
    target.splice(key, 1);
    return
  }
  var ob = (target).__ob__;
  if (target._isVue || (ob && ob.vmCount)) {
    "production" !== 'production' && warn(
      'Avoid deleting properties on a Vue instance or its root $data ' +
      '- just set it to null.'
    );
    return
  }
  if (!hasOwn(target, key)) {
    return
  }
  delete target[key];
  if (!ob) {
    return
  }
  ob.dep.notify();
}

/**
 * Collect dependencies on array elements when the array is touched, since
 * we cannot intercept array element access like property getters.
 */
function dependArray (value) {
  for (var e = (void 0), i = 0, l = value.length; i < l; i++) {
    e = value[i];
    e && e.__ob__ && e.__ob__.dep.depend();
    if (Array.isArray(e)) {
      dependArray(e);
    }
  }
}

/*  */

/**
 * Option overwriting strategies are functions that handle
 * how to merge a parent option value and a child option
 * value into the final value.
 */
var strats = config.optionMergeStrategies;

/**
 * Options with restrictions
 */
if (false) {
  strats.el = strats.propsData = function (parent, child, vm, key) {
    if (!vm) {
      warn(
        "option \"" + key + "\" can only be used during instance " +
        'creation with the `new` keyword.'
      );
    }
    return defaultStrat(parent, child)
  };
}

/**
 * Helper that recursively merges two data objects together.
 */
function mergeData (to, from) {
  if (!from) { return to }
  var key, toVal, fromVal;
  var keys = Object.keys(from);
  for (var i = 0; i < keys.length; i++) {
    key = keys[i];
    toVal = to[key];
    fromVal = from[key];
    if (!hasOwn(to, key)) {
      set(to, key, fromVal);
    } else if (isPlainObject(toVal) && isPlainObject(fromVal)) {
      mergeData(toVal, fromVal);
    }
  }
  return to
}

/**
 * Data
 */
function mergeDataOrFn (
  parentVal,
  childVal,
  vm
) {
  if (!vm) {
    // in a Vue.extend merge, both should be functions
    if (!childVal) {
      return parentVal
    }
    if (!parentVal) {
      return childVal
    }
    // when parentVal & childVal are both present,
    // we need to return a function that returns the
    // merged result of both functions... no need to
    // check if parentVal is a function here because
    // it has to be a function to pass previous merges.
    return function mergedDataFn () {
      return mergeData(
        typeof childVal === 'function' ? childVal.call(this, this) : childVal,
        typeof parentVal === 'function' ? parentVal.call(this, this) : parentVal
      )
    }
  } else {
    return function mergedInstanceDataFn () {
      // instance merge
      var instanceData = typeof childVal === 'function'
        ? childVal.call(vm, vm)
        : childVal;
      var defaultData = typeof parentVal === 'function'
        ? parentVal.call(vm, vm)
        : parentVal;
      if (instanceData) {
        return mergeData(instanceData, defaultData)
      } else {
        return defaultData
      }
    }
  }
}

strats.data = function (
  parentVal,
  childVal,
  vm
) {
  if (!vm) {
    if (childVal && typeof childVal !== 'function') {
      "production" !== 'production' && warn(
        'The "data" option should be a function ' +
        'that returns a per-instance value in component ' +
        'definitions.',
        vm
      );

      return parentVal
    }
    return mergeDataOrFn(parentVal, childVal)
  }

  return mergeDataOrFn(parentVal, childVal, vm)
};

/**
 * Hooks and props are merged as arrays.
 */
function mergeHook (
  parentVal,
  childVal
) {
  return childVal
    ? parentVal
      ? parentVal.concat(childVal)
      : Array.isArray(childVal)
        ? childVal
        : [childVal]
    : parentVal
}

LIFECYCLE_HOOKS.forEach(function (hook) {
  strats[hook] = mergeHook;
});

/**
 * Assets
 *
 * When a vm is present (instance creation), we need to do
 * a three-way merge between constructor options, instance
 * options and parent options.
 */
function mergeAssets (
  parentVal,
  childVal,
  vm,
  key
) {
  var res = Object.create(parentVal || null);
  if (childVal) {
    "production" !== 'production' && assertObjectType(key, childVal, vm);
    return extend(res, childVal)
  } else {
    return res
  }
}

ASSET_TYPES.forEach(function (type) {
  strats[type + 's'] = mergeAssets;
});

/**
 * Watchers.
 *
 * Watchers hashes should not overwrite one
 * another, so we merge them as arrays.
 */
strats.watch = function (
  parentVal,
  childVal,
  vm,
  key
) {
  // work around Firefox's Object.prototype.watch...
  if (parentVal === nativeWatch) { parentVal = undefined; }
  if (childVal === nativeWatch) { childVal = undefined; }
  /* istanbul ignore if */
  if (!childVal) { return Object.create(parentVal || null) }
  if (false) {
    assertObjectType(key, childVal, vm);
  }
  if (!parentVal) { return childVal }
  var ret = {};
  extend(ret, parentVal);
  for (var key$1 in childVal) {
    var parent = ret[key$1];
    var child = childVal[key$1];
    if (parent && !Array.isArray(parent)) {
      parent = [parent];
    }
    ret[key$1] = parent
      ? parent.concat(child)
      : Array.isArray(child) ? child : [child];
  }
  return ret
};

/**
 * Other object hashes.
 */
strats.props =
strats.methods =
strats.inject =
strats.computed = function (
  parentVal,
  childVal,
  vm,
  key
) {
  if (childVal && "production" !== 'production') {
    assertObjectType(key, childVal, vm);
  }
  if (!parentVal) { return childVal }
  var ret = Object.create(null);
  extend(ret, parentVal);
  if (childVal) { extend(ret, childVal); }
  return ret
};
strats.provide = mergeDataOrFn;

/**
 * Default strategy.
 */
var defaultStrat = function (parentVal, childVal) {
  return childVal === undefined
    ? parentVal
    : childVal
};

/**
 * Validate component names
 */
function checkComponents (options) {
  for (var key in options.components) {
    validateComponentName(key);
  }
}

function validateComponentName (name) {
  if (!/^[a-zA-Z][\w-]*$/.test(name)) {
    warn(
      'Invalid component name: "' + name + '". Component names ' +
      'can only contain alphanumeric characters and the hyphen, ' +
      'and must start with a letter.'
    );
  }
  if (isBuiltInTag(name) || config.isReservedTag(name)) {
    warn(
      'Do not use built-in or reserved HTML elements as component ' +
      'id: ' + name
    );
  }
}

/**
 * Ensure all props option syntax are normalized into the
 * Object-based format.
 */
function normalizeProps (options, vm) {
  var props = options.props;
  if (!props) { return }
  var res = {};
  var i, val, name;
  if (Array.isArray(props)) {
    i = props.length;
    while (i--) {
      val = props[i];
      if (typeof val === 'string') {
        name = camelize(val);
        res[name] = { type: null };
      } else if (false) {
        warn('props must be strings when using array syntax.');
      }
    }
  } else if (isPlainObject(props)) {
    for (var key in props) {
      val = props[key];
      name = camelize(key);
      res[name] = isPlainObject(val)
        ? val
        : { type: val };
    }
  } else if (false) {
    warn(
      "Invalid value for option \"props\": expected an Array or an Object, " +
      "but got " + (toRawType(props)) + ".",
      vm
    );
  }
  options.props = res;
}

/**
 * Normalize all injections into Object-based format
 */
function normalizeInject (options, vm) {
  var inject = options.inject;
  if (!inject) { return }
  var normalized = options.inject = {};
  if (Array.isArray(inject)) {
    for (var i = 0; i < inject.length; i++) {
      normalized[inject[i]] = { from: inject[i] };
    }
  } else if (isPlainObject(inject)) {
    for (var key in inject) {
      var val = inject[key];
      normalized[key] = isPlainObject(val)
        ? extend({ from: key }, val)
        : { from: val };
    }
  } else if (false) {
    warn(
      "Invalid value for option \"inject\": expected an Array or an Object, " +
      "but got " + (toRawType(inject)) + ".",
      vm
    );
  }
}

/**
 * Normalize raw function directives into object format.
 */
function normalizeDirectives (options) {
  var dirs = options.directives;
  if (dirs) {
    for (var key in dirs) {
      var def = dirs[key];
      if (typeof def === 'function') {
        dirs[key] = { bind: def, update: def };
      }
    }
  }
}

function assertObjectType (name, value, vm) {
  if (!isPlainObject(value)) {
    warn(
      "Invalid value for option \"" + name + "\": expected an Object, " +
      "but got " + (toRawType(value)) + ".",
      vm
    );
  }
}

/**
 * Merge two option objects into a new one.
 * Core utility used in both instantiation and inheritance.
 */
function mergeOptions (
  parent,
  child,
  vm
) {
  if (false) {
    checkComponents(child);
  }

  if (typeof child === 'function') {
    child = child.options;
  }

  normalizeProps(child, vm);
  normalizeInject(child, vm);
  normalizeDirectives(child);
  var extendsFrom = child.extends;
  if (extendsFrom) {
    parent = mergeOptions(parent, extendsFrom, vm);
  }
  if (child.mixins) {
    for (var i = 0, l = child.mixins.length; i < l; i++) {
      parent = mergeOptions(parent, child.mixins[i], vm);
    }
  }
  var options = {};
  var key;
  for (key in parent) {
    mergeField(key);
  }
  for (key in child) {
    if (!hasOwn(parent, key)) {
      mergeField(key);
    }
  }
  function mergeField (key) {
    var strat = strats[key] || defaultStrat;
    options[key] = strat(parent[key], child[key], vm, key);
  }
  return options
}

/**
 * Resolve an asset.
 * This function is used because child instances need access
 * to assets defined in its ancestor chain.
 */
function resolveAsset (
  options,
  type,
  id,
  warnMissing
) {
  /* istanbul ignore if */
  if (typeof id !== 'string') {
    return
  }
  var assets = options[type];
  // check local registration variations first
  if (hasOwn(assets, id)) { return assets[id] }
  var camelizedId = camelize(id);
  if (hasOwn(assets, camelizedId)) { return assets[camelizedId] }
  var PascalCaseId = capitalize(camelizedId);
  if (hasOwn(assets, PascalCaseId)) { return assets[PascalCaseId] }
  // fallback to prototype chain
  var res = assets[id] || assets[camelizedId] || assets[PascalCaseId];
  if (false) {
    warn(
      'Failed to resolve ' + type.slice(0, -1) + ': ' + id,
      options
    );
  }
  return res
}

/*  */

function validateProp (
  key,
  propOptions,
  propsData,
  vm
) {
  var prop = propOptions[key];
  var absent = !hasOwn(propsData, key);
  var value = propsData[key];
  // boolean casting
  var booleanIndex = getTypeIndex(Boolean, prop.type);
  if (booleanIndex > -1) {
    if (absent && !hasOwn(prop, 'default')) {
      value = false;
    } else if (value === '' || value === hyphenate(key)) {
      // only cast empty string / same name to boolean if
      // boolean has higher priority
      var stringIndex = getTypeIndex(String, prop.type);
      if (stringIndex < 0 || booleanIndex < stringIndex) {
        value = true;
      }
    }
  }
  // check default value
  if (value === undefined) {
    value = getPropDefaultValue(vm, prop, key);
    // since the default value is a fresh copy,
    // make sure to observe it.
    var prevShouldObserve = shouldObserve;
    toggleObserving(true);
    observe(value);
    toggleObserving(prevShouldObserve);
  }
  if (
    false
  ) {
    assertProp(prop, key, value, vm, absent);
  }
  return value
}

/**
 * Get the default value of a prop.
 */
function getPropDefaultValue (vm, prop, key) {
  // no default, return undefined
  if (!hasOwn(prop, 'default')) {
    return undefined
  }
  var def = prop.default;
  // warn against non-factory defaults for Object & Array
  if (false) {
    warn(
      'Invalid default value for prop "' + key + '": ' +
      'Props with type Object/Array must use a factory function ' +
      'to return the default value.',
      vm
    );
  }
  // the raw prop value was also undefined from previous render,
  // return previous default value to avoid unnecessary watcher trigger
  if (vm && vm.$options.propsData &&
    vm.$options.propsData[key] === undefined &&
    vm._props[key] !== undefined
  ) {
    return vm._props[key]
  }
  // call factory function for non-Function types
  // a value is Function if its prototype is function even across different execution context
  return typeof def === 'function' && getType(prop.type) !== 'Function'
    ? def.call(vm)
    : def
}

/**
 * Assert whether a prop is valid.
 */
function assertProp (
  prop,
  name,
  value,
  vm,
  absent
) {
  if (prop.required && absent) {
    warn(
      'Missing required prop: "' + name + '"',
      vm
    );
    return
  }
  if (value == null && !prop.required) {
    return
  }
  var type = prop.type;
  var valid = !type || type === true;
  var expectedTypes = [];
  if (type) {
    if (!Array.isArray(type)) {
      type = [type];
    }
    for (var i = 0; i < type.length && !valid; i++) {
      var assertedType = assertType(value, type[i]);
      expectedTypes.push(assertedType.expectedType || '');
      valid = assertedType.valid;
    }
  }
  if (!valid) {
    warn(
      "Invalid prop: type check failed for prop \"" + name + "\"." +
      " Expected " + (expectedTypes.map(capitalize).join(', ')) +
      ", got " + (toRawType(value)) + ".",
      vm
    );
    return
  }
  var validator = prop.validator;
  if (validator) {
    if (!validator(value)) {
      warn(
        'Invalid prop: custom validator check failed for prop "' + name + '".',
        vm
      );
    }
  }
}

var simpleCheckRE = /^(String|Number|Boolean|Function|Symbol)$/;

function assertType (value, type) {
  var valid;
  var expectedType = getType(type);
  if (simpleCheckRE.test(expectedType)) {
    var t = typeof value;
    valid = t === expectedType.toLowerCase();
    // for primitive wrapper objects
    if (!valid && t === 'object') {
      valid = value instanceof type;
    }
  } else if (expectedType === 'Object') {
    valid = isPlainObject(value);
  } else if (expectedType === 'Array') {
    valid = Array.isArray(value);
  } else {
    valid = value instanceof type;
  }
  return {
    valid: valid,
    expectedType: expectedType
  }
}

/**
 * Use function string name to check built-in types,
 * because a simple equality check will fail when running
 * across different vms / iframes.
 */
function getType (fn) {
  var match = fn && fn.toString().match(/^\s*function (\w+)/);
  return match ? match[1] : ''
}

function isSameType (a, b) {
  return getType(a) === getType(b)
}

function getTypeIndex (type, expectedTypes) {
  if (!Array.isArray(expectedTypes)) {
    return isSameType(expectedTypes, type) ? 0 : -1
  }
  for (var i = 0, len = expectedTypes.length; i < len; i++) {
    if (isSameType(expectedTypes[i], type)) {
      return i
    }
  }
  return -1
}

/*  */

function handleError (err, vm, info) {
  if (vm) {
    var cur = vm;
    while ((cur = cur.$parent)) {
      var hooks = cur.$options.errorCaptured;
      if (hooks) {
        for (var i = 0; i < hooks.length; i++) {
          try {
            var capture = hooks[i].call(cur, err, vm, info) === false;
            if (capture) { return }
          } catch (e) {
            globalHandleError(e, cur, 'errorCaptured hook');
          }
        }
      }
    }
  }
  globalHandleError(err, vm, info);
}

function globalHandleError (err, vm, info) {
  if (config.errorHandler) {
    try {
      return config.errorHandler.call(null, err, vm, info)
    } catch (e) {
      logError(e, null, 'config.errorHandler');
    }
  }
  logError(err, vm, info);
}

function logError (err, vm, info) {
  if (false) {
    warn(("Error in " + info + ": \"" + (err.toString()) + "\""), vm);
  }
  /* istanbul ignore else */
  if ((inBrowser || inWeex) && typeof console !== 'undefined') {
    console.error(err);
  } else {
    throw err
  }
}

/*  */
/* globals MessageChannel */

var callbacks = [];
var pending = false;

function flushCallbacks () {
  pending = false;
  var copies = callbacks.slice(0);
  callbacks.length = 0;
  for (var i = 0; i < copies.length; i++) {
    copies[i]();
  }
}

// Here we have async deferring wrappers using both microtasks and (macro) tasks.
// In < 2.4 we used microtasks everywhere, but there are some scenarios where
// microtasks have too high a priority and fire in between supposedly
// sequential events (e.g. #4521, #6690) or even between bubbling of the same
// event (#6566). However, using (macro) tasks everywhere also has subtle problems
// when state is changed right before repaint (e.g. #6813, out-in transitions).
// Here we use microtask by default, but expose a way to force (macro) task when
// needed (e.g. in event handlers attached by v-on).
var microTimerFunc;
var macroTimerFunc;
var useMacroTask = false;

// Determine (macro) task defer implementation.
// Technically setImmediate should be the ideal choice, but it's only available
// in IE. The only polyfill that consistently queues the callback after all DOM
// events triggered in the same loop is by using MessageChannel.
/* istanbul ignore if */
if (typeof setImmediate !== 'undefined' && isNative(setImmediate)) {
  macroTimerFunc = function () {
    setImmediate(flushCallbacks);
  };
} else if (typeof MessageChannel !== 'undefined' && (
  isNative(MessageChannel) ||
  // PhantomJS
  MessageChannel.toString() === '[object MessageChannelConstructor]'
)) {
  var channel = new MessageChannel();
  var port = channel.port2;
  channel.port1.onmessage = flushCallbacks;
  macroTimerFunc = function () {
    port.postMessage(1);
  };
} else {
  /* istanbul ignore next */
  macroTimerFunc = function () {
    setTimeout(flushCallbacks, 0);
  };
}

// Determine microtask defer implementation.
/* istanbul ignore next, $flow-disable-line */
if (typeof Promise !== 'undefined' && isNative(Promise)) {
  var p = Promise.resolve();
  microTimerFunc = function () {
    p.then(flushCallbacks);
    // in problematic UIWebViews, Promise.then doesn't completely break, but
    // it can get stuck in a weird state where callbacks are pushed into the
    // microtask queue but the queue isn't being flushed, until the browser
    // needs to do some other work, e.g. handle a timer. Therefore we can
    // "force" the microtask queue to be flushed by adding an empty timer.
    if (isIOS) { setTimeout(noop); }
  };
} else {
  // fallback to macro
  microTimerFunc = macroTimerFunc;
}

/**
 * Wrap a function so that if any code inside triggers state change,
 * the changes are queued using a (macro) task instead of a microtask.
 */
function withMacroTask (fn) {
  return fn._withTask || (fn._withTask = function () {
    useMacroTask = true;
    var res = fn.apply(null, arguments);
    useMacroTask = false;
    return res
  })
}

function nextTick (cb, ctx) {
  var _resolve;
  callbacks.push(function () {
    if (cb) {
      try {
        cb.call(ctx);
      } catch (e) {
        handleError(e, ctx, 'nextTick');
      }
    } else if (_resolve) {
      _resolve(ctx);
    }
  });
  if (!pending) {
    pending = true;
    if (useMacroTask) {
      macroTimerFunc();
    } else {
      microTimerFunc();
    }
  }
  // $flow-disable-line
  if (!cb && typeof Promise !== 'undefined') {
    return new Promise(function (resolve) {
      _resolve = resolve;
    })
  }
}

/*  */

var mark;
var measure;

if (false) {
  var perf = inBrowser && window.performance;
  /* istanbul ignore if */
  if (
    perf &&
    perf.mark &&
    perf.measure &&
    perf.clearMarks &&
    perf.clearMeasures
  ) {
    mark = function (tag) { return perf.mark(tag); };
    measure = function (name, startTag, endTag) {
      perf.measure(name, startTag, endTag);
      perf.clearMarks(startTag);
      perf.clearMarks(endTag);
      perf.clearMeasures(name);
    };
  }
}

/* not type checking this file because flow doesn't play well with Proxy */

var initProxy;

if (false) {
  var allowedGlobals = makeMap(
    'Infinity,undefined,NaN,isFinite,isNaN,' +
    'parseFloat,parseInt,decodeURI,decodeURIComponent,encodeURI,encodeURIComponent,' +
    'Math,Number,Date,Array,Object,Boolean,String,RegExp,Map,Set,JSON,Intl,' +
    'require' // for Webpack/Browserify
  );

  var warnNonPresent = function (target, key) {
    warn(
      "Property or method \"" + key + "\" is not defined on the instance but " +
      'referenced during render. Make sure that this property is reactive, ' +
      'either in the data option, or for class-based components, by ' +
      'initializing the property. ' +
      'See: https://vuejs.org/v2/guide/reactivity.html#Declaring-Reactive-Properties.',
      target
    );
  };

  var hasProxy =
    typeof Proxy !== 'undefined' && isNative(Proxy);

  if (hasProxy) {
    var isBuiltInModifier = makeMap('stop,prevent,self,ctrl,shift,alt,meta,exact');
    config.keyCodes = new Proxy(config.keyCodes, {
      set: function set (target, key, value) {
        if (isBuiltInModifier(key)) {
          warn(("Avoid overwriting built-in modifier in config.keyCodes: ." + key));
          return false
        } else {
          target[key] = value;
          return true
        }
      }
    });
  }

  var hasHandler = {
    has: function has (target, key) {
      var has = key in target;
      var isAllowed = allowedGlobals(key) || key.charAt(0) === '_';
      if (!has && !isAllowed) {
        warnNonPresent(target, key);
      }
      return has || !isAllowed
    }
  };

  var getHandler = {
    get: function get (target, key) {
      if (typeof key === 'string' && !(key in target)) {
        warnNonPresent(target, key);
      }
      return target[key]
    }
  };

  initProxy = function initProxy (vm) {
    if (hasProxy) {
      // determine which proxy handler to use
      var options = vm.$options;
      var handlers = options.render && options.render._withStripped
        ? getHandler
        : hasHandler;
      vm._renderProxy = new Proxy(vm, handlers);
    } else {
      vm._renderProxy = vm;
    }
  };
}

/*  */

var seenObjects = new _Set();

/**
 * Recursively traverse an object to evoke all converted
 * getters, so that every nested property inside the object
 * is collected as a "deep" dependency.
 */
function traverse (val) {
  _traverse(val, seenObjects);
  seenObjects.clear();
}

function _traverse (val, seen) {
  var i, keys;
  var isA = Array.isArray(val);
  if ((!isA && !isObject(val)) || Object.isFrozen(val) || val instanceof VNode) {
    return
  }
  if (val.__ob__) {
    var depId = val.__ob__.dep.id;
    if (seen.has(depId)) {
      return
    }
    seen.add(depId);
  }
  if (isA) {
    i = val.length;
    while (i--) { _traverse(val[i], seen); }
  } else {
    keys = Object.keys(val);
    i = keys.length;
    while (i--) { _traverse(val[keys[i]], seen); }
  }
}

/*  */

var normalizeEvent = cached(function (name) {
  var passive = name.charAt(0) === '&';
  name = passive ? name.slice(1) : name;
  var once$$1 = name.charAt(0) === '~'; // Prefixed last, checked first
  name = once$$1 ? name.slice(1) : name;
  var capture = name.charAt(0) === '!';
  name = capture ? name.slice(1) : name;
  return {
    name: name,
    once: once$$1,
    capture: capture,
    passive: passive
  }
});

function createFnInvoker (fns) {
  function invoker () {
    var arguments$1 = arguments;

    var fns = invoker.fns;
    if (Array.isArray(fns)) {
      var cloned = fns.slice();
      for (var i = 0; i < cloned.length; i++) {
        cloned[i].apply(null, arguments$1);
      }
    } else {
      // return handler return value for single handlers
      return fns.apply(null, arguments)
    }
  }
  invoker.fns = fns;
  return invoker
}

function updateListeners (
  on,
  oldOn,
  add,
  remove$$1,
  vm
) {
  var name, def, cur, old, event;
  for (name in on) {
    def = cur = on[name];
    old = oldOn[name];
    event = normalizeEvent(name);
    /* istanbul ignore if */
    if (isUndef(cur)) {
      "production" !== 'production' && warn(
        "Invalid handler for event \"" + (event.name) + "\": got " + String(cur),
        vm
      );
    } else if (isUndef(old)) {
      if (isUndef(cur.fns)) {
        cur = on[name] = createFnInvoker(cur);
      }
      add(event.name, cur, event.once, event.capture, event.passive, event.params);
    } else if (cur !== old) {
      old.fns = cur;
      on[name] = old;
    }
  }
  for (name in oldOn) {
    if (isUndef(on[name])) {
      event = normalizeEvent(name);
      remove$$1(event.name, oldOn[name], event.capture);
    }
  }
}

/*  */

function mergeVNodeHook (def, hookKey, hook) {
  if (def instanceof VNode) {
    def = def.data.hook || (def.data.hook = {});
  }
  var invoker;
  var oldHook = def[hookKey];

  function wrappedHook () {
    hook.apply(this, arguments);
    // important: remove merged hook to ensure it's called only once
    // and prevent memory leak
    remove(invoker.fns, wrappedHook);
  }

  if (isUndef(oldHook)) {
    // no existing hook
    invoker = createFnInvoker([wrappedHook]);
  } else {
    /* istanbul ignore if */
    if (isDef(oldHook.fns) && isTrue(oldHook.merged)) {
      // already a merged invoker
      invoker = oldHook;
      invoker.fns.push(wrappedHook);
    } else {
      // existing plain hook
      invoker = createFnInvoker([oldHook, wrappedHook]);
    }
  }

  invoker.merged = true;
  def[hookKey] = invoker;
}

/*  */

function extractPropsFromVNodeData (
  data,
  Ctor,
  tag
) {
  // we are only extracting raw values here.
  // validation and default values are handled in the child
  // component itself.
  var propOptions = Ctor.options.props;
  if (isUndef(propOptions)) {
    return
  }
  var res = {};
  var attrs = data.attrs;
  var props = data.props;
  if (isDef(attrs) || isDef(props)) {
    for (var key in propOptions) {
      var altKey = hyphenate(key);
      if (false) {
        var keyInLowerCase = key.toLowerCase();
        if (
          key !== keyInLowerCase &&
          attrs && hasOwn(attrs, keyInLowerCase)
        ) {
          tip(
            "Prop \"" + keyInLowerCase + "\" is passed to component " +
            (formatComponentName(tag || Ctor)) + ", but the declared prop name is" +
            " \"" + key + "\". " +
            "Note that HTML attributes are case-insensitive and camelCased " +
            "props need to use their kebab-case equivalents when using in-DOM " +
            "templates. You should probably use \"" + altKey + "\" instead of \"" + key + "\"."
          );
        }
      }
      checkProp(res, props, key, altKey, true) ||
      checkProp(res, attrs, key, altKey, false);
    }
  }
  return res
}

function checkProp (
  res,
  hash,
  key,
  altKey,
  preserve
) {
  if (isDef(hash)) {
    if (hasOwn(hash, key)) {
      res[key] = hash[key];
      if (!preserve) {
        delete hash[key];
      }
      return true
    } else if (hasOwn(hash, altKey)) {
      res[key] = hash[altKey];
      if (!preserve) {
        delete hash[altKey];
      }
      return true
    }
  }
  return false
}

/*  */

// The template compiler attempts to minimize the need for normalization by
// statically analyzing the template at compile time.
//
// For plain HTML markup, normalization can be completely skipped because the
// generated render function is guaranteed to return Array<VNode>. There are
// two cases where extra normalization is needed:

// 1. When the children contains components - because a functional component
// may return an Array instead of a single root. In this case, just a simple
// normalization is needed - if any child is an Array, we flatten the whole
// thing with Array.prototype.concat. It is guaranteed to be only 1-level deep
// because functional components already normalize their own children.
function simpleNormalizeChildren (children) {
  for (var i = 0; i < children.length; i++) {
    if (Array.isArray(children[i])) {
      return Array.prototype.concat.apply([], children)
    }
  }
  return children
}

// 2. When the children contains constructs that always generated nested Arrays,
// e.g. <template>, <slot>, v-for, or when the children is provided by user
// with hand-written render functions / JSX. In such cases a full normalization
// is needed to cater to all possible types of children values.
function normalizeChildren (children) {
  return isPrimitive(children)
    ? [createTextVNode(children)]
    : Array.isArray(children)
      ? normalizeArrayChildren(children)
      : undefined
}

function isTextNode (node) {
  return isDef(node) && isDef(node.text) && isFalse(node.isComment)
}

function normalizeArrayChildren (children, nestedIndex) {
  var res = [];
  var i, c, lastIndex, last;
  for (i = 0; i < children.length; i++) {
    c = children[i];
    if (isUndef(c) || typeof c === 'boolean') { continue }
    lastIndex = res.length - 1;
    last = res[lastIndex];
    //  nested
    if (Array.isArray(c)) {
      if (c.length > 0) {
        c = normalizeArrayChildren(c, ((nestedIndex || '') + "_" + i));
        // merge adjacent text nodes
        if (isTextNode(c[0]) && isTextNode(last)) {
          res[lastIndex] = createTextVNode(last.text + (c[0]).text);
          c.shift();
        }
        res.push.apply(res, c);
      }
    } else if (isPrimitive(c)) {
      if (isTextNode(last)) {
        // merge adjacent text nodes
        // this is necessary for SSR hydration because text nodes are
        // essentially merged when rendered to HTML strings
        res[lastIndex] = createTextVNode(last.text + c);
      } else if (c !== '') {
        // convert primitive to vnode
        res.push(createTextVNode(c));
      }
    } else {
      if (isTextNode(c) && isTextNode(last)) {
        // merge adjacent text nodes
        res[lastIndex] = createTextVNode(last.text + c.text);
      } else {
        // default key for nested array children (likely generated by v-for)
        if (isTrue(children._isVList) &&
          isDef(c.tag) &&
          isUndef(c.key) &&
          isDef(nestedIndex)) {
          c.key = "__vlist" + nestedIndex + "_" + i + "__";
        }
        res.push(c);
      }
    }
  }
  return res
}

/*  */

function ensureCtor (comp, base) {
  if (
    comp.__esModule ||
    (hasSymbol && comp[Symbol.toStringTag] === 'Module')
  ) {
    comp = comp.default;
  }
  return isObject(comp)
    ? base.extend(comp)
    : comp
}

function createAsyncPlaceholder (
  factory,
  data,
  context,
  children,
  tag
) {
  var node = createEmptyVNode();
  node.asyncFactory = factory;
  node.asyncMeta = { data: data, context: context, children: children, tag: tag };
  return node
}

function resolveAsyncComponent (
  factory,
  baseCtor,
  context
) {
  if (isTrue(factory.error) && isDef(factory.errorComp)) {
    return factory.errorComp
  }

  if (isDef(factory.resolved)) {
    return factory.resolved
  }

  if (isTrue(factory.loading) && isDef(factory.loadingComp)) {
    return factory.loadingComp
  }

  if (isDef(factory.contexts)) {
    // already pending
    factory.contexts.push(context);
  } else {
    var contexts = factory.contexts = [context];
    var sync = true;

    var forceRender = function () {
      for (var i = 0, l = contexts.length; i < l; i++) {
        contexts[i].$forceUpdate();
      }
    };

    var resolve = once(function (res) {
      // cache resolved
      factory.resolved = ensureCtor(res, baseCtor);
      // invoke callbacks only if this is not a synchronous resolve
      // (async resolves are shimmed as synchronous during SSR)
      if (!sync) {
        forceRender();
      }
    });

    var reject = once(function (reason) {
      "production" !== 'production' && warn(
        "Failed to resolve async component: " + (String(factory)) +
        (reason ? ("\nReason: " + reason) : '')
      );
      if (isDef(factory.errorComp)) {
        factory.error = true;
        forceRender();
      }
    });

    var res = factory(resolve, reject);

    if (isObject(res)) {
      if (typeof res.then === 'function') {
        // () => Promise
        if (isUndef(factory.resolved)) {
          res.then(resolve, reject);
        }
      } else if (isDef(res.component) && typeof res.component.then === 'function') {
        res.component.then(resolve, reject);

        if (isDef(res.error)) {
          factory.errorComp = ensureCtor(res.error, baseCtor);
        }

        if (isDef(res.loading)) {
          factory.loadingComp = ensureCtor(res.loading, baseCtor);
          if (res.delay === 0) {
            factory.loading = true;
          } else {
            setTimeout(function () {
              if (isUndef(factory.resolved) && isUndef(factory.error)) {
                factory.loading = true;
                forceRender();
              }
            }, res.delay || 200);
          }
        }

        if (isDef(res.timeout)) {
          setTimeout(function () {
            if (isUndef(factory.resolved)) {
              reject(
                 false
                  ? ("timeout (" + (res.timeout) + "ms)")
                  : null
              );
            }
          }, res.timeout);
        }
      }
    }

    sync = false;
    // return in case resolved synchronously
    return factory.loading
      ? factory.loadingComp
      : factory.resolved
  }
}

/*  */

function isAsyncPlaceholder (node) {
  return node.isComment && node.asyncFactory
}

/*  */

function getFirstComponentChild (children) {
  if (Array.isArray(children)) {
    for (var i = 0; i < children.length; i++) {
      var c = children[i];
      if (isDef(c) && (isDef(c.componentOptions) || isAsyncPlaceholder(c))) {
        return c
      }
    }
  }
}

/*  */

/*  */

function initEvents (vm) {
  vm._events = Object.create(null);
  vm._hasHookEvent = false;
  // init parent attached events
  var listeners = vm.$options._parentListeners;
  if (listeners) {
    updateComponentListeners(vm, listeners);
  }
}

var target;

function add (event, fn, once) {
  if (once) {
    target.$once(event, fn);
  } else {
    target.$on(event, fn);
  }
}

function remove$1 (event, fn) {
  target.$off(event, fn);
}

function updateComponentListeners (
  vm,
  listeners,
  oldListeners
) {
  target = vm;
  updateListeners(listeners, oldListeners || {}, add, remove$1, vm);
  target = undefined;
}

function eventsMixin (Vue) {
  var hookRE = /^hook:/;
  Vue.prototype.$on = function (event, fn) {
    var this$1 = this;

    var vm = this;
    if (Array.isArray(event)) {
      for (var i = 0, l = event.length; i < l; i++) {
        this$1.$on(event[i], fn);
      }
    } else {
      (vm._events[event] || (vm._events[event] = [])).push(fn);
      // optimize hook:event cost by using a boolean flag marked at registration
      // instead of a hash lookup
      if (hookRE.test(event)) {
        vm._hasHookEvent = true;
      }
    }
    return vm
  };

  Vue.prototype.$once = function (event, fn) {
    var vm = this;
    function on () {
      vm.$off(event, on);
      fn.apply(vm, arguments);
    }
    on.fn = fn;
    vm.$on(event, on);
    return vm
  };

  Vue.prototype.$off = function (event, fn) {
    var this$1 = this;

    var vm = this;
    // all
    if (!arguments.length) {
      vm._events = Object.create(null);
      return vm
    }
    // array of events
    if (Array.isArray(event)) {
      for (var i = 0, l = event.length; i < l; i++) {
        this$1.$off(event[i], fn);
      }
      return vm
    }
    // specific event
    var cbs = vm._events[event];
    if (!cbs) {
      return vm
    }
    if (!fn) {
      vm._events[event] = null;
      return vm
    }
    if (fn) {
      // specific handler
      var cb;
      var i$1 = cbs.length;
      while (i$1--) {
        cb = cbs[i$1];
        if (cb === fn || cb.fn === fn) {
          cbs.splice(i$1, 1);
          break
        }
      }
    }
    return vm
  };

  Vue.prototype.$emit = function (event) {
    var vm = this;
    if (false) {
      var lowerCaseEvent = event.toLowerCase();
      if (lowerCaseEvent !== event && vm._events[lowerCaseEvent]) {
        tip(
          "Event \"" + lowerCaseEvent + "\" is emitted in component " +
          (formatComponentName(vm)) + " but the handler is registered for \"" + event + "\". " +
          "Note that HTML attributes are case-insensitive and you cannot use " +
          "v-on to listen to camelCase events when using in-DOM templates. " +
          "You should probably use \"" + (hyphenate(event)) + "\" instead of \"" + event + "\"."
        );
      }
    }
    var cbs = vm._events[event];
    if (cbs) {
      cbs = cbs.length > 1 ? toArray(cbs) : cbs;
      var args = toArray(arguments, 1);
      for (var i = 0, l = cbs.length; i < l; i++) {
        try {
          cbs[i].apply(vm, args);
        } catch (e) {
          handleError(e, vm, ("event handler for \"" + event + "\""));
        }
      }
    }
    return vm
  };
}

/*  */



/**
 * Runtime helper for resolving raw children VNodes into a slot object.
 */
function resolveSlots (
  children,
  context
) {
  var slots = {};
  if (!children) {
    return slots
  }
  for (var i = 0, l = children.length; i < l; i++) {
    var child = children[i];
    var data = child.data;
    // remove slot attribute if the node is resolved as a Vue slot node
    if (data && data.attrs && data.attrs.slot) {
      delete data.attrs.slot;
    }
    // named slots should only be respected if the vnode was rendered in the
    // same context.
    if ((child.context === context || child.fnContext === context) &&
      data && data.slot != null
    ) {
      var name = data.slot;
      var slot = (slots[name] || (slots[name] = []));
      if (child.tag === 'template') {
        slot.push.apply(slot, child.children || []);
      } else {
        slot.push(child);
      }
    } else {
      (slots.default || (slots.default = [])).push(child);
    }
  }
  // ignore slots that contains only whitespace
  for (var name$1 in slots) {
    if (slots[name$1].every(isWhitespace)) {
      delete slots[name$1];
    }
  }
  return slots
}

function isWhitespace (node) {
  return (node.isComment && !node.asyncFactory) || node.text === ' '
}

function resolveScopedSlots (
  fns, // see flow/vnode
  res
) {
  res = res || {};
  for (var i = 0; i < fns.length; i++) {
    if (Array.isArray(fns[i])) {
      resolveScopedSlots(fns[i], res);
    } else {
      res[fns[i].key] = fns[i].fn;
    }
  }
  return res
}

/*  */

var activeInstance = null;
var isUpdatingChildComponent = false;

function initLifecycle (vm) {
  var options = vm.$options;

  // locate first non-abstract parent
  var parent = options.parent;
  if (parent && !options.abstract) {
    while (parent.$options.abstract && parent.$parent) {
      parent = parent.$parent;
    }
    parent.$children.push(vm);
  }

  vm.$parent = parent;
  vm.$root = parent ? parent.$root : vm;

  vm.$children = [];
  vm.$refs = {};

  vm._watcher = null;
  vm._inactive = null;
  vm._directInactive = false;
  vm._isMounted = false;
  vm._isDestroyed = false;
  vm._isBeingDestroyed = false;
}

function lifecycleMixin (Vue) {
  Vue.prototype._update = function (vnode, hydrating) {
    var vm = this;
    if (vm._isMounted) {
      callHook(vm, 'beforeUpdate');
    }
    var prevEl = vm.$el;
    var prevVnode = vm._vnode;
    var prevActiveInstance = activeInstance;
    activeInstance = vm;
    vm._vnode = vnode;
    // Vue.prototype.__patch__ is injected in entry points
    // based on the rendering backend used.
    if (!prevVnode) {
      // initial render
      vm.$el = vm.__patch__(
        vm.$el, vnode, hydrating, false /* removeOnly */,
        vm.$options._parentElm,
        vm.$options._refElm
      );
      // no need for the ref nodes after initial patch
      // this prevents keeping a detached DOM tree in memory (#5851)
      vm.$options._parentElm = vm.$options._refElm = null;
    } else {
      // updates
      vm.$el = vm.__patch__(prevVnode, vnode);
    }
    activeInstance = prevActiveInstance;
    // update __vue__ reference
    if (prevEl) {
      prevEl.__vue__ = null;
    }
    if (vm.$el) {
      vm.$el.__vue__ = vm;
    }
    // if parent is an HOC, update its $el as well
    if (vm.$vnode && vm.$parent && vm.$vnode === vm.$parent._vnode) {
      vm.$parent.$el = vm.$el;
    }
    // updated hook is called by the scheduler to ensure that children are
    // updated in a parent's updated hook.
  };

  Vue.prototype.$forceUpdate = function () {
    var vm = this;
    if (vm._watcher) {
      vm._watcher.update();
    }
  };

  Vue.prototype.$destroy = function () {
    var vm = this;
    if (vm._isBeingDestroyed) {
      return
    }
    callHook(vm, 'beforeDestroy');
    vm._isBeingDestroyed = true;
    // remove self from parent
    var parent = vm.$parent;
    if (parent && !parent._isBeingDestroyed && !vm.$options.abstract) {
      remove(parent.$children, vm);
    }
    // teardown watchers
    if (vm._watcher) {
      vm._watcher.teardown();
    }
    var i = vm._watchers.length;
    while (i--) {
      vm._watchers[i].teardown();
    }
    // remove reference from data ob
    // frozen object may not have observer.
    if (vm._data.__ob__) {
      vm._data.__ob__.vmCount--;
    }
    // call the last hook...
    vm._isDestroyed = true;
    // invoke destroy hooks on current rendered tree
    vm.__patch__(vm._vnode, null);
    // fire destroyed hook
    callHook(vm, 'destroyed');
    // turn off all instance listeners.
    vm.$off();
    // remove __vue__ reference
    if (vm.$el) {
      vm.$el.__vue__ = null;
    }
    // release circular reference (#6759)
    if (vm.$vnode) {
      vm.$vnode.parent = null;
    }
  };
}

function mountComponent (
  vm,
  el,
  hydrating
) {
  vm.$el = el;
  if (!vm.$options.render) {
    vm.$options.render = createEmptyVNode;
    if (false) {
      /* istanbul ignore if */
      if ((vm.$options.template && vm.$options.template.charAt(0) !== '#') ||
        vm.$options.el || el) {
        warn(
          'You are using the runtime-only build of Vue where the template ' +
          'compiler is not available. Either pre-compile the templates into ' +
          'render functions, or use the compiler-included build.',
          vm
        );
      } else {
        warn(
          'Failed to mount component: template or render function not defined.',
          vm
        );
      }
    }
  }
  callHook(vm, 'beforeMount');

  var updateComponent;
  /* istanbul ignore if */
  if (false) {
    updateComponent = function () {
      var name = vm._name;
      var id = vm._uid;
      var startTag = "vue-perf-start:" + id;
      var endTag = "vue-perf-end:" + id;

      mark(startTag);
      var vnode = vm._render();
      mark(endTag);
      measure(("vue " + name + " render"), startTag, endTag);

      mark(startTag);
      vm._update(vnode, hydrating);
      mark(endTag);
      measure(("vue " + name + " patch"), startTag, endTag);
    };
  } else {
    updateComponent = function () {
      vm._update(vm._render(), hydrating);
    };
  }

  // we set this to vm._watcher inside the watcher's constructor
  // since the watcher's initial patch may call $forceUpdate (e.g. inside child
  // component's mounted hook), which relies on vm._watcher being already defined
  new Watcher(vm, updateComponent, noop, null, true /* isRenderWatcher */);
  hydrating = false;

  // manually mounted instance, call mounted on self
  // mounted is called for render-created child components in its inserted hook
  if (vm.$vnode == null) {
    vm._isMounted = true;
    callHook(vm, 'mounted');
  }
  return vm
}

function updateChildComponent (
  vm,
  propsData,
  listeners,
  parentVnode,
  renderChildren
) {
  if (false) {
    isUpdatingChildComponent = true;
  }

  // determine whether component has slot children
  // we need to do this before overwriting $options._renderChildren
  var hasChildren = !!(
    renderChildren ||               // has new static slots
    vm.$options._renderChildren ||  // has old static slots
    parentVnode.data.scopedSlots || // has new scoped slots
    vm.$scopedSlots !== emptyObject // has old scoped slots
  );

  vm.$options._parentVnode = parentVnode;
  vm.$vnode = parentVnode; // update vm's placeholder node without re-render

  if (vm._vnode) { // update child tree's parent
    vm._vnode.parent = parentVnode;
  }
  vm.$options._renderChildren = renderChildren;

  // update $attrs and $listeners hash
  // these are also reactive so they may trigger child update if the child
  // used them during render
  vm.$attrs = parentVnode.data.attrs || emptyObject;
  vm.$listeners = listeners || emptyObject;

  // update props
  if (propsData && vm.$options.props) {
    toggleObserving(false);
    var props = vm._props;
    var propKeys = vm.$options._propKeys || [];
    for (var i = 0; i < propKeys.length; i++) {
      var key = propKeys[i];
      var propOptions = vm.$options.props; // wtf flow?
      props[key] = validateProp(key, propOptions, propsData, vm);
    }
    toggleObserving(true);
    // keep a copy of raw propsData
    vm.$options.propsData = propsData;
  }

  // update listeners
  listeners = listeners || emptyObject;
  var oldListeners = vm.$options._parentListeners;
  vm.$options._parentListeners = listeners;
  updateComponentListeners(vm, listeners, oldListeners);

  // resolve slots + force update if has children
  if (hasChildren) {
    vm.$slots = resolveSlots(renderChildren, parentVnode.context);
    vm.$forceUpdate();
  }

  if (false) {
    isUpdatingChildComponent = false;
  }
}

function isInInactiveTree (vm) {
  while (vm && (vm = vm.$parent)) {
    if (vm._inactive) { return true }
  }
  return false
}

function activateChildComponent (vm, direct) {
  if (direct) {
    vm._directInactive = false;
    if (isInInactiveTree(vm)) {
      return
    }
  } else if (vm._directInactive) {
    return
  }
  if (vm._inactive || vm._inactive === null) {
    vm._inactive = false;
    for (var i = 0; i < vm.$children.length; i++) {
      activateChildComponent(vm.$children[i]);
    }
    callHook(vm, 'activated');
  }
}

function deactivateChildComponent (vm, direct) {
  if (direct) {
    vm._directInactive = true;
    if (isInInactiveTree(vm)) {
      return
    }
  }
  if (!vm._inactive) {
    vm._inactive = true;
    for (var i = 0; i < vm.$children.length; i++) {
      deactivateChildComponent(vm.$children[i]);
    }
    callHook(vm, 'deactivated');
  }
}

function callHook (vm, hook) {
  // #7573 disable dep collection when invoking lifecycle hooks
  pushTarget();
  var handlers = vm.$options[hook];
  if (handlers) {
    for (var i = 0, j = handlers.length; i < j; i++) {
      try {
        handlers[i].call(vm);
      } catch (e) {
        handleError(e, vm, (hook + " hook"));
      }
    }
  }
  if (vm._hasHookEvent) {
    vm.$emit('hook:' + hook);
  }
  popTarget();
}

/*  */


var MAX_UPDATE_COUNT = 100;

var queue = [];
var activatedChildren = [];
var has = {};
var circular = {};
var waiting = false;
var flushing = false;
var index = 0;

/**
 * Reset the scheduler's state.
 */
function resetSchedulerState () {
  index = queue.length = activatedChildren.length = 0;
  has = {};
  if (false) {
    circular = {};
  }
  waiting = flushing = false;
}

/**
 * Flush both queues and run the watchers.
 */
function flushSchedulerQueue () {
  flushing = true;
  var watcher, id;

  // Sort queue before flush.
  // This ensures that:
  // 1. Components are updated from parent to child. (because parent is always
  //    created before the child)
  // 2. A component's user watchers are run before its render watcher (because
  //    user watchers are created before the render watcher)
  // 3. If a component is destroyed during a parent component's watcher run,
  //    its watchers can be skipped.
  queue.sort(function (a, b) { return a.id - b.id; });

  // do not cache length because more watchers might be pushed
  // as we run existing watchers
  for (index = 0; index < queue.length; index++) {
    watcher = queue[index];
    id = watcher.id;
    has[id] = null;
    watcher.run();
    // in dev build, check and stop circular updates.
    if (false) {
      circular[id] = (circular[id] || 0) + 1;
      if (circular[id] > MAX_UPDATE_COUNT) {
        warn(
          'You may have an infinite update loop ' + (
            watcher.user
              ? ("in watcher with expression \"" + (watcher.expression) + "\"")
              : "in a component render function."
          ),
          watcher.vm
        );
        break
      }
    }
  }

  // keep copies of post queues before resetting state
  var activatedQueue = activatedChildren.slice();
  var updatedQueue = queue.slice();

  resetSchedulerState();

  // call component updated and activated hooks
  callActivatedHooks(activatedQueue);
  callUpdatedHooks(updatedQueue);

  // devtool hook
  /* istanbul ignore if */
  if (devtools && config.devtools) {
    devtools.emit('flush');
  }
}

function callUpdatedHooks (queue) {
  var i = queue.length;
  while (i--) {
    var watcher = queue[i];
    var vm = watcher.vm;
    if (vm._watcher === watcher && vm._isMounted) {
      callHook(vm, 'updated');
    }
  }
}

/**
 * Queue a kept-alive component that was activated during patch.
 * The queue will be processed after the entire tree has been patched.
 */
function queueActivatedComponent (vm) {
  // setting _inactive to false here so that a render function can
  // rely on checking whether it's in an inactive tree (e.g. router-view)
  vm._inactive = false;
  activatedChildren.push(vm);
}

function callActivatedHooks (queue) {
  for (var i = 0; i < queue.length; i++) {
    queue[i]._inactive = true;
    activateChildComponent(queue[i], true /* true */);
  }
}

/**
 * Push a watcher into the watcher queue.
 * Jobs with duplicate IDs will be skipped unless it's
 * pushed when the queue is being flushed.
 */
function queueWatcher (watcher) {
  var id = watcher.id;
  if (has[id] == null) {
    has[id] = true;
    if (!flushing) {
      queue.push(watcher);
    } else {
      // if already flushing, splice the watcher based on its id
      // if already past its id, it will be run next immediately.
      var i = queue.length - 1;
      while (i > index && queue[i].id > watcher.id) {
        i--;
      }
      queue.splice(i + 1, 0, watcher);
    }
    // queue the flush
    if (!waiting) {
      waiting = true;
      nextTick(flushSchedulerQueue);
    }
  }
}

/*  */

var uid$1 = 0;

/**
 * A watcher parses an expression, collects dependencies,
 * and fires callback when the expression value changes.
 * This is used for both the $watch() api and directives.
 */
var Watcher = function Watcher (
  vm,
  expOrFn,
  cb,
  options,
  isRenderWatcher
) {
  this.vm = vm;
  if (isRenderWatcher) {
    vm._watcher = this;
  }
  vm._watchers.push(this);
  // options
  if (options) {
    this.deep = !!options.deep;
    this.user = !!options.user;
    this.lazy = !!options.lazy;
    this.sync = !!options.sync;
  } else {
    this.deep = this.user = this.lazy = this.sync = false;
  }
  this.cb = cb;
  this.id = ++uid$1; // uid for batching
  this.active = true;
  this.dirty = this.lazy; // for lazy watchers
  this.deps = [];
  this.newDeps = [];
  this.depIds = new _Set();
  this.newDepIds = new _Set();
  this.expression =  false
    ? expOrFn.toString()
    : '';
  // parse expression for getter
  if (typeof expOrFn === 'function') {
    this.getter = expOrFn;
  } else {
    this.getter = parsePath(expOrFn);
    if (!this.getter) {
      this.getter = function () {};
      "production" !== 'production' && warn(
        "Failed watching path: \"" + expOrFn + "\" " +
        'Watcher only accepts simple dot-delimited paths. ' +
        'For full control, use a function instead.',
        vm
      );
    }
  }
  this.value = this.lazy
    ? undefined
    : this.get();
};

/**
 * Evaluate the getter, and re-collect dependencies.
 */
Watcher.prototype.get = function get () {
  pushTarget(this);
  var value;
  var vm = this.vm;
  try {
    value = this.getter.call(vm, vm);
  } catch (e) {
    if (this.user) {
      handleError(e, vm, ("getter for watcher \"" + (this.expression) + "\""));
    } else {
      throw e
    }
  } finally {
    // "touch" every property so they are all tracked as
    // dependencies for deep watching
    if (this.deep) {
      traverse(value);
    }
    popTarget();
    this.cleanupDeps();
  }
  return value
};

/**
 * Add a dependency to this directive.
 */
Watcher.prototype.addDep = function addDep (dep) {
  var id = dep.id;
  if (!this.newDepIds.has(id)) {
    this.newDepIds.add(id);
    this.newDeps.push(dep);
    if (!this.depIds.has(id)) {
      dep.addSub(this);
    }
  }
};

/**
 * Clean up for dependency collection.
 */
Watcher.prototype.cleanupDeps = function cleanupDeps () {
    var this$1 = this;

  var i = this.deps.length;
  while (i--) {
    var dep = this$1.deps[i];
    if (!this$1.newDepIds.has(dep.id)) {
      dep.removeSub(this$1);
    }
  }
  var tmp = this.depIds;
  this.depIds = this.newDepIds;
  this.newDepIds = tmp;
  this.newDepIds.clear();
  tmp = this.deps;
  this.deps = this.newDeps;
  this.newDeps = tmp;
  this.newDeps.length = 0;
};

/**
 * Subscriber interface.
 * Will be called when a dependency changes.
 */
Watcher.prototype.update = function update () {
  /* istanbul ignore else */
  if (this.lazy) {
    this.dirty = true;
  } else if (this.sync) {
    this.run();
  } else {
    queueWatcher(this);
  }
};

/**
 * Scheduler job interface.
 * Will be called by the scheduler.
 */
Watcher.prototype.run = function run () {
  if (this.active) {
    var value = this.get();
    if (
      value !== this.value ||
      // Deep watchers and watchers on Object/Arrays should fire even
      // when the value is the same, because the value may
      // have mutated.
      isObject(value) ||
      this.deep
    ) {
      // set new value
      var oldValue = this.value;
      this.value = value;
      if (this.user) {
        try {
          this.cb.call(this.vm, value, oldValue);
        } catch (e) {
          handleError(e, this.vm, ("callback for watcher \"" + (this.expression) + "\""));
        }
      } else {
        this.cb.call(this.vm, value, oldValue);
      }
    }
  }
};

/**
 * Evaluate the value of the watcher.
 * This only gets called for lazy watchers.
 */
Watcher.prototype.evaluate = function evaluate () {
  this.value = this.get();
  this.dirty = false;
};

/**
 * Depend on all deps collected by this watcher.
 */
Watcher.prototype.depend = function depend () {
    var this$1 = this;

  var i = this.deps.length;
  while (i--) {
    this$1.deps[i].depend();
  }
};

/**
 * Remove self from all dependencies' subscriber list.
 */
Watcher.prototype.teardown = function teardown () {
    var this$1 = this;

  if (this.active) {
    // remove self from vm's watcher list
    // this is a somewhat expensive operation so we skip it
    // if the vm is being destroyed.
    if (!this.vm._isBeingDestroyed) {
      remove(this.vm._watchers, this);
    }
    var i = this.deps.length;
    while (i--) {
      this$1.deps[i].removeSub(this$1);
    }
    this.active = false;
  }
};

/*  */

var sharedPropertyDefinition = {
  enumerable: true,
  configurable: true,
  get: noop,
  set: noop
};

function proxy (target, sourceKey, key) {
  sharedPropertyDefinition.get = function proxyGetter () {
    return this[sourceKey][key]
  };
  sharedPropertyDefinition.set = function proxySetter (val) {
    this[sourceKey][key] = val;
  };
  Object.defineProperty(target, key, sharedPropertyDefinition);
}

function initState (vm) {
  vm._watchers = [];
  var opts = vm.$options;
  if (opts.props) { initProps(vm, opts.props); }
  if (opts.methods) { initMethods(vm, opts.methods); }
  if (opts.data) {
    initData(vm);
  } else {
    observe(vm._data = {}, true /* asRootData */);
  }
  if (opts.computed) { initComputed(vm, opts.computed); }
  if (opts.watch && opts.watch !== nativeWatch) {
    initWatch(vm, opts.watch);
  }
}

function initProps (vm, propsOptions) {
  var propsData = vm.$options.propsData || {};
  var props = vm._props = {};
  // cache prop keys so that future props updates can iterate using Array
  // instead of dynamic object key enumeration.
  var keys = vm.$options._propKeys = [];
  var isRoot = !vm.$parent;
  // root instance props should be converted
  if (!isRoot) {
    toggleObserving(false);
  }
  var loop = function ( key ) {
    keys.push(key);
    var value = validateProp(key, propsOptions, propsData, vm);
    /* istanbul ignore else */
    if (false) {
      var hyphenatedKey = hyphenate(key);
      if (isReservedAttribute(hyphenatedKey) ||
          config.isReservedAttr(hyphenatedKey)) {
        warn(
          ("\"" + hyphenatedKey + "\" is a reserved attribute and cannot be used as component prop."),
          vm
        );
      }
      defineReactive(props, key, value, function () {
        if (vm.$parent && !isUpdatingChildComponent) {
          warn(
            "Avoid mutating a prop directly since the value will be " +
            "overwritten whenever the parent component re-renders. " +
            "Instead, use a data or computed property based on the prop's " +
            "value. Prop being mutated: \"" + key + "\"",
            vm
          );
        }
      });
    } else {
      defineReactive(props, key, value);
    }
    // static props are already proxied on the component's prototype
    // during Vue.extend(). We only need to proxy props defined at
    // instantiation here.
    if (!(key in vm)) {
      proxy(vm, "_props", key);
    }
  };

  for (var key in propsOptions) loop( key );
  toggleObserving(true);
}

function initData (vm) {
  var data = vm.$options.data;
  data = vm._data = typeof data === 'function'
    ? getData(data, vm)
    : data || {};
  if (!isPlainObject(data)) {
    data = {};
    "production" !== 'production' && warn(
      'data functions should return an object:\n' +
      'https://vuejs.org/v2/guide/components.html#data-Must-Be-a-Function',
      vm
    );
  }
  // proxy data on instance
  var keys = Object.keys(data);
  var props = vm.$options.props;
  var methods = vm.$options.methods;
  var i = keys.length;
  while (i--) {
    var key = keys[i];
    if (false) {
      if (methods && hasOwn(methods, key)) {
        warn(
          ("Method \"" + key + "\" has already been defined as a data property."),
          vm
        );
      }
    }
    if (props && hasOwn(props, key)) {
      "production" !== 'production' && warn(
        "The data property \"" + key + "\" is already declared as a prop. " +
        "Use prop default value instead.",
        vm
      );
    } else if (!isReserved(key)) {
      proxy(vm, "_data", key);
    }
  }
  // observe data
  observe(data, true /* asRootData */);
}

function getData (data, vm) {
  // #7573 disable dep collection when invoking data getters
  pushTarget();
  try {
    return data.call(vm, vm)
  } catch (e) {
    handleError(e, vm, "data()");
    return {}
  } finally {
    popTarget();
  }
}

var computedWatcherOptions = { lazy: true };

function initComputed (vm, computed) {
  // $flow-disable-line
  var watchers = vm._computedWatchers = Object.create(null);
  // computed properties are just getters during SSR
  var isSSR = isServerRendering();

  for (var key in computed) {
    var userDef = computed[key];
    var getter = typeof userDef === 'function' ? userDef : userDef.get;
    if (false) {
      warn(
        ("Getter is missing for computed property \"" + key + "\"."),
        vm
      );
    }

    if (!isSSR) {
      // create internal watcher for the computed property.
      watchers[key] = new Watcher(
        vm,
        getter || noop,
        noop,
        computedWatcherOptions
      );
    }

    // component-defined computed properties are already defined on the
    // component prototype. We only need to define computed properties defined
    // at instantiation here.
    if (!(key in vm)) {
      defineComputed(vm, key, userDef);
    } else if (false) {
      if (key in vm.$data) {
        warn(("The computed property \"" + key + "\" is already defined in data."), vm);
      } else if (vm.$options.props && key in vm.$options.props) {
        warn(("The computed property \"" + key + "\" is already defined as a prop."), vm);
      }
    }
  }
}

function defineComputed (
  target,
  key,
  userDef
) {
  var shouldCache = !isServerRendering();
  if (typeof userDef === 'function') {
    sharedPropertyDefinition.get = shouldCache
      ? createComputedGetter(key)
      : userDef;
    sharedPropertyDefinition.set = noop;
  } else {
    sharedPropertyDefinition.get = userDef.get
      ? shouldCache && userDef.cache !== false
        ? createComputedGetter(key)
        : userDef.get
      : noop;
    sharedPropertyDefinition.set = userDef.set
      ? userDef.set
      : noop;
  }
  if (false) {
    sharedPropertyDefinition.set = function () {
      warn(
        ("Computed property \"" + key + "\" was assigned to but it has no setter."),
        this
      );
    };
  }
  Object.defineProperty(target, key, sharedPropertyDefinition);
}

function createComputedGetter (key) {
  return function computedGetter () {
    var watcher = this._computedWatchers && this._computedWatchers[key];
    if (watcher) {
      if (watcher.dirty) {
        watcher.evaluate();
      }
      if (Dep.target) {
        watcher.depend();
      }
      return watcher.value
    }
  }
}

function initMethods (vm, methods) {
  var props = vm.$options.props;
  for (var key in methods) {
    if (false) {
      if (methods[key] == null) {
        warn(
          "Method \"" + key + "\" has an undefined value in the component definition. " +
          "Did you reference the function correctly?",
          vm
        );
      }
      if (props && hasOwn(props, key)) {
        warn(
          ("Method \"" + key + "\" has already been defined as a prop."),
          vm
        );
      }
      if ((key in vm) && isReserved(key)) {
        warn(
          "Method \"" + key + "\" conflicts with an existing Vue instance method. " +
          "Avoid defining component methods that start with _ or $."
        );
      }
    }
    vm[key] = methods[key] == null ? noop : bind(methods[key], vm);
  }
}

function initWatch (vm, watch) {
  for (var key in watch) {
    var handler = watch[key];
    if (Array.isArray(handler)) {
      for (var i = 0; i < handler.length; i++) {
        createWatcher(vm, key, handler[i]);
      }
    } else {
      createWatcher(vm, key, handler);
    }
  }
}

function createWatcher (
  vm,
  expOrFn,
  handler,
  options
) {
  if (isPlainObject(handler)) {
    options = handler;
    handler = handler.handler;
  }
  if (typeof handler === 'string') {
    handler = vm[handler];
  }
  return vm.$watch(expOrFn, handler, options)
}

function stateMixin (Vue) {
  // flow somehow has problems with directly declared definition object
  // when using Object.defineProperty, so we have to procedurally build up
  // the object here.
  var dataDef = {};
  dataDef.get = function () { return this._data };
  var propsDef = {};
  propsDef.get = function () { return this._props };
  if (false) {
    dataDef.set = function (newData) {
      warn(
        'Avoid replacing instance root $data. ' +
        'Use nested data properties instead.',
        this
      );
    };
    propsDef.set = function () {
      warn("$props is readonly.", this);
    };
  }
  Object.defineProperty(Vue.prototype, '$data', dataDef);
  Object.defineProperty(Vue.prototype, '$props', propsDef);

  Vue.prototype.$set = set;
  Vue.prototype.$delete = del;

  Vue.prototype.$watch = function (
    expOrFn,
    cb,
    options
  ) {
    var vm = this;
    if (isPlainObject(cb)) {
      return createWatcher(vm, expOrFn, cb, options)
    }
    options = options || {};
    options.user = true;
    var watcher = new Watcher(vm, expOrFn, cb, options);
    if (options.immediate) {
      cb.call(vm, watcher.value);
    }
    return function unwatchFn () {
      watcher.teardown();
    }
  };
}

/*  */

function initProvide (vm) {
  var provide = vm.$options.provide;
  if (provide) {
    vm._provided = typeof provide === 'function'
      ? provide.call(vm)
      : provide;
  }
}

function initInjections (vm) {
  var result = resolveInject(vm.$options.inject, vm);
  if (result) {
    toggleObserving(false);
    Object.keys(result).forEach(function (key) {
      /* istanbul ignore else */
      if (false) {
        defineReactive(vm, key, result[key], function () {
          warn(
            "Avoid mutating an injected value directly since the changes will be " +
            "overwritten whenever the provided component re-renders. " +
            "injection being mutated: \"" + key + "\"",
            vm
          );
        });
      } else {
        defineReactive(vm, key, result[key]);
      }
    });
    toggleObserving(true);
  }
}

function resolveInject (inject, vm) {
  if (inject) {
    // inject is :any because flow is not smart enough to figure out cached
    var result = Object.create(null);
    var keys = hasSymbol
      ? Reflect.ownKeys(inject).filter(function (key) {
        /* istanbul ignore next */
        return Object.getOwnPropertyDescriptor(inject, key).enumerable
      })
      : Object.keys(inject);

    for (var i = 0; i < keys.length; i++) {
      var key = keys[i];
      var provideKey = inject[key].from;
      var source = vm;
      while (source) {
        if (source._provided && hasOwn(source._provided, provideKey)) {
          result[key] = source._provided[provideKey];
          break
        }
        source = source.$parent;
      }
      if (!source) {
        if ('default' in inject[key]) {
          var provideDefault = inject[key].default;
          result[key] = typeof provideDefault === 'function'
            ? provideDefault.call(vm)
            : provideDefault;
        } else if (false) {
          warn(("Injection \"" + key + "\" not found"), vm);
        }
      }
    }
    return result
  }
}

/*  */

/**
 * Runtime helper for rendering v-for lists.
 */
function renderList (
  val,
  render
) {
  var ret, i, l, keys, key;
  if (Array.isArray(val) || typeof val === 'string') {
    ret = new Array(val.length);
    for (i = 0, l = val.length; i < l; i++) {
      ret[i] = render(val[i], i);
    }
  } else if (typeof val === 'number') {
    ret = new Array(val);
    for (i = 0; i < val; i++) {
      ret[i] = render(i + 1, i);
    }
  } else if (isObject(val)) {
    keys = Object.keys(val);
    ret = new Array(keys.length);
    for (i = 0, l = keys.length; i < l; i++) {
      key = keys[i];
      ret[i] = render(val[key], key, i);
    }
  }
  if (isDef(ret)) {
    (ret)._isVList = true;
  }
  return ret
}

/*  */

/**
 * Runtime helper for rendering <slot>
 */
function renderSlot (
  name,
  fallback,
  props,
  bindObject
) {
  var scopedSlotFn = this.$scopedSlots[name];
  var nodes;
  if (scopedSlotFn) { // scoped slot
    props = props || {};
    if (bindObject) {
      if (false) {
        warn(
          'slot v-bind without argument expects an Object',
          this
        );
      }
      props = extend(extend({}, bindObject), props);
    }
    nodes = scopedSlotFn(props) || fallback;
  } else {
    var slotNodes = this.$slots[name];
    // warn duplicate slot usage
    if (slotNodes) {
      if (false) {
        warn(
          "Duplicate presence of slot \"" + name + "\" found in the same render tree " +
          "- this will likely cause render errors.",
          this
        );
      }
      slotNodes._rendered = true;
    }
    nodes = slotNodes || fallback;
  }

  var target = props && props.slot;
  if (target) {
    return this.$createElement('template', { slot: target }, nodes)
  } else {
    return nodes
  }
}

/*  */

/**
 * Runtime helper for resolving filters
 */
function resolveFilter (id) {
  return resolveAsset(this.$options, 'filters', id, true) || identity
}

/*  */

function isKeyNotMatch (expect, actual) {
  if (Array.isArray(expect)) {
    return expect.indexOf(actual) === -1
  } else {
    return expect !== actual
  }
}

/**
 * Runtime helper for checking keyCodes from config.
 * exposed as Vue.prototype._k
 * passing in eventKeyName as last argument separately for backwards compat
 */
function checkKeyCodes (
  eventKeyCode,
  key,
  builtInKeyCode,
  eventKeyName,
  builtInKeyName
) {
  var mappedKeyCode = config.keyCodes[key] || builtInKeyCode;
  if (builtInKeyName && eventKeyName && !config.keyCodes[key]) {
    return isKeyNotMatch(builtInKeyName, eventKeyName)
  } else if (mappedKeyCode) {
    return isKeyNotMatch(mappedKeyCode, eventKeyCode)
  } else if (eventKeyName) {
    return hyphenate(eventKeyName) !== key
  }
}

/*  */

/**
 * Runtime helper for merging v-bind="object" into a VNode's data.
 */
function bindObjectProps (
  data,
  tag,
  value,
  asProp,
  isSync
) {
  if (value) {
    if (!isObject(value)) {
      "production" !== 'production' && warn(
        'v-bind without argument expects an Object or Array value',
        this
      );
    } else {
      if (Array.isArray(value)) {
        value = toObject(value);
      }
      var hash;
      var loop = function ( key ) {
        if (
          key === 'class' ||
          key === 'style' ||
          isReservedAttribute(key)
        ) {
          hash = data;
        } else {
          var type = data.attrs && data.attrs.type;
          hash = asProp || config.mustUseProp(tag, type, key)
            ? data.domProps || (data.domProps = {})
            : data.attrs || (data.attrs = {});
        }
        if (!(key in hash)) {
          hash[key] = value[key];

          if (isSync) {
            var on = data.on || (data.on = {});
            on[("update:" + key)] = function ($event) {
              value[key] = $event;
            };
          }
        }
      };

      for (var key in value) loop( key );
    }
  }
  return data
}

/*  */

/**
 * Runtime helper for rendering static trees.
 */
function renderStatic (
  index,
  isInFor
) {
  var cached = this._staticTrees || (this._staticTrees = []);
  var tree = cached[index];
  // if has already-rendered static tree and not inside v-for,
  // we can reuse the same tree.
  if (tree && !isInFor) {
    return tree
  }
  // otherwise, render a fresh tree.
  tree = cached[index] = this.$options.staticRenderFns[index].call(
    this._renderProxy,
    null,
    this // for render fns generated for functional component templates
  );
  markStatic(tree, ("__static__" + index), false);
  return tree
}

/**
 * Runtime helper for v-once.
 * Effectively it means marking the node as static with a unique key.
 */
function markOnce (
  tree,
  index,
  key
) {
  markStatic(tree, ("__once__" + index + (key ? ("_" + key) : "")), true);
  return tree
}

function markStatic (
  tree,
  key,
  isOnce
) {
  if (Array.isArray(tree)) {
    for (var i = 0; i < tree.length; i++) {
      if (tree[i] && typeof tree[i] !== 'string') {
        markStaticNode(tree[i], (key + "_" + i), isOnce);
      }
    }
  } else {
    markStaticNode(tree, key, isOnce);
  }
}

function markStaticNode (node, key, isOnce) {
  node.isStatic = true;
  node.key = key;
  node.isOnce = isOnce;
}

/*  */

function bindObjectListeners (data, value) {
  if (value) {
    if (!isPlainObject(value)) {
      "production" !== 'production' && warn(
        'v-on without argument expects an Object value',
        this
      );
    } else {
      var on = data.on = data.on ? extend({}, data.on) : {};
      for (var key in value) {
        var existing = on[key];
        var ours = value[key];
        on[key] = existing ? [].concat(existing, ours) : ours;
      }
    }
  }
  return data
}

/*  */

function installRenderHelpers (target) {
  target._o = markOnce;
  target._n = toNumber;
  target._s = toString;
  target._l = renderList;
  target._t = renderSlot;
  target._q = looseEqual;
  target._i = looseIndexOf;
  target._m = renderStatic;
  target._f = resolveFilter;
  target._k = checkKeyCodes;
  target._b = bindObjectProps;
  target._v = createTextVNode;
  target._e = createEmptyVNode;
  target._u = resolveScopedSlots;
  target._g = bindObjectListeners;
}

/*  */

function FunctionalRenderContext (
  data,
  props,
  children,
  parent,
  Ctor
) {
  var options = Ctor.options;
  // ensure the createElement function in functional components
  // gets a unique context - this is necessary for correct named slot check
  var contextVm;
  if (hasOwn(parent, '_uid')) {
    contextVm = Object.create(parent);
    // $flow-disable-line
    contextVm._original = parent;
  } else {
    // the context vm passed in is a functional context as well.
    // in this case we want to make sure we are able to get a hold to the
    // real context instance.
    contextVm = parent;
    // $flow-disable-line
    parent = parent._original;
  }
  var isCompiled = isTrue(options._compiled);
  var needNormalization = !isCompiled;

  this.data = data;
  this.props = props;
  this.children = children;
  this.parent = parent;
  this.listeners = data.on || emptyObject;
  this.injections = resolveInject(options.inject, parent);
  this.slots = function () { return resolveSlots(children, parent); };

  // support for compiled functional template
  if (isCompiled) {
    // exposing $options for renderStatic()
    this.$options = options;
    // pre-resolve slots for renderSlot()
    this.$slots = this.slots();
    this.$scopedSlots = data.scopedSlots || emptyObject;
  }

  if (options._scopeId) {
    this._c = function (a, b, c, d) {
      var vnode = createElement(contextVm, a, b, c, d, needNormalization);
      if (vnode && !Array.isArray(vnode)) {
        vnode.fnScopeId = options._scopeId;
        vnode.fnContext = parent;
      }
      return vnode
    };
  } else {
    this._c = function (a, b, c, d) { return createElement(contextVm, a, b, c, d, needNormalization); };
  }
}

installRenderHelpers(FunctionalRenderContext.prototype);

function createFunctionalComponent (
  Ctor,
  propsData,
  data,
  contextVm,
  children
) {
  var options = Ctor.options;
  var props = {};
  var propOptions = options.props;
  if (isDef(propOptions)) {
    for (var key in propOptions) {
      props[key] = validateProp(key, propOptions, propsData || emptyObject);
    }
  } else {
    if (isDef(data.attrs)) { mergeProps(props, data.attrs); }
    if (isDef(data.props)) { mergeProps(props, data.props); }
  }

  var renderContext = new FunctionalRenderContext(
    data,
    props,
    children,
    contextVm,
    Ctor
  );

  var vnode = options.render.call(null, renderContext._c, renderContext);

  if (vnode instanceof VNode) {
    return cloneAndMarkFunctionalResult(vnode, data, renderContext.parent, options)
  } else if (Array.isArray(vnode)) {
    var vnodes = normalizeChildren(vnode) || [];
    var res = new Array(vnodes.length);
    for (var i = 0; i < vnodes.length; i++) {
      res[i] = cloneAndMarkFunctionalResult(vnodes[i], data, renderContext.parent, options);
    }
    return res
  }
}

function cloneAndMarkFunctionalResult (vnode, data, contextVm, options) {
  // #7817 clone node before setting fnContext, otherwise if the node is reused
  // (e.g. it was from a cached normal slot) the fnContext causes named slots
  // that should not be matched to match.
  var clone = cloneVNode(vnode);
  clone.fnContext = contextVm;
  clone.fnOptions = options;
  if (data.slot) {
    (clone.data || (clone.data = {})).slot = data.slot;
  }
  return clone
}

function mergeProps (to, from) {
  for (var key in from) {
    to[camelize(key)] = from[key];
  }
}

/*  */




// Register the component hook to weex native render engine.
// The hook will be triggered by native, not javascript.


// Updates the state of the component to weex native render engine.

/*  */

// https://github.com/Hanks10100/weex-native-directive/tree/master/component

// listening on native callback

/*  */

/*  */

// inline hooks to be invoked on component VNodes during patch
var componentVNodeHooks = {
  init: function init (
    vnode,
    hydrating,
    parentElm,
    refElm
  ) {
    if (
      vnode.componentInstance &&
      !vnode.componentInstance._isDestroyed &&
      vnode.data.keepAlive
    ) {
      // kept-alive components, treat as a patch
      var mountedNode = vnode; // work around flow
      componentVNodeHooks.prepatch(mountedNode, mountedNode);
    } else {
      var child = vnode.componentInstance = createComponentInstanceForVnode(
        vnode,
        activeInstance,
        parentElm,
        refElm
      );
      child.$mount(hydrating ? vnode.elm : undefined, hydrating);
    }
  },

  prepatch: function prepatch (oldVnode, vnode) {
    var options = vnode.componentOptions;
    var child = vnode.componentInstance = oldVnode.componentInstance;
    updateChildComponent(
      child,
      options.propsData, // updated props
      options.listeners, // updated listeners
      vnode, // new parent vnode
      options.children // new children
    );
  },

  insert: function insert (vnode) {
    var context = vnode.context;
    var componentInstance = vnode.componentInstance;
    if (!componentInstance._isMounted) {
      componentInstance._isMounted = true;
      callHook(componentInstance, 'mounted');
    }
    if (vnode.data.keepAlive) {
      if (context._isMounted) {
        // vue-router#1212
        // During updates, a kept-alive component's child components may
        // change, so directly walking the tree here may call activated hooks
        // on incorrect children. Instead we push them into a queue which will
        // be processed after the whole patch process ended.
        queueActivatedComponent(componentInstance);
      } else {
        activateChildComponent(componentInstance, true /* direct */);
      }
    }
  },

  destroy: function destroy (vnode) {
    var componentInstance = vnode.componentInstance;
    if (!componentInstance._isDestroyed) {
      if (!vnode.data.keepAlive) {
        componentInstance.$destroy();
      } else {
        deactivateChildComponent(componentInstance, true /* direct */);
      }
    }
  }
};

var hooksToMerge = Object.keys(componentVNodeHooks);

function createComponent (
  Ctor,
  data,
  context,
  children,
  tag
) {
  if (isUndef(Ctor)) {
    return
  }

  var baseCtor = context.$options._base;

  // plain options object: turn it into a constructor
  if (isObject(Ctor)) {
    Ctor = baseCtor.extend(Ctor);
  }

  // if at this stage it's not a constructor or an async component factory,
  // reject.
  if (typeof Ctor !== 'function') {
    if (false) {
      warn(("Invalid Component definition: " + (String(Ctor))), context);
    }
    return
  }

  // async component
  var asyncFactory;
  if (isUndef(Ctor.cid)) {
    asyncFactory = Ctor;
    Ctor = resolveAsyncComponent(asyncFactory, baseCtor, context);
    if (Ctor === undefined) {
      // return a placeholder node for async component, which is rendered
      // as a comment node but preserves all the raw information for the node.
      // the information will be used for async server-rendering and hydration.
      return createAsyncPlaceholder(
        asyncFactory,
        data,
        context,
        children,
        tag
      )
    }
  }

  data = data || {};

  // resolve constructor options in case global mixins are applied after
  // component constructor creation
  resolveConstructorOptions(Ctor);

  // transform component v-model data into props & events
  if (isDef(data.model)) {
    transformModel(Ctor.options, data);
  }

  // extract props
  var propsData = extractPropsFromVNodeData(data, Ctor, tag);

  // functional component
  if (isTrue(Ctor.options.functional)) {
    return createFunctionalComponent(Ctor, propsData, data, context, children)
  }

  // extract listeners, since these needs to be treated as
  // child component listeners instead of DOM listeners
  var listeners = data.on;
  // replace with listeners with .native modifier
  // so it gets processed during parent component patch.
  data.on = data.nativeOn;

  if (isTrue(Ctor.options.abstract)) {
    // abstract components do not keep anything
    // other than props & listeners & slot

    // work around flow
    var slot = data.slot;
    data = {};
    if (slot) {
      data.slot = slot;
    }
  }

  // install component management hooks onto the placeholder node
  installComponentHooks(data);

  // return a placeholder vnode
  var name = Ctor.options.name || tag;
  var vnode = new VNode(
    ("vue-component-" + (Ctor.cid) + (name ? ("-" + name) : '')),
    data, undefined, undefined, undefined, context,
    { Ctor: Ctor, propsData: propsData, listeners: listeners, tag: tag, children: children },
    asyncFactory
  );

  // Weex specific: invoke recycle-list optimized @render function for
  // extracting cell-slot template.
  // https://github.com/Hanks10100/weex-native-directive/tree/master/component
  /* istanbul ignore if */
  return vnode
}

function createComponentInstanceForVnode (
  vnode, // we know it's MountedComponentVNode but flow doesn't
  parent, // activeInstance in lifecycle state
  parentElm,
  refElm
) {
  var options = {
    _isComponent: true,
    parent: parent,
    _parentVnode: vnode,
    _parentElm: parentElm || null,
    _refElm: refElm || null
  };
  // check inline-template render functions
  var inlineTemplate = vnode.data.inlineTemplate;
  if (isDef(inlineTemplate)) {
    options.render = inlineTemplate.render;
    options.staticRenderFns = inlineTemplate.staticRenderFns;
  }
  return new vnode.componentOptions.Ctor(options)
}

function installComponentHooks (data) {
  var hooks = data.hook || (data.hook = {});
  for (var i = 0; i < hooksToMerge.length; i++) {
    var key = hooksToMerge[i];
    hooks[key] = componentVNodeHooks[key];
  }
}

// transform component v-model info (value and callback) into
// prop and event handler respectively.
function transformModel (options, data) {
  var prop = (options.model && options.model.prop) || 'value';
  var event = (options.model && options.model.event) || 'input';(data.props || (data.props = {}))[prop] = data.model.value;
  var on = data.on || (data.on = {});
  if (isDef(on[event])) {
    on[event] = [data.model.callback].concat(on[event]);
  } else {
    on[event] = data.model.callback;
  }
}

/*  */

var SIMPLE_NORMALIZE = 1;
var ALWAYS_NORMALIZE = 2;

// wrapper function for providing a more flexible interface
// without getting yelled at by flow
function createElement (
  context,
  tag,
  data,
  children,
  normalizationType,
  alwaysNormalize
) {
  if (Array.isArray(data) || isPrimitive(data)) {
    normalizationType = children;
    children = data;
    data = undefined;
  }
  if (isTrue(alwaysNormalize)) {
    normalizationType = ALWAYS_NORMALIZE;
  }
  return _createElement(context, tag, data, children, normalizationType)
}

function _createElement (
  context,
  tag,
  data,
  children,
  normalizationType
) {
  if (isDef(data) && isDef((data).__ob__)) {
    "production" !== 'production' && warn(
      "Avoid using observed data object as vnode data: " + (JSON.stringify(data)) + "\n" +
      'Always create fresh vnode data objects in each render!',
      context
    );
    return createEmptyVNode()
  }
  // object syntax in v-bind
  if (isDef(data) && isDef(data.is)) {
    tag = data.is;
  }
  if (!tag) {
    // in case of component :is set to falsy value
    return createEmptyVNode()
  }
  // warn against non-primitive key
  if (false
  ) {
    {
      warn(
        'Avoid using non-primitive value as key, ' +
        'use string/number value instead.',
        context
      );
    }
  }
  // support single function children as default scoped slot
  if (Array.isArray(children) &&
    typeof children[0] === 'function'
  ) {
    data = data || {};
    data.scopedSlots = { default: children[0] };
    children.length = 0;
  }
  if (normalizationType === ALWAYS_NORMALIZE) {
    children = normalizeChildren(children);
  } else if (normalizationType === SIMPLE_NORMALIZE) {
    children = simpleNormalizeChildren(children);
  }
  var vnode, ns;
  if (typeof tag === 'string') {
    var Ctor;
    ns = (context.$vnode && context.$vnode.ns) || config.getTagNamespace(tag);
    if (config.isReservedTag(tag)) {
      // platform built-in elements
      vnode = new VNode(
        config.parsePlatformTagName(tag), data, children,
        undefined, undefined, context
      );
    } else if (isDef(Ctor = resolveAsset(context.$options, 'components', tag))) {
      // component
      vnode = createComponent(Ctor, data, context, children, tag);
    } else {
      // unknown or unlisted namespaced elements
      // check at runtime because it may get assigned a namespace when its
      // parent normalizes children
      vnode = new VNode(
        tag, data, children,
        undefined, undefined, context
      );
    }
  } else {
    // direct component options / constructor
    vnode = createComponent(tag, data, context, children);
  }
  if (Array.isArray(vnode)) {
    return vnode
  } else if (isDef(vnode)) {
    if (isDef(ns)) { applyNS(vnode, ns); }
    if (isDef(data)) { registerDeepBindings(data); }
    return vnode
  } else {
    return createEmptyVNode()
  }
}

function applyNS (vnode, ns, force) {
  vnode.ns = ns;
  if (vnode.tag === 'foreignObject') {
    // use default namespace inside foreignObject
    ns = undefined;
    force = true;
  }
  if (isDef(vnode.children)) {
    for (var i = 0, l = vnode.children.length; i < l; i++) {
      var child = vnode.children[i];
      if (isDef(child.tag) && (
        isUndef(child.ns) || (isTrue(force) && child.tag !== 'svg'))) {
        applyNS(child, ns, force);
      }
    }
  }
}

// ref #5318
// necessary to ensure parent re-render when deep bindings like :style and
// :class are used on slot nodes
function registerDeepBindings (data) {
  if (isObject(data.style)) {
    traverse(data.style);
  }
  if (isObject(data.class)) {
    traverse(data.class);
  }
}

/*  */

function initRender (vm) {
  vm._vnode = null; // the root of the child tree
  vm._staticTrees = null; // v-once cached trees
  var options = vm.$options;
  var parentVnode = vm.$vnode = options._parentVnode; // the placeholder node in parent tree
  var renderContext = parentVnode && parentVnode.context;
  vm.$slots = resolveSlots(options._renderChildren, renderContext);
  vm.$scopedSlots = emptyObject;
  // bind the createElement fn to this instance
  // so that we get proper render context inside it.
  // args order: tag, data, children, normalizationType, alwaysNormalize
  // internal version is used by render functions compiled from templates
  vm._c = function (a, b, c, d) { return createElement(vm, a, b, c, d, false); };
  // normalization is always applied for the public version, used in
  // user-written render functions.
  vm.$createElement = function (a, b, c, d) { return createElement(vm, a, b, c, d, true); };

  // $attrs & $listeners are exposed for easier HOC creation.
  // they need to be reactive so that HOCs using them are always updated
  var parentData = parentVnode && parentVnode.data;

  /* istanbul ignore else */
  if (false) {
    defineReactive(vm, '$attrs', parentData && parentData.attrs || emptyObject, function () {
      !isUpdatingChildComponent && warn("$attrs is readonly.", vm);
    }, true);
    defineReactive(vm, '$listeners', options._parentListeners || emptyObject, function () {
      !isUpdatingChildComponent && warn("$listeners is readonly.", vm);
    }, true);
  } else {
    defineReactive(vm, '$attrs', parentData && parentData.attrs || emptyObject, null, true);
    defineReactive(vm, '$listeners', options._parentListeners || emptyObject, null, true);
  }
}

function renderMixin (Vue) {
  // install runtime convenience helpers
  installRenderHelpers(Vue.prototype);

  Vue.prototype.$nextTick = function (fn) {
    return nextTick(fn, this)
  };

  Vue.prototype._render = function () {
    var vm = this;
    var ref = vm.$options;
    var render = ref.render;
    var _parentVnode = ref._parentVnode;

    // reset _rendered flag on slots for duplicate slot check
    if (false) {
      for (var key in vm.$slots) {
        // $flow-disable-line
        vm.$slots[key]._rendered = false;
      }
    }

    if (_parentVnode) {
      vm.$scopedSlots = _parentVnode.data.scopedSlots || emptyObject;
    }

    // set parent vnode. this allows render functions to have access
    // to the data on the placeholder node.
    vm.$vnode = _parentVnode;
    // render self
    var vnode;
    try {
      vnode = render.call(vm._renderProxy, vm.$createElement);
    } catch (e) {
      handleError(e, vm, "render");
      // return error render result,
      // or previous vnode to prevent render error causing blank component
      /* istanbul ignore else */
      if (false) {
        if (vm.$options.renderError) {
          try {
            vnode = vm.$options.renderError.call(vm._renderProxy, vm.$createElement, e);
          } catch (e) {
            handleError(e, vm, "renderError");
            vnode = vm._vnode;
          }
        } else {
          vnode = vm._vnode;
        }
      } else {
        vnode = vm._vnode;
      }
    }
    // return empty vnode in case the render function errored out
    if (!(vnode instanceof VNode)) {
      if (false) {
        warn(
          'Multiple root nodes returned from render function. Render function ' +
          'should return a single root node.',
          vm
        );
      }
      vnode = createEmptyVNode();
    }
    // set parent
    vnode.parent = _parentVnode;
    return vnode
  };
}

/*  */

var uid$3 = 0;

function initMixin (Vue) {
  Vue.prototype._init = function (options) {
    var vm = this;
    // a uid
    vm._uid = uid$3++;

    var startTag, endTag;
    /* istanbul ignore if */
    if (false) {
      startTag = "vue-perf-start:" + (vm._uid);
      endTag = "vue-perf-end:" + (vm._uid);
      mark(startTag);
    }

    // a flag to avoid this being observed
    vm._isVue = true;
    // merge options
    if (options && options._isComponent) {
      // optimize internal component instantiation
      // since dynamic options merging is pretty slow, and none of the
      // internal component options needs special treatment.
      initInternalComponent(vm, options);
    } else {
      vm.$options = mergeOptions(
        resolveConstructorOptions(vm.constructor),
        options || {},
        vm
      );
    }
    /* istanbul ignore else */
    if (false) {
      initProxy(vm);
    } else {
      vm._renderProxy = vm;
    }
    // expose real self
    vm._self = vm;
    initLifecycle(vm);
    initEvents(vm);
    initRender(vm);
    callHook(vm, 'beforeCreate');
    initInjections(vm); // resolve injections before data/props
    initState(vm);
    initProvide(vm); // resolve provide after data/props
    callHook(vm, 'created');

    /* istanbul ignore if */
    if (false) {
      vm._name = formatComponentName(vm, false);
      mark(endTag);
      measure(("vue " + (vm._name) + " init"), startTag, endTag);
    }

    if (vm.$options.el) {
      vm.$mount(vm.$options.el);
    }
  };
}

function initInternalComponent (vm, options) {
  var opts = vm.$options = Object.create(vm.constructor.options);
  // doing this because it's faster than dynamic enumeration.
  var parentVnode = options._parentVnode;
  opts.parent = options.parent;
  opts._parentVnode = parentVnode;
  opts._parentElm = options._parentElm;
  opts._refElm = options._refElm;

  var vnodeComponentOptions = parentVnode.componentOptions;
  opts.propsData = vnodeComponentOptions.propsData;
  opts._parentListeners = vnodeComponentOptions.listeners;
  opts._renderChildren = vnodeComponentOptions.children;
  opts._componentTag = vnodeComponentOptions.tag;

  if (options.render) {
    opts.render = options.render;
    opts.staticRenderFns = options.staticRenderFns;
  }
}

function resolveConstructorOptions (Ctor) {
  var options = Ctor.options;
  if (Ctor.super) {
    var superOptions = resolveConstructorOptions(Ctor.super);
    var cachedSuperOptions = Ctor.superOptions;
    if (superOptions !== cachedSuperOptions) {
      // super option changed,
      // need to resolve new options.
      Ctor.superOptions = superOptions;
      // check if there are any late-modified/attached options (#4976)
      var modifiedOptions = resolveModifiedOptions(Ctor);
      // update base extend options
      if (modifiedOptions) {
        extend(Ctor.extendOptions, modifiedOptions);
      }
      options = Ctor.options = mergeOptions(superOptions, Ctor.extendOptions);
      if (options.name) {
        options.components[options.name] = Ctor;
      }
    }
  }
  return options
}

function resolveModifiedOptions (Ctor) {
  var modified;
  var latest = Ctor.options;
  var extended = Ctor.extendOptions;
  var sealed = Ctor.sealedOptions;
  for (var key in latest) {
    if (latest[key] !== sealed[key]) {
      if (!modified) { modified = {}; }
      modified[key] = dedupe(latest[key], extended[key], sealed[key]);
    }
  }
  return modified
}

function dedupe (latest, extended, sealed) {
  // compare latest and sealed to ensure lifecycle hooks won't be duplicated
  // between merges
  if (Array.isArray(latest)) {
    var res = [];
    sealed = Array.isArray(sealed) ? sealed : [sealed];
    extended = Array.isArray(extended) ? extended : [extended];
    for (var i = 0; i < latest.length; i++) {
      // push original options and not sealed options to exclude duplicated options
      if (extended.indexOf(latest[i]) >= 0 || sealed.indexOf(latest[i]) < 0) {
        res.push(latest[i]);
      }
    }
    return res
  } else {
    return latest
  }
}

function Vue (options) {
  if (false
  ) {
    warn('Vue is a constructor and should be called with the `new` keyword');
  }
  this._init(options);
}

initMixin(Vue);
stateMixin(Vue);
eventsMixin(Vue);
lifecycleMixin(Vue);
renderMixin(Vue);

/*  */

function initUse (Vue) {
  Vue.use = function (plugin) {
    var installedPlugins = (this._installedPlugins || (this._installedPlugins = []));
    if (installedPlugins.indexOf(plugin) > -1) {
      return this
    }

    // additional parameters
    var args = toArray(arguments, 1);
    args.unshift(this);
    if (typeof plugin.install === 'function') {
      plugin.install.apply(plugin, args);
    } else if (typeof plugin === 'function') {
      plugin.apply(null, args);
    }
    installedPlugins.push(plugin);
    return this
  };
}

/*  */

function initMixin$1 (Vue) {
  Vue.mixin = function (mixin) {
    this.options = mergeOptions(this.options, mixin);
    return this
  };
}

/*  */

function initExtend (Vue) {
  /**
   * Each instance constructor, including Vue, has a unique
   * cid. This enables us to create wrapped "child
   * constructors" for prototypal inheritance and cache them.
   */
  Vue.cid = 0;
  var cid = 1;

  /**
   * Class inheritance
   */
  Vue.extend = function (extendOptions) {
    extendOptions = extendOptions || {};
    var Super = this;
    var SuperId = Super.cid;
    var cachedCtors = extendOptions._Ctor || (extendOptions._Ctor = {});
    if (cachedCtors[SuperId]) {
      return cachedCtors[SuperId]
    }

    var name = extendOptions.name || Super.options.name;
    if (false) {
      validateComponentName(name);
    }

    var Sub = function VueComponent (options) {
      this._init(options);
    };
    Sub.prototype = Object.create(Super.prototype);
    Sub.prototype.constructor = Sub;
    Sub.cid = cid++;
    Sub.options = mergeOptions(
      Super.options,
      extendOptions
    );
    Sub['super'] = Super;

    // For props and computed properties, we define the proxy getters on
    // the Vue instances at extension time, on the extended prototype. This
    // avoids Object.defineProperty calls for each instance created.
    if (Sub.options.props) {
      initProps$1(Sub);
    }
    if (Sub.options.computed) {
      initComputed$1(Sub);
    }

    // allow further extension/mixin/plugin usage
    Sub.extend = Super.extend;
    Sub.mixin = Super.mixin;
    Sub.use = Super.use;

    // create asset registers, so extended classes
    // can have their private assets too.
    ASSET_TYPES.forEach(function (type) {
      Sub[type] = Super[type];
    });
    // enable recursive self-lookup
    if (name) {
      Sub.options.components[name] = Sub;
    }

    // keep a reference to the super options at extension time.
    // later at instantiation we can check if Super's options have
    // been updated.
    Sub.superOptions = Super.options;
    Sub.extendOptions = extendOptions;
    Sub.sealedOptions = extend({}, Sub.options);

    // cache constructor
    cachedCtors[SuperId] = Sub;
    return Sub
  };
}

function initProps$1 (Comp) {
  var props = Comp.options.props;
  for (var key in props) {
    proxy(Comp.prototype, "_props", key);
  }
}

function initComputed$1 (Comp) {
  var computed = Comp.options.computed;
  for (var key in computed) {
    defineComputed(Comp.prototype, key, computed[key]);
  }
}

/*  */

function initAssetRegisters (Vue) {
  /**
   * Create asset registration methods.
   */
  ASSET_TYPES.forEach(function (type) {
    Vue[type] = function (
      id,
      definition
    ) {
      if (!definition) {
        return this.options[type + 's'][id]
      } else {
        /* istanbul ignore if */
        if (false) {
          validateComponentName(id);
        }
        if (type === 'component' && isPlainObject(definition)) {
          definition.name = definition.name || id;
          definition = this.options._base.extend(definition);
        }
        if (type === 'directive' && typeof definition === 'function') {
          definition = { bind: definition, update: definition };
        }
        this.options[type + 's'][id] = definition;
        return definition
      }
    };
  });
}

/*  */

function getComponentName (opts) {
  return opts && (opts.Ctor.options.name || opts.tag)
}

function matches (pattern, name) {
  if (Array.isArray(pattern)) {
    return pattern.indexOf(name) > -1
  } else if (typeof pattern === 'string') {
    return pattern.split(',').indexOf(name) > -1
  } else if (isRegExp(pattern)) {
    return pattern.test(name)
  }
  /* istanbul ignore next */
  return false
}

function pruneCache (keepAliveInstance, filter) {
  var cache = keepAliveInstance.cache;
  var keys = keepAliveInstance.keys;
  var _vnode = keepAliveInstance._vnode;
  for (var key in cache) {
    var cachedNode = cache[key];
    if (cachedNode) {
      var name = getComponentName(cachedNode.componentOptions);
      if (name && !filter(name)) {
        pruneCacheEntry(cache, key, keys, _vnode);
      }
    }
  }
}

function pruneCacheEntry (
  cache,
  key,
  keys,
  current
) {
  var cached$$1 = cache[key];
  if (cached$$1 && (!current || cached$$1.tag !== current.tag)) {
    cached$$1.componentInstance.$destroy();
  }
  cache[key] = null;
  remove(keys, key);
}

var patternTypes = [String, RegExp, Array];

var KeepAlive = {
  name: 'keep-alive',
  abstract: true,

  props: {
    include: patternTypes,
    exclude: patternTypes,
    max: [String, Number]
  },

  created: function created () {
    this.cache = Object.create(null);
    this.keys = [];
  },

  destroyed: function destroyed () {
    var this$1 = this;

    for (var key in this$1.cache) {
      pruneCacheEntry(this$1.cache, key, this$1.keys);
    }
  },

  mounted: function mounted () {
    var this$1 = this;

    this.$watch('include', function (val) {
      pruneCache(this$1, function (name) { return matches(val, name); });
    });
    this.$watch('exclude', function (val) {
      pruneCache(this$1, function (name) { return !matches(val, name); });
    });
  },

  render: function render () {
    var slot = this.$slots.default;
    var vnode = getFirstComponentChild(slot);
    var componentOptions = vnode && vnode.componentOptions;
    if (componentOptions) {
      // check pattern
      var name = getComponentName(componentOptions);
      var ref = this;
      var include = ref.include;
      var exclude = ref.exclude;
      if (
        // not included
        (include && (!name || !matches(include, name))) ||
        // excluded
        (exclude && name && matches(exclude, name))
      ) {
        return vnode
      }

      var ref$1 = this;
      var cache = ref$1.cache;
      var keys = ref$1.keys;
      var key = vnode.key == null
        // same constructor may get registered as different local components
        // so cid alone is not enough (#3269)
        ? componentOptions.Ctor.cid + (componentOptions.tag ? ("::" + (componentOptions.tag)) : '')
        : vnode.key;
      if (cache[key]) {
        vnode.componentInstance = cache[key].componentInstance;
        // make current key freshest
        remove(keys, key);
        keys.push(key);
      } else {
        cache[key] = vnode;
        keys.push(key);
        // prune oldest entry
        if (this.max && keys.length > parseInt(this.max)) {
          pruneCacheEntry(cache, keys[0], keys, this._vnode);
        }
      }

      vnode.data.keepAlive = true;
    }
    return vnode || (slot && slot[0])
  }
}

var builtInComponents = {
  KeepAlive: KeepAlive
}

/*  */

function initGlobalAPI (Vue) {
  // config
  var configDef = {};
  configDef.get = function () { return config; };
  if (false) {
    configDef.set = function () {
      warn(
        'Do not replace the Vue.config object, set individual fields instead.'
      );
    };
  }
  Object.defineProperty(Vue, 'config', configDef);

  // exposed util methods.
  // NOTE: these are not considered part of the public API - avoid relying on
  // them unless you are aware of the risk.
  Vue.util = {
    warn: warn,
    extend: extend,
    mergeOptions: mergeOptions,
    defineReactive: defineReactive
  };

  Vue.set = set;
  Vue.delete = del;
  Vue.nextTick = nextTick;

  Vue.options = Object.create(null);
  ASSET_TYPES.forEach(function (type) {
    Vue.options[type + 's'] = Object.create(null);
  });

  // this is used to identify the "base" constructor to extend all plain-object
  // components with in Weex's multi-instance scenarios.
  Vue.options._base = Vue;

  extend(Vue.options.components, builtInComponents);

  initUse(Vue);
  initMixin$1(Vue);
  initExtend(Vue);
  initAssetRegisters(Vue);
}

initGlobalAPI(Vue);

Object.defineProperty(Vue.prototype, '$isServer', {
  get: isServerRendering
});

Object.defineProperty(Vue.prototype, '$ssrContext', {
  get: function get () {
    /* istanbul ignore next */
    return this.$vnode && this.$vnode.ssrContext
  }
});

// expose FunctionalRenderContext for ssr runtime helper installation
Object.defineProperty(Vue, 'FunctionalRenderContext', {
  value: FunctionalRenderContext
});

Vue.version = '2.5.17';

/*  */

// these are reserved for web because they are directly compiled away
// during template compilation
var isReservedAttr = makeMap('style,class');

// attributes that should be using props for binding
var acceptValue = makeMap('input,textarea,option,select,progress');
var mustUseProp = function (tag, type, attr) {
  return (
    (attr === 'value' && acceptValue(tag)) && type !== 'button' ||
    (attr === 'selected' && tag === 'option') ||
    (attr === 'checked' && tag === 'input') ||
    (attr === 'muted' && tag === 'video')
  )
};

var isEnumeratedAttr = makeMap('contenteditable,draggable,spellcheck');

var isBooleanAttr = makeMap(
  'allowfullscreen,async,autofocus,autoplay,checked,compact,controls,declare,' +
  'default,defaultchecked,defaultmuted,defaultselected,defer,disabled,' +
  'enabled,formnovalidate,hidden,indeterminate,inert,ismap,itemscope,loop,multiple,' +
  'muted,nohref,noresize,noshade,novalidate,nowrap,open,pauseonexit,readonly,' +
  'required,reversed,scoped,seamless,selected,sortable,translate,' +
  'truespeed,typemustmatch,visible'
);

var xlinkNS = 'http://www.w3.org/1999/xlink';

var isXlink = function (name) {
  return name.charAt(5) === ':' && name.slice(0, 5) === 'xlink'
};

var getXlinkProp = function (name) {
  return isXlink(name) ? name.slice(6, name.length) : ''
};

var isFalsyAttrValue = function (val) {
  return val == null || val === false
};

/*  */

function genClassForVnode (vnode) {
  var data = vnode.data;
  var parentNode = vnode;
  var childNode = vnode;
  while (isDef(childNode.componentInstance)) {
    childNode = childNode.componentInstance._vnode;
    if (childNode && childNode.data) {
      data = mergeClassData(childNode.data, data);
    }
  }
  while (isDef(parentNode = parentNode.parent)) {
    if (parentNode && parentNode.data) {
      data = mergeClassData(data, parentNode.data);
    }
  }
  return renderClass(data.staticClass, data.class)
}

function mergeClassData (child, parent) {
  return {
    staticClass: concat(child.staticClass, parent.staticClass),
    class: isDef(child.class)
      ? [child.class, parent.class]
      : parent.class
  }
}

function renderClass (
  staticClass,
  dynamicClass
) {
  if (isDef(staticClass) || isDef(dynamicClass)) {
    return concat(staticClass, stringifyClass(dynamicClass))
  }
  /* istanbul ignore next */
  return ''
}

function concat (a, b) {
  return a ? b ? (a + ' ' + b) : a : (b || '')
}

function stringifyClass (value) {
  if (Array.isArray(value)) {
    return stringifyArray(value)
  }
  if (isObject(value)) {
    return stringifyObject(value)
  }
  if (typeof value === 'string') {
    return value
  }
  /* istanbul ignore next */
  return ''
}

function stringifyArray (value) {
  var res = '';
  var stringified;
  for (var i = 0, l = value.length; i < l; i++) {
    if (isDef(stringified = stringifyClass(value[i])) && stringified !== '') {
      if (res) { res += ' '; }
      res += stringified;
    }
  }
  return res
}

function stringifyObject (value) {
  var res = '';
  for (var key in value) {
    if (value[key]) {
      if (res) { res += ' '; }
      res += key;
    }
  }
  return res
}

/*  */

var namespaceMap = {
  svg: 'http://www.w3.org/2000/svg',
  math: 'http://www.w3.org/1998/Math/MathML'
};

var isHTMLTag = makeMap(
  'html,body,base,head,link,meta,style,title,' +
  'address,article,aside,footer,header,h1,h2,h3,h4,h5,h6,hgroup,nav,section,' +
  'div,dd,dl,dt,figcaption,figure,picture,hr,img,li,main,ol,p,pre,ul,' +
  'a,b,abbr,bdi,bdo,br,cite,code,data,dfn,em,i,kbd,mark,q,rp,rt,rtc,ruby,' +
  's,samp,small,span,strong,sub,sup,time,u,var,wbr,area,audio,map,track,video,' +
  'embed,object,param,source,canvas,script,noscript,del,ins,' +
  'caption,col,colgroup,table,thead,tbody,td,th,tr,' +
  'button,datalist,fieldset,form,input,label,legend,meter,optgroup,option,' +
  'output,progress,select,textarea,' +
  'details,dialog,menu,menuitem,summary,' +
  'content,element,shadow,template,blockquote,iframe,tfoot'
);

// this map is intentionally selective, only covering SVG elements that may
// contain child elements.
var isSVG = makeMap(
  'svg,animate,circle,clippath,cursor,defs,desc,ellipse,filter,font-face,' +
  'foreignObject,g,glyph,image,line,marker,mask,missing-glyph,path,pattern,' +
  'polygon,polyline,rect,switch,symbol,text,textpath,tspan,use,view',
  true
);

var isPreTag = function (tag) { return tag === 'pre'; };

var isReservedTag = function (tag) {
  return isHTMLTag(tag) || isSVG(tag)
};

function getTagNamespace (tag) {
  if (isSVG(tag)) {
    return 'svg'
  }
  // basic support for MathML
  // note it doesn't support other MathML elements being component roots
  if (tag === 'math') {
    return 'math'
  }
}

var unknownElementCache = Object.create(null);
function isUnknownElement (tag) {
  /* istanbul ignore if */
  if (!inBrowser) {
    return true
  }
  if (isReservedTag(tag)) {
    return false
  }
  tag = tag.toLowerCase();
  /* istanbul ignore if */
  if (unknownElementCache[tag] != null) {
    return unknownElementCache[tag]
  }
  var el = document.createElement(tag);
  if (tag.indexOf('-') > -1) {
    // http://stackoverflow.com/a/28210364/1070244
    return (unknownElementCache[tag] = (
      el.constructor === window.HTMLUnknownElement ||
      el.constructor === window.HTMLElement
    ))
  } else {
    return (unknownElementCache[tag] = /HTMLUnknownElement/.test(el.toString()))
  }
}

var isTextInputType = makeMap('text,number,password,search,email,tel,url');

/*  */

/**
 * Query an element selector if it's not an element already.
 */
function query (el) {
  if (typeof el === 'string') {
    var selected = document.querySelector(el);
    if (!selected) {
      "production" !== 'production' && warn(
        'Cannot find element: ' + el
      );
      return document.createElement('div')
    }
    return selected
  } else {
    return el
  }
}

/*  */

function createElement$1 (tagName, vnode) {
  var elm = document.createElement(tagName);
  if (tagName !== 'select') {
    return elm
  }
  // false or null will remove the attribute but undefined will not
  if (vnode.data && vnode.data.attrs && vnode.data.attrs.multiple !== undefined) {
    elm.setAttribute('multiple', 'multiple');
  }
  return elm
}

function createElementNS (namespace, tagName) {
  return document.createElementNS(namespaceMap[namespace], tagName)
}

function createTextNode (text) {
  return document.createTextNode(text)
}

function createComment (text) {
  return document.createComment(text)
}

function insertBefore (parentNode, newNode, referenceNode) {
  parentNode.insertBefore(newNode, referenceNode);
}

function removeChild (node, child) {
  node.removeChild(child);
}

function appendChild (node, child) {
  node.appendChild(child);
}

function parentNode (node) {
  return node.parentNode
}

function nextSibling (node) {
  return node.nextSibling
}

function tagName (node) {
  return node.tagName
}

function setTextContent (node, text) {
  node.textContent = text;
}

function setStyleScope (node, scopeId) {
  node.setAttribute(scopeId, '');
}


var nodeOps = Object.freeze({
	createElement: createElement$1,
	createElementNS: createElementNS,
	createTextNode: createTextNode,
	createComment: createComment,
	insertBefore: insertBefore,
	removeChild: removeChild,
	appendChild: appendChild,
	parentNode: parentNode,
	nextSibling: nextSibling,
	tagName: tagName,
	setTextContent: setTextContent,
	setStyleScope: setStyleScope
});

/*  */

var ref = {
  create: function create (_, vnode) {
    registerRef(vnode);
  },
  update: function update (oldVnode, vnode) {
    if (oldVnode.data.ref !== vnode.data.ref) {
      registerRef(oldVnode, true);
      registerRef(vnode);
    }
  },
  destroy: function destroy (vnode) {
    registerRef(vnode, true);
  }
}

function registerRef (vnode, isRemoval) {
  var key = vnode.data.ref;
  if (!isDef(key)) { return }

  var vm = vnode.context;
  var ref = vnode.componentInstance || vnode.elm;
  var refs = vm.$refs;
  if (isRemoval) {
    if (Array.isArray(refs[key])) {
      remove(refs[key], ref);
    } else if (refs[key] === ref) {
      refs[key] = undefined;
    }
  } else {
    if (vnode.data.refInFor) {
      if (!Array.isArray(refs[key])) {
        refs[key] = [ref];
      } else if (refs[key].indexOf(ref) < 0) {
        // $flow-disable-line
        refs[key].push(ref);
      }
    } else {
      refs[key] = ref;
    }
  }
}

/**
 * Virtual DOM patching algorithm based on Snabbdom by
 * Simon Friis Vindum (@paldepind)
 * Licensed under the MIT License
 * https://github.com/paldepind/snabbdom/blob/master/LICENSE
 *
 * modified by Evan You (@yyx990803)
 *
 * Not type-checking this because this file is perf-critical and the cost
 * of making flow understand it is not worth it.
 */

var emptyNode = new VNode('', {}, []);

var hooks = ['create', 'activate', 'update', 'remove', 'destroy'];

function sameVnode (a, b) {
  return (
    a.key === b.key && (
      (
        a.tag === b.tag &&
        a.isComment === b.isComment &&
        isDef(a.data) === isDef(b.data) &&
        sameInputType(a, b)
      ) || (
        isTrue(a.isAsyncPlaceholder) &&
        a.asyncFactory === b.asyncFactory &&
        isUndef(b.asyncFactory.error)
      )
    )
  )
}

function sameInputType (a, b) {
  if (a.tag !== 'input') { return true }
  var i;
  var typeA = isDef(i = a.data) && isDef(i = i.attrs) && i.type;
  var typeB = isDef(i = b.data) && isDef(i = i.attrs) && i.type;
  return typeA === typeB || isTextInputType(typeA) && isTextInputType(typeB)
}

function createKeyToOldIdx (children, beginIdx, endIdx) {
  var i, key;
  var map = {};
  for (i = beginIdx; i <= endIdx; ++i) {
    key = children[i].key;
    if (isDef(key)) { map[key] = i; }
  }
  return map
}

function createPatchFunction (backend) {
  var i, j;
  var cbs = {};

  var modules = backend.modules;
  var nodeOps = backend.nodeOps;

  for (i = 0; i < hooks.length; ++i) {
    cbs[hooks[i]] = [];
    for (j = 0; j < modules.length; ++j) {
      if (isDef(modules[j][hooks[i]])) {
        cbs[hooks[i]].push(modules[j][hooks[i]]);
      }
    }
  }

  function emptyNodeAt (elm) {
    return new VNode(nodeOps.tagName(elm).toLowerCase(), {}, [], undefined, elm)
  }

  function createRmCb (childElm, listeners) {
    function remove () {
      if (--remove.listeners === 0) {
        removeNode(childElm);
      }
    }
    remove.listeners = listeners;
    return remove
  }

  function removeNode (el) {
    var parent = nodeOps.parentNode(el);
    // element may have already been removed due to v-html / v-text
    if (isDef(parent)) {
      nodeOps.removeChild(parent, el);
    }
  }

  function isUnknownElement$$1 (vnode, inVPre) {
    return (
      !inVPre &&
      !vnode.ns &&
      !(
        config.ignoredElements.length &&
        config.ignoredElements.some(function (ignore) {
          return isRegExp(ignore)
            ? ignore.test(vnode.tag)
            : ignore === vnode.tag
        })
      ) &&
      config.isUnknownElement(vnode.tag)
    )
  }

  var creatingElmInVPre = 0;

  function createElm (
    vnode,
    insertedVnodeQueue,
    parentElm,
    refElm,
    nested,
    ownerArray,
    index
  ) {
    if (isDef(vnode.elm) && isDef(ownerArray)) {
      // This vnode was used in a previous render!
      // now it's used as a new node, overwriting its elm would cause
      // potential patch errors down the road when it's used as an insertion
      // reference node. Instead, we clone the node on-demand before creating
      // associated DOM element for it.
      vnode = ownerArray[index] = cloneVNode(vnode);
    }

    vnode.isRootInsert = !nested; // for transition enter check
    if (createComponent(vnode, insertedVnodeQueue, parentElm, refElm)) {
      return
    }

    var data = vnode.data;
    var children = vnode.children;
    var tag = vnode.tag;
    if (isDef(tag)) {
      if (false) {
        if (data && data.pre) {
          creatingElmInVPre++;
        }
        if (isUnknownElement$$1(vnode, creatingElmInVPre)) {
          warn(
            'Unknown custom element: <' + tag + '> - did you ' +
            'register the component correctly? For recursive components, ' +
            'make sure to provide the "name" option.',
            vnode.context
          );
        }
      }

      vnode.elm = vnode.ns
        ? nodeOps.createElementNS(vnode.ns, tag)
        : nodeOps.createElement(tag, vnode);
      setScope(vnode);

      /* istanbul ignore if */
      {
        createChildren(vnode, children, insertedVnodeQueue);
        if (isDef(data)) {
          invokeCreateHooks(vnode, insertedVnodeQueue);
        }
        insert(parentElm, vnode.elm, refElm);
      }

      if (false) {
        creatingElmInVPre--;
      }
    } else if (isTrue(vnode.isComment)) {
      vnode.elm = nodeOps.createComment(vnode.text);
      insert(parentElm, vnode.elm, refElm);
    } else {
      vnode.elm = nodeOps.createTextNode(vnode.text);
      insert(parentElm, vnode.elm, refElm);
    }
  }

  function createComponent (vnode, insertedVnodeQueue, parentElm, refElm) {
    var i = vnode.data;
    if (isDef(i)) {
      var isReactivated = isDef(vnode.componentInstance) && i.keepAlive;
      if (isDef(i = i.hook) && isDef(i = i.init)) {
        i(vnode, false /* hydrating */, parentElm, refElm);
      }
      // after calling the init hook, if the vnode is a child component
      // it should've created a child instance and mounted it. the child
      // component also has set the placeholder vnode's elm.
      // in that case we can just return the element and be done.
      if (isDef(vnode.componentInstance)) {
        initComponent(vnode, insertedVnodeQueue);
        if (isTrue(isReactivated)) {
          reactivateComponent(vnode, insertedVnodeQueue, parentElm, refElm);
        }
        return true
      }
    }
  }

  function initComponent (vnode, insertedVnodeQueue) {
    if (isDef(vnode.data.pendingInsert)) {
      insertedVnodeQueue.push.apply(insertedVnodeQueue, vnode.data.pendingInsert);
      vnode.data.pendingInsert = null;
    }
    vnode.elm = vnode.componentInstance.$el;
    if (isPatchable(vnode)) {
      invokeCreateHooks(vnode, insertedVnodeQueue);
      setScope(vnode);
    } else {
      // empty component root.
      // skip all element-related modules except for ref (#3455)
      registerRef(vnode);
      // make sure to invoke the insert hook
      insertedVnodeQueue.push(vnode);
    }
  }

  function reactivateComponent (vnode, insertedVnodeQueue, parentElm, refElm) {
    var i;
    // hack for #4339: a reactivated component with inner transition
    // does not trigger because the inner node's created hooks are not called
    // again. It's not ideal to involve module-specific logic in here but
    // there doesn't seem to be a better way to do it.
    var innerNode = vnode;
    while (innerNode.componentInstance) {
      innerNode = innerNode.componentInstance._vnode;
      if (isDef(i = innerNode.data) && isDef(i = i.transition)) {
        for (i = 0; i < cbs.activate.length; ++i) {
          cbs.activate[i](emptyNode, innerNode);
        }
        insertedVnodeQueue.push(innerNode);
        break
      }
    }
    // unlike a newly created component,
    // a reactivated keep-alive component doesn't insert itself
    insert(parentElm, vnode.elm, refElm);
  }

  function insert (parent, elm, ref$$1) {
    if (isDef(parent)) {
      if (isDef(ref$$1)) {
        if (ref$$1.parentNode === parent) {
          nodeOps.insertBefore(parent, elm, ref$$1);
        }
      } else {
        nodeOps.appendChild(parent, elm);
      }
    }
  }

  function createChildren (vnode, children, insertedVnodeQueue) {
    if (Array.isArray(children)) {
      if (false) {
        checkDuplicateKeys(children);
      }
      for (var i = 0; i < children.length; ++i) {
        createElm(children[i], insertedVnodeQueue, vnode.elm, null, true, children, i);
      }
    } else if (isPrimitive(vnode.text)) {
      nodeOps.appendChild(vnode.elm, nodeOps.createTextNode(String(vnode.text)));
    }
  }

  function isPatchable (vnode) {
    while (vnode.componentInstance) {
      vnode = vnode.componentInstance._vnode;
    }
    return isDef(vnode.tag)
  }

  function invokeCreateHooks (vnode, insertedVnodeQueue) {
    for (var i$1 = 0; i$1 < cbs.create.length; ++i$1) {
      cbs.create[i$1](emptyNode, vnode);
    }
    i = vnode.data.hook; // Reuse variable
    if (isDef(i)) {
      if (isDef(i.create)) { i.create(emptyNode, vnode); }
      if (isDef(i.insert)) { insertedVnodeQueue.push(vnode); }
    }
  }

  // set scope id attribute for scoped CSS.
  // this is implemented as a special case to avoid the overhead
  // of going through the normal attribute patching process.
  function setScope (vnode) {
    var i;
    if (isDef(i = vnode.fnScopeId)) {
      nodeOps.setStyleScope(vnode.elm, i);
    } else {
      var ancestor = vnode;
      while (ancestor) {
        if (isDef(i = ancestor.context) && isDef(i = i.$options._scopeId)) {
          nodeOps.setStyleScope(vnode.elm, i);
        }
        ancestor = ancestor.parent;
      }
    }
    // for slot content they should also get the scopeId from the host instance.
    if (isDef(i = activeInstance) &&
      i !== vnode.context &&
      i !== vnode.fnContext &&
      isDef(i = i.$options._scopeId)
    ) {
      nodeOps.setStyleScope(vnode.elm, i);
    }
  }

  function addVnodes (parentElm, refElm, vnodes, startIdx, endIdx, insertedVnodeQueue) {
    for (; startIdx <= endIdx; ++startIdx) {
      createElm(vnodes[startIdx], insertedVnodeQueue, parentElm, refElm, false, vnodes, startIdx);
    }
  }

  function invokeDestroyHook (vnode) {
    var i, j;
    var data = vnode.data;
    if (isDef(data)) {
      if (isDef(i = data.hook) && isDef(i = i.destroy)) { i(vnode); }
      for (i = 0; i < cbs.destroy.length; ++i) { cbs.destroy[i](vnode); }
    }
    if (isDef(i = vnode.children)) {
      for (j = 0; j < vnode.children.length; ++j) {
        invokeDestroyHook(vnode.children[j]);
      }
    }
  }

  function removeVnodes (parentElm, vnodes, startIdx, endIdx) {
    for (; startIdx <= endIdx; ++startIdx) {
      var ch = vnodes[startIdx];
      if (isDef(ch)) {
        if (isDef(ch.tag)) {
          removeAndInvokeRemoveHook(ch);
          invokeDestroyHook(ch);
        } else { // Text node
          removeNode(ch.elm);
        }
      }
    }
  }

  function removeAndInvokeRemoveHook (vnode, rm) {
    if (isDef(rm) || isDef(vnode.data)) {
      var i;
      var listeners = cbs.remove.length + 1;
      if (isDef(rm)) {
        // we have a recursively passed down rm callback
        // increase the listeners count
        rm.listeners += listeners;
      } else {
        // directly removing
        rm = createRmCb(vnode.elm, listeners);
      }
      // recursively invoke hooks on child component root node
      if (isDef(i = vnode.componentInstance) && isDef(i = i._vnode) && isDef(i.data)) {
        removeAndInvokeRemoveHook(i, rm);
      }
      for (i = 0; i < cbs.remove.length; ++i) {
        cbs.remove[i](vnode, rm);
      }
      if (isDef(i = vnode.data.hook) && isDef(i = i.remove)) {
        i(vnode, rm);
      } else {
        rm();
      }
    } else {
      removeNode(vnode.elm);
    }
  }

  function updateChildren (parentElm, oldCh, newCh, insertedVnodeQueue, removeOnly) {
    var oldStartIdx = 0;
    var newStartIdx = 0;
    var oldEndIdx = oldCh.length - 1;
    var oldStartVnode = oldCh[0];
    var oldEndVnode = oldCh[oldEndIdx];
    var newEndIdx = newCh.length - 1;
    var newStartVnode = newCh[0];
    var newEndVnode = newCh[newEndIdx];
    var oldKeyToIdx, idxInOld, vnodeToMove, refElm;

    // removeOnly is a special flag used only by <transition-group>
    // to ensure removed elements stay in correct relative positions
    // during leaving transitions
    var canMove = !removeOnly;

    if (false) {
      checkDuplicateKeys(newCh);
    }

    while (oldStartIdx <= oldEndIdx && newStartIdx <= newEndIdx) {
      if (isUndef(oldStartVnode)) {
        oldStartVnode = oldCh[++oldStartIdx]; // Vnode has been moved left
      } else if (isUndef(oldEndVnode)) {
        oldEndVnode = oldCh[--oldEndIdx];
      } else if (sameVnode(oldStartVnode, newStartVnode)) {
        patchVnode(oldStartVnode, newStartVnode, insertedVnodeQueue);
        oldStartVnode = oldCh[++oldStartIdx];
        newStartVnode = newCh[++newStartIdx];
      } else if (sameVnode(oldEndVnode, newEndVnode)) {
        patchVnode(oldEndVnode, newEndVnode, insertedVnodeQueue);
        oldEndVnode = oldCh[--oldEndIdx];
        newEndVnode = newCh[--newEndIdx];
      } else if (sameVnode(oldStartVnode, newEndVnode)) { // Vnode moved right
        patchVnode(oldStartVnode, newEndVnode, insertedVnodeQueue);
        canMove && nodeOps.insertBefore(parentElm, oldStartVnode.elm, nodeOps.nextSibling(oldEndVnode.elm));
        oldStartVnode = oldCh[++oldStartIdx];
        newEndVnode = newCh[--newEndIdx];
      } else if (sameVnode(oldEndVnode, newStartVnode)) { // Vnode moved left
        patchVnode(oldEndVnode, newStartVnode, insertedVnodeQueue);
        canMove && nodeOps.insertBefore(parentElm, oldEndVnode.elm, oldStartVnode.elm);
        oldEndVnode = oldCh[--oldEndIdx];
        newStartVnode = newCh[++newStartIdx];
      } else {
        if (isUndef(oldKeyToIdx)) { oldKeyToIdx = createKeyToOldIdx(oldCh, oldStartIdx, oldEndIdx); }
        idxInOld = isDef(newStartVnode.key)
          ? oldKeyToIdx[newStartVnode.key]
          : findIdxInOld(newStartVnode, oldCh, oldStartIdx, oldEndIdx);
        if (isUndef(idxInOld)) { // New element
          createElm(newStartVnode, insertedVnodeQueue, parentElm, oldStartVnode.elm, false, newCh, newStartIdx);
        } else {
          vnodeToMove = oldCh[idxInOld];
          if (sameVnode(vnodeToMove, newStartVnode)) {
            patchVnode(vnodeToMove, newStartVnode, insertedVnodeQueue);
            oldCh[idxInOld] = undefined;
            canMove && nodeOps.insertBefore(parentElm, vnodeToMove.elm, oldStartVnode.elm);
          } else {
            // same key but different element. treat as new element
            createElm(newStartVnode, insertedVnodeQueue, parentElm, oldStartVnode.elm, false, newCh, newStartIdx);
          }
        }
        newStartVnode = newCh[++newStartIdx];
      }
    }
    if (oldStartIdx > oldEndIdx) {
      refElm = isUndef(newCh[newEndIdx + 1]) ? null : newCh[newEndIdx + 1].elm;
      addVnodes(parentElm, refElm, newCh, newStartIdx, newEndIdx, insertedVnodeQueue);
    } else if (newStartIdx > newEndIdx) {
      removeVnodes(parentElm, oldCh, oldStartIdx, oldEndIdx);
    }
  }

  function checkDuplicateKeys (children) {
    var seenKeys = {};
    for (var i = 0; i < children.length; i++) {
      var vnode = children[i];
      var key = vnode.key;
      if (isDef(key)) {
        if (seenKeys[key]) {
          warn(
            ("Duplicate keys detected: '" + key + "'. This may cause an update error."),
            vnode.context
          );
        } else {
          seenKeys[key] = true;
        }
      }
    }
  }

  function findIdxInOld (node, oldCh, start, end) {
    for (var i = start; i < end; i++) {
      var c = oldCh[i];
      if (isDef(c) && sameVnode(node, c)) { return i }
    }
  }

  function patchVnode (oldVnode, vnode, insertedVnodeQueue, removeOnly) {
    if (oldVnode === vnode) {
      return
    }

    var elm = vnode.elm = oldVnode.elm;

    if (isTrue(oldVnode.isAsyncPlaceholder)) {
      if (isDef(vnode.asyncFactory.resolved)) {
        hydrate(oldVnode.elm, vnode, insertedVnodeQueue);
      } else {
        vnode.isAsyncPlaceholder = true;
      }
      return
    }

    // reuse element for static trees.
    // note we only do this if the vnode is cloned -
    // if the new node is not cloned it means the render functions have been
    // reset by the hot-reload-api and we need to do a proper re-render.
    if (isTrue(vnode.isStatic) &&
      isTrue(oldVnode.isStatic) &&
      vnode.key === oldVnode.key &&
      (isTrue(vnode.isCloned) || isTrue(vnode.isOnce))
    ) {
      vnode.componentInstance = oldVnode.componentInstance;
      return
    }

    var i;
    var data = vnode.data;
    if (isDef(data) && isDef(i = data.hook) && isDef(i = i.prepatch)) {
      i(oldVnode, vnode);
    }

    var oldCh = oldVnode.children;
    var ch = vnode.children;
    if (isDef(data) && isPatchable(vnode)) {
      for (i = 0; i < cbs.update.length; ++i) { cbs.update[i](oldVnode, vnode); }
      if (isDef(i = data.hook) && isDef(i = i.update)) { i(oldVnode, vnode); }
    }
    if (isUndef(vnode.text)) {
      if (isDef(oldCh) && isDef(ch)) {
        if (oldCh !== ch) { updateChildren(elm, oldCh, ch, insertedVnodeQueue, removeOnly); }
      } else if (isDef(ch)) {
        if (isDef(oldVnode.text)) { nodeOps.setTextContent(elm, ''); }
        addVnodes(elm, null, ch, 0, ch.length - 1, insertedVnodeQueue);
      } else if (isDef(oldCh)) {
        removeVnodes(elm, oldCh, 0, oldCh.length - 1);
      } else if (isDef(oldVnode.text)) {
        nodeOps.setTextContent(elm, '');
      }
    } else if (oldVnode.text !== vnode.text) {
      nodeOps.setTextContent(elm, vnode.text);
    }
    if (isDef(data)) {
      if (isDef(i = data.hook) && isDef(i = i.postpatch)) { i(oldVnode, vnode); }
    }
  }

  function invokeInsertHook (vnode, queue, initial) {
    // delay insert hooks for component root nodes, invoke them after the
    // element is really inserted
    if (isTrue(initial) && isDef(vnode.parent)) {
      vnode.parent.data.pendingInsert = queue;
    } else {
      for (var i = 0; i < queue.length; ++i) {
        queue[i].data.hook.insert(queue[i]);
      }
    }
  }

  var hydrationBailed = false;
  // list of modules that can skip create hook during hydration because they
  // are already rendered on the client or has no need for initialization
  // Note: style is excluded because it relies on initial clone for future
  // deep updates (#7063).
  var isRenderedModule = makeMap('attrs,class,staticClass,staticStyle,key');

  // Note: this is a browser-only function so we can assume elms are DOM nodes.
  function hydrate (elm, vnode, insertedVnodeQueue, inVPre) {
    var i;
    var tag = vnode.tag;
    var data = vnode.data;
    var children = vnode.children;
    inVPre = inVPre || (data && data.pre);
    vnode.elm = elm;

    if (isTrue(vnode.isComment) && isDef(vnode.asyncFactory)) {
      vnode.isAsyncPlaceholder = true;
      return true
    }
    // assert node match
    if (false) {
      if (!assertNodeMatch(elm, vnode, inVPre)) {
        return false
      }
    }
    if (isDef(data)) {
      if (isDef(i = data.hook) && isDef(i = i.init)) { i(vnode, true /* hydrating */); }
      if (isDef(i = vnode.componentInstance)) {
        // child component. it should have hydrated its own tree.
        initComponent(vnode, insertedVnodeQueue);
        return true
      }
    }
    if (isDef(tag)) {
      if (isDef(children)) {
        // empty element, allow client to pick up and populate children
        if (!elm.hasChildNodes()) {
          createChildren(vnode, children, insertedVnodeQueue);
        } else {
          // v-html and domProps: innerHTML
          if (isDef(i = data) && isDef(i = i.domProps) && isDef(i = i.innerHTML)) {
            if (i !== elm.innerHTML) {
              /* istanbul ignore if */
              if (false
              ) {
                hydrationBailed = true;
                console.warn('Parent: ', elm);
                console.warn('server innerHTML: ', i);
                console.warn('client innerHTML: ', elm.innerHTML);
              }
              return false
            }
          } else {
            // iterate and compare children lists
            var childrenMatch = true;
            var childNode = elm.firstChild;
            for (var i$1 = 0; i$1 < children.length; i$1++) {
              if (!childNode || !hydrate(childNode, children[i$1], insertedVnodeQueue, inVPre)) {
                childrenMatch = false;
                break
              }
              childNode = childNode.nextSibling;
            }
            // if childNode is not null, it means the actual childNodes list is
            // longer than the virtual children list.
            if (!childrenMatch || childNode) {
              /* istanbul ignore if */
              if (false
              ) {
                hydrationBailed = true;
                console.warn('Parent: ', elm);
                console.warn('Mismatching childNodes vs. VNodes: ', elm.childNodes, children);
              }
              return false
            }
          }
        }
      }
      if (isDef(data)) {
        var fullInvoke = false;
        for (var key in data) {
          if (!isRenderedModule(key)) {
            fullInvoke = true;
            invokeCreateHooks(vnode, insertedVnodeQueue);
            break
          }
        }
        if (!fullInvoke && data['class']) {
          // ensure collecting deps for deep class bindings for future updates
          traverse(data['class']);
        }
      }
    } else if (elm.data !== vnode.text) {
      elm.data = vnode.text;
    }
    return true
  }

  function assertNodeMatch (node, vnode, inVPre) {
    if (isDef(vnode.tag)) {
      return vnode.tag.indexOf('vue-component') === 0 || (
        !isUnknownElement$$1(vnode, inVPre) &&
        vnode.tag.toLowerCase() === (node.tagName && node.tagName.toLowerCase())
      )
    } else {
      return node.nodeType === (vnode.isComment ? 8 : 3)
    }
  }

  return function patch (oldVnode, vnode, hydrating, removeOnly, parentElm, refElm) {
    if (isUndef(vnode)) {
      if (isDef(oldVnode)) { invokeDestroyHook(oldVnode); }
      return
    }

    var isInitialPatch = false;
    var insertedVnodeQueue = [];

    if (isUndef(oldVnode)) {
      // empty mount (likely as component), create new root element
      isInitialPatch = true;
      createElm(vnode, insertedVnodeQueue, parentElm, refElm);
    } else {
      var isRealElement = isDef(oldVnode.nodeType);
      if (!isRealElement && sameVnode(oldVnode, vnode)) {
        // patch existing root node
        patchVnode(oldVnode, vnode, insertedVnodeQueue, removeOnly);
      } else {
        if (isRealElement) {
          // mounting to a real element
          // check if this is server-rendered content and if we can perform
          // a successful hydration.
          if (oldVnode.nodeType === 1 && oldVnode.hasAttribute(SSR_ATTR)) {
            oldVnode.removeAttribute(SSR_ATTR);
            hydrating = true;
          }
          if (isTrue(hydrating)) {
            if (hydrate(oldVnode, vnode, insertedVnodeQueue)) {
              invokeInsertHook(vnode, insertedVnodeQueue, true);
              return oldVnode
            } else if (false) {
              warn(
                'The client-side rendered virtual DOM tree is not matching ' +
                'server-rendered content. This is likely caused by incorrect ' +
                'HTML markup, for example nesting block-level elements inside ' +
                '<p>, or missing <tbody>. Bailing hydration and performing ' +
                'full client-side render.'
              );
            }
          }
          // either not server-rendered, or hydration failed.
          // create an empty node and replace it
          oldVnode = emptyNodeAt(oldVnode);
        }

        // replacing existing element
        var oldElm = oldVnode.elm;
        var parentElm$1 = nodeOps.parentNode(oldElm);

        // create new node
        createElm(
          vnode,
          insertedVnodeQueue,
          // extremely rare edge case: do not insert if old element is in a
          // leaving transition. Only happens when combining transition +
          // keep-alive + HOCs. (#4590)
          oldElm._leaveCb ? null : parentElm$1,
          nodeOps.nextSibling(oldElm)
        );

        // update parent placeholder node element, recursively
        if (isDef(vnode.parent)) {
          var ancestor = vnode.parent;
          var patchable = isPatchable(vnode);
          while (ancestor) {
            for (var i = 0; i < cbs.destroy.length; ++i) {
              cbs.destroy[i](ancestor);
            }
            ancestor.elm = vnode.elm;
            if (patchable) {
              for (var i$1 = 0; i$1 < cbs.create.length; ++i$1) {
                cbs.create[i$1](emptyNode, ancestor);
              }
              // #6513
              // invoke insert hooks that may have been merged by create hooks.
              // e.g. for directives that uses the "inserted" hook.
              var insert = ancestor.data.hook.insert;
              if (insert.merged) {
                // start at index 1 to avoid re-invoking component mounted hook
                for (var i$2 = 1; i$2 < insert.fns.length; i$2++) {
                  insert.fns[i$2]();
                }
              }
            } else {
              registerRef(ancestor);
            }
            ancestor = ancestor.parent;
          }
        }

        // destroy old node
        if (isDef(parentElm$1)) {
          removeVnodes(parentElm$1, [oldVnode], 0, 0);
        } else if (isDef(oldVnode.tag)) {
          invokeDestroyHook(oldVnode);
        }
      }
    }

    invokeInsertHook(vnode, insertedVnodeQueue, isInitialPatch);
    return vnode.elm
  }
}

/*  */

var directives = {
  create: updateDirectives,
  update: updateDirectives,
  destroy: function unbindDirectives (vnode) {
    updateDirectives(vnode, emptyNode);
  }
}

function updateDirectives (oldVnode, vnode) {
  if (oldVnode.data.directives || vnode.data.directives) {
    _update(oldVnode, vnode);
  }
}

function _update (oldVnode, vnode) {
  var isCreate = oldVnode === emptyNode;
  var isDestroy = vnode === emptyNode;
  var oldDirs = normalizeDirectives$1(oldVnode.data.directives, oldVnode.context);
  var newDirs = normalizeDirectives$1(vnode.data.directives, vnode.context);

  var dirsWithInsert = [];
  var dirsWithPostpatch = [];

  var key, oldDir, dir;
  for (key in newDirs) {
    oldDir = oldDirs[key];
    dir = newDirs[key];
    if (!oldDir) {
      // new directive, bind
      callHook$1(dir, 'bind', vnode, oldVnode);
      if (dir.def && dir.def.inserted) {
        dirsWithInsert.push(dir);
      }
    } else {
      // existing directive, update
      dir.oldValue = oldDir.value;
      callHook$1(dir, 'update', vnode, oldVnode);
      if (dir.def && dir.def.componentUpdated) {
        dirsWithPostpatch.push(dir);
      }
    }
  }

  if (dirsWithInsert.length) {
    var callInsert = function () {
      for (var i = 0; i < dirsWithInsert.length; i++) {
        callHook$1(dirsWithInsert[i], 'inserted', vnode, oldVnode);
      }
    };
    if (isCreate) {
      mergeVNodeHook(vnode, 'insert', callInsert);
    } else {
      callInsert();
    }
  }

  if (dirsWithPostpatch.length) {
    mergeVNodeHook(vnode, 'postpatch', function () {
      for (var i = 0; i < dirsWithPostpatch.length; i++) {
        callHook$1(dirsWithPostpatch[i], 'componentUpdated', vnode, oldVnode);
      }
    });
  }

  if (!isCreate) {
    for (key in oldDirs) {
      if (!newDirs[key]) {
        // no longer present, unbind
        callHook$1(oldDirs[key], 'unbind', oldVnode, oldVnode, isDestroy);
      }
    }
  }
}

var emptyModifiers = Object.create(null);

function normalizeDirectives$1 (
  dirs,
  vm
) {
  var res = Object.create(null);
  if (!dirs) {
    // $flow-disable-line
    return res
  }
  var i, dir;
  for (i = 0; i < dirs.length; i++) {
    dir = dirs[i];
    if (!dir.modifiers) {
      // $flow-disable-line
      dir.modifiers = emptyModifiers;
    }
    res[getRawDirName(dir)] = dir;
    dir.def = resolveAsset(vm.$options, 'directives', dir.name, true);
  }
  // $flow-disable-line
  return res
}

function getRawDirName (dir) {
  return dir.rawName || ((dir.name) + "." + (Object.keys(dir.modifiers || {}).join('.')))
}

function callHook$1 (dir, hook, vnode, oldVnode, isDestroy) {
  var fn = dir.def && dir.def[hook];
  if (fn) {
    try {
      fn(vnode.elm, dir, vnode, oldVnode, isDestroy);
    } catch (e) {
      handleError(e, vnode.context, ("directive " + (dir.name) + " " + hook + " hook"));
    }
  }
}

var baseModules = [
  ref,
  directives
]

/*  */

function updateAttrs (oldVnode, vnode) {
  var opts = vnode.componentOptions;
  if (isDef(opts) && opts.Ctor.options.inheritAttrs === false) {
    return
  }
  if (isUndef(oldVnode.data.attrs) && isUndef(vnode.data.attrs)) {
    return
  }
  var key, cur, old;
  var elm = vnode.elm;
  var oldAttrs = oldVnode.data.attrs || {};
  var attrs = vnode.data.attrs || {};
  // clone observed objects, as the user probably wants to mutate it
  if (isDef(attrs.__ob__)) {
    attrs = vnode.data.attrs = extend({}, attrs);
  }

  for (key in attrs) {
    cur = attrs[key];
    old = oldAttrs[key];
    if (old !== cur) {
      setAttr(elm, key, cur);
    }
  }
  // #4391: in IE9, setting type can reset value for input[type=radio]
  // #6666: IE/Edge forces progress value down to 1 before setting a max
  /* istanbul ignore if */
  if ((isIE || isEdge) && attrs.value !== oldAttrs.value) {
    setAttr(elm, 'value', attrs.value);
  }
  for (key in oldAttrs) {
    if (isUndef(attrs[key])) {
      if (isXlink(key)) {
        elm.removeAttributeNS(xlinkNS, getXlinkProp(key));
      } else if (!isEnumeratedAttr(key)) {
        elm.removeAttribute(key);
      }
    }
  }
}

function setAttr (el, key, value) {
  if (el.tagName.indexOf('-') > -1) {
    baseSetAttr(el, key, value);
  } else if (isBooleanAttr(key)) {
    // set attribute for blank value
    // e.g. <option disabled>Select one</option>
    if (isFalsyAttrValue(value)) {
      el.removeAttribute(key);
    } else {
      // technically allowfullscreen is a boolean attribute for <iframe>,
      // but Flash expects a value of "true" when used on <embed> tag
      value = key === 'allowfullscreen' && el.tagName === 'EMBED'
        ? 'true'
        : key;
      el.setAttribute(key, value);
    }
  } else if (isEnumeratedAttr(key)) {
    el.setAttribute(key, isFalsyAttrValue(value) || value === 'false' ? 'false' : 'true');
  } else if (isXlink(key)) {
    if (isFalsyAttrValue(value)) {
      el.removeAttributeNS(xlinkNS, getXlinkProp(key));
    } else {
      el.setAttributeNS(xlinkNS, key, value);
    }
  } else {
    baseSetAttr(el, key, value);
  }
}

function baseSetAttr (el, key, value) {
  if (isFalsyAttrValue(value)) {
    el.removeAttribute(key);
  } else {
    // #7138: IE10 & 11 fires input event when setting placeholder on
    // <textarea>... block the first input event and remove the blocker
    // immediately.
    /* istanbul ignore if */
    if (
      isIE && !isIE9 &&
      el.tagName === 'TEXTAREA' &&
      key === 'placeholder' && !el.__ieph
    ) {
      var blocker = function (e) {
        e.stopImmediatePropagation();
        el.removeEventListener('input', blocker);
      };
      el.addEventListener('input', blocker);
      // $flow-disable-line
      el.__ieph = true; /* IE placeholder patched */
    }
    el.setAttribute(key, value);
  }
}

var attrs = {
  create: updateAttrs,
  update: updateAttrs
}

/*  */

function updateClass (oldVnode, vnode) {
  var el = vnode.elm;
  var data = vnode.data;
  var oldData = oldVnode.data;
  if (
    isUndef(data.staticClass) &&
    isUndef(data.class) && (
      isUndef(oldData) || (
        isUndef(oldData.staticClass) &&
        isUndef(oldData.class)
      )
    )
  ) {
    return
  }

  var cls = genClassForVnode(vnode);

  // handle transition classes
  var transitionClass = el._transitionClasses;
  if (isDef(transitionClass)) {
    cls = concat(cls, stringifyClass(transitionClass));
  }

  // set the class
  if (cls !== el._prevClass) {
    el.setAttribute('class', cls);
    el._prevClass = cls;
  }
}

var klass = {
  create: updateClass,
  update: updateClass
}

/*  */

var validDivisionCharRE = /[\w).+\-_$\]]/;

function parseFilters (exp) {
  var inSingle = false;
  var inDouble = false;
  var inTemplateString = false;
  var inRegex = false;
  var curly = 0;
  var square = 0;
  var paren = 0;
  var lastFilterIndex = 0;
  var c, prev, i, expression, filters;

  for (i = 0; i < exp.length; i++) {
    prev = c;
    c = exp.charCodeAt(i);
    if (inSingle) {
      if (c === 0x27 && prev !== 0x5C) { inSingle = false; }
    } else if (inDouble) {
      if (c === 0x22 && prev !== 0x5C) { inDouble = false; }
    } else if (inTemplateString) {
      if (c === 0x60 && prev !== 0x5C) { inTemplateString = false; }
    } else if (inRegex) {
      if (c === 0x2f && prev !== 0x5C) { inRegex = false; }
    } else if (
      c === 0x7C && // pipe
      exp.charCodeAt(i + 1) !== 0x7C &&
      exp.charCodeAt(i - 1) !== 0x7C &&
      !curly && !square && !paren
    ) {
      if (expression === undefined) {
        // first filter, end of expression
        lastFilterIndex = i + 1;
        expression = exp.slice(0, i).trim();
      } else {
        pushFilter();
      }
    } else {
      switch (c) {
        case 0x22: inDouble = true; break         // "
        case 0x27: inSingle = true; break         // '
        case 0x60: inTemplateString = true; break // `
        case 0x28: paren++; break                 // (
        case 0x29: paren--; break                 // )
        case 0x5B: square++; break                // [
        case 0x5D: square--; break                // ]
        case 0x7B: curly++; break                 // {
        case 0x7D: curly--; break                 // }
      }
      if (c === 0x2f) { // /
        var j = i - 1;
        var p = (void 0);
        // find first non-whitespace prev char
        for (; j >= 0; j--) {
          p = exp.charAt(j);
          if (p !== ' ') { break }
        }
        if (!p || !validDivisionCharRE.test(p)) {
          inRegex = true;
        }
      }
    }
  }

  if (expression === undefined) {
    expression = exp.slice(0, i).trim();
  } else if (lastFilterIndex !== 0) {
    pushFilter();
  }

  function pushFilter () {
    (filters || (filters = [])).push(exp.slice(lastFilterIndex, i).trim());
    lastFilterIndex = i + 1;
  }

  if (filters) {
    for (i = 0; i < filters.length; i++) {
      expression = wrapFilter(expression, filters[i]);
    }
  }

  return expression
}

function wrapFilter (exp, filter) {
  var i = filter.indexOf('(');
  if (i < 0) {
    // _f: resolveFilter
    return ("_f(\"" + filter + "\")(" + exp + ")")
  } else {
    var name = filter.slice(0, i);
    var args = filter.slice(i + 1);
    return ("_f(\"" + name + "\")(" + exp + (args !== ')' ? ',' + args : args))
  }
}

/*  */

function baseWarn (msg) {
  console.error(("[Vue compiler]: " + msg));
}

function pluckModuleFunction (
  modules,
  key
) {
  return modules
    ? modules.map(function (m) { return m[key]; }).filter(function (_) { return _; })
    : []
}

function addProp (el, name, value) {
  (el.props || (el.props = [])).push({ name: name, value: value });
  el.plain = false;
}

function addAttr (el, name, value) {
  (el.attrs || (el.attrs = [])).push({ name: name, value: value });
  el.plain = false;
}

// add a raw attr (use this in preTransforms)
function addRawAttr (el, name, value) {
  el.attrsMap[name] = value;
  el.attrsList.push({ name: name, value: value });
}

function addDirective (
  el,
  name,
  rawName,
  value,
  arg,
  modifiers
) {
  (el.directives || (el.directives = [])).push({ name: name, rawName: rawName, value: value, arg: arg, modifiers: modifiers });
  el.plain = false;
}

function addHandler (
  el,
  name,
  value,
  modifiers,
  important,
  warn
) {
  modifiers = modifiers || emptyObject;
  // warn prevent and passive modifier
  /* istanbul ignore if */
  if (
    false
  ) {
    warn(
      'passive and prevent can\'t be used together. ' +
      'Passive handler can\'t prevent default event.'
    );
  }

  // check capture modifier
  if (modifiers.capture) {
    delete modifiers.capture;
    name = '!' + name; // mark the event as captured
  }
  if (modifiers.once) {
    delete modifiers.once;
    name = '~' + name; // mark the event as once
  }
  /* istanbul ignore if */
  if (modifiers.passive) {
    delete modifiers.passive;
    name = '&' + name; // mark the event as passive
  }

  // normalize click.right and click.middle since they don't actually fire
  // this is technically browser-specific, but at least for now browsers are
  // the only target envs that have right/middle clicks.
  if (name === 'click') {
    if (modifiers.right) {
      name = 'contextmenu';
      delete modifiers.right;
    } else if (modifiers.middle) {
      name = 'mouseup';
    }
  }

  var events;
  if (modifiers.native) {
    delete modifiers.native;
    events = el.nativeEvents || (el.nativeEvents = {});
  } else {
    events = el.events || (el.events = {});
  }

  var newHandler = {
    value: value.trim()
  };
  if (modifiers !== emptyObject) {
    newHandler.modifiers = modifiers;
  }

  var handlers = events[name];
  /* istanbul ignore if */
  if (Array.isArray(handlers)) {
    important ? handlers.unshift(newHandler) : handlers.push(newHandler);
  } else if (handlers) {
    events[name] = important ? [newHandler, handlers] : [handlers, newHandler];
  } else {
    events[name] = newHandler;
  }

  el.plain = false;
}

function getBindingAttr (
  el,
  name,
  getStatic
) {
  var dynamicValue =
    getAndRemoveAttr(el, ':' + name) ||
    getAndRemoveAttr(el, 'v-bind:' + name);
  if (dynamicValue != null) {
    return parseFilters(dynamicValue)
  } else if (getStatic !== false) {
    var staticValue = getAndRemoveAttr(el, name);
    if (staticValue != null) {
      return JSON.stringify(staticValue)
    }
  }
}

// note: this only removes the attr from the Array (attrsList) so that it
// doesn't get processed by processAttrs.
// By default it does NOT remove it from the map (attrsMap) because the map is
// needed during codegen.
function getAndRemoveAttr (
  el,
  name,
  removeFromMap
) {
  var val;
  if ((val = el.attrsMap[name]) != null) {
    var list = el.attrsList;
    for (var i = 0, l = list.length; i < l; i++) {
      if (list[i].name === name) {
        list.splice(i, 1);
        break
      }
    }
  }
  if (removeFromMap) {
    delete el.attrsMap[name];
  }
  return val
}

/*  */

/**
 * Cross-platform code generation for component v-model
 */
function genComponentModel (
  el,
  value,
  modifiers
) {
  var ref = modifiers || {};
  var number = ref.number;
  var trim = ref.trim;

  var baseValueExpression = '$$v';
  var valueExpression = baseValueExpression;
  if (trim) {
    valueExpression =
      "(typeof " + baseValueExpression + " === 'string'" +
      "? " + baseValueExpression + ".trim()" +
      ": " + baseValueExpression + ")";
  }
  if (number) {
    valueExpression = "_n(" + valueExpression + ")";
  }
  var assignment = genAssignmentCode(value, valueExpression);

  el.model = {
    value: ("(" + value + ")"),
    expression: ("\"" + value + "\""),
    callback: ("function (" + baseValueExpression + ") {" + assignment + "}")
  };
}

/**
 * Cross-platform codegen helper for generating v-model value assignment code.
 */
function genAssignmentCode (
  value,
  assignment
) {
  var res = parseModel(value);
  if (res.key === null) {
    return (value + "=" + assignment)
  } else {
    return ("$set(" + (res.exp) + ", " + (res.key) + ", " + assignment + ")")
  }
}

/**
 * Parse a v-model expression into a base path and a final key segment.
 * Handles both dot-path and possible square brackets.
 *
 * Possible cases:
 *
 * - test
 * - test[key]
 * - test[test1[key]]
 * - test["a"][key]
 * - xxx.test[a[a].test1[key]]
 * - test.xxx.a["asa"][test1[key]]
 *
 */

var len;
var str;
var chr;
var index$1;
var expressionPos;
var expressionEndPos;



function parseModel (val) {
  // Fix https://github.com/vuejs/vue/pull/7730
  // allow v-model="obj.val " (trailing whitespace)
  val = val.trim();
  len = val.length;

  if (val.indexOf('[') < 0 || val.lastIndexOf(']') < len - 1) {
    index$1 = val.lastIndexOf('.');
    if (index$1 > -1) {
      return {
        exp: val.slice(0, index$1),
        key: '"' + val.slice(index$1 + 1) + '"'
      }
    } else {
      return {
        exp: val,
        key: null
      }
    }
  }

  str = val;
  index$1 = expressionPos = expressionEndPos = 0;

  while (!eof()) {
    chr = next();
    /* istanbul ignore if */
    if (isStringStart(chr)) {
      parseString(chr);
    } else if (chr === 0x5B) {
      parseBracket(chr);
    }
  }

  return {
    exp: val.slice(0, expressionPos),
    key: val.slice(expressionPos + 1, expressionEndPos)
  }
}

function next () {
  return str.charCodeAt(++index$1)
}

function eof () {
  return index$1 >= len
}

function isStringStart (chr) {
  return chr === 0x22 || chr === 0x27
}

function parseBracket (chr) {
  var inBracket = 1;
  expressionPos = index$1;
  while (!eof()) {
    chr = next();
    if (isStringStart(chr)) {
      parseString(chr);
      continue
    }
    if (chr === 0x5B) { inBracket++; }
    if (chr === 0x5D) { inBracket--; }
    if (inBracket === 0) {
      expressionEndPos = index$1;
      break
    }
  }
}

function parseString (chr) {
  var stringQuote = chr;
  while (!eof()) {
    chr = next();
    if (chr === stringQuote) {
      break
    }
  }
}

/*  */

var warn$1;

// in some cases, the event used has to be determined at runtime
// so we used some reserved tokens during compile.
var RANGE_TOKEN = '__r';
var CHECKBOX_RADIO_TOKEN = '__c';

function model (
  el,
  dir,
  _warn
) {
  warn$1 = _warn;
  var value = dir.value;
  var modifiers = dir.modifiers;
  var tag = el.tag;
  var type = el.attrsMap.type;

  if (false) {
    // inputs with type="file" are read only and setting the input's
    // value will throw an error.
    if (tag === 'input' && type === 'file') {
      warn$1(
        "<" + (el.tag) + " v-model=\"" + value + "\" type=\"file\">:\n" +
        "File inputs are read only. Use a v-on:change listener instead."
      );
    }
  }

  if (el.component) {
    genComponentModel(el, value, modifiers);
    // component v-model doesn't need extra runtime
    return false
  } else if (tag === 'select') {
    genSelect(el, value, modifiers);
  } else if (tag === 'input' && type === 'checkbox') {
    genCheckboxModel(el, value, modifiers);
  } else if (tag === 'input' && type === 'radio') {
    genRadioModel(el, value, modifiers);
  } else if (tag === 'input' || tag === 'textarea') {
    genDefaultModel(el, value, modifiers);
  } else if (!config.isReservedTag(tag)) {
    genComponentModel(el, value, modifiers);
    // component v-model doesn't need extra runtime
    return false
  } else if (false) {
    warn$1(
      "<" + (el.tag) + " v-model=\"" + value + "\">: " +
      "v-model is not supported on this element type. " +
      'If you are working with contenteditable, it\'s recommended to ' +
      'wrap a library dedicated for that purpose inside a custom component.'
    );
  }

  // ensure runtime directive metadata
  return true
}

function genCheckboxModel (
  el,
  value,
  modifiers
) {
  var number = modifiers && modifiers.number;
  var valueBinding = getBindingAttr(el, 'value') || 'null';
  var trueValueBinding = getBindingAttr(el, 'true-value') || 'true';
  var falseValueBinding = getBindingAttr(el, 'false-value') || 'false';
  addProp(el, 'checked',
    "Array.isArray(" + value + ")" +
    "?_i(" + value + "," + valueBinding + ")>-1" + (
      trueValueBinding === 'true'
        ? (":(" + value + ")")
        : (":_q(" + value + "," + trueValueBinding + ")")
    )
  );
  addHandler(el, 'change',
    "var $$a=" + value + "," +
        '$$el=$event.target,' +
        "$$c=$$el.checked?(" + trueValueBinding + "):(" + falseValueBinding + ");" +
    'if(Array.isArray($$a)){' +
      "var $$v=" + (number ? '_n(' + valueBinding + ')' : valueBinding) + "," +
          '$$i=_i($$a,$$v);' +
      "if($$el.checked){$$i<0&&(" + (genAssignmentCode(value, '$$a.concat([$$v])')) + ")}" +
      "else{$$i>-1&&(" + (genAssignmentCode(value, '$$a.slice(0,$$i).concat($$a.slice($$i+1))')) + ")}" +
    "}else{" + (genAssignmentCode(value, '$$c')) + "}",
    null, true
  );
}

function genRadioModel (
  el,
  value,
  modifiers
) {
  var number = modifiers && modifiers.number;
  var valueBinding = getBindingAttr(el, 'value') || 'null';
  valueBinding = number ? ("_n(" + valueBinding + ")") : valueBinding;
  addProp(el, 'checked', ("_q(" + value + "," + valueBinding + ")"));
  addHandler(el, 'change', genAssignmentCode(value, valueBinding), null, true);
}

function genSelect (
  el,
  value,
  modifiers
) {
  var number = modifiers && modifiers.number;
  var selectedVal = "Array.prototype.filter" +
    ".call($event.target.options,function(o){return o.selected})" +
    ".map(function(o){var val = \"_value\" in o ? o._value : o.value;" +
    "return " + (number ? '_n(val)' : 'val') + "})";

  var assignment = '$event.target.multiple ? $$selectedVal : $$selectedVal[0]';
  var code = "var $$selectedVal = " + selectedVal + ";";
  code = code + " " + (genAssignmentCode(value, assignment));
  addHandler(el, 'change', code, null, true);
}

function genDefaultModel (
  el,
  value,
  modifiers
) {
  var type = el.attrsMap.type;

  // warn if v-bind:value conflicts with v-model
  // except for inputs with v-bind:type
  if (false) {
    var value$1 = el.attrsMap['v-bind:value'] || el.attrsMap[':value'];
    var typeBinding = el.attrsMap['v-bind:type'] || el.attrsMap[':type'];
    if (value$1 && !typeBinding) {
      var binding = el.attrsMap['v-bind:value'] ? 'v-bind:value' : ':value';
      warn$1(
        binding + "=\"" + value$1 + "\" conflicts with v-model on the same element " +
        'because the latter already expands to a value binding internally'
      );
    }
  }

  var ref = modifiers || {};
  var lazy = ref.lazy;
  var number = ref.number;
  var trim = ref.trim;
  var needCompositionGuard = !lazy && type !== 'range';
  var event = lazy
    ? 'change'
    : type === 'range'
      ? RANGE_TOKEN
      : 'input';

  var valueExpression = '$event.target.value';
  if (trim) {
    valueExpression = "$event.target.value.trim()";
  }
  if (number) {
    valueExpression = "_n(" + valueExpression + ")";
  }

  var code = genAssignmentCode(value, valueExpression);
  if (needCompositionGuard) {
    code = "if($event.target.composing)return;" + code;
  }

  addProp(el, 'value', ("(" + value + ")"));
  addHandler(el, event, code, null, true);
  if (trim || number) {
    addHandler(el, 'blur', '$forceUpdate()');
  }
}

/*  */

// normalize v-model event tokens that can only be determined at runtime.
// it's important to place the event as the first in the array because
// the whole point is ensuring the v-model callback gets called before
// user-attached handlers.
function normalizeEvents (on) {
  /* istanbul ignore if */
  if (isDef(on[RANGE_TOKEN])) {
    // IE input[type=range] only supports `change` event
    var event = isIE ? 'change' : 'input';
    on[event] = [].concat(on[RANGE_TOKEN], on[event] || []);
    delete on[RANGE_TOKEN];
  }
  // This was originally intended to fix #4521 but no longer necessary
  // after 2.5. Keeping it for backwards compat with generated code from < 2.4
  /* istanbul ignore if */
  if (isDef(on[CHECKBOX_RADIO_TOKEN])) {
    on.change = [].concat(on[CHECKBOX_RADIO_TOKEN], on.change || []);
    delete on[CHECKBOX_RADIO_TOKEN];
  }
}

var target$1;

function createOnceHandler (handler, event, capture) {
  var _target = target$1; // save current target element in closure
  return function onceHandler () {
    var res = handler.apply(null, arguments);
    if (res !== null) {
      remove$2(event, onceHandler, capture, _target);
    }
  }
}

function add$1 (
  event,
  handler,
  once$$1,
  capture,
  passive
) {
  handler = withMacroTask(handler);
  if (once$$1) { handler = createOnceHandler(handler, event, capture); }
  target$1.addEventListener(
    event,
    handler,
    supportsPassive
      ? { capture: capture, passive: passive }
      : capture
  );
}

function remove$2 (
  event,
  handler,
  capture,
  _target
) {
  (_target || target$1).removeEventListener(
    event,
    handler._withTask || handler,
    capture
  );
}

function updateDOMListeners (oldVnode, vnode) {
  if (isUndef(oldVnode.data.on) && isUndef(vnode.data.on)) {
    return
  }
  var on = vnode.data.on || {};
  var oldOn = oldVnode.data.on || {};
  target$1 = vnode.elm;
  normalizeEvents(on);
  updateListeners(on, oldOn, add$1, remove$2, vnode.context);
  target$1 = undefined;
}

var events = {
  create: updateDOMListeners,
  update: updateDOMListeners
}

/*  */

function updateDOMProps (oldVnode, vnode) {
  if (isUndef(oldVnode.data.domProps) && isUndef(vnode.data.domProps)) {
    return
  }
  var key, cur;
  var elm = vnode.elm;
  var oldProps = oldVnode.data.domProps || {};
  var props = vnode.data.domProps || {};
  // clone observed objects, as the user probably wants to mutate it
  if (isDef(props.__ob__)) {
    props = vnode.data.domProps = extend({}, props);
  }

  for (key in oldProps) {
    if (isUndef(props[key])) {
      elm[key] = '';
    }
  }
  for (key in props) {
    cur = props[key];
    // ignore children if the node has textContent or innerHTML,
    // as these will throw away existing DOM nodes and cause removal errors
    // on subsequent patches (#3360)
    if (key === 'textContent' || key === 'innerHTML') {
      if (vnode.children) { vnode.children.length = 0; }
      if (cur === oldProps[key]) { continue }
      // #6601 work around Chrome version <= 55 bug where single textNode
      // replaced by innerHTML/textContent retains its parentNode property
      if (elm.childNodes.length === 1) {
        elm.removeChild(elm.childNodes[0]);
      }
    }

    if (key === 'value') {
      // store value as _value as well since
      // non-string values will be stringified
      elm._value = cur;
      // avoid resetting cursor position when value is the same
      var strCur = isUndef(cur) ? '' : String(cur);
      if (shouldUpdateValue(elm, strCur)) {
        elm.value = strCur;
      }
    } else {
      elm[key] = cur;
    }
  }
}

// check platforms/web/util/attrs.js acceptValue


function shouldUpdateValue (elm, checkVal) {
  return (!elm.composing && (
    elm.tagName === 'OPTION' ||
    isNotInFocusAndDirty(elm, checkVal) ||
    isDirtyWithModifiers(elm, checkVal)
  ))
}

function isNotInFocusAndDirty (elm, checkVal) {
  // return true when textbox (.number and .trim) loses focus and its value is
  // not equal to the updated value
  var notInFocus = true;
  // #6157
  // work around IE bug when accessing document.activeElement in an iframe
  try { notInFocus = document.activeElement !== elm; } catch (e) {}
  return notInFocus && elm.value !== checkVal
}

function isDirtyWithModifiers (elm, newVal) {
  var value = elm.value;
  var modifiers = elm._vModifiers; // injected by v-model runtime
  if (isDef(modifiers)) {
    if (modifiers.lazy) {
      // inputs with lazy should only be updated when not in focus
      return false
    }
    if (modifiers.number) {
      return toNumber(value) !== toNumber(newVal)
    }
    if (modifiers.trim) {
      return value.trim() !== newVal.trim()
    }
  }
  return value !== newVal
}

var domProps = {
  create: updateDOMProps,
  update: updateDOMProps
}

/*  */

var parseStyleText = cached(function (cssText) {
  var res = {};
  var listDelimiter = /;(?![^(]*\))/g;
  var propertyDelimiter = /:(.+)/;
  cssText.split(listDelimiter).forEach(function (item) {
    if (item) {
      var tmp = item.split(propertyDelimiter);
      tmp.length > 1 && (res[tmp[0].trim()] = tmp[1].trim());
    }
  });
  return res
});

// merge static and dynamic style data on the same vnode
function normalizeStyleData (data) {
  var style = normalizeStyleBinding(data.style);
  // static style is pre-processed into an object during compilation
  // and is always a fresh object, so it's safe to merge into it
  return data.staticStyle
    ? extend(data.staticStyle, style)
    : style
}

// normalize possible array / string values into Object
function normalizeStyleBinding (bindingStyle) {
  if (Array.isArray(bindingStyle)) {
    return toObject(bindingStyle)
  }
  if (typeof bindingStyle === 'string') {
    return parseStyleText(bindingStyle)
  }
  return bindingStyle
}

/**
 * parent component style should be after child's
 * so that parent component's style could override it
 */
function getStyle (vnode, checkChild) {
  var res = {};
  var styleData;

  if (checkChild) {
    var childNode = vnode;
    while (childNode.componentInstance) {
      childNode = childNode.componentInstance._vnode;
      if (
        childNode && childNode.data &&
        (styleData = normalizeStyleData(childNode.data))
      ) {
        extend(res, styleData);
      }
    }
  }

  if ((styleData = normalizeStyleData(vnode.data))) {
    extend(res, styleData);
  }

  var parentNode = vnode;
  while ((parentNode = parentNode.parent)) {
    if (parentNode.data && (styleData = normalizeStyleData(parentNode.data))) {
      extend(res, styleData);
    }
  }
  return res
}

/*  */

var cssVarRE = /^--/;
var importantRE = /\s*!important$/;
var setProp = function (el, name, val) {
  /* istanbul ignore if */
  if (cssVarRE.test(name)) {
    el.style.setProperty(name, val);
  } else if (importantRE.test(val)) {
    el.style.setProperty(name, val.replace(importantRE, ''), 'important');
  } else {
    var normalizedName = normalize(name);
    if (Array.isArray(val)) {
      // Support values array created by autoprefixer, e.g.
      // {display: ["-webkit-box", "-ms-flexbox", "flex"]}
      // Set them one by one, and the browser will only set those it can recognize
      for (var i = 0, len = val.length; i < len; i++) {
        el.style[normalizedName] = val[i];
      }
    } else {
      el.style[normalizedName] = val;
    }
  }
};

var vendorNames = ['Webkit', 'Moz', 'ms'];

var emptyStyle;
var normalize = cached(function (prop) {
  emptyStyle = emptyStyle || document.createElement('div').style;
  prop = camelize(prop);
  if (prop !== 'filter' && (prop in emptyStyle)) {
    return prop
  }
  var capName = prop.charAt(0).toUpperCase() + prop.slice(1);
  for (var i = 0; i < vendorNames.length; i++) {
    var name = vendorNames[i] + capName;
    if (name in emptyStyle) {
      return name
    }
  }
});

function updateStyle (oldVnode, vnode) {
  var data = vnode.data;
  var oldData = oldVnode.data;

  if (isUndef(data.staticStyle) && isUndef(data.style) &&
    isUndef(oldData.staticStyle) && isUndef(oldData.style)
  ) {
    return
  }

  var cur, name;
  var el = vnode.elm;
  var oldStaticStyle = oldData.staticStyle;
  var oldStyleBinding = oldData.normalizedStyle || oldData.style || {};

  // if static style exists, stylebinding already merged into it when doing normalizeStyleData
  var oldStyle = oldStaticStyle || oldStyleBinding;

  var style = normalizeStyleBinding(vnode.data.style) || {};

  // store normalized style under a different key for next diff
  // make sure to clone it if it's reactive, since the user likely wants
  // to mutate it.
  vnode.data.normalizedStyle = isDef(style.__ob__)
    ? extend({}, style)
    : style;

  var newStyle = getStyle(vnode, true);

  for (name in oldStyle) {
    if (isUndef(newStyle[name])) {
      setProp(el, name, '');
    }
  }
  for (name in newStyle) {
    cur = newStyle[name];
    if (cur !== oldStyle[name]) {
      // ie9 setting to null has no effect, must use empty string
      setProp(el, name, cur == null ? '' : cur);
    }
  }
}

var style = {
  create: updateStyle,
  update: updateStyle
}

/*  */

/**
 * Add class with compatibility for SVG since classList is not supported on
 * SVG elements in IE
 */
function addClass (el, cls) {
  /* istanbul ignore if */
  if (!cls || !(cls = cls.trim())) {
    return
  }

  /* istanbul ignore else */
  if (el.classList) {
    if (cls.indexOf(' ') > -1) {
      cls.split(/\s+/).forEach(function (c) { return el.classList.add(c); });
    } else {
      el.classList.add(cls);
    }
  } else {
    var cur = " " + (el.getAttribute('class') || '') + " ";
    if (cur.indexOf(' ' + cls + ' ') < 0) {
      el.setAttribute('class', (cur + cls).trim());
    }
  }
}

/**
 * Remove class with compatibility for SVG since classList is not supported on
 * SVG elements in IE
 */
function removeClass (el, cls) {
  /* istanbul ignore if */
  if (!cls || !(cls = cls.trim())) {
    return
  }

  /* istanbul ignore else */
  if (el.classList) {
    if (cls.indexOf(' ') > -1) {
      cls.split(/\s+/).forEach(function (c) { return el.classList.remove(c); });
    } else {
      el.classList.remove(cls);
    }
    if (!el.classList.length) {
      el.removeAttribute('class');
    }
  } else {
    var cur = " " + (el.getAttribute('class') || '') + " ";
    var tar = ' ' + cls + ' ';
    while (cur.indexOf(tar) >= 0) {
      cur = cur.replace(tar, ' ');
    }
    cur = cur.trim();
    if (cur) {
      el.setAttribute('class', cur);
    } else {
      el.removeAttribute('class');
    }
  }
}

/*  */

function resolveTransition (def) {
  if (!def) {
    return
  }
  /* istanbul ignore else */
  if (typeof def === 'object') {
    var res = {};
    if (def.css !== false) {
      extend(res, autoCssTransition(def.name || 'v'));
    }
    extend(res, def);
    return res
  } else if (typeof def === 'string') {
    return autoCssTransition(def)
  }
}

var autoCssTransition = cached(function (name) {
  return {
    enterClass: (name + "-enter"),
    enterToClass: (name + "-enter-to"),
    enterActiveClass: (name + "-enter-active"),
    leaveClass: (name + "-leave"),
    leaveToClass: (name + "-leave-to"),
    leaveActiveClass: (name + "-leave-active")
  }
});

var hasTransition = inBrowser && !isIE9;
var TRANSITION = 'transition';
var ANIMATION = 'animation';

// Transition property/event sniffing
var transitionProp = 'transition';
var transitionEndEvent = 'transitionend';
var animationProp = 'animation';
var animationEndEvent = 'animationend';
if (hasTransition) {
  /* istanbul ignore if */
  if (window.ontransitionend === undefined &&
    window.onwebkittransitionend !== undefined
  ) {
    transitionProp = 'WebkitTransition';
    transitionEndEvent = 'webkitTransitionEnd';
  }
  if (window.onanimationend === undefined &&
    window.onwebkitanimationend !== undefined
  ) {
    animationProp = 'WebkitAnimation';
    animationEndEvent = 'webkitAnimationEnd';
  }
}

// binding to window is necessary to make hot reload work in IE in strict mode
var raf = inBrowser
  ? window.requestAnimationFrame
    ? window.requestAnimationFrame.bind(window)
    : setTimeout
  : /* istanbul ignore next */ function (fn) { return fn(); };

function nextFrame (fn) {
  raf(function () {
    raf(fn);
  });
}

function addTransitionClass (el, cls) {
  var transitionClasses = el._transitionClasses || (el._transitionClasses = []);
  if (transitionClasses.indexOf(cls) < 0) {
    transitionClasses.push(cls);
    addClass(el, cls);
  }
}

function removeTransitionClass (el, cls) {
  if (el._transitionClasses) {
    remove(el._transitionClasses, cls);
  }
  removeClass(el, cls);
}

function whenTransitionEnds (
  el,
  expectedType,
  cb
) {
  var ref = getTransitionInfo(el, expectedType);
  var type = ref.type;
  var timeout = ref.timeout;
  var propCount = ref.propCount;
  if (!type) { return cb() }
  var event = type === TRANSITION ? transitionEndEvent : animationEndEvent;
  var ended = 0;
  var end = function () {
    el.removeEventListener(event, onEnd);
    cb();
  };
  var onEnd = function (e) {
    if (e.target === el) {
      if (++ended >= propCount) {
        end();
      }
    }
  };
  setTimeout(function () {
    if (ended < propCount) {
      end();
    }
  }, timeout + 1);
  el.addEventListener(event, onEnd);
}

var transformRE = /\b(transform|all)(,|$)/;

function getTransitionInfo (el, expectedType) {
  var styles = window.getComputedStyle(el);
  var transitionDelays = styles[transitionProp + 'Delay'].split(', ');
  var transitionDurations = styles[transitionProp + 'Duration'].split(', ');
  var transitionTimeout = getTimeout(transitionDelays, transitionDurations);
  var animationDelays = styles[animationProp + 'Delay'].split(', ');
  var animationDurations = styles[animationProp + 'Duration'].split(', ');
  var animationTimeout = getTimeout(animationDelays, animationDurations);

  var type;
  var timeout = 0;
  var propCount = 0;
  /* istanbul ignore if */
  if (expectedType === TRANSITION) {
    if (transitionTimeout > 0) {
      type = TRANSITION;
      timeout = transitionTimeout;
      propCount = transitionDurations.length;
    }
  } else if (expectedType === ANIMATION) {
    if (animationTimeout > 0) {
      type = ANIMATION;
      timeout = animationTimeout;
      propCount = animationDurations.length;
    }
  } else {
    timeout = Math.max(transitionTimeout, animationTimeout);
    type = timeout > 0
      ? transitionTimeout > animationTimeout
        ? TRANSITION
        : ANIMATION
      : null;
    propCount = type
      ? type === TRANSITION
        ? transitionDurations.length
        : animationDurations.length
      : 0;
  }
  var hasTransform =
    type === TRANSITION &&
    transformRE.test(styles[transitionProp + 'Property']);
  return {
    type: type,
    timeout: timeout,
    propCount: propCount,
    hasTransform: hasTransform
  }
}

function getTimeout (delays, durations) {
  /* istanbul ignore next */
  while (delays.length < durations.length) {
    delays = delays.concat(delays);
  }

  return Math.max.apply(null, durations.map(function (d, i) {
    return toMs(d) + toMs(delays[i])
  }))
}

function toMs (s) {
  return Number(s.slice(0, -1)) * 1000
}

/*  */

function enter (vnode, toggleDisplay) {
  var el = vnode.elm;

  // call leave callback now
  if (isDef(el._leaveCb)) {
    el._leaveCb.cancelled = true;
    el._leaveCb();
  }

  var data = resolveTransition(vnode.data.transition);
  if (isUndef(data)) {
    return
  }

  /* istanbul ignore if */
  if (isDef(el._enterCb) || el.nodeType !== 1) {
    return
  }

  var css = data.css;
  var type = data.type;
  var enterClass = data.enterClass;
  var enterToClass = data.enterToClass;
  var enterActiveClass = data.enterActiveClass;
  var appearClass = data.appearClass;
  var appearToClass = data.appearToClass;
  var appearActiveClass = data.appearActiveClass;
  var beforeEnter = data.beforeEnter;
  var enter = data.enter;
  var afterEnter = data.afterEnter;
  var enterCancelled = data.enterCancelled;
  var beforeAppear = data.beforeAppear;
  var appear = data.appear;
  var afterAppear = data.afterAppear;
  var appearCancelled = data.appearCancelled;
  var duration = data.duration;

  // activeInstance will always be the <transition> component managing this
  // transition. One edge case to check is when the <transition> is placed
  // as the root node of a child component. In that case we need to check
  // <transition>'s parent for appear check.
  var context = activeInstance;
  var transitionNode = activeInstance.$vnode;
  while (transitionNode && transitionNode.parent) {
    transitionNode = transitionNode.parent;
    context = transitionNode.context;
  }

  var isAppear = !context._isMounted || !vnode.isRootInsert;

  if (isAppear && !appear && appear !== '') {
    return
  }

  var startClass = isAppear && appearClass
    ? appearClass
    : enterClass;
  var activeClass = isAppear && appearActiveClass
    ? appearActiveClass
    : enterActiveClass;
  var toClass = isAppear && appearToClass
    ? appearToClass
    : enterToClass;

  var beforeEnterHook = isAppear
    ? (beforeAppear || beforeEnter)
    : beforeEnter;
  var enterHook = isAppear
    ? (typeof appear === 'function' ? appear : enter)
    : enter;
  var afterEnterHook = isAppear
    ? (afterAppear || afterEnter)
    : afterEnter;
  var enterCancelledHook = isAppear
    ? (appearCancelled || enterCancelled)
    : enterCancelled;

  var explicitEnterDuration = toNumber(
    isObject(duration)
      ? duration.enter
      : duration
  );

  if (false) {
    checkDuration(explicitEnterDuration, 'enter', vnode);
  }

  var expectsCSS = css !== false && !isIE9;
  var userWantsControl = getHookArgumentsLength(enterHook);

  var cb = el._enterCb = once(function () {
    if (expectsCSS) {
      removeTransitionClass(el, toClass);
      removeTransitionClass(el, activeClass);
    }
    if (cb.cancelled) {
      if (expectsCSS) {
        removeTransitionClass(el, startClass);
      }
      enterCancelledHook && enterCancelledHook(el);
    } else {
      afterEnterHook && afterEnterHook(el);
    }
    el._enterCb = null;
  });

  if (!vnode.data.show) {
    // remove pending leave element on enter by injecting an insert hook
    mergeVNodeHook(vnode, 'insert', function () {
      var parent = el.parentNode;
      var pendingNode = parent && parent._pending && parent._pending[vnode.key];
      if (pendingNode &&
        pendingNode.tag === vnode.tag &&
        pendingNode.elm._leaveCb
      ) {
        pendingNode.elm._leaveCb();
      }
      enterHook && enterHook(el, cb);
    });
  }

  // start enter transition
  beforeEnterHook && beforeEnterHook(el);
  if (expectsCSS) {
    addTransitionClass(el, startClass);
    addTransitionClass(el, activeClass);
    nextFrame(function () {
      removeTransitionClass(el, startClass);
      if (!cb.cancelled) {
        addTransitionClass(el, toClass);
        if (!userWantsControl) {
          if (isValidDuration(explicitEnterDuration)) {
            setTimeout(cb, explicitEnterDuration);
          } else {
            whenTransitionEnds(el, type, cb);
          }
        }
      }
    });
  }

  if (vnode.data.show) {
    toggleDisplay && toggleDisplay();
    enterHook && enterHook(el, cb);
  }

  if (!expectsCSS && !userWantsControl) {
    cb();
  }
}

function leave (vnode, rm) {
  var el = vnode.elm;

  // call enter callback now
  if (isDef(el._enterCb)) {
    el._enterCb.cancelled = true;
    el._enterCb();
  }

  var data = resolveTransition(vnode.data.transition);
  if (isUndef(data) || el.nodeType !== 1) {
    return rm()
  }

  /* istanbul ignore if */
  if (isDef(el._leaveCb)) {
    return
  }

  var css = data.css;
  var type = data.type;
  var leaveClass = data.leaveClass;
  var leaveToClass = data.leaveToClass;
  var leaveActiveClass = data.leaveActiveClass;
  var beforeLeave = data.beforeLeave;
  var leave = data.leave;
  var afterLeave = data.afterLeave;
  var leaveCancelled = data.leaveCancelled;
  var delayLeave = data.delayLeave;
  var duration = data.duration;

  var expectsCSS = css !== false && !isIE9;
  var userWantsControl = getHookArgumentsLength(leave);

  var explicitLeaveDuration = toNumber(
    isObject(duration)
      ? duration.leave
      : duration
  );

  if (false) {
    checkDuration(explicitLeaveDuration, 'leave', vnode);
  }

  var cb = el._leaveCb = once(function () {
    if (el.parentNode && el.parentNode._pending) {
      el.parentNode._pending[vnode.key] = null;
    }
    if (expectsCSS) {
      removeTransitionClass(el, leaveToClass);
      removeTransitionClass(el, leaveActiveClass);
    }
    if (cb.cancelled) {
      if (expectsCSS) {
        removeTransitionClass(el, leaveClass);
      }
      leaveCancelled && leaveCancelled(el);
    } else {
      rm();
      afterLeave && afterLeave(el);
    }
    el._leaveCb = null;
  });

  if (delayLeave) {
    delayLeave(performLeave);
  } else {
    performLeave();
  }

  function performLeave () {
    // the delayed leave may have already been cancelled
    if (cb.cancelled) {
      return
    }
    // record leaving element
    if (!vnode.data.show) {
      (el.parentNode._pending || (el.parentNode._pending = {}))[(vnode.key)] = vnode;
    }
    beforeLeave && beforeLeave(el);
    if (expectsCSS) {
      addTransitionClass(el, leaveClass);
      addTransitionClass(el, leaveActiveClass);
      nextFrame(function () {
        removeTransitionClass(el, leaveClass);
        if (!cb.cancelled) {
          addTransitionClass(el, leaveToClass);
          if (!userWantsControl) {
            if (isValidDuration(explicitLeaveDuration)) {
              setTimeout(cb, explicitLeaveDuration);
            } else {
              whenTransitionEnds(el, type, cb);
            }
          }
        }
      });
    }
    leave && leave(el, cb);
    if (!expectsCSS && !userWantsControl) {
      cb();
    }
  }
}

// only used in dev mode
function checkDuration (val, name, vnode) {
  if (typeof val !== 'number') {
    warn(
      "<transition> explicit " + name + " duration is not a valid number - " +
      "got " + (JSON.stringify(val)) + ".",
      vnode.context
    );
  } else if (isNaN(val)) {
    warn(
      "<transition> explicit " + name + " duration is NaN - " +
      'the duration expression might be incorrect.',
      vnode.context
    );
  }
}

function isValidDuration (val) {
  return typeof val === 'number' && !isNaN(val)
}

/**
 * Normalize a transition hook's argument length. The hook may be:
 * - a merged hook (invoker) with the original in .fns
 * - a wrapped component method (check ._length)
 * - a plain function (.length)
 */
function getHookArgumentsLength (fn) {
  if (isUndef(fn)) {
    return false
  }
  var invokerFns = fn.fns;
  if (isDef(invokerFns)) {
    // invoker
    return getHookArgumentsLength(
      Array.isArray(invokerFns)
        ? invokerFns[0]
        : invokerFns
    )
  } else {
    return (fn._length || fn.length) > 1
  }
}

function _enter (_, vnode) {
  if (vnode.data.show !== true) {
    enter(vnode);
  }
}

var transition = inBrowser ? {
  create: _enter,
  activate: _enter,
  remove: function remove$$1 (vnode, rm) {
    /* istanbul ignore else */
    if (vnode.data.show !== true) {
      leave(vnode, rm);
    } else {
      rm();
    }
  }
} : {}

var platformModules = [
  attrs,
  klass,
  events,
  domProps,
  style,
  transition
]

/*  */

// the directive module should be applied last, after all
// built-in modules have been applied.
var modules = platformModules.concat(baseModules);

var patch = createPatchFunction({ nodeOps: nodeOps, modules: modules });

/**
 * Not type checking this file because flow doesn't like attaching
 * properties to Elements.
 */

/* istanbul ignore if */
if (isIE9) {
  // http://www.matts411.com/post/internet-explorer-9-oninput/
  document.addEventListener('selectionchange', function () {
    var el = document.activeElement;
    if (el && el.vmodel) {
      trigger(el, 'input');
    }
  });
}

var directive = {
  inserted: function inserted (el, binding, vnode, oldVnode) {
    if (vnode.tag === 'select') {
      // #6903
      if (oldVnode.elm && !oldVnode.elm._vOptions) {
        mergeVNodeHook(vnode, 'postpatch', function () {
          directive.componentUpdated(el, binding, vnode);
        });
      } else {
        setSelected(el, binding, vnode.context);
      }
      el._vOptions = [].map.call(el.options, getValue);
    } else if (vnode.tag === 'textarea' || isTextInputType(el.type)) {
      el._vModifiers = binding.modifiers;
      if (!binding.modifiers.lazy) {
        el.addEventListener('compositionstart', onCompositionStart);
        el.addEventListener('compositionend', onCompositionEnd);
        // Safari < 10.2 & UIWebView doesn't fire compositionend when
        // switching focus before confirming composition choice
        // this also fixes the issue where some browsers e.g. iOS Chrome
        // fires "change" instead of "input" on autocomplete.
        el.addEventListener('change', onCompositionEnd);
        /* istanbul ignore if */
        if (isIE9) {
          el.vmodel = true;
        }
      }
    }
  },

  componentUpdated: function componentUpdated (el, binding, vnode) {
    if (vnode.tag === 'select') {
      setSelected(el, binding, vnode.context);
      // in case the options rendered by v-for have changed,
      // it's possible that the value is out-of-sync with the rendered options.
      // detect such cases and filter out values that no longer has a matching
      // option in the DOM.
      var prevOptions = el._vOptions;
      var curOptions = el._vOptions = [].map.call(el.options, getValue);
      if (curOptions.some(function (o, i) { return !looseEqual(o, prevOptions[i]); })) {
        // trigger change event if
        // no matching option found for at least one value
        var needReset = el.multiple
          ? binding.value.some(function (v) { return hasNoMatchingOption(v, curOptions); })
          : binding.value !== binding.oldValue && hasNoMatchingOption(binding.value, curOptions);
        if (needReset) {
          trigger(el, 'change');
        }
      }
    }
  }
};

function setSelected (el, binding, vm) {
  actuallySetSelected(el, binding, vm);
  /* istanbul ignore if */
  if (isIE || isEdge) {
    setTimeout(function () {
      actuallySetSelected(el, binding, vm);
    }, 0);
  }
}

function actuallySetSelected (el, binding, vm) {
  var value = binding.value;
  var isMultiple = el.multiple;
  if (isMultiple && !Array.isArray(value)) {
    "production" !== 'production' && warn(
      "<select multiple v-model=\"" + (binding.expression) + "\"> " +
      "expects an Array value for its binding, but got " + (Object.prototype.toString.call(value).slice(8, -1)),
      vm
    );
    return
  }
  var selected, option;
  for (var i = 0, l = el.options.length; i < l; i++) {
    option = el.options[i];
    if (isMultiple) {
      selected = looseIndexOf(value, getValue(option)) > -1;
      if (option.selected !== selected) {
        option.selected = selected;
      }
    } else {
      if (looseEqual(getValue(option), value)) {
        if (el.selectedIndex !== i) {
          el.selectedIndex = i;
        }
        return
      }
    }
  }
  if (!isMultiple) {
    el.selectedIndex = -1;
  }
}

function hasNoMatchingOption (value, options) {
  return options.every(function (o) { return !looseEqual(o, value); })
}

function getValue (option) {
  return '_value' in option
    ? option._value
    : option.value
}

function onCompositionStart (e) {
  e.target.composing = true;
}

function onCompositionEnd (e) {
  // prevent triggering an input event for no reason
  if (!e.target.composing) { return }
  e.target.composing = false;
  trigger(e.target, 'input');
}

function trigger (el, type) {
  var e = document.createEvent('HTMLEvents');
  e.initEvent(type, true, true);
  el.dispatchEvent(e);
}

/*  */

// recursively search for possible transition defined inside the component root
function locateNode (vnode) {
  return vnode.componentInstance && (!vnode.data || !vnode.data.transition)
    ? locateNode(vnode.componentInstance._vnode)
    : vnode
}

var show = {
  bind: function bind (el, ref, vnode) {
    var value = ref.value;

    vnode = locateNode(vnode);
    var transition$$1 = vnode.data && vnode.data.transition;
    var originalDisplay = el.__vOriginalDisplay =
      el.style.display === 'none' ? '' : el.style.display;
    if (value && transition$$1) {
      vnode.data.show = true;
      enter(vnode, function () {
        el.style.display = originalDisplay;
      });
    } else {
      el.style.display = value ? originalDisplay : 'none';
    }
  },

  update: function update (el, ref, vnode) {
    var value = ref.value;
    var oldValue = ref.oldValue;

    /* istanbul ignore if */
    if (!value === !oldValue) { return }
    vnode = locateNode(vnode);
    var transition$$1 = vnode.data && vnode.data.transition;
    if (transition$$1) {
      vnode.data.show = true;
      if (value) {
        enter(vnode, function () {
          el.style.display = el.__vOriginalDisplay;
        });
      } else {
        leave(vnode, function () {
          el.style.display = 'none';
        });
      }
    } else {
      el.style.display = value ? el.__vOriginalDisplay : 'none';
    }
  },

  unbind: function unbind (
    el,
    binding,
    vnode,
    oldVnode,
    isDestroy
  ) {
    if (!isDestroy) {
      el.style.display = el.__vOriginalDisplay;
    }
  }
}

var platformDirectives = {
  model: directive,
  show: show
}

/*  */

// Provides transition support for a single element/component.
// supports transition mode (out-in / in-out)

var transitionProps = {
  name: String,
  appear: Boolean,
  css: Boolean,
  mode: String,
  type: String,
  enterClass: String,
  leaveClass: String,
  enterToClass: String,
  leaveToClass: String,
  enterActiveClass: String,
  leaveActiveClass: String,
  appearClass: String,
  appearActiveClass: String,
  appearToClass: String,
  duration: [Number, String, Object]
};

// in case the child is also an abstract component, e.g. <keep-alive>
// we want to recursively retrieve the real component to be rendered
function getRealChild (vnode) {
  var compOptions = vnode && vnode.componentOptions;
  if (compOptions && compOptions.Ctor.options.abstract) {
    return getRealChild(getFirstComponentChild(compOptions.children))
  } else {
    return vnode
  }
}

function extractTransitionData (comp) {
  var data = {};
  var options = comp.$options;
  // props
  for (var key in options.propsData) {
    data[key] = comp[key];
  }
  // events.
  // extract listeners and pass them directly to the transition methods
  var listeners = options._parentListeners;
  for (var key$1 in listeners) {
    data[camelize(key$1)] = listeners[key$1];
  }
  return data
}

function placeholder (h, rawChild) {
  if (/\d-keep-alive$/.test(rawChild.tag)) {
    return h('keep-alive', {
      props: rawChild.componentOptions.propsData
    })
  }
}

function hasParentTransition (vnode) {
  while ((vnode = vnode.parent)) {
    if (vnode.data.transition) {
      return true
    }
  }
}

function isSameChild (child, oldChild) {
  return oldChild.key === child.key && oldChild.tag === child.tag
}

var Transition = {
  name: 'transition',
  props: transitionProps,
  abstract: true,

  render: function render (h) {
    var this$1 = this;

    var children = this.$slots.default;
    if (!children) {
      return
    }

    // filter out text nodes (possible whitespaces)
    children = children.filter(function (c) { return c.tag || isAsyncPlaceholder(c); });
    /* istanbul ignore if */
    if (!children.length) {
      return
    }

    // warn multiple elements
    if (false) {
      warn(
        '<transition> can only be used on a single element. Use ' +
        '<transition-group> for lists.',
        this.$parent
      );
    }

    var mode = this.mode;

    // warn invalid mode
    if (false
    ) {
      warn(
        'invalid <transition> mode: ' + mode,
        this.$parent
      );
    }

    var rawChild = children[0];

    // if this is a component root node and the component's
    // parent container node also has transition, skip.
    if (hasParentTransition(this.$vnode)) {
      return rawChild
    }

    // apply transition data to child
    // use getRealChild() to ignore abstract components e.g. keep-alive
    var child = getRealChild(rawChild);
    /* istanbul ignore if */
    if (!child) {
      return rawChild
    }

    if (this._leaving) {
      return placeholder(h, rawChild)
    }

    // ensure a key that is unique to the vnode type and to this transition
    // component instance. This key will be used to remove pending leaving nodes
    // during entering.
    var id = "__transition-" + (this._uid) + "-";
    child.key = child.key == null
      ? child.isComment
        ? id + 'comment'
        : id + child.tag
      : isPrimitive(child.key)
        ? (String(child.key).indexOf(id) === 0 ? child.key : id + child.key)
        : child.key;

    var data = (child.data || (child.data = {})).transition = extractTransitionData(this);
    var oldRawChild = this._vnode;
    var oldChild = getRealChild(oldRawChild);

    // mark v-show
    // so that the transition module can hand over the control to the directive
    if (child.data.directives && child.data.directives.some(function (d) { return d.name === 'show'; })) {
      child.data.show = true;
    }

    if (
      oldChild &&
      oldChild.data &&
      !isSameChild(child, oldChild) &&
      !isAsyncPlaceholder(oldChild) &&
      // #6687 component root is a comment node
      !(oldChild.componentInstance && oldChild.componentInstance._vnode.isComment)
    ) {
      // replace old child transition data with fresh one
      // important for dynamic transitions!
      var oldData = oldChild.data.transition = extend({}, data);
      // handle transition mode
      if (mode === 'out-in') {
        // return placeholder node and queue update when leave finishes
        this._leaving = true;
        mergeVNodeHook(oldData, 'afterLeave', function () {
          this$1._leaving = false;
          this$1.$forceUpdate();
        });
        return placeholder(h, rawChild)
      } else if (mode === 'in-out') {
        if (isAsyncPlaceholder(child)) {
          return oldRawChild
        }
        var delayedLeave;
        var performLeave = function () { delayedLeave(); };
        mergeVNodeHook(data, 'afterEnter', performLeave);
        mergeVNodeHook(data, 'enterCancelled', performLeave);
        mergeVNodeHook(oldData, 'delayLeave', function (leave) { delayedLeave = leave; });
      }
    }

    return rawChild
  }
}

/*  */

// Provides transition support for list items.
// supports move transitions using the FLIP technique.

// Because the vdom's children update algorithm is "unstable" - i.e.
// it doesn't guarantee the relative positioning of removed elements,
// we force transition-group to update its children into two passes:
// in the first pass, we remove all nodes that need to be removed,
// triggering their leaving transition; in the second pass, we insert/move
// into the final desired state. This way in the second pass removed
// nodes will remain where they should be.

var props = extend({
  tag: String,
  moveClass: String
}, transitionProps);

delete props.mode;

var TransitionGroup = {
  props: props,

  render: function render (h) {
    var tag = this.tag || this.$vnode.data.tag || 'span';
    var map = Object.create(null);
    var prevChildren = this.prevChildren = this.children;
    var rawChildren = this.$slots.default || [];
    var children = this.children = [];
    var transitionData = extractTransitionData(this);

    for (var i = 0; i < rawChildren.length; i++) {
      var c = rawChildren[i];
      if (c.tag) {
        if (c.key != null && String(c.key).indexOf('__vlist') !== 0) {
          children.push(c);
          map[c.key] = c
          ;(c.data || (c.data = {})).transition = transitionData;
        } else if (false) {
          var opts = c.componentOptions;
          var name = opts ? (opts.Ctor.options.name || opts.tag || '') : c.tag;
          warn(("<transition-group> children must be keyed: <" + name + ">"));
        }
      }
    }

    if (prevChildren) {
      var kept = [];
      var removed = [];
      for (var i$1 = 0; i$1 < prevChildren.length; i$1++) {
        var c$1 = prevChildren[i$1];
        c$1.data.transition = transitionData;
        c$1.data.pos = c$1.elm.getBoundingClientRect();
        if (map[c$1.key]) {
          kept.push(c$1);
        } else {
          removed.push(c$1);
        }
      }
      this.kept = h(tag, null, kept);
      this.removed = removed;
    }

    return h(tag, null, children)
  },

  beforeUpdate: function beforeUpdate () {
    // force removing pass
    this.__patch__(
      this._vnode,
      this.kept,
      false, // hydrating
      true // removeOnly (!important, avoids unnecessary moves)
    );
    this._vnode = this.kept;
  },

  updated: function updated () {
    var children = this.prevChildren;
    var moveClass = this.moveClass || ((this.name || 'v') + '-move');
    if (!children.length || !this.hasMove(children[0].elm, moveClass)) {
      return
    }

    // we divide the work into three loops to avoid mixing DOM reads and writes
    // in each iteration - which helps prevent layout thrashing.
    children.forEach(callPendingCbs);
    children.forEach(recordPosition);
    children.forEach(applyTranslation);

    // force reflow to put everything in position
    // assign to this to avoid being removed in tree-shaking
    // $flow-disable-line
    this._reflow = document.body.offsetHeight;

    children.forEach(function (c) {
      if (c.data.moved) {
        var el = c.elm;
        var s = el.style;
        addTransitionClass(el, moveClass);
        s.transform = s.WebkitTransform = s.transitionDuration = '';
        el.addEventListener(transitionEndEvent, el._moveCb = function cb (e) {
          if (!e || /transform$/.test(e.propertyName)) {
            el.removeEventListener(transitionEndEvent, cb);
            el._moveCb = null;
            removeTransitionClass(el, moveClass);
          }
        });
      }
    });
  },

  methods: {
    hasMove: function hasMove (el, moveClass) {
      /* istanbul ignore if */
      if (!hasTransition) {
        return false
      }
      /* istanbul ignore if */
      if (this._hasMove) {
        return this._hasMove
      }
      // Detect whether an element with the move class applied has
      // CSS transitions. Since the element may be inside an entering
      // transition at this very moment, we make a clone of it and remove
      // all other transition classes applied to ensure only the move class
      // is applied.
      var clone = el.cloneNode();
      if (el._transitionClasses) {
        el._transitionClasses.forEach(function (cls) { removeClass(clone, cls); });
      }
      addClass(clone, moveClass);
      clone.style.display = 'none';
      this.$el.appendChild(clone);
      var info = getTransitionInfo(clone);
      this.$el.removeChild(clone);
      return (this._hasMove = info.hasTransform)
    }
  }
}

function callPendingCbs (c) {
  /* istanbul ignore if */
  if (c.elm._moveCb) {
    c.elm._moveCb();
  }
  /* istanbul ignore if */
  if (c.elm._enterCb) {
    c.elm._enterCb();
  }
}

function recordPosition (c) {
  c.data.newPos = c.elm.getBoundingClientRect();
}

function applyTranslation (c) {
  var oldPos = c.data.pos;
  var newPos = c.data.newPos;
  var dx = oldPos.left - newPos.left;
  var dy = oldPos.top - newPos.top;
  if (dx || dy) {
    c.data.moved = true;
    var s = c.elm.style;
    s.transform = s.WebkitTransform = "translate(" + dx + "px," + dy + "px)";
    s.transitionDuration = '0s';
  }
}

var platformComponents = {
  Transition: Transition,
  TransitionGroup: TransitionGroup
}

/*  */

// install platform specific utils
Vue.config.mustUseProp = mustUseProp;
Vue.config.isReservedTag = isReservedTag;
Vue.config.isReservedAttr = isReservedAttr;
Vue.config.getTagNamespace = getTagNamespace;
Vue.config.isUnknownElement = isUnknownElement;

// install platform runtime directives & components
extend(Vue.options.directives, platformDirectives);
extend(Vue.options.components, platformComponents);

// install platform patch function
Vue.prototype.__patch__ = inBrowser ? patch : noop;

// public mount method
Vue.prototype.$mount = function (
  el,
  hydrating
) {
  el = el && inBrowser ? query(el) : undefined;
  return mountComponent(this, el, hydrating)
};

// devtools global hook
/* istanbul ignore next */
if (inBrowser) {
  setTimeout(function () {
    if (config.devtools) {
      if (devtools) {
        devtools.emit('init', Vue);
      } else if (
        false
      ) {
        console[console.info ? 'info' : 'log'](
          'Download the Vue Devtools extension for a better development experience:\n' +
          'https://github.com/vuejs/vue-devtools'
        );
      }
    }
    if (false
    ) {
      console[console.info ? 'info' : 'log'](
        "You are running Vue in development mode.\n" +
        "Make sure to turn on production mode when deploying for production.\n" +
        "See more tips at https://vuejs.org/guide/deployment.html"
      );
    }
  }, 0);
}

/*  */

var defaultTagRE = /\{\{((?:.|\n)+?)\}\}/g;
var regexEscapeRE = /[-.*+?^${}()|[\]\/\\]/g;

var buildRegex = cached(function (delimiters) {
  var open = delimiters[0].replace(regexEscapeRE, '\\$&');
  var close = delimiters[1].replace(regexEscapeRE, '\\$&');
  return new RegExp(open + '((?:.|\\n)+?)' + close, 'g')
});



function parseText (
  text,
  delimiters
) {
  var tagRE = delimiters ? buildRegex(delimiters) : defaultTagRE;
  if (!tagRE.test(text)) {
    return
  }
  var tokens = [];
  var rawTokens = [];
  var lastIndex = tagRE.lastIndex = 0;
  var match, index, tokenValue;
  while ((match = tagRE.exec(text))) {
    index = match.index;
    // push text token
    if (index > lastIndex) {
      rawTokens.push(tokenValue = text.slice(lastIndex, index));
      tokens.push(JSON.stringify(tokenValue));
    }
    // tag token
    var exp = parseFilters(match[1].trim());
    tokens.push(("_s(" + exp + ")"));
    rawTokens.push({ '@binding': exp });
    lastIndex = index + match[0].length;
  }
  if (lastIndex < text.length) {
    rawTokens.push(tokenValue = text.slice(lastIndex));
    tokens.push(JSON.stringify(tokenValue));
  }
  return {
    expression: tokens.join('+'),
    tokens: rawTokens
  }
}

/*  */

function transformNode (el, options) {
  var warn = options.warn || baseWarn;
  var staticClass = getAndRemoveAttr(el, 'class');
  if (false) {
    var res = parseText(staticClass, options.delimiters);
    if (res) {
      warn(
        "class=\"" + staticClass + "\": " +
        'Interpolation inside attributes has been removed. ' +
        'Use v-bind or the colon shorthand instead. For example, ' +
        'instead of <div class="{{ val }}">, use <div :class="val">.'
      );
    }
  }
  if (staticClass) {
    el.staticClass = JSON.stringify(staticClass);
  }
  var classBinding = getBindingAttr(el, 'class', false /* getStatic */);
  if (classBinding) {
    el.classBinding = classBinding;
  }
}

function genData (el) {
  var data = '';
  if (el.staticClass) {
    data += "staticClass:" + (el.staticClass) + ",";
  }
  if (el.classBinding) {
    data += "class:" + (el.classBinding) + ",";
  }
  return data
}

var klass$1 = {
  staticKeys: ['staticClass'],
  transformNode: transformNode,
  genData: genData
}

/*  */

function transformNode$1 (el, options) {
  var warn = options.warn || baseWarn;
  var staticStyle = getAndRemoveAttr(el, 'style');
  if (staticStyle) {
    /* istanbul ignore if */
    if (false) {
      var res = parseText(staticStyle, options.delimiters);
      if (res) {
        warn(
          "style=\"" + staticStyle + "\": " +
          'Interpolation inside attributes has been removed. ' +
          'Use v-bind or the colon shorthand instead. For example, ' +
          'instead of <div style="{{ val }}">, use <div :style="val">.'
        );
      }
    }
    el.staticStyle = JSON.stringify(parseStyleText(staticStyle));
  }

  var styleBinding = getBindingAttr(el, 'style', false /* getStatic */);
  if (styleBinding) {
    el.styleBinding = styleBinding;
  }
}

function genData$1 (el) {
  var data = '';
  if (el.staticStyle) {
    data += "staticStyle:" + (el.staticStyle) + ",";
  }
  if (el.styleBinding) {
    data += "style:(" + (el.styleBinding) + "),";
  }
  return data
}

var style$1 = {
  staticKeys: ['staticStyle'],
  transformNode: transformNode$1,
  genData: genData$1
}

/*  */

var decoder;

var he = {
  decode: function decode (html) {
    decoder = decoder || document.createElement('div');
    decoder.innerHTML = html;
    return decoder.textContent
  }
}

/*  */

var isUnaryTag = makeMap(
  'area,base,br,col,embed,frame,hr,img,input,isindex,keygen,' +
  'link,meta,param,source,track,wbr'
);

// Elements that you can, intentionally, leave open
// (and which close themselves)
var canBeLeftOpenTag = makeMap(
  'colgroup,dd,dt,li,options,p,td,tfoot,th,thead,tr,source'
);

// HTML5 tags https://html.spec.whatwg.org/multipage/indices.html#elements-3
// Phrasing Content https://html.spec.whatwg.org/multipage/dom.html#phrasing-content
var isNonPhrasingTag = makeMap(
  'address,article,aside,base,blockquote,body,caption,col,colgroup,dd,' +
  'details,dialog,div,dl,dt,fieldset,figcaption,figure,footer,form,' +
  'h1,h2,h3,h4,h5,h6,head,header,hgroup,hr,html,legend,li,menuitem,meta,' +
  'optgroup,option,param,rp,rt,source,style,summary,tbody,td,tfoot,th,thead,' +
  'title,tr,track'
);

/**
 * Not type-checking this file because it's mostly vendor code.
 */

/*!
 * HTML Parser By John Resig (ejohn.org)
 * Modified by Juriy "kangax" Zaytsev
 * Original code by Erik Arvidsson, Mozilla Public License
 * http://erik.eae.net/simplehtmlparser/simplehtmlparser.js
 */

// Regular Expressions for parsing tags and attributes
var attribute = /^\s*([^\s"'<>\/=]+)(?:\s*(=)\s*(?:"([^"]*)"+|'([^']*)'+|([^\s"'=<>`]+)))?/;
// could use https://www.w3.org/TR/1999/REC-xml-names-19990114/#NT-QName
// but for Vue templates we can enforce a simple charset
var ncname = '[a-zA-Z_][\\w\\-\\.]*';
var qnameCapture = "((?:" + ncname + "\\:)?" + ncname + ")";
var startTagOpen = new RegExp(("^<" + qnameCapture));
var startTagClose = /^\s*(\/?)>/;
var endTag = new RegExp(("^<\\/" + qnameCapture + "[^>]*>"));
var doctype = /^<!DOCTYPE [^>]+>/i;
// #7298: escape - to avoid being pased as HTML comment when inlined in page
var comment = /^<!\--/;
var conditionalComment = /^<!\[/;

var IS_REGEX_CAPTURING_BROKEN = false;
'x'.replace(/x(.)?/g, function (m, g) {
  IS_REGEX_CAPTURING_BROKEN = g === '';
});

// Special Elements (can contain anything)
var isPlainTextElement = makeMap('script,style,textarea', true);
var reCache = {};

var decodingMap = {
  '&lt;': '<',
  '&gt;': '>',
  '&quot;': '"',
  '&amp;': '&',
  '&#10;': '\n',
  '&#9;': '\t'
};
var encodedAttr = /&(?:lt|gt|quot|amp);/g;
var encodedAttrWithNewLines = /&(?:lt|gt|quot|amp|#10|#9);/g;

// #5992
var isIgnoreNewlineTag = makeMap('pre,textarea', true);
var shouldIgnoreFirstNewline = function (tag, html) { return tag && isIgnoreNewlineTag(tag) && html[0] === '\n'; };

function decodeAttr (value, shouldDecodeNewlines) {
  var re = shouldDecodeNewlines ? encodedAttrWithNewLines : encodedAttr;
  return value.replace(re, function (match) { return decodingMap[match]; })
}

function parseHTML (html, options) {
  var stack = [];
  var expectHTML = options.expectHTML;
  var isUnaryTag$$1 = options.isUnaryTag || no;
  var canBeLeftOpenTag$$1 = options.canBeLeftOpenTag || no;
  var index = 0;
  var last, lastTag;
  while (html) {
    last = html;
    // Make sure we're not in a plaintext content element like script/style
    if (!lastTag || !isPlainTextElement(lastTag)) {
      var textEnd = html.indexOf('<');
      if (textEnd === 0) {
        // Comment:
        if (comment.test(html)) {
          var commentEnd = html.indexOf('-->');

          if (commentEnd >= 0) {
            if (options.shouldKeepComment) {
              options.comment(html.substring(4, commentEnd));
            }
            advance(commentEnd + 3);
            continue
          }
        }

        // http://en.wikipedia.org/wiki/Conditional_comment#Downlevel-revealed_conditional_comment
        if (conditionalComment.test(html)) {
          var conditionalEnd = html.indexOf(']>');

          if (conditionalEnd >= 0) {
            advance(conditionalEnd + 2);
            continue
          }
        }

        // Doctype:
        var doctypeMatch = html.match(doctype);
        if (doctypeMatch) {
          advance(doctypeMatch[0].length);
          continue
        }

        // End tag:
        var endTagMatch = html.match(endTag);
        if (endTagMatch) {
          var curIndex = index;
          advance(endTagMatch[0].length);
          parseEndTag(endTagMatch[1], curIndex, index);
          continue
        }

        // Start tag:
        var startTagMatch = parseStartTag();
        if (startTagMatch) {
          handleStartTag(startTagMatch);
          if (shouldIgnoreFirstNewline(lastTag, html)) {
            advance(1);
          }
          continue
        }
      }

      var text = (void 0), rest = (void 0), next = (void 0);
      if (textEnd >= 0) {
        rest = html.slice(textEnd);
        while (
          !endTag.test(rest) &&
          !startTagOpen.test(rest) &&
          !comment.test(rest) &&
          !conditionalComment.test(rest)
        ) {
          // < in plain text, be forgiving and treat it as text
          next = rest.indexOf('<', 1);
          if (next < 0) { break }
          textEnd += next;
          rest = html.slice(textEnd);
        }
        text = html.substring(0, textEnd);
        advance(textEnd);
      }

      if (textEnd < 0) {
        text = html;
        html = '';
      }

      if (options.chars && text) {
        options.chars(text);
      }
    } else {
      var endTagLength = 0;
      var stackedTag = lastTag.toLowerCase();
      var reStackedTag = reCache[stackedTag] || (reCache[stackedTag] = new RegExp('([\\s\\S]*?)(</' + stackedTag + '[^>]*>)', 'i'));
      var rest$1 = html.replace(reStackedTag, function (all, text, endTag) {
        endTagLength = endTag.length;
        if (!isPlainTextElement(stackedTag) && stackedTag !== 'noscript') {
          text = text
            .replace(/<!\--([\s\S]*?)-->/g, '$1') // #7298
            .replace(/<!\[CDATA\[([\s\S]*?)]]>/g, '$1');
        }
        if (shouldIgnoreFirstNewline(stackedTag, text)) {
          text = text.slice(1);
        }
        if (options.chars) {
          options.chars(text);
        }
        return ''
      });
      index += html.length - rest$1.length;
      html = rest$1;
      parseEndTag(stackedTag, index - endTagLength, index);
    }

    if (html === last) {
      options.chars && options.chars(html);
      if (false) {
        options.warn(("Mal-formatted tag at end of template: \"" + html + "\""));
      }
      break
    }
  }

  // Clean up any remaining tags
  parseEndTag();

  function advance (n) {
    index += n;
    html = html.substring(n);
  }

  function parseStartTag () {
    var start = html.match(startTagOpen);
    if (start) {
      var match = {
        tagName: start[1],
        attrs: [],
        start: index
      };
      advance(start[0].length);
      var end, attr;
      while (!(end = html.match(startTagClose)) && (attr = html.match(attribute))) {
        advance(attr[0].length);
        match.attrs.push(attr);
      }
      if (end) {
        match.unarySlash = end[1];
        advance(end[0].length);
        match.end = index;
        return match
      }
    }
  }

  function handleStartTag (match) {
    var tagName = match.tagName;
    var unarySlash = match.unarySlash;

    if (expectHTML) {
      if (lastTag === 'p' && isNonPhrasingTag(tagName)) {
        parseEndTag(lastTag);
      }
      if (canBeLeftOpenTag$$1(tagName) && lastTag === tagName) {
        parseEndTag(tagName);
      }
    }

    var unary = isUnaryTag$$1(tagName) || !!unarySlash;

    var l = match.attrs.length;
    var attrs = new Array(l);
    for (var i = 0; i < l; i++) {
      var args = match.attrs[i];
      // hackish work around FF bug https://bugzilla.mozilla.org/show_bug.cgi?id=369778
      if (IS_REGEX_CAPTURING_BROKEN && args[0].indexOf('""') === -1) {
        if (args[3] === '') { delete args[3]; }
        if (args[4] === '') { delete args[4]; }
        if (args[5] === '') { delete args[5]; }
      }
      var value = args[3] || args[4] || args[5] || '';
      var shouldDecodeNewlines = tagName === 'a' && args[1] === 'href'
        ? options.shouldDecodeNewlinesForHref
        : options.shouldDecodeNewlines;
      attrs[i] = {
        name: args[1],
        value: decodeAttr(value, shouldDecodeNewlines)
      };
    }

    if (!unary) {
      stack.push({ tag: tagName, lowerCasedTag: tagName.toLowerCase(), attrs: attrs });
      lastTag = tagName;
    }

    if (options.start) {
      options.start(tagName, attrs, unary, match.start, match.end);
    }
  }

  function parseEndTag (tagName, start, end) {
    var pos, lowerCasedTagName;
    if (start == null) { start = index; }
    if (end == null) { end = index; }

    if (tagName) {
      lowerCasedTagName = tagName.toLowerCase();
    }

    // Find the closest opened tag of the same type
    if (tagName) {
      for (pos = stack.length - 1; pos >= 0; pos--) {
        if (stack[pos].lowerCasedTag === lowerCasedTagName) {
          break
        }
      }
    } else {
      // If no tag name is provided, clean shop
      pos = 0;
    }

    if (pos >= 0) {
      // Close all the open elements, up the stack
      for (var i = stack.length - 1; i >= pos; i--) {
        if (false
        ) {
          options.warn(
            ("tag <" + (stack[i].tag) + "> has no matching end tag.")
          );
        }
        if (options.end) {
          options.end(stack[i].tag, start, end);
        }
      }

      // Remove the open elements from the stack
      stack.length = pos;
      lastTag = pos && stack[pos - 1].tag;
    } else if (lowerCasedTagName === 'br') {
      if (options.start) {
        options.start(tagName, [], true, start, end);
      }
    } else if (lowerCasedTagName === 'p') {
      if (options.start) {
        options.start(tagName, [], false, start, end);
      }
      if (options.end) {
        options.end(tagName, start, end);
      }
    }
  }
}

/*  */

var onRE = /^@|^v-on:/;
var dirRE = /^v-|^@|^:/;
var forAliasRE = /([^]*?)\s+(?:in|of)\s+([^]*)/;
var forIteratorRE = /,([^,\}\]]*)(?:,([^,\}\]]*))?$/;
var stripParensRE = /^\(|\)$/g;

var argRE = /:(.*)$/;
var bindRE = /^:|^v-bind:/;
var modifierRE = /\.[^.]+/g;

var decodeHTMLCached = cached(he.decode);

// configurable state
var warn$2;
var delimiters;
var transforms;
var preTransforms;
var postTransforms;
var platformIsPreTag;
var platformMustUseProp;
var platformGetTagNamespace;



function createASTElement (
  tag,
  attrs,
  parent
) {
  return {
    type: 1,
    tag: tag,
    attrsList: attrs,
    attrsMap: makeAttrsMap(attrs),
    parent: parent,
    children: []
  }
}

/**
 * Convert HTML string to AST.
 */
function parse (
  template,
  options
) {
  warn$2 = options.warn || baseWarn;

  platformIsPreTag = options.isPreTag || no;
  platformMustUseProp = options.mustUseProp || no;
  platformGetTagNamespace = options.getTagNamespace || no;

  transforms = pluckModuleFunction(options.modules, 'transformNode');
  preTransforms = pluckModuleFunction(options.modules, 'preTransformNode');
  postTransforms = pluckModuleFunction(options.modules, 'postTransformNode');

  delimiters = options.delimiters;

  var stack = [];
  var preserveWhitespace = options.preserveWhitespace !== false;
  var root;
  var currentParent;
  var inVPre = false;
  var inPre = false;
  var warned = false;

  function warnOnce (msg) {
    if (!warned) {
      warned = true;
      warn$2(msg);
    }
  }

  function closeElement (element) {
    // check pre state
    if (element.pre) {
      inVPre = false;
    }
    if (platformIsPreTag(element.tag)) {
      inPre = false;
    }
    // apply post-transforms
    for (var i = 0; i < postTransforms.length; i++) {
      postTransforms[i](element, options);
    }
  }

  parseHTML(template, {
    warn: warn$2,
    expectHTML: options.expectHTML,
    isUnaryTag: options.isUnaryTag,
    canBeLeftOpenTag: options.canBeLeftOpenTag,
    shouldDecodeNewlines: options.shouldDecodeNewlines,
    shouldDecodeNewlinesForHref: options.shouldDecodeNewlinesForHref,
    shouldKeepComment: options.comments,
    start: function start (tag, attrs, unary) {
      // check namespace.
      // inherit parent ns if there is one
      var ns = (currentParent && currentParent.ns) || platformGetTagNamespace(tag);

      // handle IE svg bug
      /* istanbul ignore if */
      if (isIE && ns === 'svg') {
        attrs = guardIESVGBug(attrs);
      }

      var element = createASTElement(tag, attrs, currentParent);
      if (ns) {
        element.ns = ns;
      }

      if (isForbiddenTag(element) && !isServerRendering()) {
        element.forbidden = true;
        "production" !== 'production' && warn$2(
          'Templates should only be responsible for mapping the state to the ' +
          'UI. Avoid placing tags with side-effects in your templates, such as ' +
          "<" + tag + ">" + ', as they will not be parsed.'
        );
      }

      // apply pre-transforms
      for (var i = 0; i < preTransforms.length; i++) {
        element = preTransforms[i](element, options) || element;
      }

      if (!inVPre) {
        processPre(element);
        if (element.pre) {
          inVPre = true;
        }
      }
      if (platformIsPreTag(element.tag)) {
        inPre = true;
      }
      if (inVPre) {
        processRawAttrs(element);
      } else if (!element.processed) {
        // structural directives
        processFor(element);
        processIf(element);
        processOnce(element);
        // element-scope stuff
        processElement(element, options);
      }

      function checkRootConstraints (el) {
        if (false) {
          if (el.tag === 'slot' || el.tag === 'template') {
            warnOnce(
              "Cannot use <" + (el.tag) + "> as component root element because it may " +
              'contain multiple nodes.'
            );
          }
          if (el.attrsMap.hasOwnProperty('v-for')) {
            warnOnce(
              'Cannot use v-for on stateful component root element because ' +
              'it renders multiple elements.'
            );
          }
        }
      }

      // tree management
      if (!root) {
        root = element;
        checkRootConstraints(root);
      } else if (!stack.length) {
        // allow root elements with v-if, v-else-if and v-else
        if (root.if && (element.elseif || element.else)) {
          checkRootConstraints(element);
          addIfCondition(root, {
            exp: element.elseif,
            block: element
          });
        } else if (false) {
          warnOnce(
            "Component template should contain exactly one root element. " +
            "If you are using v-if on multiple elements, " +
            "use v-else-if to chain them instead."
          );
        }
      }
      if (currentParent && !element.forbidden) {
        if (element.elseif || element.else) {
          processIfConditions(element, currentParent);
        } else if (element.slotScope) { // scoped slot
          currentParent.plain = false;
          var name = element.slotTarget || '"default"';(currentParent.scopedSlots || (currentParent.scopedSlots = {}))[name] = element;
        } else {
          currentParent.children.push(element);
          element.parent = currentParent;
        }
      }
      if (!unary) {
        currentParent = element;
        stack.push(element);
      } else {
        closeElement(element);
      }
    },

    end: function end () {
      // remove trailing whitespace
      var element = stack[stack.length - 1];
      var lastNode = element.children[element.children.length - 1];
      if (lastNode && lastNode.type === 3 && lastNode.text === ' ' && !inPre) {
        element.children.pop();
      }
      // pop stack
      stack.length -= 1;
      currentParent = stack[stack.length - 1];
      closeElement(element);
    },

    chars: function chars (text) {
      if (!currentParent) {
        if (false) {
          if (text === template) {
            warnOnce(
              'Component template requires a root element, rather than just text.'
            );
          } else if ((text = text.trim())) {
            warnOnce(
              ("text \"" + text + "\" outside root element will be ignored.")
            );
          }
        }
        return
      }
      // IE textarea placeholder bug
      /* istanbul ignore if */
      if (isIE &&
        currentParent.tag === 'textarea' &&
        currentParent.attrsMap.placeholder === text
      ) {
        return
      }
      var children = currentParent.children;
      text = inPre || text.trim()
        ? isTextTag(currentParent) ? text : decodeHTMLCached(text)
        // only preserve whitespace if its not right after a starting tag
        : preserveWhitespace && children.length ? ' ' : '';
      if (text) {
        var res;
        if (!inVPre && text !== ' ' && (res = parseText(text, delimiters))) {
          children.push({
            type: 2,
            expression: res.expression,
            tokens: res.tokens,
            text: text
          });
        } else if (text !== ' ' || !children.length || children[children.length - 1].text !== ' ') {
          children.push({
            type: 3,
            text: text
          });
        }
      }
    },
    comment: function comment (text) {
      currentParent.children.push({
        type: 3,
        text: text,
        isComment: true
      });
    }
  });
  return root
}

function processPre (el) {
  if (getAndRemoveAttr(el, 'v-pre') != null) {
    el.pre = true;
  }
}

function processRawAttrs (el) {
  var l = el.attrsList.length;
  if (l) {
    var attrs = el.attrs = new Array(l);
    for (var i = 0; i < l; i++) {
      attrs[i] = {
        name: el.attrsList[i].name,
        value: JSON.stringify(el.attrsList[i].value)
      };
    }
  } else if (!el.pre) {
    // non root node in pre blocks with no attributes
    el.plain = true;
  }
}

function processElement (element, options) {
  processKey(element);

  // determine whether this is a plain element after
  // removing structural attributes
  element.plain = !element.key && !element.attrsList.length;

  processRef(element);
  processSlot(element);
  processComponent(element);
  for (var i = 0; i < transforms.length; i++) {
    element = transforms[i](element, options) || element;
  }
  processAttrs(element);
}

function processKey (el) {
  var exp = getBindingAttr(el, 'key');
  if (exp) {
    if (false) {
      warn$2("<template> cannot be keyed. Place the key on real elements instead.");
    }
    el.key = exp;
  }
}

function processRef (el) {
  var ref = getBindingAttr(el, 'ref');
  if (ref) {
    el.ref = ref;
    el.refInFor = checkInFor(el);
  }
}

function processFor (el) {
  var exp;
  if ((exp = getAndRemoveAttr(el, 'v-for'))) {
    var res = parseFor(exp);
    if (res) {
      extend(el, res);
    } else if (false) {
      warn$2(
        ("Invalid v-for expression: " + exp)
      );
    }
  }
}



function parseFor (exp) {
  var inMatch = exp.match(forAliasRE);
  if (!inMatch) { return }
  var res = {};
  res.for = inMatch[2].trim();
  var alias = inMatch[1].trim().replace(stripParensRE, '');
  var iteratorMatch = alias.match(forIteratorRE);
  if (iteratorMatch) {
    res.alias = alias.replace(forIteratorRE, '');
    res.iterator1 = iteratorMatch[1].trim();
    if (iteratorMatch[2]) {
      res.iterator2 = iteratorMatch[2].trim();
    }
  } else {
    res.alias = alias;
  }
  return res
}

function processIf (el) {
  var exp = getAndRemoveAttr(el, 'v-if');
  if (exp) {
    el.if = exp;
    addIfCondition(el, {
      exp: exp,
      block: el
    });
  } else {
    if (getAndRemoveAttr(el, 'v-else') != null) {
      el.else = true;
    }
    var elseif = getAndRemoveAttr(el, 'v-else-if');
    if (elseif) {
      el.elseif = elseif;
    }
  }
}

function processIfConditions (el, parent) {
  var prev = findPrevElement(parent.children);
  if (prev && prev.if) {
    addIfCondition(prev, {
      exp: el.elseif,
      block: el
    });
  } else if (false) {
    warn$2(
      "v-" + (el.elseif ? ('else-if="' + el.elseif + '"') : 'else') + " " +
      "used on element <" + (el.tag) + "> without corresponding v-if."
    );
  }
}

function findPrevElement (children) {
  var i = children.length;
  while (i--) {
    if (children[i].type === 1) {
      return children[i]
    } else {
      if (false) {
        warn$2(
          "text \"" + (children[i].text.trim()) + "\" between v-if and v-else(-if) " +
          "will be ignored."
        );
      }
      children.pop();
    }
  }
}

function addIfCondition (el, condition) {
  if (!el.ifConditions) {
    el.ifConditions = [];
  }
  el.ifConditions.push(condition);
}

function processOnce (el) {
  var once$$1 = getAndRemoveAttr(el, 'v-once');
  if (once$$1 != null) {
    el.once = true;
  }
}

function processSlot (el) {
  if (el.tag === 'slot') {
    el.slotName = getBindingAttr(el, 'name');
    if (false) {
      warn$2(
        "`key` does not work on <slot> because slots are abstract outlets " +
        "and can possibly expand into multiple elements. " +
        "Use the key on a wrapping element instead."
      );
    }
  } else {
    var slotScope;
    if (el.tag === 'template') {
      slotScope = getAndRemoveAttr(el, 'scope');
      /* istanbul ignore if */
      if (false) {
        warn$2(
          "the \"scope\" attribute for scoped slots have been deprecated and " +
          "replaced by \"slot-scope\" since 2.5. The new \"slot-scope\" attribute " +
          "can also be used on plain elements in addition to <template> to " +
          "denote scoped slots.",
          true
        );
      }
      el.slotScope = slotScope || getAndRemoveAttr(el, 'slot-scope');
    } else if ((slotScope = getAndRemoveAttr(el, 'slot-scope'))) {
      /* istanbul ignore if */
      if (false) {
        warn$2(
          "Ambiguous combined usage of slot-scope and v-for on <" + (el.tag) + "> " +
          "(v-for takes higher priority). Use a wrapper <template> for the " +
          "scoped slot to make it clearer.",
          true
        );
      }
      el.slotScope = slotScope;
    }
    var slotTarget = getBindingAttr(el, 'slot');
    if (slotTarget) {
      el.slotTarget = slotTarget === '""' ? '"default"' : slotTarget;
      // preserve slot as an attribute for native shadow DOM compat
      // only for non-scoped slots.
      if (el.tag !== 'template' && !el.slotScope) {
        addAttr(el, 'slot', slotTarget);
      }
    }
  }
}

function processComponent (el) {
  var binding;
  if ((binding = getBindingAttr(el, 'is'))) {
    el.component = binding;
  }
  if (getAndRemoveAttr(el, 'inline-template') != null) {
    el.inlineTemplate = true;
  }
}

function processAttrs (el) {
  var list = el.attrsList;
  var i, l, name, rawName, value, modifiers, isProp;
  for (i = 0, l = list.length; i < l; i++) {
    name = rawName = list[i].name;
    value = list[i].value;
    if (dirRE.test(name)) {
      // mark element as dynamic
      el.hasBindings = true;
      // modifiers
      modifiers = parseModifiers(name);
      if (modifiers) {
        name = name.replace(modifierRE, '');
      }
      if (bindRE.test(name)) { // v-bind
        name = name.replace(bindRE, '');
        value = parseFilters(value);
        isProp = false;
        if (modifiers) {
          if (modifiers.prop) {
            isProp = true;
            name = camelize(name);
            if (name === 'innerHtml') { name = 'innerHTML'; }
          }
          if (modifiers.camel) {
            name = camelize(name);
          }
          if (modifiers.sync) {
            addHandler(
              el,
              ("update:" + (camelize(name))),
              genAssignmentCode(value, "$event")
            );
          }
        }
        if (isProp || (
          !el.component && platformMustUseProp(el.tag, el.attrsMap.type, name)
        )) {
          addProp(el, name, value);
        } else {
          addAttr(el, name, value);
        }
      } else if (onRE.test(name)) { // v-on
        name = name.replace(onRE, '');
        addHandler(el, name, value, modifiers, false, warn$2);
      } else { // normal directives
        name = name.replace(dirRE, '');
        // parse arg
        var argMatch = name.match(argRE);
        var arg = argMatch && argMatch[1];
        if (arg) {
          name = name.slice(0, -(arg.length + 1));
        }
        addDirective(el, name, rawName, value, arg, modifiers);
        if (false) {
          checkForAliasModel(el, value);
        }
      }
    } else {
      // literal attribute
      if (false) {
        var res = parseText(value, delimiters);
        if (res) {
          warn$2(
            name + "=\"" + value + "\": " +
            'Interpolation inside attributes has been removed. ' +
            'Use v-bind or the colon shorthand instead. For example, ' +
            'instead of <div id="{{ val }}">, use <div :id="val">.'
          );
        }
      }
      addAttr(el, name, JSON.stringify(value));
      // #6887 firefox doesn't update muted state if set via attribute
      // even immediately after element creation
      if (!el.component &&
          name === 'muted' &&
          platformMustUseProp(el.tag, el.attrsMap.type, name)) {
        addProp(el, name, 'true');
      }
    }
  }
}

function checkInFor (el) {
  var parent = el;
  while (parent) {
    if (parent.for !== undefined) {
      return true
    }
    parent = parent.parent;
  }
  return false
}

function parseModifiers (name) {
  var match = name.match(modifierRE);
  if (match) {
    var ret = {};
    match.forEach(function (m) { ret[m.slice(1)] = true; });
    return ret
  }
}

function makeAttrsMap (attrs) {
  var map = {};
  for (var i = 0, l = attrs.length; i < l; i++) {
    if (
      false
    ) {
      warn$2('duplicate attribute: ' + attrs[i].name);
    }
    map[attrs[i].name] = attrs[i].value;
  }
  return map
}

// for script (e.g. type="x/template") or style, do not decode content
function isTextTag (el) {
  return el.tag === 'script' || el.tag === 'style'
}

function isForbiddenTag (el) {
  return (
    el.tag === 'style' ||
    (el.tag === 'script' && (
      !el.attrsMap.type ||
      el.attrsMap.type === 'text/javascript'
    ))
  )
}

var ieNSBug = /^xmlns:NS\d+/;
var ieNSPrefix = /^NS\d+:/;

/* istanbul ignore next */
function guardIESVGBug (attrs) {
  var res = [];
  for (var i = 0; i < attrs.length; i++) {
    var attr = attrs[i];
    if (!ieNSBug.test(attr.name)) {
      attr.name = attr.name.replace(ieNSPrefix, '');
      res.push(attr);
    }
  }
  return res
}

function checkForAliasModel (el, value) {
  var _el = el;
  while (_el) {
    if (_el.for && _el.alias === value) {
      warn$2(
        "<" + (el.tag) + " v-model=\"" + value + "\">: " +
        "You are binding v-model directly to a v-for iteration alias. " +
        "This will not be able to modify the v-for source array because " +
        "writing to the alias is like modifying a function local variable. " +
        "Consider using an array of objects and use v-model on an object property instead."
      );
    }
    _el = _el.parent;
  }
}

/*  */

/**
 * Expand input[v-model] with dyanmic type bindings into v-if-else chains
 * Turn this:
 *   <input v-model="data[type]" :type="type">
 * into this:
 *   <input v-if="type === 'checkbox'" type="checkbox" v-model="data[type]">
 *   <input v-else-if="type === 'radio'" type="radio" v-model="data[type]">
 *   <input v-else :type="type" v-model="data[type]">
 */

function preTransformNode (el, options) {
  if (el.tag === 'input') {
    var map = el.attrsMap;
    if (!map['v-model']) {
      return
    }

    var typeBinding;
    if (map[':type'] || map['v-bind:type']) {
      typeBinding = getBindingAttr(el, 'type');
    }
    if (!map.type && !typeBinding && map['v-bind']) {
      typeBinding = "(" + (map['v-bind']) + ").type";
    }

    if (typeBinding) {
      var ifCondition = getAndRemoveAttr(el, 'v-if', true);
      var ifConditionExtra = ifCondition ? ("&&(" + ifCondition + ")") : "";
      var hasElse = getAndRemoveAttr(el, 'v-else', true) != null;
      var elseIfCondition = getAndRemoveAttr(el, 'v-else-if', true);
      // 1. checkbox
      var branch0 = cloneASTElement(el);
      // process for on the main node
      processFor(branch0);
      addRawAttr(branch0, 'type', 'checkbox');
      processElement(branch0, options);
      branch0.processed = true; // prevent it from double-processed
      branch0.if = "(" + typeBinding + ")==='checkbox'" + ifConditionExtra;
      addIfCondition(branch0, {
        exp: branch0.if,
        block: branch0
      });
      // 2. add radio else-if condition
      var branch1 = cloneASTElement(el);
      getAndRemoveAttr(branch1, 'v-for', true);
      addRawAttr(branch1, 'type', 'radio');
      processElement(branch1, options);
      addIfCondition(branch0, {
        exp: "(" + typeBinding + ")==='radio'" + ifConditionExtra,
        block: branch1
      });
      // 3. other
      var branch2 = cloneASTElement(el);
      getAndRemoveAttr(branch2, 'v-for', true);
      addRawAttr(branch2, ':type', typeBinding);
      processElement(branch2, options);
      addIfCondition(branch0, {
        exp: ifCondition,
        block: branch2
      });

      if (hasElse) {
        branch0.else = true;
      } else if (elseIfCondition) {
        branch0.elseif = elseIfCondition;
      }

      return branch0
    }
  }
}

function cloneASTElement (el) {
  return createASTElement(el.tag, el.attrsList.slice(), el.parent)
}

var model$2 = {
  preTransformNode: preTransformNode
}

var modules$1 = [
  klass$1,
  style$1,
  model$2
]

/*  */

function text (el, dir) {
  if (dir.value) {
    addProp(el, 'textContent', ("_s(" + (dir.value) + ")"));
  }
}

/*  */

function html (el, dir) {
  if (dir.value) {
    addProp(el, 'innerHTML', ("_s(" + (dir.value) + ")"));
  }
}

var directives$1 = {
  model: model,
  text: text,
  html: html
}

/*  */

var baseOptions = {
  expectHTML: true,
  modules: modules$1,
  directives: directives$1,
  isPreTag: isPreTag,
  isUnaryTag: isUnaryTag,
  mustUseProp: mustUseProp,
  canBeLeftOpenTag: canBeLeftOpenTag,
  isReservedTag: isReservedTag,
  getTagNamespace: getTagNamespace,
  staticKeys: genStaticKeys(modules$1)
};

/*  */

var isStaticKey;
var isPlatformReservedTag;

var genStaticKeysCached = cached(genStaticKeys$1);

/**
 * Goal of the optimizer: walk the generated template AST tree
 * and detect sub-trees that are purely static, i.e. parts of
 * the DOM that never needs to change.
 *
 * Once we detect these sub-trees, we can:
 *
 * 1. Hoist them into constants, so that we no longer need to
 *    create fresh nodes for them on each re-render;
 * 2. Completely skip them in the patching process.
 */
function optimize (root, options) {
  if (!root) { return }
  isStaticKey = genStaticKeysCached(options.staticKeys || '');
  isPlatformReservedTag = options.isReservedTag || no;
  // first pass: mark all non-static nodes.
  markStatic$1(root);
  // second pass: mark static roots.
  markStaticRoots(root, false);
}

function genStaticKeys$1 (keys) {
  return makeMap(
    'type,tag,attrsList,attrsMap,plain,parent,children,attrs' +
    (keys ? ',' + keys : '')
  )
}

function markStatic$1 (node) {
  node.static = isStatic(node);
  if (node.type === 1) {
    // do not make component slot content static. this avoids
    // 1. components not able to mutate slot nodes
    // 2. static slot content fails for hot-reloading
    if (
      !isPlatformReservedTag(node.tag) &&
      node.tag !== 'slot' &&
      node.attrsMap['inline-template'] == null
    ) {
      return
    }
    for (var i = 0, l = node.children.length; i < l; i++) {
      var child = node.children[i];
      markStatic$1(child);
      if (!child.static) {
        node.static = false;
      }
    }
    if (node.ifConditions) {
      for (var i$1 = 1, l$1 = node.ifConditions.length; i$1 < l$1; i$1++) {
        var block = node.ifConditions[i$1].block;
        markStatic$1(block);
        if (!block.static) {
          node.static = false;
        }
      }
    }
  }
}

function markStaticRoots (node, isInFor) {
  if (node.type === 1) {
    if (node.static || node.once) {
      node.staticInFor = isInFor;
    }
    // For a node to qualify as a static root, it should have children that
    // are not just static text. Otherwise the cost of hoisting out will
    // outweigh the benefits and it's better off to just always render it fresh.
    if (node.static && node.children.length && !(
      node.children.length === 1 &&
      node.children[0].type === 3
    )) {
      node.staticRoot = true;
      return
    } else {
      node.staticRoot = false;
    }
    if (node.children) {
      for (var i = 0, l = node.children.length; i < l; i++) {
        markStaticRoots(node.children[i], isInFor || !!node.for);
      }
    }
    if (node.ifConditions) {
      for (var i$1 = 1, l$1 = node.ifConditions.length; i$1 < l$1; i$1++) {
        markStaticRoots(node.ifConditions[i$1].block, isInFor);
      }
    }
  }
}

function isStatic (node) {
  if (node.type === 2) { // expression
    return false
  }
  if (node.type === 3) { // text
    return true
  }
  return !!(node.pre || (
    !node.hasBindings && // no dynamic bindings
    !node.if && !node.for && // not v-if or v-for or v-else
    !isBuiltInTag(node.tag) && // not a built-in
    isPlatformReservedTag(node.tag) && // not a component
    !isDirectChildOfTemplateFor(node) &&
    Object.keys(node).every(isStaticKey)
  ))
}

function isDirectChildOfTemplateFor (node) {
  while (node.parent) {
    node = node.parent;
    if (node.tag !== 'template') {
      return false
    }
    if (node.for) {
      return true
    }
  }
  return false
}

/*  */

var fnExpRE = /^([\w$_]+|\([^)]*?\))\s*=>|^function\s*\(/;
var simplePathRE = /^[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*|\['[^']*?']|\["[^"]*?"]|\[\d+]|\[[A-Za-z_$][\w$]*])*$/;

// KeyboardEvent.keyCode aliases
var keyCodes = {
  esc: 27,
  tab: 9,
  enter: 13,
  space: 32,
  up: 38,
  left: 37,
  right: 39,
  down: 40,
  'delete': [8, 46]
};

// KeyboardEvent.key aliases
var keyNames = {
  esc: 'Escape',
  tab: 'Tab',
  enter: 'Enter',
  space: ' ',
  // #7806: IE11 uses key names without `Arrow` prefix for arrow keys.
  up: ['Up', 'ArrowUp'],
  left: ['Left', 'ArrowLeft'],
  right: ['Right', 'ArrowRight'],
  down: ['Down', 'ArrowDown'],
  'delete': ['Backspace', 'Delete']
};

// #4868: modifiers that prevent the execution of the listener
// need to explicitly return null so that we can determine whether to remove
// the listener for .once
var genGuard = function (condition) { return ("if(" + condition + ")return null;"); };

var modifierCode = {
  stop: '$event.stopPropagation();',
  prevent: '$event.preventDefault();',
  self: genGuard("$event.target !== $event.currentTarget"),
  ctrl: genGuard("!$event.ctrlKey"),
  shift: genGuard("!$event.shiftKey"),
  alt: genGuard("!$event.altKey"),
  meta: genGuard("!$event.metaKey"),
  left: genGuard("'button' in $event && $event.button !== 0"),
  middle: genGuard("'button' in $event && $event.button !== 1"),
  right: genGuard("'button' in $event && $event.button !== 2")
};

function genHandlers (
  events,
  isNative,
  warn
) {
  var res = isNative ? 'nativeOn:{' : 'on:{';
  for (var name in events) {
    res += "\"" + name + "\":" + (genHandler(name, events[name])) + ",";
  }
  return res.slice(0, -1) + '}'
}

function genHandler (
  name,
  handler
) {
  if (!handler) {
    return 'function(){}'
  }

  if (Array.isArray(handler)) {
    return ("[" + (handler.map(function (handler) { return genHandler(name, handler); }).join(',')) + "]")
  }

  var isMethodPath = simplePathRE.test(handler.value);
  var isFunctionExpression = fnExpRE.test(handler.value);

  if (!handler.modifiers) {
    if (isMethodPath || isFunctionExpression) {
      return handler.value
    }
    /* istanbul ignore if */
    return ("function($event){" + (handler.value) + "}") // inline statement
  } else {
    var code = '';
    var genModifierCode = '';
    var keys = [];
    for (var key in handler.modifiers) {
      if (modifierCode[key]) {
        genModifierCode += modifierCode[key];
        // left/right
        if (keyCodes[key]) {
          keys.push(key);
        }
      } else if (key === 'exact') {
        var modifiers = (handler.modifiers);
        genModifierCode += genGuard(
          ['ctrl', 'shift', 'alt', 'meta']
            .filter(function (keyModifier) { return !modifiers[keyModifier]; })
            .map(function (keyModifier) { return ("$event." + keyModifier + "Key"); })
            .join('||')
        );
      } else {
        keys.push(key);
      }
    }
    if (keys.length) {
      code += genKeyFilter(keys);
    }
    // Make sure modifiers like prevent and stop get executed after key filtering
    if (genModifierCode) {
      code += genModifierCode;
    }
    var handlerCode = isMethodPath
      ? ("return " + (handler.value) + "($event)")
      : isFunctionExpression
        ? ("return (" + (handler.value) + ")($event)")
        : handler.value;
    /* istanbul ignore if */
    return ("function($event){" + code + handlerCode + "}")
  }
}

function genKeyFilter (keys) {
  return ("if(!('button' in $event)&&" + (keys.map(genFilterCode).join('&&')) + ")return null;")
}

function genFilterCode (key) {
  var keyVal = parseInt(key, 10);
  if (keyVal) {
    return ("$event.keyCode!==" + keyVal)
  }
  var keyCode = keyCodes[key];
  var keyName = keyNames[key];
  return (
    "_k($event.keyCode," +
    (JSON.stringify(key)) + "," +
    (JSON.stringify(keyCode)) + "," +
    "$event.key," +
    "" + (JSON.stringify(keyName)) +
    ")"
  )
}

/*  */

function on (el, dir) {
  if (false) {
    warn("v-on without argument does not support modifiers.");
  }
  el.wrapListeners = function (code) { return ("_g(" + code + "," + (dir.value) + ")"); };
}

/*  */

function bind$1 (el, dir) {
  el.wrapData = function (code) {
    return ("_b(" + code + ",'" + (el.tag) + "'," + (dir.value) + "," + (dir.modifiers && dir.modifiers.prop ? 'true' : 'false') + (dir.modifiers && dir.modifiers.sync ? ',true' : '') + ")")
  };
}

/*  */

var baseDirectives = {
  on: on,
  bind: bind$1,
  cloak: noop
}

/*  */

var CodegenState = function CodegenState (options) {
  this.options = options;
  this.warn = options.warn || baseWarn;
  this.transforms = pluckModuleFunction(options.modules, 'transformCode');
  this.dataGenFns = pluckModuleFunction(options.modules, 'genData');
  this.directives = extend(extend({}, baseDirectives), options.directives);
  var isReservedTag = options.isReservedTag || no;
  this.maybeComponent = function (el) { return !isReservedTag(el.tag); };
  this.onceId = 0;
  this.staticRenderFns = [];
};



function generate (
  ast,
  options
) {
  var state = new CodegenState(options);
  var code = ast ? genElement(ast, state) : '_c("div")';
  return {
    render: ("with(this){return " + code + "}"),
    staticRenderFns: state.staticRenderFns
  }
}

function genElement (el, state) {
  if (el.staticRoot && !el.staticProcessed) {
    return genStatic(el, state)
  } else if (el.once && !el.onceProcessed) {
    return genOnce(el, state)
  } else if (el.for && !el.forProcessed) {
    return genFor(el, state)
  } else if (el.if && !el.ifProcessed) {
    return genIf(el, state)
  } else if (el.tag === 'template' && !el.slotTarget) {
    return genChildren(el, state) || 'void 0'
  } else if (el.tag === 'slot') {
    return genSlot(el, state)
  } else {
    // component or element
    var code;
    if (el.component) {
      code = genComponent(el.component, el, state);
    } else {
      var data = el.plain ? undefined : genData$2(el, state);

      var children = el.inlineTemplate ? null : genChildren(el, state, true);
      code = "_c('" + (el.tag) + "'" + (data ? ("," + data) : '') + (children ? ("," + children) : '') + ")";
    }
    // module transforms
    for (var i = 0; i < state.transforms.length; i++) {
      code = state.transforms[i](el, code);
    }
    return code
  }
}

// hoist static sub-trees out
function genStatic (el, state) {
  el.staticProcessed = true;
  state.staticRenderFns.push(("with(this){return " + (genElement(el, state)) + "}"));
  return ("_m(" + (state.staticRenderFns.length - 1) + (el.staticInFor ? ',true' : '') + ")")
}

// v-once
function genOnce (el, state) {
  el.onceProcessed = true;
  if (el.if && !el.ifProcessed) {
    return genIf(el, state)
  } else if (el.staticInFor) {
    var key = '';
    var parent = el.parent;
    while (parent) {
      if (parent.for) {
        key = parent.key;
        break
      }
      parent = parent.parent;
    }
    if (!key) {
      "production" !== 'production' && state.warn(
        "v-once can only be used inside v-for that is keyed. "
      );
      return genElement(el, state)
    }
    return ("_o(" + (genElement(el, state)) + "," + (state.onceId++) + "," + key + ")")
  } else {
    return genStatic(el, state)
  }
}

function genIf (
  el,
  state,
  altGen,
  altEmpty
) {
  el.ifProcessed = true; // avoid recursion
  return genIfConditions(el.ifConditions.slice(), state, altGen, altEmpty)
}

function genIfConditions (
  conditions,
  state,
  altGen,
  altEmpty
) {
  if (!conditions.length) {
    return altEmpty || '_e()'
  }

  var condition = conditions.shift();
  if (condition.exp) {
    return ("(" + (condition.exp) + ")?" + (genTernaryExp(condition.block)) + ":" + (genIfConditions(conditions, state, altGen, altEmpty)))
  } else {
    return ("" + (genTernaryExp(condition.block)))
  }

  // v-if with v-once should generate code like (a)?_m(0):_m(1)
  function genTernaryExp (el) {
    return altGen
      ? altGen(el, state)
      : el.once
        ? genOnce(el, state)
        : genElement(el, state)
  }
}

function genFor (
  el,
  state,
  altGen,
  altHelper
) {
  var exp = el.for;
  var alias = el.alias;
  var iterator1 = el.iterator1 ? ("," + (el.iterator1)) : '';
  var iterator2 = el.iterator2 ? ("," + (el.iterator2)) : '';

  if (false
  ) {
    state.warn(
      "<" + (el.tag) + " v-for=\"" + alias + " in " + exp + "\">: component lists rendered with " +
      "v-for should have explicit keys. " +
      "See https://vuejs.org/guide/list.html#key for more info.",
      true /* tip */
    );
  }

  el.forProcessed = true; // avoid recursion
  return (altHelper || '_l') + "((" + exp + ")," +
    "function(" + alias + iterator1 + iterator2 + "){" +
      "return " + ((altGen || genElement)(el, state)) +
    '})'
}

function genData$2 (el, state) {
  var data = '{';

  // directives first.
  // directives may mutate the el's other properties before they are generated.
  var dirs = genDirectives(el, state);
  if (dirs) { data += dirs + ','; }

  // key
  if (el.key) {
    data += "key:" + (el.key) + ",";
  }
  // ref
  if (el.ref) {
    data += "ref:" + (el.ref) + ",";
  }
  if (el.refInFor) {
    data += "refInFor:true,";
  }
  // pre
  if (el.pre) {
    data += "pre:true,";
  }
  // record original tag name for components using "is" attribute
  if (el.component) {
    data += "tag:\"" + (el.tag) + "\",";
  }
  // module data generation functions
  for (var i = 0; i < state.dataGenFns.length; i++) {
    data += state.dataGenFns[i](el);
  }
  // attributes
  if (el.attrs) {
    data += "attrs:{" + (genProps(el.attrs)) + "},";
  }
  // DOM props
  if (el.props) {
    data += "domProps:{" + (genProps(el.props)) + "},";
  }
  // event handlers
  if (el.events) {
    data += (genHandlers(el.events, false, state.warn)) + ",";
  }
  if (el.nativeEvents) {
    data += (genHandlers(el.nativeEvents, true, state.warn)) + ",";
  }
  // slot target
  // only for non-scoped slots
  if (el.slotTarget && !el.slotScope) {
    data += "slot:" + (el.slotTarget) + ",";
  }
  // scoped slots
  if (el.scopedSlots) {
    data += (genScopedSlots(el.scopedSlots, state)) + ",";
  }
  // component v-model
  if (el.model) {
    data += "model:{value:" + (el.model.value) + ",callback:" + (el.model.callback) + ",expression:" + (el.model.expression) + "},";
  }
  // inline-template
  if (el.inlineTemplate) {
    var inlineTemplate = genInlineTemplate(el, state);
    if (inlineTemplate) {
      data += inlineTemplate + ",";
    }
  }
  data = data.replace(/,$/, '') + '}';
  // v-bind data wrap
  if (el.wrapData) {
    data = el.wrapData(data);
  }
  // v-on data wrap
  if (el.wrapListeners) {
    data = el.wrapListeners(data);
  }
  return data
}

function genDirectives (el, state) {
  var dirs = el.directives;
  if (!dirs) { return }
  var res = 'directives:[';
  var hasRuntime = false;
  var i, l, dir, needRuntime;
  for (i = 0, l = dirs.length; i < l; i++) {
    dir = dirs[i];
    needRuntime = true;
    var gen = state.directives[dir.name];
    if (gen) {
      // compile-time directive that manipulates AST.
      // returns true if it also needs a runtime counterpart.
      needRuntime = !!gen(el, dir, state.warn);
    }
    if (needRuntime) {
      hasRuntime = true;
      res += "{name:\"" + (dir.name) + "\",rawName:\"" + (dir.rawName) + "\"" + (dir.value ? (",value:(" + (dir.value) + "),expression:" + (JSON.stringify(dir.value))) : '') + (dir.arg ? (",arg:\"" + (dir.arg) + "\"") : '') + (dir.modifiers ? (",modifiers:" + (JSON.stringify(dir.modifiers))) : '') + "},";
    }
  }
  if (hasRuntime) {
    return res.slice(0, -1) + ']'
  }
}

function genInlineTemplate (el, state) {
  var ast = el.children[0];
  if (false) {
    state.warn('Inline-template components must have exactly one child element.');
  }
  if (ast.type === 1) {
    var inlineRenderFns = generate(ast, state.options);
    return ("inlineTemplate:{render:function(){" + (inlineRenderFns.render) + "},staticRenderFns:[" + (inlineRenderFns.staticRenderFns.map(function (code) { return ("function(){" + code + "}"); }).join(',')) + "]}")
  }
}

function genScopedSlots (
  slots,
  state
) {
  return ("scopedSlots:_u([" + (Object.keys(slots).map(function (key) {
      return genScopedSlot(key, slots[key], state)
    }).join(',')) + "])")
}

function genScopedSlot (
  key,
  el,
  state
) {
  if (el.for && !el.forProcessed) {
    return genForScopedSlot(key, el, state)
  }
  var fn = "function(" + (String(el.slotScope)) + "){" +
    "return " + (el.tag === 'template'
      ? el.if
        ? ((el.if) + "?" + (genChildren(el, state) || 'undefined') + ":undefined")
        : genChildren(el, state) || 'undefined'
      : genElement(el, state)) + "}";
  return ("{key:" + key + ",fn:" + fn + "}")
}

function genForScopedSlot (
  key,
  el,
  state
) {
  var exp = el.for;
  var alias = el.alias;
  var iterator1 = el.iterator1 ? ("," + (el.iterator1)) : '';
  var iterator2 = el.iterator2 ? ("," + (el.iterator2)) : '';
  el.forProcessed = true; // avoid recursion
  return "_l((" + exp + ")," +
    "function(" + alias + iterator1 + iterator2 + "){" +
      "return " + (genScopedSlot(key, el, state)) +
    '})'
}

function genChildren (
  el,
  state,
  checkSkip,
  altGenElement,
  altGenNode
) {
  var children = el.children;
  if (children.length) {
    var el$1 = children[0];
    // optimize single v-for
    if (children.length === 1 &&
      el$1.for &&
      el$1.tag !== 'template' &&
      el$1.tag !== 'slot'
    ) {
      return (altGenElement || genElement)(el$1, state)
    }
    var normalizationType = checkSkip
      ? getNormalizationType(children, state.maybeComponent)
      : 0;
    var gen = altGenNode || genNode;
    return ("[" + (children.map(function (c) { return gen(c, state); }).join(',')) + "]" + (normalizationType ? ("," + normalizationType) : ''))
  }
}

// determine the normalization needed for the children array.
// 0: no normalization needed
// 1: simple normalization needed (possible 1-level deep nested array)
// 2: full normalization needed
function getNormalizationType (
  children,
  maybeComponent
) {
  var res = 0;
  for (var i = 0; i < children.length; i++) {
    var el = children[i];
    if (el.type !== 1) {
      continue
    }
    if (needsNormalization(el) ||
        (el.ifConditions && el.ifConditions.some(function (c) { return needsNormalization(c.block); }))) {
      res = 2;
      break
    }
    if (maybeComponent(el) ||
        (el.ifConditions && el.ifConditions.some(function (c) { return maybeComponent(c.block); }))) {
      res = 1;
    }
  }
  return res
}

function needsNormalization (el) {
  return el.for !== undefined || el.tag === 'template' || el.tag === 'slot'
}

function genNode (node, state) {
  if (node.type === 1) {
    return genElement(node, state)
  } if (node.type === 3 && node.isComment) {
    return genComment(node)
  } else {
    return genText(node)
  }
}

function genText (text) {
  return ("_v(" + (text.type === 2
    ? text.expression // no need for () because already wrapped in _s()
    : transformSpecialNewlines(JSON.stringify(text.text))) + ")")
}

function genComment (comment) {
  return ("_e(" + (JSON.stringify(comment.text)) + ")")
}

function genSlot (el, state) {
  var slotName = el.slotName || '"default"';
  var children = genChildren(el, state);
  var res = "_t(" + slotName + (children ? ("," + children) : '');
  var attrs = el.attrs && ("{" + (el.attrs.map(function (a) { return ((camelize(a.name)) + ":" + (a.value)); }).join(',')) + "}");
  var bind$$1 = el.attrsMap['v-bind'];
  if ((attrs || bind$$1) && !children) {
    res += ",null";
  }
  if (attrs) {
    res += "," + attrs;
  }
  if (bind$$1) {
    res += (attrs ? '' : ',null') + "," + bind$$1;
  }
  return res + ')'
}

// componentName is el.component, take it as argument to shun flow's pessimistic refinement
function genComponent (
  componentName,
  el,
  state
) {
  var children = el.inlineTemplate ? null : genChildren(el, state, true);
  return ("_c(" + componentName + "," + (genData$2(el, state)) + (children ? ("," + children) : '') + ")")
}

function genProps (props) {
  var res = '';
  for (var i = 0; i < props.length; i++) {
    var prop = props[i];
    /* istanbul ignore if */
    {
      res += "\"" + (prop.name) + "\":" + (transformSpecialNewlines(prop.value)) + ",";
    }
  }
  return res.slice(0, -1)
}

// #3895, #4268
function transformSpecialNewlines (text) {
  return text
    .replace(/\u2028/g, '\\u2028')
    .replace(/\u2029/g, '\\u2029')
}

/*  */

// these keywords should not appear inside expressions, but operators like
// typeof, instanceof and in are allowed
var prohibitedKeywordRE = new RegExp('\\b' + (
  'do,if,for,let,new,try,var,case,else,with,await,break,catch,class,const,' +
  'super,throw,while,yield,delete,export,import,return,switch,default,' +
  'extends,finally,continue,debugger,function,arguments'
).split(',').join('\\b|\\b') + '\\b');

// these unary operators should not be used as property/method names
var unaryOperatorsRE = new RegExp('\\b' + (
  'delete,typeof,void'
).split(',').join('\\s*\\([^\\)]*\\)|\\b') + '\\s*\\([^\\)]*\\)');

// strip strings in expressions
var stripStringRE = /'(?:[^'\\]|\\.)*'|"(?:[^"\\]|\\.)*"|`(?:[^`\\]|\\.)*\$\{|\}(?:[^`\\]|\\.)*`|`(?:[^`\\]|\\.)*`/g;

// detect problematic expressions in a template
function detectErrors (ast) {
  var errors = [];
  if (ast) {
    checkNode(ast, errors);
  }
  return errors
}

function checkNode (node, errors) {
  if (node.type === 1) {
    for (var name in node.attrsMap) {
      if (dirRE.test(name)) {
        var value = node.attrsMap[name];
        if (value) {
          if (name === 'v-for') {
            checkFor(node, ("v-for=\"" + value + "\""), errors);
          } else if (onRE.test(name)) {
            checkEvent(value, (name + "=\"" + value + "\""), errors);
          } else {
            checkExpression(value, (name + "=\"" + value + "\""), errors);
          }
        }
      }
    }
    if (node.children) {
      for (var i = 0; i < node.children.length; i++) {
        checkNode(node.children[i], errors);
      }
    }
  } else if (node.type === 2) {
    checkExpression(node.expression, node.text, errors);
  }
}

function checkEvent (exp, text, errors) {
  var stipped = exp.replace(stripStringRE, '');
  var keywordMatch = stipped.match(unaryOperatorsRE);
  if (keywordMatch && stipped.charAt(keywordMatch.index - 1) !== '$') {
    errors.push(
      "avoid using JavaScript unary operator as property name: " +
      "\"" + (keywordMatch[0]) + "\" in expression " + (text.trim())
    );
  }
  checkExpression(exp, text, errors);
}

function checkFor (node, text, errors) {
  checkExpression(node.for || '', text, errors);
  checkIdentifier(node.alias, 'v-for alias', text, errors);
  checkIdentifier(node.iterator1, 'v-for iterator', text, errors);
  checkIdentifier(node.iterator2, 'v-for iterator', text, errors);
}

function checkIdentifier (
  ident,
  type,
  text,
  errors
) {
  if (typeof ident === 'string') {
    try {
      new Function(("var " + ident + "=_"));
    } catch (e) {
      errors.push(("invalid " + type + " \"" + ident + "\" in expression: " + (text.trim())));
    }
  }
}

function checkExpression (exp, text, errors) {
  try {
    new Function(("return " + exp));
  } catch (e) {
    var keywordMatch = exp.replace(stripStringRE, '').match(prohibitedKeywordRE);
    if (keywordMatch) {
      errors.push(
        "avoid using JavaScript keyword as property name: " +
        "\"" + (keywordMatch[0]) + "\"\n  Raw expression: " + (text.trim())
      );
    } else {
      errors.push(
        "invalid expression: " + (e.message) + " in\n\n" +
        "    " + exp + "\n\n" +
        "  Raw expression: " + (text.trim()) + "\n"
      );
    }
  }
}

/*  */

function createFunction (code, errors) {
  try {
    return new Function(code)
  } catch (err) {
    errors.push({ err: err, code: code });
    return noop
  }
}

function createCompileToFunctionFn (compile) {
  var cache = Object.create(null);

  return function compileToFunctions (
    template,
    options,
    vm
  ) {
    options = extend({}, options);
    var warn$$1 = options.warn || warn;
    delete options.warn;

    /* istanbul ignore if */
    if (false) {
      // detect possible CSP restriction
      try {
        new Function('return 1');
      } catch (e) {
        if (e.toString().match(/unsafe-eval|CSP/)) {
          warn$$1(
            'It seems you are using the standalone build of Vue.js in an ' +
            'environment with Content Security Policy that prohibits unsafe-eval. ' +
            'The template compiler cannot work in this environment. Consider ' +
            'relaxing the policy to allow unsafe-eval or pre-compiling your ' +
            'templates into render functions.'
          );
        }
      }
    }

    // check cache
    var key = options.delimiters
      ? String(options.delimiters) + template
      : template;
    if (cache[key]) {
      return cache[key]
    }

    // compile
    var compiled = compile(template, options);

    // check compilation errors/tips
    if (false) {
      if (compiled.errors && compiled.errors.length) {
        warn$$1(
          "Error compiling template:\n\n" + template + "\n\n" +
          compiled.errors.map(function (e) { return ("- " + e); }).join('\n') + '\n',
          vm
        );
      }
      if (compiled.tips && compiled.tips.length) {
        compiled.tips.forEach(function (msg) { return tip(msg, vm); });
      }
    }

    // turn code into functions
    var res = {};
    var fnGenErrors = [];
    res.render = createFunction(compiled.render, fnGenErrors);
    res.staticRenderFns = compiled.staticRenderFns.map(function (code) {
      return createFunction(code, fnGenErrors)
    });

    // check function generation errors.
    // this should only happen if there is a bug in the compiler itself.
    // mostly for codegen development use
    /* istanbul ignore if */
    if (false) {
      if ((!compiled.errors || !compiled.errors.length) && fnGenErrors.length) {
        warn$$1(
          "Failed to generate render function:\n\n" +
          fnGenErrors.map(function (ref) {
            var err = ref.err;
            var code = ref.code;

            return ((err.toString()) + " in\n\n" + code + "\n");
        }).join('\n'),
          vm
        );
      }
    }

    return (cache[key] = res)
  }
}

/*  */

function createCompilerCreator (baseCompile) {
  return function createCompiler (baseOptions) {
    function compile (
      template,
      options
    ) {
      var finalOptions = Object.create(baseOptions);
      var errors = [];
      var tips = [];
      finalOptions.warn = function (msg, tip) {
        (tip ? tips : errors).push(msg);
      };

      if (options) {
        // merge custom modules
        if (options.modules) {
          finalOptions.modules =
            (baseOptions.modules || []).concat(options.modules);
        }
        // merge custom directives
        if (options.directives) {
          finalOptions.directives = extend(
            Object.create(baseOptions.directives || null),
            options.directives
          );
        }
        // copy other options
        for (var key in options) {
          if (key !== 'modules' && key !== 'directives') {
            finalOptions[key] = options[key];
          }
        }
      }

      var compiled = baseCompile(template, finalOptions);
      if (false) {
        errors.push.apply(errors, detectErrors(compiled.ast));
      }
      compiled.errors = errors;
      compiled.tips = tips;
      return compiled
    }

    return {
      compile: compile,
      compileToFunctions: createCompileToFunctionFn(compile)
    }
  }
}

/*  */

// `createCompilerCreator` allows creating compilers that use alternative
// parser/optimizer/codegen, e.g the SSR optimizing compiler.
// Here we just export a default compiler using the default parts.
var createCompiler = createCompilerCreator(function baseCompile (
  template,
  options
) {
  var ast = parse(template.trim(), options);
  if (options.optimize !== false) {
    optimize(ast, options);
  }
  var code = generate(ast, options);
  return {
    ast: ast,
    render: code.render,
    staticRenderFns: code.staticRenderFns
  }
});

/*  */

var ref$1 = createCompiler(baseOptions);
var compileToFunctions = ref$1.compileToFunctions;

/*  */

// check whether current browser encodes a char inside attribute values
var div;
function getShouldDecode (href) {
  div = div || document.createElement('div');
  div.innerHTML = href ? "<a href=\"\n\"/>" : "<div a=\"\n\"/>";
  return div.innerHTML.indexOf('&#10;') > 0
}

// #3663: IE encodes newlines inside attribute values while other browsers don't
var shouldDecodeNewlines = inBrowser ? getShouldDecode(false) : false;
// #6828: chrome encodes content in a[href]
var shouldDecodeNewlinesForHref = inBrowser ? getShouldDecode(true) : false;

/*  */

var idToTemplate = cached(function (id) {
  var el = query(id);
  return el && el.innerHTML
});

var mount = Vue.prototype.$mount;
Vue.prototype.$mount = function (
  el,
  hydrating
) {
  el = el && query(el);

  /* istanbul ignore if */
  if (el === document.body || el === document.documentElement) {
    "production" !== 'production' && warn(
      "Do not mount Vue to <html> or <body> - mount to normal elements instead."
    );
    return this
  }

  var options = this.$options;
  // resolve template/el and convert to render function
  if (!options.render) {
    var template = options.template;
    if (template) {
      if (typeof template === 'string') {
        if (template.charAt(0) === '#') {
          template = idToTemplate(template);
          /* istanbul ignore if */
          if (false) {
            warn(
              ("Template element not found or is empty: " + (options.template)),
              this
            );
          }
        }
      } else if (template.nodeType) {
        template = template.innerHTML;
      } else {
        if (false) {
          warn('invalid template option:' + template, this);
        }
        return this
      }
    } else if (el) {
      template = getOuterHTML(el);
    }
    if (template) {
      /* istanbul ignore if */
      if (false) {
        mark('compile');
      }

      var ref = compileToFunctions(template, {
        shouldDecodeNewlines: shouldDecodeNewlines,
        shouldDecodeNewlinesForHref: shouldDecodeNewlinesForHref,
        delimiters: options.delimiters,
        comments: options.comments
      }, this);
      var render = ref.render;
      var staticRenderFns = ref.staticRenderFns;
      options.render = render;
      options.staticRenderFns = staticRenderFns;

      /* istanbul ignore if */
      if (false) {
        mark('compile end');
        measure(("vue " + (this._name) + " compile"), 'compile', 'compile end');
      }
    }
  }
  return mount.call(this, el, hydrating)
};

/**
 * Get outerHTML of elements, taking care
 * of SVG elements in IE as well.
 */
function getOuterHTML (el) {
  if (el.outerHTML) {
    return el.outerHTML
  } else {
    var container = document.createElement('div');
    container.appendChild(el.cloneNode(true));
    return container.innerHTML
  }
}

Vue.compile = compileToFunctions;

/* harmony default export */ __webpack_exports__["a"] = (Vue);

/* WEBPACK VAR INJECTION */}.call(__webpack_exports__, __webpack_require__(1), __webpack_require__(49).setImmediate))

/***/ }),
/* 21 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(43)
}
var Component = __webpack_require__(10)(
  /* script */
  __webpack_require__(40),
  /* template */
  __webpack_require__(53),
  /* styles */
  injectStyle,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 22 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var utils = __webpack_require__(0);
var bind = __webpack_require__(8);
var Axios = __webpack_require__(24);
var defaults = __webpack_require__(2);

/**
 * Create an instance of Axios
 *
 * @param {Object} defaultConfig The default config for the instance
 * @return {Axios} A new instance of Axios
 */
function createInstance(defaultConfig) {
  var context = new Axios(defaultConfig);
  var instance = bind(Axios.prototype.request, context);

  // Copy axios.prototype to instance
  utils.extend(instance, Axios.prototype, context);

  // Copy context to instance
  utils.extend(instance, context);

  return instance;
}

// Create the default instance to be exported
var axios = createInstance(defaults);

// Expose Axios class to allow class inheritance
axios.Axios = Axios;

// Factory for creating new instances
axios.create = function create(instanceConfig) {
  return createInstance(utils.merge(defaults, instanceConfig));
};

// Expose Cancel & CancelToken
axios.Cancel = __webpack_require__(5);
axios.CancelToken = __webpack_require__(23);
axios.isCancel = __webpack_require__(6);

// Expose all/spread
axios.all = function all(promises) {
  return Promise.all(promises);
};
axios.spread = __webpack_require__(38);

module.exports = axios;

// Allow use of default import syntax in TypeScript
module.exports.default = axios;


/***/ }),
/* 23 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var Cancel = __webpack_require__(5);

/**
 * A `CancelToken` is an object that can be used to request cancellation of an operation.
 *
 * @class
 * @param {Function} executor The executor function.
 */
function CancelToken(executor) {
  if (typeof executor !== 'function') {
    throw new TypeError('executor must be a function.');
  }

  var resolvePromise;
  this.promise = new Promise(function promiseExecutor(resolve) {
    resolvePromise = resolve;
  });

  var token = this;
  executor(function cancel(message) {
    if (token.reason) {
      // Cancellation has already been requested
      return;
    }

    token.reason = new Cancel(message);
    resolvePromise(token.reason);
  });
}

/**
 * Throws a `Cancel` if cancellation has been requested.
 */
CancelToken.prototype.throwIfRequested = function throwIfRequested() {
  if (this.reason) {
    throw this.reason;
  }
};

/**
 * Returns an object that contains a new `CancelToken` and a function that, when called,
 * cancels the `CancelToken`.
 */
CancelToken.source = function source() {
  var cancel;
  var token = new CancelToken(function executor(c) {
    cancel = c;
  });
  return {
    token: token,
    cancel: cancel
  };
};

module.exports = CancelToken;


/***/ }),
/* 24 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var defaults = __webpack_require__(2);
var utils = __webpack_require__(0);
var InterceptorManager = __webpack_require__(25);
var dispatchRequest = __webpack_require__(26);

/**
 * Create a new instance of Axios
 *
 * @param {Object} instanceConfig The default config for the instance
 */
function Axios(instanceConfig) {
  this.defaults = instanceConfig;
  this.interceptors = {
    request: new InterceptorManager(),
    response: new InterceptorManager()
  };
}

/**
 * Dispatch a request
 *
 * @param {Object} config The config specific for this request (merged with this.defaults)
 */
Axios.prototype.request = function request(config) {
  /*eslint no-param-reassign:0*/
  // Allow for axios('example/url'[, config]) a la fetch API
  if (typeof config === 'string') {
    config = utils.merge({
      url: arguments[0]
    }, arguments[1]);
  }

  config = utils.merge(defaults, {method: 'get'}, this.defaults, config);
  config.method = config.method.toLowerCase();

  // Hook up interceptors middleware
  var chain = [dispatchRequest, undefined];
  var promise = Promise.resolve(config);

  this.interceptors.request.forEach(function unshiftRequestInterceptors(interceptor) {
    chain.unshift(interceptor.fulfilled, interceptor.rejected);
  });

  this.interceptors.response.forEach(function pushResponseInterceptors(interceptor) {
    chain.push(interceptor.fulfilled, interceptor.rejected);
  });

  while (chain.length) {
    promise = promise.then(chain.shift(), chain.shift());
  }

  return promise;
};

// Provide aliases for supported request methods
utils.forEach(['delete', 'get', 'head', 'options'], function forEachMethodNoData(method) {
  /*eslint func-names:0*/
  Axios.prototype[method] = function(url, config) {
    return this.request(utils.merge(config || {}, {
      method: method,
      url: url
    }));
  };
});

utils.forEach(['post', 'put', 'patch'], function forEachMethodWithData(method) {
  /*eslint func-names:0*/
  Axios.prototype[method] = function(url, data, config) {
    return this.request(utils.merge(config || {}, {
      method: method,
      url: url,
      data: data
    }));
  };
});

module.exports = Axios;


/***/ }),
/* 25 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var utils = __webpack_require__(0);

function InterceptorManager() {
  this.handlers = [];
}

/**
 * Add a new interceptor to the stack
 *
 * @param {Function} fulfilled The function to handle `then` for a `Promise`
 * @param {Function} rejected The function to handle `reject` for a `Promise`
 *
 * @return {Number} An ID used to remove interceptor later
 */
InterceptorManager.prototype.use = function use(fulfilled, rejected) {
  this.handlers.push({
    fulfilled: fulfilled,
    rejected: rejected
  });
  return this.handlers.length - 1;
};

/**
 * Remove an interceptor from the stack
 *
 * @param {Number} id The ID that was returned by `use`
 */
InterceptorManager.prototype.eject = function eject(id) {
  if (this.handlers[id]) {
    this.handlers[id] = null;
  }
};

/**
 * Iterate over all the registered interceptors
 *
 * This method is particularly useful for skipping over any
 * interceptors that may have become `null` calling `eject`.
 *
 * @param {Function} fn The function to call for each interceptor
 */
InterceptorManager.prototype.forEach = function forEach(fn) {
  utils.forEach(this.handlers, function forEachHandler(h) {
    if (h !== null) {
      fn(h);
    }
  });
};

module.exports = InterceptorManager;


/***/ }),
/* 26 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var utils = __webpack_require__(0);
var transformData = __webpack_require__(29);
var isCancel = __webpack_require__(6);
var defaults = __webpack_require__(2);
var isAbsoluteURL = __webpack_require__(34);
var combineURLs = __webpack_require__(32);

/**
 * Throws a `Cancel` if cancellation has been requested.
 */
function throwIfCancellationRequested(config) {
  if (config.cancelToken) {
    config.cancelToken.throwIfRequested();
  }
}

/**
 * Dispatch a request to the server using the configured adapter.
 *
 * @param {object} config The config that is to be used for the request
 * @returns {Promise} The Promise to be fulfilled
 */
module.exports = function dispatchRequest(config) {
  throwIfCancellationRequested(config);

  // Support baseURL config
  if (config.baseURL && !isAbsoluteURL(config.url)) {
    config.url = combineURLs(config.baseURL, config.url);
  }

  // Ensure headers exist
  config.headers = config.headers || {};

  // Transform request data
  config.data = transformData(
    config.data,
    config.headers,
    config.transformRequest
  );

  // Flatten headers
  config.headers = utils.merge(
    config.headers.common || {},
    config.headers[config.method] || {},
    config.headers || {}
  );

  utils.forEach(
    ['delete', 'get', 'head', 'post', 'put', 'patch', 'common'],
    function cleanHeaderConfig(method) {
      delete config.headers[method];
    }
  );

  var adapter = config.adapter || defaults.adapter;

  return adapter(config).then(function onAdapterResolution(response) {
    throwIfCancellationRequested(config);

    // Transform response data
    response.data = transformData(
      response.data,
      response.headers,
      config.transformResponse
    );

    return response;
  }, function onAdapterRejection(reason) {
    if (!isCancel(reason)) {
      throwIfCancellationRequested(config);

      // Transform response data
      if (reason && reason.response) {
        reason.response.data = transformData(
          reason.response.data,
          reason.response.headers,
          config.transformResponse
        );
      }
    }

    return Promise.reject(reason);
  });
};


/***/ }),
/* 27 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


/**
 * Update an Error with the specified config, error code, and response.
 *
 * @param {Error} error The error to update.
 * @param {Object} config The config.
 * @param {string} [code] The error code (for example, 'ECONNABORTED').
 * @param {Object} [request] The request.
 * @param {Object} [response] The response.
 * @returns {Error} The error.
 */
module.exports = function enhanceError(error, config, code, request, response) {
  error.config = config;
  if (code) {
    error.code = code;
  }
  error.request = request;
  error.response = response;
  return error;
};


/***/ }),
/* 28 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var createError = __webpack_require__(7);

/**
 * Resolve or reject a Promise based on response status.
 *
 * @param {Function} resolve A function that resolves the promise.
 * @param {Function} reject A function that rejects the promise.
 * @param {object} response The response.
 */
module.exports = function settle(resolve, reject, response) {
  var validateStatus = response.config.validateStatus;
  // Note: status is not exposed by XDomainRequest
  if (!response.status || !validateStatus || validateStatus(response.status)) {
    resolve(response);
  } else {
    reject(createError(
      'Request failed with status code ' + response.status,
      response.config,
      null,
      response.request,
      response
    ));
  }
};


/***/ }),
/* 29 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var utils = __webpack_require__(0);

/**
 * Transform the data for a request or a response
 *
 * @param {Object|String} data The data to be transformed
 * @param {Array} headers The headers for the request or response
 * @param {Array|Function} fns A single function or Array of functions
 * @returns {*} The resulting transformed data
 */
module.exports = function transformData(data, headers, fns) {
  /*eslint no-param-reassign:0*/
  utils.forEach(fns, function transform(fn) {
    data = fn(data, headers);
  });

  return data;
};


/***/ }),
/* 30 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


// btoa polyfill for IE<10 courtesy https://github.com/davidchambers/Base64.js

var chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';

function E() {
  this.message = 'String contains an invalid character';
}
E.prototype = new Error;
E.prototype.code = 5;
E.prototype.name = 'InvalidCharacterError';

function btoa(input) {
  var str = String(input);
  var output = '';
  for (
    // initialize result and counter
    var block, charCode, idx = 0, map = chars;
    // if the next str index does not exist:
    //   change the mapping table to "="
    //   check if d has no fractional digits
    str.charAt(idx | 0) || (map = '=', idx % 1);
    // "8 - idx % 1 * 8" generates the sequence 2, 4, 6, 8
    output += map.charAt(63 & block >> 8 - idx % 1 * 8)
  ) {
    charCode = str.charCodeAt(idx += 3 / 4);
    if (charCode > 0xFF) {
      throw new E();
    }
    block = block << 8 | charCode;
  }
  return output;
}

module.exports = btoa;


/***/ }),
/* 31 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var utils = __webpack_require__(0);

function encode(val) {
  return encodeURIComponent(val).
    replace(/%40/gi, '@').
    replace(/%3A/gi, ':').
    replace(/%24/g, '$').
    replace(/%2C/gi, ',').
    replace(/%20/g, '+').
    replace(/%5B/gi, '[').
    replace(/%5D/gi, ']');
}

/**
 * Build a URL by appending params to the end
 *
 * @param {string} url The base of the url (e.g., http://www.google.com)
 * @param {object} [params] The params to be appended
 * @returns {string} The formatted url
 */
module.exports = function buildURL(url, params, paramsSerializer) {
  /*eslint no-param-reassign:0*/
  if (!params) {
    return url;
  }

  var serializedParams;
  if (paramsSerializer) {
    serializedParams = paramsSerializer(params);
  } else if (utils.isURLSearchParams(params)) {
    serializedParams = params.toString();
  } else {
    var parts = [];

    utils.forEach(params, function serialize(val, key) {
      if (val === null || typeof val === 'undefined') {
        return;
      }

      if (utils.isArray(val)) {
        key = key + '[]';
      } else {
        val = [val];
      }

      utils.forEach(val, function parseValue(v) {
        if (utils.isDate(v)) {
          v = v.toISOString();
        } else if (utils.isObject(v)) {
          v = JSON.stringify(v);
        }
        parts.push(encode(key) + '=' + encode(v));
      });
    });

    serializedParams = parts.join('&');
  }

  if (serializedParams) {
    url += (url.indexOf('?') === -1 ? '?' : '&') + serializedParams;
  }

  return url;
};


/***/ }),
/* 32 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


/**
 * Creates a new URL by combining the specified URLs
 *
 * @param {string} baseURL The base URL
 * @param {string} relativeURL The relative URL
 * @returns {string} The combined URL
 */
module.exports = function combineURLs(baseURL, relativeURL) {
  return relativeURL
    ? baseURL.replace(/\/+$/, '') + '/' + relativeURL.replace(/^\/+/, '')
    : baseURL;
};


/***/ }),
/* 33 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var utils = __webpack_require__(0);

module.exports = (
  utils.isStandardBrowserEnv() ?

  // Standard browser envs support document.cookie
  (function standardBrowserEnv() {
    return {
      write: function write(name, value, expires, path, domain, secure) {
        var cookie = [];
        cookie.push(name + '=' + encodeURIComponent(value));

        if (utils.isNumber(expires)) {
          cookie.push('expires=' + new Date(expires).toGMTString());
        }

        if (utils.isString(path)) {
          cookie.push('path=' + path);
        }

        if (utils.isString(domain)) {
          cookie.push('domain=' + domain);
        }

        if (secure === true) {
          cookie.push('secure');
        }

        document.cookie = cookie.join('; ');
      },

      read: function read(name) {
        var match = document.cookie.match(new RegExp('(^|;\\s*)(' + name + ')=([^;]*)'));
        return (match ? decodeURIComponent(match[3]) : null);
      },

      remove: function remove(name) {
        this.write(name, '', Date.now() - 86400000);
      }
    };
  })() :

  // Non standard browser env (web workers, react-native) lack needed support.
  (function nonStandardBrowserEnv() {
    return {
      write: function write() {},
      read: function read() { return null; },
      remove: function remove() {}
    };
  })()
);


/***/ }),
/* 34 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


/**
 * Determines whether the specified URL is absolute
 *
 * @param {string} url The URL to test
 * @returns {boolean} True if the specified URL is absolute, otherwise false
 */
module.exports = function isAbsoluteURL(url) {
  // A URL is considered absolute if it begins with "<scheme>://" or "//" (protocol-relative URL).
  // RFC 3986 defines scheme name as a sequence of characters beginning with a letter and followed
  // by any combination of letters, digits, plus, period, or hyphen.
  return /^([a-z][a-z\d\+\-\.]*:)?\/\//i.test(url);
};


/***/ }),
/* 35 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var utils = __webpack_require__(0);

module.exports = (
  utils.isStandardBrowserEnv() ?

  // Standard browser envs have full support of the APIs needed to test
  // whether the request URL is of the same origin as current location.
  (function standardBrowserEnv() {
    var msie = /(msie|trident)/i.test(navigator.userAgent);
    var urlParsingNode = document.createElement('a');
    var originURL;

    /**
    * Parse a URL to discover it's components
    *
    * @param {String} url The URL to be parsed
    * @returns {Object}
    */
    function resolveURL(url) {
      var href = url;

      if (msie) {
        // IE needs attribute set twice to normalize properties
        urlParsingNode.setAttribute('href', href);
        href = urlParsingNode.href;
      }

      urlParsingNode.setAttribute('href', href);

      // urlParsingNode provides the UrlUtils interface - http://url.spec.whatwg.org/#urlutils
      return {
        href: urlParsingNode.href,
        protocol: urlParsingNode.protocol ? urlParsingNode.protocol.replace(/:$/, '') : '',
        host: urlParsingNode.host,
        search: urlParsingNode.search ? urlParsingNode.search.replace(/^\?/, '') : '',
        hash: urlParsingNode.hash ? urlParsingNode.hash.replace(/^#/, '') : '',
        hostname: urlParsingNode.hostname,
        port: urlParsingNode.port,
        pathname: (urlParsingNode.pathname.charAt(0) === '/') ?
                  urlParsingNode.pathname :
                  '/' + urlParsingNode.pathname
      };
    }

    originURL = resolveURL(window.location.href);

    /**
    * Determine if a URL shares the same origin as the current location
    *
    * @param {String} requestURL The URL to test
    * @returns {boolean} True if URL shares the same origin, otherwise false
    */
    return function isURLSameOrigin(requestURL) {
      var parsed = (utils.isString(requestURL)) ? resolveURL(requestURL) : requestURL;
      return (parsed.protocol === originURL.protocol &&
            parsed.host === originURL.host);
    };
  })() :

  // Non standard browser envs (web workers, react-native) lack needed support.
  (function nonStandardBrowserEnv() {
    return function isURLSameOrigin() {
      return true;
    };
  })()
);


/***/ }),
/* 36 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var utils = __webpack_require__(0);

module.exports = function normalizeHeaderName(headers, normalizedName) {
  utils.forEach(headers, function processHeader(value, name) {
    if (name !== normalizedName && name.toUpperCase() === normalizedName.toUpperCase()) {
      headers[normalizedName] = value;
      delete headers[name];
    }
  });
};


/***/ }),
/* 37 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var utils = __webpack_require__(0);

// Headers whose duplicates are ignored by node
// c.f. https://nodejs.org/api/http.html#http_message_headers
var ignoreDuplicateOf = [
  'age', 'authorization', 'content-length', 'content-type', 'etag',
  'expires', 'from', 'host', 'if-modified-since', 'if-unmodified-since',
  'last-modified', 'location', 'max-forwards', 'proxy-authorization',
  'referer', 'retry-after', 'user-agent'
];

/**
 * Parse headers into an object
 *
 * ```
 * Date: Wed, 27 Aug 2014 08:58:49 GMT
 * Content-Type: application/json
 * Connection: keep-alive
 * Transfer-Encoding: chunked
 * ```
 *
 * @param {String} headers Headers needing to be parsed
 * @returns {Object} Headers parsed into an object
 */
module.exports = function parseHeaders(headers) {
  var parsed = {};
  var key;
  var val;
  var i;

  if (!headers) { return parsed; }

  utils.forEach(headers.split('\n'), function parser(line) {
    i = line.indexOf(':');
    key = utils.trim(line.substr(0, i)).toLowerCase();
    val = utils.trim(line.substr(i + 1));

    if (key) {
      if (parsed[key] && ignoreDuplicateOf.indexOf(key) >= 0) {
        return;
      }
      if (key === 'set-cookie') {
        parsed[key] = (parsed[key] ? parsed[key] : []).concat([val]);
      } else {
        parsed[key] = parsed[key] ? parsed[key] + ', ' + val : val;
      }
    }
  });

  return parsed;
};


/***/ }),
/* 38 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


/**
 * Syntactic sugar for invoking a function and expanding an array for arguments.
 *
 * Common use case would be to use `Function.prototype.apply`.
 *
 *  ```js
 *  function f(x, y, z) {}
 *  var args = [1, 2, 3];
 *  f.apply(null, args);
 *  ```
 *
 * With `spread` this example can be re-written.
 *
 *  ```js
 *  spread(function(x, y, z) {})([1, 2, 3]);
 *  ```
 *
 * @param {Function} callback
 * @returns {Function}
 */
module.exports = function spread(callback) {
  return function wrap(arr) {
    return callback.apply(null, arr);
  };
};


/***/ }),
/* 39 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__static_reset_css__ = __webpack_require__(14);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__static_reset_css___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__static_reset_css__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_vue__ = __webpack_require__(20);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__App_vue__ = __webpack_require__(21);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__App_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__App_vue__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_axios__ = __webpack_require__(11);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_axios___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_axios__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_lib_flexible__ = __webpack_require__(16);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_lib_flexible___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_lib_flexible__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__router__ = __webpack_require__(12);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6_vue_router__ = __webpack_require__(19);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7_fastclick__ = __webpack_require__(15);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7_fastclick___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_7_fastclick__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8_vue_resource__ = __webpack_require__(18);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9_vue_lazyload__ = __webpack_require__(17);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9_vue_lazyload___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_9_vue_lazyload__);










__webpack_require__(13).polyfill();
Array.prototype.S = String.fromCharCode(2);
Array.prototype.in_array = function (e) {
    var r = new RegExp(this.S + e + this.S);
    return r.test(this.S + this.join(this.S) + this.S);
};

//设置头信息
document.setTitle = function (t) {
    document.title = t;
    var i = document.createElement('iframe');
    i.style.display = 'none';
    i.onload = function () {
        setTimeout(function () {
            i.remove();
        }, 9);
    };
    document.body.appendChild(i);
};
__WEBPACK_IMPORTED_MODULE_1_vue__["a" /* default */].prototype.comdify = function (n) {
    n = n.toString().replace(/,/gi, '');
    let re = /\d{1,3}(?=(\d{3})+$)/g;
    let n1 = n.replace(/^(\d+)((\.\d+)?)$/, function (s, s1, s2) {
        return s1.replace(re, "$&,") + s2;
    });
    return n1;
};
__WEBPACK_IMPORTED_MODULE_1_vue__["a" /* default */].prototype.$http = __WEBPACK_IMPORTED_MODULE_3_axios___default.a;
__WEBPACK_IMPORTED_MODULE_1_vue__["a" /* default */].prototype.changeUrlData = function (url) {
    var urlObject = {};
    if (/\?/.test(url)) {
        var urlString = url.substring(url.indexOf("?") + 1);
        var urlArray = urlString.split("&");
        for (var i = 0, len = urlArray.length; i < len; i++) {
            var urlItem = urlArray[i];
            var item = urlItem.split("=");
            urlObject[item[0]] = item[1];
        }
        return urlObject;
    }
};
__WEBPACK_IMPORTED_MODULE_1_vue__["a" /* default */].use(__WEBPACK_IMPORTED_MODULE_8_vue_resource__["a" /* default */]);
__WEBPACK_IMPORTED_MODULE_1_vue__["a" /* default */].use(__WEBPACK_IMPORTED_MODULE_6_vue_router__["a" /* default */]);
if ('addEventListener' in document) {
    document.addEventListener('DOMContentLoaded', function () {
        __WEBPACK_IMPORTED_MODULE_7_fastclick___default.a.attach(document.body);
    }, false);
}
const router = new __WEBPACK_IMPORTED_MODULE_6_vue_router__["a" /* default */](__WEBPACK_IMPORTED_MODULE_5__router__["a" /* default */]);
const app = new __WEBPACK_IMPORTED_MODULE_1_vue__["a" /* default */]({
    router: router,
    render: h => h(__WEBPACK_IMPORTED_MODULE_2__App_vue___default.a)
}).$mount('#app');

/***/ }),
/* 40 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
//
//
//
//
//

/* harmony default export */ __webpack_exports__["default"] = ({
    data() {
        return {};
    },
    mounted: function () {},
    methods: {}
});

/***/ }),
/* 41 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_qs__ = __webpack_require__(45);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_qs___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_qs__);
//
//
//
//
//
//
//
//
//
//


/* harmony default export */ __webpack_exports__["default"] = ({
  data() {
    return {
      bg: __webpack_require__(50),
      stopClick: true,
      isWeiXin: false, //是否在微信端打开
      vImg: __webpack_require__(51),
      uploadAppUrl: "https://www.yzb668.com/download/android/app/yzb-android-1.1.0.apk"
    };
  },
  mounted: function () {
    if (this.isWeiXinFn()) {
      this.isWeiXin = true;
    } else {
      this.isWeiXin = false;
    }
  },
  methods: {
    //去下载页面
    jumpUploadApp() {
      if (this.isiOSApp()) {
        window.open("https://www.yzb668.com/download/ios/guide.html", "_self");
      } else {
        window.open(this.uploadAppUrl, "_self");
      }
      return false;
    },
    //判断是否为微信端
    isWeiXinFn() {
      var ua = window.navigator.userAgent.toLowerCase();
      if (ua.match(/MicroMessenger/i) == "micromessenger") {
        return true;
      } else {
        return false;
      }
    },
    isiOSApp() {
      var u = navigator.userAgent;
      var isAndroid = u.indexOf("Android") > -1 || u.indexOf("Adr") > -1; //android终端
      var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
      return isiOS;
    }
  }
});

/***/ }),
/* 42 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 43 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 44 */
/***/ (function(module, exports) {

/*!
 * Determine if an object is a Buffer
 *
 * @author   Feross Aboukhadijeh <https://feross.org>
 * @license  MIT
 */

// The _isBuffer check is for Safari 5-7 support, because it's missing
// Object.prototype.constructor. Remove this eventually
module.exports = function (obj) {
  return obj != null && (isBuffer(obj) || isSlowBuffer(obj) || !!obj._isBuffer)
}

function isBuffer (obj) {
  return !!obj.constructor && typeof obj.constructor.isBuffer === 'function' && obj.constructor.isBuffer(obj)
}

// For Node v0.10 support. Remove this eventually.
function isSlowBuffer (obj) {
  return typeof obj.readFloatLE === 'function' && typeof obj.slice === 'function' && isBuffer(obj.slice(0, 0))
}


/***/ }),
/* 45 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var Stringify = __webpack_require__(47);
var Parse = __webpack_require__(46);

module.exports = {
    stringify: Stringify,
    parse: Parse
};


/***/ }),
/* 46 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var Utils = __webpack_require__(9);

var has = Object.prototype.hasOwnProperty;

var defaults = {
    delimiter: '&',
    depth: 5,
    arrayLimit: 20,
    parameterLimit: 1000,
    strictNullHandling: false,
    plainObjects: false,
    allowPrototypes: false,
    allowDots: false,
    decoder: Utils.decode
};

var parseValues = function parseValues(str, options) {
    var obj = {};
    var parts = str.split(options.delimiter, options.parameterLimit === Infinity ? undefined : options.parameterLimit);

    for (var i = 0; i < parts.length; ++i) {
        var part = parts[i];
        var pos = part.indexOf(']=') === -1 ? part.indexOf('=') : part.indexOf(']=') + 1;

        var key, val;
        if (pos === -1) {
            key = options.decoder(part);
            val = options.strictNullHandling ? null : '';
        } else {
            key = options.decoder(part.slice(0, pos));
            val = options.decoder(part.slice(pos + 1));
        }
        if (has.call(obj, key)) {
            obj[key] = [].concat(obj[key]).concat(val);
        } else {
            obj[key] = val;
        }
    }

    return obj;
};

var parseObject = function parseObject(chain, val, options) {
    if (!chain.length) {
        return val;
    }

    var root = chain.shift();

    var obj;
    if (root === '[]') {
        obj = [];
        obj = obj.concat(parseObject(chain, val, options));
    } else {
        obj = options.plainObjects ? Object.create(null) : {};
        var cleanRoot = root.charAt(0) === '[' && root.charAt(root.length - 1) === ']' ? root.slice(1, -1) : root;
        var index = parseInt(cleanRoot, 10);
        if (
            !isNaN(index) &&
            root !== cleanRoot &&
            String(index) === cleanRoot &&
            index >= 0 &&
            (options.parseArrays && index <= options.arrayLimit)
        ) {
            obj = [];
            obj[index] = parseObject(chain, val, options);
        } else {
            obj[cleanRoot] = parseObject(chain, val, options);
        }
    }

    return obj;
};

var parseKeys = function parseKeys(givenKey, val, options) {
    if (!givenKey) {
        return;
    }

    // Transform dot notation to bracket notation
    var key = options.allowDots ? givenKey.replace(/\.([^.[]+)/g, '[$1]') : givenKey;

    // The regex chunks

    var brackets = /(\[[^[\]]*])/;
    var child = /(\[[^[\]]*])/g;

    // Get the parent

    var segment = brackets.exec(key);
    var parent = segment ? key.slice(0, segment.index) : key;

    // Stash the parent if it exists

    var keys = [];
    if (parent) {
        // If we aren't using plain objects, optionally prefix keys
        // that would overwrite object prototype properties
        if (!options.plainObjects && has.call(Object.prototype, parent)) {
            if (!options.allowPrototypes) {
                return;
            }
        }

        keys.push(parent);
    }

    // Loop through children appending to the array until we hit depth

    var i = 0;
    while ((segment = child.exec(key)) !== null && i < options.depth) {
        i += 1;
        if (!options.plainObjects && has.call(Object.prototype, segment[1].slice(1, -1))) {
            if (!options.allowPrototypes) {
                return;
            }
        }
        keys.push(segment[1]);
    }

    // If there's a remainder, just add whatever is left

    if (segment) {
        keys.push('[' + key.slice(segment.index) + ']');
    }

    return parseObject(keys, val, options);
};

module.exports = function (str, opts) {
    var options = opts || {};

    if (options.decoder !== null && options.decoder !== undefined && typeof options.decoder !== 'function') {
        throw new TypeError('Decoder has to be a function.');
    }

    options.delimiter = typeof options.delimiter === 'string' || Utils.isRegExp(options.delimiter) ? options.delimiter : defaults.delimiter;
    options.depth = typeof options.depth === 'number' ? options.depth : defaults.depth;
    options.arrayLimit = typeof options.arrayLimit === 'number' ? options.arrayLimit : defaults.arrayLimit;
    options.parseArrays = options.parseArrays !== false;
    options.decoder = typeof options.decoder === 'function' ? options.decoder : defaults.decoder;
    options.allowDots = typeof options.allowDots === 'boolean' ? options.allowDots : defaults.allowDots;
    options.plainObjects = typeof options.plainObjects === 'boolean' ? options.plainObjects : defaults.plainObjects;
    options.allowPrototypes = typeof options.allowPrototypes === 'boolean' ? options.allowPrototypes : defaults.allowPrototypes;
    options.parameterLimit = typeof options.parameterLimit === 'number' ? options.parameterLimit : defaults.parameterLimit;
    options.strictNullHandling = typeof options.strictNullHandling === 'boolean' ? options.strictNullHandling : defaults.strictNullHandling;

    if (str === '' || str === null || typeof str === 'undefined') {
        return options.plainObjects ? Object.create(null) : {};
    }

    var tempObj = typeof str === 'string' ? parseValues(str, options) : str;
    var obj = options.plainObjects ? Object.create(null) : {};

    // Iterate over the keys and setup the new object

    var keys = Object.keys(tempObj);
    for (var i = 0; i < keys.length; ++i) {
        var key = keys[i];
        var newObj = parseKeys(key, tempObj[key], options);
        obj = Utils.merge(obj, newObj, options);
    }

    return Utils.compact(obj);
};


/***/ }),
/* 47 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var Utils = __webpack_require__(9);

var arrayPrefixGenerators = {
    brackets: function brackets(prefix) {
        return prefix + '[]';
    },
    indices: function indices(prefix, key) {
        return prefix + '[' + key + ']';
    },
    repeat: function repeat(prefix) {
        return prefix;
    }
};

var defaults = {
    delimiter: '&',
    strictNullHandling: false,
    skipNulls: false,
    encode: true,
    encoder: Utils.encode
};

var stringify = function stringify(object, prefix, generateArrayPrefix, strictNullHandling, skipNulls, encoder, filter, sort, allowDots) {
    var obj = object;
    if (typeof filter === 'function') {
        obj = filter(prefix, obj);
    } else if (obj instanceof Date) {
        obj = obj.toISOString();
    } else if (obj === null) {
        if (strictNullHandling) {
            return encoder ? encoder(prefix) : prefix;
        }

        obj = '';
    }

    if (typeof obj === 'string' || typeof obj === 'number' || typeof obj === 'boolean' || Utils.isBuffer(obj)) {
        if (encoder) {
            return [encoder(prefix) + '=' + encoder(obj)];
        }
        return [prefix + '=' + String(obj)];
    }

    var values = [];

    if (typeof obj === 'undefined') {
        return values;
    }

    var objKeys;
    if (Array.isArray(filter)) {
        objKeys = filter;
    } else {
        var keys = Object.keys(obj);
        objKeys = sort ? keys.sort(sort) : keys;
    }

    for (var i = 0; i < objKeys.length; ++i) {
        var key = objKeys[i];

        if (skipNulls && obj[key] === null) {
            continue;
        }

        if (Array.isArray(obj)) {
            values = values.concat(stringify(obj[key], generateArrayPrefix(prefix, key), generateArrayPrefix, strictNullHandling, skipNulls, encoder, filter, sort, allowDots));
        } else {
            values = values.concat(stringify(obj[key], prefix + (allowDots ? '.' + key : '[' + key + ']'), generateArrayPrefix, strictNullHandling, skipNulls, encoder, filter, sort, allowDots));
        }
    }

    return values;
};

module.exports = function (object, opts) {
    var obj = object;
    var options = opts || {};
    var delimiter = typeof options.delimiter === 'undefined' ? defaults.delimiter : options.delimiter;
    var strictNullHandling = typeof options.strictNullHandling === 'boolean' ? options.strictNullHandling : defaults.strictNullHandling;
    var skipNulls = typeof options.skipNulls === 'boolean' ? options.skipNulls : defaults.skipNulls;
    var encode = typeof options.encode === 'boolean' ? options.encode : defaults.encode;
    var encoder = encode ? (typeof options.encoder === 'function' ? options.encoder : defaults.encoder) : null;
    var sort = typeof options.sort === 'function' ? options.sort : null;
    var allowDots = typeof options.allowDots === 'undefined' ? false : options.allowDots;
    var objKeys;
    var filter;

    if (options.encoder !== null && options.encoder !== undefined && typeof options.encoder !== 'function') {
        throw new TypeError('Encoder has to be a function.');
    }

    if (typeof options.filter === 'function') {
        filter = options.filter;
        obj = filter('', obj);
    } else if (Array.isArray(options.filter)) {
        objKeys = filter = options.filter;
    }

    var keys = [];

    if (typeof obj !== 'object' || obj === null) {
        return '';
    }

    var arrayFormat;
    if (options.arrayFormat in arrayPrefixGenerators) {
        arrayFormat = options.arrayFormat;
    } else if ('indices' in options) {
        arrayFormat = options.indices ? 'indices' : 'repeat';
    } else {
        arrayFormat = 'indices';
    }

    var generateArrayPrefix = arrayPrefixGenerators[arrayFormat];

    if (!objKeys) {
        objKeys = Object.keys(obj);
    }

    if (sort) {
        objKeys.sort(sort);
    }

    for (var i = 0; i < objKeys.length; ++i) {
        var key = objKeys[i];

        if (skipNulls && obj[key] === null) {
            continue;
        }

        keys = keys.concat(stringify(obj[key], key, generateArrayPrefix, strictNullHandling, skipNulls, encoder, filter, sort, allowDots));
    }

    return keys.join(delimiter);
};


/***/ }),
/* 48 */
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(global, process) {(function (global, undefined) {
    "use strict";

    if (global.setImmediate) {
        return;
    }

    var nextHandle = 1; // Spec says greater than zero
    var tasksByHandle = {};
    var currentlyRunningATask = false;
    var doc = global.document;
    var registerImmediate;

    function setImmediate(callback) {
      // Callback can either be a function or a string
      if (typeof callback !== "function") {
        callback = new Function("" + callback);
      }
      // Copy function arguments
      var args = new Array(arguments.length - 1);
      for (var i = 0; i < args.length; i++) {
          args[i] = arguments[i + 1];
      }
      // Store and register the task
      var task = { callback: callback, args: args };
      tasksByHandle[nextHandle] = task;
      registerImmediate(nextHandle);
      return nextHandle++;
    }

    function clearImmediate(handle) {
        delete tasksByHandle[handle];
    }

    function run(task) {
        var callback = task.callback;
        var args = task.args;
        switch (args.length) {
        case 0:
            callback();
            break;
        case 1:
            callback(args[0]);
            break;
        case 2:
            callback(args[0], args[1]);
            break;
        case 3:
            callback(args[0], args[1], args[2]);
            break;
        default:
            callback.apply(undefined, args);
            break;
        }
    }

    function runIfPresent(handle) {
        // From the spec: "Wait until any invocations of this algorithm started before this one have completed."
        // So if we're currently running a task, we'll need to delay this invocation.
        if (currentlyRunningATask) {
            // Delay by doing a setTimeout. setImmediate was tried instead, but in Firefox 7 it generated a
            // "too much recursion" error.
            setTimeout(runIfPresent, 0, handle);
        } else {
            var task = tasksByHandle[handle];
            if (task) {
                currentlyRunningATask = true;
                try {
                    run(task);
                } finally {
                    clearImmediate(handle);
                    currentlyRunningATask = false;
                }
            }
        }
    }

    function installNextTickImplementation() {
        registerImmediate = function(handle) {
            process.nextTick(function () { runIfPresent(handle); });
        };
    }

    function canUsePostMessage() {
        // The test against `importScripts` prevents this implementation from being installed inside a web worker,
        // where `global.postMessage` means something completely different and can't be used for this purpose.
        if (global.postMessage && !global.importScripts) {
            var postMessageIsAsynchronous = true;
            var oldOnMessage = global.onmessage;
            global.onmessage = function() {
                postMessageIsAsynchronous = false;
            };
            global.postMessage("", "*");
            global.onmessage = oldOnMessage;
            return postMessageIsAsynchronous;
        }
    }

    function installPostMessageImplementation() {
        // Installs an event handler on `global` for the `message` event: see
        // * https://developer.mozilla.org/en/DOM/window.postMessage
        // * http://www.whatwg.org/specs/web-apps/current-work/multipage/comms.html#crossDocumentMessages

        var messagePrefix = "setImmediate$" + Math.random() + "$";
        var onGlobalMessage = function(event) {
            if (event.source === global &&
                typeof event.data === "string" &&
                event.data.indexOf(messagePrefix) === 0) {
                runIfPresent(+event.data.slice(messagePrefix.length));
            }
        };

        if (global.addEventListener) {
            global.addEventListener("message", onGlobalMessage, false);
        } else {
            global.attachEvent("onmessage", onGlobalMessage);
        }

        registerImmediate = function(handle) {
            global.postMessage(messagePrefix + handle, "*");
        };
    }

    function installMessageChannelImplementation() {
        var channel = new MessageChannel();
        channel.port1.onmessage = function(event) {
            var handle = event.data;
            runIfPresent(handle);
        };

        registerImmediate = function(handle) {
            channel.port2.postMessage(handle);
        };
    }

    function installReadyStateChangeImplementation() {
        var html = doc.documentElement;
        registerImmediate = function(handle) {
            // Create a <script> element; its readystatechange event will be fired asynchronously once it is inserted
            // into the document. Do so, thus queuing up the task. Remember to clean up once it's been called.
            var script = doc.createElement("script");
            script.onreadystatechange = function () {
                runIfPresent(handle);
                script.onreadystatechange = null;
                html.removeChild(script);
                script = null;
            };
            html.appendChild(script);
        };
    }

    function installSetTimeoutImplementation() {
        registerImmediate = function(handle) {
            setTimeout(runIfPresent, 0, handle);
        };
    }

    // If supported, we should attach to the prototype of global, since that is where setTimeout et al. live.
    var attachTo = Object.getPrototypeOf && Object.getPrototypeOf(global);
    attachTo = attachTo && attachTo.setTimeout ? attachTo : global;

    // Don't get fooled by e.g. browserify environments.
    if ({}.toString.call(global.process) === "[object process]") {
        // For Node.js before 0.9
        installNextTickImplementation();

    } else if (canUsePostMessage()) {
        // For non-IE10 modern browsers
        installPostMessageImplementation();

    } else if (global.MessageChannel) {
        // For web workers, where supported
        installMessageChannelImplementation();

    } else if (doc && "onreadystatechange" in doc.createElement("script")) {
        // For IE 6–8
        installReadyStateChangeImplementation();

    } else {
        // For older browsers
        installSetTimeoutImplementation();
    }

    attachTo.setImmediate = setImmediate;
    attachTo.clearImmediate = clearImmediate;
}(typeof self === "undefined" ? typeof global === "undefined" ? this : global : self));

/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(1), __webpack_require__(3)))

/***/ }),
/* 49 */
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(global) {var scope = (typeof global !== "undefined" && global) ||
            (typeof self !== "undefined" && self) ||
            window;
var apply = Function.prototype.apply;

// DOM APIs, for completeness

exports.setTimeout = function() {
  return new Timeout(apply.call(setTimeout, scope, arguments), clearTimeout);
};
exports.setInterval = function() {
  return new Timeout(apply.call(setInterval, scope, arguments), clearInterval);
};
exports.clearTimeout =
exports.clearInterval = function(timeout) {
  if (timeout) {
    timeout.close();
  }
};

function Timeout(id, clearFn) {
  this._id = id;
  this._clearFn = clearFn;
}
Timeout.prototype.unref = Timeout.prototype.ref = function() {};
Timeout.prototype.close = function() {
  this._clearFn.call(scope, this._id);
};

// Does not start the time, just sets up the members needed.
exports.enroll = function(item, msecs) {
  clearTimeout(item._idleTimeoutId);
  item._idleTimeout = msecs;
};

exports.unenroll = function(item) {
  clearTimeout(item._idleTimeoutId);
  item._idleTimeout = -1;
};

exports._unrefActive = exports.active = function(item) {
  clearTimeout(item._idleTimeoutId);

  var msecs = item._idleTimeout;
  if (msecs >= 0) {
    item._idleTimeoutId = setTimeout(function onTimeout() {
      if (item._onTimeout)
        item._onTimeout();
    }, msecs);
  }
};

// setimmediate attaches itself to the global object
__webpack_require__(48);
// On some exotic environments, it's not clear which object `setimmediate` was
// able to install onto.  Search each possibility in the same order as the
// `setimmediate` library.
exports.setImmediate = (typeof self !== "undefined" && self.setImmediate) ||
                       (typeof global !== "undefined" && global.setImmediate) ||
                       (this && this.setImmediate);
exports.clearImmediate = (typeof self !== "undefined" && self.clearImmediate) ||
                         (typeof global !== "undefined" && global.clearImmediate) ||
                         (this && this.clearImmediate);

/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(1)))

/***/ }),
/* 50 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAu4AAAU2CAIAAABFtaRRAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjI3RkQ0MkJERjQ3RTExRTg4MjVEOUFFNUZGNkNCMDNCIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjI3RkQ0MkJFRjQ3RTExRTg4MjVEOUFFNUZGNkNCMDNCIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MjdGRDQyQkJGNDdFMTFFODgyNUQ5QUU1RkY2Q0IwM0IiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MjdGRDQyQkNGNDdFMTFFODgyNUQ5QUU1RkY2Q0IwM0IiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7h9lbqAAFhKUlEQVR42uzd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"

/***/ }),
/* 51 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADkAAABaCAYAAAACeP5xAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RjVCRDhGRjA2M0IxMTFFNzlFQTlGRkE5MzUwMDJFRjYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NzdGQkY1RDY2M0I5MTFFNzlFQTlGRkE5MzUwMDJFRjYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNUJEOEZFRTYzQjExMUU3OUVBOUZGQTkzNTAwMkVGNiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpGNUJEOEZFRjYzQjExMUU3OUVBOUZGQTkzNTAwMkVGNiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PvOrzCkAAAelSURBVHja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"

/***/ }),
/* 52 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "main"
  }, [_c('img', {
    staticClass: "img",
    attrs: {
      "src": _vm.bg,
      "alt": ""
    }
  }), _vm._v(" "), _c('div', {
    staticClass: "btn-foot",
    on: {
      "click": _vm.jumpUploadApp
    }
  }), _vm._v(" "), (_vm.isWeiXin) ? _c('div', {
    staticClass: "new-user-pop tigs-img-pop"
  }, [_c('img', {
    staticClass: "tigs-img",
    attrs: {
      "src": _vm.vImg,
      "alt": ""
    }
  }), _vm._v(" "), _c('div', {
    staticClass: "tigs-text"
  }, [_vm._v("请在浏览器打开下载")])]) : _vm._e()])
},staticRenderFns: []}

/***/ }),
/* 53 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    attrs: {
      "id": "app"
    }
  }, [_c('router-view')], 1)
},staticRenderFns: []}

/***/ }),
/* 54 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(42)
}
var Component = __webpack_require__(10)(
  /* script */
  __webpack_require__(41),
  /* template */
  __webpack_require__(52),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-17357e70",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 55 */
/***/ (function(module, exports) {

/* (ignored) */

/***/ })
/******/ ]);