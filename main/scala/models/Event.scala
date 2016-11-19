package scala.models

object Event {

  case class FollowEvent {
    value: String
  }

  case class UnfollowEvent {
    value: String
  }
}