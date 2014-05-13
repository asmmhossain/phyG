"""
  fragsStat.py
  This program adds one fragment at a time to the alignment of the long sequences
  It calculates number of gaps before and after the fragment is added to the alignment
  It places the fragment to an existing tree created from the original alignment and 
    calculates the distance of the branch
  MACSE is used to add the fragment to the alignment to generate a numer of statistics.
    Statistics include: # of (FS, DEl, INS)  
"""

from Bio import SeqIO
from Bio import AlignIO

import os,sys,subprocess,shutil,optparse

class fragsStat:
  """
  """

  def __init__(self,opts=None):
    self.opts = opts
    self.err = open(self.opts.log,'w')

  def run(self):

    file_path = os.path.dirname(os.path.abspath(__file__)) 
    dir_path = file_path[:file_path.rfind("tools")]

    temp = [line.strip('\n') for line in open('control.txt','r')]

    nc = int(temp[0].split()[3]) + 1
    
    for i in xrange(nc):
      print(i)
      fname = 'fragments/frag_' + `i` + '.fasta'

      if os.path.exists(fname):
        frags = list(SeqIO.parse(fname,'fasta'))
        nfrags = len(frags)

        outname = 'clusters/stats_' + `i` + '.txt'
        fh = open(outname,'w')
        st = ""


        aname = 'clusters/cluster_' + `i` + '.aln'
        atree = 'clusters/cluster_' + `i` + '.tree'
        cl = []
        cl = ('%sdependencies/FastTree/FastTree -nt %s > %s' % (dir_path,aname,atree))

        process1 = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
        rval1 = process1.wait()

        aln = AlignIO.read(aname,'fasta')
        laln = len(aln)
        oGaps = 0
        blen = 0.0
        for rec in aln:
          oGaps += rec.seq.count('-')

        for j in xrange(nfrags):
          #print(j)
          SeqIO.write(frags[j],'clusters/ftemp.fasta','fasta')

#*************************************mafft to add fragmemts to alignment and calculate gaps**********
          cl = []
          cl = ['%sdependencies/mafft-7.027-without-extensions/core/mafft --auto --quiet --addfragments clusters/ftemp.fasta %s > clusters/ftemp.aln' % (dir_path,aname)]

          process1 = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
          rval1 = process1.wait()

          naln = AlignIO.read('clusters/ftemp.aln','fasta')
          nGaps = 0
          for k in xrange(laln):
            nGaps += naln[k].seq.count('-')

          dif = nGaps - oGaps

#*********************************RAxML to use the mafft alignment with -f v option to find the branch length of the placed fragment

          if os.path.exists('RAxML_info.raxml'):
            os.remove('RAxML_info.raxml')

          if os.path.exists('RAxML_labelledTree.raxml'):
            os.remove('RAxML_labelledTree.raxml')

          if os.path.exists('RAxML_originalLabelledTree.raxml'):
            os.remove('RAxML_originalLabelledTree.raxml')

          if os.path.exists('RAxML_portableTree.raxml.jplace'):
            os.remove('RAxML_portableTree.raxml.jplace')

          if os.path.exists('RAxML_classification.raxml'):
            os.remove('RAxML_classification.raxml')

          if os.path.exists('RAxML_classificationLikelihoodWeights.raxml'):
            os.remove('RAxML_classificationLikelihoodWeights.raxml')

          if os.path.exists('RAxML_entropy.raxml'):
            os.remove('RAxML_entropy.raxml')

          cl = []
          cl.append('raxmlHPC -f v -s clusters/ftemp.aln -n raxml -m GTRGAMMAI -t %s' % atree)

          process1 = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
          rval1 = process1.wait()

          tree = [line.strip('\n') for line in open('RAxML_labelledTree.raxml','r')]
          words = tree[0].replace('(','').replace(')','').replace(';','').split(',')
          for rec in words:
            if 'QUERY' in rec:
              blen = float(rec.split(':')[1])
              #print(ws[1])
              #st += frags[j].id + '\t' + `dif` + '\t' + `blen` + '\n'
              #print('%s\t%d\t%f\n' % (frags[j].id,dif,blen))

          #st += frags[j].id + '\t' + `dif` + '\n'

#*********************************************MACSE to genarate stats of FS, INS and DEL for the fragment using enrichAlignnent

          cl = []
          cl.append('java -jar -Xmx1G %sdependencies/macse_v1.01b.jar -prog enrichAlignment -align %s -seq clusters/ftemp.fasta -out_NT macse_test -seqToAdd_logFile stats.csv' % (dir_path,aname))

          process1 = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
          rval1 = process1.wait()

          stats = [line.strip('\n') for line in open('stats.csv','r')]
          words = stats[0].split(';')

          fs = int(words[2].split('=')[1])
          dels =  int(words[4].split('=')[1])
          stop = int(words[3].split('=')[1])
          ins_int = int(words[5].split('=')[1])
          ins_total = int(words[6].split('=')[1])
          
          #print('%s\t%d\t%f\t%d\t%d\t%d\n' % (frags[j].id,dif,blen,fs,dels,ins_total))
          st += frags[j].id + '\t' + `dif` + '\t' + `blen` + '\t' + `fs` + '\t' + `stop`
          st += '\t' + `dels` + '\t' + `ins_int` + '\t' + `ins_total` + '\n' 

        fh.write(st)
        fh.close()



              
#********************************************

if __name__ == "__main__":
  op = optparse.OptionParser()
  #op.add_option('-i','--input',default='full.fasta',help='Input sequence file')
  #op.add_option('-c','--input',default=None,help='Input sequence file')
  op.add_option('-l','--log',default='fragStat.log',help='Log file')

  opts, args = op.parse_args()
  #assert opts.input <> None

  c = fragStat(opts)
  c.run() 

