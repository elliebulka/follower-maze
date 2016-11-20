package userclient

import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by elliebulka on 11/20/16.
  */
object UserClientConnectionListener {
  def props(eventHandler: ActorRef) = Props(classOf[UserClientConnectionListener], eventHandler)
}

class UserClientConnectionListener(eventHandler: ActorRef) extends Actor {

  def receive: Receive = {
    case event: String => println(event)
  }

}
