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

/**
 * The schema for an object that can either take some value, or be null. In
 * this case, the null value is represented by the single bit 0, while the
 * non-null value is represented by the bit 1, followed by the serialization of
 * the object itself. Not to be confused with the {@link NullSchema}.
 *   
 * @author Sylvain Hallé
 */
public class NullableSchema extends VariantSchema
{
	public NullableSchema(Schema s)
	{
		super();
		add(new NullSchemaEntry());
		add(new NonNullSchemaEntry(s));
	}
	
	protected static class NullSchemaEntry extends SchemaEntry
	{
		public NullSchemaEntry()
		{
			super(null, NullSchema.instance);
		}
		
		@Override
		public boolean matches(Object o)
		{
			return o == null;
		}
	}
	
	protected static class NonNullSchemaEntry extends SchemaEntry
	{
		public NonNullSchemaEntry(Schema s)
		{
			super(null, s);
		}
		
		@Override
		public boolean matches(Object o)
		{
			return o != null;
		}
	}
}
