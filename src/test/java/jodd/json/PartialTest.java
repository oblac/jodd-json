package jodd.json;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PartialTest {

	public static class MyMappingClass {
		public String something;
		public String somemore;
	}

	@Test
	void testMapArray() {
		final String json = "{" +
				"\"field1\": [" +
				"{" +
				"\"something\": \"else\"," +
				"\"somemore\": \"yesplease\"" +
				"}" +
				"]," +
				"\"field2\": true," +
				"\"field3\": 1" +
				"}";

		final Map<String, Object> m = new JsonParser()
				.map("values.values", MyMappingClass.class)
				.parse(json);

		final List<MyMappingClass> mm = (List<MyMappingClass>) m.get("field1");

		assertEquals(1, mm.size());
		assertEquals("else", mm.get(0).something);
		assertEquals("yesplease", mm.get(0).somemore);
	}

	@Test
	void testMapArrayAlt() {
		final String json = "{" +
				"\"field1\": [" +
				"{" +
				"\"something\": \"else\"," +
				"\"somemore\": \"yesplease\"" +
				"}" +
				"]," +
				"\"field2\": true," +
				"\"field3\": 1" +
				"}";

		final Map<String, Object> m = new JsonParser()
				.map("field1.values", MyMappingClass.class)
				.useAltPaths()
				.parse(json);

		final List<MyMappingClass> mm = (List<MyMappingClass>) m.get("field1");

		assertEquals(1, mm.size());
		assertEquals("else", mm.get(0).something);
		assertEquals("yesplease", mm.get(0).somemore);
	}


	@Test
	void testMap() {
		final String json = "{" +
				"\"field1\":" +
				"{" +
				"\"something\": \"else\"," +
				"\"somemore\": \"yesplease\"" +
				"}}";

		final Map<String, Object> map = new JsonParser()
				.map("values", MyMappingClass.class)
				.parse(json);

		final MyMappingClass mm = (MyMappingClass) map.get("field1");
	}
}
