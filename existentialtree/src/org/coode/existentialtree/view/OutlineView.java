package org.coode.existentialtree.view;

import org.coode.existentialtree.model2.OWLDescriptionNode;
import org.coode.existentialtree.model2.OWLPropertyNode;
import org.coode.existentialtree.model2.OutlineNode;
import org.coode.existentialtree.model2.OutlineTreeModel;
import org.coode.existentialtree.ui.ExistentialTree;
import org.coode.existentialtree.util.ExistentialNodeComparator;
import org.protege.editor.core.ui.view.DisposableAction;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.UIHelper;
import org.protege.editor.owl.ui.renderer.OWLEntityRenderer;
import org.protege.editor.owl.ui.renderer.OWLObjectRenderer;
import org.protege.editor.owl.ui.view.AbstractOWLClassViewComponent;
import org.semanticweb.owl.model.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.HashSet;
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
public class OutlineView extends AbstractOWLClassViewComponent {

    private OutlineTreeModel model;
    private ExistentialTree tree;

    private OutlineNode currentSelection; // @@TODO or shall we just ask the tree for this when needed?

    private static final String ALL_PROPERTIES = "all properties";

    private String propertyLabel = ALL_PROPERTIES;

    private boolean ignoreUpdateView = false;

    private boolean requiresRefresh = false;

    private TreeSelectionListener treeSelListener = new TreeSelectionListener(){
        public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
            OutlineNode node = (OutlineNode)treeSelectionEvent.getPath().getLastPathComponent();
            handleNodeSelection(node);
        }
    };

    private OWLOntologyChangeListener ontListener = new OWLOntologyChangeListener(){
        public void ontologiesChanged(java.util.List<? extends OWLOntologyChange> changes) throws OWLException {
            refresh();
        }
    };

    private HierarchyListener hListener = new HierarchyListener(){
        public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
            if (requiresRefresh){
                refresh();
            }
        }
    };

    private DisposableAction addExistentialRestrictionAction = new DisposableAction("Add Existential Restriction", OWLIcons.getIcon("class.add.sub.png")){
        public void actionPerformed(ActionEvent actionEvent) {
            handleAddNode();
        }
        public void dispose() {
        }
    };

    private DisposableAction selectObjectPropertyAction = new DisposableAction("Select Object Property", OWLIcons.getIcon("property.object.png")){
        public void dispose() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            handleSelectObjectProperty();
        }
    };

    private DisposableAction selectDataPropertyAction = new DisposableAction("Select Data Property", OWLIcons.getIcon("property.data.png")){
        public void dispose() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            handleSelectDataProperty();
        }
    };

    private DisposableAction clearPropertyAction = new DisposableAction("Clear Property", OWLIcons.getIcon("property.object.delete.png")){
        public void dispose() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            handleClearProperty();
        }
    };

    private DisposableAction showMinZeroAction = new DisposableAction("Show min 0", null){
        public void dispose() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            handleMinZeroToggle();
        }
    };


    public void initialiseClassView() throws Exception {
        setLayout(new BorderLayout());

        OWLModelManager mngr = getOWLModelManager();
        model = new OutlineTreeModel(mngr.getOWLOntologyManager(),
                mngr.getActiveOntologies(),
                new ExistentialNodeComparator(mngr));

        getOWLModelManager().addOntologyChangeListener(ontListener);

        getOWLWorkspace().addHierarchyListener(hListener);

        addAction(addExistentialRestrictionAction, "A", "A");
        addAction(selectObjectPropertyAction, "B", "A");
        addAction(selectDataPropertyAction, "B", "B");
        addAction(clearPropertyAction, "B", "C");
        addAction(showMinZeroAction, "C", "A");

        clearPropertyAction.setEnabled(false);
    }

    public void disposeView() {
        tree.getSelectionModel().removeTreeSelectionListener(treeSelListener);
        getOWLModelManager().removeOntologyChangeListener(ontListener);
        getOWLWorkspace().removeHierarchyListener(hListener);

        model = null;
        tree = null;
    }


    private void refresh() {
        if (isShowing()){
            OWLClass selectedOWLClass = getSelectedOWLClass();
            if (selectedOWLClass != null){
                model.setRoot(selectedOWLClass);
                if (tree == null){
                    tree = new ExistentialTree(model, getOWLEditorKit());
                    tree.getSelectionModel().addTreeSelectionListener(treeSelListener);

                    add(new JScrollPane(tree), BorderLayout.CENTER);
                }

                expandFirstChildren();
            }
            requiresRefresh = false;
        }
        else{
            requiresRefresh = true;
        }
    }

    private void expandFirstChildren() {
        for (TreePath path : getFirstChildrenPaths()){
            tree.expandPath(path);
        }
    }

    private Set<TreePath> getFirstChildrenPaths() {
        Set<TreePath> paths = new HashSet<TreePath>();
        for (int i=tree.getRowCount()-1; i>0; i--){
            paths.add(tree.getPathForRow(i));
        }
        return paths;
    }

    protected OWLClass updateView(OWLClass selectedClass) {
        if (!ignoreUpdateView){
            refresh();
        }
        ignoreUpdateView = false;
        return selectedClass;
    }

    protected void updateHeader(OWLObject object) {
        String str = "(" + propertyLabel + ")";
        if (object != null){
            final OWLObjectRenderer owlObjectRenderer = getOWLModelManager().getOWLObjectRenderer();
            final OWLEntityRenderer owlEntityRenderer = getOWLModelManager().getOWLEntityRenderer();
            str += " " + owlObjectRenderer.render(object, owlEntityRenderer);
        }
        getView().setHeaderText(str);
    }

    private Set<OWLProperty> generateAllDescendants(OWLProperty prop) {
        Set<OWLProperty> propAndDescendants = null;
        if (prop != null){
            if (prop instanceof OWLObjectProperty){
                OWLObjectHierarchyProvider<OWLObjectProperty> ohp = getOWLModelManager().getOWLObjectPropertyHierarchyProvider();
                propAndDescendants = new HashSet<OWLProperty>(ohp.getDescendants((OWLObjectProperty)prop));
                propAndDescendants.add(prop);
            }
            else if (prop instanceof OWLDataProperty){
                OWLObjectHierarchyProvider<OWLDataProperty> dhp = getOWLModelManager().getOWLDataPropertyHierarchyProvider();
                propAndDescendants = new HashSet<OWLProperty>(dhp.getDescendants((OWLDataProperty)prop));
                propAndDescendants.add(prop);
            }
        }
        return propAndDescendants;
    }


    private void handleAddNode() {
        String errMessage = null;
        final UIHelper uiHelper = new UIHelper(getOWLEditorKit());
        if (currentSelection != null){
            if (currentSelection instanceof OWLPropertyNode){
                OWLPropertyNode node = (OWLPropertyNode)currentSelection;
                OWLDescription descr = node.getParent().getUserObject();
                // for now, only add existentials to named classes
                if (descr instanceof OWLClass){
                    OWLPropertyExpression property = node.getProperty();
                    if (property instanceof OWLObjectProperty){
                        OWLClass filler = uiHelper.pickOWLClass();
                        if (filler != null){
                            OutlineNode newNode = model.createNode(filler, currentSelection);
                            try {
                                model.add(newNode, currentSelection, getOWLModelManager().getActiveOntology());
                            } catch (OWLOntologyChangeException e) {
                                errMessage = e.getMessage();
                            }
                        }
                    }
                }
                else{
                    errMessage = "Cannot currently add a node into an anonymous class";
                    // would need to create a new expression with the additional existential in
                }
            }
            else if (currentSelection instanceof OWLDescriptionNode){
                OWLDescriptionNode node = (OWLDescriptionNode)currentSelection;
                OWLDescription descr = node.getUserObject();
                if (descr instanceof OWLClass){
                    OWLPropertyExpression property = pickProperty();
                    if (property != null){
                        OutlineNode newNode = model.createNode(property, currentSelection);
                        try {
                            model.add(newNode, currentSelection, getOWLModelManager().getActiveOntology());
                        } catch (OWLOntologyChangeException e) {
                            errMessage = e.getMessage();
                        }
                    }
                }
                else{
                    errMessage = "Cannot currently add a node into an anonymous class";
                    // would need to create a new expression with the additional existential in
                }

            }
        }
        else{
            errMessage = "Please select a property or class in the tree first";
        }

        if (errMessage != null){
            uiHelper.showDialog(errMessage, null);
        }
    }

    private OWLPropertyExpression pickProperty() {
        // @@TODO have a dialog with either obj or data properties
        return new UIHelper(getOWLEditorKit()).pickOWLObjectProperty();
    }


    private void handleNodeSelection(OutlineNode node) {
        this.currentSelection = node;
        OWLObject owlObject = node.getRenderedObject();
        if (owlObject instanceof OWLClass){
            ignoreUpdateView = true;
            getOWLWorkspace().getOWLSelectionModel().setSelectedEntity((OWLEntity)owlObject);
        }
    }

    private void handleSelectObjectProperty() {
        OWLProperty prop = new UIHelper(getOWLEditorKit()).pickOWLObjectProperty();
        handleSelectProperty(prop);
    }

    private void handleSelectDataProperty() {
        OWLProperty prop = new UIHelper(getOWLEditorKit()).pickOWLDataProperty();
        handleSelectProperty(prop);
    }

    private void handleSelectProperty(OWLProperty prop) {
        if (prop != null){
            propertyLabel = getOWLModelManager().getOWLEntityRenderer().render(prop);
            clearPropertyAction.setEnabled(true);
            model.setProperties(new HashSet<OWLPropertyExpression>(generateAllDescendants(prop)));
            refresh();
            updateHeader(getSelectedOWLClass());
            // change clearProp
        }
    }

    private void handleClearProperty() {
        propertyLabel = ALL_PROPERTIES;
        model.setProperties(null);
        refresh();
        updateHeader(getSelectedOWLClass());
        setEnabled(false);
    }

    private void handleMinZeroToggle() {
        if (model.getMin() == 0){
            model.setMin(1);
        }
        else{
            model.setMin(0);
        }
    }
}