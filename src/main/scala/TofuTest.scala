import cats.data.EitherT
import cats.effect._
import cats.effect.unsafe.implicits.global
import tofu._
import tofu.syntax.monadic._
import tofu.syntax.raise._

object TofuTest extends IOApp.Simple {

  private type F[A] = EitherT[IO, MathematicalError, A]

  private def divide[F[_]: Sync](x: String, y: String)(implicit R: Raise[F, MathematicalError]): F[String] =
    (x.toIntOption.orRaise(ParseError(x)), y.toIntOption.orRaise(ParseError(y)).verified(_ != 0)(ArithmeticError)).mapN(_ / _).map(_.toString)

  override def run: IO[Unit] = {
    divide[F]("10", "0").value
      .map {
        case Left(e)  => IO.println(s"Error $e")
        case Right(value) => IO.println(s"Success $value")
      }
      .unsafeRunSync()
  }
}
