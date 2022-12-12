/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2022 Sylvain Hallé
    Laboratoire d'informatique formelle
    Université du Québec à Chicoutimi, Canada

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.azrael.buffy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class ReflectiveSchema extends VariantSchema
{
	public ReflectiveSchema()
	{
		super();
		add(BooleanSchema.class, new BooleanReflectiveSchema());
		add(IntSchema.class, new IntReflectiveSchema());
		add(ByteArraySchema.class, new ByteArrayReflectiveSchema());
		add(StringBlobSchema.class, new StringBlobReflectiveSchema());
		add(SmallsciiSchema.class, new SmallsciiReflectiveSchema());
		add(BlobSchema.class, new BlobReflectiveSchema());
		add(ListSchema.class, new ListReflectiveSchema());
		add(FixedMapSchema.class, new FixedMapReflectiveSchema());
		add(VariantSchema.class, new VariantReflectiveSchema());
	}

	@Override
	public Schema read(BitSequence t) throws ReadException
	{
		return (Schema) super.read(t);
	}

	protected class ByteArrayReflectiveSchema implements Schema
	{
		protected ByteArrayReflectiveSchema()
		{
			super();
		}

		@Override
		public ByteArraySchema read(BitSequence t) throws ReadException
		{
			return ByteArraySchema.instance;
		}

		@Override
		public BitSequence print(Object o) throws PrintException
		{
			return new BitSequence();
		}
	}

	protected class ListReflectiveSchema implements Schema
	{
		protected ListReflectiveSchema()
		{
			super();
		}

		@Override
		public ListSchema read(BitSequence t) throws ReadException
		{
			Schema element_schema = ReflectiveSchema.this.read(t);
			return new ListSchema(element_schema);
		}

		@Override
		public BitSequence print(Object o) throws PrintException
		{
			if (!(o instanceof ListSchema))
			{
				throw new PrintException("Expected a ListSchema");
			}
			ListSchema s = (ListSchema) o;
			BitSequence bs = ReflectiveSchema.this.print(s.m_elementSchema);
			return bs;
		}
	}

	protected class FixedMapReflectiveSchema implements Schema
	{
		protected static final ListSchema s_listSchema = new ListSchema(StringBlobSchema.instance);

		protected FixedMapReflectiveSchema()
		{
			super();
		}

		@SuppressWarnings("unchecked")
		@Override
		public FixedMapSchema read(BitSequence t) throws ReadException
		{
			List<String> keys = (List<String>) s_listSchema.read(t);
			Schema value_type = ReflectiveSchema.this.read(t);
			return new FixedMapSchema(value_type, keys);
		}

		@Override
		public BitSequence print(Object o) throws PrintException
		{
			if (!(o instanceof FixedMapSchema))
			{
				throw new PrintException("Expected a FixedMapSchema");
			}
			FixedMapSchema s = (FixedMapSchema) o;
			BitSequence bs = s_listSchema.print(s.m_keys);
			bs.addAll(ReflectiveSchema.this.print(s.m_valueType));
			return bs;
		}
	}

	protected class VariantReflectiveSchema implements Schema
	{
		protected static final ListSchema s_classListSchema = new ListSchema(new ClassSchema(Boolean.class, Integer.class, String.class, List.class, Map.class));

		protected static final ListSchema s_blobListSchema = new ListSchema(BlobSchema.blob32);

		protected VariantReflectiveSchema()
		{
			super();
		}

		@SuppressWarnings("unchecked")
		@Override
		public VariantSchema read(BitSequence t) throws ReadException
		{
			List<Class<?>> class_names = (List<Class<?>>) s_classListSchema.read(t);
			List<BitSequence> schemas = (List<BitSequence>) s_blobListSchema.read(t);
			if (class_names.size() != schemas.size())
			{
				throw new ReadException("Lists do not have the same size");
			}
			VariantSchema ms = new VariantSchema();
			for (int i = 0; i < class_names.size(); i++)
			{
				BitSequence b_id;
				try
				{
					b_id = IntSchema.int4.print(i);
				}
				catch (PrintException e)
				{
					throw new ReadException(e);
				}
				Schema decoded_s = ReflectiveSchema.this.read(schemas.get(i));
				Class<?> c = class_names.get(i);
				ms.add(b_id, c, decoded_s);
			}
			return ms;
		}


		@Override
		public BitSequence print(Object o) throws PrintException
		{
			if (!(o instanceof VariantSchema))
			{
				throw new PrintException("Expected a VariantSchema");
			}
			VariantSchema s = (VariantSchema) o;
			List<Class<?>> class_names = new ArrayList<Class<?>>();
			List<BitSequence> schemas = new ArrayList<BitSequence>();
			for (int id = 0; id < s.m_schemaMap.size(); id++)
			{
				BitSequence b_id = IntSchema.int4.print(id);
				SchemaEntry entry = s.m_schemaMap.get(b_id);
				class_names.add(entry.m_class);
				schemas.add(ReflectiveSchema.this.print(entry.m_schema));
			}
			BitSequence bs = new BitSequence();
			bs.addAll(s_classListSchema.print(class_names));
			bs.addAll(s_blobListSchema.print(schemas));
			return bs;
		}
	}

	protected class BooleanReflectiveSchema implements Schema
	{
		protected BooleanReflectiveSchema()
		{
			super();
		}

		@Override
		public BooleanSchema read(BitSequence t) throws ReadException
		{
			return BooleanSchema.instance;
		}

		@Override
		public BitSequence print(Object o) throws PrintException
		{
			if (!(o instanceof BooleanSchema))
			{
				throw new PrintException("Expected a BooleanSchema");
			}
			return new BitSequence();
		}
	}

	protected class StringBlobReflectiveSchema implements Schema
	{
		protected StringBlobReflectiveSchema()
		{
			super();
		}

		@Override
		public StringBlobSchema read(BitSequence t) throws ReadException
		{
			return StringBlobSchema.instance;
		}

		@Override
		public BitSequence print(Object o) throws PrintException
		{
			if (!(o instanceof StringBlobSchema))
			{
				throw new PrintException("Expected a StringBlobSchema");
			}
			return new BitSequence();
		}
	}

	protected class SmallsciiReflectiveSchema implements Schema
	{
		protected SmallsciiReflectiveSchema()
		{
			super();
		}

		@Override
		public SmallsciiSchema read(BitSequence t) throws ReadException
		{
			return SmallsciiSchema.instance;
		}

		@Override
		public BitSequence print(Object o) throws PrintException
		{
			if (!(o instanceof SmallsciiSchema))
			{
				throw new PrintException("Expected a SmallsciiSchema");
			}
			return new BitSequence();
		}
	}

	protected class BlobReflectiveSchema implements Schema
	{
		protected BlobReflectiveSchema()
		{
			super();
		}

		@Override
		public BlobSchema read(BitSequence t) throws ReadException
		{
			return BlobSchema.blob32;
		}

		@Override
		public BitSequence print(Object o) throws PrintException
		{
			if (!(o instanceof BlobSchema))
			{
				throw new PrintException("Expected a BlobSchema");
			}
			return new BitSequence();
		}
	}

	protected class IntReflectiveSchema implements Schema
	{
		protected IntReflectiveSchema()
		{
			super();
		}

		@Override
		public IntSchema read(BitSequence t) throws ReadException
		{
			boolean signed = BooleanSchema.instance.read(t);
			int num_bits = IntSchema.int8.read(t).intValue();
			return new IntSchema(num_bits, signed);
		}

		@Override
		public BitSequence print(Object o) throws PrintException
		{
			if (!(o instanceof IntSchema))
			{
				throw new PrintException("Expected an IntSchema");
			}
			IntSchema s = (IntSchema) o;
			BooleanSchema.instance.print(s.m_signed);
			IntSchema.int8.print(s.m_numBits);
			return new BitSequence();
		}
	}

	public static class SelfDescribedSchema implements Schema
	{
		protected static final ReflectiveSchema s_reflectiveSchema = new ReflectiveSchema();

		protected final Schema m_schema;

		public SelfDescribedSchema(Schema s)
		{
			super();
			m_schema = s;
		}

		@Override
		public Object read(BitSequence t) throws ReadException
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BitSequence print(Object o) throws PrintException
		{
			BitSequence s = s_reflectiveSchema.print(m_schema);
			s.addAll(m_schema.print(o));
			return s;
		}

	}
}
