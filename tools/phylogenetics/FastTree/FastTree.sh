#!/bin/bash

if [ $# = "2" ];
then
	FastTree -quiet -nopr $1 > $2
elif [ $# = "3" ];
then
	FastTree -quiet -nopr $1 $2 > $3
elif [ $# = "4" ];
then
	FastTree -quiet -nopr $1 $2 $3 > $4
fi


