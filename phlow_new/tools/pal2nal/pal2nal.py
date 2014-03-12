"""
pal2nal.py
Galaxy wrapper for PAL2NAL program to convert protein alignments
 and nucleotide sequences into a codon alignment 
"""


import sys,optparse,os,subprocess,tempfile,shutil

class pal2nal:
    """
    """
    def __init__(self,opts=None):
        self.opts = opts
        #self.iname = 'infile_copy'
        #shutil.copy(self.opts.paln,self.iname) 

    def run(self):
        tlf = open(self.opts.output,'w')
        err = open(self.opts.log,'w')
        cl = []
        file_path = os.path.dirname(os.path.abspath(__file__)) 
        dir_path = file_path[:file_path.rfind("tools")]

        cl.append('perl %sdependencies/pal2nal.v14/pal2nal.pl %s %s' % (dir_path,self.opts.paln,self.opts.nseq))
        cl.append(' -output %s -codontable %s' % (self.opts.outtype,self.opts.code))

        if self.opts.gap=='yes':
            cl.append(' -nogap')

        if self.opts.mm=='y':
            cl.append(' -nomismatch')

        process = subprocess.Popen(' '.join(cl), shell=True, stderr=err, stdout=tlf)
       #process = subprocess.Popen(' '.join(cl), shell=True)

        rval = process.wait()

        
        #os.unlink(tlf)
        #os.unlink(outFile)
    


if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-p', '--paln', default=None,help='Protein alignment file')
    op.add_option('-n', '--nseq', default=None,help='DNA sequence file')
    op.add_option('-o', '--output', default=None,help='Codon alignment file')
    op.add_option('-t','--outtype', default=None,help='Output format')
    op.add_option('-g','--gap', default=None,help='Remove gap')
    op.add_option('-c','--code', default=None,help='Codon table')
    op.add_option('-m','--mm',default=None,help='Remove mismatch codon')
    op.add_option('-l','--log',default=None,help='Log file')

       
    opts, args = op.parse_args()
    assert opts.paln <> None
    assert os.path.isfile(opts.paln)
    assert opts.nseq <> None
    assert os.path.isfile(opts.nseq)

    c = pal2nal(opts)
    c.run()
    
            

