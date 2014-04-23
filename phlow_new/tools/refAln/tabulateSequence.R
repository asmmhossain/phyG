#!/usr/bin/env Rscript

#setwd("~/Documents/Dengue_project/")

library(XML)

args <- commandArgs(TRUE)
fileName <- args[1]
outName <- args[2]

null.to.other <- function(x,y=NA){
  if(is.null(x)){
    return(y)
  }
  else{
    return(x)
  }
}

read.gbc <- function (fn, include.sequence = FALSE) 
{
  myxml <- xmlTreeParse(fn,useInternalNodes=TRUE)
  xmltop <- xmlRoot(myxml)
  numseq <- length(getNodeSet(myxml,"//INSDSeq"))
  locus <- rep("", numseq)
  accession <- rep("", numseq)
  seqlen <- rep(0, numseq)
  isolate <- rep(NA, numseq)
  isolationsource <- rep(NA, numseq)
  country <- rep(NA, numseq)
  collectiondate <- rep(NA, numseq)
  organism <- rep(NA, numseq)
  strain <- rep(NA, numseq)
  genotype <- rep(NA,numseq)
  host <- rep(NA, numseq)
  note <- rep("", numseq)
  if (include.sequence) {
    seq <- rep("", numseq)
  }
  for (i in 1:numseq) {
    print(paste("Processing", i, "of", numseq))
    locus[i] <- unlist(lapply(getNodeSet(xmltop, paste("//INSDSeq[",i,"]/INSDSeq_locus",sep="")), 
                              xmlValue))
    accession[i] <- unlist(lapply(getNodeSet(xmltop, 
                                             paste("//INSDSeq[",i,"]/INSDSeq_primary-accession",sep="")), xmlValue))
    seqlen[i] <- null.to.other(unlist(lapply(getNodeSet(xmltop, 
                                                        paste("//INSDSeq[",i,"]/INSDSeq_length",sep="")), xmlValue)), NA)
    isolate[i] <- null.to.other(unlist(lapply(getNodeSet(xmltop, 
                                                         paste("//INSDSeq[",i,"]/INSDSeq_feature-table/INSDFeature/INSDFeature_quals/INSDQualifier[INSDQualifier_name='isolate']/INSDQualifier_value",sep="")), 
                                              xmlValue)), NA)
    isolationsource[i] <- null.to.other(unlist(lapply(getNodeSet(xmltop, 
                                                                 paste("//INSDSeq[",i,"]/INSDSeq_feature-table/INSDFeature/INSDFeature_quals/INSDQualifier[INSDQualifier_name='isolation_source']/INSDQualifier_value",sep="")), 
                                                      xmlValue)), NA)
    country[i] <- null.to.other(unlist(lapply(getNodeSet(xmltop, 
                                                         paste("//INSDSeq[",i,"]/INSDSeq_feature-table/INSDFeature/INSDFeature_quals/INSDQualifier[INSDQualifier_name='country']/INSDQualifier_value",sep="")), 
                                              xmlValue)), NA)
    collectiondate[i] <- null.to.other(unlist(lapply(getNodeSet(xmltop, 
                                                                paste("//INSDSeq[",i,"]/INSDSeq_feature-table/INSDFeature/INSDFeature_quals/INSDQualifier[INSDQualifier_name='collection_date']/INSDQualifier_value",sep="")), 
                                                     xmlValue)), NA)
    organism[i] <- null.to.other(unlist(lapply(getNodeSet(xmltop, 
                                                          paste("//INSDSeq[",i,"]/INSDSeq_feature-table/INSDFeature/INSDFeature_quals/INSDQualifier[INSDQualifier_name='organism']/INSDQualifier_value",sep="")), 
                                               xmlValue)), NA)
    strain[i] <- null.to.other(unlist(lapply(getNodeSet(xmltop, 
                                                        paste("//INSDSeq[",i,"]/INSDSeq_feature-table/INSDFeature/INSDFeature_quals/INSDQualifier[INSDQualifier_name='strain']/INSDQualifier_value",sep="")), 
                                             xmlValue)), NA)
    genotype[i] <- null.to.other(unlist(lapply(getNodeSet(xmltop, 
                                                          paste("//INSDSeq[",i,"]/INSDSeq_feature-table/INSDFeature/INSDFeature_quals/INSDQualifier[INSDQualifier_name='genotype']/INSDQualifier_value",sep="")),
                                               xmlValue)), NA)
    host[i] <- null.to.other(unlist(lapply(getNodeSet(xmltop, 
                                                      paste("//INSDSeq[",i,"]/INSDSeq_feature-table/INSDFeature/INSDFeature_quals/INSDQualifier[INSDQualifier_name='host']/INSDQualifier_value",sep="")), 
                                           xmlValue)), NA)
    note[i] <- null.to.other(paste(unlist(lapply(getNodeSet(xmltop, 
                                                            paste("//INSDSeq[",i,"]/INSDSeq_feature-table/INSDFeature/INSDFeature_quals/INSDQualifier[INSDQualifier_name='note']/INSDQualifier_value",sep="")), 
                                                 xmlValue)),collapse="|"), "")
    if (include.sequence) {
      seq[i] <- unlist(lapply(getNodeSet(xmltop, paste("//INSDSeq[",i,"]/INSDSeq_sequence",sep="")), 
                              xmlValue))
    }
  }
  if (include.sequence) {
    return(data.frame(locus, accession, seqlen, isolate, 
                      isolationsource, country, collectiondate, organism, 
                      strain, genotype, host, note, seq))
  }
  else {
    return(data.frame(locus, accession, seqlen, isolate, 
                      isolationsource, country, collectiondate, organism, 
                      strain, genotype, host,note))
  }
}


#denv.tbl <- read.gbc("sequence.gbc.xml",include.sequence=TRUE) # commented by mukarram
denv.tbl <- read.gbc(fileName,include.sequence=TRUE) # added by mukarram
# check for duplicates
denv.tbl.nodup <- denv.tbl[!duplicated(denv.tbl),]
#write.table(denv.tbl.nodup,"denv.gbc.tbl.nodup.txt",col.names=T,row.names=F,sep="\t") # commented by M
#write.table(denv.tbl[,1:12],"denv.gbc.tbl.nodup.noseq.txt",col.names=T,row.names=F,sep="\t") # commented by M
write.table(denv.tbl[,1:12],"sequence.annotation.txt",col.names=T,row.names=F,sep="\t") # added by M

#denv.genomes.tbl <- read.gbc("sequence.gbc.xml",include.sequence=TRUE) # original genome.gbc.xml
denv.genomes.tbl <- read.gbc(fileName,include.sequence=TRUE) # added by M

gbc.to.fasta <- function(tbl,fn){
  numseq <- dim(tbl)[[1]]
  output <- c()
  for(i in 1:numseq){
    output <- c(output,paste(">",tbl$accession[i],sep=""))
    output <- c(output,as.character(tbl$seq[i]))
  }
  write(paste(output,collapse="\n"),file=fn)
}

#gbc.to.fasta(tbl=denv.tbl.nodup,fn="denv.gbc.nodup.fas") # commented by M
#gbc.to.fasta(tbl=denv.genomes.tbl,fn="denv.genomes.fas") # commented by M

gbc.to.fasta(tbl=denv.tbl.nodup,fn=outName)
