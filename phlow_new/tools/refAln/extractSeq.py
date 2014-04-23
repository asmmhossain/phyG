"""
  extractSeq.py
  This program will accept user input of either genebank data or fasta formatted sequences
  For genebank data, program output will be a fasta formatted sequence file with accession numbers as headers + a JSON formatted file with all other annotation related sequence information
  For fasta formatted sequence file, the output will be a fasta formatted file with only accession numbers as headers
  
"""

import sys,optparse,os,subprocess,shutil

class extractSeq:
  """
  """
  def __init__(self,opts=None):
    self.opts = opts


  def build_json(self):
    str = '' # str will hold JSON texts to be written out in a file

    lines = [line.strip('\n') for line in open('sequence.annotation.txt','r')] # reads the annotation file for building JSON
    headers = lines[0].split('\t') # extracts the headers
    hlen = len(headers) 
    nline = len(lines)
    if nline > 2:
      str += '['
    else:
      str += '{'

    for line in lines[1: nline-1]:
      vals = line.split('\t')
      str += '\n  {'

      for i in xrange(hlen-1):
        str += '\n        ' + headers[i] + ': ' + vals[i] + ',' # features with their annotations are added to the string

      str +=  '\n        ' + headers[hlen-1] + ': ' + vals[hlen-1] + '\n  },' # last feature for a record
    
    vals = lines[nline-1].split('\t') # now dealing with the last record
    str += '\n  {'
    
    for i in xrange(hlen-1):
      str += '\n        ' + headers[i] + ': ' + vals[i] + ',' #features of the last records

    str +=  '\n        ' + headers[hlen-1] + ': ' + vals[hlen-1] + '\n  }' # last feature of the last record

    if nline > 2:
      str += '\n]' # wrapping up the JSON file
    else:
      str += '\n}'

    fileOut = open('seqAnnotate.json','w')
    fileOut.write(str) # writing the JSON string to the file 
    fileOut.close()


  def run(self):
    err = open(self.opts.log,'w')

    file_path = os.path.dirname(os.path.abspath(__file__)) 
    dir_path = file_path[:file_path.rfind("tools")]

    if self.opts.inType == 'gbc':
      cl = []
      cl.append("%s/tools/refAln/tabulateSequence.R %s %s" % (dir_path,self.opts.input,self.opts.output)) # generates two files: sequences.fas containing only sequences and sequence.annotation.txt file with other information

      process = subprocess.Popen(''.join(cl), shell=True, stderr=err, stdout=err)
      rval = process.wait()

      mk = []
      mk.append("cat sequence.annotation.txt | sed 's/NA/\"NA\"/g' > temp; mv temp sequence.annotation.txt") # replaces NA values with "NA" for easy processing in JSON

      process1 = subprocess.Popen(''.join(mk), shell=True, stderr=err, stdout=err)
      rval1 = process1.wait()
      
      c.build_json()

    if self.opts.inType == 'fasta':
      cl = []
      cl.append("%s/tools/refAln/cleannames.sh %s > tmp; mv tmp %s" % (dir_path,self.opts.input,self.opts.output))
      process = subprocess.Popen(''.join(cl), shell=True, stderr=err, stdout=err)
      rval = process.wait()



if __name__ == "__main__":
  op = optparse.OptionParser()
  op.add_option('-i','--input',default=None,help='Input sequence file')
  op.add_option('-o','--output',default=None,help='Output sequence file')
  op.add_option('-t','--inType',default='fasta',help='Input file type: genbank/fasta')
  op.add_option('-l','--log',default='log',help='Log file')

  opts, args = op.parse_args()
  assert opts.input <> None

  c = extractSeq(opts)
  c.run() 

