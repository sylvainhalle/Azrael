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

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Defines how the contents of various objects should be "printed" as
 * another type of object.
 * @author Sylvain Hallé
 *
 * @param <T> The type of the output object
 */
public interface ObjectPrinter<T>
{
	/**
	 * Prints an object
	 * @param o The object
	 * @return The printed object
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public T print(Object o) throws PrintException;
	
	/**
	 * Prints a Boolean
	 * @param b The Boolean
	 * @return The printed object
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public T print(Boolean b) throws PrintException;
	
	/**
	 * Prints a number
	 * @param n The number
	 * @return The printed number
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public T print(Number n) throws PrintException;
	
	/**
	 * Prints a string
	 * @param o The string
	 * @return The printed string
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public T print(String s) throws PrintException;
	
	/**
	 * Prints a map
	 * @param m The map
	 * @return The printed map
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public T print(Map<String,?> m) throws PrintException;
	
	/**
	 * Prints a list
	 * @param l The list
	 * @return The printed list
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public T print(List<?> l) throws PrintException;
	
	/**
	 * Prints a set
	 * @param s The set
	 * @return The printed set
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public T print(Set<?> m) throws PrintException;
	
	/**
	 * Prints a queue
	 * @param q The queue
	 * @return The printed queue
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public T print(Queue<?> q) throws PrintException;
	
	/**
	 * Prints a null value
	 * @return The printed null
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public T printNull() throws PrintException;
	
	/**
	 * Wraps an object into a structure that contains its type declaration
	 * and its printed contents
	 * @param o The object
	 * @param t The object's printed contents
	 * @return The printed structure
	 * @throws PrintException Thrown if the print operation caused an error
	 */
	public T wrap(Object o, T t) throws PrintException; 
}
