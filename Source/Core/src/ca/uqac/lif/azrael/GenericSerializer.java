/*
    A serializer for Java objects
    Copyright (C) 2016 Sylvain Hallé
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Serializes Java objects into elements of some type T
 * @author sylvain
 *
 * @param <T> The type to serialize
 */
public abstract class GenericSerializer<T> implements Serializer<T>
{
	protected List<PrimitiveHandler<T>> m_primitiveHandlers;

	protected List<Handler<T>> m_compoundHandlers;
	
	protected Set<ClassLoader> m_classLoaders;
	
	/**
	 * The version number of this library
	 */
	protected static String s_versionString = "0.7.1";
	
	/**
	 * Gets the version number of this library
	 * @return The version number
	 */
	public static String getVersionString()
	{
		return s_versionString;
	}
	
	public GenericSerializer()
	{
		super();
		m_compoundHandlers = new ArrayList<Handler<T>>();
		m_primitiveHandlers = new ArrayList<PrimitiveHandler<T>>();
		m_primitiveHandlers.add(getStringHandler());
		m_primitiveHandlers.add(getNumberHandler());
		m_primitiveHandlers.add(getBooleanHandler());
		m_primitiveHandlers.add(getEnumHandler());
		m_classLoaders = new HashSet<ClassLoader>();
	}

	public GenericSerializer<T> addPrimitiveHandler(int position, PrimitiveHandler<T> h)
	{
		if (position < 0)
		{
			position = 0;
		}
		if (position >= m_primitiveHandlers.size())
		{
			position = m_primitiveHandlers.size();
		}
		m_primitiveHandlers.add(position, h);
		return this;
	}
	
	/**
	 * Adds a new class loader used to create new class instances 
	 * @param cl The class loader
	 */
	public void addClassLoader(ClassLoader cl)
	{
		m_classLoaders.add(cl);
	}
	
	@Override
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

	public GenericSerializer<T> addObjectHandler(int position, Handler<T> h)
	{
		if (position < 0)
		{
			position = 0;
		}
		if (position >= m_compoundHandlers.size())
		{
			position = m_compoundHandlers.size();
		}
		m_compoundHandlers.add(position, h);
		return this;
	}

	/**
	 * Serializes an object into an element of type T
	 * @param o The object to serialize
	 * @return A serialized object of type T
	 * @throws SerializerException If something goes wrong
	 */
	public T serialize(Object o) throws SerializerException
	{
		return serializeAs(o, o.getClass());
	}

	/**
	 * Serializes an object into an element of type T
	 * @param o The object to serialize
	 * @param clazz The definition of the type
	 * @return A serialized object of type T
	 * @throws SerializerException If something goes wrong
	 */
	public T serializeAs(Object o, Class<?> clazz) throws SerializerException
	{
		if (o == null)
		{
			Handler<T> nh = getNullHandler();
			return nh.serialize(o);
		}
		Class<?> o_class = GenericSerializer.getWrapperClass(o.getClass());
		clazz = getWrapperClass(clazz);
		// Do we need to wrap type info?
		if (!o_class.equals(clazz))
		{
			// Serialize as the exact type
			T serialization = serializeAs(o, o.getClass());
			// Then wrap type information around it
			T out = wrapTypeInfo(o, serialization);
			return out;
		}
		// Is the object a primitive?
		for (PrimitiveHandler<T> h : m_primitiveHandlers)
		{
			if (h.appliesTo(clazz))
			{
				return h.serialize(o);
			}
		}
		// Find an object handler that would like to take care of this object
		T e_serialized = null;
		for (Handler<T> h : m_compoundHandlers)
		{
			if (h.appliesTo(clazz))
			{
				e_serialized = h.serializeAs(o, clazz);
				break;
			}
		}
		// No handler? Last resort: try the default object handler
		if (e_serialized == null)
		{
			ObjectHandler<T> h = getDefaultObjectHandler();
			e_serialized = h.serializeAs(o, clazz);      
		}
		if (e_serialized == null)
		{
			throw new SerializerException("No handler wants to serialize an object of class " + clazz.getSimpleName());
		}
		return e_serialized; 
	}

	public Object deserializeAs(T e, Class<?> clazz) throws SerializerException
	{
		if (clazz == null)
		{
			return null;
		}
		if (e == null)
		{
			NullHandler<T> nh = getNullHandler();
			return nh.deserializeAs(e, clazz);
		}
		// Does the object contain wrapped type information
		TypeInfo<T> type_info = null;
		try
		{
			type_info = getTypeInfo(e, clazz);
		}
		catch (ClassNotFoundException ex)
		{
			throw new SerializerException(ex);
		}
		assert type_info != null;
		if (type_info.clazz == null)
		{
			return null;
		}
		// Is the object a primitive?
		clazz = getWrapperClass(type_info.clazz);
		for (PrimitiveHandler<T> h : m_primitiveHandlers)
		{
			if (h.appliesTo(clazz))
			{
				return h.deserializeAs(unwrapTypeInfo(type_info.e), clazz);
			}
		}
		// Then the object must be compound. Ask a handler
		for (Handler<T> h : m_compoundHandlers)
		{
			if (h.appliesTo(clazz))
			{
				return h.deserializeAs(e, clazz);
			}
		}
		// No handler? Try the default compound handler
		ObjectHandler<T> h = getDefaultObjectHandler();
		return h.deserializeAs(e, clazz);
	}

	/**
	 * Returns a handler for serializing String primitives
	 * @return The handler
	 */
	protected abstract StringHandler<T> getStringHandler();

	/**
	 * Returns a handler for serializing Number primitives
	 * @return The handler
	 */
	protected abstract NumberHandler<T> getNumberHandler();

	/**
	 * Returns a handler for serializing Boolean primitives
	 * @return The handler
	 */
	protected abstract BooleanHandler<T> getBooleanHandler();
	
	/**
	 * Returns a handler for serializing null values
	 * @return The handler
	 */
	protected abstract NullHandler<T> getNullHandler();
	
	/**
	 * Returns a handler for serializing enumerations
	 * @return The handler
	 */
	protected abstract EnumHandler<T> getEnumHandler();

	/**
	 * Returns a handler for serializing non-primitive objects
	 * @return The handler
	 */
	protected abstract ObjectHandler<T> getDefaultObjectHandler();

	public static Class<?> getWrapperClass(Class<?> clazz)
	{
		if (clazz == null)
		{
			return null;
		}
		String class_name = clazz.getName();
		if (class_name.compareTo("int") == 0)
		{
			return Integer.class;
		}
		if (class_name.compareTo("boolean") == 0)
		{
			return Boolean.class;
		}
		if (class_name.compareTo("float") == 0)
		{
			return Float.class;
		}
		if (class_name.compareTo("long") == 0)
		{
			return Long.class;
		}
		if (class_name.compareTo("double") == 0)
		{
			return Double.class;
		}
		if (class_name.compareTo("short") == 0)
		{
			return Short.class;
		}
		// Otherwise, just return the class itself
		return clazz;
	}

	public static List<Field> getAllFields(Class<?> type)
	{
		List<Field> fields = getAllFields(new LinkedList<Field>(), type);
		Iterator<Field> it = fields.iterator();
		while (it.hasNext())
		{
			Field f = it.next();
			if (f.getName().startsWith("$") || f.getName().startsWith(("!")))
			{
				it.remove();
			}
		}
		return fields;
	}

	protected static List<Field> getAllFields(List<Field> fields, Class<?> type)
	{
		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		if (type.getSuperclass() != null)
		{
			fields = getAllFields(fields, type.getSuperclass());
		}
		return fields;
	}

	public static Field getFromAllFields(String name, Class<?> type) throws NoSuchFieldException
	{
		List<Field> fields = getAllFields(type);
		for (Field f : fields)
		{
			if (f.getName().compareTo(name) == 0)
			{
				return f;
			}
		}
		throw new NoSuchFieldException();
	}

	/**
	 * Adds to the serialization of o information about its precise type
	 * @param o The object to serialize
	 * @param in The serialized contents of o
	 * @return The serialized contents of o, modified to contain
	 *   type information
	 */
	public abstract T wrapTypeInfo(Object o, T in);

	/**
	 * Removes to the serialization information about an object's type.
	 * If the serialization does not contain type information, it should
	 * simply return the input as is.
	 * @param e The serialization of an object
	 * @return The same serialization, with the type information removed
	 */
	public abstract T unwrapTypeInfo(T e);

	/**
	 * Retrieves type information about an object. By default, this method
	 * simply returns <code>in</code> as is, and uses the target class
	 * passed as argument.
	 * @param in The serialized object
	 * @param target_class The target class for this object
	 * @return Type information
	 * @throws ClassNotFoundException If class cannot be found
	 */
	public abstract TypeInfo<T> getTypeInfo(T in, Class<?> target_class) throws ClassNotFoundException;

	public static class TypeInfo<T>
	{
		public Class<?> clazz;
		public T e;
	}
}
