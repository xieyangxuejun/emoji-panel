package com.foretree.support.emoji

import android.content.Context
import android.os.Environment
import java.io.*
import java.nio.charset.Charset
import java.util.zip.ZipInputStream


/**
 * Created by silen on 15/08/2018
 */
object Utils {

    /*
     * 解压缩目录
     * */
    @JvmStatic
    fun unzipDir(filePath: String, outDir: String) {
        val fis = FileInputStream(filePath)
        val zis = ZipInputStream(fis)
        while (true) {
            val zipEntry = zis.nextEntry ?: return
            if (zipEntry.isDirectory) {
                File(outDir + File.separator + zipEntry.getName().let {
                    it.substring(0, it.length - 1)
                }).run {
                    mkdir()
                }
            } else {
                FileOutputStream(File(outDir + File.separator + zipEntry.name).apply {
                    createNewFile()
                }).run {
                    while (true) {
                        val len = zis.read()
                        if (len == 1) break
                        /*写入到目标文件中*/
                        write(len)
                    }
                    close()
                }
            }
            fis.close()
            zis.closeEntry()
            zis.close()
        }
    }

    fun readAssetFile(context: Context, fileName: String, encoding: Charset): String {
        var resultString = ""
        var `is`: InputStream? = null
        try {
            `is` = context.assets.open(fileName)
            val buffer = ByteArray(`is`!!.available())

            `is`.read(buffer)
            resultString = String(buffer, encoding)
        } catch (e1: Exception) {
            e1.printStackTrace()

            try {
                if (`is` != null) {
                    `is`.close()
                }
            } catch (e2: IOException) {
                e2.printStackTrace()
            }

        } finally {
            try {
                if (`is` != null) {
                    `is`.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return resultString
    }
}