"""
tn93.py
Galaxy wrapper for fast calculation of pairwise distances between sequences in an alignment
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
            
        cl.append('~/apps/bin/tn93 -t %s -o %s %s' % (self.opts.threshold,self.opts.output,self.opts.input))

        
        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()

        
        os.unlink(self.iname)



if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option('-t','--threshold', default=None, help="Threshold distance for reporting")  
    op.add_option('-l', '--log', default=None,help='Log file')

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = Test(opts)
    c.run()
    
            

