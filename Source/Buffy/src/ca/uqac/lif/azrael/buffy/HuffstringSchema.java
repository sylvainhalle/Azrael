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

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class HuffstringSchema extends StringSchema
{
	protected final HuffNode m_tree;
	
	protected final Map<String,BitSequence> m_map;
	
	public HuffstringSchema(HuffNode tree)
	{
		super();
		m_tree = tree;
		m_map = new HashMap<String,BitSequence>();
		fillMap(tree, new BitSequence(), m_map);
	}
	
	@Override
	public String read(Object o) throws ReadException
	{
		if (!(o instanceof BitSequence))
		{
			throw new ReadException("Expected a bit sequence");
		}
		BitSequence t = (BitSequence) o;
		StringBuilder out = new StringBuilder();
		String c = null;
		do
		{
			c = readSymbol(m_tree, t);
			if (c != null)
			{
				out.append(c);
			}
		}
		while (c != null);
		return out.toString();
	}

	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (!(o instanceof String))
		{
			throw new PrintException("Expected a string");
		}
		String s = (String) o;
		BitSequence seq = new BitSequence();
		for (int i = 0; i < s.length(); i++)
		{
			String c = s.substring(i, i + 1);
			if (!m_map.containsKey(c))
			{
				throw new PrintException("No codeword for '" + c + "'");
			}
			seq.addAll(m_map.get(c));
		}
		seq.addAll(m_map.get(null)); // Add the symbol for null
		return seq;
	}
	
	public static String readSymbol(HuffNode n, BitSequence seq) throws ReadException
	{
		if (n == null)
		{
			throw new ReadException("Undefined code point");
		}
		if (n instanceof HuffLeaf)
		{
			return ((HuffLeaf) n).getSymbol();
		}
		BitSequence bit = seq.truncatePrefix(1);
		if (!bit.get(0))
		{
			return readSymbol(((HuffMid) n).getLeft(), seq);
		}
		else
		{
			return readSymbol(((HuffMid) n).getRight(), seq);
		}
	}
	
	protected static void fillMap(HuffNode n, BitSequence seq, Map<String,BitSequence> map)
	{
		if (n == null)
		{
			return;
		}
		if (n instanceof HuffLeaf)
		{
			map.put(((HuffLeaf) n).m_symbol, seq);
			return;
		}
		HuffMid mid = (HuffMid) n;
		BitSequence bs_left = new BitSequence();
		bs_left.addAll(seq);
		bs_left.add(false);
		BitSequence bs_right = new BitSequence();
		bs_right.addAll(seq);
		bs_right.add(true);
		fillMap(mid.getLeft(), bs_left, map);
		fillMap(mid.getRight(), bs_right, map);
	}
	
	
	public static abstract class HuffNode
	{
		protected HuffNode m_parent = null;
		
		protected int m_weight = 0;
		
		public void setParent(HuffNode p)
		{
			m_parent = p;
		}
		
		public HuffNode getParent()
		{
			return m_parent;
		}
		
		public int getWeight()
		{
			return m_weight;
		}
		
		public void setWeight(int w)
		{
			m_weight = w;
		}
		
		protected abstract String toString(String indent);
	}
	
	public static class HuffLeaf extends HuffNode
	{
		protected final String m_symbol;
		
		public HuffLeaf(String symbol)
		{
			super();
			m_symbol = symbol;
		}
		
		public String getSymbol()
		{
			return m_symbol;
		}
		
		@Override
		public String toString()
		{
			return toString("");
		}
		
		@Override
		protected String toString(String indent)
		{
			return indent + m_symbol + "|" + m_weight;
		}
	}
	
	public static class HuffMid extends HuffNode
	{
		protected HuffNode m_left;
		
		protected HuffNode m_right;
		
		public HuffMid()
		{
			super();
			m_left = null;
			m_right = null;
		}
		
		public HuffMid(HuffNode left, HuffNode right)
		{
			super();
			m_left = left;
			m_right = right;
		}
		
		public HuffNode getLeft()
		{
			return m_left;
		}
		
		public HuffNode getRight()
		{
			return m_right;
		}
		
		@Override
		public String toString()
		{
			return toString("");
		}
		
		@Override
		protected String toString(String indent)
		{
			StringBuilder out = new StringBuilder();
			out.append(indent).append(m_weight).append("\n");
			if (m_left == null)
			{
				out.append(indent + " -");
			}
			else
			{
				out.append(m_left.toString(indent + " "));
			}
			if (m_right == null)
			{
				out.append(indent + " -");
			}
			else
			{
				out.append(m_right.toString(indent + " "));
			}
			return out.toString();
		}
	}
	
	public static HuffMid mid(HuffNode left, HuffNode right)
	{
		return new HuffMid(left, right);
	}
	
	public static HuffLeaf leaf(String symbol)
	{
		return new HuffLeaf(symbol);
	}
}
