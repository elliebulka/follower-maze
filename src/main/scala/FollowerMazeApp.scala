package src.main.scala

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.eventsource.EventHandler

object FollowerMazeApp extends App {

  val system = ActorSystem("follower-maze-system")
  val followerMazeApp = new FollowerMazeApp(system)

}

class FollowerMazeApp(system: ActorSystem) {

  import akka.actor.{Actor, ActorRef, ActorSystem, Props}

  private val eventHandler = createEventHandler()

  protected def createEventHandler(): ActorRef ={
    system.actorOf(Props[EventHandler], "event-handler")
  }

}