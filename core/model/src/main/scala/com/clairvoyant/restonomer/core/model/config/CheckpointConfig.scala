package com.clairvoyant.restonomer.core.model.config

case class CheckpointConfig(
    name: String,
    http: HttpConfig
) extends RestonomerConfig
