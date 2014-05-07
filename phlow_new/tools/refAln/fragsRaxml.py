"""
  fragsRaxml.py
  This program will align one fragments at a time to the respective aligned cluster using mafft's addfragment
  Trees will be created using RAxML program with GTRGAMMAI model and the branch length for the fragment will be
  reported to a text file
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
    #if not os.path.exists('RAxML'):
      #os.makedirs('RAxML')
    

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

        outname = 'branch_length_raxml_' + `i` + '.txt'
        fh = open(outname,'w')
        st = ""

        aname = 'clusters/cluster_' + `i` + '.aln'
        aln = AlignIO.read(aname,'fasta')
        laln = len(aln)

        for j in xrange(nfrags):
          SeqIO.write(frags[j],'clusters/ftemp.fasta','fasta')
          cl = []
          cl = ['%sdependencies/mafft-7.027-without-extensions/core/mafft --auto --quiet --addfragments clusters/ftemp.fasta %s > clusters/ftemp_raxml.aln.' % (dir_path,aname)]

          process1 = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
          rval1 = process1.wait()

          if os.path.exists('RAxML_bestTree.raxml'):
            os.remove('RAxML_bestTree.raxml')

          if os.path.exists('RAxML_info.raxml'):
            os.remove('RAxML_info.raxml')

          if os.path.exists('RAxML_log.raxml'):
            os.remove('RAxML_log.raxml')

          if os.path.exists('RAxML_parsimonyTree.raxml'):
            os.remove('RAxML_parsimonyTree.raxml')

          if os.path.exists('RAxML_result.raxml'):
            os.remove('RAxML_result.raxml')

          cl = []
          cl.append('raxmlHPC-SSE3 -s clusters/ftemp_raxml.aln -n raxml -m GTRGAMMAI -p 123456789')

          process1 = subprocess.Popen(''.join(cl), shell=True, stderr=self.err, stdout=self.err)
          rval1 = process1.wait()

          tree = [line.strip('\n') for line in open('RAxML_bestTree.raxml','r')]
          words = tree[0].replace('(','').replace(')','').replace(';','').split(',')
          for rec in words:
            ws = rec.split(':')
            if ws[0] == 'AY618990':
              print(ws[1])
              st += frags[j].id + '\t' + ws[1] + '\n'

        
        fh.write(st)
        fh.close()

              
#********************************************

if __name__ == "__main__":
  op = optparse.OptionParser()
  #op.add_option('-i','--input',default='full.fasta',help='Input sequence file')
  #op.add_option('-c','--input',default=None,help='Input sequence file')
  op.add_option('-l','--log',default='fragsRaxml.log',help='Log file')

  opts, args = op.parse_args()
  #assert opts.input <> None

  c = fragsAlign(opts)
  c.run() 

