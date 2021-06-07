package io.micronaut.jms.docs.exceptions

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.micronaut.jms.docs.AbstractJmsKotest
import java.lang.RuntimeException
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ErrorHandlingSpec: AbstractJmsKotest ({

    val specName = javaClass.simpleName

    given("A consumer which will throw an error") {
        val applicationContext = startContext(specName)
        `when`("the message is sent") {
            val producer = applicationContext.getBean(ErrorHandlingProducer::class.java)
            val consumer = applicationContext.getBean(ErrorThrowingConsumer::class.java)
            val errorHandler = applicationContext.getBean(CountingErrorHandler::class.java)
            val classLevelErrorHandler = applicationContext.getBean(AccumulatingErrorHandler::class.java)

            producer.push("throw an error")
            then("the exception is handled by the handlers") {
                consumer.processed shouldHaveSize 0
                errorHandler.count shouldBe 1
                classLevelErrorHandler.exceptions shouldHaveSize 1
            }
        }
        applicationContext.stop()
    }
})