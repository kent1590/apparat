/*
 * This file is part of Apparat.
 *
 * Copyright (C) 2010 Joa Ebert
 * http://www.joa-ebert.com/
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package jitb.lang;

/**
 * @author Joa Ebert
 */
public class trace extends jitb.lang.Object {
	public static java.lang.Object callStatic(final java.lang.Object... arguments) {
		final StringBuilder builder = new StringBuilder();
		final int n = arguments.length;
		final int m = n - 1;

		for(int i = 0; i < n; ++i) {
			if(null == arguments[i]) {
				builder.append("null");
			} else {
				builder.append(arguments[i].toString());
			}

			if(i != m) {
				builder.append(" ");
			}
		}

		System.out.println(builder.toString());
		return null;
	}
}
