package demo.flink.checkpoint

import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object CheckpointUtils {
  def setCheckpoint(env: StreamExecutionEnvironment, fileBackend: String): Unit = {
    // start a checkpoint every 1000 ms
    env.enableCheckpointing(1000);
    // advanced options:
    // set mode to exactly-once (this is the default)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
    // checkpoints have to complete within one minute, or are discarded
    env.getCheckpointConfig.setCheckpointTimeout(60000);
    // make sure 500 ms of progress happen between checkpoints
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500);
    // allow only one checkpoint to be in progress at the same time
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1);
    // enable externalized checkpoints which are retained after job cancellation

    // enableExternalizedCheckpoints用于开启checkpoints的外部持久化，但是在job失败的时候不会自动清理，需要自己手工清理state；
    // ExternalizedCheckpointCleanup用于指定当job canceled的时候externalized checkpoint该如何清理，
    // DELETE_ON_CANCELLATION的话，在job canceled的时候会自动删除externalized state，但是如果是FAILED的状态则会保留；
    // RETAIN_ON_CANCELLATION则在job canceled的时候会保留externalized checkpoint state
    env.getCheckpointConfig.enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
    // This determines if a task will be failed if an error occurs in the execution of the task’s checkpoint procedure.
    env.getCheckpointConfig.setFailOnCheckpointingErrors(true);

    // TODO: RocksDBStateBackend 支持增量Checkpoint
    env.setStateBackend(new RocksDBStateBackend(fileBackend, true));

  }


}
