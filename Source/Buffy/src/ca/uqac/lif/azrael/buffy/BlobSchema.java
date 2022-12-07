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

public class BlobSchema implements Schema
{
	public static final BlobSchema instance = new BlobSchema();
	
	protected BlobSchema()
	{
		super();
	}
	
	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (!(o instanceof BitSequence))
		{
			throw new PrintException("Expected a BitSequence");
		}
		BitSequence seq = (BitSequence) o;
		BitSequence out = IntSchema.int32.print(seq.size());
		out.addAll(seq);
		return out;
	}

	@Override
	public BitSequence read(BitSequence s) throws ReadException
	{
		long length = IntSchema.int32.read(s).longValue();
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