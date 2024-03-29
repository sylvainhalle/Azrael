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

import ca.uqac.lif.azrael.ReadException;

/**
 * A schema encoding a single Boolean value.
 * 
 * @author Sylvain Hallé
 */
public class BooleanSchema extends EnumSchema
{
	/**
	 * A single publicly visible reference of this schema.
	 */
	/*@ non_null @*/ public static final BooleanSchema instance = new BooleanSchema();
	
	/**
	 * Creates a new instance of this schema.
	 */
	protected BooleanSchema()
	{
		super(false, true);
	}
	
	@Override
	public Boolean read(Object o) throws ReadException
	{
		return (Boolean) super.read(o);
	}
	
	@Override
	public String toString()
	{
		return "Bool";
	}
}
