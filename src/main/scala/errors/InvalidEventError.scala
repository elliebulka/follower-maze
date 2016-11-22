package errors


object InvalidEventError {
  case class InvalidEventError(message: String) extends Throwable
}
