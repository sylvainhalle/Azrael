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

/**
 * Core package containing only the definition of the interfaces.
 * <p>
 * Typically, if you want to create objects that <em>support</em> serialization,
 * you only need to import this package into your project and make sure your
 * objects implement the {@link ca.uqac.lif.azrael.Readable Readable} and
 * {@link ca.uqac.lif.azrael.Readable Printable} interfaces.
 * <p>
 * On the other hand, if you want to <em>use</em> serialization, you also need
 * one of the concrete packages that implement serialization (such as
 * {@link ca.uqac.lif.azrael.json} or
 * {@link ca.uqac.lif.azrael.xml}).
 */
package ca.uqac.lif.azrael;