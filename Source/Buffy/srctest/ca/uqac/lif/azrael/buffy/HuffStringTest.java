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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.azrael.buffy.HuffstringSchema.HuffNode;

import static ca.uqac.lif.azrael.buffy.HuffstringSchema.leaf;
import static ca.uqac.lif.azrael.buffy.HuffstringSchema.mid;

public class HuffStringTest
{
	@Test
	public void testPrint1() throws PrintException
	{
		HuffNode tree = mid(
				mid(leaf("a"), leaf(null)),
				mid(leaf("b"), leaf("c"))
				);
		HuffstringSchema schema = new HuffstringSchema(tree);
		BitSequence seq = schema.print("aa");
		assertEquals("000001", seq.toString());
	}
	
	@Test
	public void testPrint2() throws PrintException
	{
		HuffNode tree = mid(
				mid(leaf("a"), leaf(null)),
				mid(leaf("b"), leaf("c"))
				);
		HuffstringSchema schema = new HuffstringSchema(tree);
		BitSequence seq = schema.print("bac");
		assertEquals("10001101", seq.toString());
	}
	
	@Test(expected=PrintException.class)
	public void testPrint3() throws PrintException
	{
		HuffNode tree = mid(
				mid(leaf("a"), leaf(null)),
				mid(leaf("b"), null)
				);
		HuffstringSchema schema = new HuffstringSchema(tree);
		schema.print("bac"); // no code point for 'c'
	}
	
	@Test
	public void testRead1() throws ReadException
	{
		HuffNode tree = mid(
				mid(leaf("a"), leaf(null)),
				mid(leaf("b"), leaf("c"))
				);
		HuffstringSchema schema = new HuffstringSchema(tree);
		String read = schema.read(new BitSequence("10001101"));
		assertEquals("bac", read);
	}
	
	@Test
	public void testRead2() throws ReadException
	{
		HuffNode tree = mid(
				mid(leaf("a"), leaf(null)),
				mid(leaf("b"), leaf("c"))
				);
		HuffstringSchema schema = new HuffstringSchema(tree);
		BitSequence seq = new BitSequence("100011010000000");
		String read = schema.read(seq);
		assertEquals("bac", read);
		assertEquals(7, seq.size());
	}
	
	@Test(expected=ReadException.class)
	public void testRead3() throws ReadException
	{
		HuffNode tree = mid(
				mid(leaf("a"), leaf(null)),
				mid(leaf("b"), null)
				);
		HuffstringSchema schema = new HuffstringSchema(tree);
		schema.read(new BitSequence("10 00 11 01")); // no character for 11
	}
	
	@Test
	public void testRead4() throws ReadException
	{
		HuffNode tree = mid(
				mid(leaf("a"), leaf(null)),
				mid(leaf("b"), mid(
						leaf("c"), leaf("d")))
				);
		HuffstringSchema schema = new HuffstringSchema(tree);
		BitSequence seq = new BitSequence("111 00 111 00 01");
		String read = schema.read(seq);
		assertEquals("dada", read);
	}
}
