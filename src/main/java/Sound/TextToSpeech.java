package Sound;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.print.DocFlavor;
import java.util.Scanner;

public class TextToSpeech {

    public static void TextSpeech(String target) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        //System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        //Voice voice = VoiceManager.getInstance().getVoice("alan");
        //kevin or alan
        // Voice []voiceList = VoiceManager.getInstance().getVoices();
        // for(int i = 0; i < voiceList.length; ++i) {
        //     System.out.println("# Voice: + " + voiceList[i].getName());
        // }

        if(voice != null) {
            voice.allocate();

            Properties properties = new Properties();
            FileInputStream fileInputStream = null;

            final String CONFIG_FILE_PATH =  (System.getProperty("user.dir") + "/src/main/java/GUIVersion/resources/config.properties");
            try {
                fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
                properties.load(fileInputStream);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            float voiceRate = Float.parseFloat(properties.getProperty("voiceRate"));
            float voicePitch = Float.parseFloat(properties.getProperty("voicePitch"));
            float voiceVolume = Float.parseFloat(properties.getProperty("voiceVolume"));


            voice.setRate(voiceRate);
            voice.setPitch(voicePitch);
            voice.setVolume(voiceVolume);

            System.out.println("Voice Rate: " + voice.getRate());
            System.out.println("Voice Pitch: " + voice.getPitch());
            System.out.println("Voice Volume: " + voice.getVolume());

            //AudioPlayer audioPlayer = new SingleFileAudioPlayer("", javax.sound)

            boolean status = voice.speak(target);
            System.out.println("Status: " + status);
            voice.deallocate();
        } else {
            System.out.println("Error in getting Voices");
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s1 = sc.nextLine();
        TextToSpeech.TextSpeech(s1);
    }
}
