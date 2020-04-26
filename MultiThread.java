import java.io.*;
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.concurrent.TimeUnit;
import java.lang.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.sun.jna.Library;
import com.sun.jna.Native;

class ImageMagickException extends Exception {

    ImageMagickException(String message){
        super(message);
    }

}

public class MultiThread implements Runnable{

	public String OriginalImage;
	public String ResizedImage;
	public String ThreadName;
	public String result;
	public long StartTime,EndTime;
	public long size;
	private long TotalTime;
	public static LibraryMapping obj;
		
	
	MultiThread(String Name,String original,String resized){
		this.ThreadName=Name;
		this.OriginalImage=original;
		this.ResizedImage=resized;
	}
	
	public void run(){

		try{
	
			this.StartTime=System.currentTimeMillis();
			this.result=obj.resize(this.OriginalImage,this.ResizedImage);
			this.EndTime=System.currentTimeMillis();
			this.TotalTime=this.EndTime-this.StartTime;
			
			File file=new File(this.OriginalImage);
			this.size=file.length();
			if(this.result.equals("done")){
				System.out.println("Thread Number :"+this.ThreadName+" Image : "+this.OriginalImage
				+" Size : "+this.size+"Bytes"+" Total Time : "+TimeUnit.MILLISECONDS.toSeconds(TotalTime)%60
				+" Seconds to resize the image");
			}else{
				throw new ImageMagickException(this.result+"\tImage : "+this.OriginalImage);
			}
 
	        }catch(Exception e){
				e.printStackTrace();
		}
		
	}
	
	public static void processImage()throws IOException{

		BufferedReader imageInput=null,imageOutput=null;
		String originalImageValue=null;
		String resizedImageValue=null;
		try{

			imageInput=new BufferedReader( new FileReader("OriginalImagesList.txt") );
			imageOutput=new BufferedReader( new FileReader("ResizedImagesList.txt") );
			int i=1;
			while( (originalImageValue = imageInput.readLine()) != null && (resizedImageValue = imageOutput.readLine()) != null )
			{
				MultiThread threadObject=new MultiThread(String.valueOf(i),originalImageValue,resizedImageValue);
				Thread threads=new Thread(threadObject);
				threads.start();	
				i++;
			}
					
		}catch(FileNotFoundException e){ 
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			imageInput.close();
			imageOutput.close();
		}
	}
		
	public static void main(String arg[]){

		long ProcessStartTime,ProcessEndTime,ProcessTotalTime;
		ProcessStartTime=System.currentTimeMillis();
		System.out.println("Start Time : "+ProcessStartTime);

		obj=(LibraryMapping)Native.load("resize",LibraryMapping.class);
		//obj.open();	
		try{

			processImage();
			while(Thread.currentThread().activeCount()>1){

			}

		}catch(IOException e){
			e.printStackTrace();
		}
	
		if(Thread.currentThread().activeCount()==1){
			System.out.println("Active Thread Count : "+Thread.currentThread().activeCount()
			+" & Thread Name : "+Thread.currentThread().getName());
			//obj.close();
		}
		ProcessEndTime=System.currentTimeMillis();
		System.out.println("End Time : "+ProcessEndTime);
		ProcessTotalTime=ProcessEndTime-ProcessStartTime;
		System.out.println("Total Time : "+TimeUnit.MILLISECONDS.toMinutes(ProcessTotalTime)+" Minutes "
		+TimeUnit.MILLISECONDS.toSeconds(ProcessTotalTime)%60+" Seconds");

	}

}
