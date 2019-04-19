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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class ObjectReader<T>
{
	/**
	 * Additional class loaders
	 */
	protected Set<ClassLoader> m_classLoaders;
	
	protected List<ReadHandler<T>> m_handlers;
	
	protected ReadHandler<T> m_reflectionHandler = new ReflectionReadHandler<T>(this);
	
	public ObjectReader()
	{
		super();
		m_handlers = new ArrayList<ReadHandler<T>>();
	}

	/**
	 * Deserializes the content of an object
	 * @param o The serialized contents of the object
	 * @return The deserialized object
	 * @throws ReadException Thrown if deserialization produced an error
	 */
	@SuppressWarnings("unchecked")
	public Object read(Object t) throws ReadException
	{
		if (t == null)
		{
			throw new ReadException("Cannot deserialize from null");
		}
		if (isWrapped(t))
		{
			Class<?> clazz = unwrapType(t);
			if (Readable.class.isAssignableFrom(clazz))
			{
				Object o = unwrapContents(t);
				Readable r = (Readable) getInstance(clazz);
				return r.read(this, o);
			}
		}
		for (ReadHandler<T> handler : m_handlers)
		{
			if (handler.canHandle((T) t))
			{
				return handler.handle((T) t);
			}
		}
		return m_reflectionHandler.handle((T) t);
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
	public Object getInstance(Class<?> clazz) throws ReadException
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
	
	/**
	 * Adds a new class loader used to create new class instances 
	 * @param cl The class loader
	 */
	public void addClassLoader(ClassLoader cl)
	{
		m_classLoaders.add(cl);
	}

	public Class<?> findClass(String class_name) throws ClassNotFoundException
	{
		Class<?> candidate = null;
		try
		{
			// Try first with the bootstrap class loader
			candidate = Class.forName(class_name, true, null);
		}
		catch (ClassNotFoundException e)
		{
			// Do nothing
		}
		for (ClassLoader cl : m_classLoaders)
		{
			// Go through all other class loaders, if any
			if (candidate != null)
			{
				break;
			}
			try
			{
				candidate = Class.forName(class_name, true, cl);
			} 
			catch (ClassNotFoundException e)
			{
				// Do nothing
			}
		}
		if (candidate == null)
		{
			throw new ClassNotFoundException("Class " + class_name + " not found");
		}
		return candidate;
	}

}