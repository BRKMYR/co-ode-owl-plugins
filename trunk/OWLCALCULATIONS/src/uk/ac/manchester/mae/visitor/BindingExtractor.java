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
package uk.ac.manchester.mae.visitor;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLProperty;

import uk.ac.manchester.mae.MAEBinding;
import uk.ac.manchester.mae.MAEConflictStrategy;
import uk.ac.manchester.mae.MAEPropertyChain;
import uk.ac.manchester.mae.MAEPropertyFacet;
import uk.ac.manchester.mae.MAEStart;
import uk.ac.manchester.mae.MAEStoreTo;
import uk.ac.manchester.mae.MAEmanSyntaxClassExpression;
import uk.ac.manchester.mae.Node;
import uk.ac.manchester.mae.evaluation.BindingModel;
import uk.ac.manchester.mae.evaluation.PropertyChainModel;

/**
 * @author Luigi Iannone
 * 
 *         The University Of Manchester<br>
 *         Bio-Health Informatics Group<br>
 *         Apr 7, 2008
 */
public class BindingExtractor extends FormulaSetupVisitor {
	private Set<OWLOntology> ontologies;
	private OWLOntologyManager owlOntologyManager;

	/**
	 * @param owlOntologyManager
	 * @param ontologies
	 * @param shortFormProvider
	 */
	public BindingExtractor(OWLOntologyManager owlOntologyManager,
			Set<OWLOntology> ontologies) {
		this.owlOntologyManager = owlOntologyManager;
		this.ontologies = ontologies;
	}

	/**
	 * @see uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEStart,
	 *      java.lang.Object)
	 */
	@Override
	public Object visit(MAEStart node, Object data) {
		Set<BindingModel> toReturn = new HashSet<BindingModel>();
		FormulaModelExtractor fme = new FormulaModelExtractor(
				this.owlOntologyManager, this.ontologies);
		node.jjtAccept(fme, data);
		if (fme.getExtractedFormulaModel() != null) {
			toReturn.addAll(fme.getExtractedFormulaModel().getBindings());
		}
		return toReturn;
	}

	/**
	 * @see uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEConflictStrategy,
	 *      java.lang.Object)
	 */
	public Object visit(MAEConflictStrategy node, Object data) {
		return null;
	}

	/**
	 * @see uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEmanSyntaxClassExpression,
	 *      java.lang.Object)
	 */
	public Object visit(MAEmanSyntaxClassExpression node, Object data) {
		return null;
	}

	/**
	 * @see uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEBinding,
	 *      java.lang.Object)
	 */
	public Object visit(MAEBinding node, Object data) {
		PropertyChainModel propertyChainModel = null;
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (child instanceof MAEPropertyChain) {
				propertyChainModel = (PropertyChainModel) child.jjtAccept(this,
						data);
			}
		}
		return new BindingModel(node.getIdentifier(), propertyChainModel);
	}

	/**
	 * @see uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEPropertyChain,
	 *      java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Object visit(MAEPropertyChain node, Object data) {
		URI propertyURI = URI.create(node.getPropertyName());
		OWLProperty property = node.isEnd() ? this.owlOntologyManager
				.getOWLDataFactory().getOWLDataProperty(propertyURI)
				: this.owlOntologyManager.getOWLDataFactory()
						.getOWLObjectProperty(propertyURI);
		DescriptionFacetExtractor pdfExtractor = new DescriptionFacetExtractor(
				this.owlOntologyManager, this.ontologies);
		node.jjtAccept(pdfExtractor, data);
		OWLDescription facet = pdfExtractor.getExtractedDescription();
		PropertyChainModel toReturn = new PropertyChainModel(property, facet);
		if (!node.isEnd() && node.jjtGetNumChildren() > 0) {
			boolean found = false;
			for (int i = 0; !found && i < node.jjtGetNumChildren(); i++) {
				Node child = node.jjtGetChild(i);
				if (child instanceof MAEPropertyChain) {
					found = true;
					toReturn.setChild((PropertyChainModel) node.jjtGetChild(i)
							.jjtAccept(this, data));
				}
			}
		}
		return toReturn;
	}

	/**
	 * @see uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEStoreTo,
	 *      java.lang.Object)
	 */
	public Object visit(MAEStoreTo node, Object data) {
		return null;
	}

	/**
	 * @see uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEPropertyFacet,
	 *      java.lang.Object)
	 */
	public Object visit(MAEPropertyFacet node, Object data) {
		return null;
	}
}
// public class BindingExtractor implements ArithmeticsParserVisitor {
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.SimpleNode,
// * java.lang.Object)
// */
// public Object visit(SimpleNode node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEStart,
// * java.lang.Object)
// */
// public Object visit(MAEStart node, Object data) {
// Set<MAEBinding> toReturn = new HashSet<MAEBinding>();
// int childrenCount = node.jjtGetNumChildren();
// for (int i = 0; i < childrenCount; i++) {
// Node element = node.jjtGetChild(i);
// MAEBinding visitResult = (MAEBinding) element.jjtAccept(this, null);
// if (visitResult != null) {
// toReturn.add(visitResult);
// }
// }
// return toReturn;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEConflictStrategy,
// * java.lang.Object)
// */
// public Object visit(MAEConflictStrategy node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEmanSyntaxClassExpression,
// * java.lang.Object)
// */
// public Object visit(MAEmanSyntaxClassExpression node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEBinding,
// * java.lang.Object)
// */
// public Object visit(MAEBinding node, Object data) {
// return node;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEPropertyChain,
// * java.lang.Object)
// */
// public Object visit(MAEPropertyChain node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEAdd,
// * java.lang.Object)
// */
// public Object visit(MAEAdd node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEMult,
// * java.lang.Object)
// */
// public Object visit(MAEMult node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEPower,
// * java.lang.Object)
// */
// public Object visit(MAEPower node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEIntNode,
// * java.lang.Object)
// */
// public Object visit(MAEIntNode node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEIdentifier,
// * java.lang.Object)
// */
// public Object visit(MAEIdentifier node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEBigSum,
// * java.lang.Object)
// */
// public Object visit(MAEBigSum node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEStoreTo,
// * java.lang.Object)
// */
// public Object visit(MAEStoreTo node, Object data) {
// return null;
// }
//
// /**
// * @see
// uk.ac.manchester.mae.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.MAEPropertyFacet,
// * java.lang.Object)
// */
// public Object visit(MAEPropertyFacet node, Object data) {
// return null;
// }
// }
