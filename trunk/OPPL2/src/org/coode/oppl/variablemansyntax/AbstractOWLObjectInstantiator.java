package org.coode.oppl.variablemansyntax;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.coode.oppl.variablemansyntax.bindingtree.BindingNode;
import org.semanticweb.owl.model.OWLAntiSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLAxiomAnnotationAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLConstantAnnotation;
import org.semanticweb.owl.model.OWLDataAllRestriction;
import org.semanticweb.owl.model.OWLDataComplementOf;
import org.semanticweb.owl.model.OWLDataExactCardinalityRestriction;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataMaxCardinalityRestriction;
import org.semanticweb.owl.model.OWLDataMinCardinalityRestriction;
import org.semanticweb.owl.model.OWLDataOneOf;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDataRangeFacetRestriction;
import org.semanticweb.owl.model.OWLDataRangeRestriction;
import org.semanticweb.owl.model.OWLDataSomeRestriction;
import org.semanticweb.owl.model.OWLDataSubPropertyAxiom;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLDataValueRestriction;
import org.semanticweb.owl.model.OWLDeclarationAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointUnionAxiom;
import org.semanticweb.owl.model.OWLEntityAnnotationAxiom;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owl.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLImportsDeclaration;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectAllRestriction;
import org.semanticweb.owl.model.OWLObjectAnnotation;
import org.semanticweb.owl.model.OWLObjectComplementOf;
import org.semanticweb.owl.model.OWLObjectExactCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectIntersectionOf;
import org.semanticweb.owl.model.OWLObjectMaxCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectMinCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectOneOf;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyChainSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectPropertyInverse;
import org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLObjectSelfRestriction;
import org.semanticweb.owl.model.OWLObjectSomeRestriction;
import org.semanticweb.owl.model.OWLObjectSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectUnionOf;
import org.semanticweb.owl.model.OWLObjectValueRestriction;
import org.semanticweb.owl.model.OWLObjectVisitorEx;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyAnnotationAxiom;
import org.semanticweb.owl.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLSameIndividualsAxiom;
import org.semanticweb.owl.model.OWLSubClassAxiom;
import org.semanticweb.owl.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLTypedConstant;
import org.semanticweb.owl.model.OWLUntypedConstant;
import org.semanticweb.owl.model.SWRLAtomConstantObject;
import org.semanticweb.owl.model.SWRLAtomDVariable;
import org.semanticweb.owl.model.SWRLAtomIVariable;
import org.semanticweb.owl.model.SWRLAtomIndividualObject;
import org.semanticweb.owl.model.SWRLBuiltInAtom;
import org.semanticweb.owl.model.SWRLClassAtom;
import org.semanticweb.owl.model.SWRLDataRangeAtom;
import org.semanticweb.owl.model.SWRLDataValuedPropertyAtom;
import org.semanticweb.owl.model.SWRLDifferentFromAtom;
import org.semanticweb.owl.model.SWRLObjectPropertyAtom;
import org.semanticweb.owl.model.SWRLRule;
import org.semanticweb.owl.model.SWRLSameAsAtom;

abstract class AbstractOWLObjectInstantiator implements
		OWLObjectVisitorEx<OWLObject> {
	protected final BindingNode bindingNode;
	protected final ConstraintSystem constraintSystem;
	private final OWLDataFactory df;

	protected AbstractOWLObjectInstantiator(BindingNode bindingNode,
			ConstraintSystem cs) {
		this.bindingNode = bindingNode;
		this.constraintSystem = cs;
		this.df = this.constraintSystem.getDataFactory();
	}

	public OWLObject visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		return this.df
				.getOWLAntiSymmetricObjectPropertyAxiom((OWLObjectPropertyExpression) property
						.accept(this));
	}

	public OWLObject visit(OWLAxiomAnnotationAxiom axiom) {
		return axiom;
	}

	public OWLDescription visit(OWLClass desc) {
		OWLDescription toReturn = null;
		if (this.constraintSystem.isVariable(desc)) {
			Variable variable = this.constraintSystem
					.getVariable(desc.getURI());
			OWLDescription assignmentValue = (OWLDescription) this.bindingNode
					.getAssignmentValue(variable);
			toReturn = assignmentValue == null ? desc : assignmentValue;
		} else {
			toReturn = desc;
		}
		return toReturn;
	}

	public OWLObject visit(OWLClassAssertionAxiom axiom) {
		OWLDescription description = axiom.getDescription();
		OWLIndividual individual = axiom.getIndividual();
		return this.df.getOWLClassAssertionAxiom((OWLIndividual) individual
				.accept(this), (OWLDescription) description.accept(this));
	}

	public OWLObject visit(OWLConstantAnnotation annotation) {
		return annotation;
	}

	public OWLObject visit(OWLDataAllRestriction desc) {
		OWLDataRange filler = desc.getFiller();
		OWLDataPropertyExpression property = desc.getProperty();
		return this.df.getOWLDataAllRestriction(
				(OWLDataPropertyExpression) property.accept(this),
				(OWLDataRange) filler.accept(this));
	}

	public OWLObject visit(OWLDataComplementOf node) {
		OWLDataRange dataRange = node.getDataRange();
		return this.df.getOWLDataComplementOf((OWLDataRange) dataRange
				.accept(this));
	}

	public OWLObject visit(OWLDataExactCardinalityRestriction desc) {
		int cardinality = desc.getCardinality();
		OWLDataRange filler = desc.getFiller();
		OWLDataPropertyExpression property = desc.getProperty();
		return this.df.getOWLDataExactCardinalityRestriction(
				(OWLDataPropertyExpression) property.accept(this), cardinality,
				(OWLDataRange) filler.accept(this));
	}

	public OWLObject visit(OWLDataMaxCardinalityRestriction desc) {
		int cardinality = desc.getCardinality();
		OWLDataRange filler = desc.getFiller();
		OWLDataPropertyExpression property = desc.getProperty();
		return this.df.getOWLDataMaxCardinalityRestriction(
				(OWLDataPropertyExpression) property.accept(this), cardinality,
				(OWLDataRange) filler.accept(this));
	}

	public OWLObject visit(OWLDataMinCardinalityRestriction desc) {
		int cardinality = desc.getCardinality();
		OWLDataRange filler = desc.getFiller();
		OWLDataPropertyExpression property = desc.getProperty();
		return this.df.getOWLDataMinCardinalityRestriction(
				(OWLDataPropertyExpression) property.accept(this), cardinality,
				(OWLDataRange) filler.accept(this));
	}

	public OWLObject visit(OWLDataOneOf node) {
		Set<OWLConstant> values = node.getValues();
		Set<OWLConstant> instantiatedValues = new HashSet<OWLConstant>();
		for (OWLConstant constant : values) {
			instantiatedValues.add((OWLConstant) constant.accept(this));
		}
		return this.df.getOWLDataOneOf(instantiatedValues);
	}

	public OWLObject visit(OWLDataProperty property) {
		OWLDataProperty toReturn = property;
		if (this.constraintSystem.isVariable(property)) {
			Variable variable = this.constraintSystem.getVariable(property
					.getURI());
			OWLDataProperty assignmentValue = (OWLDataProperty) this.bindingNode
					.getAssignmentValue(variable);
			toReturn = assignmentValue == null ? property : assignmentValue;
		}
		return toReturn;
	}

	public OWLObject visit(OWLDataPropertyAssertionAxiom axiom) {
		OWLIndividual subject = axiom.getSubject();
		OWLDataPropertyExpression property = axiom.getProperty();
		OWLConstant object = axiom.getObject();
		return this.df.getOWLDataPropertyAssertionAxiom((OWLIndividual) subject
				.accept(this), (OWLDataPropertyExpression) property
				.accept(this), (OWLConstant) object.accept(this));
	}

	public OWLObject visit(OWLDataPropertyDomainAxiom axiom) {
		OWLDescription domain = axiom.getDomain();
		OWLDataPropertyExpression property = axiom.getProperty();
		return this.df.getOWLDataPropertyDomainAxiom(
				(OWLDataPropertyExpression) property.accept(this),
				(OWLDescription) domain.accept(this));
	}

	public OWLObject visit(OWLDataPropertyRangeAxiom axiom) {
		OWLDataPropertyExpression property = axiom.getProperty();
		OWLDataRange range = axiom.getRange();
		return this.df.getOWLDataPropertyRangeAxiom(
				(OWLDataPropertyExpression) property.accept(this),
				(OWLDataRange) range.accept(this));
	}

	public OWLObject visit(OWLDataRangeFacetRestriction node) {
		return node;
	}

	public OWLObject visit(OWLDataRangeRestriction node) {
		OWLDataRange dataRange = node.getDataRange();
		Set<OWLDataRangeFacetRestriction> facetRestrictions = node
				.getFacetRestrictions();
		return this.df.getOWLDataRangeRestriction((OWLDataRange) dataRange
				.accept(this), facetRestrictions);
	}

	public OWLObject visit(OWLDataSomeRestriction desc) {
		OWLDataRange filler = desc.getFiller();
		OWLDataPropertyExpression property = desc.getProperty();
		return this.df.getOWLDataSomeRestriction(
				(OWLDataPropertyExpression) property.accept(this),
				(OWLDataRange) filler.accept(this));
	}

	public OWLObject visit(OWLDataSubPropertyAxiom axiom) {
		OWLDataPropertyExpression subProperty = axiom.getSubProperty();
		OWLDataPropertyExpression superProperty = axiom.getSuperProperty();
		return this.df.getOWLSubDataPropertyAxiom(
				(OWLDataPropertyExpression) subProperty.accept(this),
				(OWLDataPropertyExpression) superProperty.accept(this));
	}

	public OWLObject visit(OWLDataType node) {
		return node;
	}

	public OWLObject visit(OWLDataValueRestriction desc) {
		OWLDataPropertyExpression property = desc.getProperty();
		OWLConstant value = desc.getValue();
		return this.df.getOWLDataValueRestriction(
				(OWLDataPropertyExpression) property.accept(this),
				(OWLConstant) value.accept(this));
	}

	public OWLObject visit(OWLDeclarationAxiom axiom) {
		return axiom;
	}

	public OWLObject visit(OWLDifferentIndividualsAxiom axiom) {
		Set<OWLIndividual> individuals = axiom.getIndividuals();
		Set<OWLIndividual> instantiatedIndividuals = new HashSet<OWLIndividual>();
		for (OWLIndividual individual : individuals) {
			instantiatedIndividuals
					.add((OWLIndividual) individual.accept(this));
		}
		return this.df.getOWLDifferentIndividualsAxiom(instantiatedIndividuals);
	}

	public OWLObject visit(OWLDisjointClassesAxiom axiom) {
		Set<OWLDescription> descriptions = axiom.getDescriptions();
		Set<OWLDescription> instatiatedDescriptions = new HashSet<OWLDescription>();
		for (OWLDescription description : descriptions) {
			instatiatedDescriptions.add((OWLDescription) description
					.accept(this));
		}
		return this.df.getOWLDisjointClassesAxiom(instatiatedDescriptions);
	}

	public OWLObject visit(OWLDisjointDataPropertiesAxiom axiom) {
		Set<OWLDataPropertyExpression> properties = axiom.getProperties();
		Set<OWLDataPropertyExpression> instantiatedProperties = new HashSet<OWLDataPropertyExpression>();
		for (OWLDataPropertyExpression objectPropertyExpression : properties) {
			instantiatedProperties
					.add((OWLDataPropertyExpression) objectPropertyExpression
							.accept(this));
		}
		return this.df
				.getOWLDisjointDataPropertiesAxiom(instantiatedProperties);
	}

	public OWLObject visit(OWLDisjointObjectPropertiesAxiom axiom) {
		Set<OWLObjectPropertyExpression> properties = axiom.getProperties();
		Set<OWLObjectPropertyExpression> instantiatedProperties = new HashSet<OWLObjectPropertyExpression>();
		for (OWLObjectPropertyExpression objectPropertyExpression : properties) {
			instantiatedProperties
					.add((OWLObjectPropertyExpression) objectPropertyExpression
							.accept(this));
		}
		return this.df
				.getOWLDisjointObjectPropertiesAxiom(instantiatedProperties);
	}

	public OWLObject visit(OWLDisjointUnionAxiom axiom) {
		Set<OWLDescription> descriptions = axiom.getDescriptions();
		OWLClass owlClass = axiom.getOWLClass();
		Set<OWLDescription> instantiatedDescriptions = axiom.getDescriptions();
		for (OWLDescription description : descriptions) {
			instantiatedDescriptions.add((OWLDescription) description
					.accept(this));
		}
		return this.df.getOWLDisjointUnionAxiom((OWLClass) owlClass
				.accept(this), instantiatedDescriptions);
	}

	public OWLObject visit(OWLEntityAnnotationAxiom axiom) {
		return axiom;
	}

	public OWLObject visit(OWLEquivalentClassesAxiom axiom) {
		Set<OWLDescription> descriptions = axiom.getDescriptions();
		Set<OWLDescription> instantiatedDescriptions = new HashSet<OWLDescription>();
		for (OWLDescription description : descriptions) {
			instantiatedDescriptions.add((OWLDescription) description
					.accept(this));
		}
		return this.df.getOWLEquivalentClassesAxiom(instantiatedDescriptions);
	}

	public OWLObject visit(OWLEquivalentDataPropertiesAxiom axiom) {
		Set<OWLDataPropertyExpression> properties = axiom.getProperties();
		Set<OWLDataPropertyExpression> instantiatedProperties = new HashSet<OWLDataPropertyExpression>();
		for (OWLDataPropertyExpression dataPropertyExpression : properties) {
			instantiatedProperties
					.add((OWLDataPropertyExpression) dataPropertyExpression
							.accept(this));
		}
		return this.df
				.getOWLEquivalentDataPropertiesAxiom(instantiatedProperties);
	}

	public OWLObject visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		Set<OWLObjectPropertyExpression> properties = axiom.getProperties();
		Set<OWLObjectPropertyExpression> instantiatedProperties = new HashSet<OWLObjectPropertyExpression>();
		for (OWLObjectPropertyExpression objectPropertyExpression : properties) {
			instantiatedProperties
					.add((OWLObjectPropertyExpression) objectPropertyExpression
							.accept(this));
		}
		return this.df
				.getOWLEquivalentObjectPropertiesAxiom(instantiatedProperties);
	}

	public OWLObject visit(OWLFunctionalDataPropertyAxiom axiom) {
		OWLDataPropertyExpression property = axiom.getProperty();
		return this.df
				.getOWLFunctionalDataPropertyAxiom((OWLDataPropertyExpression) property
						.accept(this));
	}

	public OWLObject visit(OWLFunctionalObjectPropertyAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		return this.df
				.getOWLFunctionalObjectPropertyAxiom((OWLObjectPropertyExpression) property
						.accept(this));
	}

	public OWLObject visit(OWLImportsDeclaration axiom) {
		return axiom;
	}

	public OWLObject visit(OWLIndividual individual) {
		OWLIndividual toReturn = individual;
		if (this.constraintSystem.isVariable(individual)) {
			Variable variable = this.constraintSystem.getVariable(individual
					.getURI());
			OWLIndividual assignmentValue = (OWLIndividual) this.bindingNode
					.getAssignmentValue(variable);
			toReturn = assignmentValue == null ? individual : assignmentValue;
		}
		return toReturn;
	}

	public OWLObject visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		return this.df
				.getOWLInverseFunctionalObjectPropertyAxiom((OWLObjectPropertyExpression) property
						.accept(this));
	}

	public OWLObject visit(OWLInverseObjectPropertiesAxiom axiom) {
		OWLObjectPropertyExpression firstProperty = axiom.getFirstProperty();
		OWLObjectPropertyExpression secondProperty = axiom.getSecondProperty();
		return this.df.getOWLInverseObjectPropertiesAxiom(
				(OWLObjectPropertyExpression) firstProperty.accept(this),
				(OWLObjectPropertyExpression) secondProperty.accept(this));
	}

	public OWLObject visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		return this.df
				.getOWLIrreflexiveObjectPropertyAxiom((OWLObjectPropertyExpression) property
						.accept(this));
	}

	public OWLObject visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		OWLDataPropertyExpression property = axiom.getProperty();
		OWLIndividual subject = axiom.getSubject();
		OWLConstant object = axiom.getObject();
		return this.df.getOWLNegativeDataPropertyAssertionAxiom(
				(OWLIndividual) subject.accept(this),
				(OWLDataPropertyExpression) property.accept(this),
				(OWLConstant) object.accept(this));
	}

	public OWLObject visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		OWLIndividual subject = axiom.getSubject();
		OWLIndividual object = axiom.getObject();
		OWLIndividual instantiatedSubject = (OWLIndividual) subject
				.accept(this);
		OWLObjectPropertyExpression instantiatedProperty = (OWLObjectPropertyExpression) property
				.accept(this);
		OWLIndividual instantiatedObject = (OWLIndividual) object.accept(this);
		return this.df.getOWLNegativeObjectPropertyAssertionAxiom(
				instantiatedSubject, instantiatedProperty, instantiatedObject);
	}

	public OWLDescription visit(OWLObjectAllRestriction desc) {
		OWLDescription filler = desc.getFiller();
		OWLObjectPropertyExpression property = desc.getProperty();
		return this.df.getOWLObjectAllRestriction(
				(OWLObjectPropertyExpression) property.accept(this),
				(OWLDescription) filler.accept(this));
	}

	public OWLObject visit(OWLObjectAnnotation annotation) {
		return annotation;
	}

	public OWLDescription visit(OWLObjectComplementOf desc) {
		OWLDescription operand = desc.getOperand();
		return this.df.getOWLObjectComplementOf((OWLDescription) operand
				.accept(this));
	}

	public OWLObject visit(OWLObjectExactCardinalityRestriction desc) {
		int cardinality = desc.getCardinality();
		OWLDescription filler = desc.getFiller();
		OWLObjectPropertyExpression property = desc.getProperty();
		return this.df.getOWLObjectExactCardinalityRestriction(
				(OWLObjectPropertyExpression) property.accept(this),
				cardinality, (OWLDescription) filler.accept(this));
	}

	public OWLDescription visit(OWLObjectIntersectionOf desc) {
		Set<OWLDescription> operands = desc.getOperands();
		Set<OWLDescription> instantiatedOperands = new HashSet<OWLDescription>();
		for (OWLDescription description : operands) {
			instantiatedOperands.add((OWLDescription) description.accept(this));
		}
		return this.df.getOWLObjectIntersectionOf(instantiatedOperands);
	}

	public OWLObject visit(OWLObjectMaxCardinalityRestriction desc) {
		int cardinality = desc.getCardinality();
		OWLDescription filler = desc.getFiller();
		OWLObjectPropertyExpression property = desc.getProperty();
		return this.df.getOWLObjectMaxCardinalityRestriction(
				(OWLObjectPropertyExpression) property.accept(this),
				cardinality, (OWLDescription) filler.accept(this));
	}

	public OWLObject visit(OWLObjectMinCardinalityRestriction desc) {
		int cardinality = desc.getCardinality();
		OWLDescription filler = desc.getFiller();
		OWLObjectPropertyExpression property = desc.getProperty();
		return this.df.getOWLObjectMinCardinalityRestriction(
				(OWLObjectPropertyExpression) property.accept(this),
				cardinality, (OWLDescription) filler.accept(this));
	}

	public OWLObject visit(OWLObjectOneOf desc) {
		Set<OWLIndividual> individuals = desc.getIndividuals();
		Set<OWLIndividual> instantiatedIndividuals = new HashSet<OWLIndividual>();
		for (OWLIndividual individual : individuals) {
			instantiatedIndividuals
					.add((OWLIndividual) individual.accept(this));
		}
		return this.df.getOWLObjectOneOf(instantiatedIndividuals);
	}

	public OWLObject visit(OWLObjectProperty property) {
		OWLObjectProperty toReturn = property;
		if (this.constraintSystem.isVariable(property)) {
			Variable variable = this.constraintSystem.getVariable(property
					.getURI());
			OWLObjectProperty assignmentValue = (OWLObjectProperty) this.bindingNode
					.getAssignmentValue(variable);
			toReturn = assignmentValue == null ? property : assignmentValue;
		}
		return toReturn;
	}

	public OWLObject visit(OWLObjectPropertyAssertionAxiom axiom) {
		OWLIndividual subject = axiom.getSubject();
		OWLObjectPropertyExpression property = axiom.getProperty();
		OWLIndividual object = axiom.getObject();
		return this.df.getOWLObjectPropertyAssertionAxiom(
				(OWLIndividual) subject.accept(this),
				(OWLObjectPropertyExpression) property.accept(this),
				(OWLIndividual) object.accept(this));
	}

	public OWLObject visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
		List<OWLObjectPropertyExpression> propertyChain = axiom
				.getPropertyChain();
		List<OWLObjectPropertyExpression> instantiatedPropertyChain = axiom
				.getPropertyChain();
		OWLObjectPropertyExpression superProperty = axiom.getSuperProperty();
		for (OWLObjectPropertyExpression objectPropertyExpression : propertyChain) {
			instantiatedPropertyChain
					.add((OWLObjectPropertyExpression) objectPropertyExpression
							.accept(this));
		}
		return this.df.getOWLObjectPropertyChainSubPropertyAxiom(
				instantiatedPropertyChain,
				(OWLObjectPropertyExpression) superProperty.accept(this));
	}

	public OWLObject visit(OWLObjectPropertyDomainAxiom axiom) {
		OWLDescription domain = axiom.getDomain();
		OWLObjectPropertyExpression property = axiom.getProperty();
		return this.df.getOWLObjectPropertyDomainAxiom(
				(OWLObjectPropertyExpression) property.accept(this),
				(OWLDescription) domain.accept(this));
	}

	public OWLObject visit(OWLObjectPropertyInverse property) {
		OWLObjectPropertyExpression inverse = property.getInverse();
		return this.df
				.getOWLObjectPropertyInverse((OWLObjectPropertyExpression) inverse
						.accept(this));
	}

	public OWLObject visit(OWLObjectPropertyRangeAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		OWLDescription range = axiom.getRange();
		return this.df.getOWLObjectPropertyRangeAxiom(
				(OWLObjectPropertyExpression) property.accept(this),
				(OWLDescription) range.accept(this));
	}

	public OWLObject visit(OWLObjectSelfRestriction desc) {
		OWLObjectPropertyExpression property = desc.getProperty();
		return this.df
				.getOWLObjectSelfRestriction((OWLObjectPropertyExpression) property
						.accept(this));
	}

	public OWLDescription visit(OWLObjectSomeRestriction desc) {
		OWLDescription filler = desc.getFiller();
		OWLObjectPropertyExpression property = desc.getProperty();
		return this.df.getOWLObjectSomeRestriction(
				(OWLObjectPropertyExpression) property.accept(this),
				(OWLDescription) filler.accept(this));
	}

	public OWLObject visit(OWLObjectSubPropertyAxiom axiom) {
		OWLObjectPropertyExpression subProperty = axiom.getSubProperty();
		OWLObjectPropertyExpression superProperty = axiom.getSuperProperty();
		return this.df.getOWLSubObjectPropertyAxiom(
				(OWLObjectPropertyExpression) subProperty.accept(this),
				(OWLObjectPropertyExpression) superProperty.accept(this));
	}

	public OWLDescription visit(OWLObjectUnionOf desc) {
		Set<OWLDescription> operands = desc.getOperands();
		Set<OWLDescription> instantiatedOperands = new HashSet<OWLDescription>();
		for (OWLDescription description : operands) {
			instantiatedOperands.add((OWLDescription) description.accept(this));
		}
		return this.df.getOWLObjectUnionOf(instantiatedOperands);
	}

	public OWLDescription visit(OWLObjectValueRestriction desc) {
		OWLObjectPropertyExpression property = desc.getProperty();
		OWLIndividual value = desc.getValue();
		return this.df.getOWLObjectValueRestriction(
				(OWLObjectPropertyExpression) property.accept(this),
				(OWLIndividual) value.accept(this));
	}

	public OWLObject visit(OWLOntology ontology) {
		return ontology;
	}

	public OWLObject visit(OWLOntologyAnnotationAxiom axiom) {
		return axiom;
	}

	public OWLObject visit(OWLReflexiveObjectPropertyAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		return this.df
				.getOWLReflexiveObjectPropertyAxiom((OWLObjectPropertyExpression) property
						.accept(this));
	}

	public OWLObject visit(OWLSameIndividualsAxiom axiom) {
		Set<OWLIndividual> individuals = axiom.getIndividuals();
		Set<OWLIndividual> instantiatedIndividuals = new HashSet<OWLIndividual>(
				axiom.getIndividuals().size());
		for (OWLIndividual individual : individuals) {
			instantiatedIndividuals
					.add((OWLIndividual) individual.accept(this));
		}
		return this.df.getOWLSameIndividualsAxiom(instantiatedIndividuals);
	}

	public OWLObject visit(OWLSubClassAxiom axiom) {
		OWLDescription superClass = (OWLDescription) axiom.getSuperClass()
				.accept(this);
		OWLDescription subClass = (OWLDescription) axiom.getSubClass().accept(
				this);
		return this.df.getOWLSubClassAxiom(subClass, superClass);
	}

	public OWLObject visit(OWLSymmetricObjectPropertyAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		return this.df
				.getOWLSymmetricObjectPropertyAxiom((OWLObjectPropertyExpression) property
						.accept(this));
	}

	public OWLObject visit(OWLTransitiveObjectPropertyAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		return this.df
				.getOWLTransitiveObjectPropertyAxiom((OWLObjectPropertyExpression) property
						.accept(this));
	}

	public abstract OWLObject visit(OWLTypedConstant node);

	public abstract OWLObject visit(OWLUntypedConstant node);

	public OWLObject visit(SWRLAtomConstantObject node) {
		return node;
	}

	public OWLObject visit(SWRLAtomDVariable node) {
		return node;
	}

	public OWLObject visit(SWRLAtomIndividualObject node) {
		return node;
	}

	public OWLObject visit(SWRLAtomIVariable node) {
		return node;
	}

	public OWLObject visit(SWRLBuiltInAtom node) {
		return node;
	}

	public OWLObject visit(SWRLClassAtom node) {
		return node;
	}

	public OWLObject visit(SWRLDataRangeAtom node) {
		return node;
	}

	public OWLObject visit(SWRLDataValuedPropertyAtom node) {
		return node;
	}

	public OWLObject visit(SWRLDifferentFromAtom node) {
		return node;
	}

	public OWLObject visit(SWRLObjectPropertyAtom node) {
		return node;
	}

	public OWLObject visit(SWRLRule rule) {
		return rule;
	}

	public OWLObject visit(SWRLSameAsAtom node) {
		return node;
	}
}