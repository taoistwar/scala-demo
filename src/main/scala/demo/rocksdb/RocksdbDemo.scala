package demo.rocksdb

import org.rocksdb.{Options, RocksDB, WriteBatch, WriteOptions}

object RocksdbDemo {

  RocksDB.loadLibrary()

  def main(args: Array[String]): Unit = {
    val path = "./tmp.rocksdb"
    val options = new Options()
    options.setCreateIfMissing(true)
    val rocksDB = RocksDB.open(options, path)


    save(rocksDB)


    query(rocksDB)
  }

  private def query(rocksDB: RocksDB) = {
    val iterator = rocksDB.newIterator()
    iterator.seekToFirst()
    while (iterator.isValid) {
      println("iter key:" + new String(iterator.key()) + ", iter value:" + new String(iterator.value()));
      iterator.next()
    }
    iterator.close()


    iterator.seekToLast()
    while (iterator.isValid) {
      println("iter key:" + new String(iterator.key()) + ", iter value:" + new String(iterator.value()));
      iterator.prev()
    }
    iterator.close()
  }

  private def save(rocksDB: RocksDB) = {
    rocksDB.put("k1".getBytes(), "v1".getBytes())
  }

  private def batch(rocksDB: RocksDB) = {
    val batch = new WriteBatch()
    batch.put("k1".getBytes(), "v1".getBytes())
    val options = new WriteOptions()
    rocksDB.write(options, batch)
  }

}
