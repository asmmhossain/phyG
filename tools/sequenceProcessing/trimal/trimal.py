"""
trimal.py
Galaxy wrapper for automatic trimming of alignments using TrimAl version 1.2 
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
	   
        cl.append('%sdependencies/trimal -in %s -out %s -keepheader -htmlout %s' % (dir_path,self.opts.input,self.opts.output,self.opts.htmlout))
        
        if self.opts.method=="1":
            cl.append('-strictplus')
        if self.opts.method=="2":
            cl.append('-automated1')
        if self.opts.method=="3":
            cl.append('-strict')

        
       # process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        process = subprocess.Popen(' '.join(cl), shell=True)

        rval = process.wait()

        
        os.unlink(self.iname)



if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option('-m','--method', default=None, help="Method used for trimming")  
    op.add_option('--htmlout', default=None, help="Summary output in HTML")  

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = Test(opts)
    c.run()
    
            

