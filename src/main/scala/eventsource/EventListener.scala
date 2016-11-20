package eventsource

import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by elliebulka on 11/20/16.
  */
object EventListener {
  def props(eventHandler: ActorRef) = Props(classOf[EventListener], eventHandler)
}

class EventListener(eventHandler: ActorRef) extends Actor {

  def receive: Receive = {
    case event: String => println(event)
  }

}

