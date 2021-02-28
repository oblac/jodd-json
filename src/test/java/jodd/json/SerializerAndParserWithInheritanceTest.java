package jodd.json;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SerializerAndParserWithInheritanceTest {

	interface Base2 {
	}

	public static class One implements Base2 {
		private String value = "one";

		public String getValue() {
			return value;
		}

		public void setValue(final String value) {
			this.value = value;
		}
	}

	public static class Two implements Base2 {
		private Long value = 222L;

		public Long getValue() {
			return value;
		}

		public void setValue(final Long value) {
			this.value = value;
		}
	}

	public static class Holder {
		private List<One> ones = new ArrayList<>();
		private List<Two> twos = new ArrayList<>();

		public List<One> getOnes() {
			return ones;
		}

		public void setOnes(final List<One> ones) {
			this.ones = ones;
		}

		public List<Two> getTwos() {
			return twos;
		}

		public void setTwos(final List<Two> twos) {
			this.twos = twos;
		}
	}

	@Test
	void testBackAndForthWithPlainJodd() {
		final JsonSerializer s = JsonSerializer.create();

		final Holder holder = new Holder();
		holder.getOnes().add(new One());
		holder.getTwos().add(new Two());

		final String json = s.deep(true).serialize(holder);

		assertEquals("{\"ones\":[{\"value\":\"one\"}],\"twos\":[{\"value\":222}]}", json);

		final Holder holderOut = JsonParser.create().useAltPaths()
				.map("ones.values", One.class)
				.map("Two.values", Two.class)
				.parse(json, Holder.class);

		assertEquals(1, holderOut.getOnes().size());
		assertTrue(holderOut.getOnes().get(0) instanceof One);
		assertEquals(1, holderOut.getTwos().size());
		assertTrue(holderOut.getTwos().get(0) instanceof Two);
	}

	@Test
	void testBackAndFortWithMetaData() {
		final List<Base2> list = new ArrayList<>();
		list.add(new One());
		list.add(new Two());

		final String json = JsonSerializer.create().withClassMetadata(true).serialize(list);

		final List<Base2> listout = JsonParser.create().withClassMetadata(true).parse(json);

		assertEquals(2, listout.size());
		assertTrue(listout.get(0) instanceof One);
		assertTrue(listout.get(1) instanceof Two);
	}


	@Test
	void testRegularSerializationAndList() {
		final JsonSerializer s = JsonSerializer.create();

		final List<Base2> list = new ArrayList<>();
		list.add(new One());
		list.add(new Two());

		final String json = s.serialize(list);
		assertEquals("[{\"value\":\"one\"},{\"value\":222}]", json);

		final List<Base2> listOut = JsonParser.create()
				.useAltPaths()
				.map("values[0]", One.class)
				.map("values[1]", Two.class)
				.parse(json);

		assertNotNull(listOut);
	}


	@Test
	void testSpecificSerialization() {
		final JsonSerializer s = createCustomJsonSerializer();

		final List<Base2> list = new ArrayList<>();
		list.add(new One());
		list.add(new Two());

		final String json = s.serialize(list);
		assertEquals("[[1,\"one\"],[2,222]]", json);

		final List<Base2> listOut = JsonParser.create()
				.useAltPaths()
				.withValueConverter("values", new ValueConverter() {
					@Override
					public Object convert(final Object source) {
						return null;
					}
				}).parse(json);

		assertNotNull(listOut);
	}



	private JsonSerializer createCustomJsonSerializer() {
		return JsonSerializer.create().withSerializer(Base2.class, (ctx, value) -> {
			ctx.writeOpenArray();
			if (value instanceof One) {
				ctx.writeNumber(1);
				ctx.writeComma();
				ctx.writeString(((One) value).getValue());
			}
			if (value instanceof Two) {
				ctx.writeNumber(2);
				ctx.writeComma();
				ctx.writeNumber(((Two) value).getValue());
			}
			ctx.writeCloseArray();
			return true;
		});
	}


}
