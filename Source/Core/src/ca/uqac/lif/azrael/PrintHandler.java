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

/**
 * An interface that takes care of printing a specific type of object.
 * @author Sylvain Hallé
 *
 * @param <T> The type to which objects are serialized
 */
public interface PrintHandler<T> 
{
	/**
	 * Indicates whether this handler can take care of printing a particular
	 * object.
	 * @param o The object
	 * @return <tt>true</tt> if this handler can print this object,
	 * <tt>false</tt> otherwise
	 */
	public boolean canHandle(Object o);
	
	/**
	 * Prints an object
	 * @param o The object to be printed
	 * @return The printed object
	 * @throws PrintException Thrown if the object cannot be printed
	 */
	public T handle(Object o) throws PrintException;
	
	/**
	 * Resets the internal state of the handler
	 */
	public void reset();
}
