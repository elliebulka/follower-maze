package src.main.scala

import akka.actor.{ActorRef, ActorSystem, Props}
import eventsource.{EventSourceConnectionListener}
import userclient.UserClientConnectionListener

import scala.eventsource.EventHandler

object FollowerMazeApp extends App {

  val system = ActorSystem("follower-maze-system")
  val followerMazeApp = new FollowerMazeApp(system)

}

class FollowerMazeApp(system: ActorSystem) {

  private val eventHandler = createEventHandler()
  private val eventSourceConnectionListener = createEventSourceConnectionListner()
  private val userClientConnectionListener = createUserClientConnectionListener()

  protected def createEventHandler(): ActorRef = {
    system.actorOf(Props[EventHandler], "event-handler")
  }

  protected def createEventSourceConnectionListner(): ActorRef = {
    system.actorOf(EventSourceConnectionListener.props(eventHandler), "event-source-connection-listener")
  }

  protected def createUserClientConnectionListener(): ActorRef = {
    system.actorOf(UserClientConnectionListener.props(eventHandler), "user-client-connection-listener")
  }

}