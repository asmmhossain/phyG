#!/bin/bash

if [ $# -eq "2" ];
then
	muscle -in $1 -out $2 -quiet 

elif [ $# -eq "3" ];
then
	muscle -in $1 -out $2 -quiet $3

elif [ $# -eq "4" ];
then
	muscle -in $1 -out $2 -quiet $3 $4
fi

