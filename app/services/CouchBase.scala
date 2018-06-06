package services

import javax.inject.{Inject, Singleton}
import org.reactivecouchbase.rs.scaladsl.{Bucket, ReactiveCouchbase}
import play.api.Configuration
import play.api.inject.ApplicationLifecycle

@Singleton
class CouchBase @Inject()(configuration: Configuration,
                          lifecycle: ApplicationLifecycle) {

  private val driver = ReactiveCouchbase(configuration.underlying.getConfig("cb"))

  def bucket(name: String): Bucket = driver.bucket(name)

  lifecycle.addStopHook { () =>
    driver.terminate()
  }
}
