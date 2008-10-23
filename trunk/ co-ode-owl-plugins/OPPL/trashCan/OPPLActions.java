/* Generated By:JJTree: Do not edit this line. OPPLActions.java */
package org.coode.oppl.syntax;

public @SuppressWarnings("all")
class OPPLActions extends SimpleNode {
	public OPPLActions(int id) {
		super(id);
	}

	public OPPLActions(OPPLParser p, int id) {
		super(p, id);
	}

	@Override
	public String toString() {
		String toReturn = "BEGIN\n";
		for (Node child : this.children) {
			toReturn += child.toString() + "\n";
		}
		return toReturn += "END;";
	}

	/** Accept the visitor. * */
	public Object jjtAccept(OPPLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
