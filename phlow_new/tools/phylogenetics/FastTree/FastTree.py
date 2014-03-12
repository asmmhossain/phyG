"""
FastTree.py
Galaxy wrapper for FastTree tree building program
"""


import sys,optparse,os,subprocess,tempfile,shutil

class FastTree:
    """
    """
    def __init__(self,opts=None):
        self.opts = opts
        self.iname = 'infile_copy'
        shutil.copy(self.opts.input,self.iname) 

    def run(self):
        tlf = open(self.opts.outlog,'w')

        file_path = os.path.dirname(os.path.abspath(__file__)) 
        dir_path = file_path[:file_path.rfind("tools")]

        if self.opts.nt == '0':
            cl = ['%sdependencies/FastTree/FastTree -quiet -nopr %s > %s' % (dir_path,self.iname,self.opts.output)]     
        if self.opts.nt == '1':
            cl = ['%sdependencies/FastTree/FastTree -quiet -nopr -nt %s > %s' % (dir_path,self.iname,self.opts.output)]     
        
        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()
        tlf.close()
        os.unlink(self.iname)
    


if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input alignment file')
    op.add_option('-o', '--output', default=None,help='Output tree file')
    op.add_option('-l', '--outlog', default='FastTree.log',help='Progress log file')
    op.add_option('-f', '--nt', default='0',help='Nucleotide alignment file')

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = FastTree(opts)
    c.run()
    
            

