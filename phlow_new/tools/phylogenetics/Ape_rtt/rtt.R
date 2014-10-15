#!/usr/bin/env Rscript

options(warn=1)
library(ape)

args <- commandArgs(T)

if (length(args) < 4)
{
  print('USAGE: ./rtt.R treeFile outFile objective tolerance')
  quit()
}

t <- read.tree(args[1])

l <- length(t$tip.label)

dates <- c()

#print(l)

for (i in 1:l)
{
  tl <- t$tip.label[i]
  words <- strsplit(tl,'_')
  wl <- length(words[[1]])
  td <- words[[1]][wl]
  #print(td)
  dates[i] <- as.double(td)
  #print(dates[i])
}
#print(dates)


t1<- rtt(t,dates,objective=as.character(args[3]))

write.tree(t1,args[2])