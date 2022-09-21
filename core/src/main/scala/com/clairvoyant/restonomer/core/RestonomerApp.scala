package com.clairvoyant.restonomer.core

import com.clairvoyant.restonomer.core.app.RestonomerContext

object RestonomerApp extends App {
  val restonomerContext = RestonomerContext()

  restonomerContext.runCheckpoint(checkpointFilePath = "checkpoint_basic_authentication_token.conf")
  restonomerContext.runCheckpointsUnderDirectory(checkpointsDirectoryPath = "category-1")
  restonomerContext.runAllCheckpoints()
}
