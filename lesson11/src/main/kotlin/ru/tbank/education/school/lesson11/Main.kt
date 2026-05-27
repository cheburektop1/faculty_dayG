package ru.tbank.education.school.lesson11

import kotlinx.coroutines.*
import java.io.File
import java.math.BigInteger
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

object CreateThreads {

    fun run(): List<Thread> {

        val names = listOf("Thread-A", "Thread-B", "Thread-C")

        val threads = names.map { name ->
            Thread({
                repeat(5) {
                    println("$name iteration $it")
                    Thread.sleep(500)
                }
            }, name)
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        return threads
    }
}


object RaceCondition {

    var counter = 0

    fun run(): Int {

        counter = 0

        val threads = List(10) {
            Thread {
                repeat(1000) {
                    counter++
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        println("Final counter = $counter")

        return counter
    }
}


object SynchronizedCounter {

    var counter = 0

    @Synchronized
    fun inc() {
        counter++
    }

    fun run(): Int {

        counter = 0

        val threads = List(10) {
            Thread {
                repeat(1000) {
                    inc()
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        return counter
    }
}


object Deadlock {

    private val lock1 = Object()
    private val lock2 = Object()

    fun runDeadlock() {

        val t1 = Thread {
            synchronized(lock1) {
                Thread.sleep(100)
                synchronized(lock2) {
                    println("Thread1 finished")
                }
            }
        }

        val t2 = Thread {
            synchronized(lock2) {
                Thread.sleep(100)
                synchronized(lock1) {
                    println("Thread2 finished")
                }
            }
        }

        t1.start()
        t2.start()
    }

    fun runFixed(): Boolean {

        val t1 = Thread {
            synchronized(lock1) {
                synchronized(lock2) {
                    println("Thread1 finished safely")
                }
            }
        }

        val t2 = Thread {
            synchronized(lock1) {
                synchronized(lock2) {
                    println("Thread2 finished safely")
                }
            }
        }

        t1.start()
        t2.start()

        t1.join()
        t2.join()

        return true
    }
}

object ExecutorServiceExample {

    fun run(): List<String> {

        val executor = Executors.newFixedThreadPool(4)

        val results = mutableListOf<String>()

        val futures = (1..20).map { i ->
            executor.submit<String> {

                val text = "Task $i executed by ${Thread.currentThread().name}"

                println(text)

                Thread.sleep(200)

                text
            }
        }

        futures.forEach {
            results.add(it.get())
        }

        executor.shutdown()

        return results
    }
}

object FutureFactorial {

    private fun factorial(n: Int): BigInteger {

        var result = BigInteger.ONE

        for (i in 2..n) {
            result = result.multiply(BigInteger.valueOf(i.toLong()))
        }

        return result
    }

    fun run(): Map<Int, BigInteger> {

        val executor = Executors.newFixedThreadPool(4)

        val futures = (1..10).associateWith { n ->
            executor.submit(Callable {
                factorial(n)
            })
        }

        val result = futures.mapValues { it.value.get() }

        executor.shutdown()

        return result
    }
}

object CoroutineLaunch {

    fun run(): List<String> = runBlocking {

        val logs = mutableListOf<String>()

        val jobs = (1..3).map { id ->
            launch {

                repeat(5) {

                    val text = "Coroutine-$id iteration $it"

                    println(text)

                    logs.add(text)

                    delay(500)
                }
            }
        }

        jobs.joinAll()

        logs
    }
}

object AsyncAwait {

    fun run(): Long = runBlocking {

        val part = 1_000_000 / 4

        val tasks = (0 until 4).map { i ->

            async {

                val start = i * part + 1

                val end = if (i == 3) 1_000_000 else (i + 1) * part

                var sum = 0L

                for (x in start..end) {
                    sum += x
                }

                sum
            }
        }

        tasks.sumOf { it.await() }
    }
}

object StructuredConcurrency {

    fun run(failingCoroutineIndex: Int): Int = runBlocking {

        try {

            coroutineScope {

                repeat(5) { i ->

                    launch {

                        if (i == failingCoroutineIndex) {
                            throw RuntimeException("Failure in coroutine $i")
                        }

                        delay(500)

                        println("Coroutine $i finished")
                    }
                }
            }

        } catch (e: Exception) {
            println("Exception caught: ${e.message}")
        }

        return@runBlocking 0
    }
}

// ------------------------------------------------
// Задание 10
// ------------------------------------------------

object WithContextIO {

    fun run(filePaths: List<String>): Map<String, String> = runBlocking {

        val tasks = filePaths.map { path ->

            async {

                val content = withContext(Dispatchers.IO) {
                    File(path).readText()
                }

                path to content
            }
        }

        tasks.map { it.await() }.toMap()
    }
}

data class DownloadStats(
    val success: Int,
    val failed: Int,
    val timeMs: Long
)

object ImageDownloader {

    fun run(urls: List<String>, outputDir: String): DownloadStats = runBlocking {

        val dir = File(outputDir)

        if (!dir.exists()) {
            dir.mkdirs()
        }

        var success = AtomicInteger(0)
        var failed = AtomicInteger(0)
        var progress = AtomicInteger(0)

        val time = measureTimeMillis {

            val jobs = urls.mapIndexed { index, url ->

                async(Dispatchers.IO) {

                    try {

                        val bytes = URL(url).readBytes()

                        val file = File(dir, "img_$index.jpg")

                        file.writeBytes(bytes)

                        val done = progress.incrementAndGet()

                        println("Downloaded $done/${urls.size}")

                        success.incrementAndGet()

                    } catch (e: Exception) {

                        failed.incrementAndGet()
                    }
                }
            }

            jobs.awaitAll()
        }

        DownloadStats(
            success.get(),
            failed.get(),
            time
        )
    }
}
fun main() {

    println("----- Threads -----")
    CreateThreads.run()

    println("\n----- Race Condition -----")
    RaceCondition.run()

    println("\n----- Synchronized Counter -----")
    println(SynchronizedCounter.run())

    println("\n----- Deadlock fixed -----")
    Deadlock.runFixed()

    println("\n----- Executor Service -----")
    ExecutorServiceExample.run()

    println("\n----- Futures factorial -----")
    println(FutureFactorial.run())

    println("\n----- Coroutines launch -----")
    CoroutineLaunch.run()

    println("\n----- Async await sum -----")
    println(AsyncAwait.run())

    println("\n----- Structured concurrency -----")
    StructuredConcurrency.run(2)

    println("\n----- Image downloader -----")

    val urls = List(10) {
        "https://picsum.photos/200/300"
    }

    val stats = ImageDownloader.run(urls, "downloads")

    println("Downloaded successfully: ${stats.success}")
    println("Failed: ${stats.failed}")
    println("Time: ${stats.timeMs} ms")
}