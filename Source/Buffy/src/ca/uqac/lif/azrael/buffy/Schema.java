/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2022 Sylvain Hallé
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
package ca.uqac.lif.azrael.buffy;

import ca.uqac.lif.azrael.AzraelPrinter;
import ca.uqac.lif.azrael.AzraelReader;

/**
 * Turns an object into a sequence if bits, and reads an object from a sequence
 * of bits. Contrary to a classical serializer, a schema typically only deals
 * with a single object type having a very precise structure.
 *  
 * @author Sylvain Hallé
 */
public interface Schema extends AzraelReader<BitSequence>, AzraelPrinter<BitSequence>
{
	
}
