package set

import collection.JavaConversions


trait JavaBeanModelSupport extends CollectionSupport {
  implicit def wrapJavaUtilCollectionForOaw [A <: AnyRef] (c: java.util.Collection[A])  = wrapIterableForOaw (JavaConversions.asIterable (c))
  implicit def wrapJavaUtilIteratorForOaw   [A <: AnyRef] (iter: java.util.Iterator[A]) = new OawIterator (JavaConversions.asIterator (iter))
}

