/* Generated By:JJTree: Do not edit this line. MAEBigSum.java */
package uk.ac.manchester.mae;

public class MAEBigSum extends SimpleNode {
	public MAEBigSum(int id) {
		super(id);
	}

	public MAEBigSum(ArithmeticsParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(ArithmeticsParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		String toReturn = "";
		toReturn += "SUM(" + this.children[0].toString() + ")";
		return toReturn;
	}
}
