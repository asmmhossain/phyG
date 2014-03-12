"""
newick_utils.py
Galaxy wrapper for working with phylogenetic trees using Newick Utilities version 1.6.0 
"""


import sys,optparse,os,subprocess,tempfile,shutil

class NW:
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
	   
        if self.opts.operation=='1':    # display radial tree 
            cl.append('%sdependencies/newick-utils-1.6/src/%s -sr -w 700 %s > %s' % (dir_path,self.opts.command,self.opts.input,self.opts.output))

        if self.opts.operation=='0':    # display tree 
            cl.append('%sdependencies/newick-utils-1.6/src/%s -s -w 700 %s > %s' % (dir_path,self.opts.command,self.opts.input,self.opts.output))

        if self.opts.operation=='2':    # root tree without outgroup 
            cl.append('%sdependencies/newick-utils-1.6/src/%s %s > %s' % (dir_path,self.opts.command,self.opts.input,self.opts.output))

        if self.opts.operation=='3':    # root tree using outgroup(s)
            cl.append('%sdependencies/newick-utils-1.6/src/%s %s %s > %s' % (dir_path,self.opts.command,self.opts.input,self.opts.outgrp,self.opts.output))

        if self.opts.operation=='4':    # root tree using ingroup(s)
            cl.append('%sdependencies/newick-utils-1.6/src/%s %s %s > %s' % (dir_path,self.opts.command,self.opts.input,self.opts.outgrp,self.opts.output))

        if self.opts.operation=='5':    # make unrooted tree
            cl.append('%sdependencies/newick-utils-1.6/src/%s %s > %s' % (dir_path,self.opts.command,self.opts.input,self.opts.output))

        if self.opts.operation=='6':    # extract subtrees
            cl.append('%sdependencies/newick-utils-1.6/src/%s %s %s > %s' % (dir_path,self.opts.command,self.opts.input,self.opts.outgrp,self.opts.output))
        
       # process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        process = subprocess.Popen(' '.join(cl), shell=True)

        rval = process.wait()

        
        os.unlink(self.iname)



if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input tree file')
    op.add_option('-o', '--output', default=None,help='Output tree file')
    op.add_option('-r','--operation', default='0', help="Output in radial format")  
    op.add_option('-c','--command', default='0', help="Newick Utilities program")  
    op.add_option('-g','--outgrp', default=None, help="List of outgroups")  

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = NW(opts)
    c.run()
    
            

