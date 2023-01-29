import scala.util.control.NoStackTrace

sealed trait MathematicalError extends NoStackTrace

case object ArithmeticError extends MathematicalError

case class ParseError(i: String) extends MathematicalError {
  override def toString: String = s"$i is not a number"
}
