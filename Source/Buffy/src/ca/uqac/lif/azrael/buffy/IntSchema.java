/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2023 Sylvain Hallé
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

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class IntSchema implements Schema
{
	public static final BuffyUnsignedInt64Schema int64 = new BuffyUnsignedInt64Schema();
	
	public static final BuffyUnsignedInt32Schema int32 = new BuffyUnsignedInt32Schema();
	
	public static final BuffyUnsignedInt16Schema int16 = new BuffyUnsignedInt16Schema();
	
	public static final BuffyUnsignedInt8Schema int8 = new BuffyUnsignedInt8Schema();
	
	public static final BuffyUnsignedInt4Schema int4 = new BuffyUnsignedInt4Schema();
	
	protected final int m_numBits;

	protected final boolean m_signed;
	
	protected static final double LG2 = Math.log(2);
	
	/**
	 * Gets an instance of IntSchema that is just wide enough to encode a
	 * given number of values.
	 * @param num_values The number of values
	 * @return The int schema
	 */
	public static IntSchema getSchemaFor(int num_values)
	{
		int num_bits = (int) Math.ceil(Math.log(num_values) / LG2);
		return new IntSchema(num_bits, false);
	}

	public IntSchema(int num_bits, boolean signed)
	{
		super();
		m_numBits = num_bits;
		m_signed = signed;
	}
	
	public int getWidth()
	{
		return m_numBits;
	}
	
	public boolean isSigned()
	{
		return m_signed;
	}

	public boolean checkValidValue(long v)
	{
		long max, min;
		if (m_signed)
		{
			max = (long) Math.pow(2, m_numBits - 1) - 1;
			min = -max;
		}
		else
		{
			max = (long) Math.pow(2, m_numBits) - 1;
			min = 0;
		}
		return v <= max && v >= min;
	}

	@Override
	public int hashCode()
	{
		return m_numBits * (m_signed ? 2 : 3);
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof IntSchema))
		{
			return false;
		}
		IntSchema bis = (IntSchema) o;
		return m_numBits == bis.m_numBits && m_signed == bis.m_signed;
	}

	@Override
	public String toString()
	{
		return (m_signed ? "SI[" : "UI[") + m_numBits + "]";
	}

	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (m_numBits == 0)
		{
			// Don't print anything
			return new BitSequence();
		}
		if (!(o instanceof Integer) && !(o instanceof Long))
		{
			throw new PrintException("Expected an Integer or a Long");
		}
		long bi = ((Number) o).longValue();
		BitSequence bs = new BitSequence();
		try
		{
			if (!m_signed)
			{
				BitSequence new_bs = new BitSequence(bi, m_numBits);
				bs.addAll(new_bs);
			}
			else
			{
				if (bi < 0)
				{
					bs.addAll(new BitSequence("1")); // First bit indicates sign
				}
				else
				{
					bs.addAll(new BitSequence("0"));
				}
				// Encode absolute value over remaining bits
				bs.addAll(new BitSequence(Math.abs(bi), m_numBits - 1));
			}
		}
		catch (BitFormatException e)
		{
			throw new PrintException(e);
		}
		return bs;
	}

	@Override
	public Number read(Object o) throws ReadException
	{
		if (!(o instanceof BitSequence))
		{
			throw new ReadException("Expected a bit sequence");
		}
		if (m_numBits == 0)
		{
			// Don't need to read anything
			return 0;
		}
		BitSequence bs = (BitSequence) o;
		long i_value = 0;
		if (m_numBits > bs.size())
		{
			throw new ReadException("Not enough bytes to read");
		}
		if (m_signed)
		{
			// Number is signed: read first bit to get sign
			BitSequence sign = bs.truncatePrefix(1);
			int multiple = 1;
			if (sign.intValue() == 1)
			{
				multiple = -1;
			}
			BitSequence value = bs.truncatePrefix(m_numBits - 1);
			i_value = multiple * value.longValue();
		}
		else
		{
			BitSequence int_v = bs.truncatePrefix(m_numBits);
			i_value = int_v.longValue();
		}
		return getBuffyInt(m_numBits, m_signed, i_value);
	}

	protected Number getBuffyInt(int num_bits, boolean signed, long value)
	{
		return (long) value;
	}
	
	public static class BuffyUnsignedInt64Schema extends IntSchema
	{
		protected BuffyUnsignedInt64Schema()
		{
			super(64, false);
		}
	}
	
	public static class BuffyUnsignedInt32Schema extends IntSchema
	{
		protected BuffyUnsignedInt32Schema()
		{
			super(32, false);
		}
	}
	
	public static class BuffyUnsignedInt16Schema extends IntSchema
	{
		protected BuffyUnsignedInt16Schema()
		{
			super(16, false);
		}
	}
	
	public static class BuffyUnsignedInt8Schema extends IntSchema
	{
		protected BuffyUnsignedInt8Schema()
		{
			super(8, false);
		}
	}
	
	public static class BuffyUnsignedInt4Schema extends IntSchema
	{
		protected BuffyUnsignedInt4Schema()
		{
			super(4, false);
		}
	}
}