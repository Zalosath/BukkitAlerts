package com.jordna.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioManager
{

    private boolean playAudio = false;
    
    public void setPlayAudio(boolean b)
    {
	playAudio = b;
    }

    public void playPing()
    {
	if (!playAudio) return;

	try
	{
	    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("/ping.wav"));
	    Clip clip = AudioSystem.getClip();
	    clip.open(audioInputStream);
	    clip.start();
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

}
