/* Generated By:JJTree: Do not edit this line. MAEPropertyFacet.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=MAE,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.manchester.mae;

public
class MAEPropertyFacet extends SimpleNode {
  public MAEPropertyFacet(int id) {
    super(id);
  }

  public MAEPropertyFacet(ArithmeticsParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ArithmeticsParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=4a26fa906d0c6bed2b039baff55d15a0 (do not edit this line) */
