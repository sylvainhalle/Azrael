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
package ca.uqac.lif.azrael.clone;

import java.util.Queue;

import ca.uqac.lif.azrael.ObjectReader;
import ca.uqac.lif.azrael.ReadException;

/**
 * Print handler that returns a copy of the queue that is passed to it
 * @author Sylvain Hallé
 */
public class QueueReadHandler extends CloneReadHandler
{
	public QueueReadHandler(ObjectReader<Object> reader)
	{
		super(reader);
	}
	
	@Override
	public boolean canHandle(Object o) throws ReadException
	{
		return o instanceof Queue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Queue<?> handle(Object o) throws ReadException 
	{
		Queue<Object> in_queue =  (Queue<Object>) o;
		Queue<Object> queue = (Queue<Object>) m_reader.getInstance(o.getClass());
		queue.addAll(in_queue);
		return queue;
	}
}
