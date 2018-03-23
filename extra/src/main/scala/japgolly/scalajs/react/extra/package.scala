package japgolly.scalajs.react

package object extra {

  type ~=>[-A, +B] = Reusable[A => B]

  implicit final class ReactExtrasExt_Any[A](private val self: A) extends AnyVal {
    @inline def ~=~(a: A)(implicit r: Reusability[A]): Boolean = r.test(self, a)
    @inline def ~/~(a: A)(implicit r: Reusability[A]): Boolean = !r.test(self, a)
  }

}