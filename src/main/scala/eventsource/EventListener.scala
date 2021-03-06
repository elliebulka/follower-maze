package eventsource

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.Received

object EventListener {
  def props(eventHandler: ActorRef) = Props(classOf[EventListener], eventHandler)
}

/**
  * The event listener actor recieves incoming events from the event source and
  * forwards them to the event handler
  *
  * @param eventHandler event handler actor
  */
class EventListener(eventHandler: ActorRef) extends Actor {

  def receive: Receive = {

    case Received(eventData) => eventHandler ! eventData.utf8String.trim

  }

}

