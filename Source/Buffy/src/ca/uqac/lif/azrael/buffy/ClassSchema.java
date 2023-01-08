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

import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class ClassSchema extends EnumSchema
{
	/*@ non_null @*/ protected static final StringSchema s_stringSchema = StringBlobSchema.instance;
	
	public ClassSchema(Class<?> ... classes)
	{
		super((Object[]) classes);
	}
	
	@Override
	public Class<?> read(Object o) throws ReadException
	{
		if (!(o instanceof BitSequence))
		{
			throw new ReadException("Expected a bit sequence");
		}
		BitSequence t = (BitSequence) o;
		BitSequence is_shortcut = t.truncatePrefix(1);
		if (is_shortcut.get(0))
		{
			return (Class<?>) super.read(t);
		}
		else
		{
			String class_name = s_stringSchema.read(t);
			try
			{
				return Class.forName(class_name);
			}
			catch (ClassNotFoundException e)
			{
				throw new ReadException(e);
			}
		}
	}

	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (!(o instanceof Class))
		{
			throw new PrintException("Expected a class");
		}
		Class<?> c = (Class<?>) o;
		BitSequence bs = new BitSequence();
		for (Map.Entry<Object,BitSequence> e : m_toBits.entrySet())
		{
			Class<?> target_c = (Class<?>) e.getKey();
			if (target_c.isAssignableFrom(c))
			{
				bs.add(true);
				bs.addAll(e.getValue());
				return bs;
			}
		}
		bs.add(false);
		System.out.println(o);
		bs.addAll(s_stringSchema.print(c.getName()));
		return bs;
	}
}
