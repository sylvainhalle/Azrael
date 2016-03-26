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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.azrael.GenericSerializer.TypeInfo;

/**
 * Handler for a <em>compound</em> object, i.e. an object that contains
 * member fields.
 * @author sylvain
 *
 * @param <T> The type
 */
public abstract class ObjectHandler<T> extends Handler<T>
{

	public ObjectHandler(GenericSerializer<T> s)
	{
		super(s);
	}

	@Override
	public final T serializeAs(Object o, Class<?> clazz) throws SerializerException
	{
		if (PrimitiveHandler.isPrimitive(o))
		{
			// Problem
			throw new SerializerException("Asked to serialize primitive instance as a composite object");
		}
		// We get here if the object is not primitive
		Map<String,T> contents = new HashMap<String,T>();
		for (Field field : GenericSerializer.getAllFields(o.getClass()))
		{
			// Is this field declared as transient?
			if (Modifier.isTransient(field.getModifiers()))
			{
				// Yes: don't serialize this field
				continue; 
			}
			field.setAccessible(true);
			try
			{
				String field_name = field.getName();
				Object field_value = field.get(o);
				Class<?> declared_type = field.getType();
				T serialized_value = m_serializer.serializeAs(field_value, declared_type);
				contents.put(field_name, serialized_value);
			} 
			catch (IllegalArgumentException e)
			{
				throw new SerializerException(e);
			} 
			catch (IllegalAccessException e)
			{
				throw new SerializerException(e);
			}
		}
		return serializeObject(o, contents);
	}

	/**
	 * Serialize an object, given the serialization of each of its
	 * attributes.
	 * @param o The object to serialize
	 * @param contents A map from the object's attribute names to their
	 *   appropriate serialization
	 * @return The serialization of that object
	 * @throws SerializerException If serialization cannot proceed
	 */
	public abstract T serializeObject(Object o, Map<String,T> contents) throws SerializerException;

	@Override
	public final Object deserializeAs(T e, Class<?> clazz) throws SerializerException
	{
		try
		{
			TypeInfo<T> info = m_serializer.getTypeInfo(e, clazz);
			info.e = m_serializer.unwrapTypeInfo(info.e);
			Map<String,Object> contents = deserializeContents(info.e, info.clazz);
			Object o1 = getInstance(contents, info.clazz);
			Object o2 = populateObject(o1, contents, info.clazz);
			return o2;
		}
		catch (ClassNotFoundException ex)
		{
			return new SerializerException(ex);
		}
	}

	/**
	 * Deserializes the content (i.e. the member fields) of an enclosing
	 * object.
	 * @param e The serialized contents of the object
	 * @param clazz The class the object should be an instance of
	 * @return A map from member field names to deserialized objects
	 * @throws SerializerException
	 */
	public abstract Map<String,Object> deserializeContents(T e, Class<?> clazz) throws SerializerException;

	/**
	 * Produces an instance of an object based on its deserialized contents
	 * and a target class. This method implements the default technique
	 * for obtaining an instance of an object:
	 * just ask for an instance based on the class, using the
	 * <code>newInstance()</code> method.
	 * <p>
	 * You need to override this in your
	 * own ObjectHandler if you cannot instantiate your objects in this way.
	 * This generally occurs in two occasions:
	 * <ol>
	 * <li>The object doesn't have a no-argument constructor. Calling
	 *   <tt>getInstance</tt> will throw an <tt>InstantiationException</tt>.</li>
	 * <li>The declared type of the object is an abstract class or an
	 *   interface. This will also throw an exception.</li>
	 * </ol>
	 * @param contents The deserialized member fields of the object
	 * @param clazz The target class for the object
	 * @return An instance of the object
	 * @throws SerializerException 
	 */
	protected Object getInstance(Map<String,Object> contents, Class<?> clazz) throws SerializerException
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
			throw new SerializerException(ex);
		} 
		catch (IllegalAccessException ex)
		{
			throw new SerializerException(ex);
		} 
		catch (NoSuchMethodException ex) 
		{
			throw new SerializerException(ex);
		}
		catch (SecurityException ex)
		{
			throw new SerializerException(ex);
		} catch (IllegalArgumentException ex)
		{
			throw new SerializerException(ex);
		}
		catch (InvocationTargetException ex)
		{
			throw new SerializerException(ex);
		}
		return o;
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
	 * @throws SerializerException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object populateObject(Object o, Map<String,Object> contents, Class<?> clazz) throws SerializerException
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
				Field fld = GenericSerializer.getFromAllFields(attribute, clazz);
				fld.setAccessible(true);
				Object value_o = contents.get(attribute);
				if (fld.getType().isEnum())
				{
					if (!(value_o instanceof String))
					{
						throw new SerializerException("The deserialized value of an enum field should be a string");
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
				throw new SerializerException(ex);
			}
			catch (IllegalAccessException ex)
			{
				throw new SerializerException(ex);
			}
		}
		return o;
	}

}
