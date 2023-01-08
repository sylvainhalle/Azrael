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

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

/**
 * A schema encoding a bit sequence of variable length. The schema encodes
 * the sequence by first printing an unsigned integer specifying the
 * length of the bit sequence (in number of bits), followed by the bit
 * sequence itself. There are two variants of blobs: those using a 32-bit
 * integer as the length (storing at most 512 megabytes), and those using
 * 16-bit integers (storing at most 8 kilobytes).
 * 
 * @author Sylvain Hallé
 */
public class BlobSchema implements Schema
{
	/**
	 * A single publicly visible reference of the blob-32 schema.
	 */
	public static final BlobSchema blob32 = new BlobSchema(IntSchema.int32);
	
	/**
	 * A single publicly visible reference of the blob-16 schema.
	 */
	public static final BlobSchema blob16 = new BlobSchema(IntSchema.int16);
	
	/**
	 * The schema used to write the length of the blob.
	 */
	protected final IntSchema m_intSchema;
	
	/**
	 * Creates a new instance of the schema.
	 */
	protected BlobSchema(IntSchema s)
	{
		super();
		m_intSchema = s;
	}
	
	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (!(o instanceof BitSequence))
		{
			throw new PrintException("Expected a BitSequence");
		}
		BitSequence seq = (BitSequence) o;
		BitSequence out = m_intSchema.print(seq.size());
		out.addAll(seq);
		return out;
	}

	@Override
	public BitSequence read(Object o) throws ReadException
	{
		if (!(o instanceof BitSequence))
		{
			throw new ReadException("Expected a bit sequence");
		}
		BitSequence s = (BitSequence) o;
		long length = m_intSchema.read(s).longValue();
		if (s.size() < length)
		{
			throw new ReadException("Not enough bits to read");
		}
		return s.truncatePrefix(length);
	}

	@Override
	public String toString()
	{
		return "B";
	}
}