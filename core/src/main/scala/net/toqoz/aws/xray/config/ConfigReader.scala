package net.toqoz.aws.xray.config

import com.amazonaws.xray.entities.StringValidator

trait ConfigReader {
  val OVERRIDE_ENVIRONMENT_VARIABLE_KEY: String
  val OVERRIDE_SYSTEM_PROPERTY_KEY: String

  protected def readValue: Option[String] =
    (System.getenv(OVERRIDE_ENVIRONMENT_VARIABLE_KEY), System.getProperty(OVERRIDE_SYSTEM_PROPERTY_KEY)) match {
      case (env, _) if StringValidator.isNotNullOrBlank(env) => Some(env)
      case (_, sys) if StringValidator.isNotNullOrBlank(sys) => Some(sys)
      case _                                                 => None
    }
}
