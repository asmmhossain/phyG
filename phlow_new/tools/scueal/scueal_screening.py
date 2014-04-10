"""
scueal_screening.py
This program calls SCUEAL to automatically assign genotypes to sequences
MPIScreenFASTA.bf is called with user defined reference alignment

"""


import sys,optparse,os,subprocess,shutil


class scueal:
  """
  """
  def __init__(self,opts=None):
    self.opts = opts
  
 
#  def copyFile(src, dest):
 #   try:
  #    shutil.copy(src, dest)
   #   # eg. src and dest are the same file
    #    except shutil.Error as e:
     #     print('Error: %s' % e)
      ## eg. source or destination doesn't exist
       # except IOError as e:
        #  print('Error: %s' % e.strerror)

  def run(self):
    err = open(self.opts.log,'w')
    cl = []
    mk = []
    file_path = os.path.dirname(os.path.abspath(__file__)) 
    dir_path = file_path[:file_path.rfind("tools")]

    if self.opts.refAlnName == "other.nex":
      mk.append('(echo %s; echo %s; echo 2; echo 2; echo %sdependencies/SCUEAL-galaxy/data/other.nex) | mpirun -np 6 %sdependencies/hyphy-master/HYPHYMPI BASEPATH=%sdependencies/hyphy-master/src/ USEPATH=/dev/null %sdependencies/SCUEAL-galaxy/TopLevel/MakeReferenceAlignment.bf' % (self.opts.refType,self.opts.source,dir_path,dir_path,dir_path,dir_path))

      process1 = subprocess.Popen(' '.join(mk), shell=True, stderr=err, stdout=err)

      rval1 = process1.wait()
    

    cl.append('(echo 128; echo 50; echo 10; echo 100; echo %s; echo %s; echo %s; echo %s; echo %s) | mpirun -np 6 %sdependencies/hyphy-master/HYPHYMPI BASEPATH=%sdependencies/hyphy-master/src/ USEPATH=/dev/null %sdependencies/SCUEAL-galaxy/TopLevel/MPIScreenFASTA.bf' % (self.opts.refAlnName,self.opts.alnType,self.opts.input,self.opts.output,self.opts.outType,dir_path,dir_path,dir_path))

    #print(cl)
    process = subprocess.Popen(' '.join(cl), shell=True, stderr=err, stdout=err)

    rval = process.wait()

    
    err.close()

if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input sequence file for screening')
    op.add_option('-o', '--output', default=None,help='Output file with assigned types')
    op.add_option('-r','--refAlnName', default='pol2011.nex', help="Reference alignment file")  
    op.add_option('-t','--alnType', default='1', help="Alignment type (in-frame codon, nucleotide, codon-direct)")  
    op.add_option('-l','--log', default=None, help="Process log")  
    op.add_option('-g','--outType', default=None, help="Output format (summary, summary+detail") 
    op.add_option('-q','--refType', default=None, help="Reference alignment type for MakeReferenceAlignment.bf")
    op.add_option('-s','--source', default=None, help="Source alignment for MakeReferenceAlignment.bf")  
  
 
    
       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = scueal(opts)
    c.run()
    

