/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2019 Sylvain Hallé
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
package ca.uqac.lif.azrael;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Read handler that uses reflection to deserialize and populate the fields
 * of an object.
 * @author Sylvain Hallé
 *
 * @param <T> The type from which objects are deserialized
 */
public class ReflectionReadHandler<T> implements ReadHandler<T>
{
	/**
	 * The internal object reader
	 */
	protected ObjectReader<T> m_reader;
	
	/**
	 * Whether the access checks should be ignored. This is due to the
	 * fact that access methods can throw a <tt>InaccessibleObjectException</tt>
	 * in Java 9 onwards.
	 */
	protected boolean m_ignoreAccessChecks = false;

	/**
	 * Creates a new reflection read handler
	 * @param reader The internal object reader
	 */
	public ReflectionReadHandler(ObjectReader<T> reader)
	{
		super();
		m_reader = reader;
	}
	
	/**
	 * Sets whether the access checks should be ignored. This is due to the
	 * fact that access methods can throw a <tt>InaccessibleObjectException</tt>
	 * in Java 9 onwards.
	 * @param b <tt>true</tt> to ignore access checks, <tt>false</tt> otherwise
	 * (default)
	 */
	public void ignoreAccessChecks(boolean b)
	{
		m_ignoreAccessChecks = b;
	}
	
	/**
	 * Determines whether the access checks are be ignored by this handler.
	 * @return <tt>true</tt> if access checks are ignored, <tt>false</tt>
	 * otherwise
	 * @see ReflectionReadHandler#ignoreAccessChecks(boolean)
	 */
	public boolean ignoresAccessChecks()
	{
		return m_ignoreAccessChecks;
	}

	@Override
	public boolean canHandle(T o) throws ReadException 
	{
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object handle(T o) throws ReadException 
	{
		Class<?> clazz = m_reader.unwrapType(o);
		T contents = (T) m_reader.unwrapContents(o);
		Map<String,Object> contents_map = (Map<String,Object>) m_reader.read(contents);
		Object instance = m_reader.getInstance(clazz);
		return populateObject(instance, contents_map, clazz);
	}

	/**
	 * Populates an instance of an object based on its deserialized
	 * contents. That is, this method is expected to "fill" the object's
	 * member fields with deserialized data.
	 * @param o The object to use as the basis for the population
	 * @param contents The deserialized member fields of the object
	 * @param clazz The class this object should be an instance of
	 * @return A populated instance of the object. Note that this can
	 *   be a different instance than the one passed through <code>o</code>.
	 * @throws ReadException If the operation cannot be carried on
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object populateObject(Object o, Map<String,Object> contents, Class<?> clazz) throws ReadException
	{
		if (o == null)
		{
			return null;
		}
		for (String attribute : contents.keySet())
		{
			try
			{
				// Get the field associated with the map key and its declared type
				Field fld = ReflectionPrintHandler.getFromAllFields(attribute, clazz);
				fld.setAccessible(true);
				Object value_o = contents.get(attribute);
				if (fld.getType().isEnum())
				{
					if (!(value_o instanceof String))
					{
						throw new ReadException("The deserialized value of an enum field should be a string");
					}
					fld.set(o, Enum.valueOf((Class<Enum>) fld.getType(), (String) value_o));
				}
				else
				{
					fld.set(o, value_o);
				}
			}
			catch (NoSuchFieldException ex)
			{
				throw new ReadException(ex);
			}
			catch (IllegalAccessException ex)
			{
				throw new ReadException(ex);
			}
		}
		return o;
	}
}
