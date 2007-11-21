package org.coode.existentialtree.model2;

import org.coode.existentialtree.util.AxiomAccumulator;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*
* Copyright (C) 2007, University of Manchester
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

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Oct 29, 2007<br><br>
 */
public class OWLDescriptionNode implements ExistentialNode<OWLDescription> {

    private OWLDescription descr;
    private List<ExistentialNode> children;
    private OWLExistentialTreeModel model;

    public OWLDescriptionNode(OWLDescription descr, OWLExistentialTreeModel model){
        this.descr = descr;
        this.model = model;
    }

    public OWLDescription getUserObject() {
        return descr;
    }

    public List<ExistentialNode> getChildren() {
        if (children == null){
            children = new ArrayList<ExistentialNode>();
            AxiomAccumulator acc = new AxiomAccumulator(descr, model.getOntologies());
            Set<OWLObject> objects = acc.getObjectsForDescription();
            if (!objects.isEmpty()){
                Set<OWLObjectProperty> filterproperties = model.getProperties();
                Set<OWLObjectPropertyExpression> properties;
                if (filterproperties == null){
                    properties = acc.getUsedProperties();
                }
                else{
                    properties = new HashSet<OWLObjectPropertyExpression>(filterproperties);
                }
                for (OWLObjectPropertyExpression prop : properties){
                    Set<OWLObject> owlAxioms = acc.filterObjectsForProp(prop);
                    if (owlAxioms != null){
                        children.add(new PropertyNode(owlAxioms, this, prop, model));
                    }
                }
            }
        }
        return children;
    }

    public String toString() {
        return getUserObject().toString();
    }


    public boolean equals(Object object) {
        return descr.equals(((OWLDescriptionNode)object).getUserObject());
    }
}