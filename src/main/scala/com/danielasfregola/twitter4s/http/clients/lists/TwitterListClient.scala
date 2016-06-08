package com.danielasfregola.twitter4s.http.clients.lists

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.entities.enums.Mode
import com.danielasfregola.twitter4s.entities.enums.Mode.Mode
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.lists.parameters._
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `lists` resource.
  */
trait TwitterListClient extends OAuthClient with Configurations {

  private val listsUrl = s"$apiTwitterUrl/$twitterVersion/lists"

  /** Returns all lists the specified user subscribes to, including their own.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/list</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param reverse : By default it is `false`.
    *                Set this to true if you would like owned lists to be returned first.
    * @return : The sequence of all lists the specified user subscribes to.
    */
  def getListsForUser(screen_name: String, reverse: Boolean = false): Future[Seq[TwitterList]] = {
    val parameters = ListsParameters(user_id = None, Some(screen_name), reverse)
    genericGetLists(parameters)
  }

  /** Returns all lists the specified user subscribes to, including their own.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/list" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/list</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param reverse : By default it is `false`.
    *                Set this to true if you would like owned lists to be returned first.
    * @return : The sequence of all lists the specified user subscribes to.
    */
  def getListsForUserId(user_id: Long, reverse: Boolean = false): Future[Seq[TwitterList]] = {
    val parameters = ListsParameters(Some(user_id), screen_name = None, reverse)
    genericGetLists(parameters)
  }

  private def genericGetLists(parameters: ListsParameters): Future[Seq[TwitterList]] =
    Get(s"$listsUrl/list.json", parameters).respondAs[Seq[TwitterList]]

  /** Returns a timeline of tweets authored by members of the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/statuses" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/statuses</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param count : By default it is `20`.
    *              Specifies the number of results to retrieve per "page".
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param include_rts : By default it is `false`.
    *                    When set to `true`, the list timeline will contain native retweets (if they exist) in addition to the standard stream of tweets.
    * @return : The sequence of tweets for the specified list.
    */
  def getListTimelineBySlugAndOwnerId(slug: String,
                                      owner_id: Long,
                                      count: Int = 20,
                                      since_id: Option[Long] = None,
                                      max_id: Option[Long] = None,
                                      include_entities: Boolean = true,
                                      include_rts: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = ListTimelineParameters(
      slug = Some(slug), owner_id = Some(owner_id), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericGetListTimeline(parameters)
  }

  /** Returns a timeline of tweets authored by members of the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/statuses" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/statuses</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : TThe screen name of the user who owns the list being requested by a `slug`.
    * @param count : By default it is `20`.
    *              Specifies the number of results to retrieve per "page".
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param include_rts : By default it is `false`.
    *                    When set to `true`, the list timeline will contain native retweets (if they exist) in addition to the standard stream of tweets.
    * @return : The sequence of tweets for the specified list.
    */
  def getListTimelineBySlugAndOwnerName(slug: String,
                                        owner_screen_name: String,
                                        count: Int = 20,
                                        since_id: Option[Long] = None,
                                        max_id: Option[Long] = None,
                                        include_entities: Boolean = true,
                                        include_rts: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = ListTimelineParameters(
      slug = Some(slug), owner_screen_name = Some(owner_screen_name), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericGetListTimeline(parameters)
  }

  /** Returns a timeline of tweets authored by members of the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/statuses" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/statuses</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param count : By default it is `20`.
    *              Specifies the number of results to retrieve per "page".
    * @param since_id : Optional, by default it is `None`.
    *                 Returns results with an ID greater than (that is, more recent than) the specified ID.
    *                 There are limits to the number of Tweets which can be accessed through the API.
    *                 If the limit of Tweets has occured since the `since_id`, the `since_id` will be forced to the oldest ID available.
    * @param max_id : Optional, by default it is `None`.
    *               Returns results with an ID less than (that is, older than) or equal to the specified ID.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param include_rts : By default it is `false`.
    *                    When set to `true`, the list timeline will contain native retweets (if they exist) in addition to the standard stream of tweets.
    * @return : The sequence of tweets for the specified list.
    */
  def getListTimelineByListId(list_id: Long,
                              count: Int = 20,
                              since_id: Option[Long] = None,
                              max_id: Option[Long] = None,
                              include_entities: Boolean = true,
                              include_rts: Boolean = false): Future[Seq[Tweet]] = {
    val parameters = ListTimelineParameters(
      list_id = Some(list_id), count = count, since_id = since_id,
      max_id = max_id, include_entities = include_entities, include_rts = include_rts)
    genericGetListTimeline(parameters)
  }

  private def genericGetListTimeline(parameters: ListTimelineParameters): Future[Seq[Tweet]] =
    Get(s"$listsUrl/statuses.json", parameters).respondAs[Seq[Tweet]]

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param member_screen_name : The screen name of the user for whom to remove from the list.
    *                           Helpful for disambiguating when a valid screen name is also a user ID.
    */
  def removeListMemberByListId(list_id: Long, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(list_id = Some(list_id), screen_name = Some(member_screen_name))
    genericRemoveListMember(parameters)
  }

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param member_screen_name : The screen name of the user for whom to remove from the list.
    *                           Helpful for disambiguating when a valid screen name is also a user ID.
    */
  def removeListMemberBySlugAndOwnerName(slug: String, owner_screen_name: String, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_screen_name = Some(owner_screen_name),
                                            screen_name = Some(member_screen_name))
    genericRemoveListMember(parameters)
  }

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param member_screen_name : The screen name of the user for whom to remove from the list.
    *                           Helpful for disambiguating when a valid screen name is also a user ID.
    */
  def removeListMemberBySlugAndOwnerId(slug: String, owner_id: Long, member_screen_name: String): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_id = Some(owner_id),
                                            screen_name = Some(member_screen_name))
    genericRemoveListMember(parameters)
  }

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param member_id : The ID of the user to remove from the list.
    *                  Helpful for disambiguating when a valid user ID is also a valid screen name.
    */
  def removeListMemberIdByListId(list_id: Long, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(list_id = Some(list_id), user_id = Some(member_id))
    genericRemoveListMember(parameters)
  }

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param member_id : The ID of the user to remove from the list.
    *                  Helpful for disambiguating when a valid user ID is also a valid screen name.
    */
  def removeListMemberIdBySlugAndOwnerName(slug: String, owner_screen_name: String, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_screen_name = Some(owner_screen_name),
                                            user_id = Some(member_id))
    genericRemoveListMember(parameters)
  }

  /** Removes the specified member from the list. The authenticated user must be the list’s owner to remove members from the list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param member_id : The ID of the user to remove from the list.
    *                  Helpful for disambiguating when a valid user ID is also a valid screen name.
    */
  def removeListMemberIdBySlugAndOwnerId(slug: String, owner_id: Long, member_id: Long): Future[Unit] = {
    val parameters = RemoveMemberParameters(slug = Some(slug),
                                            owner_id = Some(owner_id),
                                            user_id = Some(member_id))
    genericRemoveListMember(parameters)
  }

  private def genericRemoveListMember(parameters: RemoveMemberParameters): Future[Unit] =
    Post(s"$listsUrl/members/destroy.json", parameters).respondAs[Unit]

  /** Returns the twitter lists the specified user has been added to.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/memberships" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/memberships</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @param filter_to_owned_lists : By default it is `false`.
    *                    When set to `true`, will return just lists the authenticating user owns, and the user represented by user_id or screen_name is a member of.
    * @return : The twitter lists the specified user has been added to.
    */
  def getListMembershipsForUser(screen_name: String,
                                count: Int = 20,
                                cursor: Long = -1,
                                filter_to_owned_lists: Boolean = false): Future[TwitterLists] = {
    val parameters = MembershipsParameters(user_id = None, Some(screen_name), count, cursor, filter_to_owned_lists)
    genericGetListMemberships(parameters)
  }

  /** Returns the twitter lists the specified user has been added to.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/memberships" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/memberships</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @param filter_to_owned_lists : By default it is `false`.
    *                    When set to `true`, will return just lists the authenticating user owns, and the user represented by user_id or screen_name is a member of.
    * @return : The twitter lists the specified user has been added to.
    */
  def getListMembershipsForUserId(user_id: Long,
                                  count: Int = 20,
                                  cursor: Long = -1,
                                  filter_to_owned_lists: Boolean = false): Future[TwitterLists] = {
    val parameters = MembershipsParameters(Some(user_id), screen_name = None, count, cursor, filter_to_owned_lists)
    genericGetListMemberships(parameters)
  }

  private def genericGetListMemberships(parameters: MembershipsParameters): Future[TwitterLists] =
    Get(s"$listsUrl/memberships.json", parameters).respondAs[TwitterLists]

  /** Adds multiple members to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists can’t have more than 5,000 members, and you are limited to adding up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships.
    * Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create_all</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param user_ids : The list of user IDs to add, up to 100 are allowed in a single request.
    */
  def addListMemberIdsByListId(list_id: Long, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(list_id = Some(list_id), user_id = Some(user_ids.mkString(",")))
    genericAddListMembers(parameters)
  }

  /** Adds multiple members to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists can’t have more than 5,000 members, and you are limited to adding up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships.
    * Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create_all</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param user_ids : The list of user IDs to add, up to 100 are allowed in a single request.
    */
  def addListMemberIdsBySlugAndOwnerName(slug: String, owner_screen_name: String, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), user_id = Some(user_ids.mkString(",")))
    genericAddListMembers(parameters)
  }

  /** Adds multiple members to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists can’t have more than 5,000 members, and you are limited to adding up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships.
    * Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create_all</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param user_ids : The list of user IDs to add, up to 100 are allowed in a single request.
    */
  def addListMemberIdsBySlugAndOwnerId(slug: String, owner_id: Long, user_ids: Seq[Long]): Future[Unit] = {
    require(!user_ids.isEmpty, "please, provide at least one user id")
    val parameters = MembersParameters(slug = Some(slug), owner_id = Some(owner_id), user_id = Some(user_ids.mkString(",")))
    genericAddListMembers(parameters)
  }

  /** Adds multiple members to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists can’t have more than 5,000 members, and you are limited to adding up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships.
    * Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create_all</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param screen_names : The list of user screen names to add, up to 100 are allowed in a single request.
    */
  def addListMembersByListId(list_id: Long, screen_names: Seq[String]): Future[Unit] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = MembersParameters(list_id = Some(list_id), screen_name = Some(screen_names.mkString(",")))
    genericAddListMembers(parameters)
  }

  /** Adds multiple members to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists can’t have more than 5,000 members, and you are limited to adding up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships.
    * Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create_all</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param screen_names : The list of user screen names to add, up to 100 are allowed in a single request.
    */
  def addListMembersBySlugAndOwnerName(slug: String, owner_screen_name: String, screen_names: Seq[String]): Future[Unit] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = MembersParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), screen_name = Some(screen_names.mkString(",")))
    genericAddListMembers(parameters)
  }

  /** Adds multiple members to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists can’t have more than 5,000 members, and you are limited to adding up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships.
    * Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create_all</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param screen_names : The list of user screen names to add, up to 100 are allowed in a single request.
    */
  def addListMembersBySlugAndOwnerId(slug: String, owner_id: Long, screen_names: Seq[String]): Future[Unit] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = MembersParameters(slug = Some(slug), owner_id = Some(owner_id), screen_name = Some(screen_names.mkString(",")))
    genericAddListMembers(parameters)
  }

  private def genericAddListMembers(parameters: MembersParameters): Future[Unit] =
    Post(s"$listsUrl/members/create_all.json", parameters).respondAs[Unit]

  /** Check if the specified user is a member of the specified list.
    * If the user is a member of the specified list, his user representation is returned.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/members/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/members/show</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true`, statuses will not be included in the returned user objects.
    * @return : The user representation if the specified user is a member of the specified list, it throws an `TwitterException` instead.
    */
  def checkListMemberByUserIdAndListId(list_id: Long,
                                       user_id: Long,
                                       include_entities: Boolean = true,
                                       skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(list_id = Some(list_id),
                                      user_id = Some(user_id),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericCheckListMember(parameters)
  }

  /** Check if the specified user is a member of the specified list.
    * If the user is a member of the specified list, his user representation is returned.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/members/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/members/show</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true`, statuses will not be included in the returned user objects.
    * @return : The user representation if the specified user is a member of the specified list, it throws an `TwitterException` instead.
    */
  def checkListMemberByUserIdSlugAndOwnerName(slug: String,
                                              owner_screen_name: String,
                                              user_id: Long,
                                              include_entities: Boolean = true,
                                              skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_screen_name = Some(owner_screen_name),
                                      user_id = Some(user_id),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericCheckListMember(parameters)
  }

  /** Check if the specified user is a member of the specified list.
    * If the user is a member of the specified list, his user representation is returned.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/members/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/members/show</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true`, statuses will not be included in the returned user objects.
    * @return : The user representation if the specified user is a member of the specified list, it throws an `TwitterException` instead.
    */
  def checkListMemberByUserIdSlugAndOwnerId(slug: String,
                                            owner_id: Long,
                                            user_id: Long,
                                            include_entities: Boolean = true,
                                            skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_id = Some(owner_id),
                                      user_id = Some(user_id),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericCheckListMember(parameters)
  }

  /** Check if the specified user is a member of the specified list.
    * If the user is a member of the specified list, his user representation is returned.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/members/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/members/show</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true`, statuses will not be included in the returned user objects.
    * @return : The user representation if the specified user is a member of the specified list, it throws an `TwitterException` instead.
    */
  def checkListMemberByUserAndListId(list_id: Long,
                                     screen_name: String,
                                     include_entities: Boolean = true,
                                     skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(list_id = Some(list_id),
                                      screen_name = Some(screen_name),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericCheckListMember(parameters)
  }

  /** Check if the specified user is a member of the specified list.
    * If the user is a member of the specified list, his user representation is returned.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/members/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/members/show</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true`, statuses will not be included in the returned user objects.
    * @return : The user representation if the specified user is a member of the specified list, it throws an `TwitterException` instead.
    */
  def checkListMemberByUserSlugAndOwnerName(slug: String,
                                            owner_screen_name: String,
                                            screen_name: String,
                                            include_entities: Boolean = true,
                                            skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_screen_name = Some(owner_screen_name),
                                      screen_name = Some(screen_name),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericCheckListMember(parameters)
  }

  /** Check if the specified user is a member of the specified list.
    * If the user is a member of the specified list, his user representation is returned.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/members/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/members/show</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true`, statuses will not be included in the returned user objects.
    * @return : The user representation if the specified user is a member of the specified list, it throws an `TwitterException` instead.
    */
  def checkListMemberByUserSlugAndOwnerId(slug: String,
                                          owner_id: Long,
                                          screen_name: String,
                                          include_entities: Boolean = true,
                                          skip_status: Boolean = false): Future[User] = {
    val parameters = MemberParameters(slug = Some(slug),
                                      owner_id = Some(owner_id),
                                      screen_name = Some(screen_name),
                                      include_entities = include_entities,
                                      skip_status = skip_status)
    genericCheckListMember(parameters)
  }

  private def genericCheckListMember(parameters: MemberParameters): Future[User] =
    Get(s"$listsUrl/members/show.json", parameters).respondAs[User]

  /** Returns the members of the specified list. Private list members will only be shown if the authenticated user owns the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/members" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/members</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true`, statuses will not be included in the returned user objects.
    * @return : The users representation of the members of the list.
    */
  def getListMembersByListId(list_id: Long,
                             count: Int = 20,
                             cursor: Long = -1,
                             include_entities: Boolean = true,
                             skip_status: Boolean = false): Future[Users] = {
    val parameters = ListMembersParameters(list_id = Some(list_id),
                                             count = count,
                                             cursor = cursor,
                                             include_entities = include_entities,
                                             skip_status = skip_status)
    genericGetListMembers(parameters)
  }

  /** Returns the members of the specified list. Private list members will only be shown if the authenticated user owns the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/members" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/members</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true`, statuses will not be included in the returned user objects.
    * @return : The users representation of the members of the list.
    */
  def getListMembersBySlugAndOwnerName(slug: String,
                                       owner_screen_name: String,
                                       count: Int = 20,
                                       cursor: Long = -1,
                                       include_entities: Boolean = true,
                                       skip_status: Boolean = false): Future[Users] = {
    val parameters = ListMembersParameters(slug = Some(slug),
                                             owner_screen_name = Some(owner_screen_name),
                                             count = count,
                                             cursor = cursor,
                                             include_entities = include_entities,
                                             skip_status = skip_status)
    genericGetListMembers(parameters)
  }

  /** Returns the members of the specified list. Private list members will only be shown if the authenticated user owns the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/members" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/members</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @param include_entities : By default it is `true`.
    *                         his node offers a variety of metadata about the tweet in a discreet structure, including: user_mentions, urls, and hashtags.
    *                         You can omit parameters from the result by setting `include_entities` to `false`.
    * @param skip_status : By default it is `false`.
    *                    When set to either `true`, statuses will not be included in the returned user objects.
    * @return : The users representation of the members of the list.
    */
  def getListMembersBySlugAndOwnerId(slug: String,
                                     owner_id: Long,
                                     count: Int = 20,
                                     cursor: Long = -1,
                                     include_entities: Boolean = true,
                                     skip_status: Boolean = false): Future[Users] = {
    val parameters = ListMembersParameters(slug = Some(slug),
                                             owner_id = Some(owner_id),
                                             count = count,
                                             cursor = cursor,
                                             include_entities = include_entities,
                                             skip_status = skip_status)
    genericGetListMembers(parameters)
  }

  private def genericGetListMembers(parameters: ListMembersParameters): Future[Users] =
    Get(s"$listsUrl/members.json", parameters).respondAs[Users]

  /** Add a member to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists cannot have more than 5,000 members.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    */
  def addListMemberIdByListId(list_id: Long, user_id: Long): Future[Unit] = {
    val parameters = AddMemberParameters(list_id = Some(list_id), user_id = Some(user_id))
    genericAddListMember(parameters)
  }

  /** Add a member to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists cannot have more than 5,000 members.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    */
  def addListMemberIdBySlugAndOwnerName(slug: String, owner_screen_name: String, user_id: Long): Future[Unit] = {
    val parameters = AddMemberParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), user_id = Some(user_id))
    genericAddListMember(parameters)
  }


  /** Add a member to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists cannot have more than 5,000 members.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    */
  def addListMemberIdBySlugAndOwnerId(slug: String, owner_id: Long, user_id: Long): Future[Unit] = {
    val parameters = AddMemberParameters(slug = Some(slug), owner_id = Some(owner_id), user_id = Some(user_id))
    genericAddListMember(parameters)
  }

  /** Add a member to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists cannot have more than 5,000 members.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    */
  def addListMemberByListId(list_id: Long, screen_name: String): Future[Unit] = {
    val parameters = AddMemberParameters(list_id = Some(list_id), screen_name = Some(screen_name))
    genericAddListMember(parameters)
  }

  /** Add a member to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists cannot have more than 5,000 members.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    */
  def addListMemberBySlugAndOwnerName(slug: String, owner_screen_name: String, screen_name: String): Future[Unit] = {
    val parameters = AddMemberParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name), screen_name = Some(screen_name))
    genericAddListMember(parameters)
  }

  /** Add a member to a list. The authenticated user must own the list to be able to add members to it.
    * Note that lists cannot have more than 5,000 members.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/create</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    */
  def addListMemberBySlugAndOwnerId(slug: String, owner_id: Long, screen_name: String): Future[Unit] = {
    val parameters = AddMemberParameters(slug = Some(slug), owner_id = Some(owner_id), screen_name = Some(screen_name))
    genericAddListMember(parameters)
  }

  private def genericAddListMember(parameters: AddMemberParameters): Future[Unit] =
    Post(s"$listsUrl/members/create.json", parameters).respondAs[Unit]

  /** Deletes the specified list. The authenticated user must own the list to be able to destroy it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/destroy</a>.
    *
    * @param list_id : The numerical id of the list.
    * @return : The representation of the deleted twitter list
    */
  def deleteListById(list_id: Long): Future[TwitterList] = {
    val parameters = ListParameters(list_id = Some(list_id))
    genericDeleteList(parameters)
  }

  /** Deletes the specified list. The authenticated user must own the list to be able to destroy it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/destroy</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @return : The representation of the deleted twitter list
    */
  def deleteListBySlugAndOwnerName(slug: String, owner_screen_name: String): Future[TwitterList] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    genericDeleteList(parameters)
  }

  /** Deletes the specified list. The authenticated user must own the list to be able to destroy it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/destroy" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/destroy</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @return : The representation of the deleted twitter list
    */
  def deleteListBySlugAndOwnerId(slug: String, owner_id: Long): Future[TwitterList] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    genericDeleteList(parameters)
  }

  private def genericDeleteList(parameters: ListParameters): Future[TwitterList] =
    Post(s"$listsUrl/destroy.json", parameters).respondAs[TwitterList]

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param mode : Whether your list is public or private.
    */
  def updateListMode(list_id: Long, mode: Mode): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    val update = TwitterListUpdate(mode = Some(mode))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param mode : Whether your list is public or private.
    */
  def updateListModeBySlugAndOwnerName(slug: String, owner_screen_name: String, mode: Mode): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    val update = TwitterListUpdate(mode = Some(mode))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param mode : Whether your list is public or private.
    */
  def updateListModeBySlugAndOwnerId(slug: String, owner_id: Long, mode: Mode): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    val update = TwitterListUpdate(mode = Some(mode))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param name : The name for the list.
    */
  def updateListName(list_id: Long, name: String): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    val update = TwitterListUpdate(name = Some(name))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param name : The name for the list.
    */
  def updateListNameBySlugAndOwnerName(slug: String, owner_screen_name: String, name: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    val update = TwitterListUpdate(name = Some(name))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id: The user ID of the user who owns the list being requested by a `slug`.
    * @param name : The name for the list.
    */
  def updateListNameBySlugAndOwnerId(slug: String, owner_id: Long, name: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    val update = TwitterListUpdate(name = Some(name))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param description : The description to give the list.
    */
  def updateListDescription(list_id: Long, description: String): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    val update = TwitterListUpdate(description = Some(description))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param description : The description to give the list.
    */
  def updateListDescriptionBySlugAndOwnerName(slug: String, owner_screen_name: String, description: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    val update = TwitterListUpdate(description = Some(description))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id: The user ID of the user who owns the list being requested by a `slug`.
    * @param description : The description to give the list.
    */
  def updateListDescriptionBySlugAndOwnerId(slug: String, owner_id: Long, description: String): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    val update = TwitterListUpdate(description = Some(description))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param update : The updates to perform on the list.
    */
  def updateList(list_id: Long, update: TwitterListUpdate): Future[Unit] = {
    val parameters = ListParameters(list_id = Some(list_id))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param update : The updates to perform on the list.
    */
  def updateListBySlugAndOwnerName(slug: String, owner_screen_name: String, update: TwitterListUpdate): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    genericUpdateList(parameters, update)
  }

  /** Updates the specified list. The authenticated user must own the list to be able to update it.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/update" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/update</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id: The user ID of the user who owns the list being requested by a `slug`.
    * @param update : The updates to perform on the list.
    */
  def updateListBySlugAndOwnerId(slug: String, owner_id: Long, update: TwitterListUpdate): Future[Unit] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    genericUpdateList(parameters, update)
  }

  private def genericUpdateList(parameters: ListParameters, update: TwitterListUpdate): Future[Unit] = {
    val listUpdate = UpdateListParameters(
      list_id = parameters.list_id,
      slug = parameters.slug,
      owner_screen_name = parameters.owner_screen_name,
      owner_id = parameters.owner_id,
      description = update.description,
      name = update.name,
      mode = update.mode)
    Post(s"$listsUrl/update.json", listUpdate).respondAs[Unit]
  }

  /** Creates a new list for the authenticated user. Note that you can create up to 1000 lists per account.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/create" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/create</a>.
    *
    * @param name : The name for the list.
    *             A list’s name must start with a letter and can consist only of 25 or fewer letters, numbers, “-“, or “_” characters.
    * @param mode : By default it is `Public`.
    *             Whether your list is public or private.
    * @param description : Optional, by default it is `None`.
    *                    The description to give the list.
    * @return : The new created Twitter list.
    */
  def createList(name: String, mode: Mode = Mode.Public, description: Option[String] = None): Future[TwitterList] = {
    val parameters = CreateListParameters(name, mode, description)
    Post(s"$listsUrl/create.json", parameters).respondAs[TwitterList]
  }

  /** Returns the specified list. Private lists will only be shown if the authenticated user owns the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/show</a>.
    *
    * @param list_id : The numerical id of the list.
    * @return : The Twitter list.
    */
  def getListById(list_id: Long): Future[TwitterList] = {
    val parameters = ListParameters(list_id = Some(list_id))
    genericList(parameters)
  }

  /** Returns the specified list. Private lists will only be shown if the authenticated user owns the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/show</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @return : The Twitter list.
    */
  def getListBySlugAndOwnerName(slug: String, owner_screen_name: String): Future[TwitterList] = {
    val parameters = ListParameters(slug = Some(slug), owner_screen_name = Some(owner_screen_name))
    genericList(parameters)
  }

  /** Returns the specified list. Private lists will only be shown if the authenticated user owns the specified list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/show</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @return : The Twitter list.
    */
  def getListBySlugAndOwnerId(slug: String, owner_id: Long): Future[TwitterList] = {
    val parameters = ListParameters(slug = Some(slug), owner_id = Some(owner_id))
    genericList(parameters)
  }

  private def genericList(parameters: ListParameters): Future[TwitterList] =
    Get(s"$listsUrl/show.json", parameters).respondAs[TwitterList]

  /** Obtain a collection of the lists the specified user is subscribed to, 20 lists per page by default.
    * Does not include the user’s own lists.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/subscriptions" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/subscriptions</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @return : The Twitter lists the specified user is subscribed to.
    */
  def getListSubscriptions(screen_name: String,
                           count: Int = 20,
                           cursor: Long = -1): Future[TwitterLists] = {
    val parameters = SubscriptionsParameters(user_id = None, Some(screen_name), count, cursor)
    genericListSubscriptions(parameters)
  }

  /** Obtain a collection of the lists the specified user is subscribed to, 20 lists per page by default.
    * Does not include the user’s own lists.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/subscriptions" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/subscriptions</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @return : The Twitter lists the specified user is subscribed to.
    */
  def getListSubscriptionsByUserId(user_id: Long,
                                   count: Int = 20,
                                   cursor: Long = -1): Future[TwitterLists] = {
    val parameters = SubscriptionsParameters(Some(user_id), screen_name = None, count, cursor)
    genericListSubscriptions(parameters)
  }

  private def genericListSubscriptions(parameters: SubscriptionsParameters): Future[TwitterLists] =
    Get(s"$listsUrl/subscriptions.json", parameters).respondAs[TwitterLists]

  /** Removes multiple members from a list. The authenticated user must own the list to be able to remove members from it.
    * Note that lists can’t have more than 500 members, and you are limited to removing up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships. Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy_all</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param members_screen_names : A sequence of screen names to remove from the list, up to 100 are allowed in a single request.
    */
  def removeListMembers(list_id: Long, members_screen_names: Seq[String]): Future[Unit] = {
    require(!members_screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = RemoveMembersParameters(list_id = Some(list_id), screen_name = Some(members_screen_names.mkString(",")))
    genericRemoveMembers(parameters)
  }

  /** Removes multiple members from a list. The authenticated user must own the list to be able to remove members from it.
    * Note that lists can’t have more than 500 members, and you are limited to removing up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships. Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy_all</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param members_screen_names : A sequence of screen names to remove from the list, up to 100 are allowed in a single request.
    */
  def removeListMembersBySlugAndOwnerName(slug: String, owner_screen_name: String, members_screen_names: Seq[String]): Future[Unit] = {
    require(!members_screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = RemoveMembersParameters(slug = Some(slug),
      owner_screen_name = Some(owner_screen_name),
      screen_name = Some(members_screen_names.mkString(",")))
    genericRemoveMembers(parameters)
  }

  /** Removes multiple members from a list. The authenticated user must own the list to be able to remove members from it.
    * Note that lists can’t have more than 500 members, and you are limited to removing up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships. Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy_all</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param members_screen_names : A sequence of screen names to remove from the list, up to 100 are allowed in a single request.
    */
  def removeListMembersBySlugAndOwnerId(slug: String, owner_id: Long, members_screen_names: Seq[String]): Future[Unit] = {
    require(!members_screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = RemoveMembersParameters(slug = Some(slug),
      owner_id = Some(owner_id),
      screen_name = Some(members_screen_names.mkString(",")))
    genericRemoveMembers(parameters)
  }

  /** Removes multiple members from a list. The authenticated user must own the list to be able to remove members from it.
    * Note that lists can’t have more than 500 members, and you are limited to removing up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships. Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy_all</a>.
    *
    * @param list_id : The numerical id of the list.
    * @param members_ids : A sequence of user ids to remove from the list, up to 100 are allowed in a single request.
    */
  def removeListMembersIds(list_id: Long, members_ids: Seq[Long]): Future[Unit] = {
    require(!members_ids.isEmpty, "please, provide at least one user id")
    val parameters = RemoveMembersParameters(list_id = Some(list_id), user_id = Some(members_ids.mkString(",")))
    genericRemoveMembers(parameters)
  }

  /** Removes multiple members from a list. The authenticated user must own the list to be able to remove members from it.
    * Note that lists can’t have more than 500 members, and you are limited to removing up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships. Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy_all</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_screen_name : The screen name of the user who owns the list being requested by a `slug`.
    * @param members_ids : A sequence of user ids to remove from the list, up to 100 are allowed in a single request.
    */
  def removeListMembersIdsBySlugAndOwnerName(slug: String, owner_screen_name: String, members_ids: Seq[Long]): Future[Unit] = {
    require(!members_ids.isEmpty, "please, provide at least one user id")
    val parameters = RemoveMembersParameters(slug = Some(slug),
      owner_screen_name = Some(owner_screen_name),
      user_id = Some(members_ids.mkString(",")))
    genericRemoveMembers(parameters)
  }

  /** Removes multiple members from a list. The authenticated user must own the list to be able to remove members from it.
    * Note that lists can’t have more than 500 members, and you are limited to removing up to 100 members to a list at a time with this method.
    * Please note that there can be issues with lists that rapidly remove and add memberships. Take care when using these methods such that you are not too rapidly switching between removals and adds on the same list.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/post/lists/members/destroy_all" target="_blank">
    *   https://dev.twitter.com/rest/reference/post/lists/members/destroy_all</a>.
    *
    * @param slug : You can identify a list by its slug instead of its numerical id.
    * @param owner_id : The user ID of the user who owns the list being requested by a `slug`.
    * @param members_ids : A sequence of user ids to remove from the list, up to 100 are allowed in a single request.
    */
  def removeListMembersIdsBySlugAndOwnerId(slug: String, owner_id: Long, members_ids: Seq[Long]): Future[Unit] = {
    require(!members_ids.isEmpty, "please, provide at least one user id")
    val parameters = RemoveMembersParameters(slug = Some(slug),
      owner_id = Some(owner_id),
      user_id = Some(members_ids.mkString(",")))
    genericRemoveMembers(parameters)
  }

  private def genericRemoveMembers(parameters: RemoveMembersParameters): Future[Unit] =
    Post(s"$listsUrl/members/destroy_all.json", parameters).respondAs[Unit]

  /** Returns the lists owned by the specified Twitter user.
    * Private lists will only be shown if the authenticated user is also the owner of the lists.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/ownerships" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/ownerships</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @return : The Twitter lists owned by the specified user.
    */
  def getListOwnerships(screen_name: String, count: Int = 20, cursor: Long = -1): Future[TwitterLists] = {
    val parameters = OwnershipsParameters(user_id = None, Some(screen_name), count, cursor)
    genericGetListOwnerships(parameters)
  }

  /** Returns the lists owned by the specified Twitter user.
    * Private lists will only be shown if the authenticated user is also the owner of the lists.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/lists/ownerships" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/lists/ownerships</a>.
    *
    * @param user_id : The ID of the user for whom to return results for.
    * @param count : By default it is `20`.
    *              The amount of results to return per page. Defaults to 20.
    *              No more than 1000 results will ever be returned in a single page.
    * @param cursor : By default it is `-1`,  which is the first “page”.
    *               Breaks the results into pages. Provide values as returned in the response body’s `next_cursor` and `previous_cursor` attributes to page back and forth in the list.
    * @return : The Twitter lists owned by the specified user.
    */
  def getListOwnershipsForUserId(user_id: Long, count: Int = 20, cursor: Long = -1): Future[TwitterLists] = {
    val parameters = OwnershipsParameters(Some(user_id), screen_name = None, count, cursor)
    genericGetListOwnerships(parameters)
  }

  private def genericGetListOwnerships(parameters: OwnershipsParameters): Future[TwitterLists] =
    Get(s"$listsUrl/ownerships.json", parameters).respondAs[TwitterLists]
}
