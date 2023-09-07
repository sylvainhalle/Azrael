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

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

/**
 * A schema enconding a range of floating-point numbers. The range is defined
 * as the interval [<i>m</i>&times;10<sup><i>k</i></sup>,
 * <i>n</i>&times;10<sup><i>k</i></sup>], with <i>m</i>, <i>n</i> and
 * <i>k</i> all being integers and with <i>m</i> &lt; <i>n</i>.
 * <p>
 * The advantage of using such a schema (if the range of numerical values to
 * encode is appropriate for it) is that these three values are known to the
 * schema, so that a number <i>x</i> = <i>m</i>'&times;10<sup><i>k</i></sup>
 * in the interval is uniquely defined by <i>m</i>'&minus;<i>m</i>. This value
 * is an integer that requires log<sub>2</sub>(<i>n</i>&minus;<i>m</i>) bits
 * to encode.
 * <p>
 * As an example, consider the interval of numbers [10,100] with a precision of
 * two decimals. This range can be encoded by setting <i>m</i>=1000,
 * <i>n</i>=10000, and <i>k</i>=&minus;2. Thus <i>x</i> = 57.39 becomes
 * 5739&times;10<sup>&minus;2</sup>, which is encoded as the integer 4739
 * (since the interval does not start at 0). It takes 14 bits to encode this
 * value, compared to 32 if stored as a {@link Float}, or 40 if stored as a
 * {@link String}.
 * 
 * @author Sylvain Hallé
 */
public class RangeDecimalSchema extends IntSchema
{
	protected final int m_first;
	
	protected final int m_last;
	
	protected final int m_power;
	
	protected final double m_powP;
	
	protected final double m_powN;
	
	public RangeDecimalSchema(int first, int last, int power)
	{
		super((int) Math.ceil(Math.log(last - first + 1) / LG2), false);
		m_first = first;
		m_last = last;
		m_power = power;
		m_powP = Math.pow(10, power);
		m_powN = Math.pow(10, -power);
	}

	@Override
	public Number read(Object t) throws ReadException
	{
		int x_pow = super.read(t).intValue();
		double x = (x_pow + m_first) * m_powP;
		return x;
	}

	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (!(o instanceof Number))
		{
			throw new PrintException("Expected a number");
		}
		Number num = (Number) o;
		double x = num.doubleValue();
		int x_pow = (int) (x * m_powN);
		return super.print(x_pow - m_first);
	}
}
