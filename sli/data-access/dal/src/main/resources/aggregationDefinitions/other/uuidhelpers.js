/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


makeID = function(uuid) {
	JUUID = function(uuid) {
	    var hex = uuid.replace(/[{}-]/g, ""); // remove extra characters
	    var msb = hex.substr(0, 16);
	    var lsb = hex.substr(16, 16);
	    msb = msb.substr(14, 2) + msb.substr(12, 2) + msb.substr(10, 2) + msb.substr(8, 2) + msb.substr(6, 2) + msb.substr(4, 2) + msb.substr(2, 2) + msb.substr(0, 2);
	    lsb = lsb.substr(14, 2) + lsb.substr(12, 2) + lsb.substr(10, 2) + lsb.substr(8, 2) + lsb.substr(6, 2) + lsb.substr(4, 2) + lsb.substr(2, 2) + lsb.substr(0, 2);
	    hex = msb + lsb;
	    var base64 = HexToBase64(hex);
	    return new BinData(3, base64);
	}
	
	HexToBase64 = function(hex) {
	    var base64Digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	    var base64 = "";
	    var group;
	    for (var i = 0; i < 30; i += 6) {
	        group = parseInt(hex.substr(i, 6), 16);
	        base64 += base64Digits[(group >> 18) & 0x3f];
	        base64 += base64Digits[(group >> 12) & 0x3f];
	        base64 += base64Digits[(group >> 6) & 0x3f];
	        base64 += base64Digits[group & 0x3f];
	    }
	    group = parseInt(hex.substr(30, 2), 16);
	    base64 += base64Digits[(group >> 2) & 0x3f];
	    base64 += base64Digits[(group << 4) & 0x3f];
	    base64 += "==";
	    return base64;
	}
	return JUUID(uuid)
}

db.system.js.save({ "_id" : "makeID", "value" : makeID })
