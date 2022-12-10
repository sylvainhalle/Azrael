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

public class SelfDescribedObject
{
	public static final SelfDescribedObjectSchema schema = new SelfDescribedObjectSchema();
	
	protected final Schema m_schema;
	
	protected final Object m_object;
	
	public SelfDescribedObject(Schema s, Object o)
	{
		super();
		m_schema = s;
		m_object = o;
	}
	
	public Object getObject()
	{
		return m_object;
	}
	
	public Schema getSchema()
	{
		return m_schema;
	}
	
	public static class SelfDescribedObjectSchema implements Schema
	{
		protected static final ReflectiveSchema s_reflectiveSchema = new ReflectiveSchema();
		
		@Override
		public SelfDescribedObject read(BitSequence t) throws ReadException
		{
			Schema o_s = s_reflectiveSchema.read(t);
			Object o = o_s.read(t);
			return new SelfDescribedObject(o_s, o);
		}

		@Override
		public BitSequence print(Object o) throws PrintException
		{
			if (!(o instanceof SelfDescribedObject))
			{
				throw new PrintException("Expected a SelfDescribedObject");
			}
			SelfDescribedObject sdo = (SelfDescribedObject) o;
			Schema o_s = sdo.getSchema();
			BitSequence seq = s_reflectiveSchema.print(o_s);
			seq.addAll(o_s.print(sdo.getObject()));
			return seq;
		}
		
	}
}
