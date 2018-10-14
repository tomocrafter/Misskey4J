package net.misskey4j

import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

//Twitter4jを参考にしました。
class Dispatcher(configuration: Configuration) {

    private val executorService: ExecutorService

    init {
        executorService = Executors.newFixedThreadPool(configuration.getAsyncNumThreads(), object : ThreadFactory {
            val atomicLong = AtomicLong(0)

            override fun newThread(r: Runnable): Thread {
                val thread: Thread = Executors.defaultThreadFactory().newThread(r)
                thread.name = String.format(Locale.ROOT, "Async Executor Pool #%1\$d", atomicLong.getAndIncrement())
                return thread
            }
        })
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            this@Dispatcher.shutdown()
        })
    }

    @Synchronized
    fun execute(block: () -> Unit) {
        executorService.execute(block)
    }

    @Synchronized
    fun shutdown() {
        this.executorService.shutdown()

        try {
            if (!this.executorService.awaitTermination(5000L, TimeUnit.MILLISECONDS)) {
                this.executorService.shutdownNow()
            }
        } catch (var2: InterruptedException) {
        }
    }
}