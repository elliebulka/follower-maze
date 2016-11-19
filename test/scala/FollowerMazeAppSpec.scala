package scala

import akka.actor.ActorDSL._
import akka.testkit.{ EventFilter, TestProbe }
import akka.util.Timeout

class FollowerMazeAppSpec extends BaseAkkaSpec {

  import FollowerMazeApp._

  "Creating FollowerMazeApp" should {
    "result in creating a top-level actor named 'event-handler'" in {
      new FollowerMazeApp(system)
      TestProbe().expectActor("/user/event-handler")
    }
  }


}