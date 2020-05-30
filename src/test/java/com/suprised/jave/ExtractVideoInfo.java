package com.suprised.jave;

import java.io.File;
import java.util.Arrays;

public class ExtractVideoInfo {

    public static long getObtainVideoTime(String filePath) throws Exception {
        long ls = 0L;
        File source = new File(filePath);
        Encoder encoder = new Encoder();

        try {
            if (source.exists() && source.isFile()) {
                MultimediaInfo m = encoder.getInfo(source);
                System.out.println(m);
                ls = m.getDuration();
                // 时长
                // System.out.println(ls);
                // 视频编码
                System.out.println(m.getVideo().getDecoder());
                // 音频编码
                System.out.println(m.getAudio().getDecoder());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }

    public static long encoderVideo(String sourcePath, String taregetPath) throws Exception {
        long ls = 0L;
        File source = new File(sourcePath);
        File target = new File(taregetPath);
        Encoder encoder = new Encoder();

        try {
            if (source.exists() && source.isFile()) {
                EncodingAttributes attributes = new EncodingAttributes();
                attributes.setFormat("mov");
                VideoAttributes videoAttr = new VideoAttributes();
                videoAttr.setCodec("libx264");
                attributes.setVideoAttributes(videoAttr);
                encoder.encode(source, target, attributes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }

    public static void printFomartInfo() throws Exception {
        Encoder encoder = new Encoder();

        System.out.println("getVideoDecoders: " + Arrays.toString(encoder.getVideoDecoders()));
        System.out.println("getAudioDecoders: " + Arrays.toString(encoder.getAudioDecoders()));

        System.out.println("getSupportedDecodingFormats: " + Arrays.toString(encoder.getSupportedDecodingFormats()));
        System.out.println("getSupportedEncodingFormats: " + Arrays.toString(encoder.getSupportedEncodingFormats()));

        System.out.println("getVideoEncoders: " + Arrays.toString(encoder.getVideoEncoders()));
        System.out.println("getAudioEncoders: " + Arrays.toString(encoder.getAudioEncoders()));
    }

    public static void main(String[] args) throws Exception {
        ExtractVideoInfo.getObtainVideoTime("D:\\storage\\空调制冷.mp4");
        ExtractVideoInfo.getObtainVideoTime("D:\\storage\\544946.mp4");
        
        // System.out.println(System.getProperty("java.io.tmpdir"));
        printFomartInfo();
        // ExtractVideoInfo.encoderVideo("D:\\storage\\空调制冷.mp4", "D:\\storage\\encoder.mp4");

        // ExtractVideoInfo.encoderVideo("D:\\storage\\544946.mp4", "D:\\storage\\544946_encoder.mp4");
    }
}
