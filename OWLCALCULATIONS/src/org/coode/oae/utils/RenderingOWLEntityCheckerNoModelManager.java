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
package org.coode.oae.utils;

import java.util.Set;

import org.semanticweb.owl.expression.OWLEntityChecker;
import org.semanticweb.owl.expression.ShortFormEntityChecker;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owl.util.NamespaceUtil;
import org.semanticweb.owl.util.SimpleShortFormProvider;

/**
 * @author Luigi Iannone
 * 
 *         The University Of Manchester<br>
 *         Bio-Health Informatics Group<br>
 *         Aug 12, 2008
 */
public class RenderingOWLEntityCheckerNoModelManager implements
		OWLEntityChecker {
	Set<OWLOntology> ontologies;
	protected ShortFormEntityChecker shortFormEntityChecker = null;

	/**
	 * @param owlModelManager
	 */
	public RenderingOWLEntityCheckerNoModelManager(Set<OWLOntology> ontologies) {
		this.ontologies = ontologies;
		this.shortFormEntityChecker = new ShortFormEntityChecker(
				new BidirectionalShortFormProviderAdapter(ontologies,
						new SimpleShortFormProvider()));
	}

	private OWLClass getOWLClassByFullURI(String uri) {
		for (OWLOntology o : this.ontologies) {
			Set<OWLClass> classes = o.getReferencedClasses();
			for (OWLClass c : classes) {
				if (c.getURI().toString().equals(uri)) {
					return c;
				}
			}
		}
		return null;
	}

	private OWLDataProperty getOWLDataPropertyByFullURI(String uri) {
		for (OWLOntology o : this.ontologies) {
			Set<OWLDataProperty> props = o.getReferencedDataProperties();
			for (OWLDataProperty c : props) {
				if (c.getURI().toString().equals(uri)) {
					return c;
				}
			}
		}
		return null;
	}

	private OWLObjectProperty getOWLObjectPropertyByFullURI(String uri) {
		for (OWLOntology o : this.ontologies) {
			Set<OWLObjectProperty> props = o.getReferencedObjectProperties();
			for (OWLObjectProperty c : props) {
				if (c.getURI().toString().equals(uri)) {
					return c;
				}
			}
		}
		return null;
	}

	private OWLIndividual getOWLIndividualByFullURI(String uri) {
		for (OWLOntology o : this.ontologies) {
			Set<OWLIndividual> props = o.getReferencedIndividuals();
			for (OWLIndividual c : props) {
				if (c.getURI().toString().equals(uri)) {
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * @see org.semanticweb.owl.expression.OWLEntityChecker#getOWLClass(java.lang.String)
	 */
	public OWLClass getOWLClass(String name) {
		OWLClass toReturn = getOWLClassByFullURI(name);
		if (toReturn == null) {
			toReturn = this.shortFormEntityChecker.getOWLClass(name);
		}
		if (name.length() > 0 && toReturn == null) {
			NamespaceUtil nsUtil = new NamespaceUtil();
			String[] split = nsUtil.split(name, null);
			if (split.length == 2) {
				toReturn = this.shortFormEntityChecker.getOWLClass(split[1]);
			}
		}
		return toReturn;
	}

	/**
	 * @see org.semanticweb.owl.expression.OWLEntityChecker#getOWLDataProperty(java.lang.String)
	 */
	public OWLDataProperty getOWLDataProperty(String name) {
		OWLDataProperty toReturn = getOWLDataPropertyByFullURI(name);
		if (toReturn == null) {
			toReturn = this.shortFormEntityChecker.getOWLDataProperty(name);
		}
		if (name.length() > 0 && toReturn == null) {
			NamespaceUtil nsUtil = new NamespaceUtil();
			String[] split = nsUtil.split(name, null);
			if (split.length == 2) {
				toReturn = this.shortFormEntityChecker
						.getOWLDataProperty(split[1]);
			}
		}
		return toReturn;
	}

	/**
	 * @see org.semanticweb.owl.expression.OWLEntityChecker#getOWLDataType(java.lang.String)
	 */
	public OWLDataType getOWLDataType(String name) {
		return this.shortFormEntityChecker.getOWLDataType(name);
	}

	/**
	 * @see org.semanticweb.owl.expression.OWLEntityChecker#getOWLIndividual(java.lang.String)
	 */
	public OWLIndividual getOWLIndividual(String name) {
		OWLIndividual toReturn = getOWLIndividualByFullURI(name);
		if (toReturn == null) {
			toReturn = this.shortFormEntityChecker.getOWLIndividual(name);
		}
		if (name.length() > 0 && toReturn == null) {
			NamespaceUtil nsUtil = new NamespaceUtil();
			String[] split = nsUtil.split(name, null);
			if (split.length == 2) {
				toReturn = this.shortFormEntityChecker
						.getOWLIndividual(split[1]);
			}
		}
		return toReturn;
	}

	/**
	 * @see org.semanticweb.owl.expression.OWLEntityChecker#getOWLObjectProperty(java.lang.String)
	 */
	public OWLObjectProperty getOWLObjectProperty(String name) {
		OWLObjectProperty toReturn = getOWLObjectPropertyByFullURI(name);
		if (toReturn == null) {
			toReturn = this.shortFormEntityChecker.getOWLObjectProperty(name);
		}
		if (name.length() > 0 && toReturn == null) {
			NamespaceUtil nsUtil = new NamespaceUtil();
			String[] split = nsUtil.split(name, null);
			if (split.length == 2) {
				toReturn = this.shortFormEntityChecker
						.getOWLObjectProperty(split[1]);
			}
		}
		return toReturn;
	}
}
