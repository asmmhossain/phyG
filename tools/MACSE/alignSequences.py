"""
alignSequences.py
Galaxy wrapper for aligning coding sequences by using their AA translations while
allowing frameshifts and stop codons
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

        file_path = os.path.dirname(os.path.abspath(__file__)) 
        dir_path = file_path[:file_path.rfind("tools")]
            
        cl.append('java -jar -Xmx2000m %sdependencies/macse/jar_file/macse.jar -prog alignSequences -gc_def %s -%s %s -seq %s' % (dir_path,self.opts.gc_def,self.opts.out_type,self.opts.output,self.opts.input))

        
        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()

        
        os.unlink(self.iname)



if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option('-g',"--gc_def", default=None, help="Deafault genetic code")  
    op.add_option('-l', '--log', default=None,help='Log file')
    op.add_option('-n','--out_type', default=None,help='Type of output alignment file (NT | AA)')
#    op.add_option('-f','--gc_list', default=None,help='List of genetic codes for each sequence')


       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = Test(opts)
    c.run()
    
            

