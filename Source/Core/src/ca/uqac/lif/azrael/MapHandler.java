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
import java.util.Map;
import java.util.Set;

public abstract class MapHandler<T> extends Handler<T>
{
	public MapHandler(GenericSerializer<T> s)
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
		return Map.class.isAssignableFrom(clazz);
	}

	@Override
	public final T serializeAs(Object o, Class<?> clazz) throws SerializerException
	{
		if (o == null || !(o instanceof Map))
		{
			throw new SerializerException("Object to be serialized as a Map, but not an instance of Map");
		}
		Map<?,?> map = (Map<?,?>) o;
		T t_map = getNewMap();
		for (Object key : map.keySet())
		{
			Object value = map.get(key);
			// We deliberately serialize the elements of the list as
			// if they were declared as mere "Object"s; this will force
			// the exact type of each element to be inserted in the
			// serialization
			T t_key = m_serializer.serializeAs(key, Object.class);
			T t_value = m_serializer.serializeAs(value, Object.class);
			t_map = addToMap(t_map, new KeyValuePair<T,T>(t_key, t_value));
		}
		return t_map;
	}

	@Override
	public final Object deserializeAs(T e, Class<?> clazz) throws SerializerException
	{
		Set<KeyValuePair<T,T>> pairs = getPairs(e);
		Set<KeyValuePair<Object,Object>> o_elements = new HashSet<KeyValuePair<Object,Object>>();
		for (KeyValuePair<T,T> pair : pairs)
		{
			Object o_key = m_serializer.deserializeAs(pair.getKey(), Object.class);
			Object o_value = m_serializer.deserializeAs(pair.getValue(), Object.class);
			o_elements.add(new KeyValuePair<Object,Object>(o_key, o_value));
		}
		Map<?,?> out = createMapFromContents(o_elements, clazz);
		return out;
	}
	
	/**
	 * Creates a new instance of the serialized equivalent of a map.
	 * The exact object to be created depends on the serialization type.
	 * @return A serialization of an empty map
	 */
	protected abstract T getNewMap();
	
	/**
	 * Adds a serialized element to a serialized map
	 * @param list The serialized map
	 * @param pair The key-value pair to add to the map
	 * @return The new serialized map
	 * @throws SerializerException If the operation cannot be carried on
	 */
	protected abstract T addToMap(T list, KeyValuePair<T,T> pair) throws SerializerException;
	
	/**
	 * Gets the set of key-value pairs from a serialized map
	 * @param e The serialized map
	 * @return A set of key-value pairs, each of which contains a
	 *  serialized key and a serialized value
	 * @throws SerializerException 
	 */
	protected abstract Set<KeyValuePair<T,T>> getPairs(T e) throws SerializerException;
	
	/**
	 * Instantiates a deserialized map from its deserialized contents
	 * @param contents The contents of the map
	 * @return The map
	 * @throws SerializerException If something goes wrong
	 */
	@SuppressWarnings("unchecked")
	protected Map<?,?> createMapFromContents(Set<KeyValuePair<Object,Object>> contents, Class<?> clazz) throws SerializerException
	{
		Map<Object,Object> out_map = null;
		try
		{
			out_map = (Map<Object,Object>) clazz.newInstance();
			for (KeyValuePair<Object,Object> pair : contents)
			{
				out_map.put(pair.getKey(), pair.getValue());
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
		return out_map;
	}
	
	public static class KeyValuePair<U,V>
	{
		public U m_key;
		public V m_value;
		
		public KeyValuePair(U key, V value)
		{
			super();
			m_key = key;
			m_value = value;
		}
		
		public U getKey()
		{
			return m_key;
		}
		
		public V getValue()
		{
			return m_value;
		}
	}

}
