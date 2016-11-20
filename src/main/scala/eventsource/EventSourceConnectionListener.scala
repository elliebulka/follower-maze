package eventsource

import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by elliebulka on 11/20/16.
  */
object EventSourceConnectionListener {
  def props(eventHandler: ActorRef) = Props(classOf[EventSourceConnectionListener], eventHandler)
}

class EventSourceConnectionListener(eventHandler: ActorRef) extends Actor {

  def receive: Receive = {
    case event: String => println(event)
  }

}