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

public class IntTest
{
	@Test
	public void testPrint1() throws PrintException, ReadException
	{
		BitSequence bs = IntSchema.int32.print(8);
		assertEquals(32, bs.size());
		Number n = IntSchema.int32.read(bs);
		assertEquals(8, n.intValue());
	}
	
	@Test//(timeout=1000)
	public void testPrint2() throws PrintException, ReadException
	{
		long time = System.currentTimeMillis();
		BitSequence bs = IntSchema.int64.print(time);
		assertEquals(64, bs.size());
		Number n = IntSchema.int64.read(bs);
		assertEquals(time, n.longValue());
	}
}
