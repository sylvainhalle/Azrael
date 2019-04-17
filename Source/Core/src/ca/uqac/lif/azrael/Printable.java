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
 * Interface advertising that an object can write its contents
 * into an object printer. This is equivalent to serializing the
 * contents of the object.
 * 
 * @author Sylvain Hallé
 */
public interface Printable 
{
	/**
	 * Prints the content of the object into an object printer
	 * @param printer The printer to print the object to
	 */
	public Object print(ObjectPrinter<?> printer) throws PrintException;
	
	
}
