#!/bin/bash

#if [ $3="fftns2" ];
#then
#	mafft --retree 2 --maxiterate 1000 --quiet $1 > $2

#elif [ $3="linsi" ];
#then
#	linsi --quiet $1 > $2 
	
#elif [ $3="ginsi" ];
#then
#	ginsi --quiet $1 > $2	

#elif [ $3="einsi" ];
#then
#	einsi --quiet $1 > $2
	
#elif [ $3="fftnsi" ];
#then
#	mafft --retree 2 --maxiterate 2 --quiet $1 > $2
	
#elif [ $3="fftns" ];
#then
#	fftns --quiet $1 > $2

#elif [ $3="nwnsi" ];
#then
#	nwnsi --quiet $1 > $2

#elif [ $3="nwns" ];
#then
#	nwns --quiet $1 > $2
	
#elif [ $3 -eq "profile" ];
#then
		
#fi

if [ $# = "2" ];
then
	mafft --auto --quiet $1 > $2
elif [ $# = "4" ];
then
	mafft --auto --quiet $1 $2 $3 > $4
fi 
	

