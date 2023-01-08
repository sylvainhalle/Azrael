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
 * The schema that prints nothing and returns <tt>null</tt> when read.
 * Not to be confused with the {@link NullableSchema}.
 * 
 * @author Sylvain Hallé
 */
public class NullSchema implements Schema
{
	/**
	 * A single publicly visible reference to the null schema.
	 */
	public static final NullSchema instance = new NullSchema();
	
	/**
	 * Creates a new null schema.
	 */
	protected NullSchema()
	{
		super();
	}
	
	@Override
	public Object read(Object o) throws ReadException
	{
		return null;
	}

	@Override
	public BitSequence print(Object o) throws PrintException
	{
		return new BitSequence();
	}
	
	@Override
	public String toString()
	{
		return "N";
	}
}
