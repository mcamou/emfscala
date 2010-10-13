package set

import collection.JavaConversions._
import org.eclipse.emf.ecore._


object ClassGenerator {
	def apply (cls: EClass) = cls match {
		case _ if cls.isAbstract    => new AbstractClassGenerator (cls)
		case _ if hasSubClass (cls) => new NonCaseClassGenerator (cls)
		case _                      => new CaseClassGenerator (cls)
	}

	private def hasSubClass (cls: EClass) = ScalaGeneratorSupport.allClasses (cls).exists (_.getESuperTypes contains cls)
}

object ScalaGeneratorSupport extends ScalaGeneratorSupport {
	val cls = null
}

trait ScalaGeneratorSupport extends EmfModelSupport with CollectionSupport {
	val cls: EClass
	
	def allClasses (modelElement: EClass = cls): Iterator[EClass] = modelElement.eRootContainer.eAllContents.typeSelect[EClass]
	def hasSubClass (c: EClass = cls) = allClasses (c).exists (_.getESuperTypes contains c)
	def superClass (c: EClass = cls) = c.getESuperTypes.get(0)
	
	def definedFeatures (c: EClass = cls) = c.getEStructuralFeatures.map (_ match {
		case a: EAttribute                => ScalaFeature (scalaName (a), a.getEAttributeType, true,  true,            false, false)
		case r: EReference if ! r.isMany  => ScalaFeature (scalaName (r), r.getEReferenceType, false, r.isContainment, false, false)
		case r: EReference if r.isOrdered => ScalaFeature (scalaName (r), r.getEReferenceType, false, r.isContainment, true,  true)
		case r: EReference                => ScalaFeature (scalaName (r), r.getEReferenceType, false, r.isContainment, true,  false)
	})

	def inheritedFeatures (c: EClass = cls): Iterable[ScalaFeature] = c.getESuperTypes.flatMap (features)
	def features          (c: EClass = cls) = definedFeatures (c) ++ inheritedFeatures (c)
	def ctorFeatures      (c: EClass = cls) = features (c).filter (_.isContainment)
	
	def scalaPrimitiveTypeName (t: EClassifier) = t.getName match {
	    case "EBoolean" => "Boolean"
	    case "EInt"     => "Int"
        case "EString"  => "String"
        case "String"   => "String"
        case "int"      => "Int"
        case "Integer"  => "Int"
	} //TODO Mapping der primitiven Typen nach Scala vervollstaendigen
	
	def scalaName (f: EStructuralFeature) = f.getName match {
	    case "class"   => "cls"
	    case "default" => "deflt"
	    case "type"    => "tpe"
	    case "val"     => "value"
	    case "var"     => "variable"
	    case n         => n
	}
}

case class ScalaFeature (name: String, tpe: EClassifier, isAttribute: Boolean, isContainment: Boolean, isToMany: Boolean, isOrdered: Boolean) {
	val scalaTypeName = tpe match {
        case _: EClass if isToMany && isOrdered => "List [" + tpe.getName + "]"
        case _: EClass if isToMany              => "Set  [" + tpe.getName + "]"
        case _: EClass                          => tpe.getName
        case _                                  => ScalaGeneratorSupport.scalaPrimitiveTypeName (tpe)
	}
}

trait ClassGenerator extends ScalaGeneratorSupport {
    val classQualifier: String
    def formalParamPrefix (f: ScalaFeature): String
    def classCode = classQualifier + "class " + cls.getName + paramClause + extendsClause + body + "\n" + extractor + "\n"
	
	protected def extractor = if (ctorFeatures(cls).isEmpty) "" else "object " + cls.getName + " {\n" +
	   "    def unapply (o: " + cls.getName + ") = Some (" + ctorFeatures (cls).map(f => "o." + f.name).mkString (", ") + ")\n" +
	   "}\n"

    private def mkParamList (params: Iterable[String]) = if (params.isEmpty) "" else params.mkString (" (", ", ", ")")
	
	private def formalParamList (params: Iterable[ScalaFeature]) = mkParamList (params.map (f => formalParamPrefix (f) + f.name + ": " + f.scalaTypeName))
	private def paramNameList   (params: Iterable[ScalaFeature]) = mkParamList (params.map (_.name))
	private def paramTypeList   (params: Iterable[ScalaFeature]) = mkParamList (params.map (_.scalaTypeName))
	
	private def paramClause = formalParamList (ctorFeatures (cls))
	
    private def extendsClause = if (cls.getESuperTypes.isEmpty) "" else {
    	" extends " + superClass (cls).getName + paramNameList (inheritedFeatures (cls).filter (_.isContainment))
    }
	
	private val nonContainmentFeatures = definedFeatures (cls).filter (! _.isContainment)
	private def body = if (nonContainmentFeatures.isEmpty) "" else 
	    nonContainmentFeatures.map (f => "    var " + f.name + ": " + f.scalaTypeName + " = " + varInitExpr (f)).mkString (" {\n", "\n", "\n}")
	private def varInitExpr (f: ScalaFeature) = f match {
	    case _ if f.isToMany && f.isOrdered => "List ()"
	    case _ if f.isToMany                => "Set ()"
	    case _                              => "null"
	}
}

//TODO Extraktoren für non-containment features?

class AbstractClassGenerator (val cls: EClass) extends ClassGenerator {
    override val classQualifier = "abstract "
    override def formalParamPrefix (f: ScalaFeature)= if (inheritedFeatures (cls) contains f) "override val " else "val "
}

class NonCaseClassGenerator (val cls: EClass) extends ClassGenerator {
    override val classQualifier = ""
    override def formalParamPrefix (f: ScalaFeature)= if (inheritedFeatures (cls) contains f) "override val " else "val "
}

class CaseClassGenerator (val cls: EClass) extends ClassGenerator {
    override val classQualifier = "case "
    override def formalParamPrefix (f: ScalaFeature)= if (inheritedFeatures (cls) contains f) "override val " else ""
    override def extractor = ""
}


