package Sound;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

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
