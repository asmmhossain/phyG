"""
jModelTest.py
Galaxy wrapper for nucleotide substitution model selection using jModelTest2
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
        tlf = open(self.opts.output,'w')
        cl = ['java -jar /home/mukarram/Downloads/jModelTest-2.1.4/jModelTest.jar -d %s -s %s' % (self.opts.input,self.opts.nss)]
    #    cl = ['java -jar jModelTest.jar -d %s -s %s' % (self.opts.input,self.opts.nss)]

        if self.opts.baseFreq == "1":
            cl.append('-f')
        if self.opts.prInSites == "1":
            cl.append('-i')
        if self.opts.AIC == "1":
            cl.append('-AIC')
        if self.opts.AICc == "1":
            cl.append('-AICc')
        if self.opts.BIC == "1":
            cl.append('-BIC')
        if self.opts.DT == "1":
            cl.append('-DT')
        if self.opts.paraImp == "1":
            cl.append('-p')
        if self.opts.avg == "1":
            cl.append('-v')
        if self.opts.paup == "1":
            cl.append('-w')
        if self.opts.dLRT == "1":
            cl.append('-dLRT')
        if self.opts.gammacat.isdigit():    
            if self.opts.gammacat != "0":
                cl.append('-g %d' % int(self.opts.gammacat))
        if self.opts.baseTree:
            cl.append('-t %s' % self.opts.baseTree)
        if self.opts.searchTree:
            cl.append('-S %s' % self.opts.searchTree)

        
        process = subprocess.Popen(' '.join(cl), shell=True, stderr=tlf, stdout=tlf)
        rval = process.wait()
        
        os.unlink(self.iname)
    


if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-d', '--input', default=None,help='Input file')
    op.add_option('-q', '--output', default=None,help='Output file')
    op.add_option("-f", "--baseFreq", default=None, help="Use models with unequal base frequencies")  
    op.add_option('-i', '--prInSites', default=None,help='Use models with a proportion invariable sites')
    op.add_option('--AIC', default=None,help='Calculate the Akaike Information Criterion (AIC)')
    op.add_option('--AICc', default=None,help='Calculate the corrected Akaike Information Criterion (AICc)')
    op.add_option('--BIC', default=None,help='Calculate the Bayesian Information Criterion (BIC)')
    op.add_option('--DT', default=None,help='Calculate the Decision Theory Criterion (DT)')
    op.add_option('-p', '--paraImp', default=None,help='Calculate Parameter importances')
    op.add_option('-v','--avg', default=None,help='Do model averaging and parameter importances')
    op.add_option('-w','--paup', default=None,help='Write PAUP block')
    op.add_option('--dLRT', default=None,help='Do dynamical likelihood ratio tests')
    op.add_option('-s','--nss', default=None,help='Number os substitution scheme')
    op.add_option('-g','--gammacat', default=None,help='Number of categories for rate variation among sites')
    op.add_option('-t','--baseTree', default=None,help='Base tree for likelihood calculation')
    op.add_option('-S','--searchTree', default=None,help='Tree topology search operation option')


       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = Test(opts)
    c.run()
    
            

