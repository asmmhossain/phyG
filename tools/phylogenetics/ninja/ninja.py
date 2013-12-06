"""
cd-hit.py
Galaxy wrapper for clustering and comparing nucleotide and protein sequences
"""


import sys,optparse,os,subprocess,tempfile,shutil

class Test:
    """
    """
    def __init__(self,opts=None):
        self.opts = opts
        self.iname = 'infile_copy'
        shutil.copy(self.opts.input,self.iname) 

    def run(self):
        tlf = open(self.opts.log,'w')
        cl = []
            
        cl.append('~/apps/bin/ninja --in_type %s -i %s --out_type %s -o %s' % (self.opts.in_type,self.opts.input,self.opts.out_type,self.opts.output))

        
        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()

        
        os.unlink(self.iname)



if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option("--in_type", default=None, help="Type of input file (alignment | distance)")  
    op.add_option('-l', '--log', default=None,help='Log file')
    op.add_option('--out_type', default=None,help='Type of output file (tree | distance)')

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = Test(opts)
    c.run()
    
            

