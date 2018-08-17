package com.foretree.support.emoji

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipInputStream


/**
 * Created by silen on 15/08/2018
 */
object FileUtils {

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
}