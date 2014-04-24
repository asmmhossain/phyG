"""
  clusterSeq.py
  This program calls cd-hit-est on combined.fasta to cluster sequences with more than 80% similarity.
  Sequences belonging to the same cluster are grouped in separate fasta formatted files.
  The representative sequences for each clusters are used to generate a local blastdb
  sequences in each clusters with > 99% similarity are then removed to reduce number of sequences
"""

import sys,optparse,os,subprocess,shutil
from Bio import SeqIO

class clusterSeq:
  """
  """
#********************************************
  def __init__(self,opts=None):
    self.opts = opts
    self.err = open(self.opts.log,'w')

#********************************************
  def run(self):

    file_path = os.path.dirname(os.path.abspath(__file__)) 
    dir_path = file_path[:file_path.rfind("tools")]

    cl = []
    cl.append('%sdependencies/cd-hit-v4.5.4-2011-03-07/cd-hit-est -c 0.8 -n 5 -i %s -o temp' % (dir_path,self.opts.input))

    process = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
    rval = process.wait()

#********************************************
  def makeFile(self):
    seqs = list(SeqIO.parse('combined.fasta','fasta'))

    lines = [line.strip('\n') for line in open('temp.clstr','r')]
    #print(len(seqs))
    #print(len(lines))

    flag = 0
    temp = []
    cls = 0

    ids = []

    for seq in seqs:
      ids.append(seq.id)


    for line in lines:
      if 'Cluster' in line:
        if flag == 0:
          flag = 1
        else:
          fname = 'cluster_' + `cls` + '.fasta'
          #print(fname)
          #print(len(temp))
          cls = cls + 1
          SeqIO.write(temp,fname,'fasta')
          temp = []
      else:
        name = line.split()[2].replace('>','').replace('.','')
        #print(name)
        sid = ids.index(name)
        temp.append(seqs[sid])

    fname = 'cluster_' + `cls` + '.fasta'
    #print(fname)
    #print(len(temp))
    SeqIO.write(temp,fname,'fasta')
    return cls

#**********************************************
  def removeRedundant(self,nc=-1):
    file_path = os.path.dirname(os.path.abspath(__file__)) 
    dir_path = file_path[:file_path.rfind("tools")]

    for i in xrange(nc):
      cl = []
      cl.append('%sdependencies/cd-hit-v4.5.4-2011-03-07/cd-hit-est -c 0.99 -n 10 -i cluster_%d.fasta -o reduced_cluster_%d' % (dir_path,i,i))
      #print(cl)

      process = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
      rval = process.wait()            

#**********************************************      
if __name__ == "__main__":
  op = optparse.OptionParser()
  op.add_option('-i','--input',default=None,help='Input sequence file')
  op.add_option('-l','--log',default='log',help='Log file')

  opts, args = op.parse_args()
  assert opts.input <> None

  c = clusterSeq(opts)
  #c.run() 
  nc = c.makeFile()
  ##print(nc)
  #nc = 19
  c.removeRedundant(nc+1)

