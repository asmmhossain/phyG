"""
muscle.py
Galaxy wrapper for Muscle alignment
"""


import sys,optparse,os,subprocess,tempfile,shutil

class Muscle:
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

        cl = ['%sdependencies/muscle3.8.31_i86linux32 -in %s -out %s' % (dir_path,self.iname,self.opts.output)]
        if self.opts.html:
            cl.append('-html')
        if self.opts.msf:
            cl.append('-msf')
        if self.opts.clw:
            cl.append('-clw')
        
        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()
        tlf.close()
        os.unlink(self.iname)
    


if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option('-l', '--outlog', default=None,help='Progress log file')
    op.add_option('--html', action="store_true", default=False,dest="html",help='HTML format output')
    op.add_option('--msf', action="store_true", default=False,dest="msf",help='GCG MSF format output')
    op.add_option('--clw', action="store_true", default=False,dest="clw",help='CLUSTALW format output')
#    op.add_option('-m', '--maxiters', default='16', help='Maximum number of iterations (Default 16)')
#    op.add_option('-t', '--maxhours', default=None,help='Maximum time of iterations in hours')

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = Muscle(opts)
    c.run()
    
            

