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
 * Exception that can be thrown when using an object printer.
 */
public class PrintException extends Exception
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new print exception
	 * @param t The throwable from which the exception is created
	 */
	public PrintException(Throwable t)
	{
		super(t);
	}

	/**
	 * Creates a new print exception
	 * @param e The exception from which the exception is created
	 */
	public PrintException(Exception e)
	{
		super(e);
	}

	/**
	 * Creates a new print exception
	 * @param message The message associated to the exception
	 */
	public PrintException(String message)
	{
		super(message);
	}

}