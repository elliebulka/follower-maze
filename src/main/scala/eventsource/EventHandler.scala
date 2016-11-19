package scala.eventsource

import akka.actor.{Actor, ActorRef, ActorSystem, Props}


class EventHandler extends Actor with EventConversion {

  def receive: Receive = {
    case event: String =>
      handleEvent(event)
    println("received test")
  }

  private def handleEvent(event: String) = {

  }

  private def follow(message: String) = {

  }

}