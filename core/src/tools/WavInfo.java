package tools;

import game.Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class WavInfo {
	byte[] allBytes=new byte[50];
	short[] samples;
	public int channels;
	public int sampleRate;
	public int bytesPerSample;
	public float wavLength;
	public Texture waveForm;
	public String path;
	float seconds=0;
	public float delay=0;
	public byte[] bonkBytes;
	public ArrayList<Tag> tags = new ArrayList<WavInfo.Tag>();
	private Music audio;
	private WavInfo(String path, boolean createBonks){
		this.path=path;
		
		FileHandle handle= Gdx.files.internal(path+".mp3");
		Main.manager.load(handle.path(), Music.class);
		
		setupTags();
		if(createBonks){
			FileHandle wavHandle = Gdx.files.internal(path+".wav");
			allBytes = wavHandle.readBytes();
			channels=allBytes[22];
			sampleRate=twoBytesToShort(allBytes[24], allBytes[25]);
			bytesPerSample=allBytes[34]/8;
			wavLength=(allBytes.length-40)/(float)bytesPerSample/(float)sampleRate/(float)channels;
			setupSamples();
			makeBonk();
			System.out.println("making bonk");
		}
		FileHandle bonkHandle=Gdx.files.internal(path+".bonk");
		bonkBytes=bonkHandle.readBytes();
		Main.manager.finishLoading();
		audio=Main.manager.get(handle.path(), Music.class);
	
		audio.setLooping(false);
		
		
	}
	
	private void setupTags(){
		//tag stuff//
				FileHandle handle=Gdx.files.internal(path+".txt");
				System.out.println(handle);
				if(!handle.exists()){
					System.out.println("returning because no tags");
					return; //no tags//
				}
				char[] chars = new char[2000];
				try {
					handle.reader().read(chars);
				} catch (IOException e) {
					e.printStackTrace();
				}
				for(String tagString:new String(chars).split("\n")){
					tagString=tagString.trim();
					if(tagString.length()==0)continue;
					tags.add(new Tag(tagString));
				}
	}

	
	private static int resampleRate=30;
	private static float resampleDistance=.0005f;
	public static float bonkSamplesPerSecond=300;
	private void makeBonk() {
		float increment = 1/bonkSamplesPerSecond;
		byte[] bonks = new byte[(int) (bonkSamplesPerSecond*wavLength)+1];
		int bonkIndex=0;
		for(float i=0;i<wavLength-.1;i+=increment){
			bonks[bonkIndex]= getSample(i, resampleRate, resampleDistance);
			bonkIndex++;
		}
		
		String bonkPath = (Gdx.files.getLocalStoragePath()+path).replace('/', '\\');
		String folderPath=bonkPath.substring(0, bonkPath.lastIndexOf("\\"));
		File folderMaker = new File(folderPath);
		folderMaker.mkdirs();
		bonkPath+=".bonk";
		File file = new File(bonkPath);
		try {
			file.createNewFile();
			java.nio.file.Files.write(file.toPath(), bonks, new OpenOption[]{});
		} catch (IOException e) {
			System.out.println(file.getPath());
			e.printStackTrace();
		}
	}

	private byte getSample(float seconds, int samples, float resampleDistance){
		if(seconds<0||seconds>wavLength) return 0;
		int divider = (int) Math.pow(2, 8);
		byte max = (byte) Math.abs((this.samples[(int) (seconds*sampleRate)]/divider));
		for(int i=0;i<samples/2;i++){
			float newTime=seconds-i*resampleDistance;
			if(newTime>0&&newTime<wavLength){
				byte test = (byte) Math.abs((this.samples[(int) (newTime*sampleRate)]/divider));
				if(max<test)max=test;
			}
			newTime=seconds+i*resampleDistance;
			if(newTime>0&&newTime<wavLength){
				byte test = (byte) Math.abs((this.samples[(int) (newTime*sampleRate)]/divider));
				if(max<test)max=test;
			}

		}
		return max;
	}



	private void setupSamples(){
		samples = new short[allBytes.length/bytesPerSample];
		int sampleIndex=0;
		if(bytesPerSample!=2)System.out.println("Need to do something about non-2byte wavs");
		for(int i=41;i<allBytes.length-1;i+=bytesPerSample*channels){
			samples[sampleIndex]= (short) twoBytesToShort(allBytes[i+1], allBytes[i]);
			sampleIndex++;
		}
	}
	
	public boolean tagAt(float time){
		return getTagAt(time)!=null;
	}
	
	public Tag getTagAt(float time){
		for(Tag t:tags){
			if(time>t.start&&time<t.end){
				return t;
			}
		}
		return null;
	}

	public static int twoBytesToShort(int low, int high) {
		return  (int) (((high & 0xff) << 8) | (low & 0x00ff));
	}
	public String toString(){
		return "path: "+path+"\n"
				+ "Channels: "+channels+"\n"
				+ "Sample rate: "+sampleRate+"\n"
				+ "BytesPerSample "+bytesPerSample+"\n"
				+ "WavLength: "+wavLength;
	}
	
	public ArrayList<Tag> getOffsetTags(float offset){
		ArrayList<Tag> offsetTags = new ArrayList<WavInfo.Tag>();
		for(Tag t:tags){
			offsetTags.add(t.offset(offset));
		}
		return offsetTags;
	}
	
	public class Tag{
		public float start;
		public float end;
		public String specific;
		public String tag;
		public boolean used=false;
		public float offset=0;
		public float censored;
		public Tag(String file){

			String[] s = file.split(":");
			tag=s[0];
			specific=s[1];
			start=Float.parseFloat(s[2]);
			end=Float.parseFloat(s[3]);
		}
		public Tag(){}
		public String toString(){
			return path+"\n"
					+ tag+":"+specific+"\n"
					+ "From "+start+" to "+end;
		}
		public String getName(){
			return "\""+specific+"\"";
		}
		public boolean isCensored() {
			return censored>(end-start)/2f;
		}
		public void reset() {
			used=false;
			censored=0;
			offset=0;
		}
		public Tag copy() {
			Tag t= new Tag();
			t.start=start;
			t.end=end;
			t.specific=specific;
			t.tag=tag;
			t.offset=offset;
			return t;
		}
		public Tag offset(float amount){
			Tag result = copy();
			result.offset+=amount;
			result.start+=amount;
			result.end+=amount;
			return result;
		}
		public boolean isAt(float f) {
			return f>start&&f<end;
		}
	}


	public Music getAudio() {
		return audio;
	}


	public float getLength() {
		return (float)bonkBytes.length/(float)bonkSamplesPerSecond;
	}
	
	private static HashMap<String, WavInfo> infoMap = new HashMap<String, WavInfo>();
	public static WavInfo getInfo(String path){
		if(infoMap.get(path)==null){
			infoMap.put(path, new WavInfo(path, Main.createBonks));
		}
		return infoMap.get(path);
	}
}
