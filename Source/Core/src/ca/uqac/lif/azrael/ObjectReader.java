/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2025 Sylvain Hallé
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reads the content of an object in a given format, to recreate the
 * corresponding instance.
 * @author Sylvain Hallé
 *
 * @param <T> The format to which the object has been written
 */
public abstract class ObjectReader<T> implements AzraelReader<T>
{
	/**
	 * Additional class loaders
	 */
	protected Set<ClassLoader> m_classLoaders;

	/**
	 * A list of objects that handle the printing of objects of various
	 * types
	 */
	protected List<ReadHandler<T>> m_handlers;

	/**
	 * The default handler to use when no other accepts an object
	 */
	protected ReflectionReadHandler<T> m_reflectionHandler = new ReflectionReadHandler<T>(this);

	/**
	 * Creates a new object reader
	 */
	public ObjectReader()
	{
		super();
		m_handlers = new ArrayList<ReadHandler<T>>();
		m_classLoaders = new HashSet<ClassLoader>();
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
		m_reflectionHandler.ignoreAccessChecks(b);
	}

	/**
	 * Deserializes the content of an object.
	 * @param t The serialized contents of the object
	 * @return The deserialized object
	 * @throws ReadException Thrown if deserialization produced an error
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object read(Object t) throws ReadException
	{
		if (t != null && isWrapped(t))
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
		if (t == null)
		{
			throw new ReadException("Cannot deserialize from null");
		}
		return m_reflectionHandler.handle((T) t);
	}
	
	protected abstract String getWrappedTypeName(Object t) throws ReadException;

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
		catch (RuntimeException e)
		{
			// Must check exception by its name, as the actual class only exists in Java 9+
			if (e.getClass().getSimpleName().contains("InaccessibleObjectException") && m_reflectionHandler.ignoresAccessChecks())
			{
				// Do nothing
			}
			else
			{
				throw new ReadException(e);
			}
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

	/**
	 * Finds a class by its name
	 * @param class_name The name of the class
	 * @return The class
	 * @throws ClassNotFoundException Thrown if the class could not be found
	 */
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

	/**
	 * Sets the value of a field in an object
	 * @param o The object
	 * @param field_name The name of the field to set
	 * @param value The value to set
	 * @throws ReadException Thrown if the value cannot be assigned to this field
	 */
	public void setField(Object o, String field_name, Object value) throws ReadException
	{
		if (o == null)
		{
			return;
		}
		try
		{
			Field fld = ReflectionPrintHandler.getFromAllFields(field_name, o.getClass());
			fld.setAccessible(true);
			fld.set(o, value);
		}
		catch (NoSuchFieldException e)
		{
			throw new ReadException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new ReadException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new ReadException(e);
		}
		catch (RuntimeException e)
		{
			// Must check exception by its name, as the actual class only exists in Java 9+
			if (e.getClass().getSimpleName().contains("InaccessibleObjectException") && m_reflectionHandler.ignoresAccessChecks())
			{
				// Nothing to do
			}
			else
			{
				throw new ReadException(e);
			}
		}
	}
	
	/**
	 * Adds a handler to the reader
	 * @param h The handler
	 */
	public void addHandler(ReadHandler<T> h)
	{
		m_handlers.add(h);
	}
}
