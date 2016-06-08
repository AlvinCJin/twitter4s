package com.danielasfregola.twitter4s.http.clients.media.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class MediaAppendParameters(media_id: Long,
                                 segment_index: Int,
                                 media: String,
                                 command: String = "APPEND") extends Parameters
