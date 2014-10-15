"""
rtt.py
Galaxy wrapper for rtt() from APE package
"""


import sys,optparse,os,subprocess,tempfile,shutil

class rtt:
    """
    """
    def __init__(self,opts=None):
        self.opts = opts
            

    def run(self):
        tlf = open(self.opts.log,'w')
        cl = []
        file_path = os.path.dirname(os.path.abspath(__file__)) 
        dir_path = file_path[:file_path.rfind("tools")]
        
        if os.path.isfile(self.opts.output):
          os.remove(self.opts.output)

        cl.append('%stools/phylogenetics/Ape_rtt/rtt.R %s %s "%s" %s' % (dir_path,self.opts.input,self.opts.output,self.opts.objective,self.opts.tolerance))
        
        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()
        #print(cl)



if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input tree file')
    op.add_option('-o', '--output', default=None,help='Output tree file')
    op.add_option('-j', '--objective', default='correlation', help='Objective function')  
    op.add_option('-l', '--log', default=None,help='Log file')
    op.add_option('-t', '--tolerance', default='-1',help='Tolerance')
    
#    op.add_option('-w', '--rate', default="0",help='Substitution rate')

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = rtt(opts)
    c.run()
    
            

