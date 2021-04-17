import java.util.concurrent.SynchronousQueue;

final class app {
  public static void main(String[] args) {
    int n = 100;
    if (args.length > 0) {
      n = Integer.parseInt(args[0]);
    }

    var queue = new SynchronousQueue<Integer>();
    final var q1 = queue;
    Thread.ofVirtual().start(() -> generate(q1));
    try {
      for (var i = 0; i < n; i++) {
        var prime = queue.take();
        System.out.println(prime);
        final var inQueue = queue;
        final var outQueue = new SynchronousQueue<Integer>();
        Thread.ofVirtual().start(() -> filter(inQueue, outQueue, prime));
        queue = outQueue;
      }
    } catch (InterruptedException e) {
    }
  }

  static void generate(SynchronousQueue<Integer> queue) {
    try {
      for (var i = 2; ; i++) {
        queue.put(i);
      }
    } catch (InterruptedException e) {
    }
  }

  static void filter(
      SynchronousQueue<Integer> inQueue, SynchronousQueue<Integer> outQueue, Integer prime) {
    try {
      while (true) {
        var i = inQueue.take();
        if (i % prime != 0) {
          outQueue.put(i);
        }
      }
    } catch (InterruptedException e) {
    }
  }
}