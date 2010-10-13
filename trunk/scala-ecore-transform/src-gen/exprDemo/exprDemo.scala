package exprDemo

case class Model (elements: List [Element])

class Element (val name: String)
object Element {
    def unapply (o: Element) = Some (o.name)
}

case class EnumDecl (literals: List [Symbol], override val name: String) extends Element (name)

class Symbol (override val name: String) extends Element (name)
object Symbol {
    def unapply (o: Symbol) = Some (o.name)
}

class Type

class PrimitiveType extends Type

case class ArrayType (baseType: Type) extends Type

case class EnumType extends PrimitiveType {
    var enumRef: EnumDecl = null
}

case class IntType extends PrimitiveType

case class BoolType extends PrimitiveType

case class FloatType extends PrimitiveType

case class StringType extends PrimitiveType

case class Formula (tpe: Type, expr: Expr, override val name: String) extends Element (name)

class Expr

class Expression extends Expr

case class EnumLiteral (override val name: String) extends Symbol (name)

case class VarDecl (tpe: Type, init: Expr, override val name: String) extends Symbol (name)

case class Plus (left: Expression, right: Expression) extends Expression

case class Multi (left: Expression, right: Expression) extends Expression

case class ArrayAccess (expr: Expression, index: Expr) extends Expression

case class SymbolRef extends Expression {
    var symbol: Symbol = null
}

case class NumberLiteral (value: Int) extends Expression

case class StringLiteral (value: String) extends Expression

