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
package ca.uqac.lif.azrael.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.azrael.ObjectReader;
import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.azrael.Readable;

public abstract class ObjectDeserializer implements ObjectReader
{
	public final Object read(Object t) throws ReadException
	{
		if (t == null)
		{
			throw new ReadException("Cannot deserialize from null");
		}
		if (isWrapped(t))
		{
			Class<?> clazz = unwrapType(t);
			Object in_t = unwrapContents(t);
			return readAs(clazz, in_t);
		}
		else
		{
			Class<?> clazz = t.getClass();
			return readAs(clazz, t);
		}
	}
	
	public Object readAs(Class<?> clazz, Object t) throws ReadException
	{
		if (Readable.class.isAssignableFrom(clazz))
		{
			Readable r = (Readable) getInstance(clazz);
			return r.read(this, t);
		}
		if (clazz.isAssignableFrom(Boolean.class))
		{
			return readBoolean(t);
		}
		if (clazz.isAssignableFrom(String.class))
		{
			return readString(t);
		}
		if (clazz.isAssignableFrom(Number.class))
		{
			return readNumber(t);
		}
		if (clazz.isAssignableFrom(List.class))
		{
			return readList(t);
		}
		if (clazz.isAssignableFrom(Map.class))
		{
			return readMap(t);
		}
		throw new ReadException("Cannot deserialize");
	}
	
	protected abstract Class<?> unwrapType(Object t) throws ReadException;
	
	protected abstract Object unwrapContents(Object t) throws ReadException;
	
	protected abstract boolean isWrapped(Object t);
	
	/**
	 * Produces an instance of an object based on its deserialized contents
	 * and a target class. This method implements the default technique
	 * for obtaining an instance of an object:
	 * just ask for an instance based on the class, using the
	 * <code>newInstance()</code> method.
	 * @param clazz The target class for the object
	 * @return An instance of the object
	 * @throws ReadException If the operation cannot be carried on
	 */
	protected Object getInstance(Class<?> clazz) throws ReadException
	{
		if (clazz == null)
		{
			return null;
		}
		// Instantiate "empty" object of the target class
		Object o = null;
		
		try
		{
			Class<?>[] params = new Class[0];
			Constructor<?> c = clazz.getDeclaredConstructor(params);
			c.setAccessible(true);
			Object[] args = new Object[0];
			o = c.newInstance(args);
			//o = clazz.newInstance();
		} 
		catch (InstantiationException ex)
		{
			throw new ReadException(ex);
		} 
		catch (IllegalAccessException ex)
		{
			throw new ReadException(ex);
		} 
		catch (NoSuchMethodException ex) 
		{
			throw new ReadException(ex);
		}
		catch (SecurityException ex)
		{
			throw new ReadException(ex);
		}
		catch (IllegalArgumentException ex)
		{
			throw new ReadException(ex);
		}
		catch (InvocationTargetException ex)
		{
			throw new ReadException(ex);
		}
		return o;
}
}
