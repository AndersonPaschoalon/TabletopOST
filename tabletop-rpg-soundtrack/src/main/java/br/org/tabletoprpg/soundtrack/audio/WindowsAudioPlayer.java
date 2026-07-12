package br.org.tabletoprpg.soundtrack.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WindowsAudioPlayer implements AudioPlayer  {
    private String fileName;
    private Clip localClip;


    public WindowsAudioPlayer(){

        this.fileName = null;
        this.localClip = null;
    }

    @Override
    public void play(String fileDir){

        this.stop();

        if(fileDir.equals(this.fileName) == false || this.fileName == null){
            this.fileName = fileDir;
            File file = new File(this.fileName);
    
            try(AudioInputStream audioStream = AudioSystem.getAudioInputStream(file)){
                
                this.localClip = AudioSystem.getClip();
                this.localClip.open(audioStream);
            
            }catch(FileNotFoundException e){
                System.err.print("Arquivo nao encontrado: " + e);
            }
            catch(IOException e){
                System.err.print("Erro IOException: " + e);
            }catch(UnsupportedAudioFileException e){
                System.err.print("Erro UnsupportedAudioFileException: " + e);
            }catch(LineUnavailableException e){
                System.err.print("Erro LineUnavailableException: " + e);
            }
        }

        this.localClip.start();



    }



    @Override
    public void stop(){
        if(isPlaying()){
            this.localClip.stop();
        }
    }

    @Override
    public boolean isPlaying(){
        if (this.fileName == null || this.localClip == null){
            return false;
        }
        return this.localClip.isActive();

    }
}
