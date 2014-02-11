"""
mafft.py
Galaxy wrapper for MAFFT alignment
"""


import sys,optparse,os,subprocess,tempfile,shutil

class MAFFT:
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
        
        if self.opts.addfrag == '0':
            cl = ['%sdependencies/mafft-7.027-without-extensions/core/mafft --auto --quiet %s > %s' % (dir_path,self.iname,self.opts.output)]
        if self.opts.addfrag == '1':
            cl = ['%sdependencies/mafft-7.027-without-extensions/core/mafft --auto --quiet --addfragments %s %s > %s' % (dir_path,self.opts.fragfile,self.iname,self.opts.output)]

        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()
        tlf.close()
        os.unlink(self.iname)
    


if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option('-l', '--outlog', default='out.log',help='Progress log file')
    op.add_option('-f', '--addfrag', default='0', help='Flaf to indicate --addfragment ')
    op.add_option('-q', '--fragfile', default=None,help='Name of the fragment sequence file')

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = MAFFT(opts)
    c.run()
    
            

