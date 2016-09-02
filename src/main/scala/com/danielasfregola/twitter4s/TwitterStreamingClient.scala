package com.danielasfregola.twitter4s

import akka.actor.{ActorRefFactory, ActorSystem, Props}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.TwitterStatusClient
import com.danielasfregola.twitter4s.listeners.TwitterStreamListener
import com.danielasfregola.twitter4s.util.TokensFromConfig

import scala.reflect.ClassTag

class TwitterStreamingClient[Listener <: TwitterStreamListener : ClassTag](val consumerToken: ConsumerToken, val accessToken: AccessToken)
                            (implicit val actorRefFactory: ActorRefFactory = ActorSystem("twitter4s")) extends StreamingClients {

  implicit val listenerRef = actorRefFactory.actorOf(Props[Listener])
}

trait StreamingClients extends TwitterStatusClient

object TwitterStreamingClient {

  def apply[Listener <: TwitterStreamListener : ClassTag]: TwitterStreamingClient[Listener] = {
    val consumerToken = ConsumerToken(key = TokensFromConfig.consumerTokenKey, secret = TokensFromConfig.consumerTokenSecret)
    val accessToken = AccessToken(key = TokensFromConfig.accessTokenKey, secret = TokensFromConfig.accessTokenSecret)
    new TwitterStreamingClient[Listener](consumerToken, accessToken)
  }
}
