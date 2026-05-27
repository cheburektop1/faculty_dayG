package ru.tbank.education.school.lesson1

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun zipDirectory(sourceDir: File, zipFile: File) {
    ZipOutputStream(FileOutputStream(zipFile)).use { zipOut ->
        addDirToZip(sourceDir, sourceDir, zipOut)
    }
}

private fun addDirToZip(
    rootDir: File,
    currentFile: File,
    zipOut: ZipOutputStream
) {
    if (currentFile.isDirectory) {
        currentFile.listFiles()?.forEach { file ->
            addDirToZip(rootDir, file, zipOut)
        }
    } else {
        val extension = currentFile.extension.lowercase()
        if (extension == "txt" || extension == "log") {

            val relativePath = rootDir.toPath()
                .relativize(currentFile.toPath())
                .toString()

            zipOut.putNextEntry(ZipEntry(relativePath))

            FileInputStream(currentFile).use { input ->
                input.copyTo(zipOut)
            }

            zipOut.closeEntry()
        }
    }
}