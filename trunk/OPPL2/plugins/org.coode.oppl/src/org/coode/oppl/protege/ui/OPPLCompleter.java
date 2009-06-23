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
package org.coode.oppl.protege.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.coode.oppl.syntax.OPPLParser;
import org.coode.oppl.syntax.ParseException;
import org.coode.oppl.syntax.TokenMgrError;
import org.coode.oppl.utils.ProtegeParserFactory;
import org.coode.oppl.variablemansyntax.Variable;
import org.coode.oppl.variablemansyntax.VariableType;
import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.clsdescriptioneditor.OWLExpressionChecker;
import org.semanticweb.owl.model.OWLObject;

/**
 * @author Luigi Iannone
 * 
 *         Jul 2, 2008
 */
@SuppressWarnings("unchecked")
public class OPPLCompleter {
	private static Logger logger = Logger.getLogger(OPPLCompleter.class
			.getName());
	public static final int DEFAULT_MAX_ENTRIES = 100;
	private OWLEditorKit owlEditorKit;
	private JTextComponent textComponent;
	private KeyListener keyListener;
	private Set<String> wordDelimeters;
	// private AutoCompleterMatcher matcher;
	private JList popupList;
	private JWindow popupWindow;
	public static final int POPUP_WIDTH = 350;
	public static final int POPUP_HEIGHT = 300;
	private String lastTextUpdate = "*";
	private int maxEntries = DEFAULT_MAX_ENTRIES;

	public OPPLCompleter(OWLEditorKit owlEditorKit, JTextComponent tc,
			OWLExpressionChecker checker) {
		this.owlEditorKit = owlEditorKit;
		this.textComponent = tc;
		this.keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				OPPLCompleter.this.processKeyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_UP
						&& e.getKeyCode() != KeyEvent.VK_DOWN) {
					if (OPPLCompleter.this.popupWindow.isVisible()
							&& !OPPLCompleter.this.lastTextUpdate
									.equals(OPPLCompleter.this.textComponent
											.getText())) {
						OPPLCompleter.this.lastTextUpdate = OPPLCompleter.this.textComponent
								.getText();
						OPPLCompleter.this.updatePopup(OPPLCompleter.this
								.getMatches());
					}
				}
			}
		};
		this.textComponent.addKeyListener(this.keyListener);
		this.wordDelimeters = new HashSet<String>();
		this.wordDelimeters.add(" ");
		this.wordDelimeters.add("\n");
		this.wordDelimeters.add("[");
		this.wordDelimeters.add("]");
		this.wordDelimeters.add("{");
		this.wordDelimeters.add("}");
		this.wordDelimeters.add("(");
		this.wordDelimeters.add(")");
		this.wordDelimeters.add(",");
		this.wordDelimeters.add("^");
		this.wordDelimeters.add(":");
		this.wordDelimeters.add("]");
		this.wordDelimeters.add("[");
		this.wordDelimeters.add(",");
		for (VariableType variableType : EnumSet.allOf(VariableType.class)) {
			this.wordDelimeters.add(variableType.toString());
		}
		// this.matcher = new AutoCompleterMatcherImpl(owlEditorKit
		// .getOWLModelManager());
		this.popupList = new JList();
		this.popupList.setAutoscrolls(true);
		this.popupList.setCellRenderer(owlEditorKit.getWorkspace()
				.createOWLCellRenderer());
		this.popupList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					OPPLCompleter.this.completeWithPopupSelection();
				}
			}
		});
		this.popupList.setRequestFocusEnabled(false);
		this.createPopupWindow();
		this.textComponent.addHierarchyListener(new HierarchyListener() {
			/**
			 * Called when the hierarchy has been changed. To discern the actual
			 * type of change, call <code>HierarchyEvent.getChangeFlags()</code>
			 * .
			 * 
			 * @see java.awt.event.HierarchyEvent#getChangeFlags()
			 */
			public void hierarchyChanged(HierarchyEvent e) {
				if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
					OPPLCompleter.this.createPopupWindow();
				}
			}
		});
	}

	private void processKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && e.isControlDown()) {
			// Show popup
			this.performAutoCompletion();
		} else if (e.getKeyCode() == KeyEvent.VK_TAB) {
			e.consume();
			this.performAutoCompletion();
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (this.popupWindow.isVisible()) {
				// Hide popup
				e.consume();
				this.hidePopup();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (this.popupWindow.isVisible()) {
				// Complete
				e.consume();
				this.completeWithPopupSelection();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (this.popupWindow.isVisible()) {
				e.consume();
				this.incrementSelection();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (this.popupWindow.isVisible()) {
				e.consume();
				this.decrementSelection();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.hidePopup();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.hidePopup();
		}
	}

	private void completeWithPopupSelection() {
		if (this.popupWindow.isVisible()) {
			Object selObject = this.popupList.getSelectedValue();
			if (selObject != null) {
				this.insertWord(this.getInsertText(selObject));
				this.hidePopup();
			}
		}
	}

	private List getMatches() {
		ProtegeParserFactory.initParser(this.textComponent.getText(),
				this.owlEditorKit.getModelManager());
		List completions;
		try {
			OPPLParser.Start();
		} catch (ParseException e) {
		} catch (TokenMgrError e) {
		} finally {
			completions = OPPLParser.getCompletions();
		}
		List toReturn = new ArrayList(completions);
		String wordToComplete = this.getWordToComplete();
		if (wordToComplete.length() > 0) {
			for (Object object : completions) {
				if (!object.toString().trim().startsWith(wordToComplete)) {
					toReturn.remove(object);
				}
			}
		}
		return toReturn;
	}

	private void createPopupWindow() {
		JScrollPane sp = ComponentFactory.createScrollPane(this.popupList);
		this.popupWindow = new JWindow((Window) SwingUtilities
				.getAncestorOfClass(Window.class, this.textComponent));
		this.popupWindow.setAlwaysOnTop(true);
		this.popupWindow.getContentPane().setLayout(new BorderLayout());
		this.popupWindow.getContentPane().add(sp, BorderLayout.CENTER);
		this.popupWindow.setFocusableWindowState(false);
	}

	private void performAutoCompletion() {
		List matches = this.getMatches();
		if (matches.size() == 1) {
			// Don't show popup
			this.insertWord(this.getInsertText(matches.iterator().next()));
		} else if (matches.size() > 1) {
			// Show popup
			this.lastTextUpdate = this.textComponent.getText();
			this.showPopup();
			this.updatePopup(matches);
		}
	}

	private void insertWord(String word) {
		try {
			int index = this.getWordIndex();
			int caretIndex = this.textComponent.getCaretPosition();
			this.textComponent.getDocument().remove(index, caretIndex - index);
			this.textComponent.getDocument().insertString(index, word, null);
		} catch (BadLocationException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	private void showPopup() {
		if (this.popupWindow == null) {
			this.createPopupWindow();
		}
		if (!this.popupWindow.isVisible()) {
			this.popupWindow.setSize(POPUP_WIDTH, POPUP_HEIGHT);
			try {
				int wordIndex = this.getWordIndex();
				if (wordIndex < 0) {
					wordIndex = 0;
				}
				Point p = this.textComponent.modelToView(wordIndex)
						.getLocation();
				SwingUtilities.convertPointToScreen(p, this.textComponent);
				p.y = p.y
						+ this.textComponent.getFontMetrics(
								this.textComponent.getFont()).getHeight();
				this.popupWindow.setLocation(p);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			this.popupWindow.setVisible(true);
		}
	}

	private void hidePopup() {
		this.popupWindow.setVisible(false);
		this.popupList.setListData(new Object[0]);
	}

	private void updatePopup(List matches) {
		int count = matches.size();
		if (count > this.maxEntries) {
			count = this.maxEntries;
		}
		if (!matches.isEmpty()) {
			this.popupList.setListData(matches.subList(0, count).toArray());
		} else {
			this.popupList.setListData(matches.toArray());
		}
		this.popupList.setSelectedIndex(0);
		this.popupWindow.setSize(POPUP_WIDTH, POPUP_HEIGHT);
	}

	private void incrementSelection() {
		if (this.popupList.getModel().getSize() > 0) {
			int selIndex = this.popupList.getSelectedIndex();
			selIndex++;
			if (selIndex > this.popupList.getModel().getSize() - 1) {
				selIndex = 0;
			}
			this.popupList.setSelectedIndex(selIndex);
			this.popupList.scrollRectToVisible(this.popupList.getCellBounds(
					selIndex, selIndex));
		}
	}

	private void decrementSelection() {
		if (this.popupList.getModel().getSize() > 0) {
			int selIndex = this.popupList.getSelectedIndex();
			selIndex--;
			if (selIndex < 0) {
				selIndex = this.popupList.getModel().getSize() - 1;
			}
			this.popupList.setSelectedIndex(selIndex);
			this.popupList.scrollRectToVisible(this.popupList.getCellBounds(
					selIndex, selIndex));
		}
	}

	private int getWordIndex() {
		// try {
		int caretPos = this.textComponent.getCaretPosition();
		String string2Caret = this.textComponent.getText().substring(0,
				caretPos);
		int index = -1;
		Iterator<String> it = this.wordDelimeters.iterator();
		while (it.hasNext()) {
			String delimiter = it.next();
			int delimiterIndex = string2Caret.lastIndexOf(delimiter);
			index = delimiterIndex >= index && delimiterIndex != -1 ? delimiterIndex
					+ delimiter.length()
					: index;
		}
		return index == -1 ? caretPos : index;
		// for (int index = caretPos; index > -1; index--) {
		// if (this.wordDelimeters.contains(this.textComponent
		// .getDocument().getText(index, 1))) {
		// return index + 1;
		// }
		// if (index == 0) {
		// return 0;
		// }
		// }
		// } catch (BadLocationException e) {
		// logger.log(Level.SEVERE, e.getMessage());
		// }
		// return -1;
	}

	private String getInsertText(Object o) {
		if (o instanceof OWLObject) {
			OWLModelManager owlModelManager = this.owlEditorKit
					.getModelManager();
			return owlModelManager.getOWLObjectRenderer().render((OWLObject) o,
					owlModelManager.getOWLEntityRenderer());
		} else if (o instanceof Variable) {
			return ((Variable) o).getName();
		} else {
			return o.toString();
		}
	}

	private String getWordToComplete() {
		try {
			int index = this.getWordIndex();
			int caretIndex = this.textComponent.getCaretPosition();
			String toReturn = this.textComponent.getDocument().getText(index,
					caretIndex - index);
			return toReturn;
		} catch (BadLocationException e) {
			return "";
		}
	}

	public void uninstall() {
		this.textComponent.removeKeyListener(this.keyListener);
	}
}