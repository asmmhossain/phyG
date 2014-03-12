"""
clustalomega.py
Galaxy wrapper for Clustal Omega alignment
"""


import sys,optparse,os,subprocess,tempfile,shutil

class clustalomega:
    """
    """
    def __init__(self,opts=None):
        self.opts = opts
        self.iname = 'infile_copy'
        shutil.copy(self.opts.input,self.iname) 

    def run(self):
        tlf = open(self.opts.outlog,'w')
        cl = ['clustalo -i %s -o %s --threads=1 --force' % (self.iname,self.opts.output)]
        
        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()
        tlf.close()
        os.unlink(self.iname)
    


if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option('-l', '--outlog', default=None,help='Progress log file')

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = clustalomega(opts)
    c.run()
    
            

