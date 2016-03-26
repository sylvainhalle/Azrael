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

import java.util.LinkedList;
import java.util.List;

public abstract class ListHandler<T> extends Handler<T>
{
	public ListHandler(GenericSerializer<T> s)
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
		return List.class.isAssignableFrom(clazz);
	}

	@Override
	public final T serializeAs(Object o, Class<?> clazz)
			throws SerializerException
			{
		if (o == null || !(o instanceof List))
		{
			throw new SerializerException("Object to be serialized as a List, but not an instance of List");
		}
		List<?> list = (List<?>) o;
		T t_list = getNewList();
		for (Object elem : list)
		{
			// We deliberately serialize the elements of the list as
			// if they were declared as mere "Object"s; this will force
			// the exact type of each element to be inserted in the
			// serialization
			T t_elem = m_serializer.serializeAs(elem, Object.class);
			t_list = addToList(t_list, t_elem);
		}
		return t_list;
	}

	@Override
	public final Object deserializeAs(T e, Class<?> clazz) throws SerializerException
	{
		List<T> elements = getElements(e);
		List<Object> o_elements = new LinkedList<Object>();
		for (T elem : elements)
		{
			Object o_elem = m_serializer.deserializeAs(elem, Object.class);
			o_elements.add(o_elem);
		}
		List<?> out = createListFromContents(o_elements, clazz);
		return out;
	}

	/**
	 * Creates a new instance of the serialized equivalent of a list.
	 * The exact object to be created depends on the serialization type.
	 * @return A serialization of an empty list
	 */
	protected abstract T getNewList();
	
	/**
	 * Adds a serialized element to a serialized list
	 * @param list The serialized list
	 * @param element The serialized element to add to it
	 * @return The new serialized list
	 * @throws SerializerException If the operation cannot be carried on
	 */
	protected abstract T addToList(T list, T element) throws SerializerException;
	
	/**
	 * Gets a list of serialized elements from a serialized list
	 * @param e The serialized list
	 * @return A list of serialized elements
	 * @throws SerializerException 
	 */
	protected abstract List<T> getElements(T e) throws SerializerException;
	
	/**
	 * Instantiates a deserialized list from its deserialized contents
	 * @param contents The contents of the list
	 * @return The list
	 * @throws SerializerException If something goes wrong
	 */
	@SuppressWarnings("unchecked")
	protected List<?> createListFromContents(List<Object> contents, Class<?> clazz) throws SerializerException
	{
		List<Object> out_list = null;
		try
		{
			out_list = (List<Object>) clazz.newInstance();
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
