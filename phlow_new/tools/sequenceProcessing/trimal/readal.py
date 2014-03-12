"""
readal.py
Galaxy wrapper for automatic conversion of alignments into different formats using ReadAl version 1.4 
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
       # tlf = open(self.opts.log,'w')
        cl = []
        file_path = os.path.dirname(os.path.abspath(__file__)) 
        dir_path = file_path[:file_path.rfind("tools")]
	   
        cl.append('%sdependencies/trimal-trimAl_1.4/source/readal -in %s -out %s -%s' % (dir_path,self.opts.input,self.opts.output,self.opts.format))
        
       # process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        process = subprocess.Popen(' '.join(cl), shell=True)

        rval = process.wait()

        
        os.unlink(self.iname)



if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option('-f','--format', default=None, help="Output format")  

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = Test(opts)
    c.run()
    
            

