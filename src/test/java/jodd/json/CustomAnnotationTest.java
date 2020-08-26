// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.json;

import jodd.json.fixtures.JsonParsers;
import jodd.json.fixtures.mock.LocationAlt;
import jodd.json.meta.JSON;
import jodd.json.meta.JsonAnnotationManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomAnnotationTest {

	@BeforeEach
	void setUp() {
		JsonAnnotationManager.get().setJsonAnnotation(JSON2.class);
	}

	@AfterEach
	void tearDown() {
		JsonAnnotationManager.get().setJsonAnnotation(JSON.class);
	}

	@Test
	void testAnnName() {
		JsonParsers.forEachParser(jsonParser -> {
			LocationAlt location = new LocationAlt();

			location.setLatitude(65);
			location.setLongitude(12);

			String json = new JsonSerializer().serialize(location);

			assertEquals("{\"lat\":65,\"lng\":12}", json);

			LocationAlt jsonLocation = jsonParser.parse(json, LocationAlt.class);

			assertEquals(location.getLatitude(), jsonLocation.getLatitude());
			assertEquals(location.getLongitude(), jsonLocation.getLongitude());
		});
	}

	@Test
	void testAnnNameWithClass() {
		JsonParsers.forEachParser(jsonParser -> {
			LocationAlt location = new LocationAlt();

			location.setLatitude(65);
			location.setLongitude(12);

			String json = new JsonSerializer().setClassMetadataName("class").serialize(location);

			assertEquals("{\"lat\":65,\"lng\":12}", json);

			LocationAlt jsonLocation = jsonParser.setClassMetadataName("class").parse(json, LocationAlt.class);

			assertEquals(location.getLatitude(), jsonLocation.getLatitude());
			assertEquals(location.getLongitude(), jsonLocation.getLongitude());
		});
	}

}