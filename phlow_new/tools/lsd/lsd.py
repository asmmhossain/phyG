"""
lsd.py
Galaxy wrapper for LSD
"""


import sys,optparse,os,subprocess,tempfile,shutil

class lsd:
    """
    """
    def __init__(self,opts=None):
        self.opts = opts
        #self.iname = 'infile_copy'
        #shutil.copy(self.opts.input,self.iname) 
            

    def run(self):
        tlf = open(self.opts.log,'w')
        cl = []
        file_path = os.path.dirname(os.path.abspath(__file__)) 
        dir_path = file_path[:file_path.rfind("tools")]
        
        if os.path.isfile(self.opts.output):
          os.remove(self.opts.output)

        cl.append('%sdependencies/lsd-0.1/src/lsd -i %s -d %s -o %s' % (dir_path,self.opts.input,self.opts.dates,self.opts.output))
        #cl.append(' -d %s' % self.opts.dates)
        #cl.append(' -o %s' % self.opts.output)

        #print(cl)
        
        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()

        #---writing the branch length, nexus and date files---#
        words = self.opts.output.split('.')
        fname = words[0] + '_newick_brl.txt'
        
        if os.path.isfile(fname):
          shutil.copy(fname,self.opts.brl)
        else:
          tlf.close()
          tlf = open(self.opts.log,'a')
          tlf.write('\nCould not write the tree file with branch length')
          
        fname = words[0] + '_newick_date.txt'        
        if os.path.isfile(fname):
          shutil.copy(fname,self.opts.newick)
        else:
          tlf.close()
          tlf = open(self.opts.log,'a')
          tlf.write('\nCould not write the tree file with date')

        fname = words[0] + '_nexus.txt'        
        if os.path.isfile(fname):
          shutil.copy(fname,self.opts.nx)
        else:
          tlf.close()
          tlf = open(self.opts.log,'a')
          tlf.write('\nCould not write the nexus tree file')

            
        #os.unlink(self.iname)



if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input tree file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option('-d', '--dates', default=None, help='Input date file')  
    op.add_option('-l', '--log', default=None,help='Log file')
    op.add_option('-t', '--newick', default=None,help='Dated newick tree file')
    op.add_option('-n', '--nx', default=None,help='Nexus tree file')
    op.add_option('-b', '--brl', default=None,help='Tree file with branch length')
    
#    op.add_option('-w', '--rate', default="0",help='Substitution rate')

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = lsd(opts)
    c.run()
    
            

