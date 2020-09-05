package io.github.ztkmkoo.dss.core.actor;

import akka.actor.Cancellable;
import akka.actor.typed.*;
import akka.actor.typed.javadsl.ActorContext;
import akka.japi.function.Function;
import akka.japi.function.Function2;
import io.github.ztkmkoo.dss.core.message.DssCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.ExecutionContextExecutor;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 02:58
 */
class DssActorTest {

    @Test
    void getLog() {
        final TestActor actor = new TestActor();

        final Logger logger = actor.getLog();
        assertNotNull(logger);
        assertEquals(TestContext.class.getName(), logger.getName());
    }

    private static class TestActor implements DssActor<DssCommand> {

        @Override
        public Behavior<DssCommand> getBehavior() {
            return null;
        }

        @Override
        public ActorContext<DssCommand> getContext() {
            return new TestContext();
        }
    }

    private static class TestContext implements ActorContext<DssCommand> {

        final Logger logger = LoggerFactory.getLogger(TestContext.class);

        @Override
        public ActorContext<DssCommand> asJava() {
            return null;
        }

        @Override
        public akka.actor.typed.scaladsl.ActorContext<DssCommand> asScala() {
            return null;
        }

        @Override
        public ActorRef<DssCommand> getSelf() {
            return null;
        }

        @Override
        public ActorSystem<Void> getSystem() {
            return null;
        }

        @Override
        public Logger getLog() {
            return logger;
        }

        @Override
        public void setLoggerName(String name) {

        }

        @Override
        public void setLoggerName(Class<?> clazz) {

        }

        @Override
        public List<ActorRef<Void>> getChildren() {
            return null;
        }

        @Override
        public Optional<ActorRef<Void>> getChild(String name) {
            return Optional.empty();
        }

        @Override
        public <U> ActorRef<U> spawnAnonymous(Behavior<U> behavior) {
            return null;
        }

        @Override
        public <U> ActorRef<U> spawnAnonymous(Behavior<U> behavior, Props props) {
            return null;
        }

        @Override
        public <U> ActorRef<U> spawn(Behavior<U> behavior, String name) {
            return null;
        }

        @Override
        public <U> ActorRef<U> spawn(Behavior<U> behavior, String name, Props props) {
            return null;
        }

        @Override
        public <U> void stop(ActorRef<U> child) {

        }

        @Override
        public <U> void watch(ActorRef<U> other) {

        }

        @Override
        public <U> void watchWith(ActorRef<U> other, DssCommand msg) {

        }

        @Override
        public <U> void unwatch(ActorRef<U> other) {

        }

        @Override
        public void setReceiveTimeout(Duration timeout, DssCommand msg) {

        }

        @Override
        public void cancelReceiveTimeout() {

        }

        @Override
        public <U> Cancellable scheduleOnce(Duration delay, ActorRef<U> target, U msg) {
            return null;
        }

        @Override
        public ExecutionContextExecutor getExecutionContext() {
            return null;
        }

        @Override
        public <U> ActorRef<U> messageAdapter(Class<U> messageClass, Function<U, DssCommand> f) {
            return null;
        }

        @Override
        public <Req, Res> void ask(Class<Res> resClass, RecipientRef<Req> target, Duration responseTimeout, Function<ActorRef<Res>, Req> createRequest, Function2<Res, Throwable, DssCommand> applyToResponse) {

        }

        @Override
        public <Value> void pipeToSelf(CompletionStage<Value> future, Function2<Value, Throwable, DssCommand> applyToResult) {

        }

        @Override
        public akka.actor.ActorContext classicActorContext() {
            return null;
        }
    }
}