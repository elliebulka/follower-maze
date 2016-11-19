package scala.eventsource

import akka.actor.{Actor, ActorRef, ActorSystem, Props}


class EventHandler extends Actor with EventConversion {

  def receive: Receive = {
    println("received test")
  }

  private def follow(message: String) = {

  }

}