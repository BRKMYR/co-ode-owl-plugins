/*
* Copyright (C) 2007, University of Manchester
*/
package org.coode.taxonomy;

import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.ui.view.cls.AbstractOWLClassViewComponent;
import org.protege.editor.owl.ui.OWLObjectComparator;
import org.semanticweb.owlapi.model.OWLClass;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jan 16, 2008<br><br>
 *
 * Used to generate inf/asserted hierarchy of classes - sorted URIs
 */
public class InferredTabbedHierarchyView extends AbstractOWLClassViewComponent {

    private JTextArea namesComponent;

    // convenience class for querying the asserted subsumption hierarchy directly
    private OWLObjectHierarchyProvider<OWLClass> hp;


    // create the GUI
    public void initialiseClassView() throws Exception {
        setLayout(new BorderLayout(6, 6));
        // in our implementation, just create a simple text area in a scrollpane
        namesComponent = new JTextArea();
        namesComponent.setTabSize(2);
        add(new JScrollPane(namesComponent), BorderLayout.CENTER);
    }

    // called automatically when the global selection changes
    protected OWLClass updateView(OWLClass selectedClass) {
        namesComponent.setText("");
        if (selectedClass != null){
            hp = getOWLModelManager().getOWLHierarchyManager().getInferredOWLClassHierarchyProvider();
            render(selectedClass, 0);
        }
        return selectedClass;
    }

    // render the class and recursively all of its subclasses
    private void render(OWLClass selectedClass, int indent) {
        for (int i=0; i<indent; i++){
            namesComponent.append("\t");
        }
        namesComponent.append(selectedClass.getIRI().toString());
        namesComponent.append("\n");


        // the hierarchy provider gets subclasses for us
        final java.util.List<OWLClass> children = new ArrayList<OWLClass>(hp.getChildren(selectedClass));
        Collections.sort(children, getOWLModelManager().getOWLObjectComparator());
        for (OWLClass sub: children){
            render(sub, indent+1);
        }
    }

    // remove any listeners and perform tidyup (none required in this case)
    public void disposeView() {
    }
}