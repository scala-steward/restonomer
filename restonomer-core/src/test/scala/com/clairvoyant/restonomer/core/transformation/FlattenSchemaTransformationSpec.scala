package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame

class FlattenSchemaTransformationSpec extends CoreSpec with DataFrameMatchers {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(
      text = Seq(
        """
          |{
          |  "rewardApprovedMonthPeriod": {
          |      "from": "2021-09",
          |      "to": "2021-10"
          |    }
          |}
          |""".stripMargin
      )
    )

  "transform()" should "flatten the response dataframe" in {
    val restonomerTransformation = FlattenSchema()

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    val expectedRestonomerResponseTransformedDF: DataFrame =
      new JSONTextToDataFrameReader(
        sparkSession = sparkSession
      ).read(text =
        Seq(
          """
            |{
            |  "rewardApprovedMonthPeriod_from": "2021-09",
            |  "rewardApprovedMonthPeriod_to": "2021-10"
            |}
            |""".stripMargin
        )
      )

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(expectedRestonomerResponseTransformedDF)
  }

}
