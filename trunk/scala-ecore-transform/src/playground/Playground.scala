package playground

import exprDemo._

object Playground extends AnyRef {
	
    def main (args: Array[String]) {
    	val m = Model( List( 
    					VarDecl( IntType(), NumberLiteral(10), "v1" ), 
    					VarDecl( StringType(), StringLiteral("Hallo"), "v2" )
    				)
    			) 
    			
    	m.elements.collect (
	    	_ match {
	    		case VarDecl(IntType(),NumberLiteral(x@_),_) => if ( x > 0 ) "ok" else "error"
	    		case _ => "fail"
	    	}
    	).foreach( System.err.println(_));
    }
    
}
