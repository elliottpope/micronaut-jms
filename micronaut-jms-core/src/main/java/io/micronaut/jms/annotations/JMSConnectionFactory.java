package io.micronaut.jms.annotations;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.DefaultScope;

import java.lang.annotation.*;

/***
 *
 * Annotation to denote a JMS Connection Factory bean. Any connection factory that a {@link JMSListener}
 *  references must be annotated with this annotation. If a {@link javax.jms.ConnectionFactory} is present in the
 *  {@link io.micronaut.context.BeanContext} but not annotated with this annotation, the post-processing logic will fail
 *  and the {@link JMSListener} will not work.
 *
 * This annotation also requires that the {@link javax.jms.ConnectionFactory} be eagerly initialized as a {@link Context}
 *  bean. This ensures that it is always available before the {@link JMSListener} post-processing is initiated.
 *
 * Usage:
 *  <pre>
 *      {@code
 * @JMSConnectionFactory("myConnectionFactory")
 * public ConnectionFactory myConnectionFactory() {
 *     return new ActiveMqConnectionFactory("vm://localhost?broker.persist=false");
 * }
 *      }
 *  </pre>
 *
 * @author elliott
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Bean
@DefaultScope(Context.class)
public @interface JMSConnectionFactory {
    String value();
}