/*
 * Copyright 2012 Shared Learning Collaborative, LLC
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


package org.slc.sli.modeling.xdm;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public final class DmTextTest {

	@Test
	public void testConstructorAndGetters() {
		
		String value = "foo";
		
		DmText dmText = new DmText(value);
		
		assertTrue(dmText.getName() == DmText.NO_NAME);
		assertTrue(dmText.getChildAxis().size() == 0);
		assertTrue(dmText.getStringValue() == value);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullValueForConstructorThrowsException() {
		
		new DmText(null);
	}
}
