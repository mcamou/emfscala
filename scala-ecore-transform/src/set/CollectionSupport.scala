package set

import scala.reflect.Manifest

  
trait CollectionSupport {
  implicit def wrapIterableForOaw [A] (i: Iterable[A]) = CollectionSupport.wrapCollectionForOaw (i)
  implicit def wrapIteratorForOaw [A] (iter: Iterator[A]) = CollectionSupport.wrapIteratorForOaw (iter)
}

object CollectionSupport {
  private def manifestAsCls [X] (manifest: Manifest[X]) = asNonPrimitive (manifest.erasure.asInstanceOf [Class[X]])

  private def asNonPrimitive (cls: Class[_]) = cls match {
    case _ if ! cls.isPrimitive   => cls
    case java.lang.Boolean.TYPE   => classOf [java.lang.Boolean]
    case java.lang.Byte.TYPE      => classOf [java.lang.Byte]
    case java.lang.Short.TYPE     => classOf [java.lang.Short]
    case java.lang.Character.TYPE => classOf [java.lang.Character]
    case java.lang.Integer.TYPE   => classOf [java.lang.Integer]
    case java.lang.Long.TYPE      => classOf [java.lang.Long]
    case java.lang.Float.TYPE     => classOf [java.lang.Float]
    case java.lang.Double.TYPE    => classOf [java.lang.Double]
  }

  def wrapCollectionForOaw [A] (i: Iterable[A])    = new OawIterable (i)
  def wrapIteratorForOaw   [A] (iter: Iterator[A]) = new OawIterator   (iter)
}

  class OawIterator[T] (iter: Iterator[T]) {
    def typeSelect[X] (implicit manifest: Manifest[X]) = {val cls = manifest.erasure; iter.filter (cls.isInstance (_)).asInstanceOf [Iterator [X]]} //TODO 2.8 strongly typed return type according to the parameter type?
    def selectFirst (cond: T => Boolean): T = {
      val filtered = iter.filter (cond)
      if (filtered.hasNext)
        filtered.next
      else
        null.asInstanceOf[T]
    }
  }

  class OawIterable[A] (l: Iterable[A]) {
    def typeSelect[X] (implicit manifest: Manifest[X]) = {val cls = manifest.erasure; l.filter (cls.isInstance (_)).asInstanceOf [Iterable [X]]} //TODO 2.8 strongly typed return type according to the parameter type?
    def selectFirst (cond: A => Boolean): A = {
      val filtered = l.filter (cond)
      if (filtered.isEmpty)
        null.asInstanceOf[A]
      else
        filtered.iterator.next
    }
  }



