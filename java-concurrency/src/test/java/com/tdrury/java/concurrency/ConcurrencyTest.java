package com.tdrury.java.concurrency;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class ConcurrencyTest {

    @Test
    public void whenExecutorServiceInvokeAll_givenSomeTasks_thenAllTasksRun() throws InterruptedException {
        // given - 10 tasks with 5 threads in pool
        Map<String,Foo> results = new ConcurrentHashMap<>();
        IntStream indices = IntStream.range(0, 10);
        List<FooTask> tasks = indices.mapToObj(i -> new FooTask("name"+i, i)).collect(Collectors.toList());
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // when - invokeAll then get all futures
        List<Future<Foo>> futures = executorService.invokeAll(tasks);
        futures.forEach(f -> {
            try {
                Foo foo = f.get();
                results.put("thread-"+ foo.index, foo);
            } catch (InterruptedException | ExecutionException ex) {
                fail(ex);
            }
        });

        // then - all tasks finish
        log.info("whenExecutorServiceInvokeAll: results={}", results);
        assertThat(results.size(), is(10));
        assertThat(results.values().stream().mapToInt(Foo::getIndex).sum(), is(9+8+7+6+5+4+3+2+1));
    }

    @Test
    public void whenExecutorServiceSubmitThenGet_givenSomeTasks_thenTasksRunSequentially() {
        // given - 8 tasks with 3 threads in pool
        IntStream count = IntStream.range(0, 8);
        List<String> tasks = count.mapToObj(i -> "task"+i).collect(Collectors.toList());
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Map<String,Foo> results = new ConcurrentHashMap<>();
        AtomicInteger i = new AtomicInteger();

        // when - submit task then get its future, each task will run sequentially
        tasks.forEach( t -> {
            log.info("whenExecutorServiceSubmitThenGet: creating FooTask #{} for {}", i, t);
            FooTask task = new FooTask(t, i.getAndIncrement());
            log.info("whenExecutorServiceSubmitThenGet: submitting FooTask for {}", t);
            Future<Foo> future = executorService.submit(task);

            Foo foo = null;
            try {
                log.info("whenExecutorServiceSubmitThenGet: calling get() on FooTask for {}", t);
                foo = future.get();
                foo.message = "ok";
                log.info("whenExecutorServiceSubmitThenGet: get() completed on FooTask for {}", t);
            } catch (InterruptedException | ExecutionException ex) {
                fail(ex);
            }
            results.put(t, foo);

        });

        // then - all tasks finish
        log.info("whenExecutorServiceSubmitThenGet: all results={}", results);
        assertThat(i.get(), is(8));
        assertThat(results.values().stream().mapToInt(Foo::getIndex).sum(), is(7+6+5+4+3+2+1));
    }

    @Test
    public void whenExecutorServiceSubmitAll_givenSomeTasks_thenTasksRunInParallel() {
        // given - 8 tasks with 3 threads in pool
        IntStream count = IntStream.range(0, 8);
        List<String> tenants = count.mapToObj(i -> "task"+i).collect(Collectors.toList());
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Map<String,Foo> results = new ConcurrentHashMap<>();
        Map<String,Future<Foo>> futures = new ConcurrentHashMap<>();
        AtomicInteger i = new AtomicInteger();

        // when - submit all tasks they will run in parallel
        tenants.forEach( t -> {
            log.info("whenExecutorServiceSubmitAll: creating FooTask #{} for {}", i, t);
            FooTask task = new FooTask(t, i.getAndIncrement());
            log.info("whenExecutorServiceSubmitAll: submitting FooTask for {}", t);
            Future<Foo> future = executorService.submit(task);
            futures.put(t, future);
        });

        // and when - get all futures
        futures.forEach( (t,future) -> {
            Foo foo = null;
            try {
                log.info("whenExecutorServiceSubmitAll: calling get() on FooTask for {}...", t);
                foo = future.get();
                foo.message = "ok";
                log.info("whenExecutorServiceSubmitAll: get() completed for {}", t);
            } catch (InterruptedException | ExecutionException ex) {
                fail(ex);
            }
            results.put(t, foo);
        });

        // then - all tasks finish
        log.info("whenExecutorServiceSubmitAll: all results={}", results);
        assertThat(i.get(), is(8));
        assertThat(results.values().stream().mapToInt(Foo::getIndex).sum(), is(7+6+5+4+3+2+1));
    }

    @Test
    public void whenForkJoinPoolSubmit_givenParallelStream_thenAllThreadsRun() {
        // given - 10 tasks with 5 threads in pool
        IntStream indices = IntStream.range(0, 10);
        ForkJoinPool pool = new ForkJoinPool(5);
        List<Foo> results = null;

        // when - submit all tasks to ForkJoinPool they run in parallel
        try {
            results = pool.submit(() -> indices.parallel().mapToObj(this::buildFoo).collect(Collectors.toList())).get();
        } catch (InterruptedException | ExecutionException ex) {
            fail(ex);
        }

        // then - all tasks finish
        log.info("ForkJoin: results={}", results);
        assertThat(results.size(), is(10));
        assertThat(results.stream().mapToInt(Foo::getIndex).sum(), is(9+8+7+6+5+4+3+2+1));
    }


    Foo buildFoo(int index) {
        Foo f = new Foo();
        f.index = index;
        f.timestamp = System.currentTimeMillis();
        try {
            Thread.sleep(index*200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return f;
    }

    @Data
    static class FooTask implements Callable<Foo> {

        private final int index;
        private final String name;

        public FooTask(String name, int index) {
            this.name = name;
            this.index = index;
        }

        @Override
        public Foo call() {
            try {
                long duration = index*200;
                log.info("FooTask.call: sleeping for {} ms", duration);
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Foo f = new Foo();
            f.index = index;
            f.timestamp = System.currentTimeMillis();
            return f;
        }
    }

    @Data
    static class Foo {
        public int index;
        public String name;
        public long timestamp;
        public String message;
    }

}
