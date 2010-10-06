
abstract class Anything

		case class Model(x: String)
		extends Anything

		case class Element(x: String)
		extends Anything

		case class EnumDecl(x: String)
		extends Element	
		case class Symbol(x: String)
		extends Element	
		case class Type(x: String)
		extends Anything

		case class PrimitiveType(x: String)
		extends Type	
		case class ArrayType(x: String)
		extends Type	
		case class EnumType(x: String)
		extends PrimitiveType	
		case class IntType(x: String)
		extends PrimitiveType	
		case class BoolType(x: String)
		extends PrimitiveType	
		case class FloatType(x: String)
		extends PrimitiveType	
		case class StringType(x: String)
		extends PrimitiveType	
		case class Formula(x: String)
		extends Element	
		case class Expr(x: String)
		extends Anything

		case class Expression(x: String)
		extends Expr	
		case class EnumLiteral(x: String)
		extends Symbol	
		case class VarDecl(x: String)
		extends Symbol	
		case class Plus(x: String)
		extends Expression	
		case class Multi(x: String)
		extends Expression	
		case class ArrayAccess(x: String)
		extends Expression	
		case class SymbolRef(x: String)
		extends Expression	
		case class NumberLiteral(x: String)
		extends Expression	
		case class StringLiteral(x: String)
		extends Expression	
