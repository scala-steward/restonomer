package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.DataFrameMatchers
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame

class ConvertColumnCaseTransformationSpec extends CoreSpec with DataFrameMatchers {

  import sparkSession.implicits._

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |    "col_a": "1",
          |    "COL_B": "2"
          |}""".stripMargin
      )
    )

  "transform() - with valid column name and case type" should "transform the column case" in {
    val restonomerTransformation = ChangeColumnCase(
      caseType = "lower"
    )

    val expectedRestonomerResponseTransformedDF = Seq(("1", "2"))
      .toDF("col_a", "col_b")

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF should matchExpectedDataFrame(
      expectedDF = expectedRestonomerResponseTransformedDF
    )
  }

}
