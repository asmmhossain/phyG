"""
  fragsAlign.py
  This program will align one fragments at a time to the respective aligned cluster using mafft's addfragment
  Gaps in the alignment before and after aligning the fragment will be counted and reported in a flat file
"""

from Bio import SeqIO
from Bio import AlignIO

import os,sys,subprocess,shutil,optparse

class fragsAlign:
  """
  """

  def __init__(self,opts=None):
    self.opts = opts
    self.err = open(self.opts.log,'w')
    file_path = os.path.dirname(os.path.abspath(__file__)) 
    dir_path = file_path[:file_path.rfind("tools")]


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

        outname = 'clusters/gaps_' + `i` + '.txt'
        fh = open(outname,'w')
        st = ""

        aname = 'clusters/cluster_' + `i` + '.aln'
        aln = AlignIO.read(aname,'fasta')
        laln = len(aln)
        oGaps = 0
        for rec in aln:
          oGaps += rec.seq.count('-')

        for j in xrange(nfrags):
          print(j)
          SeqIO.write(frags[j],'clusters/ftemp.fasta','fasta')
          cl = []
          cl = ['%sdependencies/mafft-7.027-without-extensions/core/mafft --auto --quiet --addfragments clusters/ftemp.fasta %s > clusters/ftemp.aln' % (dir_path,aname)]

          process1 = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
          rval1 = process1.wait()

          naln = AlignIO.read('clusters/ftemp.aln','fasta')
          nGaps = 0
          for k in xrange(laln):
            nGaps += naln[k].seq.count('-')

          dif = nGaps - oGaps
          st += frags[j].id + '\t' + `dif` + '\n'
        
        fh.write(st)
        fh.close()

              
#********************************************

if __name__ == "__main__":
  op = optparse.OptionParser()
  #op.add_option('-i','--input',default='full.fasta',help='Input sequence file')
  #op.add_option('-c','--input',default=None,help='Input sequence file')
  op.add_option('-l','--log',default='fragsAlign.log',help='Log file')

  opts, args = op.parse_args()
  #assert opts.input <> None

  c = fragsAlign(opts)
  c.run() 

