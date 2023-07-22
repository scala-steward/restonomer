package com.clairvoyant.restonomer.transformation

import com.clairvoyant.restonomer.common.{IntegrationTestDependencies, MockFileSystemPersistence}

class FlattenSchemaTransformationIntegrationTest extends IntegrationTestDependencies with MockFileSystemPersistence {

  override val mappingsDirectory: String = "transformation"

  it should "flatten the schema of the restonomer response dataframe" in {
    runCheckpoint(checkpointFileName = "checkpoint_flatten_schema_transformation.conf")
    outputDF should matchExpectedDataFrame(readMockJSON("expected_flatten_schema_transformation.json"))
  }

}
