package com.oa.crudops.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class JMHBenchmarking{

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void doExection() throws InterruptedException {
        Thread.sleep(1000);
    }

    public static void main(String[] args) throws InterruptedException, RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHBenchmarking.class.getSimpleName())
                .forks(2)
                .build();
        new Runner(opt).run();

    }

}
