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
package ca.uqac.lif.azrael.buffy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class RangeDecimalTest
{
	@Test
	public void test1() throws PrintException, ReadException
	{
		RangeDecimalSchema rd = new RangeDecimalSchema(1000, 10000, -2);
		BitSequence bs = rd.print(57.39);
		assertEquals(14, bs.size());
		Number n = (Number) rd.read(bs);
		assertEquals(57.39, n.floatValue(), 0.01);
	}
	
	@Test
	public void test2() throws PrintException, ReadException
	{
		RangeDecimalSchema rd = new RangeDecimalSchema(1000, 10000, -2);
		BitSequence bs = rd.print(10);
		assertEquals(14, bs.size());
		Number n = (Number) rd.read(bs);
		assertEquals(10, n.floatValue(), 0.01);
	}
	
	@Test
	public void test3() throws PrintException, ReadException
	{
		RangeDecimalSchema rd = new RangeDecimalSchema(1000, 10000, -2);
		BitSequence bs = rd.print(100);
		assertEquals(14, bs.size());
		Number n = (Number) rd.read(bs);
		assertEquals(100, n.floatValue(), 0.01);
	}
}
