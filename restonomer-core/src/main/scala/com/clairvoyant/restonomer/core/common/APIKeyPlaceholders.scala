package com.clairvoyant.restonomer.core.common

import com.clairvoyant.restonomer.core.exception.RestonomerException

enum APIKeyPlaceholders {
  case QueryParam, RequestHeader, Cookie
}

object APIKeyPlaceholders {

  def apply(apiKeyPlaceholder: String): APIKeyPlaceholders =
    if (isValidAPIKeyPlaceholder(apiKeyPlaceholder))
      valueOf(apiKeyPlaceholder)
    else
      throw new RestonomerException(s"The API Key placeholder: $apiKeyPlaceholder is not supported.")

  def isValidAPIKeyPlaceholder(apiKeyPlaceholder: String): Boolean = values.exists(_.toString == apiKeyPlaceholder)

}
