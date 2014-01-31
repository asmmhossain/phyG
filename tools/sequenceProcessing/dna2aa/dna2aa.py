"""
dna2aa.py
This python program takes a DNA sequnence and translates it into a amino acid sequence
"""

import sys,optparse,os,subprocess #,tempfile,shutil
from Bio import SeqIO


class dna2aa:
    """
    """
    def __init__(self,opts=None):
        self.opts = opts
        #self.iname = 'infile_copy'
        #shutil.copy(self.opts.input,self.iname) 

    def run(self):

        rhandle = open(self.opts.input,"rU")
        whandle = open(self.opts.output,"w")
        records = list(SeqIO.parse(rhandle,self.opts.format))
        rhandle.close()
        for seq_record in records:
            seq_record.seq = seq_record.seq.translate(table=self.opts.code)

        SeqIO.write(records,whandle,self.opts.format)

        whandle.close()



if __name__ == "__main__":
    op = optparse.OptionParser()
    op.add_option('-i', '--input', default=None,help='Input file')
    op.add_option('-o', '--output', default=None,help='Output file')
    op.add_option('-f','--format', default='fasta', help="Output format")  
    op.add_option('-c','--code', default='1', help="Codon table for translation")  
#    op.add_option('-l','--log', default=None, help="Error report")  

       
    opts, args = op.parse_args()
    assert opts.input <> None
    assert os.path.isfile(opts.input)
    c = dna2aa(opts)
    c.run()
    
            

