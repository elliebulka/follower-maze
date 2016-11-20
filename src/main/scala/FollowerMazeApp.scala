package src.main.scala

import akka.actor.{ActorRef, ActorSystem, Props}
import eventsource.{EventListener, EventSourceConnectionListener}

import scala.eventsource.EventHandler

object FollowerMazeApp extends App {

  val system = ActorSystem("follower-maze-system")
  val followerMazeApp = new FollowerMazeApp(system)

}

class FollowerMazeApp(system: ActorSystem) {

  private val eventHandler = createEventHandler()
  private val eventSourceConnectionListener = createEventSourceConnectionListner()

  protected def createEventHandler(): ActorRef = {
    system.actorOf(Props[EventHandler], "event-handler")
  }

  protected def createEventSourceConnectionListner(): ActorRef = {
    system.actorOf(EventSourceConnectionListener.props(eventHandler), "event-source-connection-listener")
  }

  //create event listener and source listener actors

}