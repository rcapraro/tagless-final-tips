import cats.MonadError
import cats.data.EitherT
import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.option._

object ApplicativeErrorTest extends IOApp.Simple {

  private type F[A] = EitherT[IO, MathematicalError, A]

  private def parseNumber[F[_]: Console: MonadError[*[_], MathematicalError]]: F[Int] = for {
    _ <- Console[F].println("Enter a number:")
    maybeNumber <- Console[F].readLine()
    result <- maybeNumber.toIntOption.liftTo[F](ParseError(maybeNumber))
  } yield result

  override def run: IO[Unit] = {

    // produces an error of type MathematicalError
    Console.make[F].flatMap { c =>
      implicit val ci: Console[F] = c
      parseNumber
    }.value.unsafeRunSync() match {
      case Left(e) => IO.println(s"Error: $e")
      case Right(value) => IO.println(s"Success! You typed: $value")
    }
  }
}
