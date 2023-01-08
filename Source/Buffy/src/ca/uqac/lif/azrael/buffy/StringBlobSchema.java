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
 * Encodes a string directly as bytes.
 * @author Sylvain Hallé
 */
public class StringBlobSchema extends StringSchema implements Schema
{
	/**
	 * A single publicly visible reference of this schema.
	 */
	/*@ non_null @*/ public static final StringBlobSchema instance = new StringBlobSchema();

	@Override
	public BitSequence print(Object o) throws PrintException
	{
		byte[] bytes = o.toString().getBytes();
		try
		{
			return BlobSchema.blob16.print(new BitSequence(bytes));
		}
		catch (BitFormatException e)
		{
			throw new PrintException(e);
		}
	}

	@Override
	public String read(Object ob) throws ReadException
	{
		if (!(ob instanceof BitSequence))
		{
			throw new ReadException("Expected a bit sequence");
		}
		BitSequence o = (BitSequence) ob;
		byte[] bytes = BlobSchema.blob16.read(o).toByteArray();
		return new String(bytes);
	}
}
