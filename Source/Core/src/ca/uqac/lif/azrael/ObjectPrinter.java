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

import java.util.ArrayList;
import java.util.List;

/**
 * Prints the content of an object to a given format.
 * @author Sylvain Hallé
 *
 * @param <T> The format used to print the object's contents
 */
public abstract class ObjectPrinter<T>
{
	/**
	 * A list of objects that handle the printing of objects of various
	 * types
	 */
	protected List<PrintHandler<T>> m_handlers;
	
	/**
	 * The default handler to use when no other handler accepts an object
	 */
	protected PrintHandler<T> m_reflectionHandler = new ReflectionPrintHandler<T>(this);
	
	/**
	 * Whether to use the {@link Readable} interface when an object
	 * implements it
	 */
	protected boolean m_usePrintable = true;
	
	/**
	 * Creates a new object printer
	 */
	public ObjectPrinter()
	{
		super();
		m_handlers = new ArrayList<PrintHandler<T>>();
	}
	
	/**
	 * Serializes the contents of an object. If the object implements the
	 * {@link Printable} interface, it is serialized by calling its
	 * {@link Printable#print(ObjectPrinter) print()} method. Otherwise, the
	 * serializer uses reflection to extract the object's fields and create
	 * a key-value map of field names associated to their serialized contents.
	 * In such a case, fields that are declared as <tt>transient</tt> in the
	 * object are ignored.
	 * @param o The object
	 * @return The serialized contents
	 * @throws PrintException Thrown if an error occurs during the
	 * serialization
	 */
	public T print(Object o) throws PrintException
	{
		if (m_usePrintable && o instanceof Printable)
		{
			@SuppressWarnings("unchecked")
			T t = (T) ((Printable) o).print(this);
			return wrap(o, t);
		}
		for (PrintHandler<T> handler : m_handlers)
		{
			if (handler.canHandle(o))
			{
				return handler.handle(o);
			}
		}
		return m_reflectionHandler.handle(o);
	}
	
	/**
	 * Wraps an object into a structure that contains its type declaration
	 * and its printed contents
	 * @param o The object
	 * @param t The object's printed contents
	 * @return The printed structure
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public abstract T wrap(Object o, T t) throws PrintException;
	
	/**
	 * Resets the state of the printer
	 */
	public void reset()
	{
		for (PrintHandler<?> h : m_handlers)
		{
			h.reset();
		}
		if (m_reflectionHandler != null)
		{
			m_reflectionHandler.reset();
		}
	}
	
	/**
	 * Tells whether the object printer uses the {@link Printable}
	 * interface when an object implements it
	 * @return true or false
	 */
	public boolean usesPrintable()
	{
		return m_usePrintable;
	}
}
