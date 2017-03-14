
/**
  * Created by malarconba001 on 3/3/2017.
  */


import java.util.UUID

import com.spingo.op_rabbit.PlayJsonSupport._
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit.{Directives, Message, RabbitControl, Subscription}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}


case class Request(message: String, returnQueue:Option[String]= None)

object Request {
  implicit val taskFormat = Json.format[Request]
}


object Rabbit {
  //boot up the RabbitMQ control actor
  val system = ActorSystem.create("engine-system")
  val control = system.actorOf(Props[RabbitControl])
}

object Engine extends App {
  val run = {
    //Set up a RabittMQ consumer
    val fastSubRef = Subscription.run(Rabbit.control) {
      import Directives._
      // A qos of n will cause up to n concurrent messages to be processed at any given time.
      channel(qos = 1) {
        consume(topic(queue("mab"), Nil)) {
          (body(as[Request]) & routingKey) { (task, key) => //RunFlexiCapture
            /* do work; this body is executed in a separate thread, as
             provided by the implicit execution context */
            ack(runRequest(task))
          }
        }
      }
    }
  }

  def runRequest(params: Request) = {//RunFlexiCapture
    println(params.message)
    if (params.returnQueue.isDefined){
      Rabbit.control ! Message.queue(
        params.message.toLowerCase ,
        queue = params.returnQueue.get)
    }
    Future(params.message.toLowerCase)
  }
}

object TestMessage {
  def withCallback(msg:String) = {
    val returnQueue = UUID.randomUUID().toString
    val subscriptionRef = Subscription.run(Rabbit.control) {
      import Directives._

      // A qos of 3 will cause up to 3 concurrent messages to be processed at any given time.
      channel(qos = 1) {
        consume(queue(returnQueue, exclusive = true, autoDelete = true)) {
          (body(as[String]) & routingKey) { (payload, key) =>
            /* do work; this body is executed in a separate thread, as
                 provided by the implicit execution context */
            Promise[Option[String]]() success Some(payload)
            ack(println("Got back: "+payload))
          }
        }
      }
    }
    val messageJson = Json.obj("message" -> msg, "returnQueue" -> returnQueue)
    val message = messageJson.as[Request]
    subscriptionRef.initialized.foreach { _ =>
      Rabbit.control ! Message.queue(
        message,
        queue = "mab")
      println("done: " + message)
    }
  }

  def blocking(msg:String) = {
    // Test Consumer
    val messageJson = Json.obj("message" -> msg)
    val message = messageJson.as[Request]
    Rabbit.control ! Message.queue(
      message ,
      queue = "mab")
    println("done: " +message)
  }

    // Test Consumer

}

