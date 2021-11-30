package demo.ssl

import org.apache.lucene.codecs.CodecUtil
import org.apache.lucene.store.{BufferedChecksumIndexInput, IOContext, SimpleFSDirectory}

import java.io.{ByteArrayInputStream, DataInputStream}
import java.nio.file.{Path, Paths}
import java.security.GeneralSecurityException
import javax.crypto.{Cipher, CipherInputStream, SecretKey, SecretKeyFactory}
import javax.crypto.spec.{GCMParameterSpec, PBEKeySpec, SecretKeySpec}
import scala.reflect.io.Path

object KeyStoreDemo {
  val KEYSTORE_FILENAME = "elasticsearch.keystore"
  val MIN_FORMAT_VERSION = 1
  val FORMAT_VERSION = 4
  val DECRYPT_MODE = 2

  /**
   * "keystore.seed"
   *
   * @param args
   */
  def main(args: Array[String]): Unit = {
    val configDir = "/Users/taoistwar/bigdata/elk/elasticsearch-7.14.1-SNAPSHOT/config/"
    val path = Paths.get(configDir)
    val directory = new SimpleFSDirectory(path)
    val indexInput = directory.openInput(KEYSTORE_FILENAME, IOContext.READONCE)
    val input = new BufferedChecksumIndexInput(indexInput);
    val formatVersion = CodecUtil.checkHeader(input, KEYSTORE_FILENAME, MIN_FORMAT_VERSION, FORMAT_VERSION)
    println(formatVersion)
    val hasPasswordByte = input.readByte()
    val hasPassword = hasPasswordByte == 1
    val dataBytesLen = input.readInt()
    val dataBytes = new Array[Byte](dataBytesLen);
    input.readBytes(dataBytes, 0, dataBytesLen)
    CodecUtil.checkFooter(input)
    println(
      s"""
         |hasPassword: ${hasPassword}
         |formatVersion: ${formatVersion}
         |dataBytesLen: ${dataBytesLen}
         |dataBytes: ${dataBytes}
         |""".stripMargin)
    parsePassword(dataBytes)
  }

  private def parsePassword(dataBytes: _root_.scala.Array[Byte]) = {
    val input = new DataInputStream(new ByteArrayInputStream(dataBytes))
    val saltLen = input.readInt();
    val salt = new Array[Byte](saltLen);
    input.readFully(salt);
    val ivLen = input.readInt();
    val iv = new Array[Byte](ivLen);
    input.readFully(iv);
    val encryptedLen = input.readInt();
    val encryptedBytes = new Array[Byte](encryptedLen);
    input.readFully(encryptedBytes);
    if (input.read() != -1) {
      throw new SecurityException("Keystore has been corrupted or tampered with");
    }
    this.decryptPassword(salt, iv, encryptedBytes)
  }


  private def decryptPassword(salt: Array[Byte], iv: Array[Byte], encryptedBytes: Array[Byte]) = {
    val cipher = genCipher(DECRYPT_MODE, new Array[Char](0), salt, iv)
    val input = new DataInputStream(new CipherInputStream(new ByteArrayInputStream(encryptedBytes), cipher))
    var numEntries = input.readInt()
    while (numEntries > 0) {
      numEntries -= 1
      val setting = input.readUTF();
      println(setting)
      val entrySize = input.readInt();
      val entryBytes = new Array[Byte](entrySize);
      input.readFully(entryBytes);
      println("entryBytes", new String(entryBytes))
    }
    if (input.read() != -1) {
      println("Keystore has been corrupted or tampered with")
    }
  }

  def genCipher(opmode: Int, password: Array[Char], salt: Array[Byte], iv: Array[Byte]): Cipher = {
    val KDF_ITERS = 10000
    val CIPHER_KEY_BITS = 128;
    val KDF_ALGO = "PBKDF2WithHmacSHA512"
    val CIPHER_ALGO = "AES"
    val GCM_TAG_BITS = 128
    val CIPHER_MODE = "GCM"
    val CIPHER_PADDING = "NoPadding"

    var secretKey: SecretKey = null
    try {
      val keySpec = new PBEKeySpec(password, salt, KDF_ITERS, CIPHER_KEY_BITS);
      val keyFactory = SecretKeyFactory.getInstance(KDF_ALGO);
      secretKey = keyFactory.generateSecret(keySpec);
    } catch {
      case e: Exception =>
        // Security Providers might throw a subclass of Error in FIPS 140 mode, if some prerequisite like
        // salt, iv, or password length is not met. We catch this because we don't want the JVM to exit.
        throw new GeneralSecurityException("Error generating an encryption key from the provided password", e);
    }
    val secret = new SecretKeySpec(secretKey.getEncoded, CIPHER_ALGO);

    val spec = new GCMParameterSpec(GCM_TAG_BITS, iv);
    val cipher = Cipher.getInstance(CIPHER_ALGO + "/" + CIPHER_MODE + "/" + CIPHER_PADDING);
    cipher.init(opmode, secret, spec);
    cipher.updateAAD(salt);
    cipher;
  }
}
