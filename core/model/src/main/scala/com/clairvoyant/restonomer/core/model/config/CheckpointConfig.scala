package com.clairvoyant.restonomer.core.model.config

case class CheckpointConfig(
    name: String,
    request: Option[String],
    requestConfig: Option[RequestConfig]
)
