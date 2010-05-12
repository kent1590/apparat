/*
 * This file is part of Apparat.
 *
 * Apparat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Apparat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Apparat. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2009 Joa Ebert
 * http://www.joa-ebert.com/
 *
 */
package apparat.graph.mutable

import apparat.graph.BlockVertex

class MutableBlockVertex[T](var block: List[T] = Nil) extends BlockVertex[T] {
	def ++(elms: List[T]) = {
		block = block ++ elms
		this
	}

	def removeFirst() = {
		block = block.tail
		this
	}

	def removeLast() = {
		block = block dropRight 1
		this
	}

	def clear() = {
		block = Nil
		this
	}

	// FIXME tmp for the dot graph export
	override def toString = {
		block.mkString("[[", "\\n", "]]")
	}
}