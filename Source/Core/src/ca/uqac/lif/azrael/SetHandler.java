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

import java.util.HashSet;
import java.util.Set;

/**
 * Handler for serializing a set.
 * 
 * @param <T> The object 
 */
public abstract class SetHandler<T> extends Handler<T> 
{
	public SetHandler(GenericSerializer<T> s)
	{
		super(s);
	}

	@Override
	public boolean appliesTo(Class<?> clazz) 
	{
		if (clazz == null)
		{
			return false;
		}
		return Set.class.isAssignableFrom(clazz);
	}

	@Override
	public T serializeAs(Object o, Class<?> clazz) throws SerializerException 
	{
		if (o == null || !(o instanceof Set))
		{
			throw new SerializerException("Object to be serialized as a Set, but not an instance of Set");
		}
		Set<?> set = (Set<?>) o;
		T t_set = getNewSet();
		for (Object elem : set)
		{
			// We deliberately serialize the elements of the list as
			// if they were declared as mere "Object"s; this will force
			// the exact type of each element to be inserted in the
			// serialization
			T t_elem = m_serializer.serializeAs(elem, Object.class);
			t_set = addToSet(t_set, t_elem);
		}
		return t_set;
	}

	@Override
	public Object deserializeAs(T e, Class<?> clazz) throws SerializerException
	{
		Set<T> elements = getElements(e);
		Set<Object> o_elements = new HashSet<Object>();
		for (T elem : elements)
		{
			Object o_elem = m_serializer.deserializeAs(elem, Object.class);
			o_elements.add(o_elem);
		}
		Set<?> out = createSetFromContents(o_elements, clazz);
		return out;
	}
	
	/**
	 * Creates a new instance of the serialized equivalent of a set.
	 * The exact object to be created depends on the serialization type.
	 * @return A serialization of an empty set
	 */
	protected abstract T getNewSet();
	
	/**
	 * Adds a serialized element to a serialized set
	 * @param set The serialized set
	 * @param element The serialized element to add to it
	 * @return The new serialized set
	 * @throws SerializerException If the operation cannot be carried on
	 */
	protected abstract T addToSet(T set, T element) throws SerializerException;
	
	/**
	 * Gets a set of serialized elements from a serialized set
	 * @param e The serialized set
	 * @return A set of serialized elements
	 * @throws SerializerException 
	 */
	protected abstract Set<T> getElements(T e) throws SerializerException;
	
	/**
	 * Instantiates a deserialized set from its deserialized contents
	 * @param contents The contents of the set
	 * @return The set
	 * @throws SerializerException If something goes wrong
	 */
	@SuppressWarnings("unchecked")
	protected Set<?> createSetFromContents(Set<Object> contents, Class<?> clazz) throws SerializerException
	{
		Set<Object> out_list = null;
		try
		{
			out_list = (Set<Object>) clazz.newInstance();
			for (Object o : contents)
			{
				out_list.add(o);
			}
		} 
		catch (InstantiationException e)
		{
			throw new SerializerException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new SerializerException(e);
		}
		return out_list;
	}
}
