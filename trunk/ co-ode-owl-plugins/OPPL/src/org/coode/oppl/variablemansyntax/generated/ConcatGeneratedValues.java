/**
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.coode.oppl.variablemansyntax.generated;

import java.util.List;

/**
 * @author Luigi Iannone
 * 
 */
public class ConcatGeneratedValues extends AggregatedGeneratedValue {
	public ConcatGeneratedValues(List<GeneratedValue> values2Aggregate) {
		super(values2Aggregate);
	}

	/**
	 * @see org.coode.oppl.variablemansyntax.generated.AggregatedGeneratedValue#aggregateValues(java.util.List)
	 */
	@Override
	protected String aggregateValues(List<String> values) {
		String toReturn = "";
		for (String value : values) {
			toReturn += value;
		}
		return toReturn;
	}

	@Override
	protected String getAggregatorSymbol() {
		return "+";
	}
}