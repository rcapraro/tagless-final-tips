import cats.data.EitherT
import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.option._

import java.io.{BufferedReader, InputStreamReader}

object SyncTest extends IOApp.Simple {

  private type F[A] = EitherT[IO, MathematicalError, A]

  private def parseNumber[F[_]: Sync]: F[Int] = {
    val bufferedReader = new BufferedReader(new InputStreamReader(System.in))
    for {
      _           <- Sync[F].blocking(println("Enter a number:"))
      maybeNumber <- Sync[F].interruptible(bufferedReader.readLine())
      result      <- maybeNumber.toIntOption.liftTo[F](ParseError(maybeNumber))
    } yield result
  }

  override def run: IO[Unit] = {

    // produces an exception
    parseNumber[F].value.unsafeRunSync() match {
      case Left(e)      => IO.println(s"Error: $e")
      case Right(value) => IO.println(s"Success! You typed: $value")
    }
  }
}
