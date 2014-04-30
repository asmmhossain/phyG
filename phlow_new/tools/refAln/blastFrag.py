"""
  blastFrag.py
  This program creates a local database using makeblastdb from the full length sequences 
  each fragment is then searched against the local database to find best match
  a separate file called clusterList.txt contains the association of accession numbers with their respective clusters
  The fragments are grouped together in separate files based on the blast results: fragments matching with sequences from the same cluster are grouped together
"""

from Bio import SeqIO
import sys,optparse,os,subprocess,shutil

class blastFrag:
  """
  """

#*********************************************

  def __init__(self,opts=None):
    self.opts = opts
    self.err = open(self.opts.log,'w')
    if not os.path.exists('fragments'):
      os.makedirs('fragments')
#**********************************************

  def makeDB(self):

    cl = []

    file_path = os.path.dirname(os.path.abspath(__file__)) 
    dir_path = file_path[:file_path.rfind("tools")]

    cl.append('%sdependencies/ncbi-blast-2.2.29+-src/c++/ReleaseMT/bin/makeblastdb -in %s -input_type fasta -title local -dbtype nucl -out local.blastdb' % (dir_path,self.opts.dbSourceFile))

    process = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
    rval = process.wait()            

    

#*********************************************
  def makeQuery(self):
    seqs = list(SeqIO.parse(self.opts.input,'fasta')) 
    #print(len(seqs))
    frags = []
    for seq in seqs:
      sLen = len(seq.seq)
      nLen = seq.seq.count('N')
      if sLen is not 0:
        nRat = float(nLen)/sLen
        if nRat < float(self.opts.nPer):
          #print(seq.id)  
          frags.append(seq)

    SeqIO.write(frags,'query.fasta','fasta')

#***********************************************

  def runBlast(self):
    cl = []

    file_path = os.path.dirname(os.path.abspath(__file__)) 
    dir_path = file_path[:file_path.rfind("tools")]

    cl.append('%sdependencies/ncbi-blast-2.2.29+-src/c++/ReleaseMT/bin/blastn -query query.fasta -task megablast -db local.blastdb -max_target_seqs 1 -outfmt "10 qacc sacc" -out blast_results.txt -evalue 20' % (dir_path))

    process = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
    rval = process.wait()    

    cl = []
    cl.append('cat blast_results.txt | uniq > test.txt; mv test.txt blast_results.txt')

    process = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
    rval = process.wait()    
        

#*********************************************

  def makeFragCluster(self):
    lines = [line.strip('\n') for line in open('clusterList.txt','r')]
    acc = []
    cls = []
    for line in lines:
      words = line.split('\t')
      acc.append(words[0])
      cls.append(words[1])
 
    blines = [bline.strip('\n') for bline in open('blast_results.txt','r')]
    qacc = []
    sacc = []
    
    rf = [line.strip('\n') for line in open('control.txt','r')]
    mc = int(rf[0].split()[3])
    #mc = 0
    for bline in blines:
      words = bline.split(',')
      qacc.append(words[0])
      sacc.append(words[1])

    #print(len(sacc))

    acc_cls = [['0' for x in xrange(2)] for x in xrange(len(sacc))]
    #print(len(acc_cls))
    for i in xrange(len(sacc)):
      if sacc[i] in acc:
        acc_cls[i][0] = qacc[i]
        acc_cls[i][1] = cls[acc.index(sacc[i])]
        #if int(acc_cls[i][1]) > mc:
          #mc = int(acc_cls[i][1])
      #else:
        #print(sacc[i])

    #print(mc)

    seqs = list(SeqIO.parse(self.opts.input,'fasta'))
    ids = []

    for seq in seqs:
      ids.append(seq.id)

    for i in xrange(mc+1):
      temp = []
      for j in xrange(len(acc_cls)):
        if int(acc_cls[j][1]) == i:
          temp.append(seqs[ids.index(acc_cls[j][0])])
      if len(temp) > 0:
        name = 'frag_' + `i` + '.fasta'
        SeqIO.write(temp,name,'fasta')
        dest = 'fragments/' + name
        shutil.move(name,dest)
      #else:
        #print(i)

#**********************************************      
if __name__ == "__main__":
  op = optparse.OptionParser()
  op.add_option('-i','--input',default='frag.fasta',help='Fragment sequence file')
  op.add_option('-c','--clusterList',default='clusterList.txt',help='Cluster list file for full sequences')
  op.add_option('-l','--log',default='blastFrag.log',help='Log file')
  op.add_option('-n','--nPer',default='0.1',help='Percentage of Ns to remove fragment')
  op.add_option('-d','--dbSourceFile',default='full.fasta',help='Source file to make local database')
  op.add_option('-t','--title',default=None,help='Title of the local database')


  opts, args = op.parse_args()
  assert opts.input <> None

  c = blastFrag(opts)
  c.makeDB()
  c.makeQuery()
  c.runBlast()
  c.makeFragCluster()
