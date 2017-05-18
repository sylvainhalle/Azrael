/*
    A serializer for Java objects
    Copyright (C) 2016-2017 Sylvain Hallé
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

public abstract class Handler<T>
{
	/**
	 * The serializer this handler is attached to
	 */
	protected final GenericSerializer<T> m_serializer;

	public Handler(GenericSerializer<T> s)
	{
		super();
		m_serializer = s;
	}

	/**
	 * Determines whether this handler applies to the object to serialize or
	 * deserialize
	 * @param clazz The class of the object
	 * @return true if the handler can handle this object, false otherwise
	 */
	public abstract boolean appliesTo(Class<?> clazz);

	/**
	 * Serializes an object
	 * @param o The object to serialize
	 * @return A serialization of this object
	 * @throws SerializerException If the serialization could not be done
	 */
	public final T serialize(Object o) throws SerializerException
	{
		if (o == null)
		{
			return serializeAs(null, null);
		}
		return serializeAs(o, o.getClass());
	}

	/**
	 * Serializes an object, specifying the class used to <em>declare</em>
	 * this object.
	 * @param o The object to serialize
	 * @param def The type definition of the object to create
	 * @return A serialization of this object
	 * @throws SerializerException If the serialization could not be done
	 */
	//public abstract T serializeAs(Object o, TypeDefinition def) throws SerializerException;

	/**
	 * Serializes an object, specifying the class used to <em>declare</em>
	 * this object.
	 * @param o The object to serialize
	 * @param clazz The class of the object to create
	 * @return A serialization of this object
	 * @throws SerializerException If the serialization could not be done
	 */
	public abstract T serializeAs(Object o, Class<?> clazz) throws SerializerException;

	/**
	 * Deserializes an object
	 * @param e The serialized representation of the object
	 * @param clazz The target class
	 * @return The object
	 * @throws SerializerException If the deserialization could not be done
	 */
	public abstract Object deserializeAs(T e, Class<?> clazz) throws SerializerException;
}
