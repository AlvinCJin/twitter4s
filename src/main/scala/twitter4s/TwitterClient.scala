package twitter4s

import akka.actor.{ActorRefFactory, ActorSystem}

import twitter4s.entities.AccessToken._
import twitter4s.entities.ConsumerToken._
import twitter4s.entities.{AccessToken, ConsumerToken}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.directmessages.TwitterDirectMessageClient
import twitter4s.http.clients.search.TwitterSearchClient
import twitter4s.http.clients.statuses.TwitterStatusClient

class TwitterClient(val consumerToken: ConsumerToken = ConsumerTokenFromConf, val accessToken: AccessToken = AccessTokenFromConf)
                   (implicit val actorRefFactory: ActorRefFactory = ActorSystem("twitter4s")) extends OAuthClient with Clients

trait Clients extends TwitterStatusClient with TwitterDirectMessageClient with TwitterSearchClient