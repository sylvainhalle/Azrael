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

public class ByteArraySchema implements Schema
{
	public static final ByteArraySchema instance = new ByteArraySchema();
	
	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (o instanceof byte[])
		{
			byte[] bytes = (byte[]) o;
			try
			{
				BitSequence seq = new BitSequence(bytes, bytes.length * 8);
				return BlobSchema.instance.print(seq);
			}
			catch (BitFormatException e)
			{
				throw new PrintException(e);
			}
		}
		throw new PrintException("Expected a byte array");
	}
	
	@Override
	public byte[] read(BitSequence o) throws ReadException
	{
		BitSequence seq = (BitSequence) BlobSchema.instance.read(o);
		return seq.toByteArray();
	}
}
