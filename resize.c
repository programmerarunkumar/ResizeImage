#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <MagickWand/MagickWand.h>

void open(void);
char* resize(char *,char *);
void close(void);

int main()
{

  char oldImage[100],newImage[100]; 
  open();
  resize(oldImage,newImage);
  close();
  return(0);

}

void open(){

  MagickWandGenesis();//Starting Magick Wand Environment
    
}

void close(){
	
  MagickWandTerminus();//Terminates the MagickWand environment

}

char* resize(char *oldImage,char *newImage){ 

  MagickWand *magick_wand;
  MagickBooleanType status;
  char *description;
  ExceptionType exceptiontype;

  magick_wand=NewMagickWand();//Initialize the MagickWand
  status=IsMagickWand(magick_wand);
  if(status==MagickFalse){
	description=MagickGetException(magick_wand,&exceptiontype);//Get the Exception of the MagickWand Methods
	strcat(description,"\n\tMethod : NewMagickWand()" );
	MagickClearException(magick_wand);
	return description;
  }
	

  status=MagickReadImage(magick_wand,oldImage);//Read the Image
  if(status==MagickFalse){
	description=MagickGetException(magick_wand,&exceptiontype);
	strcat(description,"\n\tMethod : MagickReadMethod()" );
	MagickClearException(magick_wand);
	return description;			
  }
 
  status=MagickResizeImage(magick_wand,1600,1600,LanczosFilter);//Resize the image
  if(status==MagickFalse){
	description=MagickGetException(magick_wand,&exceptiontype);
	strcat(description,"\n\tMethod : MagickResizeImage()" );
	MagickClearException(magick_wand);
	return description;
  }

  MagickWriteImage(magick_wand,newImage);//write the image

  DestroyMagickWand(magick_wand);//Destroy the MagickWand

  description="done";
  return description;

}	
