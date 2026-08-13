package com.example.voiceclone.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

object WavRecorder {
    /**
     * Records audio from the microphone for a fixed duration and writes a WAV file.
     * - file: destination File (will be overwritten)
     * - durationMs: record length in milliseconds
     * - sampleRate: e.g., 16000 or 22050
     *
     * This is a simple helper; call from a coroutine scope to avoid blocking UI.
     */
    suspend fun recordToFile(file: File, durationMs: Long, sampleRate: Int = 16000) {
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val minBuffer = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        val bufferSize = if (minBuffer == AudioRecord.ERROR || minBuffer == AudioRecord.ERROR_BAD_VALUE) 2048 else minBuffer
        val audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize)
        val pcmBuffer = ShortArray(bufferSize / 2)
        val fos = FileOutputStream(file)
        // placeholder WAV header
        fos.write(ByteArray(44))
        audioRecord.startRecording()
        val start = System.currentTimeMillis()
        try {
            while (System.currentTimeMillis() - start < durationMs) {
                val read = audioRecord.read(pcmBuffer, 0, pcmBuffer.size)
                if (read > 0) {
                    val byteBuf = ByteBuffer.allocate(read * 2).order(ByteOrder.LITTLE_ENDIAN)
                    for (i in 0 until read) byteBuf.putShort(pcmBuffer[i])
                    fos.write(byteBuf.array())
                }
            }
        } finally {
            audioRecord.stop()
            audioRecord.release()
            fos.close()
        }
        writeWavHeader(file, sampleRate, 1, 16)
    }

    private fun writeWavHeader(wavFile: File, sampleRate: Int, channels: Int, bitsPerSample: Int) {
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val dataSize = (wavFile.length() - 44).toInt()
        val header = ByteArray(44)
        // RIFF
        header[0] = 'R'.code.toByte(); header[1] = 'I'.code.toByte(); header[2] = 'F'.code.toByte(); header[3] = 'F'.code.toByte()
        val totalDataLen = dataSize + 36
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = ((totalDataLen shr 8) and 0xff).toByte()
        header[6] = ((totalDataLen shr 16) and 0xff).toByte()
        header[7] = ((totalDataLen shr 24) and 0xff).toByte()
        header[8] = 'W'.code.toByte(); header[9] = 'A'.code.toByte(); header[10] = 'V'.code.toByte(); header[11] = 'E'.code.toByte()
        // fmt chunk
        header[12] = 'f'.code.toByte(); header[13] = 'm'.code.toByte(); header[14] = 't'.code.toByte(); header[15] = ' '.code.toByte()
        header[16] = 16; header[17] = 0; header[18] = 0; header[19] = 0
        header[20] = 1; header[21] = 0 // PCM
        header[22] = channels.toByte(); header[23] = 0
        header[24] = (sampleRate and 0xff).toByte()
        header[25] = ((sampleRate shr 8) and 0xff).toByte()
        header[26] = ((sampleRate shr 16) and 0xff).toByte()
        header[27] = ((sampleRate shr 24) and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = ((byteRate shr 8) and 0xff).toByte()
        header[30] = ((byteRate shr 16) and 0xff).toByte()
        header[31] = ((byteRate shr 24) and 0xff).toByte()
        header[32] = ((channels * bitsPerSample / 8).toByte())
        header[33] = 0
        header[34] = bitsPerSample.toByte()
        header[35] = 0
        header[36] = 'd'.code.toByte(); header[37] = 'a'.code.toByte(); header[38] = 't'.code.toByte(); header[39] = 'a'.code.toByte()
        header[40] = (dataSize and 0xff).toByte()
        header[41] = ((dataSize shr 8) and 0xff).toByte()
        header[42] = ((dataSize shr 16) and 0xff).toByte()
        header[43] = ((dataSize shr 24) and 0xff).toByte()
        val raf = wavFile.outputStream()
        raf.channel.position(0)
        raf.write(header)
        raf.close()
    }
}
