package set

import org.eclipse.emf.ecore.EObject
import collection.JavaConversions


trait EmfModelSupport extends JavaBeanModelSupport {
  implicit def wrapEObject (o: EObject) = new OawEObject (o)

  private object Impl {
    def eRootContainer (o: EObject): EObject = if (o.eContainer == null) o else eRootContainer (o.eContainer)
  }

  class OawEObject (o: EObject) {
    def eRootContainer = Impl.eRootContainer (o)
    def eAll = Iterator.single(o) ++ scala.collection.JavaConversions.asIterator (o.eAllContents)
  }
}

