/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2023 Sylvain Hallé
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

public interface AzraelReader<T>
{
	/**
	 * Deserializes the content of an object.
	 * @param t The serialized contents of the object
	 * @return The deserialized object
	 * @throws ReadException Thrown if deserialization produced an error
	 */
	public Object read(Object t) throws ReadException;
}
