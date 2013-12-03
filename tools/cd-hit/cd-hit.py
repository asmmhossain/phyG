"""
cd-hit.py
Galaxy wrapper for clustering and comparing nucleotide and protein sequences
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
        thr = None
        try:
            thr = float(self.opts.sim)
        except ValueError:
            thr = 0.90

        if self.opts.seqType == "dna":
            cl.append('/home/mukarram/apps/bin/cd-hit-est')
            if thr > 1.0:
                cl.append('-c 1.0 -n 10')
            elif thr > 0.90:
                cl.append('-c %s -n 10' % thr)
            elif thr > 0.88:
                cl.append('-c %s -n 7' % thr)
            elif thr > 0.85:
                cl.append('-c %s -n 6' % thr)
            elif thr > 0.80:
                cl.append('-c %s -n 5' % thr)
            elif thr > 0.75:
                cl.append('-c %s -n 4' % thr)
            else:
                cl.append('-c %s -n 3' % thr)


        elif self.opts.seqType == "protein":
            cl.append('/home/mukarram/apps/bin/cd-hit')
            if thr > 1.0:
                cl.append('-c 1.0 -n 5')
            elif thr > 0.70:
                cl.append('-c %s -n 5' % thr)
            elif thr > 0.60:
                cl.append('-c %s -n 4' % thr)
            elif thr > 0.50:
                cl.append('-c %s -n 3' % thr)
            elif thr > 0.40:
                cl.append('-c %s -n 2' % thr)
            else:
                cl.append('-c %s -n 2' % thr)
            
        cl.append('-i %s -o %s' % (self.opts.input,self.opts.output))

        
        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()

        
        os.unlink(self.iname)



if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-t', '--seqType', default=None,help='Input sequence type')
    op.add_option('-i', '--input', default=None,help='Input file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option("-c", "--sim", default=None, help="Similarity threshold for clustering")  
#    op.add_option('-n', '--wordSize', default=None,help='Word size')
    op.add_option('-l', '--log', default=None,help='Log file')
#    op.add_option('-s', '--clstr', default=None,help='Cluster file')

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = Test(opts)
    c.run()
    
            

