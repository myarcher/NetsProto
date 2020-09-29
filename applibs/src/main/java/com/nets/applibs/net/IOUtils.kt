package com.nets.applibs.net

import android.os.Build
import android.os.StatFs
import android.text.TextUtils
import java.io.*
import java.lang.reflect.Method
import java.nio.charset.Charset


object IOUtils {
    fun closeQuietly(closeable: Closeable?) {
        if (closeable == null) return
        try {
            closeable.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun flushQuietly(flushable: Flushable?) {
        if (flushable == null) return
        try {
            flushable.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toInputStream(input: CharSequence): InputStream? {
        return ByteArrayInputStream(input.toString().toByteArray())
    }

    @Throws(UnsupportedEncodingException::class)
    fun toInputStream(input: CharSequence, encoding: String?): InputStream? {
        val bytes = input.toString().toByteArray(charset(encoding!!))
        return ByteArrayInputStream(bytes)
    }

    fun toBufferedInputStream(inputStream: InputStream): BufferedInputStream {
        return if (inputStream is BufferedInputStream) inputStream as BufferedInputStream else BufferedInputStream(
            inputStream
        )
    }

    fun toBufferedOutputStream(outputStream: OutputStream?): BufferedOutputStream? {
        return if (outputStream is BufferedOutputStream) outputStream as BufferedOutputStream? else BufferedOutputStream(
            outputStream
        )
    }

    fun toBufferedReader(reader: Reader): BufferedReader {
        return if (reader is BufferedReader) reader as BufferedReader else BufferedReader(reader)
    }

    fun toBufferedWriter(writer: Writer?): BufferedWriter? {
        return if (writer is BufferedWriter) writer as BufferedWriter? else BufferedWriter(writer)
    }

    @Throws(IOException::class)
    fun toString(input: InputStream?): String? {
        return String(toByteArray(input)!!)
    }

    @Throws(IOException::class)
    fun toString(input: InputStream?, encoding: Charset): String? {
        return String(toByteArray(input)!!, encoding)
    }

    @Throws(IOException::class)
    fun toString(input: Reader?): String? {
        return String(toByteArray(input)!!)
    }

    @Throws(IOException::class)
    fun toString(input: Reader?, encoding: Charset): String? {
        return String(toByteArray(input)!!, encoding)
    }

    fun toString(byteArray: ByteArray?): String? {
        return String(byteArray!!)
    }

    fun toString(byteArray: ByteArray?, encoding: Charset): String? {
        return try { String(byteArray!!, encoding)
        } catch (e: UnsupportedEncodingException) {
            String(byteArray!!)
        }
    }

    fun toByteArray(input: Any?): ByteArray? {
        var baos: ByteArrayOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            oos = ObjectOutputStream(baos)
            oos.writeObject(input)
            oos.flush()
            return baos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeQuietly(oos)
            closeQuietly(baos)
        }
        return null
    }

    fun toObject(input: ByteArray?): Any? {
        if (input == null) return null
        var bais: ByteArrayInputStream? = null
        var ois: ObjectInputStream? = null
        try {
            bais = ByteArrayInputStream(input)
            ois = ObjectInputStream(bais)
            return ois.readObject()
        } catch (e: Exception) {
           e.printStackTrace()
        } finally {
            closeQuietly(ois)
           closeQuietly(bais)
        }
        return null
    }

    fun toByteArray(input: CharSequence?): ByteArray? {
        return input?.toString()?.toByteArray() ?: ByteArray(0)
    }

    @Throws(UnsupportedEncodingException::class)
    fun toByteArray(input: CharSequence?, encoding: String?): ByteArray? {
        return input?.toString()?.toByteArray(charset(encoding!!)) ?: ByteArray(0)
    }

    @Throws(IOException::class)
    fun toByteArray(input: InputStream?): ByteArray? {
        val output = ByteArrayOutputStream()
        write(input!!, output)
        output.close()
        return output.toByteArray()
    }

    @Throws(IOException::class)
    fun toByteArray(input: Reader?): ByteArray? {
        val output = ByteArrayOutputStream()
        write(input, output)
        output.close()
        return output.toByteArray()
    }

    @Throws(IOException::class)
    fun toByteArray(input: Reader?, encoding: String?): ByteArray? {
        val output = ByteArrayOutputStream()
        write(input, output, encoding)
        output.close()
        return output.toByteArray()
    }

    @Throws(IOException::class)
    fun toCharArray(input: CharSequence?): CharArray? {
        val output = CharArrayWriter()
        write(input, output)
        return output.toCharArray()
    }

    @Throws(IOException::class)
    fun toCharArray(input: InputStream?): CharArray? {
        val output = CharArrayWriter()
        write(input, output)
        return output.toCharArray()
    }

    @Throws(IOException::class)
    fun toCharArray(input: InputStream?, encoding: String?): CharArray? {
        val output = CharArrayWriter()
        write(input, output, encoding)
        return output.toCharArray()
    }

    @Throws(IOException::class)
    fun toCharArray(input: Reader?): CharArray? {
        val output = CharArrayWriter()
        write(input!!, output)
        return output.toCharArray()
    }

    @Throws(IOException::class)
    fun readLines(
        input: InputStream?,
        encoding: String?
    ): List<String?>? {
        val reader: Reader = InputStreamReader(input, encoding)
        return readLines(reader)
    }

    @Throws(IOException::class)
    fun readLines(input: InputStream?): List<String?>? {
        val reader: Reader = InputStreamReader(input)
        return readLines(reader)
    }

    @Throws(IOException::class)
    fun readLines(input: Reader): List<String>? {
        val reader: BufferedReader = toBufferedReader(input)
        val list: MutableList<String> = ArrayList()
        var line: String = reader.readLine()
        while (line != null) {
            list.add(line)
            line = reader.readLine()
        }
        return list
    }

    @Throws(IOException::class)
    fun write(data: ByteArray?, output: OutputStream) {
        if (data != null) output.write(data)
    }

    @Throws(IOException::class)
    fun write(data: ByteArray?, output: Writer) {
        if (data != null) output.write(String(data))
    }

    @Throws(IOException::class)
    fun write(data: ByteArray?, output: Writer, encoding: Charset) {
        if (data != null) output.write(String(data, encoding))
    }

    @Throws(IOException::class)
    fun write(data: CharArray?, output: Writer) {
        if (data != null) output.write(data)
    }

    @Throws(IOException::class)
    fun write(data: CharArray?, output: OutputStream) {
        if (data != null) output.write(String(data).toByteArray())
    }

    @Throws(IOException::class)
    fun write(data: CharArray?, output: OutputStream, encoding: String?) {
        if (data != null) output.write(String(data).toByteArray(charset(encoding!!)))
    }

    @Throws(IOException::class)
    fun write(data: CharSequence?, output: Writer) {
        if (data != null) output.write(data.toString())
    }

    @Throws(IOException::class)
    fun write(data: CharSequence?, output: OutputStream) {
        if (data != null) output.write(data.toString().toByteArray())
    }

    @Throws(IOException::class)
    fun write(
        data: CharSequence?,
        output: OutputStream,
        encoding: String?
    ) {
        if (data != null) output.write(data.toString().toByteArray(charset(encoding!!)))
    }

    @Throws(IOException::class)
    fun write(inputStream: InputStream, outputStream: OutputStream) {
        var len: Int=0
        val buffer = ByteArray(4096)
        while (inputStream.read(buffer).also({ len = it }) != -1) outputStream.write(
            buffer,
            0,
            len
        )
    }

    @Throws(IOException::class)
    fun write(input: Reader?, output: OutputStream?) {
        val out: Writer = OutputStreamWriter(output)
        write(input!!, out)
        out.flush()
    }

    @Throws(IOException::class)
    fun write(input: InputStream?, output: Writer?) {
        val ins: Reader = InputStreamReader(input)
        write(ins, output!!)
    }

    @Throws(IOException::class)
    fun write(input: Reader?, output: OutputStream?, encoding: String?) {
        val out: Writer = OutputStreamWriter(output, encoding)
        write(input!!, out)
        out.flush()
    }

    @Throws(IOException::class)
    fun write(
        input: InputStream?,
        output: OutputStream?,
        encoding: String?
    ) {
        val `in`: Reader = InputStreamReader(input, encoding)
        write(`in`, output)
    }

    @Throws(IOException::class)
    fun write(input: InputStream?, output: Writer?, encoding: String?) {
        val ins: Reader = InputStreamReader(input, encoding)
        write(ins, output!!)
    }

    @Throws(IOException::class)
    fun write(input: Reader, output: Writer) {
        var len: Int=0
        val buffer = CharArray(4096)
        while (-1 != input.read(buffer).also({ len = it })) output.write(buffer, 0, len)
    }

    @Throws(IOException::class)
    fun contentEquals(input1: InputStream, input2: InputStream): Boolean {
        var input1: InputStream = input1
        var input2: InputStream = input2
        input1 = toBufferedInputStream(input1)
        input2 = toBufferedInputStream(input2)
        var ch: Int = input1.read()
        while (-1 != ch) {
            val ch2: Int = input2.read()
            if (ch != ch2) {
                return false
            }
            ch = input1.read()
        }
        val ch2: Int = input2.read()
        return ch2 == -1
    }

    @Throws(IOException::class)
    fun contentEquals(input1: Reader, input2: Reader): Boolean {
        var input1: Reader = input1
        var input2: Reader = input2
        input1 = toBufferedReader(input1)
        input2 = toBufferedReader(input2)
        var ch: Int = input1.read()
        while (-1 != ch) {
            val ch2: Int = input2.read()
            if (ch != ch2) {
                return false
            }
            ch = input1.read()
        }
        val ch2: Int = input2.read()
        return ch2 == -1
    }

    @Throws(IOException::class)
    fun contentEqualsIgnoreEOL(input1: Reader, input2: Reader): Boolean {
        val br1: BufferedReader = toBufferedReader(input1)
        val br2: BufferedReader = toBufferedReader(input2)
        var line1: String = br1.readLine()
        var line2: String = br2.readLine()
        while (line1 != null && line2 != null && line1 == line2) {
            line1 = br1.readLine()
            line2 = br2.readLine()
        }
        return line1 != null && (line2 == null || line1 == line2)
    }

    /**
     * Access to a directory available size.
     *
     * @param path path.
     * @return space size.
     */
    fun getDirSize(path: String?): Long {
        val stat: StatFs
        stat = try {
            StatFs(path)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
        return if (Build.VERSION.SDK_INT >= 18) getStatFsSize(
            stat,
            "getBlockSizeLong",
            "getAvailableBlocksLong"
        ) else getStatFsSize(stat, "getBlockSize", "getAvailableBlocks")
    }

    private fun getStatFsSize(
        statFs: StatFs,
        blockSizeMethod: String,
        availableBlocksMethod: String
    ): Long {
        try {
            val getBlockSizeMethod: Method = statFs.javaClass.getMethod(blockSizeMethod)
            getBlockSizeMethod.isAccessible = true
            val getAvailableBlocksMethod: Method = statFs.javaClass.getMethod(availableBlocksMethod)
            getAvailableBlocksMethod.isAccessible = true
            val blockSize = getBlockSizeMethod.invoke(statFs) as Long
            val availableBlocks =
                getAvailableBlocksMethod.invoke(statFs) as Long
            return blockSize * availableBlocks
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * If the folder can be written.
     *
     * @param path path.
     * @return True: success, or false: failure.
     */
    fun canWrite(path: String?): Boolean {
        return File(path).canWrite()
    }

    /**
     * If the folder can be read.
     *
     * @param path path.
     * @return True: success, or false: failure.
     */
    fun canRead(path: String?): Boolean {
        return File(path).canRead()
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param folderPath folder path.
     * @return True: success, or false: failure.
     */
    fun createFolder(folderPath: String?): Boolean {
        if (!TextUtils.isEmpty(folderPath)) {
            val folder = File(folderPath)
            return createFolder(folder)
        }
        return false
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param targetFolder folder path.
     * @return True: success, or false: failure.
     */
    fun createFolder(targetFolder: File): Boolean {
        if (targetFolder.exists()) {
            if (targetFolder.isDirectory) return true
            targetFolder.delete()
        }
        return targetFolder.mkdirs()
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param folderPath folder path.
     * @return True: success, or false: failure.
     */
    fun createNewFolder(folderPath: String?): Boolean {
        return delFileOrFolder(folderPath) && createFolder(folderPath)
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param targetFolder folder path.
     * @return True: success, or false: failure.
     */
    fun createNewFolder(targetFolder: File?): Boolean {
        return delFileOrFolder(targetFolder) && createFolder(targetFolder!!)
    }

    /**
     * Create a file, If the file exists is not created.
     *
     * @param filePath file path.
     * @return True: success, or false: failure.
     */
    fun createFile(filePath: String?): Boolean {
        if (!TextUtils.isEmpty(filePath)) {
            val file = File(filePath)
            return createFile(file)
        }
        return false
    }

    /**
     * Create a file, If the file exists is not created.
     *
     * @param targetFile file.
     * @return True: success, or false: failure.
     */
    fun createFile(targetFile: File): Boolean {
        if (targetFile.exists()) {
            if (targetFile.isFile()) return true
            delFileOrFolder(targetFile)
        }
        return try {
            targetFile.createNewFile()
        } catch (e: IOException) {
            false
        }
    }

    /**
     * Create a new file, if the file exists, delete and create again.
     *
     * @param filePath file path.
     * @return True: success, or false: failure.
     */
    fun createNewFile(filePath: String?): Boolean {
        if (!TextUtils.isEmpty(filePath)) {
            val file = File(filePath)
            return createNewFile(file)
        }
        return false
    }

    /**
     * Create a new file, if the file exists, delete and create again.
     *
     * @param targetFile file.
     * @return True: success, or false: failure.
     */
    fun createNewFile(targetFile: File): Boolean {
        if (targetFile.exists()) delFileOrFolder(targetFile)
        return try {
            targetFile.createNewFile()
        } catch (e: IOException) {
            false
        }
    }

    /**
     * Delete file or folder.
     *
     * @param path path.
     * @return is succeed.
     * @see .delFileOrFolder
     */
    fun delFileOrFolder(path: String?): Boolean {
        return if (TextUtils.isEmpty(path)) false else delFileOrFolder(File(path))
    }

    /**
     * Delete file or folder.
     *
     * @param file file.
     * @return is succeed.
     * @see .delFileOrFolder
     */
    fun delFileOrFolder(file: File?): Boolean {
        if (file == null || !file.exists()) {
            // do nothing
        } else if (file.isFile()) {
            file.delete()
        } else if (file.isDirectory()) {
            val files: Array<File> = file.listFiles()
            if (files != null) {
                for (sonFile in files) {
                    delFileOrFolder(sonFile)
                }
            }
            file.delete()
        }
        return true
    }
}