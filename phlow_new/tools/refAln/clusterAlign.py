"""
  clusterAlign.py
  This program will align clusters of sequences
  the coding region of each cluster of sequences will be extracted first by using longorf.sh
  Then these nucleotide sequences containing coding regions will be translated into codon sequences
  These codon sequences will then be aligned with fast alignment program MUSCLE
  Alignments will then be translated into nucleotide alignments using PAL2NAL
  These alignments will then be used as reference for the fragments to add 
"""

from Bio import SeqIO
import os,sys,optparse,shutil,subprocess


class clusterAlign:
  """
  """

  def __init__(self,opts=None):
    self.opts = opts

#******************************************

  def run(self):
    err = open(self.opts.log,'w')

    file_path = os.path.dirname(os.path.abspath(__file__)) 
    dir_path = file_path[:file_path.rfind("tools")]

    temp = [line.strip('\n') for line in open('control.txt','r')]

    nc = int(temp[0].split()[3]) + 1
    #nc = 1
    #print(`nc`)
    for i in xrange(nc):
      cl = []
      cl.append('%stools/refAln/longorf.sh clusters/reduced_cluster_%d.fasta > clusters/cluster_%d_coding.fasta' % (dir_path,i,i))

      process1 = subprocess.Popen(''.join(cl), shell=True, stderr=err, stdout=err)
      rval1 = process1.wait()

      name = 'clusters/cluster_' + `i` + '_coding.fasta'      
      lines = [line.strip('\n') for line in open(name,'r')]
      fh = open('test.txt','w')
      for line in lines:
        if '>' in line:
          line = line[:line.rfind('_')]

        fh.write("%s\n" % line)
          
      fh.close()      
      shutil.move('test.txt',name)
      
      seqs = list(SeqIO.parse(name,'fasta'))
      for rec in seqs:
        rec.seq = rec.seq.translate(table=1)

      aname = 'clusters/cluster_' + `i` + '_aa.fasta'
      outname = 'clusters/cluster_' + `i` + '_aa.muscle'
      SeqIO.write(seqs,aname,'fasta')
      
      cl = ['%sdependencies/muscle3.8.31_i86linux32 -in %s -out %s' % (dir_path,aname,outname)]
        
      process = subprocess.Popen(' '.join(cl), shell=True, stderr=err, stdout=err)
      rval = process.wait()

      cl = []

      alnout = 'clusters/cluster_' + `i` + '.aln'

      tlf = open(alnout,'w')

      cl.append('perl %sdependencies/pal2nal.v14/pal2nal.pl %s %s' % (dir_path,outname,name))
      cl.append(' -output fasta -codontable 1')

      process = subprocess.Popen(' '.join(cl), shell=True, stderr=err, stdout=tlf)

      rval = process.wait()

#********************************************



if __name__ == "__main__":
  op = optparse.OptionParser()
  #op.add_option('-i','--input',default='full.fasta',help='Input sequence file')
  #op.add_option('-c','--input',default=None,help='Input sequence file')
  op.add_option('-l','--log',default='clusterAlign.log',help='Log file')

  opts, args = op.parse_args()
  #assert opts.input <> None

  c = clusterAlign(opts)
  c.run() 

