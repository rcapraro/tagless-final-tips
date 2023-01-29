import cats.effect.Sync
import cats.syntax.functor._

import java.io.{BufferedReader, InputStreamReader}

trait Console[F[_]] {
  def println[A](a: A): F[Unit]

  def readLine(): F[String]
}

object Console {

  def apply[F[_]: Console]: Console[F] = implicitly

  def make[F[_]: Sync]: F[Console[F]] = Sync[F].pure((System.in, System.out)).map { case (in, out) =>
    new Console[F] {
      def println[V](v: V): F[Unit] =
        Sync[F].blocking(out.println(v))

      def readLine(): F[String] = {
        val bufferedReader = new BufferedReader(new InputStreamReader(in))
        Sync[F].interruptible(bufferedReader.readLine())
      }
    }
  }
}
