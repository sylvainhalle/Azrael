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

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.azrael.buffy.BitSequence;
import ca.uqac.lif.azrael.buffy.BlobSchema;

public class BlobTest
{
	@Test
	public void testPrint1() throws PrintException
	{
		BitSequence blob = new BitSequence("10101010");
		BitSequence seq = BlobSchema.instance.print(blob);
		assertEquals(40, seq.size());
		System.out.println(seq);
	}
	
	@Test
	public void testRead1() throws ReadException
	{
		BitSequence seq = new BitSequence("0000000000000000000000000000100010101010");
		BitSequence blob = BlobSchema.instance.read(seq);
		assertEquals("10101010", blob.toString());
		assertEquals(0, seq.size());
	}
}
